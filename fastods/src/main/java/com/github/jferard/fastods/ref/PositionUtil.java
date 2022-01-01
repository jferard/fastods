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

import com.github.jferard.fastods.Table;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class PositionUtil {
    /**
     * a..z: 26 letters
     */
    public static final int ALPHABET_SIZE = 26;
    /**
     * the base for computing
     */
    public static final int ORD_A = 'A';

    /**
     * @return a new position util
     */
    public static PositionUtil create() {
        return new PositionUtil(new TableNameUtil());
    }

    private final TableNameUtil tableNameUtil;

    /**
     * Create a new position util
     *
     * @param tableNameUtil an util to check table names
     */
    public PositionUtil(final TableNameUtil tableNameUtil) {
        this.tableNameUtil = tableNameUtil;
    }

    /**
     * Convert a cell position string like B3 to the column number.
     *
     * @param address The cell position in the range 'A1' to 'IV65536'
     * @return The row, e.g. A1 will return 0, B1 will return 1, E1 will return
     * 4
     * @throws ParseException If the address can't be parsed.
     * @throws IOException    never
     */
    public CellRef newCellRef(final String address) throws ParseException, IOException {
        return CellAddressParser.create(this.tableNameUtil).parse(address);
    }

    /**
     * @param row the row
     * @param col the col
     * @return the position
     */
    public CellRef newCellRef(final int row, final int col) {
        return new CellRef(null, new LocalCellRef(row, col, 0));
    }

    /**
     * @param table the table
     * @param row   the row
     * @param col   the col
     * @return the position
     */
    public CellRef newCellRef(final Table table, final int row, final int col) {
        final TableRef tableRef = new TableRef(this.tableNameUtil, null, table.getName(), 0);
        return new CellRef(tableRef, new LocalCellRef(row, col, 0));
    }

    /**
     * Return the Excel/OO/LO address of a cell
     *
     * @param row the row
     * @param col the col
     * @return the Excel/OO/LO address
     */
    public String toCellAddress(final int row, final int col) {
        return this.newCellRef(row, col).toString();
    }

    /**
     * the Excel/OO/LO address of a cell, preceded by the table name
     *
     * @param row   the row
     * @param col   the col
     * @param table the table
     * @return the Excel/OO/LO address
     */
    public String toCellAddress(final Table table, final int row, final int col) {
        return this.newCellRef(table, row, col).toString();
    }

    /**
     * Return the Excel/OO/LO address of a range
     *
     * @param row1 the first row
     * @param col1 the first col
     * @param row2 the last row
     * @param col2 the last col
     * @return the Excel/OO/LO address
     */
    public String toRangeAddress(final int row1, final int col1, final int row2, final int col2) {
        return this.toCellAddress(row1, col1) + ":" + this.toCellAddress(row2, col2);
    }

    /**
     * Return the Excel/OO/LO address of a range, preceded by the table name
     *
     * @param row1  the first row
     * @param col1  the first col
     * @param row2  the last row
     * @param col2  the last col
     * @param table the table
     * @return the Excel/OO/LO address
     */
    public String toRangeAddress(final Table table, final int row1, final int col1, final int row2,
                                 final int col2) {
        return this.toCellAddress(table, row1, col1) + ":" + this.toCellAddress(table, row2, col2);
    }

    /**
     * Check if the table name is ok. Currently, this does stick to LO limitations (excepted for
     * the check of duplicate names), but may the conditions may be relaxed in a future version.
     *
     * A valid table name should not start with a single quote (`'`) or contain of of the following
     * characters: left square bracket (`[`), right square bracket (`]`), star (`*`), question
     * mark (`?`), colon (`:`), slash (`/`) or backslash (`\`).
     *
     * @param name the name to check
     * @throws IllegalArgumentException if the table name is not accepted.
     */
    public void checkTableName(final CharSequence name) {
        this.tableNameUtil.checkTableName(name);
    }
}