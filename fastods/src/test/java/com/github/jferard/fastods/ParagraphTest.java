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

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ParagraphTest {
    private XMLUtil util;
    private ParagraphBuilder parBuilder;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
        this.parBuilder = Paragraph.builder();
    }

    @Test
    public final void testNoSpan() throws IOException {
        this.assertParXMLEquals("<text:p/>");
    }

    @Test
    public final void testSpans() throws IOException {
        this.parBuilder.span("content");
        this.parBuilder.span("text");
        this.assertParXMLEquals("<text:p>contenttext</text:p>");
    }

    @Test
    public final void testWithStyle() throws IOException {
        final TextStyle ts =
                TextStyle.builder("style").visible().fontStyleNormal().fontWeightNormal().build();
        this.parBuilder.style(ts).span("text");
        this.assertParXMLEquals("<text:p text:style-name=\"style\">text</text:p>");
    }

    @Test
    public final void testLinks() throws IOException, URISyntaxException {
        final Table table = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("tableName");

        PowerMock.replayAll();
        this.parBuilder.link("to_ref", "#ref").link("to_file", new File("f"))
                .link("to_url", new URL("http://a")).link("to_uri", new URI("protocol:a"))
                .link("to_table", table);
        this.assertParXMLEquals(
                "<text:p>" + "<text:a xlink:href=\"#ref\" xlink:type=\"simple\">to_ref</text:a>" +
                        "<text:a " + "xlink:href=\"" + this.getURLStart() +
                        "f\" xlink:type=\"simple\">to_file</text:a>" + "<text:a " +
                        "xlink:href=\"http://a\" xlink:type=\"simple\">to_url</text:a>" +
                        "<text:a " +
                        "xlink:href=\"protocol:a\" xlink:type=\"simple\">to_uri</text:a>" +
                        "<text:a " +
                        "xlink:href=\"#tableName\" xlink:type=\"simple\">to_table</text:a>" +
                        "</text:p>");

        PowerMock.verifyAll();
    }

    @Test
    public final void testStyledLinks() throws IOException, URISyntaxException {
        final TextStyle ts =
                TextStyle.builder("style").visible().fontStyleNormal().fontWeightNormal().build();
        final Table table = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("tableName");

        PowerMock.replayAll();
        this.parBuilder.styledLink("to_ref", ts, "#ref").styledLink("to_file", ts, new File("f"))
                .styledLink("to_url", ts, new URL("http://a"))
                .styledLink("to_uri", ts, new URI("protocol:a")).styledLink("to_table", ts, table);
        this.assertParXMLEquals(
                "<text:p>" + "<text:a text:style-name=\"style\" xlink:href=\"#ref\" " +
                        "xlink:type=\"simple\">to_ref</text:a>" +
                        "<text:a text:style-name=\"style\" xlink:href=\"" + this.getURLStart() +
                        "f\" xlink:type=\"simple\">to_file</text:a>" + "<text:a " +
                        "text:style-name=\"style\" xlink:href=\"http://a\" " +
                        "xlink:type=\"simple\">to_url</text:a>" + "<text:a " +
                        "text:style-name=\"style\" xlink:href=\"protocol:a\" " +
                        "xlink:type=\"simple\">to_uri</text:a>" +
                        "<text:a text:style-name=\"style\" xlink:href=\"#tableName\" " +
                        "xlink:type=\"simple\">to_table</text:a>" + "</text:p>");

        PowerMock.verifyAll();
    }

    @Test
    public void testEquals() throws URISyntaxException {
        final Paragraph paragraph1 = Paragraph.builder().span("text").build();
        final Paragraph paragraph2 = Paragraph.builder().span("text").build();
        final Paragraph paragraph3 = Paragraph.builder().link("link", new URI("a")).build();
        Assert.assertEquals(paragraph1, paragraph1);
        Assert.assertEquals(paragraph1, paragraph2);
        Assert.assertNotEquals(paragraph1, paragraph3);
        Assert.assertNotEquals(paragraph1, new Object());
    }

    @Test
    public void testAddStylesFromCell() {
        final StylesContainer stylesContainer = PowerMock.createMock(StylesContainer.class);

        PowerMock.resetAll();
        EasyMock.expect(stylesContainer.addContentFontFaceContainerStyle(TextStyle.DEFAULT_TEXT_STYLE)).andReturn(true);

        PowerMock.replayAll();
        final Paragraph paragraph1 = Paragraph.builder().style(TextStyle.DEFAULT_TEXT_STYLE).span("text").build();
        paragraph1.addEmbeddedStylesFromCell(stylesContainer);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddStylesFromFooterHeader() {
        final StylesContainer stylesContainer = PowerMock.createMock(StylesContainer.class);

        PowerMock.resetAll();
        EasyMock.expect(stylesContainer.addStylesFontFaceContainerStyle(TextStyle.DEFAULT_TEXT_STYLE)).andReturn(true);

        PowerMock.replayAll();
        final Paragraph paragraph1 = Paragraph.builder().style(TextStyle.DEFAULT_TEXT_STYLE).span("text").build();
        paragraph1.addEmbeddedStylesFromFooterHeader(stylesContainer);

        PowerMock.verifyAll();
    }

    @Test
    public void testElement() throws IOException {
        final Paragraph paragraph = Paragraph.builder().element(Text.content("text")).build();

        TestHelper.assertXMLEquals("<text:p><text:p>text</text:p></text:p>", paragraph);
    }

    private String getURLStart() {
        final String p = new File(".").toURI().toString();
        return p.substring(0, p.length() - 2);
    }

    private void assertParXMLEquals(final String xml) throws IOException {
        TestHelper.assertXMLEquals(xml, this.parBuilder.build());
    }
}