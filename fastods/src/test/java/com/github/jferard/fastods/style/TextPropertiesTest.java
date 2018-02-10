/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TextPropertiesTest {
    private XMLUtil util;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public final void testBadSize() throws IOException {
        final Appendable sb = new StringBuilder();
        final TextProperties prop = TextProperties.builder().fontSize(SimpleLength.cm(10)).build();
        prop.appendXMLContent(this.util, sb);
        DomTester.assertEquals(
                "<style:text-properties fo:font-size=\"10cm\" style:font-size-asian=\"10cm\" style:font-size-complex=\"10cm\"/>",
                sb.toString());
    }

    @Test
    public final void testColorNameSize() throws IOException {
        final Appendable sb = new StringBuilder();
        final TextProperties prop = TextProperties.builder().fontColor(SimpleColor.ALICEBLUE).fontName("Verdana")
                .fontSizePercentage(10.8).build();
        prop.appendXMLContent(this.util, sb);
        DomTester.assertEquals(
                "<style:text-properties fo:color=\"#F0F8FF\" style:font-name=\"Verdana\" fo:font-size=\"10.8%\" style:font-size-asian=\"10.8%\" style:font-size-complex=\"10.8%\"/>",
                sb.toString());
    }

    @Test
    public final void testDefault() throws IOException {
        final Appendable sb = new StringBuilder();
        final TextProperties prop = TextProperties.builder().build();
        prop.appendXMLContent(this.util, sb);
        DomTester.assertEquals(
                "<style:text-properties/>",
                sb.toString());
    }

    @Test
    public final void testItalicBold() throws IOException {
        final Appendable sb = new StringBuilder();
        final TextProperties prop = TextProperties.builder().fontStyleItalic().fontWeightBold().build();
        prop.appendXMLContent(this.util, sb);
        DomTester.assertEquals(
                "<style:text-properties fo:font-weight=\"bold\" style:font-weight-asian=\"bold\" style:font-weight-complex=\"bold\" fo:font-style=\"italic\" style:font-style-asian=\"italic\" style:font-style-complex=\"italic\"/>",
                sb.toString());
    }

    @Test
    public final void testUnderline() throws IOException {
        final Appendable sb = new StringBuilder();
        final TextProperties propr = TextProperties.builder().fontUnderlineStyle(TextProperties.Underline.DASH)
                .fontUnderlineColor(SimpleColor.RED).build();
        propr.appendXMLContent(this.util, sb);
        DomTester.assertEquals(
                "<style:text-properties style:text-underline-style=\"dash\" style:text-underline-width=\"auto\" style:text-underline-color=\"#FF0000\"/>",
                sb.toString());
    }
}
