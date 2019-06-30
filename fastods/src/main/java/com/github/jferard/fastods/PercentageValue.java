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

package com.github.jferard.fastods;

/**
 * A CellValue that contains a percentage
 *
 * @author Julien Férard
 */
public class PercentageValue implements CellValue {
    public static PercentageValue from(final Object o) throws FastOdsException {
        if (o instanceof Number) {
            return new PercentageValue(((Number) o).floatValue());
        } else if (o instanceof PercentageValue) {
            return (PercentageValue) o;
        } else {
            throw new FastOdsException("Can't cast " + o + " to Percentage");
        }
    }

    private final double value;

    /**
     * @param value the value
     */
    public PercentageValue(final double value) {
        this.value = value;
    }

    @Override
    public void setToCell(final TableCell tableCell) {
        tableCell.setPercentageValue(this.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PercentageValue))
            return false;

        final PercentageValue other = (PercentageValue) o;
        return Math.abs(this.value - other.value) < 0.0001;
    }

    @Override
    public final int hashCode() {
        return (int) this.value;
    }

}
