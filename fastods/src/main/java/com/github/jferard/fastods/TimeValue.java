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

package com.github.jferard.fastods;

/**
 * A time value
 *
 * @author J. Férard
 */
public class TimeValue implements CellValue {
    private static final int MONTHS_BY_YEAR = 12;
    private static final int HOURS_BY_DAY = 24;
    private static final int MINUTES_BY_HOUR = 60;
    private static final int SECONDS_BY_MINUTE = 60;

    /**
     * @param o the object to cast
     * @return the time value
     * @throws FastOdsException if the cast was not possible
     */
    public static TimeValue from(final Object o) throws FastOdsException {
        if (o instanceof Number) {
            final Number number = (Number) o;
            long l = number.longValue();
            final boolean neg = l < 0;
            if (neg) {
                l = -l;
            }
            return new TimeValue(neg, 0, 0, 0, 0, 0, (double) l / 1000);
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

    /**
     * Create a new time value. Users must ensure that all values are positive.
     *
     * @param neg     true if the time value is negative
     * @param years   the number of years
     * @param months  the number of months
     * @param days    the number of days
     * @param hours   the number of hours
     * @param minutes the number of minutes
     * @param seconds the number of seconds
     */
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
        if (o == this) {
            return true;
        }
        if (!(o instanceof TimeValue)) {
            return false;
        }

        final TimeValue other = (TimeValue) o;

        // See XMLSchema part 2, 3.2.6.2 Order relation on duration
        // TODO: check if the implementation is correct
        return this.neg == other.neg && this.totalMonths() == other.totalMonths() &&
                Math.abs(this.totalSeconds() - other.totalSeconds()) < FloatValue.FLOAT_DELTA;
    }

    private long totalMonths() {
        return this.years * MONTHS_BY_YEAR + this.months;
    }

    private double totalSeconds() {
        return ((this.days * HOURS_BY_DAY + this.hours) * MINUTES_BY_HOUR + this.minutes) *
                SECONDS_BY_MINUTE + this.seconds;
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

    @Override
    public String toString() {
        return "TimeValue[" + (this.neg ? "-P" : "P") + this.years + "Y" + this.months + "M" +
                this.days + "DT" + this.hours + "H" + this.minutes + "M" + this.seconds + "S" + "]";

    }
}
