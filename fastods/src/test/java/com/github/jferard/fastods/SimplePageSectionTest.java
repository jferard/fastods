/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class SimplePageSectionTest {
    private XMLUtil util;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public final void testAlmostEmptyToMasterStyle() throws IOException {
        final PageSection header = PageSection.simpleBuilder().build();
        this.assertMasterXMLEquals("", header);
    }

    @Test
    public final void testFourMarginsAndMinHeightToAutomaticStyle() throws IOException {
        final TextStyle ts = TextProperties.builder().fontStyleItalic().buildStyle("style");
        final Header header = PageSection.simpleBuilder().marginTop(SimpleLength.pt(10.0))
                .marginRight(SimpleLength.pt(11.0)).marginBottom(SimpleLength.pt(12.0))
                .marginLeft(SimpleLength.pt(13.0)).minHeight(SimpleLength.pt(120.0)).styledContent("text", ts)
                .buildHeader();
        this.assertAutomaticXMLEquals(
                "<style:header-style>" + "<style:header-footer-properties fo:min-height=\"120pt\" fo:margin=\"0cm\" "
                        + "fo:margin-top=\"10pt\" fo:margin-right=\"11pt\" fo:margin-bottom=\"12pt\" " +
                        "fo:margin-left=\"13pt\"/>" + "</style:header-style>",
                header);
    }

    @Test
    public final void testMarginsAndMinHeightToAutomaticStyle() throws IOException {
        final TextStyle ts = TextProperties.builder().fontStyleItalic().buildStyle("style");
        final Header header = PageSection.simpleBuilder().allMargins(SimpleLength.pt(10.0))
                .minHeight(SimpleLength.pt(120.0)).styledContent("text", ts).buildHeader();
        this.assertAutomaticXMLEquals(
                "<style:header-style>" + "<style:header-footer-properties fo:min-height=\"120pt\" " +
                        "fo:margin=\"10pt\"/>" + "</style:header-style>",
                header);
    }

    @Test
    public final void testPageToMasterStyle() throws IOException {
        final TextStyle ts1 = TextProperties.builder().buildStyle("test1");
        final TextStyle ts2 = TextProperties.builder().buildStyle("test2");
        final PageSection header = PageSection.simpleBuilder()
                .text(Text.builder().parStyledContent(Text.TEXT_PAGE_NUMBER, ts1)
                        .parStyledContent(Text.TEXT_PAGE_COUNT, ts2).build()).build();
        this.assertMasterXMLEquals(
                "<text:p>" + "<text:span text:style-name=\"test1\">" + "<text:page-number>1</text:page-number>" +
                        "</text:span>" + "</text:p>" + "<text:p>" + "<text:span text:style-name=\"test2\">" +
                        "<text:page-count>99</text:page-count>" + "</text:span>" + "</text:p>",
                header);
    }

    @Test
    public final void testPageToMasterStyle2() throws IOException {
        final TextStyle ts1 = TextProperties.builder().buildStyle("test1");
        final TextStyle ts2 = TextProperties.builder().buildStyle("test2");
        final PageSection header = PageSection.simpleBuilder()
                .text(Text.builder().par().styledSpan(Text.TEXT_PAGE_NUMBER, ts1).styledSpan(Text.TEXT_PAGE_COUNT, ts2)
                        .build()).build();
        this.assertMasterXMLEquals(
                "<text:p>" + "<text:span text:style-name=\"test1\">" + "<text:page-number>1</text:page-number>" +
                        "</text:span>" + "<text:span text:style-name=\"test2\">" +
                        "<text:page-count>99</text:page-count>" + "</text:span>" + "</text:p>",
                header);
    }

    @Test
    public final void testSimpleFooterToAutomaticStyle() throws IOException {
        final TextStyle ts = TextProperties.builder().fontStyleItalic().buildStyle("style");
        final Header header = PageSection.simpleHeader("text", ts);
        this.assertAutomaticXMLEquals(
                "<style:header-style>" + "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
                        + "</style:header-style>",
                header);
    }

    @Test
    public final void testSimpleFooterToMasterStyle() throws IOException {
        final TextStyle ts = TextProperties.builder().fontStyleItalic().buildStyle("style");
        final Header header = PageSection.simpleHeader("text", ts);
        this.assertMasterXMLEquals(
                "<text:p>" + "<text:span text:style-name=\"style\">" + "text" + "</text:span>" + "</text:p>", header);
    }

    @Test
    public final void testSimpleStyledTextToAutomaticStyle() throws IOException {
        final TextStyle ts = TextProperties.builder().buildStyle("test");
        final String text = "text";
        final Footer footer = PageSection.simpleBuilder().styledContent(text, ts).buildFooter();
        this.assertAutomaticXMLEquals(
                "<style:footer-style>" + "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
                        + "</style:footer-style>",
                footer);
    }

    @Test
    public final void testSimpleStyledTextToMasterStyle() throws IOException {
        final TextStyle ts = TextProperties.builder().buildStyle("test");
        final PageSection footer = PageSection.simpleBuilder().styledContent("text", ts).build();
        this.assertMasterXMLEquals("<text:p>" + "<text:span text:style-name=\"test\">text</text:span>" + "</text:p>",
                footer);
    }

    @Test
    public final void testAddEmbbeded() throws IOException {
        final StylesContainer sc = PowerMock.createMock(StylesContainer.class);
        PowerMock.resetAll();

        PowerMock.replayAll();

        final PageSection footer = PageSection.simpleBuilder().content("text").build();
        footer.addEmbeddedStyles(sc);
        footer.addEmbeddedStyles(sc);
        PowerMock.verifyAll();
    }

    private void assertAutomaticXMLEquals(final String xml, final HeaderOrFooter header) throws IOException {
        final StringBuilder sb = new StringBuilder();
        header.appendPageSectionStyleXMLToAutomaticStyle(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());
    }

    private void assertMasterXMLEquals(final String xml, final HeaderOrFooter section) throws IOException {
        final StringBuilder sb = new StringBuilder();
        section.appendXMLToMasterStyle(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());
    }

    private void assertMasterXMLEquals(final String xml, final PageSection section) throws IOException {
        final StringBuilder sb = new StringBuilder();
        section.appendXMLToMasterStyle(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());
    }
}
