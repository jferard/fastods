/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

import java.io.File;

/**
 * A position builder: file + table + row/col
 *
 * @author J. Férard
 */
public class TableRefBuilder {
    private final TableNameUtil tableNameUtil;
    private int status;
    private String tableName;
    private String filename;

    /**
     * Create a position
     *
     * @param tableNameUtil util for check/escaping the table name
     */
    TableRefBuilder(final TableNameUtil tableNameUtil) {
        this.tableNameUtil = tableNameUtil;
        this.filename = null;
        this.status = 0;
    }

    /**
     * Set the table name absolute
     *
     * @param tableName the name of the table
     * @return this for fluent style
     */
    public TableRefBuilder absTable(final String tableName) {
        this.tableName = tableName;
        this.status |= TableRef.ABSOLUTE_TABLE;
        return this;
    }

    /**
     * Set the table name relative
     *
     * @param tableName the name of the table
     * @return this for fluent style
     */
    public TableRefBuilder table(final String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * Set the table name
     *
     * @param table the table
     * @return this for fluent style
     */
    public TableRefBuilder table(final Table table) {
        this.tableName = table.getName();
        return this;
    }

    /**
     * Set the table
     *
     * @param table the table
     * @return this for fluent style
     */
    public TableRefBuilder absTable(final Table table) {
        this.tableName = table.getName();
        this.status |= TableRef.ABSOLUTE_TABLE;
        return this;
    }


    /**
     * Set the file name
     *
     * @param filename the name of the file
     * @return this for fluent style
     */
    public TableRefBuilder file(final String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * Set the file name
     *
     * @param file the name of the file
     * @return this for fluent style
     */
    public TableRefBuilder file(final File file) {
        this.filename = file.getPath();
        return this;
    }

    /**
     * @return the table ref or null
     */
    public TableRef build() {
        if (this.tableName == null) {
            return null;
        } else {
            return new TableRef(this.tableNameUtil, this.filename, this.tableName, this.status);
        }
    }
}
