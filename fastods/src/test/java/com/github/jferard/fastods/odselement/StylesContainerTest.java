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
package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.PageSection;
import com.github.jferard.fastods.TableCellImpl;
import com.github.jferard.fastods.TableRowImpl;
import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.datastyle.BooleanStyle;
import com.github.jferard.fastods.datastyle.BooleanStyleBuilder;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.datastyle.FloatStyle;
import com.github.jferard.fastods.datastyle.FloatStyleBuilder;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.PageStyleBuilder;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.IntegerRepresentationCache;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StylesContainerTest {
    private static final String PS1_MASTER_XML =
            "<style:master-page style:name=\"a\" style:page-layout-name=\"a\">" + "<style:header>" +
                    "<text:p><text:span text:style-name=\"none\"></text:span></text:p>" +
                    "</style:header>" + "<style:header-left style:display=\"false\"/>" +
                    "<style:footer><text:p><text:span " +
                    "text:style-name=\"none\"></text:span></text:p>" +
                    "</style:footer><style:footer-left " + "style:display=\"false\"/>" +
                    "</style:master-page>";

    private static final String PS_LAYOUT_XML_FORMAT;
    private static final String PS1_LAYOUT_XML;
    private static final String PS2_LAYOUT_XML;

    private static final String DS_XML_FORMAT;
    private static final String DS2_XML;
    private static final String DS1_XML;

    static {
        DS_XML_FORMAT = "<number:boolean-style style:name=\"a\" number:language=\"en\" " +
                "number:country=\"%s\" " + "style:volatile=\"true\"/>";

        DS1_XML = String.format(DS_XML_FORMAT, "A");
        DS2_XML = String.format(DS_XML_FORMAT, "B");

        PS_LAYOUT_XML_FORMAT =
                "<style:page-layout style:name=\"a\">" +
                        "<style:page-layout-properties " +
                        "fo:page-width=\"21cm\" fo:page-height=\"29.7cm\" " +
                        "style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" " +
                        "style:print=\"objects charts drawings zero-values\" fo:margin=\"%s\"/>" +
                        "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" " +
                        "fo:margin=\"0cm\"/>" +
                        "</style:header-style>" +
                        "<style:footer-style>" +
                        "<style:header-footer-properties " +
                        "fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style>" +
                        "</style:page-layout>";
        PS1_LAYOUT_XML = String.format(PS_LAYOUT_XML_FORMAT, "1pt");
        PS2_LAYOUT_XML = String.format(PS_LAYOUT_XML_FORMAT, "2pt");
    }

    private DataStyle ds1;
    private DataStyle ds2;
    private Locale locale;
    private PageStyle ps1;
    private PageStyle ps2;
    private StylesContainerImpl stylesContainer;
    private XMLUtil util;
    private Logger logger;

    @Before
    public void setUp() {
        this.logger = PowerMock.createMock(Logger.class);
        this.stylesContainer = new StylesContainerImpl(this.logger);
        this.util = XMLUtil.create();
        this.locale = Locale.US;

        this.ds1 = new BooleanStyleBuilder("a", this.locale).country("a").build();
        this.ds2 = new BooleanStyleBuilder("a", this.locale).country("b").build();

        this.ps1 = PageStyle.builder("a").allMargins(SimpleLength.pt(1.0)).build();
        this.ps2 = PageStyle.builder("a").allMargins(SimpleLength.pt(2.0)).build();
    }

    @Test
    public final void testAddContentFontFaceContainerStyle() throws IOException {
        final StringBuilder sb1 = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        final XMLUtil util = XMLUtil.create();
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.stylesContainer.addContentFontFaceContainerStyle(
                TableCellStyle.builder("tcs").fontName("font").build());
        this.stylesContainer.writeFontFaceDecls(util, sb1);
        this.stylesContainer.writeStylesCommonStyles(util, sb2);

        PowerMock.verifyAll();
        Assert.assertEquals("<office:font-face-decls>" +
                "<style:font-face style:name=\"Liberation Sans\" svg:font-family=\"Liberation Sans\"/>" +
                "<style:font-face style:name=\"font\" svg:font-family=\"font\"/>" +
                "</office:font-face-decls>", sb1.toString());
        Assert.assertEquals("<style:style style:name=\"tcs\" " +
                "style:family=\"table-cell\" style:parent-style-name=\"Default\">" +
                "<style:text-properties style:font-name=\"font\"/>" +
                "</style:style>", sb2.toString());
    }

    @Test
    public final void testAddStylesFontFaceContainerStyle() throws IOException {
        final StringBuilder sb1 = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        final XMLUtil util = XMLUtil.create();
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.stylesContainer.addStylesFontFaceContainerStyle(
                TableCellStyle.builder("tcs").fontName("font").build());
        this.stylesContainer.writeFontFaceDecls(util, sb1);
        this.stylesContainer.writeStylesCommonStyles(util, sb2);

        PowerMock.verifyAll();
        Assert.assertEquals("<office:font-face-decls>" +
                "<style:font-face style:name=\"Liberation Sans\" svg:font-family=\"Liberation Sans\"/>" +
                "<style:font-face style:name=\"font\" svg:font-family=\"font\"/>" +
                "</office:font-face-decls>", sb1.toString());
        Assert.assertEquals("<style:style style:name=\"tcs\" " +
                "style:family=\"table-cell\" style:parent-style-name=\"Default\">" +
                "<style:text-properties style:font-name=\"font\"/>" +
                "</style:style>", sb2.toString());
    }

    @Test
    public final void testAddNullDataStyleFromCellStyle() throws IOException {
        final StringBuilder sb1 = new StringBuilder();
        final XMLUtil util = XMLUtil.create();
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.stylesContainer.addNewDataStyleFromCellStyle(TableCellStyle.DEFAULT_CELL_STYLE);
        this.stylesContainer.writeStylesCommonStyles(util, sb1);

        PowerMock.verifyAll();
        Assert.assertEquals("", sb1.toString());
    }

    @Test
    public final void testAddNewDataStyleFromCellStyle() throws IOException {
        final StringBuilder sb1 = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        final XMLUtil util = XMLUtil.create();
        final BooleanStyle booleanStyle =
                new BooleanStyleBuilder("bs", Locale.US).country("fr").build();
        final TableCellStyle cellStyle = TableCellStyle.builder("tcs").dataStyle(
                booleanStyle).build();
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.stylesContainer.addNewDataStyleFromCellStyle(cellStyle);
        this.stylesContainer.writeStylesCommonStyles(util, sb1);
        this.stylesContainer.writeContentAutomaticStyles(util, sb2);

        PowerMock.verifyAll();
        Assert.assertEquals(
                "<style:style style:name=\"tcs\" style:family=\"table-cell\" style:parent-style-name=\"Default\" style:data-style-name=\"bs\"/>",
                sb1.toString());
        Assert.assertEquals("", sb2.toString());
    }

    @Test
    public final void testDebug() {
        PowerMock.resetAll();
        this.logger.log(EasyMock.eq(Level.SEVERE),
                EasyMock.eq("MultiContainer put({0}, {1}) in {2}"),
                EasyMock.isA(Object[].class));

        PowerMock.replayAll();
        this.stylesContainer.debug();
        this.stylesContainer.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDebug2() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.stylesContainer.setObjectStyleMode(Mode.UPDATE);
        this.stylesContainer.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public final void testFreeze() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.stylesContainer.freeze();
        Assert.assertThrows(IllegalStateException.class,
                () -> this.stylesContainer.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE));

        PowerMock.verifyAll();
    }

    // CONTENT
    @Test
    public final void testAddDataStyle() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final DataStyle dataStyle = new BooleanStyleBuilder("test", this.locale).build();
        this.stylesContainer.addDataStyle(dataStyle);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyleCreate() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1));
        this.assertWriteDataStylesXMLEquals(DS1_XML);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyleCreateThenUpdate() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1));
        this.stylesContainer.setDataStylesMode(Mode.UPDATE);
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds2)); // country: a -> b
        this.assertWriteDataStylesXMLEquals(DS2_XML);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyleCreateThenUpdateIfExists() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1));
        this.stylesContainer.setDataStylesMode(Mode.CREATE_OR_UPDATE);
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds2));
        this.assertWriteDataStylesXMLEquals(DS2_XML);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyleCreateTwice() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1));
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds2));
        this.assertWriteDataStylesXMLEquals(DS2_XML);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyleUpdate() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.stylesContainer.setDataStylesMode(Mode.UPDATE);
        Assert.assertFalse(this.stylesContainer.addDataStyle(this.ds2));
        this.assertWriteDataStylesXMLEquals("");

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyleUpdateIfExists() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.stylesContainer.setDataStylesMode(Mode.CREATE_OR_UPDATE);
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds2));
        this.assertWriteDataStylesXMLEquals(DS2_XML);

        PowerMock.verifyAll();
    }

    @Test
    public final void testPageStyleCreate() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1));
        this.assertWriteMasterXMLEquals(PS1_MASTER_XML);
        this.assertWriteLayoutXMLEquals(PS1_LAYOUT_XML);

        PowerMock.verifyAll();
    }


    @Test
    public final void testPageStyleCreateThenUpdate() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1));
        this.stylesContainer.setPageStyleMode(Mode.UPDATE);
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps2));
        this.assertWriteLayoutXMLEquals(PS2_LAYOUT_XML);

        PowerMock.verifyAll();
    }

    @Test
    public final void testPageStyleCreateThenUpdateIfExists() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1));
        this.stylesContainer.setPageStyleMode(Mode.CREATE_OR_UPDATE);
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps2));
        this.assertWriteLayoutXMLEquals(PS2_LAYOUT_XML);

        PowerMock.verifyAll();
    }

    @Test
    public final void testPageStyleCreateTwice() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1));
        Assert.assertFalse(this.stylesContainer.addPageStyle(this.ps2));
        this.assertWriteLayoutXMLEquals(PS1_LAYOUT_XML);

        PowerMock.verifyAll();
    }

    @Test
    public final void testPageStyleUpdate() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.stylesContainer.setPageStyleMode(Mode.UPDATE);
        Assert.assertFalse(this.stylesContainer.addPageStyle(this.ps2));
        this.assertWriteLayoutXMLEquals("");

        PowerMock.verifyAll();
    }

    @Test
    public final void testPageStyleUpdateIfExists() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.stylesContainer.setPageStyleMode(Mode.CREATE_OR_UPDATE);
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps2));
        this.assertWriteLayoutXMLEquals(PS2_LAYOUT_XML);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddChildCellStyle() {
        PowerMock.resetAll();

        PowerMock.replayAll();
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

        PowerMock.verifyAll();
    }

    @Test
    public void testWriteVisibleDataStyles() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final DataStyle ds = new BooleanStyleBuilder("bs", this.locale).visible().build();

        this.stylesContainer.addDataStyle(ds);

        final StringBuilder sb = new StringBuilder();
        this.stylesContainer.writeVisibleDataStyles(this.util, sb);
        DomTester.assertEquals("<number:boolean-style style:name=\"bs\" number:language=\"en\" " +
                "number:country=\"US\" style:volatile=\"true\"/>", sb.toString());

        PowerMock.verifyAll();
    }

    @Test
    public void testStylesCommonStyles() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final TableCellStyle tcs = TableCellStyle.builder("tcs").build();

        this.stylesContainer.addStylesStyle(tcs);

        final StringBuilder sb = new StringBuilder();
        this.stylesContainer.writeStylesCommonStyles(this.util, sb);
        DomTester.assertEquals("<style:style style:name=\"tcs\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\"/>", sb.toString());

        PowerMock.verifyAll();
    }

    @Test
    public void testAddChildCellStyleNoParent() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final TableCellStyle tcs = TableCellStyle.DEFAULT_CELL_STYLE;
        final DataStyle ds = new BooleanStyleBuilder("bs", this.locale).build();

        TableCellStyle childCellStyle = this.stylesContainer.addChildCellStyle(tcs, ds);
        Assert.assertNotNull(childCellStyle);
        Assert.assertEquals(ds, childCellStyle.getDataStyle());
        Assert.assertEquals("Default-_-bs", childCellStyle.getName());

        childCellStyle = this.stylesContainer.addChildCellStyle(tcs, ds);
        Assert.assertNotNull(childCellStyle);
        Assert.assertEquals("Default-_-bs", childCellStyle.getName());
        Assert.assertEquals("Default-_-bs", childCellStyle.getName());
        Assert.assertEquals(ds, childCellStyle.getDataStyle());

        PowerMock.verifyAll();
    }

    @Test
    public void testHasFooterHeader() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.stylesContainer.addMasterPageStyle(new MasterPageStyle("ms1", "ms1", null, null));
        final HasFooterHeader ret1 = this.stylesContainer.hasFooterHeader();
        this.stylesContainer.addMasterPageStyle(new MasterPageStyle("ms2", "ms2", new Header(
                PageSection.simpleBuilder().build()), null));
        final HasFooterHeader ret2 = this.stylesContainer.hasFooterHeader();
        this.stylesContainer.addMasterPageStyle(new MasterPageStyle("ms3", "ms3", null,
                new Footer(PageSection.simpleBuilder().build())));
        final HasFooterHeader ret3 = this.stylesContainer.hasFooterHeader();
        PowerMock.verifyAll();

        Assert.assertFalse(ret1.hasHeader());
        Assert.assertFalse(ret1.hasFooter());
        Assert.assertTrue(ret2.hasHeader());
        Assert.assertFalse(ret2.hasFooter());
        Assert.assertTrue(ret3.hasHeader());
        Assert.assertTrue(ret3.hasFooter());
    }

    private void assertWriteDataStylesXMLEquals(final String xml) throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final Appendable sb = new StringBuilder();
        this.stylesContainer.writeHiddenDataStyles(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());

        PowerMock.verifyAll();
    }

    private void assertWriteLayoutXMLEquals(final String xml) throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final Appendable sb = new StringBuilder();
        this.stylesContainer.writePageLayoutStyles(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());

        PowerMock.verifyAll();
    }

    private void assertWriteMasterXMLEquals(final String xml) throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final Appendable sb = new StringBuilder();
        this.stylesContainer.writeMasterPageStyles(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());

        PowerMock.verifyAll();
    }

    @Test
    public void testSetDataStyle() throws IOException {
        // see 2., https://github.com/jferard/fastods/issues/247.

        // arrange
        final Locale locale = Locale.US;
        final FloatStyle dsf = new FloatStyleBuilder("dsf", locale).decimalPlaces(0)
                .groupThousands(true).build();
        final TableCellStyle tcs1 = TableCellStyle.builder("tcs1")
                .backgroundColor(SimpleColor.ALICEBLUE).build();
        final TableCellStyle tcs2 = TableCellStyle.builder("tcs2")
                .fontSize(SimpleLength.pt(14)).hidden().build();

        final TableRowImpl row = PowerMock.createMock(TableRowImpl.class);
        final TableCellImpl cell1 = new TableCellImpl(
                IntegerRepresentationCache.create(), XMLUtil.create(), this.stylesContainer,
                DataStylesBuilder.create(locale).build(), false, row,
                1);
        final TableCellImpl cell2 = new TableCellImpl(
                IntegerRepresentationCache.create(), XMLUtil.create(), this.stylesContainer,
                DataStylesBuilder.create(locale).build(), false, row,
                1);
        PowerMock.resetAll();

        // act
        PowerMock.replayAll();
        cell1.setStyle(tcs1);
        cell1.setDataStyle(dsf);

        cell2.setStyle(tcs2);
        cell2.setDataStyle(dsf);

        // assert
        PowerMock.verifyAll();
        // automatic styles
        final StringBuilder sb1 = new StringBuilder();
        this.stylesContainer.writeContentAutomaticStyles(XMLUtil.create(), sb1);
        DomTester.assertUnsortedEquals(
                "<style:style style:name=\"tcs2\" style:family=\"table-cell\" " +
                        "style:parent-style-name=\"Default\">" +
                        "<style:text-properties fo:font-size=\"14pt\" " +
                        "style:font-size-asian=\"14pt\" " +
                        "style:font-size-complex=\"14pt\"/>" +
                        "</style:style>" +

                        "<style:style style:name=\"tcs1-_-dsf\" style:family=\"table-cell\" " +
                        "style:parent-style-name=\"tcs1\" style:data-style-name=\"dsf\">" +
                        "<style:paragraph-properties fo:text-align=\"end\"/>" +

                        "</style:style><style:style style:name=\"tcs2-_-dsf\" " +
                        "style:family=\"table-cell\" style:parent-style-name=\"Default\" " +
                        "style:data-style-name=\"dsf\"><style:paragraph-properties " +
                        "fo:text-align=\"end\"/>" +
                        "<style:text-properties fo:font-size=\"14pt\" " +
                        "style:font-size-asian=\"14pt\" style:font-size-complex=\"14pt\"/>" +
                        "</style:style>",
                sb1.toString());

        // visible styles
        final StringBuilder sb2 = new StringBuilder();
        this.stylesContainer.writeStylesCommonStyles(XMLUtil.create(), sb2);
        DomTester.assertUnsortedEquals(
                "<style:style style:name=\"tcs1\" style:family=\"table-cell\" " +
                        "style:parent-style-name=\"Default\">" +
                        "<style:table-cell-properties fo:background-color=\"#f0f8ff\"/>" +
                        "</style:style>",
                sb2.toString());
    }
}