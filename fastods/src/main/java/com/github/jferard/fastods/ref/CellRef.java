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

import com.github.jferard.fastods.util.EqualityUtil;

import java.io.IOException;

/**
 * A position (file - table - row - col)
 *
 * @author J. Férard
 */
public class CellRef {
    /**
     * The separator for the ref.
     */
    public static final char TABLE_CELL_SEP = '.';
    /**
     * The ref is relative
     */
    public static final int RELATIVE = 0;
    /**
     * The col is absolute
     */
    public static final int ABSOLUTE_COL = 1;
    /**
     * The row is absolute
     */
    public static final int ABSOLUTE_ROW = 2;
    /**
     * The table is absolute
     */
    public static final int ABSOLUTE_TABLE = 4;

    /**
     * @param r      row index
     * @param c      column index
     * @param status status (abs)
     * @return a local cell ref
     */
    public static CellRef create(final int r, final int c, final int status) {
        return new CellRef(null, new LocalCellRef(r, c, status));
    }

    private final LocalCellRef localCellRef;
    private final TableRef tableRef;


    /**
     * Create a position
     *
     * @param tableRef     the ref of the table
     * @param localCellRef the ref of the cell
     */
    public CellRef(final TableRef tableRef, final LocalCellRef localCellRef) {
        this.tableRef = tableRef;
        this.localCellRef = localCellRef;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CellRef)) {
            return false;
        }

        final CellRef other = (CellRef) o;
        return EqualityUtil.equal(this.tableRef, other.tableRef) &&
                EqualityUtil.equal(this.localCellRef, other.localCellRef);
    }

    @Override
    public int hashCode() {
        return EqualityUtil.hashObjects(this.tableRef, this.localCellRef);
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return this.localCellRef.getColumn();
    }

    /**
     * @return the row
     */
    public int getRow() {
        return this.localCellRef.getRow();
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
     * Remainder: '[filename]'#[table-name].[col][row]
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
        if (this.tableRef != null) {
            this.tableRef.write(appendable);
            appendable.append(TABLE_CELL_SEP);
        }
        this.localCellRef.write(appendable);
    }
}
