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

/**
 * A position (file - table - row - col)
 *
 * @author J. Férard
 */
public class Position {
    /**
     * The col is absolute
     */
    static final int ABSOLUTE_COL = 1;
    /**
     * The row is absolute
     */
    static final int ABSOLUTE_ROW = 2;
    /**
     * The table is absolute
     */
    static final int ABSOLUTE_TABLE = 4;

    private final EqualityUtil equalityUtil;
    private final int column;
    private final int status;
    private final TableNameUtil tableNameUtil;
    private final int row;
    private final String filename;
    private final String tableName;

    /**
     * Create a position
     *
     * @param equalityUtil  an util for testing equality
     * @param tableNameUtil an util for checking./escaping table name
     * @param fileName      the filename
     * @param tableName     the name of the table
     * @param row           the row
     * @param column        the column
     * @param status        the absolute status = col | row | table.
     */
    public Position(final EqualityUtil equalityUtil, final TableNameUtil tableNameUtil,
                    final String fileName, final String tableName, final int row, final int column,
                    final int status) {
        this.equalityUtil = equalityUtil;
        this.tableNameUtil = tableNameUtil;
        this.filename = fileName;
        this.tableName = tableName;
        this.row = row;
        this.column = column;
        this.status = status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }

        final Position other = (Position) o;
        return this.equalityUtil.equal(this.filename, other.filename) &&
                this.equalityUtil.equal(this.tableName, other.tableName) && this.row == other.row &&
                this.column == other.column && this.status == other.status;
    }

    @Override
    public int hashCode() {
        return this.equalityUtil
                .hashObjects(this.filename, this.tableName, this.row, this.column, this.status);
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the address of a cell, in Excel/OO/LO format. Some examples:
     * <ul>
     * <li>0 gives "A"</li>
     * <li>...</li>
     * <li>25 gives "Z"</li>
     * <li>26 gives "AA"</li>
     * <li>27 gives "AB"</li>
     * <li>...</li>
     * <li>52 gives "BA"</li>
     * <li>1023 gives "AMJ"</li>
     * </ul>
     * <p>
     * Remainder: '<filename>'#<tablename>.<col><row>
     *
     * @return the Excel/OO/LO address
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        this.appendFilename(sb);
        this.appendTableName(sb);
        this.appendRowColAddress(sb);
        return sb.toString();
    }

    private void appendFilename(final StringBuilder sb) {
        if (this.filename != null) {
            sb.append((char) PositionParser.SINGLE_QUOTE);
            for (int i = 0; i < this.filename.length(); i++) {
                final char c = this.filename.charAt(i);
                if (c == PositionParser.SINGLE_QUOTE) {
                    sb.append((char) PositionParser.SINGLE_QUOTE);
                }
                sb.append(c);
            }
            sb.append("'#");
        }
    }

    private void appendTableName(final StringBuilder sb) {
        if (this.tableName != null) {
            if ((this.status & Position.ABSOLUTE_TABLE) == Position.ABSOLUTE_TABLE) {
                sb.append(this.tableNameUtil.escapeTableName('$' + this.tableName));
            } else {
                sb.append(this.tableNameUtil.escapeTableName(this.tableName));
            }
            sb.append('.');
        }
    }

    private void appendRowColAddress(final StringBuilder sb) {
        final StringBuilder tempSb = this.getColStringBuilder();
        if ((this.status & Position.ABSOLUTE_COL) == Position.ABSOLUTE_COL) {
            sb.append('$');
        }
        sb.append(tempSb);
        if ((this.status & Position.ABSOLUTE_ROW) == Position.ABSOLUTE_ROW) {
            sb.append('$');
        }
        sb.append(this.row + 1);
    }

    private StringBuilder getColStringBuilder() {
        final StringBuilder tempSb = new StringBuilder();
        int col = this.column;
        while (col >= PositionUtil.ALPHABET_SIZE) {
            tempSb.insert(0, (char) (PositionUtil.ORD_A + (col % PositionUtil.ALPHABET_SIZE)));
            col = col / PositionUtil.ALPHABET_SIZE - 1;
        }
        tempSb.insert(0, (char) (PositionUtil.ORD_A + col));
        return tempSb;
    }

}
