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
package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Currency;
import java.util.Locale;

public class CurrencyStyleTest {
    private XMLUtil util;
    private String usSymbol;
    private String frSymbol;
    private Locale locale;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
        this.locale = Locale.US;
        this.usSymbol = Currency.getInstance(this.locale).getSymbol(this.locale);
        this.frSymbol = Currency.getInstance(Locale.FRANCE).getSymbol(Locale.FRANCE);
    }

    @Test
    public final void testDecimalPlaces() throws IOException {
        final CurrencyStyle cs = new CurrencyStyleBuilder("test", this.locale).decimalPlaces(5)
                .build();
        TestHelper.assertXMLEquals(
                "<number:currency-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style" + ":volatile=\"true\">" +
                        "<number:number number:decimal-places=\"5\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text> </number:text>" +
                        "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>" +
                        "</number:currency-style>", cs);
    }

    @Test
    public final void testEuro() throws IOException {
        final CurrencyStyle cs = new CurrencyStyleBuilder("currency-data", Locale.FRANCE).build();
        TestHelper.assertXMLEquals(
                "<number:currency-style style:name=\"currency-data\" number:language=\"fr\" " +
                        "number:country=\"FR\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text> </number:text>" +
                        "<number:currency-symbol>" + this.frSymbol + "</number:currency-symbol>" +
                        "</number:currency-style>", cs);
    }

    @Test
    public final void testGroupThousands() throws IOException {
        final CurrencyStyle cs = new CurrencyStyleBuilder("test", this.locale).groupThousands(true)
                .build();
        TestHelper.assertXMLEquals(
                "<number:currency-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\" number:grouping=\"true\"/>" +
                        "<number:text> </number:text>" + "<number:currency-symbol>" +
                        this.usSymbol + "</number:currency-symbol>" + "</number:currency-style>",
                cs);
    }

    @Test
    public final void testMinIntegerDigits() throws IOException {
        final CurrencyStyle cs = new CurrencyStyleBuilder("test", this.locale).minIntegerDigits(8)
                .build();
        TestHelper.assertXMLEquals(
                "<number:currency-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"8\"/>" + "<number:text> </number:text>" +
                        "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>" +
                        "</number:currency-style>", cs);
    }

    @Test
    public final void testNegativeValueColor() throws IOException {
        final CurrencyStyle cs = new CurrencyStyleBuilder("test", this.locale)
                .negativeValueColor(SimpleColor.GREEN).build();
        TestHelper.assertXMLEquals(
                "<number:currency-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text> </number:text>" +
                        "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>" +
                        "</number:currency-style>" +
                        "<number:currency-style style:name=\"test-neg\" " +
                        "number:language=\"en\" number:country=\"US\" style:volatile=\"true\">" +
                        "<style:text-properties fo:color=\"#008000\"/>" +
                        "<number:text>-</number:text>" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text> </number:text>" +
                        "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>" +
                        "<style:map style:condition=\"value()&gt;=0\" " +
                        "style:apply-style-name=\"test\"/>" + "</number:currency-style>", cs);
    }

    @Test
    public final void testNegativeValueRed() throws IOException {
        final CurrencyStyle cs = new CurrencyStyleBuilder("test", this.locale).negativeValueRed()
                .build();
        TestHelper.assertXMLEquals(
                "<number:currency-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text> </number:text>" +
                        "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>" +
                        "</number:currency-style>" +
                        "<number:currency-style style:name=\"test-neg\" " +
                        "number:language=\"en\" number:country=\"US\" style:volatile=\"true\">" +
                        "<style:text-properties fo:color=\"#ff0000\"/>" +
                        "<number:text>-</number:text>" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text> </number:text>" +
                        "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>" +
                        "<style:map style:condition=\"value()&gt;=0\" " +
                        "style:apply-style-name=\"test\"/>" + "</number:currency-style>", cs);
    }

    @Test
    public final void testStylesElements() throws IOException {
        final CurrencyStyle cs = new CurrencyStyleBuilder("test", this.locale).negativeValueRed()
                .build();
        TestHelper.assertXMLEquals(
                "<number:currency-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text> </number:text>" +
                        "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>" +
                        "</number:currency-style>" +
                        "<number:currency-style style:name=\"test-neg\" " +
                        "number:language=\"en\" number:country=\"US\" style:volatile=\"true\">" +
                        "<style:text-properties fo:color=\"#ff0000\"/>" +
                        "<number:text>-</number:text>" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text> </number:text>" +
                        "<number:currency-symbol>" + this.usSymbol + "</number:currency-symbol>" +
                        "<style:map style:condition=\"value()&gt;=0\" " +
                        "style:apply-style-name=\"test\"/>" + "</number:currency-style>", cs);
    }

    @Test
    public final void testSymbol() throws IOException {
        final CurrencyStyle cs = new CurrencyStyleBuilder("test", this.locale).currencySymbol("ABC")
                .currencySymbolPosition(CurrencyStyle.SymbolPosition.BEGIN).build();
        TestHelper.assertXMLEquals(
                "<number:currency-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " +
                        "style:volatile=\"true\"><number:currency-symbol>ABC</number:currency" +
                        "-symbol><number:number " + "number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/></number:currency-style>", cs);
    }

    @Test
    public final void testLocaleVolatile() throws IOException {
        final CurrencyStyle cs = new CurrencyStyleBuilder("test", this.locale).locale(Locale.FRANCE)
                .country("AB").language("cd").volatileStyle(true).build();
        TestHelper.assertXMLEquals(
                "<number:currency-style style:name=\"test\" number:language=\"cd\" " +
                        "number:country=\"AB\" " +
                        "style:volatile=\"true\"><number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/><number:text> " +
                        "</number:text><number:currency-symbol>" + this.frSymbol +
                        "</number:currency-symbol></number:currency-style>", cs);
    }

    @Test
    public final void testGetters() {
        DataStyleTestHelper.testGetters(new CurrencyStyleBuilder("test", this.locale));
    }

    @Test
    public final void testAddToElements() {
        final CurrencyStyle cs = new CurrencyStyleBuilder("test", this.locale).locale(Locale.FRANCE)
                .country("AB").language("cd").volatileStyle(true).build();
        DataStyleTestHelper.testAddToElements(cs);
    }

}
