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

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.XMLConvertible;

import java.io.IOException;

/**
 * 9.4.15 <table:database-range>
 *
 * A filter on a range
 *
 * @author J. Férard
 */
public class AutoFilter implements XMLConvertible {
    /**
     *
     * @param table the table
     * @param r1 first row of the range
     * @param c1 first col of the range
     * @param r2 last row
     * @param c2 last col
     * @return a new builder
     */
    public static AutoFilterBuilder builder(final Table table, final int r1, final int c1, final int r2,
                              final int c2) {
        final String rangeAddress = PositionUtil.create().toRangeAddress(table, r1, c1, r2, c2);
        return new AutoFilterBuilder(rangeAddress);
    }

    private final String rangeAddress;

    /**
     * @param rangeAddress the range address
     */
    public AutoFilter(final String rangeAddress) {
        this.rangeAddress = rangeAddress;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable) throws IOException {
        appendable.append("<table:database-range");
        util.appendAttribute(appendable, "table:display-filter-buttons", "true");
        util.appendAttribute(appendable, "table:target-range-address", this.rangeAddress);
        appendable.append("/>");
    }
}
