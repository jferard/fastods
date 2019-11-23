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

package com.github.jferard.fastods.ref;

import com.github.jferard.fastods.Table;

import java.io.File;

/**
 * A position builder: file + table + r/col
 *
 * @author J. Férard
 */
public class RangeRefBuilder {
    private final TableRefBuilder tableRefBuilder;
    private final LocalCellRefBuilder fromLocalCellRefBuilder;
    private final LocalCellRefBuilder toLocalCellRefBuilder;

    /**
     * Create a position
     *
     * @param tableNameUtil util for check/escaping the table name
     */
    RangeRefBuilder(final TableNameUtil tableNameUtil) {
        this.tableRefBuilder = TableRef.builder(tableNameUtil);
        this.fromLocalCellRefBuilder = LocalCellRef.builder();
        this.toLocalCellRefBuilder = LocalCellRef.builder();
    }

    /**
     * Set the r
     *
     * @param r the row index
     * @return this for fluent style
     */
    public RangeRefBuilder fromRow(final int r) {
        this.fromLocalCellRefBuilder.row(r);
        return this;
    }

    /**
     * Set the r absolute
     *
     * @param r the row index
     * @return this for fluent style
     */
    public RangeRefBuilder fromAbsRow(final int r) {
        this.fromLocalCellRefBuilder.absRow(r);
        return this;
    }

    /**
     * Set the column
     *
     * @param c the column index
     * @return this for fluent style
     */
    public RangeRefBuilder fromColumn(final int c) {
        this.fromLocalCellRefBuilder.column(c);
        return this;
    }

    /**
     * Set the column absolute
     *
     * @param c the column index
     * @return this for fluent style
     */
    public RangeRefBuilder fromAbsColumn(final int c) {
        this.fromLocalCellRefBuilder.absColumn(c);
        return this;
    }

    /**
     * Set the r
     *
     * @param r the row index
     * @return this for fluent style
     */
    public RangeRefBuilder toRow(final int r) {
        this.toLocalCellRefBuilder.row(r);
        return this;
    }

    /**
     * Set the r absolute
     *
     * @param r the row index
     * @return this for fluent style
     */
    public RangeRefBuilder toAbsRow(final int r) {
        this.toLocalCellRefBuilder.absRow(r);
        return this;
    }

    /**
     * Set the column
     *
     * @param c the column index
     * @return this for fluent style
     */
    public RangeRefBuilder toColumn(final int c) {
        this.toLocalCellRefBuilder.column(c);
        return this;
    }

    /**
     * Set the column absolute
     *
     * @param c the column index
     * @return this for fluent style
     */
    public RangeRefBuilder toAbsColumn(final int c) {
        this.toLocalCellRefBuilder.absColumn(c);
        return this;
    }


    /**
     * Set the table name
     *
     * @param tableName the name of the table
     * @return this for fluent style
     */
    public RangeRefBuilder table(final String tableName) {
        this.tableRefBuilder.table(tableName);
        return this;
    }

    /**
     * Set the table name
     *
     * @param tableName the name of the table
     * @return this for fluent style
     */
    public RangeRefBuilder absTable(final String tableName) {
        this.tableRefBuilder.absTable(tableName);
        return this;
    }

    /**
     * Set the table name
     *
     * @param table the table
     * @return this for fluent style
     */
    public RangeRefBuilder table(final Table table) {
        this.tableRefBuilder.table(table);
        return this;
    }

    /**
     * Set the table
     *
     * @param table the table
     * @return this for fluent style
     */
    public RangeRefBuilder absTable(final Table table) {
        this.tableRefBuilder.absTable(table);
        return this;
    }


    /**
     * Set the file name
     *
     * @param fileName the name of the file
     * @return this for fluent style
     */
    public RangeRefBuilder file(final String fileName) {
        this.tableRefBuilder.file(fileName);
        return this;
    }

    /**
     * Set the file
     *
     * @param file the file
     * @return this for fluent style
     */
    public RangeRefBuilder file(final File file) {
        this.tableRefBuilder.file(file);
        return this;
    }

    /**
     * @return the position or null
     */
    public RangeRef build() {
        return new RangeRef(this.tableRefBuilder.build(), this.fromLocalCellRefBuilder.build(),
                toLocalCellRefBuilder.build());
    }
}
