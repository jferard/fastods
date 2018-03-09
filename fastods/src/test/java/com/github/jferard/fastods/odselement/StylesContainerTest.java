/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.datastyle.BooleanStyleBuilder;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

public class StylesContainerTest {
    private static final String PS1_MASTER_XML = "<style:master-page style:name=\"a\" style:page-layout-name=\"a\">"
            + "<style:header>" + "<text:p><text:span text:style-name=\"none\"></text:span></text:p>" +
            "</style:header>" + "<style:header-left style:display=\"false\"/>" + "<style:footer><text:p><text:span "
            + "text:style-name=\"none\"></text:span></text:p>" + "</style:footer><style:footer-left " +
            "style:display=\"false\"/>" + "</style:master-page>";

    private static final String PS_LAYOUT_XML_FORMAT;
    private static final String PS1_LAYOUT_XML;
    private static final String PS2_LAYOUT_XML;

    private static final String DS_XML_FORMAT;
    private static final String DS2_XML;
    private static final String DS1_XML;

    static {
        DS_XML_FORMAT = "<number:boolean-style style:name=\"a\" number:language=\"en\" " + "number:country=\"%s\" " +
                "style:volatile=\"true\"/>";

        DS1_XML = String.format(DS_XML_FORMAT, "A");
        DS2_XML = String.format(DS_XML_FORMAT, "B");

        PS_LAYOUT_XML_FORMAT = "<style:page-layout style:name=\"a\">" + "<style:page-layout-properties " +
                "fo:page-width=\"21cm\" " + "fo:page-height=\"29.7cm\" " + "style:num-format=\"1\" " +
                "style:writing-mode=\"lr-tb\" " + "style:print-orientation=\"portrait\" " + "fo:margin=\"%s\"/>" +
                "<style:header-style>" + "<style:header-footer-properties " + "fo:min-height=\"0cm\" " +
                "fo:margin=\"0cm\"/>" + "</style:header-style>" + "<style:footer-style>" +
                "<style:header-footer-properties " + "fo:min-height=\"0cm\" " + "fo:margin=\"0cm\"/>" +
                "</style:footer-style>" + "</style:page-layout>";
        PS1_LAYOUT_XML = String.format(PS_LAYOUT_XML_FORMAT, "1pt");
        PS2_LAYOUT_XML = String.format(PS_LAYOUT_XML_FORMAT, "2pt");
    }

    private DataStyle ds1;
    private DataStyle ds2;
    private Locale locale;
    private PageStyle ps1;
    private PageStyle ps2;
    private StylesContainer stylesContainer;
    private XMLUtil util;

    @Before
    public void setUp() {
        this.stylesContainer = new StylesContainer();
        this.util = XMLUtil.create();
        this.locale = Locale.US;

        this.ds1 = new BooleanStyleBuilder("a", this.locale).country("a").build();
        this.ds2 = new BooleanStyleBuilder("a", this.locale).country("b").build();

        this.ps1 = PageStyle.builder("a").allMargins(SimpleLength.pt(1.0)).build();
        this.ps2 = PageStyle.builder("a").allMargins(SimpleLength.pt(2.0)).build();
    }

    // CONTENT
    @Test
    public final void testAddDataStyle() {
        final DataStyle dataStyle = new BooleanStyleBuilder("test", this.locale).build();
        this.stylesContainer.addDataStyle(dataStyle);
    }

    @Test
    public final void testDataStyleCreate() throws IOException {
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1, Mode.CREATE));
        this.assertWriteDataStylesXMLEquals(DS1_XML);
    }

    @Test
    public final void testDataStyleCreateThenUpdate() throws IOException {
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1, Mode.CREATE));
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds2, Mode.UPDATE)); // country: a -> b
        this.assertWriteDataStylesXMLEquals(DS2_XML);
    }

    @Test
    public final void testDataStyleCreateThenUpdateIfExists() throws IOException {
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1, Mode.CREATE));
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds2, Mode.CREATE_OR_UPDATE));
        this.assertWriteDataStylesXMLEquals(DS2_XML);
    }

    @Test
    public final void testDataStyleCreateTwice() throws IOException {
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1, Mode.CREATE));
        Assert.assertFalse(this.stylesContainer.addDataStyle(this.ds2, Mode.CREATE));
        this.assertWriteDataStylesXMLEquals(DS1_XML);
    }

    @Test
    public final void testDataStyleUpdate() throws IOException {
        Assert.assertFalse(this.stylesContainer.addDataStyle(this.ds2, Mode.UPDATE));
        this.assertWriteDataStylesXMLEquals("");
    }

    @Test
    public final void testDataStyleUpdateIfExists() throws IOException {
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds2, Mode.CREATE_OR_UPDATE));
        this.assertWriteDataStylesXMLEquals(DS2_XML);
    }

    @Test
    public final void testPageStyleCreate() throws IOException {
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1, Mode.CREATE));
        this.assertWriteMasterXMLEquals(PS1_MASTER_XML);
        this.assertWriteLayoutXMLEquals(PS1_LAYOUT_XML);
    }


    @Test
    public final void testPageStyleCreateThenUpdate() throws IOException {
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1, Mode.CREATE));
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps2, Mode.UPDATE));
        this.assertWriteLayoutXMLEquals(PS2_LAYOUT_XML);

    }

    @Test
    public final void testPageStyleCreateThenUpdateIfExists() throws IOException {
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1, Mode.CREATE));
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps2, Mode.CREATE_OR_UPDATE));
        this.assertWriteLayoutXMLEquals(PS2_LAYOUT_XML);
    }

    @Test
    public final void testPageStyleCreateTwice() throws IOException {
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1, Mode.CREATE));
        Assert.assertFalse(this.stylesContainer.addPageStyle(this.ps2, Mode.CREATE));
        this.assertWriteLayoutXMLEquals(PS1_LAYOUT_XML);
    }

    @Test
    public final void testPageStyleUpdate() throws IOException {
        Assert.assertFalse(this.stylesContainer.addPageStyle(this.ps2, Mode.UPDATE));
        this.assertWriteLayoutXMLEquals("");
    }

    @Test
    public final void testPageStyleUpdateIfExists() throws IOException {
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps2, Mode.CREATE_OR_UPDATE));
        this.assertWriteLayoutXMLEquals(PS2_LAYOUT_XML);
    }

    @Test
    public void testAddChildCellStyle() throws Exception {
        final TableCellStyle tcs = TableCellStyle.builder("tcs").build();
        final DataStyle ds = new BooleanStyleBuilder("bs", this.locale).build();

        TableCellStyle childCellStyle = this.stylesContainer.addChildCellStyle(tcs, ds);
        Assert.assertNotNull(childCellStyle);
        Assert.assertEquals("tcs-_-bs", childCellStyle.getName());
        Assert.assertEquals(ds, childCellStyle.getDataStyle());

        childCellStyle = this.stylesContainer.addChildCellStyle(tcs, ds);
        Assert.assertNotNull(childCellStyle);
        Assert.assertEquals("tcs-_-bs", childCellStyle.getName());
        Assert.assertEquals(ds, childCellStyle.getDataStyle());
    }

    private void assertWriteDataStylesXMLEquals(final String xml) throws IOException {
        final Appendable sb = new StringBuilder();
        this.stylesContainer.writeDataStyles(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());
    }

    private void assertWriteLayoutXMLEquals(final String xml) throws IOException {
        final Appendable sb = new StringBuilder();
        this.stylesContainer.writePageLayoutStyles(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());
    }

    private void assertWriteMasterXMLEquals(final String xml) throws IOException {
        final Appendable sb = new StringBuilder();
        this.stylesContainer.writeMasterPageStyles(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());
    }
}