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

import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

public class FractionStyleTest {
    private XMLUtil util;
    private Locale locale;

    @Before
    public final void setUp() {
        this.util = XMLUtil.create();
        this.locale = Locale.US;
    }

    @Test
    public final void test1() throws IOException {
        final FractionStyle s = new FractionStyleBuilder("test", this.locale).country("FR").language("en")
                .volatileStyle(true).fractionValues(1, 3).groupThousands(true).minIntegerDigits(8).negativeValueRed()
                .build();
        final StringBuilder sb = new StringBuilder();
        s.appendXMLContent(this.util, sb);
        final String str = "<number:number-style style:name=\"test\" number:language=\"en\" number:country=\"FR\" " +
                "style:volatile=\"true\">" + "<number:fraction number:min-numerator-digits=\"1\" " +
                "number:min-denominator-digits=\"3\" number:min-integer-digits=\"8\" number:grouping=\"true\"/>" +
                "</number:number-style>" + "<number:number-style style:name=\"test-neg\" number:language=\"en\" " +
                "number:country=\"FR\" style:volatile=\"true\">" + "<style:text-properties fo:color=\"#FF0000\"/>" +
                "<number:text>-</number:text>" + "<number:fraction number:min-numerator-digits=\"1\" " +
                "number:min-denominator-digits=\"3\" number:min-integer-digits=\"8\" number:grouping=\"true\"/>" +
                "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"test\"/>" +
                "</number:number-style>";
        DomTester.assertEquals(str, sb.toString());
    }

    @Test
    public final void test2() throws IOException {
        final FractionStyle s = new FractionStyleBuilder("test", this.locale).country("FR").language("en")
                .locale(Locale.GERMANY).volatileStyle(true).fractionValues(1, 3).groupThousands(true)
                .minIntegerDigits(8).negativeValueRed().build();
        final StringBuilder sb = new StringBuilder();
        s.appendXMLContent(this.util, sb);
        final String str = "<number:number-style style:name=\"test\" number:language=\"de\" number:country=\"DE\" " +
                "style:volatile=\"true\">" + "<number:fraction number:min-numerator-digits=\"1\" " +
                "number:min-denominator-digits=\"3\" number:min-integer-digits=\"8\" number:grouping=\"true\"/>" +
                "</number:number-style>" + "<number:number-style style:name=\"test-neg\" number:language=\"de\" " +
                "number:country=\"DE\" style:volatile=\"true\">" + "<style:text-properties fo:color=\"#FF0000\"/>" +
                "<number:text>-</number:text>" + "<number:fraction number:min-numerator-digits=\"1\" " +
                "number:min-denominator-digits=\"3\" number:min-integer-digits=\"8\" number:grouping=\"true\"/>" +
                "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"test\"/>" +
                "</number:number-style>";
        DomTester.assertEquals(str, sb.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testNull() {
        new FractionStyleBuilder(null, this.locale);
    }

}
