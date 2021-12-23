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

package com.github.jferard.fastods.attribute;

import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class BorderAttributeTest {
    private XMLUtil util;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public final void postiionNameTest() {
        Assert.assertEquals("fo:border", BorderAttribute.Position.ALL.getAttrName());
    }

    @Test
    public final void basicTest() throws IOException {
        final BorderAttribute ba = BorderAttribute.builder().borderSize(SimpleLength.cm(1.0))
                .borderColor(SimpleColor.ALICEBLUE).borderStyle(BorderStyle.SOLID).build();
        this.assertXMLEquals("1cm solid #f0f8ff", ba);
    }

    @Test
    public final void nullColorTest() throws IOException {
        final BorderAttribute ba = BorderAttribute.builder().borderSize(10).build();
        this.assertXMLEquals("10pt", ba);
    }

    @Test
    public final void nullSizeTest() throws IOException {
        final BorderAttribute ba =
                BorderAttribute.builder().borderColor(SimpleColor.AQUAMARINE).build();
        this.assertXMLEquals("solid #7fffd4", ba);
    }

    @Test
    public final void nullTest() throws IOException {
        final BorderAttribute ba = BorderAttribute.builder().build();
        this.assertXMLEquals("", ba);
    }

    private void assertXMLEquals(final String expected, final BorderAttribute ba)
            throws IOException {
        final StringBuilder sb = new StringBuilder();
        ba.appendXMLAttribute(this.util, sb, "@");
        Assert.assertEquals(" @=\"" + expected + "\"", sb.toString());
    }
}
