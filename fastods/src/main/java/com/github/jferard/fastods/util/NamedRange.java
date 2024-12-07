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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.XMLConvertible;
import com.github.jferard.fastods.ref.CellRef;
import com.github.jferard.fastods.ref.PositionUtil;

import java.io.IOException;

/**
 * 9.4.12 table:named-range
 * <p>
 * A Named range.
 *
 * @author J. Férard
 */
public class NamedRange implements XMLConvertible {
    private final String rangeName;
    private final String rangeAddress;

    /**
     * Create a new NamedRange object.
     *
     * @param rangeName the name of the range
     * @param table the table
     * @param r1 the top row
     * @param c1 the left col
     * @param r2 the bottom row
     * @param c2 the right col
     * @return a NamedRange
     */
    public static NamedRange create(final String rangeName, final Table table, final int r1, final int c1, final int r2, final int c2) {
        final int status = CellRef.ABSOLUTE_TABLE | CellRef.ABSOLUTE_COL | CellRef.ABSOLUTE_ROW;
        final String rangeAddress = PositionUtil.create().toRangeAddress(table, r1, c1, status, r2, c2, status);
        return new NamedRange(rangeName, rangeAddress);
    }

    /**
     * @param rangeName         the name of the range
     * @param rangeAddress the address of the range
     */
    public NamedRange(final String rangeName, final String rangeAddress) {
        this.rangeName = rangeName;
        this.rangeAddress = rangeAddress;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable) throws IOException {
        appendable.append("<table:named-range");
        util.appendAttribute(appendable, "table:name", this.rangeName);
        util.appendAttribute(appendable, "table:cell-range-address", this.rangeAddress);
        appendable.append("/>");
    }
}
