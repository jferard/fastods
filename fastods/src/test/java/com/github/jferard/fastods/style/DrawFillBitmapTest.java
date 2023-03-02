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

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class DrawFillBitmapTest {
    @Test
    public void testName() {
        final DrawFillBitmap bitmap = new DrawFillBitmap("name", "bitmap.png");
        Assert.assertEquals("name", bitmap.getName());
    }

    @Test
    public void testFamily() {
        final DrawFillBitmap bitmap = new DrawFillBitmap("name", "bitmap.png");
        Assert.assertEquals(ObjectStyleFamily.DRAW_FILL_BITMAP, bitmap.getFamily());
    }

    @Test
    public void testKey() {
        final DrawFillBitmap bitmap = new DrawFillBitmap("name", "bitmap.png");
        Assert.assertEquals("DRAW_FILL_BITMAP@name", bitmap.getKey());
    }

    @Test
    public void testHidden() {
        final DrawFillBitmap bitmap = new DrawFillBitmap("name", "bitmap.png");
        Assert.assertFalse(bitmap.isHidden());
    }

    @Test
    public void testAppendContent() throws IOException {
        final DrawFillBitmap bitmap = new DrawFillBitmap("name", "bitmap.png");
        TestHelper.assertXMLEquals("<draw:fill-image draw:name=\"name\" " +
                "xlink:href=\"bitmap.png\" " +
                "xlink:type=\"simple\" " +
                "xlink:show=\"embed\" " +
                "xlink:actuate=\"onLoad\"/>", bitmap);
    }

    @Test
    public void testAppendAttributes() throws IOException {
        final DrawFillBitmap bitmap = new DrawFillBitmap("name", "bitmap.png");
        final StringBuilder sb = new StringBuilder();
        bitmap.appendAttributes(XMLUtil.create(), sb);
        Assert.assertEquals(" draw:fill=\"bitmap\" draw:fill-image-name=\"name\"", sb.toString());
    }

    @Test
    public void testAddToElements() throws IOException {
        final OdsElements elements = PowerMock.createMock(OdsElements.class);
        final DrawFillBitmap bitmap = new DrawFillBitmap("name", "bitmap.png");

        PowerMock.resetAll();
        // nothing happens

        PowerMock.replayAll();
        bitmap.addToElements(elements);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddToStyles() throws IOException {
        final StylesContainer container = PowerMock.createMock(StylesContainer.class);
        final DrawFillBitmap bitmap = new DrawFillBitmap("name", "bitmap.png");

        PowerMock.resetAll();
        EasyMock.expect(container.addStylesStyle(bitmap)).andReturn(true);

        PowerMock.replayAll();
        bitmap.addEmbeddedStyles(container);

        PowerMock.verifyAll();
    }
}