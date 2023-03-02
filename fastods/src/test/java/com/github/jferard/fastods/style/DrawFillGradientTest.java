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
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class DrawFillGradientTest {
    @Test
    public void testName() {
        final DrawFillGradient gradient =
                new DrawFillGradient("name", Angle.ROTATE_90, SimpleColor.RED, 10, SimpleColor.BLUE,
                        50);
        Assert.assertEquals("name", gradient.getName());
    }

    @Test
    public void testFamily() {
        final DrawFillGradient gradient =
                new DrawFillGradient("name", Angle.ROTATE_90, SimpleColor.RED, 10, SimpleColor.BLUE,
                        50);
        Assert.assertEquals(ObjectStyleFamily.DRAW_FILL_GRADIENT, gradient.getFamily());
    }

    @Test
    public void testKey() {
        final DrawFillGradient gradient =
                new DrawFillGradient("name", Angle.ROTATE_90, SimpleColor.RED, 10, SimpleColor.BLUE,
                        50);
        Assert.assertEquals("DRAW_FILL_GRADIENT@name", gradient.getKey());
    }

    @Test
    public void testHidden() {
        final DrawFillGradient gradient =
                new DrawFillGradient("name", Angle.ROTATE_90, SimpleColor.RED, 10, SimpleColor.BLUE,
                        50);
        Assert.assertFalse(gradient.isHidden());
    }

    @Test
    public void testAppendContent() throws IOException {
        final DrawFillGradient gradient =
                new DrawFillGradient("name", Angle.ROTATE_90, SimpleColor.RED, 10, SimpleColor.BLUE,
                        50);
        TestHelper.assertXMLEquals("<draw:gradient draw:name=\"name\" draw:angle=\"90\" draw:start-color=\"#ff0000\" draw:start-intensity=\"10\" draw:end-color=\"#0000ff\" draw:end-intensity=\"50\"/>", gradient);
    }

    @Test
    public void testAppendAttributes() throws IOException {
        final DrawFillGradient gradient =
                new DrawFillGradient("name", Angle.ROTATE_90, SimpleColor.RED, 10, SimpleColor.BLUE,
                        50);
        final StringBuilder sb = new StringBuilder();
        gradient.appendAttributes(XMLUtil.create(), sb);
        Assert.assertEquals(" draw:fill=\"gradient\" draw:fill-gradient-name=\"name\"", sb.toString());
    }

    @Test
    public void testAddToElements() throws IOException {
        final OdsElements elements = PowerMock.createMock(OdsElements.class);
        final DrawFillGradient gradient =
                new DrawFillGradient("name", Angle.ROTATE_90, SimpleColor.RED, 10, SimpleColor.BLUE,
                        50);

        PowerMock.resetAll();
        // nothing happens

        PowerMock.replayAll();
        gradient.addToElements(elements);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddToStyles() throws IOException {
        final StylesContainer container = PowerMock.createMock(StylesContainer.class);
        final DrawFillGradient gradient =
                new DrawFillGradient("name", Angle.ROTATE_90, SimpleColor.RED, 10, SimpleColor.BLUE,
                        50);

        PowerMock.resetAll();
        EasyMock.expect(container.addStylesStyle(gradient)).andReturn(true);

        PowerMock.replayAll();
        gradient.addEmbeddedStyles(container);

        PowerMock.verifyAll();
    }
}