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
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.style.PageStyle.WritingMode;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class PageStyleTest {
    private static final String MASTER = "<style:master-page style:name=\"test\" style:page-layout-name=\"test\">" +
            "<style:header>" + "<text:p>" + "<text:span text:style-name=\"none\">" + "</text:span>" + "</text:p>" +
            "</style:header>" + "<style:header-left style:display=\"false\"/>" + "<style:footer>" + "<text:p>" +
            "<text:span text:style-name=\"none\">" + "</text:span>" + "</text:p>" + "</style:footer>" +
            "<style:footer-left style:display=\"false\"/>" + "</style:master-page>";
    private XMLUtil util;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public final void testAlmostEmptyToAutomaticStyle() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").build();
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" + "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>" + "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style>" + "<style:footer-style>" + "<style:header-footer-properties " +
                        "fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" + "</style:footer-style>" + "</style:page-layout>",

                pageStyle);
    }

    @Test
    public final void testAlmostEmptyToMasterStyle() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
    }

    @Test
    public final void testBackground() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").backgroundColor(SimpleColor.BLANCHEDALMOND).build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" + "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" fo:background-color=\"#FFEBCD\" fo:margin=\"1.5cm\"/>"
                        + "<style:header-style>" + "<style:header-footer-properties fo:min-height=\"0cm\" " +
                        "fo:margin=\"0cm\"/></style:header-style><style:footer-style><style:header-footer-properties " +
                        "" + "" + "" + "" + "" + "" + "" + "" + "" + "fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style>" + "</style:page-layout>",
                pageStyle);
    }

    @Test(expected = IllegalStateException.class)
    public final void testEmpty() {
        PageStyle.builder(null).build();
    }

    @Test
    public final void testFooterHeader() throws IOException {
        final Header header = PowerMock.createMock(Header.class);
        final Footer footer = PowerMock.createMock(Footer.class);
        final PageStyle pageStyle = PageStyle.builder("test").header(header).footer(footer).build();
        final StringBuilder sb = new StringBuilder();

        header.appendPageSectionStyleXMLToAutomaticStyle(this.util, sb);
        footer.appendPageSectionStyleXMLToAutomaticStyle(this.util, sb);
        PowerMock.replayAll();
        pageStyle.appendXMLToAutomaticStyle(this.util, sb);
        DomTester.assertEquals(
                "<style:page-layout style:name=\"test\">" + "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>" + "</style:page-layout>",
                sb.toString());
        PowerMock.verifyAll();
    }

    @Test
    public final void testHeightAndWidth() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").pageHeight(SimpleLength.cm(20.0))
                .pageWidth(SimpleLength.cm(10.0)).build();
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" + "<style:page-layout-properties fo:page-width=\"10cm\" " +
                        "fo:page-height=\"20cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>" + "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style>" + "<style:footer-style>" + "<style:header-footer-properties " +
                        "fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" + "</style:footer-style>" + "</style:page-layout>",
                pageStyle);
    }

    @Test
    public final void testNullFooterHeader() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").header(null).footer(null).build();
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" + "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>" + "<style:header-style/>" +
                        "<style:footer-style/>" + "</style:page-layout>",
                pageStyle);
    }

    @Test
    public final void testPaperFormat() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").paperFormat(PaperFormat.A3).build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" + "<style:page-layout-properties fo:page-width=\"29.7cm\" "
                        + "fo:page-height=\"42cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" " +
                        "style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>" + "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style>" + "<style:footer-style>" + "<style:header-footer-properties " +
                        "fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" + "</style:footer-style>" + "</style:page-layout>",
                pageStyle);
    }

    @Test
    public final void testPrintOrientationH() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").printOrientationHorizontal().writingMode(WritingMode.PAGE)
                .build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" + "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" " +
                        "style:print-orientation=\"landscape\" fo:margin=\"1.5cm\"/>" + "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style>" + "<style:footer-style>" + "<style:header-footer-properties " +
                        "fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" + "</style:footer-style>" + "</style:page-layout>",
                pageStyle);
    }

    @Test
    public final void testPrintOrientationV() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").printOrientationVertical().writingMode(WritingMode.PAGE)
                .build();
        this.assertMasterXMLEquals(PageStyleTest.MASTER, pageStyle);
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" + "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" " +
                        "style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>" + "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style>" + "<style:footer-style>" + "<style:header-footer-properties " +
                        "fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" + "</style:footer-style>" + "</style:page-layout>",
                pageStyle);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWritingException() {
        PageStyle.builder("test").writingMode(null);
    }

    @Test
    public final void testWritingMode() throws IOException {
        final PageStyle pageStyle = PageStyle.builder("test").writingMode(WritingMode.PAGE).build();
        this.assertLayoutXMLEquals(
                "<style:page-layout style:name=\"test\">" + "<style:page-layout-properties fo:page-width=\"21cm\" " +
                        "fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" " +
                        "style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>" + "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style>" + "<style:footer-style>" + "<style:header-footer-properties " +
                        "fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" + "</style:footer-style>" + "</style:page-layout>",
                pageStyle);
    }

    private void assertLayoutXMLEquals(final String xml, final PageStyle pageStyle) throws IOException {
        final StringBuilder sb = new StringBuilder();
        pageStyle.appendXMLToAutomaticStyle(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());
    }

    private void assertMasterXMLEquals(final String xml, final PageStyle pageStyle) throws IOException {
        final StringBuilder sb = new StringBuilder();
        pageStyle.appendXMLToMasterStyle(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());
    }

}
