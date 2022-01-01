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

package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.SVGRectangle;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class DrawFrameTest {
    private XMLUtil util;
    private FrameContent content;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
        this.content = PowerMock.createMock(FrameContent.class);
    }


    @Test
    public void testContent() throws IOException {
        final StringBuilder sb = new StringBuilder();
        final DrawFrame frame =
                DrawFrame.builder("Frame 1", this.content, SVGRectangle.cm(1, 2, 3, 4)).build();

        PowerMock.resetAll();
        this.content.appendXMLContent(this.util, sb);

        PowerMock.replayAll();
        frame.appendXMLContent(this.util, sb);

        PowerMock.verifyAll();
        DomTester.assertEquals("<draw:frame draw:name=\"Frame 1\" draw:z-index=\"0\" " +
                "svg:width=\"3cm\" svg:height=\"4cm\" svg:x=\"1cm\" " +
                "svg:y=\"2cm\"></draw:frame>", sb.toString());
    }

    @Test
    public void testStyles() {
        final StylesContainer container = PowerMock.createMock(StylesContainer.class);
        final GraphicStyle gs = GraphicStyle.builder("gs").build();
        final DrawFrame frame =
                DrawFrame.builder("Frame 1", this.content, SVGRectangle.cm(1, 2, 3, 4)).style(gs)
                        .textStyle(TextStyle.DEFAULT_TEXT_STYLE).build();

        PowerMock.resetAll();
        EasyMock.expect(container.addContentStyle(gs)).andReturn(true);
        EasyMock.expect(container.addContentStyle(TextStyle.DEFAULT_TEXT_STYLE)).andReturn(true);

        PowerMock.replayAll();
        frame.addEmbeddedStyles(container);

        PowerMock.verifyAll();
    }
}