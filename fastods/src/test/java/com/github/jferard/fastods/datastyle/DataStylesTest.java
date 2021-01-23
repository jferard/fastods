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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.attribute.CellType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

public class DataStylesTest {
    private DataStyles ds;

    @Before
    public void setUp() {
        this.ds = DataStylesBuilder.create(Locale.US).build();
    }

    @Test
    public void testGetAll() {
        for (final CellType type : CellType.values()) {
            if (type == CellType.STRING || type == CellType.VOID) {
                Assert.assertNull(this.ds.getDataStyle(type));
            } else {
                Assert.assertNotNull(this.ds.getDataStyle(type));
            }
        }
    }

    @Test
    public void testGetString() {
        Assert.assertNull(this.ds.getDataStyle(CellType.STRING));
    }

    @Test
    public void testGetVoid() {
        Assert.assertNull(this.ds.getDataStyle(CellType.VOID));
    }


    @Test
    public void testGetBoolean() {
        Assert.assertEquals(this.ds.getBooleanDataStyle(), this.ds.getDataStyle(CellType.BOOLEAN));
    }

    @Test
    public void testGetCurrency() {
        Assert.assertEquals(this.ds.getCurrencyDataStyle(),
                this.ds.getDataStyle(CellType.CURRENCY));
    }

    @Test
    public void testGetDate() {
        Assert.assertEquals(this.ds.getDateDataStyle(), this.ds.getDataStyle(CellType.DATE));
    }

    @Test
    public void testGetFloat() {
        Assert.assertEquals(this.ds.getFloatDataStyle(), this.ds.getDataStyle(CellType.FLOAT));
    }

    @Test
    public void testGetPercentage() {
        Assert.assertEquals(this.ds.getPercentageDataStyle(),
                this.ds.getDataStyle(CellType.PERCENTAGE));
    }

    @Test
    public void testGetTime() {
        Assert.assertEquals(this.ds.getTimeDataStyle(), this.ds.getDataStyle(CellType.TIME));
    }

    @Test
    public void testBuilder() throws IOException {
        final DataStylesBuilder dsb = DataStylesBuilder.create(Locale.US);
        dsb.booleanStyleBuilder().country("a");
        dsb.currencyStyleBuilder().country("b");
        dsb.dateStyleBuilder().country("c");
        dsb.floatStyleBuilder().country("d");
        dsb.percentageStyleBuilder().country("e");
        dsb.timeStyleBuilder().country("f");

        final DataStyles ds = dsb.build();
        TestHelper.assertXMLEquals(
                "<number:boolean-style style:name=\"boolean-data\" number:language=\"en\" " +
                        "number:country=\"A\" " + "style:volatile=\"true\"/>",
                ds.getBooleanDataStyle());
        TestHelper.assertXMLEquals(
                "<number:currency-style style:name=\"currency-data\" number:language=\"en\" " +
                        "number:country=\"B\"" +
                        " style:volatile=\"true\"><number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/><number:text> " +
                        "</number:text><number:currency-symbol>$</number:currency-symbol></number" +
                        ":currency-style>", ds.getCurrencyDataStyle());
        TestHelper.assertXMLEquals(
                "<number:date-style style:name=\"date-data\" number:language=\"en\" " +
                        "number:country=\"C\" " +
                        "style:volatile=\"true\" number:automatic-order=\"false\" " +
                        "number:format-source=\"language\"/>", ds.getDateDataStyle());
        TestHelper.assertXMLEquals(
                "<number:number-style style:name=\"float-data\" number:language=\"en\" " +
                        "number:country=\"D\" " +
                        "style:volatile=\"true\"><number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/></number:number-style>",
                ds.getFloatDataStyle());
        TestHelper.assertXMLEquals(
                "<number:percentage-style style:name=\"percentage-data\" number:language=\"en\" " +
                        "number:country=\"E\" style:volatile=\"true\"><number:number " +
                        "number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/><number:text>%</number:text></number" +
                        ":percentage-style>", ds.getPercentageDataStyle());
        TestHelper.assertXMLEquals(
                "<number:time-style style:name=\"time-data\" number:language=\"en\" " +
                        "number:country=\"F\" " +
                        "style:volatile=\"true\" number:format-source=\"language\"/>",
                ds.getTimeDataStyle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {
        new DataStyles(null, this.ds.getCurrencyDataStyle(), this.ds.getDateDataStyle(),
                this.ds.getFloatDataStyle(), this.ds.getPercentageDataStyle(),
                this.ds.getTimeDataStyle());
    }

    @Test
    public final void testConstantFormat() throws IOException {
        final DateTimeStyleFormat ds = DateStyle.Format.DDMMYY;
        TestHelper.assertXMLEquals(
                "<number:day number:style=\"long\"/><number:text>.</number:text><number:month " +
                        "number:style=\"long\"/><number:text>.</number:text><number:year/>", ds);
    }
}