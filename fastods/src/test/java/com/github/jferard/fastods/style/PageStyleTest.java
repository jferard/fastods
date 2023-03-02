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

import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.attribute.PageCentering;
import com.github.jferard.fastods.attribute.PageWritingMode;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.StylesContainerImpl;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class PageStyleTest {
    public static final String EMPTY_MASTER_XML =
            "<style:master-page style:name=\"test\" style:page-layout-name=\"test\">" +
                    "<style:header><text:p>" +
                    "<text:span text:style-name=\"none\">" +
                    "</text:span></text:p>" +
                    "</style:header>" +
                    "<style:header-left style:display=\"false\"/>" +
                    "<style:footer><text:p><text:span text:style-name=\"none\"></text:span>" +
                    "</text:p></style:footer><style:footer-left style:display=\"false\"/>" +
                    "</style:master-page>";
    private static final String MASTER =
            "<style:master-page style:name=\"test\" style:page-layout-name=\"test\">" +
                    "<style:header>" + "<text:p>" + "<text:span text:style-name=\"none\">" +
                    "</text:span>" + "</text:p>" + "</style:header>" +
                    "<style:header-left style:display=\"false\"/>" + "<style:footer>" + "<text:p>" +
                    "<text:span text:style-name=\"none\">" + "</text:span>" + "</text:p>" +
                    "</style:footer>" + "<style:footer-left style:display=\"false\"/>" +
                    "</style:master-page>";
    private XMLUtil util;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public final void testAlmostEmptyToAutomaticStyle() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").build();
        this.assertLayoutXMLEquals("<style:page-layout style:name=\"test\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:num-format=\"1\" " +
                        "style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" " +
                        "style:print=\"objects charts drawings zero-values\" " +
                        "fo:margin=\"1.5cm\"/>" +
                        "<style:header-style>" + "<style:header-footer-properties " +
                        "fo:min-height=\"0cm\" " + "fo:margin=\"0cm\"/>" + "</style:header-style>" +
                        "<style:footer-style>" + "<style:header-footer-properties " +
                        "fo:min-height=\"0cm\" " + "fo:margin=\"0cm\"/>" + "</style:footer-style>" +
                        "</style:page-layout>",

                pageStyle);
    }

    @Test
    public final void testAlmostEmptyToMasterStyle() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
    }

    @Test
    public final void testBackground() throws IOException {
        final PageStyle pageStyle =
                PageStyle.builder("test").backgroundColor(SimpleColor.BLANCHEDALMOND).build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
        this.assertLayoutXMLEquals("<style:page-layout style:name=\"test\">" +
                "<style:page-layout-properties fo:page-width=\"21cm\" " +
                "fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                "style:print-orientation=\"portrait\" " +
                "style:print=\"objects charts drawings zero-values\" " +
                "fo:background-color=\"#ffebcd\" " +
                "fo:margin=\"1.5cm\"/>" + "<style:header-style>" +
                "<style:header-footer-properties fo:min-height=\"0cm\" " +
                "fo:margin=\"0cm\"/></style:header-style><style:footer-style>" +
                "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:footer-style>" +
                "</style:page-layout>", pageStyle);
    }

    @Test
    public final void testEmpty() {
        Assert.assertThrows(IllegalStateException.class, () -> PageStyle.builder(null).build());
    }

    @Test
    public final void testFooterHeader() throws IOException {
        final Header header = PowerMock.createMock(Header.class);
        final Footer footer = PowerMock.createMock(Footer.class);
        final PageStyle pageStyle = PageStyle.builder("test").header(header).footer(footer).build();
        final StringBuilder sb = new StringBuilder();

        PowerMock.resetAll();
        header.appendPageSectionStyleXMLToAutomaticStyle(this.util, sb);
        footer.appendPageSectionStyleXMLToAutomaticStyle(this.util, sb);

        PowerMock.replayAll();
        pageStyle.appendXMLToAutomaticStyle(this.util, sb);

        PowerMock.verifyAll();
        DomTester.assertEquals("<style:page-layout style:name=\"test\">" +
                "<style:page-layout-properties fo:page-width=\"21cm\" " +
                "fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                "style:print-orientation=\"portrait\" " +
                "style:print=\"objects charts drawings zero-values\" " +
                "fo:margin=\"1.5cm\"/>" +
                "</style:page-layout>", sb.toString());

    }

    @Test
    public final void testHeightAndWidth() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").pageHeight(SimpleLength.cm(20.0))
                .pageWidth(SimpleLength.cm(10.0)).build();
        this.assertLayoutXMLEquals("<style:page-layout style:name=\"test\">" +
                "<style:page-layout-properties fo:page-width=\"10cm\" " +
                "fo:page-height=\"20cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                "style:print-orientation=\"portrait\" " +
                "style:print=\"objects charts drawings zero-values\" " +
                "fo:margin=\"1.5cm\"/>" +
                "<style:header-style>" +
                "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:header-style>" + "<style:footer-style>" +
                "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:footer-style>" + "</style:page-layout>", pageStyle);
    }

    @Test
    public final void testNullFooterHeader() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").header(null).footer(null).build();
        this.assertLayoutXMLEquals("<style:page-layout style:name=\"test\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:num-format=\"1\" " +
                        "style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" " +
                        "style:print=\"objects charts drawings zero-values\" " +
                        "fo:margin=\"1.5cm\"/>" +
                        "<style:header-style/>" + "<style:footer-style/>" + "</style:page-layout>",
                pageStyle);
    }

    @Test
    public final void testPaperFormat() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").paperFormat(PaperFormat.A3).build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
        this.assertLayoutXMLEquals("<style:page-layout style:name=\"test\">" +
                "<style:page-layout-properties fo:page-width=\"29.7cm\" " +
                "fo:page-height=\"42cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                "style:print-orientation=\"portrait\" " +
                "style:print=\"objects charts drawings zero-values\" " +
                "fo:margin=\"1.5cm\"/>" +
                "<style:header-style>" +
                "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:header-style>" + "<style:footer-style>" +
                "<style:header-footer-properties " + "fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:footer-style>" + "</style:page-layout>", pageStyle);
    }

    @Test
    public final void testPrintOrientationH() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").printOrientationHorizontal()
                .writingMode(PageWritingMode.PAGE).build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
        this.assertLayoutXMLEquals("<style:page-layout style:name=\"test\">" +
                "<style:page-layout-properties fo:page-width=\"29.7cm\" " +
                "fo:page-height=\"21cm\" style:num-format=\"1\" style:writing-mode=\"page\" " +
                "style:print-orientation=\"landscape\" " +
                "style:print=\"objects charts drawings zero-values\" " +
                "fo:margin=\"1.5cm\"/>" +
                "<style:header-style>" +
                "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:header-style>" + "<style:footer-style>" +
                "<style:header-footer-properties " + "fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:footer-style>" + "</style:page-layout>", pageStyle);
    }

    @Test
    public final void testPrintOrientationV() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").printOrientationVertical()
                .writingMode(PageWritingMode.PAGE).build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
        this.assertLayoutXMLEquals("<style:page-layout style:name=\"test\">" +
                "<style:page-layout-properties fo:page-width=\"21cm\" " +
                "fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" " +
                "style:print-orientation=\"portrait\" " +
                "style:print=\"objects charts drawings zero-values\" " +
                "fo:margin=\"1.5cm\"/>" +
                "<style:header-style>" +
                "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:header-style>" + "<style:footer-style>" +
                "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:footer-style>" + "</style:page-layout>", pageStyle);
    }

    @Test
    public final void testPrintMargins() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").marginBottom(SimpleLength.pt(1))
                .marginLeft(SimpleLength.pt(2)).marginRight(SimpleLength.pt(3))
                .marginTop(SimpleLength.pt(4)).build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
        this.assertLayoutXMLEquals("<style:page-layout style:name=\"test\"><style:page-layout" +
                "-properties fo:page-width=\"21cm\" fo:page-height=\"29.7cm\" " +
                "style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                "style:print-orientation=\"portrait\" " +
                "style:print=\"objects charts drawings zero-values\" " +
                "fo:margin=\"1.5cm\" fo:margin-top=\"4pt\" " +
                "fo:margin-right=\"3pt\" fo:margin-bottom=\"1pt\" " +
                "fo:margin-left=\"2pt\"/>" +
                "<style:header-style><style:header-footer-properties " +
                "fo:min-height=\"0cm\" fo:margin=\"0cm\"/></style:header-style>" +
                "<style:footer-style>" +
                "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:footer-style></style:page-layout>", pageStyle);
    }

    @Test
    public final void testDirect() throws IOException {
        final PageStyle test = PageStyle.builder("test").build();
        final PageStyle pageStyle =
                PageStyle.builder("test").pageLayoutStyle(test.getPageLayoutStyle())
                        .masterPageStyle(test.getMasterPageStyle()).build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
        this.assertLayoutXMLEquals("<style:page-layout style:name=\"test\"><style:page-layout" +
                "-properties fo:page-width=\"21cm\" fo:page-height=\"29.7cm\" " +
                "style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                "style:print-orientation=\"portrait\" " +
                "style:print=\"objects charts drawings zero-values\" " +
                "fo:margin=\"1.5cm\"/>" +
                "<style:header-style><style:header-footer-properties " +
                "fo:min-height=\"0cm\" fo:margin=\"0cm\"/></style:header-style><style:footer" +
                "-style><style:header-footer-properties fo:min-height=\"0cm\" " +
                "fo:margin=\"0cm\"/></style:footer-style></style:page-layout>", pageStyle);
    }

    @Test
    public final void testWritingException() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> PageStyle.builder("test").writingMode(null));
    }

    @Test
    public final void testWritingMode() throws IOException {
        final PageStyle pageStyle =
                PageStyle.builder("test").writingMode(PageWritingMode.PAGE).build();
        this.assertLayoutXMLEquals("<style:page-layout style:name=\"test\">" +
                "<style:page-layout-properties fo:page-width=\"21cm\" " +
                "fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" " +
                "style:print-orientation=\"portrait\" " +
                "style:print=\"objects charts drawings zero-values\" " +
                "fo:margin=\"1.5cm\"/>" +
                "<style:header-style>" +
                "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:header-style>" +
                "<style:footer-style>" +
                "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                "</style:footer-style>" + "</style:page-layout>", pageStyle);
    }

    private void assertLayoutXMLEquals(final String xml, final PageStyle pageStyle)
            throws IOException {
        final StringBuilder sb = new StringBuilder();
        pageStyle.appendXMLToAutomaticStyle(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());
    }

    private void assertMasterXMLEquals(final String xml, final PageStyle pageStyle)
            throws IOException {
        final StringBuilder sb = new StringBuilder();
        pageStyle.appendXMLToMasterStyle(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());
    }

    @Test
    public final void testAddEmbeddedStyle() {
        final PageStyle pageStyle = PageStyle.builder("test").build();
        final StylesContainer stc = PowerMock.createMock(StylesContainerImpl.class);

        PowerMock.resetAll();
        EasyMock.expect(stc.addStylesFontFaceContainerStyle(EasyMock.isA(TextStyle.class)))
                .andReturn(true).times(2);

        PowerMock.replayAll();
        pageStyle.addEmbeddedStyles(stc);

        PowerMock.verifyAll();
    }

    @Test
    public final void testHidden() {
        final PageStyle ps1 = PageStyle.builder("test").build();
        Assert.assertFalse(ps1.isHidden());
        final PageStyle ps2 = PageStyle.builder("test").hidden().build();
        Assert.assertTrue(ps2.isHidden());
    }

    @Test
    public final void testLayoutAdd() {
        final OdsElements odsElements = PowerMock.createMock(OdsElements.class);
        final PageStyle ps1 = PageStyle.builder("test").build();
        final PageLayoutStyle ls = ps1.getPageLayoutStyle();

        PowerMock.resetAll();
        EasyMock.expect(odsElements.addPageLayoutStyle(ls)).andReturn(true);

        PowerMock.replayAll();
        ls.addToElements(odsElements);

        PowerMock.verifyAll();
    }

    @Test
    public final void testLayoutWM() {
        final PageStyle ps1 = PageStyle.builder("test").build();
        final PageLayoutStyle ls = ps1.getPageLayoutStyle();

        Assert.assertEquals(PageStyle.DEFAULT_WRITING_MODE, ls.getWritingMode());
    }

    @Test
    public final void testScaleTo() throws IOException {
        final PageStyle ps = PageStyle.builder("test").scaleTo(95).build();

        this.assertMasterXMLEquals(EMPTY_MASTER_XML, ps);
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:scale-to=\"95%\" " +
                        "style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" " +
                        "style:print=\"objects charts drawings zero-values\" " +
                        "fo:margin=\"1.5cm\"/>" +
                        "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style><style:footer-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style></style:page-layout>", ps);
    }

    @Test
    public final void testScaleToPages() throws IOException {
        final PageStyle ps = PageStyle.builder("test").scaleToPages(2).build();

        this.assertMasterXMLEquals(EMPTY_MASTER_XML, ps);
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:scale-to-pages=\"2\" " +
                        "style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" " +
                        "style:print=\"objects charts drawings zero-values\" " +
                        "fo:margin=\"1.5cm\"/>" +
                        "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style><style:footer-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style></style:page-layout>", ps);
    }

    @Test
    public final void testScaleToX() throws IOException {
        final PageStyle ps = PageStyle.builder("test").scaleToX(2).build();

        this.assertMasterXMLEquals(EMPTY_MASTER_XML, ps);
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:scale-to-X=\"2\" " +
                        "loext:scale-to-X=\"2\" " +
                        "style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" " +
                        "style:print=\"objects charts drawings zero-values\" " +
                        "fo:margin=\"1.5cm\"/>" +
                        "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style><style:footer-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style></style:page-layout>", ps);
    }

    @Test
    public final void testScaleToY() throws IOException {
        final PageStyle ps = PageStyle.builder("test").scaleToY(2).build();

        this.assertMasterXMLEquals(
                EMPTY_MASTER_XML, ps);
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:scale-to-Y=\"2\" " +
                        "loext:scale-to-Y=\"2\" " +
                        "style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" " +
                        "style:print=\"objects charts drawings zero-values\" " +
                        "fo:margin=\"1.5cm\"/>" +
                        "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style><style:footer-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style></style:page-layout>", ps);
    }

    @Test
    public final void testPrintOrientation() throws IOException {
        final PageStyle ps = PageStyle.builder("test").pageHeight(SimpleLength.cm(200))
                .printOrientationHorizontal().build();

        this.assertMasterXMLEquals(
                EMPTY_MASTER_XML, ps);
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" +
                        "<style:page-layout-properties fo:page-width=\"200cm\" " +
                        "fo:page-height=\"21cm\" style:num-format=\"1\" " +
                        "style:writing-mode=\"lr-tb\" style:print-orientation=\"landscape\" " +
                        "style:print=\"objects charts drawings zero-values\" fo:margin=\"1.5cm\"/>" +
                        "<style:header-style><style:header-footer-properties fo:min-height=\"0cm\" " +
                        "fo:margin=\"0cm\"/>" +
                        "</style:header-style><style:footer-style><style:header-footer-properties " +
                        "fo:min-height=\"0cm\" fo:margin=\"0cm\"/></style:footer-style>" +
                        "</style:page-layout>", ps);
    }

    @Test
    public final void testCenteringPage() throws IOException {
        final PageStyle ps = PageStyle.builder("test").centering(PageCentering.BOTH).build();

        this.assertMasterXMLEquals(
                EMPTY_MASTER_XML, ps);
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:table-centering=\"both\" " +
                        "style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" " +
                        "style:print=\"objects charts drawings zero-values\" " +
                        "fo:margin=\"1.5cm\"/>" +
                        "<style:header-style><style:header-footer-properties fo:min-height=\"0cm\" " +
                        "fo:margin=\"0cm\"/></style:header-style><style:footer-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style></style:page-layout>", ps);
    }

    @Test
    public final void testPrintComponent() throws IOException {
        final PageStyle ps = PageStyle.builder("test")
                .printComponents(PrintComponent.OBJECTS, PrintComponent.GRID).build();

        this.assertMasterXMLEquals(
                EMPTY_MASTER_XML, ps);
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:num-format=\"1\" " +
                        "style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" " +
                        "style:print=\"objects grid\" fo:margin=\"1.5cm\"/>" +
                        "<style:header-style><style:header-footer-properties fo:min-height=\"0cm\" " +
                        "fo:margin=\"0cm\"/>" +
                        "</style:header-style><style:footer-style><style:header-footer-properties " +
                        "fo:min-height=\"0cm\" fo:margin=\"0cm\"/></style:footer-style>" +
                        "</style:page-layout>", ps);
    }
}
