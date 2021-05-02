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

import java.text.ParseException;

/**
 * Converts a cell address to a position
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class LocalCellAddressParser {
    private static final int BEGIN_COL = 0;
    private static final int OPT_LETTER = 1;
    private static final int BEGIN_ROW = 2;
    private static final int OPT_DIGIT = 3;

    /**
     * @param address ['<filename>'#][<tablename>.]<col><row>
     * @return a new position
     * @throws ParseException If the address can't be parsed.
     */
    public LocalCellRef parse(final String address) throws ParseException {
        int status = 0;
        int col = 0;
        int row = 0;
        int state = BEGIN_COL;
        for (int i = 0; i < address.length(); i++) {
            final char c = address.charAt(i);
            switch (state) {
                case BEGIN_COL: // check for opt $
                    if (c == LocalCellRef.ABS_SIGN) {
                        status |= CellRef.ABSOLUTE_COL;
                    } else if ('A' <= c && c <= 'Z') {
                        col = c - 'A' + 1;
                        state = LocalCellAddressParser.OPT_LETTER;
                    } else {
                        throw this.parseException("Expected letter or $", address, i, c);
                    }

                    break;
                case BEGIN_ROW: // check for opt $
                    if (c == LocalCellRef.ABS_SIGN) {
                        status |= CellRef.ABSOLUTE_ROW;
                    } else if ('1' <= c && c <= '9') {
                        row = c - '0';
                        state = LocalCellAddressParser.OPT_DIGIT;
                    } else {
                        throw this.parseException("Expected digit (not 0) or $", address, i, c);
                    }
                    break;
                case OPT_LETTER: // opt letter
                    if ('A' <= c && c <= 'Z') {
                        col = col * PositionUtil.ALPHABET_SIZE + c - PositionUtil.ORD_A + 1;
                    } else {
                        state = LocalCellAddressParser.BEGIN_ROW;
                        i--;
                        continue;
                    }
                    break;
                case OPT_DIGIT: // opt digit
                    if ('0' <= c && c <= '9') {
                        row = row * 10 + c - '0';
                    } else {
                        throw this.parseException("Expected digit", address, i, c);
                    }
                    break;
                default:
                    throw this.parseException("Unexpected error", address, i, c);
            }
        }
        if (state != OPT_DIGIT) {
            throw new ParseException(
                    String.format("Address too short, expected digit: %s[]", address),
                    address.length());
        }
        return new LocalCellRef(row - 1, col - 1, status);
    }

    private ParseException parseException(final String text, final String address, final int i,
                                          final char c) {
        return new ParseException(String.format("%s: %s[%c]%s", text, address.substring(0, i), c,
                address.substring(i + 1)), i);
    }
}