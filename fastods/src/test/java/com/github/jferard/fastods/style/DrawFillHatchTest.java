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
import com.github.jferard.fastods.attribute.Angle;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class DrawFillHatchTest {
    @Test
    public void testName() {
        final DrawFillHatch hatch =
                new DrawFillHatch("name", Angle.ROTATE_180, SimpleColor.ORANGE, SimpleLength.cm(2));
        Assert.assertEquals("name", hatch.getName());
    }

    @Test
    public void testFamily() {
        final DrawFillHatch hatch =
                new DrawFillHatch("name", Angle.ROTATE_180, SimpleColor.ORANGE, SimpleLength.cm(2));

        Assert.assertEquals(ObjectStyleFamily.DRAW_FILL_HATCH, hatch.getFamily());
    }

    @Test
    public void testKey() {
        final DrawFillHatch hatch =
                new DrawFillHatch("name", Angle.ROTATE_180, SimpleColor.ORANGE, SimpleLength.cm(2));

        Assert.assertEquals("DRAW_FILL_HATCH@name", hatch.getKey());
    }

    @Test
    public void testHidden() {
        final DrawFillHatch hatch =
                new DrawFillHatch("name", Angle.ROTATE_180, SimpleColor.ORANGE, SimpleLength.cm(2));

        Assert.assertFalse(hatch.isHidden());
    }

    @Test
    public void testAppendContent() throws IOException {
        final DrawFillHatch hatch =
                new DrawFillHatch("name", Angle.ROTATE_180, SimpleColor.ORANGE, SimpleLength.cm(2));

        TestHelper.assertXMLEquals(
                "<draw:hatch draw:name=\"name\" draw:angle=\"180\" draw:color=\"#ffa500\" draw:distance=\"2cm\"/>",
                hatch);
    }

    @Test
    public void testAppendAttributes() throws IOException {
        final DrawFillHatch hatch =
                new DrawFillHatch("name", Angle.ROTATE_180, SimpleColor.ORANGE, SimpleLength.cm(2));

        final StringBuilder sb = new StringBuilder();
        hatch.appendAttributes(XMLUtil.create(), sb);
        Assert.assertEquals(" draw:fill=\"hatch\" draw:fill-hatch-name=\"name\"", sb.toString());
    }

    @Test
    public void testAddToElements() throws IOException {
        final OdsElements elements = PowerMock.createMock(OdsElements.class);
        final DrawFillHatch hatch =
                new DrawFillHatch("name", Angle.ROTATE_180, SimpleColor.ORANGE, SimpleLength.cm(2));


        PowerMock.resetAll();
        // nothing happens

        PowerMock.replayAll();
        hatch.addToElements(elements);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddToStyles() throws IOException {
        final StylesContainer container = PowerMock.createMock(StylesContainer.class);
        final DrawFillHatch hatch =
                new DrawFillHatch("name", Angle.ROTATE_180, SimpleColor.ORANGE, SimpleLength.cm(2));


        PowerMock.resetAll();
        EasyMock.expect(container.addStylesStyle(hatch)).andReturn(true);

        PowerMock.replayAll();
        hatch.addEmbeddedStyles(container);

        PowerMock.verifyAll();
    }
}