/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods;

import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.Validation;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class TableColdCellTest {
    private TableColdCell coldCell;
    private XMLUtil xmlUtil;

    @Before
    public void setUp() {
        this.xmlUtil = XMLUtil.create();
        this.coldCell = new TableColdCell(this.xmlUtil);
    }

    @Test
    public final void testCreate() throws IOException {
        this.coldCell = TableColdCell.create(this.xmlUtil);
        this.assertXMLEquals("<table:table-cell/>");
    }

    @Test
    public final void testCurrency() throws IOException {
        this.coldCell.setCurrency("€");
        Assert.assertEquals("€", this.coldCell.getCurrency());
        this.assertXMLEquals("<table:table-cell/>");
    }

    @Test
    public final void testCovered() throws IOException {
        Assert.assertFalse(this.coldCell.isCovered());
        this.coldCell.setCovered();
        Assert.assertTrue(this.coldCell.isCovered());
        this.assertXMLEquals("<table:table-cell/>");
    }

    @Test
    public final void testText() throws IOException {
        final Text t0 = Text.content("text0");
        this.coldCell.setText(t0);
        this.assertXMLEquals("<table:table-cell><text:p>text0</text:p></table:table-cell>");
    }

    @Test
    public final void testFormula() throws IOException {
        this.coldCell.setFormula("1");
        this.assertXMLEquals("<table:table-cell table:formula=\"of:=1\"/>");
    }

    @Test
    public final void testColSpan() throws IOException {
        this.coldCell.setColumnsSpanned(2);
        this.assertXMLEquals("<table:table-cell table:number-columns-spanned=\"2\"/>");
    }

    @Test
    public final void testRowSpan() throws IOException {
        this.coldCell.setRowsSpanned(8);
        this.assertXMLEquals("<table:table-cell table:number-rows-spanned=\"8\"/>");
    }

    @Test
    public final void testTooltip() throws IOException {
        this.coldCell.setTooltip("tooltip");
        this.assertXMLEquals("<table:table-cell><office:annotation>" + "<text:p>tooltip</text:p>" +
                "</office:annotation></table:table-cell>");
    }

    @Test
    public final void testTooltipObject() throws IOException {
        this.coldCell.setTooltip(Tooltip.builder(this.xmlUtil, "some text").build());
        this.assertXMLEquals(
                "<table:table-cell><office:annotation>" + "<text:p>some text</text:p>" +
                        "</office:annotation></table:table-cell>");
    }

    @Test
    public final void testTooltipWithSize() throws IOException {
        this.coldCell.setTooltip("tooltip", SimpleLength.cm(1), SimpleLength.cm(2), true);
        this.assertXMLEquals("<table:table-cell>" +
                "<office:annotation office:display=\"true\" svg:width=\"1cm\" " +
                "svg:height=\"2cm\" svg:x=\"\">" + "<text:p>tooltip</text:p>" +
                "</office:annotation>" + "</table:table-cell>");
    }

    @Test
    public final void testTooltipWithSpecialChars() throws IOException {
        this.coldCell.setTooltip("<tooltip>");
        this.assertXMLEquals(
                "<table:table-cell><office:annotation>" + "<text:p>&lt;tooltip&gt;</text:p>" +
                        "</office:annotation></table:table-cell>");
    }

    @Test
    public final void testTooltipWithCR() throws IOException {
        this.coldCell.setTooltip("tooltip\nline 1\nline2");
        this.assertXMLEquals(
                "<table:table-cell><office:annotation>" + "<text:p>tooltip</text:p><text:p>line " +
                        "1</text:p><text:p>line2</text:p>" +
                        "</office:annotation></table:table-cell>");
    }

    @Test
    public final void testAppendTextAndTooltip() throws IOException {
        this.coldCell.setText(Text.content("c"));
        this.coldCell.setTooltip("tooltip");
        this.assertXMLEquals("<table:table-cell>" + "<text:p>c</text:p>" +
                "<office:annotation><text:p>tooltip</text:p></office:annotation>" +
                "</table:table-cell>");
    }

    @Test
    public final void testMatrix() throws IOException {
        this.coldCell.setFormula("[.A1:.A10]+[.B1:.B10]");
        this.coldCell.setMatrixColumnsSpanned(1);
        this.coldCell.setMatrixRowsSpanned(10);
        this.assertXMLEquals("<table:table-cell " +
                "table:number-matrix-columns-spanned=\"1\" " +
                "table:number-matrix-rows-spanned=\"10\" " +
                "table:formula=\"of:=[.A1:.A10]+[.B1:.B10]\"/>");
    }

    @Test
    public final void testCustomAttribute() throws IOException {
        this.coldCell.setAttribute("myattr", "myvalue");
        this.coldCell.setAttribute("myattr2", "myvalue2");
        this.assertXMLEquals("<table:table-cell myattr=\"myvalue\" myattr2=\"myvalue2\"/>");
    }

    @Test
    public final void testValidation() throws IOException {
        this.coldCell.setValidation(Validation.builder("myvalid").dontAllowEmptyCells().build());
        this.assertXMLEquals("<table:table-cell table:content-validation-name=\"myvalid\"/>");
    }

    @Test
    public final void testTextCovered() throws IOException {
        final StylesContainer stylesContainer = PowerMock.createMock(StylesContainer.class);
        PowerMock.resetAll();

        EasyMock.expect(
                        stylesContainer.addContentFontFaceContainerStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true);
        PowerMock.replayAll();

        final TableCellImpl c =
                new TableCellImpl(null, this.xmlUtil, stylesContainer, null, true, null, 1);
        c.setStyle(TableCellStyle.DEFAULT_CELL_STYLE);
        c.setText(Text.content("a long\ntext"));
        c.setCovered();
        final StringBuilder sb = new StringBuilder();
        c.appendXMLToTableRow(this.xmlUtil, sb);
        PowerMock.verifyAll();

        DomTester.assertEquals("<table:covered-table-cell table:style-name=\"Default\" " +
                "office:value-type=\"string\" office:string-value=\"\">" +
                "<text:p>a long\n" +
                "text</text:p>" +
                "</table:covered-table-cell>", sb.toString());
    }

    private void assertXMLEquals(final String xml) throws IOException {
        DomTester.assertEquals(xml, this.getXML());
    }

    private String getXML() throws IOException {
        final StringBuilder sb = new StringBuilder("<table:table-cell");
        this.coldCell.appendXMLToTable(this.xmlUtil, sb);
        return sb.toString();
    }
}
