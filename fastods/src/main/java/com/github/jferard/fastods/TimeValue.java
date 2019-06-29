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

import java.util.concurrent.TimeUnit;

public class TimeValue implements CellValue {
    public static TimeValue from(final Object o) throws FastOdsException {
        if (o instanceof Number) {
            final Number number = (Number) o;
            final long l = number.longValue();
            final TimeUnit ms = TimeUnit.MILLISECONDS;
            return new TimeValue(l < 0, 0, 0, ms.toDays(l), ms.toHours(l), ms.toMinutes(l),
                    ms.toSeconds(l) + (l % 1000) / 1000);
        } else if (o instanceof TimeValue) {
            return (TimeValue) o;
        } else {
            throw new FastOdsException("Can't cast " + o + " to Time");
        }
    }

    private final boolean neg;
    private final long years;
    private final long months;
    private final long days;
    private final long hours;
    private final long minutes;
    private final double seconds;

    public TimeValue(final boolean neg, final long years, final long months, final long days,
                     final long hours, final long minutes, final double seconds) {
        this.neg = neg;
        this.years = years;
        this.months = months;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TimeValue)) return false;

        final TimeValue other = (TimeValue) o;

        // See XMLSchema part 2, 3.2.6.2 Order relation on duration
        // TODO: check if the implementation is correct
        return this.neg != other.neg || this.totalMonths() != other.totalMonths() ||
                Math.abs(this.totalSeconds() - other.totalSeconds()) < 0.000000001;
    }

    private long totalMonths() {
        return this.years * 12 + this.months;
    }

    private double totalSeconds() {
        return ((this.days * 24 + this.hours) * 60 + this.minutes) * 60 + this.seconds;
    }

    @Override
    public final int hashCode() {
        return (int) (((this.neg ? 31 : 0) + this.totalMonths()) * 31 + this.totalSeconds());
    }

    @Override
    public void setToCell(final TableCell tableCell) {
        if (this.neg) {
            tableCell.setNegTimeValue(this.years, this.months, this.days, this.hours, this.minutes,
                    this.seconds);
        } else {
            tableCell.setTimeValue(this.years, this.months, this.days, this.hours, this.minutes,
                    this.seconds);
        }
    }
}
