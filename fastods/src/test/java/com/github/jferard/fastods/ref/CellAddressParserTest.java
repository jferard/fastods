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

package com.github.jferard.fastods.ref;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class CellAddressParserTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private CellAddressParser parser;
    private TableNameUtil tableNameUtil;

    @Before
    public void setUp() {
        this.tableNameUtil = new TableNameUtil();
        this.parser = CellAddressParser.create(this.tableNameUtil);
    }

    @Test(expected = ParseException.class)
    public void testMissingQuote() throws ParseException, IOException {
        this.parser.parse("'f'f'#t.D3");
    }

    @Test(expected = ParseException.class)
    public void testMissingQuote2() throws ParseException, IOException {
        this.parser.parse("'f''#t.D3");
    }

    @Test(expected = ParseException.class)
    public void testRandomQuote2() throws ParseException, IOException {
        this.parser.parse("f'f#t.D3");
    }

    @Test
    public void test() throws ParseException, IOException {
        final TableRef tableRef = new TableRef(this.tableNameUtil, "f'", "t't", 4);
        final LocalCellRef localCellRef = new LocalCellRef(2, 3, 3);
        Assert.assertEquals(new CellRef(tableRef, localCellRef),
                this.parser.parse("'f'''#'$t''t'.$D$3"));

    }

    @Test
    public void testA2() throws ParseException, UnsupportedEncodingException {
        Assert.assertEquals(CellRef.create(1, 0, 0), this.parser.parse("A2"));
    }

    @Test
    public void testdB3() throws ParseException, UnsupportedEncodingException {
        Assert.assertEquals(CellRef.create(2, 1, 1), this.parser.parse("$B3"));
    }

    @Test
    public void testCd4() throws ParseException, UnsupportedEncodingException {
        Assert.assertEquals(CellRef.create(3, 2, 2), this.parser.parse("C$4"));
    }

    @Test
    public void testdDd5() throws ParseException, UnsupportedEncodingException {
        Assert.assertEquals(CellRef.create(4, 3, 3), this.parser.parse("$D$5"));
    }

    @Test
    public void testShort() throws ParseException, UnsupportedEncodingException {
        this.exception.expect(ParseException.class);
        this.exception.expectMessage("Address too short, expected digit: $D$[]");
        this.parser.parse("$D$");
    }

    @Test
    public void testWrongChar() throws ParseException, UnsupportedEncodingException {
        this.exception.expect(ParseException.class);
        this.exception.expectMessage("Expected digit (not 0) or $: $D[@]$5");
        this.parser.parse("$D@$5");
    }

    @Test
    public void testA0() throws ParseException, UnsupportedEncodingException {
        this.exception.expect(ParseException.class);
        this.exception.expectMessage("Expected digit (not 0) or $: A[0]");
        this.parser.parse("A0");
    }

    @Test
    public void test0A() throws ParseException, UnsupportedEncodingException {
        this.exception.expect(ParseException.class);
        this.exception.expectMessage("Expected letter or $: [0]A");
        this.parser.parse("0A");
    }

    @Test
    public void testA1A() throws ParseException, UnsupportedEncodingException {
        this.exception.expect(ParseException.class);
        this.exception.expectMessage("Expected digit: A1[A]");
        this.parser.parse("A1A");
    }

}