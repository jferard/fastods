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

package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class PositionParserTest {
    private EqualityUtil equalityUtil;
    private TableNameUtil tableNameUtil;

    @Before
    public void setUp() {
        this.equalityUtil = new EqualityUtil();
        this.tableNameUtil = new TableNameUtil();
    }

    @Test(expected = ParseException.class)
    public void testMissingQuote() throws ParseException {
        new PositionParser(this.equalityUtil, this.tableNameUtil, "'f'f'#t.D3").parse();
    }

    @Test(expected = ParseException.class)
    public void testMissingQuote2() throws ParseException {
        new PositionParser(this.equalityUtil, this.tableNameUtil, "'f''#t.D3").parse();
    }

    @Test(expected = ParseException.class)
    public void testRandomQuote2() throws ParseException {
        new PositionParser(this.equalityUtil, this.tableNameUtil, "f'f#t.D3").parse();
    }

    @Test
    public void test() throws ParseException {
        Assert.assertEquals(
                new Position(this.equalityUtil, this.tableNameUtil, "f'", "t't", 2, 3, 7),
                new PositionParser(this.equalityUtil, this.tableNameUtil, "'f'''#'$t''t'.$D$3")
                        .parse());

    }
}