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

import java.text.ParseException;
import java.util.Locale;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
class PositionParser {
    /**
     * a..z -> 26 letters
     */
    public static final int ALPHABET_SIZE = 26;
    /**
     * the base for computing
     */
    public static final int ORD_A = 'A';
    public static final int SINGLE_QUOTE = '\'';
    public static final int HASH = '#';
    private static final int BEGIN_ROW = 3;
    private static final int BEGIN_COL = 0;
    private static final int FIRST_DIGIT = 4;
    private static final int FIRST_LETTER = 1;
    private static final int OPT_DIGIT = 5;
    private static final int OPT_SECOND_LETTER = 2;
    private static final int DOT = '.';

    private final EqualityUtil equalityUtil;
    private final TableNameUtil tableNameUtil;
    private final String address;
    private final int length;
    private int cur;
    private int status;
    private String tableName;
    private String filename;
    private int row;
    private int col;

    /**
     * Create a new position util
     *
     * @param address ['<filename>'#][<tablename>.]<col><row>
     * @return
     */
    public PositionParser(final EqualityUtil equalityUtil, final TableNameUtil tableNameUtil,
                          final String address) {
        this.equalityUtil = equalityUtil;
        this.tableNameUtil = tableNameUtil;
        this.address = address;

        this.row = 0;
        this.col = 0;
        this.cur = 0;
        this.length = address.length();
    }

    /**
     *
     */
    public Position parse() throws ParseException {
        this.parseFilename();
        this.parseTableName();
        this.parseColRow();

        return new Position(this.equalityUtil, this.tableNameUtil, this.filename,
                this.tableName, this.row - 1, this.col - 1, this.status);
    }

    private void parseFilename() throws ParseException {
        final int i = this.address.indexOf(PositionParser.HASH);
        if (i == -1) {
            return;
        }
        final String escapedFilename = this.address.substring(this.cur, i);
        this.filename = this.unescape(escapedFilename);
        this.cur = i + 1;
    }

    private String unescape(final String str) throws ParseException {
        final int length = str.length();
        if (str.charAt(0) == PositionParser.SINGLE_QUOTE &&
                str.charAt(length - 1) == PositionParser.SINGLE_QUOTE) {
            return this.unescapeEscaped(str.substring(1, length - 1));
        } else {
            final int i = str.indexOf(PositionParser.SINGLE_QUOTE);
            if (i != -1) {
                throw new ParseException("Unquoted filename " + str, this.cur + i);
            }
            return str;
        }
    }

    private String unescapeEscaped(final String escaped) throws ParseException {
        final StringBuilder sb = new StringBuilder();
        int quotes = 0;
        for (int i = 0; i < escaped.length(); i++) {
            final char c = escaped.charAt(i);
            if (c == PositionParser.SINGLE_QUOTE) {
                quotes++;
                if (quotes == 2) {
                    quotes = 0;
                    sb.append(c);
                }
            } else {
                if (quotes == 1) {
                    throw new ParseException("Missing closing quote in filename " + escaped,
                            this.cur + i);
                }
                sb.append(c);
            }
        }
        if (quotes != 0) {
            throw new ParseException("Random quote in filename", this.cur);
        }
        return sb.toString();
    }

    private void parseTableName() throws ParseException {
        final int i = this.address.indexOf(PositionParser.DOT, this.cur);
        if (i == -1) {
            return;
        }

        final String escapedTableName = this.address.substring(this.cur, i);
        final String tableName = this.unescape(escapedTableName);
        this.cur = i + 1;
        if (tableName.charAt(0) == '$') {
            this.status = Position.ABSOLUTE_TABLE;
            this.tableName = tableName.substring(1);
        } else {
            this.tableName = tableName;
        }
    }

    /**
     * Convert a cell position string like B3 to the column number.
     *
     * @return The row, e.g. A1 will return 0, B1 will return 1, E1 will return
     * 4
     */
    private void parseColRow() throws ParseException {
        final String s = this.address.substring(this.cur).toUpperCase(Locale.US);
        final int len = s.length();
        int state = BEGIN_COL;
        int n = 0;
        while (n < len) {
            final char c = s.charAt(n);
            switch (state) {
                case BEGIN_COL: // check for opt $
                    if (c == '$') {
                        this.status += Position.ABSOLUTE_COL;
                        n++;
                    }
                    state = FIRST_LETTER;
                    break;
                case BEGIN_ROW: // check for opt $
                    state++;
                    if (c == '$') {
                        this.status += Position.ABSOLUTE_ROW;
                        n++;
                    }
                    state = FIRST_DIGIT;
                    break;
                case FIRST_LETTER: // mandatory letter
                    if ('A' <= c && c <= 'Z') {
                        this.col = c - 'A' + 1;
                        state = PositionParser.OPT_SECOND_LETTER;
                        n++;
                    } else {
                        throw new ParseException("Expected letter", this.cur + n);
                    }
                    break;
                case OPT_SECOND_LETTER: // opt letter
                    if ('A' <= c && c <= 'Z') {
                        this.col = this.col * ALPHABET_SIZE + c - ORD_A + 1;
                        n++;
                    } else {
                        state = PositionParser.BEGIN_ROW;
                    }
                    break;
                case FIRST_DIGIT: // mand digit
                    if ('0' <= c && c <= '9') {
                        this.row = c - '0';
                        state = PositionParser.OPT_DIGIT;
                        n++;
                    } else {
                        throw new ParseException("Expected digit", this.cur + n);
                    }
                    break;
                case OPT_DIGIT: // opt digit
                    if ('0' <= c && c <= '9') {
                        this.row = this.row * 10 + c - '0';
                        n++;
                    } else {
                        throw new ParseException("Expected digit", this.cur + n);
                    }
                    break;
                default:
                    throw new ParseException("Unexpected error", this.cur + n);
            }
        }
        if (state != OPT_DIGIT) {
            throw new ParseException("Unexpected error", this.cur + n);
        }
    }
}