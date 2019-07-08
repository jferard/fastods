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

/**
 * A builder for AutoFilter class
 */
public class AutoFilterBuilder {
    private final String rangeAddress;
    private boolean displayButtons;

    /**
     * @param rangeAddress the range address
     */
    public AutoFilterBuilder(final String rangeAddress) {
        this.rangeAddress = rangeAddress;
        this.displayButtons = true;
    }

    /**
     * @return the auto filter
     */
    public AutoFilter build() {
        return new AutoFilter(this.rangeAddress, this.displayButtons);
    }

    /**
     * 19.620 table:display-filter-buttons
     *
     * Don't display buttons
     * @return this for fluent style
     */
    public AutoFilterBuilder hideButtons() {
        this.displayButtons = false;
        return this;
    }
}
