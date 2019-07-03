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

package com.github.jferard.fastods.testlib;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class to test code points
 */
public class CodePointTester {
    /**
     * @param filter the filter to apply
     * @return the matching codepoints as a string
     */
    public static String codePointsAsString(final CategoryFilter filter) {
        final Collection<String> segments = new ArrayList<String>();
        int from = -1;
        int i;
        for (i = 0; i < 0xfffff; i++) {
            if (filter.check(i)) {
                if (from == -1) {
                    from = i;
                }
            } else {
                if (from >= 0) {
                    segments.add(format(from, i - 1));
                    from = -1;
                }
            }
        }
        if (from >= 0) {
            segments.add(format(from, i));
        }
        return Joiner.on(" | ").join(segments);
    }

    private static String format(final int from, final int to) {
        if (from == to) {
            if (0x20 < from && from < 0x80) {
                return String.format("'%c'", from);
            } else {
                return String.format("#x%x", from);
            }
        } else {
            String s = "";
            if (0x20 <= from && from < 0x80) {
                s += String.format("[%c-", from);
            } else {
                s += String.format("[#x%x-", from);
            }
            if (0x20 <= to && to < 0x80) {
                return s + String.format("%c]", to);
            } else {
                return s + String.format("#x%x]", to);
            }
        }
    }

    public interface CategoryFilter {
        boolean check(int codePoint);
    }
}
