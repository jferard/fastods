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

import com.github.jferard.fastods.attribute.Angle;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.StylesContainerImpl;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class GraphicStyleTest {
    @Test
    public void testName() {
        final GraphicStyle gs = GraphicStyle.builder("gs").build();
        Assert.assertEquals("gs", gs.getName());
    }

    @Test
    public void testFamily() {
        final GraphicStyle gs = GraphicStyle.builder("gs").build();
        Assert.assertEquals(ObjectStyleFamily.GRAPHIC, gs.getFamily());
    }

    @Test
    public void testKey() {
        final GraphicStyle gs = GraphicStyle.builder("gs").build();
        Assert.assertEquals("GRAPHIC@gs", gs.getKey());
    }

    @Test
    public void testHidden() {
        final GraphicStyle gs = GraphicStyle.builder("gs").build();
        Assert.assertTrue(gs.isHidden());
        final GraphicStyle gs2 = GraphicStyle.builder("gs2").visible().build();
        Assert.assertFalse(gs2.isHidden());
    }

    @Test
    public void testAddEmbeddedStyles() {
        final StylesContainer container = PowerMock.createMock(StylesContainer.class);
        final GraphicStyle gs = GraphicStyle.builder("gs").build();

        PowerMock.resetAll();

        PowerMock.replayAll();
        gs.addEmbeddedStyles(container);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddEmbeddedStylesDrawFill() {
        final StylesContainer container = PowerMock.createMock(StylesContainer.class);
        final DrawFillGradient gradient =
                new DrawFillGradient("gradient", Angle.ROTATE_90, SimpleColor.GREEN, 50,
                        SimpleColor.BLUE, 50);
        final GraphicStyle gs = GraphicStyle.builder("gs").drawFill(gradient).build();

        PowerMock.resetAll();
        EasyMock.expect(container.addStylesStyle(gradient)).andReturn(true);

        PowerMock.replayAll();
        gs.addEmbeddedStyles(container);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddToElements() {
        final OdsElements elements = PowerMock.createMock(OdsElements.class);
        final GraphicStyle gs = GraphicStyle.builder("gs").build();

        PowerMock.resetAll();
        EasyMock.expect(elements.addContentStyle(gs)).andReturn(true);

        PowerMock.replayAll();
        gs.addToElements(elements);

        PowerMock.verifyAll();
    }

    @Test
    public void testAppendXMLContent() throws IOException {
        final GraphicStyle gs = GraphicStyle.builder("gs").build();
        final StringBuilder sb = new StringBuilder();

        gs.appendXMLContent(XMLUtil.create(), sb);

        DomTester.assertEquals(
                "<style:style style:name=\"gs\" style:family=\"graphic\">" +
                        "<style:graphic-properties draw:fill=\"none\" draw:stroke=\"none\" " +
                        "draw:textarea-horizontal-align=\"center\" " +
                        "draw:textarea-vertical-align=\"middle\"/>" +
                        "</style:style>", sb.toString());
    }

    @Test
    public void testAppendXMLContentDrawFill() throws IOException {
        final GraphicStyle gs = GraphicStyle.builder("gs").drawFill(new DrawFillSolid(SimpleColor.ORANGE)).build();
        final StringBuilder sb = new StringBuilder();

        gs.appendXMLContent(XMLUtil.create(), sb);

        DomTester.assertEquals(
                "<style:style style:name=\"gs\" style:family=\"graphic\">" +
                        "<style:graphic-properties draw:fill=\"solid\" " +
                        "draw:fill-color=\"#ffa500\"/>" +
                        "</style:style>", sb.toString());
    }
}