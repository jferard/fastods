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

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TextStyleTest {
    private XMLUtil util;
    private TextStyle ts;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
        this.ts = new TextStyle("ts", false, TextProperties.builder().build());
    }

    @Test
    public void testXML() throws IOException {
        TestHelper.assertXMLEquals("<style:style style:name=\"ts\" " +
                "style:family=\"text\"><style:text-properties/></style:style>", this.ts);
    }

    @Test
    public void testGetter() {
        Assert.assertEquals("ts", this.ts.getName());
        Assert.assertEquals(ObjectStyleFamily.TEXT, this.ts.getFamily());
        Assert.assertEquals("TEXT@ts", this.ts.getKey());
        Assert.assertFalse(this.ts.isHidden());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddToElements() {
        this.ts.addToElements(null);
    }

    @Test
    public void testEmpty() {
        Assert.assertFalse(
                new TextStyle("ts", false, TextProperties.builder().build()).isNotEmpty());
        Assert.assertTrue(
                new TextStyle("ts", false, TextProperties.builder().fontWeightBold().build())
                        .isNotEmpty());
    }
}
