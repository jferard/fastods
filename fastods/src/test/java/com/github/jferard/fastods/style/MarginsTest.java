/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MarginsTest {
    private static final SimpleLength TEN_PT = SimpleLength.pt(10.0);

    private XMLUtil util;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public final void testAll() throws IOException {
        final Margins margins = new MarginsBuilder().all(MarginsTest.TEN_PT).build();
        TestHelper.assertXMLEquals(" fo:margin=\"10pt\"", margins);
    }

    @Test
    public final void testRedondant() throws IOException {
        final Margins margins = new MarginsBuilder().all(MarginsTest.TEN_PT).top(MarginsTest.TEN_PT)
                .left(MarginsTest.TEN_PT).bottom(MarginsTest.TEN_PT).right(MarginsTest.TEN_PT)
                .build();
        TestHelper.assertXMLEquals(" fo:margin=\"10pt\"", margins);
    }

    @Test
    public final void testVoidContent() throws IOException {
        final Margins margins = new MarginsBuilder().build();
        TestHelper.assertXMLEquals("", margins);
    }

    @Test
    public final void testTRBL() throws IOException {
        final Margins margins = new MarginsBuilder().all(MarginsTest.TEN_PT).top(MarginsTest.TEN_PT)
                .right(SimpleLength.pt(11.0)).bottom(SimpleLength.pt(12.0))
                .left(SimpleLength.pt(13.0)).build();
        TestHelper.assertXMLEquals(
                " fo:margin=\"10pt\" fo:margin-right=\"11pt\" fo:margin-bottom=\"12pt\" " +
                        "fo:margin-left=\"13pt\"", margins);
    }

    @Test
    public final void testTRBL2() throws IOException {
        final Margins margins =
                new MarginsBuilder().top(MarginsTest.TEN_PT).right(SimpleLength.pt(11.0))
                        .bottom(SimpleLength.pt(12.0)).left(SimpleLength.pt(13.0)).build();
        TestHelper.assertXMLEquals(
                " fo:margin-top=\"10pt\" fo:margin-right=\"11pt\" fo:margin-bottom=\"12pt\" " +
                        "fo:margin-left=\"13pt\"", margins);
    }

    @Test
    public final void testEquals() {
        final Margins margins =
                new MarginsBuilder().top(MarginsTest.TEN_PT).right(SimpleLength.pt(11.0))
                        .bottom(SimpleLength.pt(12.0)).left(SimpleLength.pt(13.0)).build();
        Assert.assertNotEquals(1, margins);
        Assert.assertNotEquals(1, margins);
        Assert.assertEquals(margins, margins);
        final Margins margins2 =
                new MarginsBuilder().top(MarginsTest.TEN_PT).right(SimpleLength.pt(11.0))
                        .bottom(SimpleLength.pt(120.0)).left(SimpleLength.pt(13.0)).build();
        Assert.assertNotEquals(margins2, margins);
        final Margins margins3 =
                new MarginsBuilder().top(MarginsTest.TEN_PT).right(SimpleLength.pt(11.0))
                        .bottom(SimpleLength.pt(12.0)).left(SimpleLength.pt(13.0)).build();
        Assert.assertEquals(margins3, margins);
        Assert.assertEquals(margins3.hashCode(), margins3.hashCode());
    }

    @Test
    public final void testToString() {
        final Margins margins =
                new MarginsBuilder().top(MarginsTest.TEN_PT).right(SimpleLength.pt(11.0))
                        .bottom(SimpleLength.pt(12.0)).left(SimpleLength.pt(13.0)).build();
        Assert.assertEquals("Margins[top=10pt, right=11pt, bottom=12pt, left=13pt, all=null]",
                margins.toString());
        Assert.assertEquals("Margins[top=null, right=null, bottom=null, left=null, all=null]",
                new MarginsBuilder().build().toString());
    }

    @Test
    public final void testToBuilder() throws IOException {
        final Margins margins =
                new MarginsBuilder().top(SimpleLength.pt(15)).build();
        final Margins otherMargins =
                margins.toBuilder().left(SimpleLength.pt(10)).build();
        TestHelper.assertXMLEquals(" fo:margin-top=\"15pt\" fo:margin-left=\"10pt\"", otherMargins);
    }

    @Test
    public final void testVoid() {
        final Margins margins =
                new MarginsBuilder().top(MarginsTest.TEN_PT).right(SimpleLength.pt(11.0))
                        .bottom(SimpleLength.pt(12.0)).left(SimpleLength.pt(13.0)).build();
        Assert.assertFalse(margins.areVoid());
        Assert.assertTrue(new MarginsBuilder().build().areVoid());
    }
}
