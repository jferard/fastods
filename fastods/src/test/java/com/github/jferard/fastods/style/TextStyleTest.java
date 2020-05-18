/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.attribute.SimpleLength;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TextStyleTest {
    @Test
    public void testDefaultXML() throws IOException {
        final TextStyle style = TextStyle.builder("ts").build();
        TestHelper.assertXMLEquals("<style:style style:name=\"ts\" " +
                "style:family=\"text\"><style:text-properties/></style:style>", style);
    }

    @Test
    public void testGetter() {
        final TextStyle style = TextStyle.builder("ts").visible().build();
        Assert.assertEquals("ts", style.getName());
        Assert.assertEquals(ObjectStyleFamily.TEXT, style.getFamily());
        Assert.assertEquals("TEXT@ts", style.getKey());
        Assert.assertFalse(style.isHidden());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddToElements() {
        final TextStyle style = TextStyle.builder("ts").build();
        style.addToElements(null);
    }

    @Test
    public void testEmpty() {
        Assert.assertFalse(
                TextStyle.builder("ts").build().isNotEmpty());
        Assert.assertTrue(
                TextStyle.builder("ts").fontWeightBold().build()
                        .isNotEmpty());
    }

    @Test
    public final void testBadSize() throws IOException {
        final TextStyle style = TextStyle.builder("ts").fontSize(SimpleLength.cm(10)).build();
        TestHelper.assertXMLEquals(
                "<style:style style:name=\"ts\" style:family=\"text\">" +
                        "<style:text-properties fo:font-size=\"10cm\" style:font-size-asian=\"10cm\" style:font-size-complex=\"10cm\"/>" +
                        "</style:style>", style);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testVisibleOnly() {
        TextStyle.builder("ts").fontSizePercentage(110).build();
    }

    @Test
    public final void testColorNameSize() throws IOException {
        final TextStyle style = TextStyle.builder("ts").fontColor(SimpleColor.ALICEBLUE)
                .fontName(LOFonts.LIBERATION_SERIF).fontSizePercentage(10.8).visible().build();
        TestHelper.assertXMLEquals(
                "<style:style style:name=\"ts\" style:family=\"text\">" +
                        "<style:text-properties fo:color=\"#f0f8ff\" " +
                        "style:font-name=\"Liberation Serif\" fo:font-size=\"10.8%\" " +
                        "style:font-size-asian=\"10.8%\" style:font-size-complex=\"10.8%\"/>" +
                        "</style:style>", style);
    }

    @Test
    public final void testItalicBold() throws IOException {
        final TextStyle prop =
                TextStyle.builder("ts").fontStyleItalic().fontWeightBold().build();
        TestHelper.assertXMLEquals(
                "<style:style style:name=\"ts\" style:family=\"text\">" +
                        "<style:text-properties fo:font-weight=\"bold\" " +
                        "style:font-weight-asian=\"bold\" style:font-weight-complex=\"bold\" " +
                        "fo:font-style=\"italic\" style:font-style-asian=\"italic\" " +
                        "style:font-style-complex=\"italic\"/>" +
                        "</style:style>",
                prop);
    }
}
