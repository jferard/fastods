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
import com.github.jferard.fastods.ref.PositionUtil;

import java.io.IOException;

/**
 * 9.4.15 <table:database-range>
 * <p>
 * A filter on a range
 *
 * @author J. Férard
 */
public class AutoFilter implements XMLConvertible {
    /**
     * @param table the table
     * @param r1    first row of the range
     * @param c1    first col of the range
     * @param r2    last row
     * @param c2    last col
     * @return a new builder
     */
    public static AutoFilterBuilder builder(final String rangeName, final Table table, final int r1,
                                            final int c1, final int r2, final int c2) {
        final String rangeAddress = PositionUtil.create().toRangeAddress(table, r1, c1, r2, c2);
        return new AutoFilterBuilder(rangeName, rangeAddress);
    }

    private final String rangeName;
    private final String rangeAddress;
    private final boolean displayButtons;
    private final Filter filter;

    /**
     * @param rangeAddress   the range address
     * @param displayButtons display buttons if true
     * @param filter         the filter
     */
    public AutoFilter(final String rangeName, final String rangeAddress,
                      final boolean displayButtons, final Filter filter) {
        this.rangeName = rangeName;
        this.rangeAddress = rangeAddress;
        this.displayButtons = displayButtons;
        this.filter = filter;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:database-range");
        util.appendAttribute(appendable, "table:name", this.rangeName);
        util.appendAttribute(appendable, "table:display-filter-buttons", this.displayButtons);
        util.appendAttribute(appendable, "table:target-range-address", this.rangeAddress);
        if (this.filter == null) {
            appendable.append("/>");
        } else {
            appendable.append("><table:filter>");
            this.filter.appendXMLContent(util, appendable);
            appendable.append("</table:filter></table:database-range>");
        }
    }
}
