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
import com.github.jferard.fastods.util.ColorHelper;
import com.github.jferard.fastods.util.PositionUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by jferard on 20/05/17.
 */
public class TextTest {
    private TextStyle ts;

    @Before
    public void setUp() throws IOException {
        this.ts = TextProperties.builder().fontName("fn").fontColor(ColorHelper.fromString("fc"))
                .buildStyle("ts");
    }

    @Test
    public void parContent() throws Exception {
        final Text t = Text.content("a");
        TestHelper.assertXMLEquals("<text:p>a</text:p>", t);
    }

    @Test
    public void empty() throws Exception {
        final Text t = TextBuilder.create().build();
        TestHelper.assertXMLEquals("", t);
        Assert.assertTrue(t.isEmpty());
    }

    @Test
    public void parStyledContent() throws Exception {
        final Text t = TextBuilder.create().parStyledContent("a", this.ts).build();
        TestHelper
                .assertXMLEquals("<text:p><text:span text:style-name=\"ts\">a</text:span></text:p>",
                        t);
    }

    @Test
    public void linkRef() throws Exception {
        final Text t = TextBuilder.create().par().link("a", "ref").build();
        TestHelper.assertXMLEquals(
                "<text:p><text:a xlink:href=\"#ref\" xlink:type=\"simple\">a</text:a></text:p>", t);
    }

    @Test
    public void styledLinkRef() throws Exception {
        final Text t = TextBuilder.create().par().styledLink("a", this.ts, "ref").build();
        TestHelper.assertXMLEquals("<text:p><text:a text:style-name=\"ts\" xlink:href=\"#ref\" " +
                "xlink:type=\"simple\">a</text:a></text:p>", t);
    }

    @Test
    public void linkURL() throws Exception {
        final Text t = TextBuilder.create().par().link("a", new URL("http://url")).build();
        TestHelper.assertXMLEquals("<text:p><text:a xlink:href=\"http://url\" " +
                "xlink:type=\"simple\">a</text:a></text:p>", t);
    }

    @Test
    public void styledLinkURL() throws Exception {
        final Text t = TextBuilder.create().par().styledLink("a", this.ts, new URL("http://url"))
                .build();
        TestHelper.assertXMLEquals(
                "<text:p><text:a text:style-name=\"ts\" xlink:href=\"http://url\" " +
                        "xlink:type=\"simple\">a</text:a></text:p>", t);
    }

    @Test
    public void linkFile() throws Exception {
        final File f = new File("f");
        final Text t = TextBuilder.create().par().link("a", f).build();
        TestHelper.assertXMLEquals("<text:p><text:a xlink:href=\"" + f.toURI().toString() +
                "\" xlink:type=\"simple\">a</text:a></text:p>", t);
    }

    @Test
    public void styledLinkFile() throws Exception {
        final File f = new File("f");
        final Text t = TextBuilder.create().par().styledLink("a", this.ts, f).build();
        TestHelper.assertXMLEquals(
                "<text:p><text:a text:style-name=\"ts\" xlink:href=\"" + f.toURI().toString() +
                        "\" xlink:type=\"simple\">a</text:a></text:p>", t);
    }

    @Test
    public void linkTable() throws Exception {
        final Table table = Table.create(PositionUtil.create(), null, null, "n", 0, 0, null, null,
                false);
        final Text t = TextBuilder.create().par().link("a", table).build();
        Assert.assertEquals("n", table.getName());
        TestHelper.assertXMLEquals(
                "<text:p><text:a xlink:href=\"#n\" xlink:type=\"simple\">a</text:a></text:p>", t);
    }

    @Test
    public void styledLinkTable() throws Exception {
        final Table table = Table.create(PositionUtil.create(), null, null, "n", 0, 0, null, null,
                false);
        final Text t = TextBuilder.create().par().styledLink("a", this.ts, table).build();
        TestHelper.assertXMLEquals("<text:p><text:a text:style-name=\"ts\" xlink:href=\"#n\" " +
                "xlink:type=\"simple\">a</text:a></text:p>", t);
    }

    @Test
    public void testEmbeddedStyles() {
        final StylesContainer container = PowerMock.createMock(StylesContainer.class);
        final TextStyle ts = TextProperties.builder().fontWeightBold().buildStyle("s");
        final Text text = Text.builder().parStyledContent("ok", ts).parStyledContent("ok2", ts)
                .build();

        PowerMock.resetAll();
        EasyMock.expect(container.addContentFontFaceContainerStyle(ts)).andReturn(true);
        EasyMock.expect(container.addContentFontFaceContainerStyle(ts)).andReturn(false);

        PowerMock.replayAll();
        text.addEmbeddedStylesFromCell(container);

        PowerMock.verifyAll();
    }
}