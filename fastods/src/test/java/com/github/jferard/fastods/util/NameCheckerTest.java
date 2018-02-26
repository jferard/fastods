/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.FastOdsException;
import com.google.common.base.Joiner;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NameCheckerTest {

    @Test
    public void testNameStartChar() {
        final NameChecker checker = new NameChecker();
        Assert.assertEquals(
                "'-' | ':' | [A-Z] | [a-z] | [#xc0-#xd6] | [#xd8-#xf6] | [#xf8-#x2ff] | " + "[#x370-#x37d] | " +
                        "[#x37f-#x1fff] | [#x200c-#x200d] | [#x2070-#x218f] | [#x2c00-#x2fef] " + "" + "" + "| " +
                        "[#x3001-#xd7ff] | " + "[#xf900-#xfdcf] | [#xfdf0-#xfffd] | [#x10000-#xeffff]",
                this.filter(new CategoryFilter() {
                    @Override
                    public boolean ok(final int codePoint) {
                        return checker.isNameStartChar(codePoint);
                    }
                }));
    }

    @Test
    public void testNameChar() {
        final NameChecker checker = new NameChecker();
        Assert.assertEquals(
                "[--.] | [0-:] | [A-Z] | '_' | [a-z] | #xb7 | [#xc0-#xd6] | " + "[#xd8-#xf6] | [#xf8-#x37d] | " +
                        "[#x37f-#x1fff] | [#x200c-#x200d] | [#x203f-#x2040] | " + "[#x2070-#x218f] | [#x2c00-#x2fef] " +
                        "| [#x3001-#xd7ff] | [#xf900-#xfdcf] | [#xfdf0-#xfffd] | " + "[#x10000-#xeffff]",
                this.filter(new CategoryFilter() {
                    @Override
                    public boolean ok(final int codePoint) {
                        return checker.isNameChar(codePoint);
                    }
                }));
    }

    @Test
    public void testNCNameStartChar() {
        final NameChecker checker = new NameChecker();
        Assert.assertEquals(
                "'-' | [A-Z] | [a-z] | [#xc0-#xd6] | [#xd8-#xf6] | [#xf8-#x2ff] | " + "[#x370-#x37d] | " +
                        "[#x37f-#x1fff] | [#x200c-#x200d] | [#x2070-#x218f] | [#x2c00-#x2fef] " + "" + "" + "| " +
                        "[#x3001-#xd7ff] | " + "[#xf900-#xfdcf] | [#xfdf0-#xfffd] | [#x10000-#xeffff]",
                this.filter(new CategoryFilter() {
                    @Override
                    public boolean ok(final int codePoint) {
                        return checker.isNCNameStartChar(codePoint);
                    }
                }));
    }

    @Test
    public void testNCNameChar() {
        final NameChecker checker = new NameChecker();
        Assert.assertEquals(
                "[--.] | [0-9] | [A-Z] | '_' | [a-z] | #xb7 | [#xc0-#xd6] | " + "[#xd8-#xf6] | [#xf8-#x37d] | " +
                        "[#x37f-#x1fff] | [#x200c-#x200d] | [#x203f-#x2040] | " + "[#x2070-#x218f] | [#x2c00-#x2fef] " +
                        "| [#x3001-#xd7ff] | [#xf900-#xfdcf] | [#xfdf0-#xfffd] | " + "[#x10000-#xeffff]",
                this.filter(new CategoryFilter() {
                    @Override
                    public boolean ok(final int codePoint) {
                        return checker.isNCNameChar(codePoint);
                    }
                }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongChildCellTest() throws FastOdsException {
        final NameChecker checker = new NameChecker();
        checker.checkStyleName("style@@datastyle");
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongChildCellTest2() throws FastOdsException {
        final NameChecker checker = new NameChecker();
        checker.checkStyleName("style-:-datastyle");
    }

    @Test
    public void correctChildCellTest() throws FastOdsException {
        final NameChecker checker = new NameChecker();
        checker.checkStyleName("style-_-datastyle");
    }

    @Test(expected = IllegalArgumentException.class)
    public void digitTest() throws FastOdsException {
        final NameChecker checker = new NameChecker();
        checker.checkStyleName("0style-:-datastyle");
    }

    private String filter(final CategoryFilter filter) {
        final List<String> segments = new ArrayList<String>();
        int from = -1;
        int i;
        for (i = 0; i < 0xfffff; i++) {
            if (filter.ok(i)) {
                if (from == -1) from = i;
            } else {
                if (from >= 0) {
                    segments.add(this.format(from, i - 1));
                    from = -1;
                }
            }
        }
        if (from >= 0) segments.add(this.format(from, i));
        return Joiner.on(" | ").join(segments);
    }

    private String format(final int from, final int to) {
        if (from == to) {
            if (from < 0x80) return String.format("'%c'", from);
            else return String.format("#x%x", from);
        } else {
            if (to <= 0x80) return String.format("[%c-%c]", from, to);
            else return String.format("[#x%x-#x%x]", from, to);
        }
    }

    private void printRange(final int from, final int to) {
        final Set<Integer> types = new HashSet<Integer>();
        for (int cp = from; cp <= to; cp++) {
            types.add(Character.getType(cp));
        }
        System.out.println(types);
    }

    private interface CategoryFilter {
        boolean ok(int codePoint);
    }
}