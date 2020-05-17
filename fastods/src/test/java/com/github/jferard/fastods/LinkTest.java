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
import com.github.jferard.fastods.odselement.StylesContainerImpl;
import com.github.jferard.fastods.ref.TableNameUtil;
import com.github.jferard.fastods.ref.TableRef;
import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
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

/**
 *
 */
public class LinkTest {
    private TextStyle ts;
    private TableNameUtil util;

    @Before
    public void setUp() {
        this.ts = TextProperties.builder().buildStyle("test");
        this.util = new TableNameUtil();
    }

    @Test
    public final void testTable() throws IOException {
        final Table table = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("t");

        PowerMock.replayAll();
        final Link link = Link.builder("table").to(table).build();
        TestHelper.assertXMLEquals(
                "<text:a xlink:href=\"#t\" " + "xlink:type=\"simple\">table</text:a>", link);

        PowerMock.verifyAll();
    }

    @Test
    public final void testStyleTable() throws IOException {
        final Table table = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("t");

        PowerMock.replayAll();
        final Link link = Link.builder("table").style(this.ts).to(table).build();
        TestHelper.assertXMLEquals("<text:a text:style-name=\"test\" xlink:href=\"#t\" " +
                "xlink:type=\"simple\">table</text:a>", link);

        PowerMock.verifyAll();
    }

    @Test
    public final void testStyleTableRef() throws IOException {
        final Link link =
                Link.builder("table").style(this.ts).to(new TableRef(this.util, "f", "t", 0))
                        .build();
        TestHelper.assertXMLEquals("<text:a text:style-name=\"test\" xlink:href=\"'f'#t\" " +
                "xlink:type=\"simple\">table</text:a>", link);
    }

    @Test
    public final void testTableRef() throws IOException {
        final Link link = Link.builder("table").to(new TableRef(this.util, "f", "t", 0)).build();
        TestHelper.assertXMLEquals(
                "<text:a xlink:href=\"'f'#t\" " + "xlink:type=\"simple\">table</text:a>", link);
    }

    @Test
    public final void testStyleURL() throws IOException {
        final Link link = Link.builder("url").style(this.ts)
                .to(new URL("https://www.github.com/jferard/fastods")).build();
        TestHelper.assertXMLEquals(
                "<text:a text:style-name=\"test\" xlink:href=\"https://www.github" +
                        ".com/jferard/fastods\" " + "xlink" + ":type=\"simple\">url</text:a>",
                link);
    }

    @Test
    public final void testURL() throws IOException {
        final Link link =
                Link.builder("url").to(new URL("https://www.github.com/jferard/fastods")).build();
        TestHelper.assertXMLEquals(
                "<text:a xlink:href=\"https://www.github" + ".com/jferard/fastods\" " + "xlink" +
                        ":type=\"simple\">url</text:a>", link);
    }

    @Test
    public final void testStyleURI() throws IOException, URISyntaxException {
        final Link link =
                Link.builder("A mail").style(this.ts).to(new URI("mailto:mduerst@ifi.unizh.ch"))
                        .build();
        TestHelper.assertXMLEquals(
                "<text:a text:style-name=\"test\" xlink:href=\"mailto:mduerst@ifi.unizh.ch\" " +
                        "xlink" + ":type=\"simple\">A mail</text:a>", link);
    }

    @Test
    public final void testURI() throws IOException, URISyntaxException {
        final Link link =
                new Link("A mail", null, new URI("mailto:mduerst@ifi.unizh.ch").toString());
        TestHelper.assertXMLEquals("<text:a xlink:href=\"mailto:mduerst@ifi.unizh.ch\" " + "xlink" +
                ":type=\"simple\">A mail</text:a>", link);
    }

    @Test
    public final void testStyleFile() throws IOException {
        final File f = new File("generated_files", "fastods_50_5.ods");
        final Link link = Link.builder("file").style(this.ts).to(f).build();
        TestHelper.assertXMLEquals(
                "<text:a text:style-name=\"test\" xlink:href=\"" + f.toURI().toString() +
                        "\" xlink:type=\"simple\">file</text:a>", link);
    }

    @Test
    public final void testFile() throws IOException {
        final File f = new File("generated_files", "fastods_50_5.ods");
        final Link link = Link.builder("file").to(f).build();
        TestHelper.assertXMLEquals("<text:a xlink:href=\"" + f.toURI().toString() +
                "\" xlink:type=\"simple\">file</text:a>", link);
    }

    @Test
    public final void testStyleString() throws IOException {
        final Link link = Link.builder("file").style(this.ts).to("s").build();
        TestHelper.assertXMLEquals(
                "<text:a text:style-name=\"test\" xlink:href=\"s\" " +
                        "xlink:type=\"simple\">file</text:a>",
                link);
    }

    @Test
    public final void testString() throws IOException {
        final Link link = Link.builder("file").to("s").build();
        System.out.println(TestHelper.toXML(link));
        TestHelper.assertXMLEquals("<text:a xlink:href=\"s\" xlink:type=\"simple\">file</text:a>",
                link);
    }

    @Test
    public void testEmbeddedStyles() {
        final StylesContainer container = PowerMock.createMock(StylesContainerImpl.class);
        final TextStyle ts = TextProperties.builder().fontWeightBold().buildStyle("s");
        final Link link = Link.builder("ok").style(ts).to("ref").build();

        PowerMock.resetAll();
        EasyMock.expect(container.addContentFontFaceContainerStyle(ts)).andReturn(true);

        PowerMock.replayAll();
        link.addEmbeddedStylesFromCell(container);

        PowerMock.verifyAll();
    }

    @Test
    public void testEmbeddedStyles2() {
        final StylesContainer container = PowerMock.createMock(StylesContainerImpl.class);
        final TextStyle ts = TextProperties.builder().fontWeightBold().buildStyle("s");
        final Link link = Link.builder("ok").style(ts).to("ref").build();

        PowerMock.resetAll();
        EasyMock.expect(container.addStylesFontFaceContainerStyle(ts)).andReturn(true);

        PowerMock.replayAll();
        link.addEmbeddedStylesFromFooterHeader(container);

        PowerMock.verifyAll();
    }

    @Test
    public void testEquals() {
        final Link link1 = Link.builder("ok").to("ref1").build();
        final Link link2 = Link.builder("ok").to("ref2").build();
        final Link link3 = Link.builder("ok3").to("ref1").build();
        Assert.assertEquals(link1, link1);
        Assert.assertNotEquals(new Object(), link1);
        Assert.assertNotEquals(link1, new Object());
        Assert.assertNotEquals(link2, link1);
        Assert.assertNotEquals(link1, link2);
        Assert.assertNotEquals(link3, link1);
        Assert.assertNotEquals(link1, link3);
    }

    @Test
    public void testHashCode() {
        final Link link = Link.builder("ok").to("ref1").build();
        Assert.assertEquals(108393374, link.hashCode());
    }
}