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

import com.github.jferard.fastods.util.EqualityUtil;

import java.io.IOException;

/**
 * A reference to a table
 *
 * @author J. Férard
 */
public class TableRef {

    /**
     * @param tableNameUtil util for check/escaping the table name
     * @return a builder
     */
    public static TableRefBuilder builder(final TableNameUtil tableNameUtil) {
        return new TableRefBuilder(tableNameUtil);
    }

    private final TableNameUtil tableNameUtil;
    private final String filename;
    private final String tableName;
    private final int status;

    /**
     * @param tableNameUtil an util for table names
     * @param filename      filename or null
     * @param tableName     table name
     * @param status        the status
     */
    public TableRef(final TableNameUtil tableNameUtil, final String filename,
                    final String tableName, final int status) {
        this.tableNameUtil = tableNameUtil;
        this.filename = filename;
        this.tableName = tableName;
        this.status = status;
    }

    private void appendFilename(final Appendable appendable) throws IOException {
        if (this.filename == null) {
            return;
        }

        appendable.append((char) TableAddressParser.SINGLE_QUOTE);
        for (int i = 0; i < this.filename.length(); i++) {
            final char c = this.filename.charAt(i);
            if (c == TableAddressParser.SINGLE_QUOTE) {
                appendable.append((char) TableAddressParser.SINGLE_QUOTE);
            }
            appendable.append(c);
        }
        appendable.append("'#");
    }

    private void appendTableName(final Appendable appendable) throws IOException {
        if ((this.status & CellRef.ABSOLUTE_TABLE) == CellRef.ABSOLUTE_TABLE) {
            appendable.append('$');
        }
        appendable.append(this.tableNameUtil.escapeTableName(this.tableName));
    }

    /**
     * @param appendable the appendable where to write
     * @throws IOException never
     */
    public void write(final Appendable appendable) throws IOException {
        this.appendFilename(appendable);
        this.appendTableName(appendable);
    }

    /**
     * Returns the address of a table in Excel/OO/LO format. Some examples:
     * Remainder: 'file://D:/file.ods'#table
     *
     * @return the Excel/OO/LO address
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        try {
            this.write(sb);
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TableRef)) {
            return false;
        }

        final TableRef other = (TableRef) o;
        return EqualityUtil.equal(this.filename, other.filename) &&
                EqualityUtil.equal(this.tableName, other.tableName) && this.status == other.status;
    }

    @Override
    public int hashCode() {
        return EqualityUtil.hashObjects(this.filename, this.tableName, this.status);
    }

}
