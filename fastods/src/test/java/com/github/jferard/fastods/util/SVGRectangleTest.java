/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SVGRectangleTest {
    private XMLUtil util;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public void testMm() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.mm(0, 1, 2, 3);

        this.testAux(rectangle, "mm");
    }

    @Test
    public void testCm() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.cm(0, 1, 2, 3);

        this.testAux(rectangle, "cm");
    }

    @Test
    public void testIn() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.in(0, 1, 2, 3);

        this.testAux(rectangle, "in");
    }

    @Test
    public void testPt() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.pt(0, 1, 2, 3);

        this.testAux(rectangle, "pt");
    }

    @Test
    public void testPc() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.pc(0, 1, 2, 3);

        this.testAux(rectangle, "pc");
    }

    @Test
    public void testEm() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.em(0, 1, 2, 3);

        this.testAux(rectangle, "em");
    }

    private void testAux(final SVGRectangle rectangle, final String unit) throws IOException {
        final StringBuilder sb = new StringBuilder();
        rectangle.appendXMLContent(this.util, sb);
        Assert.assertEquals(" svg:x=\"0@\" svg:y=\"1@\" svg:width=\"2@\" svg:height=\"3@\""
                .replaceAll("@", unit), sb.toString());
    }

}