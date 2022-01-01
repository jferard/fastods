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

/**
 * A position builder: file + table + r/col
 *
 * @author J. Férard
 */
class LocalCellRefBuilder {
    private int c;
    private int r;
    private int status;

    /**
     * Create a position
     */
    LocalCellRefBuilder() {
        this.r = 0;
        this.c = 0;
        this.status = 0;
    }

    /**
     * Set the r
     *
     * @param r the row index
     * @return this for fluent style
     */
    public LocalCellRefBuilder row(final int r) {
        this.r = r;
        return this;
    }

    /**
     * Set the r absolute
     *
     * @param r the row index
     * @return this for fluent style
     */
    public LocalCellRefBuilder absRow(final int r) {
        this.r = r;
        this.status |= CellRef.ABSOLUTE_ROW;
        return this;
    }

    /**
     * Set the column
     *
     * @param c the column index
     * @return this for fluent style
     */
    public LocalCellRefBuilder column(final int c) {
        this.c = c;
        return this;
    }

    /**
     * Set the column absolute
     *
     * @param c the column index
     * @return this for fluent style
     */
    public LocalCellRefBuilder absColumn(final int c) {
        this.c = c;
        this.status |= CellRef.ABSOLUTE_COL;
        return this;
    }

    /**
     * @return the position or null
     */
    public LocalCellRef build() {
        return new LocalCellRef(this.r, this.c, this.status);
    }
}
