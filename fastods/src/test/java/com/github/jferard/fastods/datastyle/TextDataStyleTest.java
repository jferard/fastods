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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

public class TextDataStyleTest {
    private Locale locale;

    @Before
    public void setUp() {
        this.locale = Locale.US;
    }

    @Test
    public final void testLocaleVolatile() throws IOException {
        final TextDataStyle bs = new TextDataStyleBuilder("test", this.locale).locale(Locale.FRANCE)
                .volatileStyle(false).build();
        TestHelper.assertXMLEquals(
                "<number:text-style style:name=\"test\" number:language=\"fr\" " +
                        "number:country=\"FR\"><number:text-content/></number:text-style>", bs);
    }

    @Test
    public final void testLanguageCountry() throws IOException {
        final TextDataStyle bs =
                new TextDataStyleBuilder("test", this.locale).language("a").country("b").build();
        TestHelper.assertXMLEquals("<number:text-style style:name=\"test\" number:language=\"a\" " +
                "number:country=\"B\" " +
                "style:volatile=\"true\"><number:text-content/></number:text-style>", bs);
    }

    @Test
    public final void testText() throws IOException {
        final TextDataStyle bs = new TextDataStyleBuilder("test", this.locale).text("text").build();
        TestHelper.assertXMLEquals(
                "<number:text-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" style:volatile=\"true\">text</number:text-style>",
                bs);
    }

    @Test
    public final void testGetters() {
        DataStyleTestHelper.testGetters(new TextDataStyleBuilder("test", this.locale));
    }

    @Test
    public final void testAddToElements() {
        final TextDataStyle bs = new TextDataStyleBuilder("test", this.locale).build();
        DataStyleTestHelper.testAddToElements(bs);
    }

}