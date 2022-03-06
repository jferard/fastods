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
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class TableRowStyleTest {
    private XMLUtil util;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public final void testCustomHeight() throws IOException {
        final TableRowStyle style =
                TableRowStyle.builder("test").visible().rowHeight(SimpleLength.cm(5)).build();

        TestHelper.assertXMLEquals("<style:style style:name=\"test\" " +
                "style:family=\"table-row\"><style:table-row-properties " +
                "style:row-height=\"5cm\" fo:break-before=\"auto\"/></style" + ":style>", style);
    }

    @Test
    public final void testOptimalHeight() throws IOException {
        final TableRowStyle style = TableRowStyle.builder("test").optimalHeight().build();

        TestHelper.assertXMLEquals("<style:style style:name=\"test\" " +
                "style:family=\"table-row\"><style:table-row-properties " +
                "fo:break-before=\"auto\" style:use-optimal-row-height=\"true\"/></style" +
                ":style>", style);
    }

    @Test
    public final void testGetters() {
        StyleTestHelper.testGettersHidden(TableRowStyle.builder("test"));
    }

    @Test
    public final void testGetNoFontFace() {
//        final TableRowStyle test = TableRowStyle.builder("test").build();
//        Assert.assertNull(test.getFontFace());
    }

    @Test
    public final void testGetRowHeight() {
        final SimpleLength height = SimpleLength.cm(3);
        final TableRowStyle test = TableRowStyle.builder("test").rowHeight(height).build();
        Assert.assertEquals(height, test.getRowHeight());
    }

    @Test
    public final void testGetFontFace() {
        final TableCellStyle tcs =
                TableCellStyle.builder("tcs").fontName(LOFonts.OPENSYMBOL).build();
        Assert.assertEquals(new FontFace(LOFonts.OPENSYMBOL), tcs.getFontFace());
    }

    @Test
    public final void testAddToContentStyles() {
        final TableCellStyle tcs =
                TableCellStyle.builder("tcs").fontName(LOFonts.OPENSYMBOL).build();
        final TableRowStyle trs = TableRowStyle.builder("test").defaultCellStyle(tcs).build();
        final StylesContainer sc = PowerMock.createMock(StylesContainer.class);

        PowerMock.resetAll();
        EasyMock.expect(sc.addContentStyle(trs)).andReturn(true);

        PowerMock.replayAll();
        trs.addToContentStyles(sc);

        PowerMock.verifyAll();
    }
}
