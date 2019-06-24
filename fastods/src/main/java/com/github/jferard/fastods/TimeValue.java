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

public class TimeValue extends CellValue {
    public static TimeValue from(final Object o) throws FastOdsException {
        if (o instanceof Number) {
            return new TimeValue(((Number) o).longValue());
        } else if (o instanceof TimeValue) {
            return (TimeValue) o;
        } else{
            throw new FastOdsException("Can't cast " + o + " to Time");
        }
    }

    private final long timeInMillis;

    /**
     * @param timeInMillis
     */
    public TimeValue(final long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TimeValue)) return false;

        final TimeValue other = (TimeValue) o;
        return this.timeInMillis == other.timeInMillis;
    }

    @Override
    public final int hashCode() {
        return (int) this.timeInMillis % Integer.MAX_VALUE;
    }

    @Override
    public void setToCell(final TableCell tableCell) {
        tableCell.setTimeValue(this.timeInMillis);
    }
}
