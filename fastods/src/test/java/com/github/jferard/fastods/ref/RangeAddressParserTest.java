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

package com.github.jferard.fastods.ref;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class RangeAddressParserTest {
    public static final LocalCellRef A1 = new LocalCellRef(0, 0, 0);

    private RangeAddressParser parser;
    private TableNameUtil tableNameUtil;

    @Before
    public void setUp() {
        this.tableNameUtil = new TableNameUtil();
        this.parser = RangeAddressParser.create(this.tableNameUtil);
    }

    @Test
    public void testMissingQuote() {
        Assert.assertThrows(ParseException.class, () -> this.parser.parse("'f'f'#t.A1:D3"));
    }

    @Test
    public void testMissingQuote2() {
        Assert.assertThrows(ParseException.class, () -> this.parser.parse("'f''#t.A1:D3"));
    }

    @Test
    public void testRandomQuote2() {
        Assert.assertThrows(ParseException.class, () -> this.parser.parse("f'f#t.A1:D3"));

    }

    @Test
    public void test() throws ParseException, IOException {
        final TableRef tableRef = new TableRef(this.tableNameUtil, "f'", "t't", 4);
        final LocalCellRef localCellRef = new LocalCellRef(2, 3, 3);
        Assert.assertEquals(new RangeRef(tableRef, A1, localCellRef),
                this.parser.parse("'f'''#'$t''t'.A1:$D$3"));

    }

    @Test
    public void testA2() throws ParseException, UnsupportedEncodingException {
        Assert.assertEquals(new RangeRef(null, A1, new LocalCellRef(1, 0, 0)),
                this.parser.parse("A1:A2"));
    }

    @Test
    public void testdB3() throws ParseException, UnsupportedEncodingException {
        Assert.assertEquals(new RangeRef(null, A1, new LocalCellRef(2, 1, 1)),
                this.parser.parse("A1:$B3"));
    }

    @Test
    public void testCd4() throws ParseException, UnsupportedEncodingException {
        Assert.assertEquals(new RangeRef(null, A1, new LocalCellRef(3, 2, 2)),
                this.parser.parse("A1:C$4"));
    }

    @Test
    public void testdDd5() throws ParseException, UnsupportedEncodingException {
        Assert.assertEquals(new RangeRef(null, A1, new LocalCellRef(4, 3, 3)),
                this.parser.parse("A1:$D$5"));
    }

    @Test
    public void testShort() {
        Assert.assertThrows("Expected a `:` symbol: $D$", ParseException.class,
                () -> this.parser.parse("$D$"));
    }

    @Test
    public void testWrongChar() {
        Assert.assertThrows("Expected digit (not 0) or $: $D[@]$5", ParseException.class,
                () -> this.parser.parse("A1:$D@$5"));
    }

    @Test
    public void testA0() {
        Assert.assertThrows("Expected digit (not 0) or $: A[0]", ParseException.class,
                () -> this.parser.parse("A1:A0"));
    }

    @Test
    public void test0A() {
        Assert.assertThrows("Expected letter or $: [0]A", ParseException.class,
                () -> this.parser.parse("A1:0A"));
    }

    @Test
    public void testA1A() {
        Assert.assertThrows("Expected digit: A1[A]", ParseException.class,
                () -> this.parser.parse("A1:A1A"));
    }

}