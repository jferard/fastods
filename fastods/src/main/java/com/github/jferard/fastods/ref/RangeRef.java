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
 * A position (file - table - from(row - col) - to(row - col))
 *
 * @author J. Férard
 */
public class RangeRef {
    public static final char RANGE_SEP = ':';

    private final TableRef tableRef;
    private final LocalCellRef fromCellRef;
    private final LocalCellRef toCellRef;

    /**
     * Create a position
     *
     * @param tableRef    the ref of the table
     * @param fromCellRef the to
     * @param toCellRef   the from
     */
    public RangeRef(final TableRef tableRef, final LocalCellRef fromCellRef,
                    final LocalCellRef toCellRef) {
        this.tableRef = tableRef;
        this.fromCellRef = fromCellRef;
        this.toCellRef = toCellRef;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RangeRef)) {
            return false;
        }

        final RangeRef other = (RangeRef) o;
        return EqualityUtil.equal(this.tableRef, other.tableRef) &&
                EqualityUtil.equal(this.fromCellRef, other.fromCellRef) &&
                EqualityUtil.equal(this.toCellRef, other.toCellRef);
    }

    @Override
    public int hashCode() {
        return EqualityUtil.hashObjects(this.tableRef, this.fromCellRef, this.toCellRef);
    }

    /**
     * Returns the address of a range
     * Remainder: [[filename#][$]table-name.]col row:col row
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

    /**
     * @param appendable the appendable where to write
     * @throws IOException never
     */
    public void write(final Appendable appendable) throws IOException {
        this.appendTableRef(appendable);
        this.fromCellRef.write(appendable);
        appendable.append(':');
        this.toCellRef.write(appendable);
    }

    private void appendTableRef(final Appendable appendable) throws IOException {
        if (this.tableRef != null) {
            this.tableRef.write(appendable);
            appendable.append('.');
        }
    }

    /**
     * @return the column
     */
    public int getFromColumn() {
        return this.fromCellRef.getColumn();
    }

    /**
     * @return the row
     */
    public int getFromRow() {
        return this.fromCellRef.getRow();
    }

    /**
     * @return the column
     */
    public int getToColumn() {
        return this.toCellRef.getColumn();
    }

    /**
     * @return the row
     */
    public int getToRow() {
        return this.toCellRef.getRow();
    }
}
