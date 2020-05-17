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

package com.github.jferard.fastods.ref;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;

public class LocalCellAddressParserTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private LocalCellAddressParser parser;

    @Before
    public void setUp() {
        this.parser = new LocalCellAddressParser();
    }

    @Test
    public void testA2() throws ParseException {
        Assert.assertEquals(new LocalCellRef(1, 0, 0), this.parser.parse("A2"));
    }

    @Test
    public void testdB3() throws ParseException {
        Assert.assertEquals(new LocalCellRef(2, 1, 1), this.parser.parse("$B3"));
    }

    @Test
    public void testCd4() throws ParseException {
        Assert.assertEquals(new LocalCellRef(3, 2, 2), this.parser.parse("C$4"));
    }

    @Test
    public void testdDd5() throws ParseException {
        Assert.assertEquals(new LocalCellRef(4, 3, 3), this.parser.parse("$D$5"));
    }

    @Test
    public void testA0() throws ParseException {
        this.exception.expect(ParseException.class);
        this.exception.expectMessage("Expected digit (not 0) or $: A[0]");
        this.parser.parse("A0");
    }

    @Test
    public void test0A() throws ParseException {
        this.exception.expect(ParseException.class);
        this.exception.expectMessage("Expected letter or $: [0]A");
        this.parser.parse("0A");
    }

    @Test
    public void testA1A() throws ParseException {
        this.exception.expect(ParseException.class);
        this.exception.expectMessage("Expected digit: A1[A]");
        this.parser.parse("A1A");
    }

    @Test
    public void testShort() throws ParseException {
        this.exception.expect(ParseException.class);
        this.exception.expectMessage("Address too short, expected digit: $D$[]");
        this.parser.parse("$D$");
    }

    @Test
    public void testWrongChar() throws ParseException {
        this.exception.expect(ParseException.class);
        this.exception.expectMessage("Expected digit (not 0) or $: $D[.]$5");
        this.parser.parse("$D.$5");
    }
}