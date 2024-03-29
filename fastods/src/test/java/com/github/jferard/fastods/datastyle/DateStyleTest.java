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
import com.github.jferard.fastods.attribute.FormatSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

public class DateStyleTest {
    private Locale locale;

    @Before
    public void setUp() {
        this.locale = Locale.US;
    }

    @Test
    public final void testWithFormat1() throws IOException {
        final DateStyle ds =
                new DateStyleBuilder("test", this.locale).dateFormat(DateStyle.Format.DDMMYY)
                        .build();
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" style:volatile=\"true\" " +
                        "number:automatic-order=\"false\">" +
                        "<number:day number:style=\"long\"/>" +
                        "<number:text>.</number:text><number:month number:style=\"long\"/>" +
                        "<number:text>.</number:text><number:year/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testWithFormat2() throws IOException {
        final DateStyle ds =
                new DateStyleBuilder("test", this.locale).dateFormat(DateStyle.Format.DDMMYYYY)
                        .build();
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" style:volatile=\"true\" " +
                        "number:automatic-order=\"false\">" +
                        "<number:day number:style=\"long\"/><number:text>.</number:text>" +
                        "<number:month number:style=\"long\"/><number:text>.</number:text>" +
                        "<number:year number:style=\"long\"/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testWithFormat3() throws IOException {
        final DateStyle ds =
                new DateStyleBuilder("test", this.locale).dateFormat(DateStyle.Format.MMMM).build();
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" style:volatile=\"true\" " +
                        "number:automatic-order=\"false\">" +
                        "<number:month number:style=\"long\" number:textual=\"true\"/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testWithFormat4() throws IOException {
        final DateStyle ds =
                new DateStyleBuilder("test", this.locale).dateFormat(DateStyle.Format.MMYY).build();
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" style:volatile=\"true\" " +
                        "number:automatic-order=\"false\">" +
                        "<number:month number:style=\"long\"/><number:text>.</number:text>" +
                        "<number:year/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testWithFormat5() throws IOException {
        final DateStyle ds =
                new DateStyleBuilder("test", this.locale).dateFormat(DateStyle.Format.TMMMMYYYY)
                        .build();
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" style:volatile=\"true\" " +
                        "number:automatic-order=\"false\">" +
                        "<number:day/><number:text>. </number:text>" +
                        "<number:month number:style=\"long\" number:textual=\"true\"/>" +
                        "<number:text> </number:text><number:year " + "number:style=\"long\"/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testWithFormat6() throws IOException {
        final DateStyle ds =
                new DateStyleBuilder("test", this.locale).dateFormat(DateStyle.Format.WW).build();
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" style:volatile=\"true\" " +
                        "number:automatic-order=\"false\">" +
                        "<number:week-of-year/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testWithFormat7() throws IOException {
        final DateStyle ds =
                new DateStyleBuilder("test", this.locale).dateFormat(DateStyle.Format.YYYYMMDD)
                        .build();
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" style:volatile=\"true\" " +
                        "number:automatic-order=\"false\">" +
                        "<number:year number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:month number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:day " + "number:style=\"long\"/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testWithLanguage() throws IOException {
        final DateStyle ds = new DateStyleBuilder("test", this.locale).language("fr").build();
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"test\" number:language=\"fr\" " +
                        "number:country=\"US\" style:volatile=\"true\" " +
                        "number:automatic-order=\"false\">" +
                        "<number:year number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:month number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:day number:style=\"long\"/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testWithLocale() throws IOException {
        final DateStyle ds =
                new DateStyleBuilder("test", Locale.FRANCE).locale(this.locale).build();
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " +
                        "style:volatile=\"true\" number:automatic-order=\"false\">" +
                        "<number:year number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:month number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:day number:style=\"long\"/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testFormatSource() throws IOException {
        final DateStyle ds = new DateStyleBuilder("test", this.locale).formatSource(FormatSource.LANGUAGE).build();
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" style:volatile=\"true\" " +
                        "number:automatic-order=\"false\" number:format-source=\"language\">" +
                        "<number:year number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:month number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:day number:style=\"long\"/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testWithNullName() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> new DateStyleBuilder(null, this.locale).build());
    }

    @Test
    public final void testWithOrder() throws IOException {
        final DateStyle ds = new DateStyleBuilder("test", this.locale).automaticOrder(true).build();
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " +
                        "style:volatile=\"true\" number:automatic-order=\"true\">" +
                        "<number:year number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:month number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:day number:style=\"long\"/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testWithVolatile() throws IOException {
        final DateStyle ds = new DateStyleBuilder("test", this.locale).volatileStyle(false).build();
        TestHelper.assertXMLEquals(
                "<number:date-style number:language=\"en\" style:name=\"test\" " +
                        "number:country=\"US\" " +
                        "number:automatic-order=\"false\">" +
                        "<number:year number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:month number:style=\"long\"/><number:text>-</number:text>" +
                        "<number:day number:style=\"long\"/>" +
                        "</number:date-style>", ds);
    }

    @Test
    public final void testGetters() {
        DataStyleTestHelper.testGetters(new DateStyleBuilder("test", this.locale));
    }

    @Test
    public final void testAddToElements() {
        final DateStyle ds =
                new DateStyleBuilder("test", this.locale).dateFormat(DateStyle.Format.DDMMYY)
                        .build();
        DataStyleTestHelper.testAddToElements(ds);
    }
}