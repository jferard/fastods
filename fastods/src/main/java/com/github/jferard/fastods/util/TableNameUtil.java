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

import com.github.jferard.fastods.Table;

import java.util.Arrays;

/**
 * A TableNameUtil checks the table name and escapes it.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableNameUtil {
    public static final char SINGLE_QUOTE = '\'';
    public static final char[] FORBIDDEN_CHARS = "[]*?:/\\".toCharArray();

    /**
     * Check if the table name is ok. Currently, this does stick to LO limitations (excepted for
     * the check of duplicate names), but may the condtions may be relaxed in a future version.
     *
     * @param name the name to check
     */
    public void checkTableName(final String name) {
        if (name.charAt(0) == SINGLE_QUOTE) {
            throw new IllegalArgumentException(
                    "Table name should not start with " + SINGLE_QUOTE + ": " + name);
        }
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            for (final char fc : FORBIDDEN_CHARS) {
                if (c == fc) {
                    throw new IllegalArgumentException(
                            "Table name should not contain " + new String(FORBIDDEN_CHARS) +
                                    ": " + name);
                }
            }
        }
    }

    /**
     * 9.2.1 Referencing Table Cells
     *
     * @param tableName the name of the table
     * @return the name of the table escaped
     */
    public String escapeTableName(final String tableName) {
        // step 1: to quote or no? how many single quotes?
        boolean toQuote = false;
        int apostropheCount = 0;
        for (int i = 0; i < tableName.length(); i++) {
            final char c = tableName.charAt(i);
            if (Character.isWhitespace(c) || c == '.') {
                toQuote = true;
            } else if (c == '\'') {
                toQuote = true;
                apostropheCount += 1;
            }
        }

        final StringBuilder sb = new StringBuilder(
                tableName.length() + (toQuote ? 2 : 0) + apostropheCount);

        // step 2: build the string
        if (toQuote) {
            sb.append(SINGLE_QUOTE);
        }
        for (int i = 0; i < tableName.length(); i++) {
            final char c = tableName.charAt(i);
            sb.append(c);
            if (c == '\'') {
                sb.append(SINGLE_QUOTE);
            }
        }
        if (toQuote) {
            sb.append(SINGLE_QUOTE);
        }
        return sb.toString();
    }
}
