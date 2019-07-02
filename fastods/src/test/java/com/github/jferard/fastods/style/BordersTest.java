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
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.style.BorderAttribute.Style;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class BordersTest {

    private XMLUtil util;
    private BorderAttribute a1;
    private BorderAttribute a2;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
        this.a1 = new BorderAttribute(SimpleLength.pt(10.0), SimpleColor.BLACK, Style.DOUBLE);
        this.a2 = new BorderAttribute(SimpleLength.pt(11.0), SimpleColor.WHITE, Style.SOLID);
    }

    @Test
    public final void testXMLEqual() throws IOException {
        final Borders b = new BordersBuilder().all(this.a1).top(this.a1).right(this.a1)
                .bottom(this.a1).left(this.a1).build();
        this.assertAttrXMLEquals(" fo:border=\"10pt double #000000\"", b);
    }

    @Test
    public final void testThreeParam() throws IOException {
        final Borders b = new BordersBuilder()
                .all(SimpleLength.pt(1), SimpleColor.ALICEBLUE, Style.DOUBLE)
                .top(SimpleLength.pt(1), SimpleColor.BLACK, Style.SOLID)
                .left(SimpleLength.pt(2), SimpleColor.ALICEBLUE, Style.DASHED)
                .right(SimpleLength.pt(3), SimpleColor.CADETBLUE, Style.DOUBLE)
                .bottom(SimpleLength.pt(4), SimpleColor.DARKBLUE, Style.GROOVE).build();
        this.assertAttrXMLEquals(" fo:border=\"1pt double #f0f8ff\" fo:border-top=\"1pt solid " +
                "#000000\" fo:border-right=\"3pt double #5f9ea0\" fo:border-bottom=\"4pt groove " +
                "#00008b\" fo:border-left=\"2pt dashed #f0f8ff\"", b);
    }

    @Test
    public final void testDifferent() throws IOException {
        final Borders b = new BordersBuilder().all(this.a1).top(this.a2).right(this.a2)
                .bottom(this.a2).left(this.a2).build();
        this.assertAttrXMLEquals(
                " fo:border=\"10pt double #000000\" fo:border-top=\"11pt solid #ffffff\" " +
                        "fo:border-right=\"11pt " +
                        "solid #ffffff\" fo:border-bottom=\"11pt solid #ffffff\" " +
                        "fo:border-left=\"11pt solid " + "#ffffff\"", b);
    }

    @Test
    public final void test() throws IOException {
        final Borders b = new BordersBuilder().all(this.a1).top(this.a2).right(this.a1)
                .bottom(this.a2).left(this.a1).build();
        this.assertAttrXMLEquals(
                " fo:border=\"10pt double #000000\" fo:border-top=\"11pt solid #ffffff\" " +
                        "fo:border-bottom=\"11pt " + "solid #ffffff\"", b);
    }

    @Test
    public final void testEquals() {
        final Borders b1 = new BordersBuilder().all(this.a1).build();
        final Borders b2 = new BordersBuilder().all(this.a2).build();
        final Borders b3 = new BordersBuilder().all(this.a1).build();

        Assert.assertNotEquals(b1, new Object());
        Assert.assertNotEquals(null, b1);
        Assert.assertNotEquals("a", b1);
        Assert.assertNotEquals(b1, b2);
        Assert.assertEquals(b1, b1);
        Assert.assertEquals(b1, b3);
    }

    @Test
    public final void testHashCode() {
        final Borders b1 = new BordersBuilder().all(this.a1).build();
        final Borders b2 = new BordersBuilder().all(this.a1).build();

        Assert.assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    public final void testNoDefaultColor() throws IOException {
        final Borders b = new BordersBuilder()
                .all(new BorderAttributeBuilder().borderSize(SimpleLength.pt(1)).build()).build();
        this.assertAttrXMLEquals(" fo:border=\"1pt\"", b);
    }

    @Test
    public final void testToString() {
        final Borders b = new BordersBuilder()
                .all(new BorderAttributeBuilder().borderSize(SimpleLength.pt(1)).build()).build();
        Assert.assertEquals(
                "Borders[top=null, right=null, bottom=null, left=null, all=BorderAttribute[1pt]]",
                b.toString());
    }

    private void assertAttrXMLEquals(final String xml, final Borders b) throws IOException {
        final StringBuilder sb = new StringBuilder();
        b.appendXMLContent(this.util, sb);
        Assert.assertEquals(xml, sb.toString());
    }

}
