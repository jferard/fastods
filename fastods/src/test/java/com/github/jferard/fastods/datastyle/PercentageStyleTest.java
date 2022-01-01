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
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.odselement.OdsElements;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Locale;

public class PercentageStyleTest {
    private Locale locale;

    @Before
    public void setUp() {
        this.locale = Locale.US;
    }

    @Test
    public final void testDecimalPlaces() throws IOException {
        final PercentageStyle ps =
                new PercentageStyleBuilder("test", this.locale).decimalPlaces(5).build();
        TestHelper.assertXMLEquals(
                "<number:percentage-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style" + ":volatile=\"true\">" +
                        "<number:number number:decimal-places=\"5\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text>%</number:text>" +
                        "</number:percentage-style>", ps);
    }

    @Test
    public final void testGroupThousands() throws IOException {
        final PercentageStyle ps =
                new PercentageStyleBuilder("test", this.locale).groupThousands(true).build();
        TestHelper.assertXMLEquals(
                "<number:percentage-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\" number:grouping=\"true\"/>" +
                        "<number:text>%</number:text>" + "</number:percentage-style>", ps);
    }

    @Test
    public final void testMinIntegerDigits() throws IOException {
        final PercentageStyle ps =
                new PercentageStyleBuilder("test", this.locale).minIntegerDigits(8).build();

        final OdsElements elements = PowerMock.createMock(OdsElements.class);

        PowerMock.resetAll();
        EasyMock.expect(elements.addDataStyle(ps)).andReturn(true);

        PowerMock.replayAll();
        ps.addToElements(elements);

        PowerMock.verifyAll();
        TestHelper.assertXMLEquals(
                "<number:percentage-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"8\"/>" + "<number:text>%</number:text>" +
                        "</number:percentage-style>", ps);
    }

    @Test
    public final void testNegativeValueColor() throws IOException {
        final PercentageStyle ps = new PercentageStyleBuilder("test", this.locale)
                .negativeValueColor(SimpleColor.GREEN).build();
        TestHelper.assertXMLEquals(
                "<number:percentage-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text>%</number:text>" +
                        "</number:percentage-style>" +
                        "<number:percentage-style style:name=\"test-neg\" " +
                        "number:language=\"en\" number:country=\"US\" style:volatile=\"true\">" +
                        "<style:text-properties fo:color=\"#008000\"/>" +
                        "<number:text>-</number:text>" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text>%</number:text>" +
                        "<style:map style:condition=\"value()&gt;=0\" " +
                        "style:apply-style-name=\"test\"/>" + "</number:percentage-style>", ps);
    }

    @Test
    public final void testNegativeValueRed() throws IOException {
        final PercentageStyle ps =
                new PercentageStyleBuilder("test", this.locale).negativeValueRed().build();
        TestHelper.assertXMLEquals(
                "<number:percentage-style style:name=\"test\" number:language=\"en\" " +
                        "number:country=\"US\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text>%</number:text>" +
                        "</number:percentage-style>" +
                        "<number:percentage-style style:name=\"test-neg\" " +
                        "number:language=\"en\" number:country=\"US\" style:volatile=\"true\">" +
                        "<style:text-properties fo:color=\"#ff0000\"/>" +
                        "<number:text>-</number:text>" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text>%</number:text>" +
                        "<style:map style:condition=\"value()&gt;=0\" " +
                        "style:apply-style-name=\"test\"/>" + "</number:percentage-style>", ps);
        Assert.assertEquals("test", ps.getName());
    }

    @Test
    public final void testCountryLanguage() throws IOException {
        final PercentageStyle ps =
                new PercentageStyleBuilder("test", this.locale).country("a").language("b").visible()
                        .build();
        TestHelper.assertXMLEquals(
                "<number:percentage-style style:name=\"test\" number:language=\"b\" " +
                        "number:country=\"A\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text>%</number:text>" +
                        "</number:percentage-style>", ps);
        Assert.assertFalse(ps.isHidden());
    }

    @Test
    public final void testLocaleVolatile() throws IOException {
        final PercentageStyle ps =
                new PercentageStyleBuilder("test", this.locale).locale(Locale.FRANCE)
                        .volatileStyle(true).build();
        TestHelper.assertXMLEquals(
                "<number:percentage-style style:name=\"test\" number:language=\"fr\" " +
                        "number:country=\"FR\" " + "style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"2\" " +
                        "number:min-integer-digits=\"1\"/>" + "<number:text>%</number:text>" +
                        "</number:percentage-style>", ps);
        Assert.assertTrue(ps.isHidden());
    }

    @Test
    public final void testAddToElements() {
        final PercentageStyle ps = new PercentageStyleBuilder("test", this.locale).build();
        DataStyleTestHelper.testAddToElements(ps);
    }

    @Test
    public final void testGetters() {
        DataStyleTestHelper.testGetters(new PercentageStyleBuilder("test", this.locale));
    }

}
