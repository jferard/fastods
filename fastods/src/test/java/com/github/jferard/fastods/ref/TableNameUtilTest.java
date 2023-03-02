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

package com.github.jferard.fastods.ref;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class TableNameUtilTest {
    private TableNameUtil util;

    @Before
    public void setUp() {
        this.util = new TableNameUtil();
    }

    @Test
    public void testCheckTableNameQuoteAtFirstPlace() {
        final TableNameUtil finalUtil = this.util;
        Assert.assertThrows("Table name should not start with ': 'quote at first place",
                IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        finalUtil.checkTableName("'quote at first place");
                    }
                });
    }

    @Test
    public void testSanitizeTableNameQuoteAtFirstPlace() {
        final String actual = this.util.sanitizeTableName("'quote at first place");
        Assert.assertEquals("_quote at first place", actual);
        this.util.checkTableName(actual);
    }

    @Test
    public void testCheckTableNameNoQuoteAtFirstPlace() {
        this.util.checkTableName(" 'quote at second place");
    }

    @Test
    public void testSanitizeTableNameNoQuoteAtFirstPlace() {
        final String actual = this.util.sanitizeTableName(" 'quote at second place");
        Assert.assertEquals(" 'quote at second place", actual);
        this.util.checkTableName(actual);
    }

    @Test
    public void testCheckTableNameForbiddenCharLeftSquareBracket() {
        final TableNameUtil finalUtil = this.util;
        Assert.assertThrows("Table name should not contain []*?:/\\: a name with [",
                IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        finalUtil.checkTableName("a name with [");
                    }
                });
    }

    @Test
    public void testSanitizeTableNameForbiddenCharLeftSquareBracket() {
        final String actual = this.util.sanitizeTableName("a name with [");
        Assert.assertEquals("a name with _", actual);
        this.util.checkTableName(actual);
    }

    @Test
    public void testCheckTableNameForbiddenCharRightSquareBracket() {
        final TableNameUtil finalUtil = this.util;
        Assert.assertThrows("Table name should not contain []*?:/\\: a name with ]",
                IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        finalUtil.checkTableName("a name with ]");
                    }
                });
    }

    @Test
    public void testSanitizeTableNameForbiddenCharRightSquareBracket() {
        final String actual = this.util.sanitizeTableName("a name with ]");
        Assert.assertEquals("a name with _", actual);
        this.util.checkTableName(actual);
    }

    @Test
    public void testCheckTableNameForbiddenCharStar() {
        final TableNameUtil finalUtil = this.util;
        Assert.assertThrows("Table name should not contain []*?:/\\: a name with *",
                IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        finalUtil.checkTableName("a name with *");
                    }
                });
    }

    @Test
    public void testCheckTableNameForbiddenCharQuestionMark() {
        final TableNameUtil finalUtil = this.util;
        Assert.assertThrows("Table name should not contain []*?:/\\: a name with ?",
                IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        finalUtil.checkTableName("a name with ?");
                    }
                });
    }

    @Test
    public void testSanitizeTableNameForbiddenCharStar() {
        final String actual = this.util.sanitizeTableName("a name with *");
        Assert.assertEquals("a name with _", actual);
        this.util.checkTableName(actual);
    }

    @Test
    public void testCheckTableNameForbiddenCharColon() {
        final TableNameUtil finalUtil = this.util;
        Assert.assertThrows("Table name should not contain []*?:/\\: a name with :",
                IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        finalUtil.checkTableName("a name with :");
                    }
                });
    }

    @Test
    public void testSanitizeTableNameForbiddenCharColon() {
        final String actual = this.util.sanitizeTableName("a name with :");
        Assert.assertEquals("a name with _", actual);
        this.util.checkTableName(actual);
    }

    @Test
    public void testCheckTableNameForbiddenCharSlash() {
        final TableNameUtil finalUtil = this.util;
        Assert.assertThrows("Table name should not contain []*?:/\\: a name with /",
                IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        finalUtil.checkTableName("a name with /");
                    }
                });
    }

    @Test
    public void testSanitizeTableNameForbiddenCharSlash() {
        final String actual = this.util.sanitizeTableName("a name with /");
        Assert.assertEquals("a name with _", actual);
        this.util.checkTableName(actual);
    }

    @Test
    public void testCheckTableNameForbiddenCharBackSlash() {
        final TableNameUtil finalUtil = this.util;
        Assert.assertThrows("Table name should not contain []*?:/\\: a name with \\",
                IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        finalUtil.checkTableName("a name with \\");
                    }
                });
    }

    @Test
    public void testSanitizeTableNameForbiddenCharBackSlash() {
        final String actual = this.util.sanitizeTableName("a name with \\");
        Assert.assertEquals("a name with _", actual);
        this.util.checkTableName(actual);
    }

    @Test
    public void escapeTableNameOk() {
        Assert.assertEquals("no_problem", this.util.escapeTableName("no_problem"));
    }

    @Test
    public void testSanitizeTableNameNoProblem() {
        final String actual = this.util.sanitizeTableName("no problem");
        Assert.assertEquals("no problem", actual);
        this.util.checkTableName(actual);
    }


    @Test
    public void escapeTableNameSpace() {
        Assert.assertEquals("'a space'", this.util.escapeTableName("a space"));
    }

    @Test
    public void escapeTableNameQuote() {
        Assert.assertEquals("'a '' quote'", this.util.escapeTableName("a ' quote"));
    }

    @Test
    public void testSanitizeTableNameQute() {
        final String actual = this.util.sanitizeTableName("a ' quote");
        Assert.assertEquals("a ' quote", actual);
        this.util.checkTableName(actual);
    }

    @Test
    public void escapeTableNameTwoQuotes() {
        Assert.assertEquals("'two '' '' quotes'", this.util.escapeTableName("two ' ' quotes"));
    }

    @Test
    public void escapeTableNameBlank() {
        Assert.assertEquals("'a\nnewline'", this.util.escapeTableName("a\nnewline"));
    }

    @Test
    public void escapeTableNameDot() {
        Assert.assertEquals("'a.dot'", this.util.escapeTableName("a.dot"));
    }
}