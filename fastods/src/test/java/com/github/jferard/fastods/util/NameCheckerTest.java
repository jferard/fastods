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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.testlib.CodePointTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NameCheckerTest {
    private NameChecker checker;

    @Before
    public void setUp() {
        this.checker = new NameChecker();
    }

    @Test
    public void testNameStartChar() {
        Assert.assertEquals(
                "'-' | ':' | [A-Z] | [a-z] | [#xc0-#xd6] | [#xd8-#xf6] | [#xf8-#x2ff] | " +
                        "[#x370-#x37d] | " +
                        "[#x37f-#x1fff] | [#x200c-#x200d] | [#x2070-#x218f] | [#x2c00-#x2fef] " +
                        "" + "" + "| " + "[#x3001-#xd7ff] | " +
                        "[#xf900-#xfdcf] | [#xfdf0-#xfffd] | [#x10000-#xeffff]",
                CodePointTester.codePointsAsString(new CodePointTester.CategoryFilter() {
                    @Override
                    public boolean check(final int codePoint) {
                        return NameCheckerTest.this.checker.isNameStartChar(codePoint);
                    }
                }));
    }

    @Test
    public void testNameChar() {
        Assert.assertEquals("[--.] | [0-:] | [A-Z] | '_' | [a-z] | #xb7 | [#xc0-#xd6] | " +
                        "[#xd8-#xf6] | [#xf8-#x37d] | " +
                        "[#x37f-#x1fff] | [#x200c-#x200d] | [#x203f-#x2040] | " +
                        "[#x2070-#x218f] | [#x2c00-#x2fef] " +
                        "| [#x3001-#xd7ff] | [#xf900-#xfdcf] | [#xfdf0-#xfffd] | " + "[#x10000" +
                        "-#xeffff]",
                CodePointTester.codePointsAsString(new CodePointTester.CategoryFilter() {
                    @Override
                    public boolean check(final int codePoint) {
                        return NameCheckerTest.this.checker.isNameChar(codePoint);
                    }
                }));
    }

    @Test
    public void testNCNameStartChar() {
        Assert.assertEquals("'-' | [A-Z] | [a-z] | [#xc0-#xd6] | [#xd8-#xf6] | [#xf8-#x2ff] | " +
                        "[#x370-#x37d] | " +
                        "[#x37f-#x1fff] | [#x200c-#x200d] | [#x2070-#x218f] | [#x2c00-#x2fef] " + "" + "" +
                        "| " + "[#x3001-#xd7ff] | " +
                        "[#xf900-#xfdcf] | [#xfdf0-#xfffd] | [#x10000-#xeffff]",
                CodePointTester.codePointsAsString(new CodePointTester.CategoryFilter() {
                    @Override
                    public boolean check(final int codePoint) {
                        return NameCheckerTest.this.checker.isNCNameStartChar(codePoint);
                    }
                }));
    }

    @Test
    public void testNCNameChar() {
        Assert.assertEquals("[--.] | [0-9] | [A-Z] | '_' | [a-z] | #xb7 | [#xc0-#xd6] | " +
                        "[#xd8-#xf6] | [#xf8-#x37d] | " +
                        "[#x37f-#x1fff] | [#x200c-#x200d] | [#x203f-#x2040] | " +
                        "[#x2070-#x218f] | [#x2c00-#x2fef] " +
                        "| [#x3001-#xd7ff] | [#xf900-#xfdcf] | [#xfdf0-#xfffd] | " + "[#x10000" +
                        "-#xeffff]",
                CodePointTester.codePointsAsString(new CodePointTester.CategoryFilter() {
                    @Override
                    public boolean check(final int codePoint) {
                        return NameCheckerTest.this.checker.isNCNameChar(codePoint);
                    }
                }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongChildCellTest() {
        this.checker.checkStyleName("style@@datastyle");
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongChildCellTest2() {
        this.checker.checkStyleName("style-:-datastyle");
    }

    @Test
    public void correctChildCellTest() {
        this.checker.checkStyleName("style-_-datastyle");
    }

    @Test(expected = IllegalArgumentException.class)
    public void digitTest() {
        this.checker.checkStyleName("0style-:-datastyle");
    }
}