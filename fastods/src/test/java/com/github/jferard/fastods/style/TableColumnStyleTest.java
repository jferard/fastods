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
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.TableColumnImpl;
import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class TableColumnStyleTest {
    public static final String EMPTY_XML =
            "<style:style style:name=\"test\" style:family=\"table-column\">" +
                    "<style:table-column-properties " + "fo:break-before=\"auto\" " +
                    "style:column-width=\"2.5cm\"/>" + "</style:style>";
    private XMLUtil util;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public final void testAddEmptyToFile() {
        final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
        final OdsElements odsElements = PowerMock.createMock(OdsElements.class);

        PowerMock.resetAll();
        EasyMock.expect(odsElements.addContentStyle(tcs)).andReturn(true);

        PowerMock.replayAll();
        tcs.addToElements(odsElements);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddNewDefaultCellStyleToFile() {
        final TableCellStyle cellStyle = TableCellStyle.builder("ok").hidden().build();
        final TableColumnStyle tcs =
                TableColumnStyle.builder("test").build();
        final OdsElements odsElements = PowerMock.createMock(OdsElements.class);

        PowerMock.resetAll();
        EasyMock.expect(odsElements.addContentStyle(tcs)).andReturn(true);

        PowerMock.replayAll();
        tcs.addToElements(odsElements);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDefaultCellStyle() throws IOException {
        final TableCellStyle cs = TableCellStyle.builder("t").build();
        final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
        final TableColumnImpl tc = new TableColumnImpl();
        tc.setColumnStyle(tcs);
        tc.setColumnDefaultCellStyle(cs);
        this.assertXMLTableEquals("<table:table-column table:style-name=\"test\" " +
                "table:default-cell-style-name=\"t\"/>", tc, -1);
    }

    @Test
    public final void testEmpty() throws IOException {
        final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
        TestHelper.assertXMLEquals(EMPTY_XML, tcs);
        Assert.assertTrue(tcs.isHidden());
        final TableColumnImpl tc = new TableColumnImpl();
        tc.setColumnStyle(tcs);
        this.assertXMLTableEquals("<table:table-column table:style-name=\"test\" " +
                "table:default-cell-style-name=\"Default\"/>", tc, -1);
    }

    @Test
    public final void testVisible() throws IOException {
        final TableColumnStyle tcs = TableColumnStyle.builder("test").visible().build();
        TestHelper.assertXMLEquals(EMPTY_XML, tcs);
        Assert.assertFalse(tcs.isHidden());
    }


    @Test
    public final void testColumnWidth() {
        final TableColumnStyle tcs = TableColumnStyle.builder("test").columnWidth(SimpleLength.cm(10)).build();
        Assert.assertEquals(SimpleLength.cm(10), tcs.getColumnWidth());
    }

    @Test
    public final void testEmpty2() throws IOException {
        final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
        final TableColumnImpl tc = new TableColumnImpl();
        tc.setColumnStyle(tcs);
        this.assertXMLTableEquals("<table:table-column table:style-name=\"test\" " +
                "table:number-columns-repeated=\"2\" " +
                "table:default-cell-style-name=\"Default\"/>", tc, 2);
    }

    @Test
    @SuppressWarnings("deprecated")
    public final void testWidth() throws IOException {
        final TableColumnStyle tcs =
                TableColumnStyle.builder("test").columnWidth(SimpleLength.pt(1.0)).build();
        TestHelper.assertXMLEquals(
                "<style:style style:name=\"test\" style:family=\"table-column\">" +
                        "<style:table-column-properties " + "fo:break-before=\"auto\" " +
                        "style:column-width=\"1pt\"/>" + "</style:style>", tcs);
        Assert.assertEquals(tcs, tcs);
        Assert.assertEquals(tcs.hashCode(), tcs.hashCode());
    }

    private void assertXMLTableEquals(final String xml, final TableColumnImpl tc, final int count)
            throws IOException {
        final StringBuilder sbt = new StringBuilder();
        tc.appendXMLToTable(this.util, sbt, count);
        DomTester.assertEquals(xml, sbt.toString());
    }

    @Test
    public final void testGetters() {
        StyleTestHelper.testGettersHidden(TableColumnStyle.builder("test"));
    }

    @Test
    public final void testGetNoFontFace() {
        final TableColumnStyle test = TableColumnStyle.builder("test").build();
        // Assert.assertEquals(new FontFace(LOFonts.LIBERATION_SANS), test.getFontFace());
    }

    @Test
    public final void testGetFontFace() {
        final TableCellStyle tcs =
                TableCellStyle.builder("tcs").fontName(LOFonts.OPENSYMBOL).build();
        final TableColumnStyle test =
                TableColumnStyle.builder("test").build(); // .defaultCellStyle(tcs)
//        Assert.assertEquals(new FontFace(LOFonts.OPENSYMBOL), test.getFontFace());
    }

    @Test
    public final void testOptimalWidth() throws IOException {
        final TableColumnStyle test = TableColumnStyle.builder("test").optimalWidth().build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" " +
                "style:family=\"table-column\"><style:table-column-properties " +
                "fo:break-before=\"auto\" " +
                "style:use-optimal-column-width=\"true\"/></style:style>", test);
    }

    @Test
    public final void testWidth4cm() throws IOException {
        final TableCellStyle tableCellStyle =
                TableCellStyle.builder("c1").fontName(LOFonts.LIBERATION_MONO)
                        .fontSize(SimpleLength.pt(10)).build();
        final TableColumnStyle test =
                TableColumnStyle.builder("test") // .defaultCellStyle(tableCellStyle)
                        .columnWidth(SimpleLength.cm(4)).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" " +
                "style:family=\"table-column\"><style:table-column-properties " +
                "fo:break-before=\"auto\" style:column-width=\"4cm\"/></style:style>", test);
    }
}
