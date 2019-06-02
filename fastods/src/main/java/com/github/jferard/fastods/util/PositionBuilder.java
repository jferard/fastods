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
 * A position (row + col)
 */
public class PositionBuilder {
    private final int column;
    private final EqualityUtil equalityUtil;
    private final TableNameUtil tableNameUtil;
    private final int row;
    private int status;
    private String tableName;
    private String fileName;

    /**
     * Create a position
     *
     * @param equalityUtil
     * @param tableNameUtil
     * @param row           the row
     * @param column        the column
     */
    PositionBuilder(final EqualityUtil equalityUtil, final TableNameUtil tableNameUtil,
                    final int row, final int column) {
        this.equalityUtil = equalityUtil;
        this.tableNameUtil = tableNameUtil;
        this.fileName = null;
        this.tableName = null;
        this.row = row;
        this.column = column;
        this.status = 0;
    }

    /**
     * Set the row absolute
     *
     * @return this for fluent style
     */
    public PositionBuilder absRow() {
        this.status += Position.ABSOLUTE_ROW;
        return this;
    }

    /**
     * Set the column absolute
     *
     * @return this for fluent style
     */
    public PositionBuilder absCol() {
        this.status += Position.ABSOLUTE_COL;
        return this;
    }

    /**
     * Set the table name
     *
     * @return this for fluent style
     */
    public PositionBuilder table(final String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * Set the table name
     *
     * @return this for fluent style
     */
    public PositionBuilder absTable(final String tableName) {
        this.tableName = tableName;
        this.status += Position.ABSOLUTE_TABLE;
        return this;
    }

    /**
     * Set the table name
     *
     * @return this for fluent style
     */
    public PositionBuilder file(final String fileName) {
        this.fileName = fileName;
        return this;
    }

    public Position build() {
        return new Position(this.equalityUtil, this.tableNameUtil, this.fileName, this.tableName,
                this.row, this.column, this.status);
    }
}
