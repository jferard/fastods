/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.ThisShouldNotHappen;
import com.github.jferard.fastods.util.EqualityUtil;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A reference on a local cell (row - col)
 *
 * @author J. Férard
 */
class LocalCellRef implements Ref {
    public static final char ABS_SIGN = '$';

    public static LocalCellRefBuilder builder() {
        return new LocalCellRefBuilder();
    }

    private final int c;
    private final int status;
    private final int r;

    /**
     * Create a position
     *
     * @param r      the row
     * @param c      the column
     * @param status the absolute status = col | row | table.
     */
    public LocalCellRef(final int r, final int c, final int status) {
        this.r = r;
        this.c = c;
        this.status = status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalCellRef)) {
            return false;
        }

        final LocalCellRef other = (LocalCellRef) o;
        return this.r == other.r && this.c == other.c && this.status == other.status;
    }

    @Override
    public int hashCode() {
        return EqualityUtil.hashObjects(this.r, this.c, this.status);
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return this.c;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return this.r;
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
     * Remainder: '<filename>'#<table-name>.<col><row>
     *
     * @return the Excel/OO/LO address
     */
    @Override
    public String toString() {
        return RefUtil.toString(this);
    }

    /**
     * @param appendable the appendable where to write
     * @throws IOException never
     */
    @Override
    public void write(final Appendable appendable) throws IOException {
        final StringBuilder tempSb = this.getColStringBuilder();
        if ((this.status & CellRef.ABSOLUTE_COL) == CellRef.ABSOLUTE_COL) {
            appendable.append('$');
        }
        appendable.append(tempSb);
        if ((this.status & CellRef.ABSOLUTE_ROW) == CellRef.ABSOLUTE_ROW) {
            appendable.append('$');
        }
        appendable.append(String.valueOf(this.r + 1));
    }

    private StringBuilder getColStringBuilder() {
        final StringBuilder tempSb = new StringBuilder();
        int col = this.c;
        while (col >= PositionUtil.ALPHABET_SIZE) {
            tempSb.insert(0, (char) (PositionUtil.ORD_A + (col % PositionUtil.ALPHABET_SIZE)));
            col = col / PositionUtil.ALPHABET_SIZE - 1;
        }
        tempSb.insert(0, (char) (PositionUtil.ORD_A + col));
        return tempSb;
    }

}
