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

package com.github.jferard.fastods.attribute;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * See Extensible Stylesheet Language (XSL) Version 1.1, 5.9.13 Definitions of Units of Measure
 * (https://www.w3.org/TR/xsl/#d0e5752)
 *
 * @author Julien Férard
 */
public class AbsoluteLength implements Length {
    private static final double CM_FACTOR = 10.0;
    private static final double INCH_FACTOR = 2.54 * CM_FACTOR;
            // inch -> cm = *2.54, cm -> mm = *10
    private static final double PT_FACTOR = INCH_FACTOR / 72.0;
            // pt -> inch = /72 inch -> cm = *2.54, cm -> mm = *10;
    private static final double PC_FACTOR = PT_FACTOR * 12.0;
            // pc -> pt = *12, pt -> inch = /72 inch -> cm = *2.54, cm -> mm = *10;

    /**
     * @param value the length in millimeters
     * @return the created AbsoluteLength
     */
    public static AbsoluteLength mm(final double value) {
        return new AbsoluteLength(value);
    }

    /**
     * @param value the length in centimeters
     * @return the created AbsoluteLength
     */
    public static AbsoluteLength cm(final double value) {
        return new AbsoluteLength(value * CM_FACTOR);
    }

    /**
     * @param value the length in inches
     * @return the created AbsoluteLength
     */
    public static AbsoluteLength in(final double value) {
        return new AbsoluteLength(value * INCH_FACTOR);
    }

    /**
     * @param value the length in points
     * @return the created AbsoluteLength
     */
    public static AbsoluteLength pt(final double value) {
        return new AbsoluteLength(value * PT_FACTOR);
    }

    /**
     * @param value the length in picas
     * @return the created AbsoluteLength
     */
    public static AbsoluteLength pc(final double value) {
        return new AbsoluteLength(value * PC_FACTOR);
    }

    private final double mm;

    /**
     * Create a new length.
     *
     * @param mm the length in millimeters
     */
    AbsoluteLength(final double mm) {
        this.mm = mm;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof AbsoluteLength)) {
            return false;
        }

        final AbsoluteLength other = (AbsoluteLength) o;
        return this.mm - other.mm < MAX_DELTA && other.mm - this.mm < MAX_DELTA;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(this.mm).hashCode();
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    @Override
    public boolean isNotNull() {
        return this.mm <= -MAX_DELTA || this.mm >= MAX_DELTA;
    }

    @Override
    public String getValue() {
        return new DecimalFormat("#.###", new DecimalFormatSymbols(Locale.US)).format(this.mm) +
                "mm";
    }
}
