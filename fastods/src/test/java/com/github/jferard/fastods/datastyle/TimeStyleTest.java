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
package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.IOException;
import java.util.Locale;

public class TimeStyleTest {
    private Locale locale;

    @Before
    public void setUp() {
        this.locale = Locale.US;
    }

    @Test
    public final void testFormat() throws IOException {
        final TimeStyle ts =
                new TimeStyleBuilder("test", this.locale).timeFormat(TimeStyle.Format.HHMMSS)
                        .build();
        TestHelper.assertXMLEquals(
                "<number:time-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style" +
                        ":volatile=\"true\">" +
                        "<number:hours number:style=\"long\"/>" + "<number:text>:</number:text>" +
                        "<number:minutes number:style=\"long\"/>" + "<number:text>:</number:text>" +
                        "<number:seconds number:style=\"long\"/>" + "</number:time-style>", ts);
    }

    @Test
    public final void testNullFormat() throws IOException {
        final TimeStyle ts = new TimeStyleBuilder("test", this.locale).timeFormat(null).build();
        Assert.assertThrows(NullPointerException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                ts.appendXMLContent(XMLUtil.create(), new StringBuilder());
            }
        });
    }

    @Test
    public final void testLocaleVolatile() throws IOException {
        final TimeStyle ts =
                new TimeStyleBuilder("test", this.locale).locale(Locale.FRANCE).volatileStyle(false)
                        .build();
        TestHelper.assertXMLEquals(
                "<number:time-style style:name=\"test\" number:language=\"fr\" " +
                        "number:country=\"FR\">" +
                        "<number:hours number:style=\"long\"/><number:text>:</number:text>" +
                        "<number:minutes number:style=\"long\"/><number:text>:</number:text>" +
                        "<number:seconds number:style=\"long\"/>" +
                        "</number:time-style>", ts);
    }

    @Test
    public final void testLanguageCountry() throws IOException {
        final TimeStyle ts =
                new TimeStyleBuilder("test", this.locale).language("a").country("b").build();
        TestHelper.assertXMLEquals("<number:time-style style:name=\"test\" number:language=\"a\" " +
                "number:country=\"B\" " +
                "style:volatile=\"true\">" +
                "<number:hours number:style=\"long\"/><number:text>:</number:text>" +
                "<number:minutes number:style=\"long\"/><number:text>:</number:text>" +
                "<number:seconds number:style=\"long\"/>" +
                "</number:time-style>", ts);
    }

    @Test
    public final void testGetters() {
        DataStyleTestHelper.testGetters(new TimeStyleBuilder("test", this.locale));
    }

    @Test
    public final void testAddToElements() {
        final TimeStyle ts =
                new TimeStyleBuilder("test", this.locale).locale(Locale.FRANCE).volatileStyle(false)
                        .build();
        DataStyleTestHelper.testAddToElements(ts);
    }

    @Test
    public final void testConstantFormat() throws IOException {
        final DateTimeStyleFormat ts = TimeStyle.Format.HHMMSS;
        TestHelper.assertXMLEquals(
                "<number:hours number:style=\"long\"/><number:text>:</number:text><number:minutes" +
                        " number:style=\"long\"/><number:text>:</number:text><number:seconds " +
                        "number:style=\"long\"/>", ts);
    }
}
