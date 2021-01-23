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

package com.github.jferard.fastods.testlib;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class to test code points
 *
 * @author J. Férard
 */
public class CodePointTester {

    private static final int FIRST_ASCII_PRINTABLE_CHAR = 0x20;
    private static final int LAST_ASCII_PRINTABLE_CHAR = 0x80 - 1;

    /**
     * @param filter the filter to apply
     * @return the matching code points as a string
     */
    public static String codePointsAsString(final CategoryFilter filter) {
        final Collection<String> segments = new ArrayList<String>();
        int from = -1;
        int i;
        for (i = Character.MIN_CODE_POINT; i <= Character.MAX_CODE_POINT; i++) {
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
            return formatChar("'%c'", "#x%x", from);
        } else {
            return formatChar("[%c-", "[#x%x-", from) + formatChar("%c]", "#x%x]", to);
        }
    }

    private static String formatChar(final String printableFormat, final String nonPrintableFormat,
                                     final int from) {
        if (FIRST_ASCII_PRINTABLE_CHAR <= from && from <= LAST_ASCII_PRINTABLE_CHAR) {
            return String.format(printableFormat, from);
        } else {
            return String.format(nonPrintableFormat, from);
        }
    }

    /**
     * A filter
     */
    public interface CategoryFilter {
        /**
         * @param codePoint the code point to check
         * @return true if the code point matches
         */
        boolean check(int codePoint);
    }
}
