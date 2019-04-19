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

import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.File;
import java.io.IOException;
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
        final TextStyle ts = TextProperties.builder().fontStyleNormal().fontWeightNormal().buildStyle("style");
        this.parBuilder.style(ts).span("text");
        this.assertParXMLEquals("<text:p text:style-name=\"style\">text</text:p>");
    }

    @Test
    public final void testLinks() throws IOException {
        final Table table = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("tableName");

        PowerMock.replayAll();
        this.parBuilder.link("text1", "ref").link("text1", new File("f")).link("text1", new URL("http:a/b"))
                .link("text1", table);
        this.assertParXMLEquals(
                "<text:p>" + "<text:a xlink:href=\"#ref\" xlink:type=\"simple\">text1</text:a>" + "<text:a " +
                        "xlink:href=\"" + this
                        .getURLStart() + "f\" xlink:type=\"simple\">text1</text:a>" + "<text:a " +
                        "xlink:href=\"http:a/b\" xlink:type=\"simple\">text1</text:a>" + "<text:a " +
                        "xlink:href=\"#tableName\" xlink:type=\"simple\">text1</text:a>" + "</text:p>");

        PowerMock.verifyAll();
    }

    @Test
    public final void testStyledLinks() throws IOException {
        final TextStyle ts = TextProperties.builder().fontStyleNormal().fontWeightNormal().buildStyle("style");
        final Table table = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("tableName");

        PowerMock.replayAll();
        this.parBuilder.styledLink("text1", ts, "ref").styledLink("text1", ts, new File("f"))
                .styledLink("text1", ts, new URL("http:a/b")).styledLink("text1", ts, table);
        this.assertParXMLEquals(
                "<text:p>" + "<text:a text:style-name=\"style\" xlink:href=\"#ref\" " +
                        "xlink:type=\"simple\">text1</text:a>" + "<text:a text:style-name=\"style\" xlink:href=\"" +
                        this
                        .getURLStart() + "f\" xlink:type=\"simple\">text1</text:a>" + "<text:a " +
                        "text:style-name=\"style\" xlink:href=\"http:a/b\" xlink:type=\"simple\">text1</text:a>" +
                        "<text:a text:style-name=\"style\" xlink:href=\"#tableName\" " +
                        "xlink:type=\"simple\">text1</text:a>" + "</text:p>");

        PowerMock.verifyAll();
    }

    private String getURLStart() {
        final String p = new File(".").toURI().toString();
        return p.substring(0, p.length() - 2);
    }

    private void assertParXMLEquals(final String xml) throws IOException {
        TestHelper.assertXMLEquals(xml, this.parBuilder.build());
    }
}