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
package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.TestHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        final TimeStyle ts = new TimeStyleBuilder("test", this.locale).timeFormat(TimeStyle.Format.HHMMSS).build();
        TestHelper.assertXMLEquals(
                "<number:time-style style:name=\"test\" number:language=\"en\" number:country=\"US\" " + "style" +
                        ":volatile=\"true\" number:format-source=\"fixed\">" + "<number:hours/>" +
                        "<number:text>:</number:text>" + "<number:minutes/>" + "<number:text>:</number:text>" +
                        "<number:seconds/>" + "</number:time-style>",
                ts);
    }

    @Test
    public final void testNullFormat() throws IOException {
        final TimeStyle ts = new TimeStyleBuilder("test", this.locale).timeFormat(null).build();
        TestHelper.assertXMLEquals(
                "<number:time-style style:name=\"test\" number:language=\"en\" number:country=\"US\" " +
                        "style:volatile=\"true\" number:format-source=\"language\"/>",
                ts);
    }

    @Test
    public final void testHiddenNullFormat() throws IOException {
        final TimeStyle ts = new TimeStyleBuilder("test", this.locale).timeFormat(null).hidden().build();
        TestHelper.assertXMLEquals(
                "<number:time-style style:name=\"test\" number:language=\"en\" number:country=\"US\" " +
                        "style:volatile=\"true\" number:format-source=\"language\"/>",
                ts);
    }

    @Test
    public final void testLocaleVolatile() throws IOException {
        final TimeStyle ts = new TimeStyleBuilder("test", this.locale).locale(Locale.FRANCE).volatileStyle(false)
                .build();
        TestHelper.assertXMLEquals(
                "<number:time-style style:name=\"test\" number:language=\"fr\" number:country=\"FR\" " +
                        "number:format-source=\"language\"/>",
                ts);
    }

    @Test
    public final void testLanguageCountry() throws IOException {
        final TimeStyle ts = new TimeStyleBuilder("test", this.locale).language("a").country("b").build();
        TestHelper.assertXMLEquals(
                "<number:time-style style:name=\"test\" number:language=\"a\" number:country=\"B\" " +
                        "style:volatile=\"true\" number:format-source=\"language\"/>",
                ts);
    }

    @Test
    public final void testGetters() throws IOException {
        final TimeStyle ts = new TimeStyleBuilder("test", this.locale).language("a").country("b").visible().build();
        Assert.assertEquals("test", ts.getName());
        Assert.assertFalse(ts.isHidden());
        final TimeStyle ts2 = new TimeStyleBuilder("test", this.locale).language("a").country("b").build();
        Assert.assertTrue(ts2.isHidden());
        final TimeStyle ts3 = new TimeStyleBuilder("test", this.locale).language("a").country("b").hidden().build();
        Assert.assertTrue(ts3.isHidden());
    }

    @Test
    public final void testAddToElements() throws IOException {
        final TimeStyle ts = new TimeStyleBuilder("test", this.locale).locale(Locale.FRANCE).volatileStyle(false)
                .build();
        DataStyleTestHelper.testAddToElements(ts);
    }
}
