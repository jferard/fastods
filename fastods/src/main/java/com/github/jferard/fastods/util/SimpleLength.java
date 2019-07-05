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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by jferard on 09/04/17.
 * See Extensible Stylesheet Language (XSL) Version 1.1, 5.9.13 Definitions of Units of Measure
 * (https://www.w3.org/TR/xsl/#d0e5752)
 *
 * @author Julien Férard
 */
public class SimpleLength implements Length {
    /**
     * Create an simple length in millimeters
     *
     * @param value the length in millimeters
     * @return the length
     */
    public static SimpleLength mm(final double value) {
        return new SimpleLength(value, Unit.MM);
    }

    /**
     * Create an simple length in centimeters
     *
     * @param value the length in centimeters
     * @return the length
     */
    public static SimpleLength cm(final double value) {
        return new SimpleLength(value, Unit.CM);
    }

    /**
     * Create an simple length in inches
     *
     * @param value the length in inches
     * @return the length
     */
    public static SimpleLength in(final double value) {
        return new SimpleLength(value, Unit.IN);
    }

    /**
     * Create an simple length in points
     *
     * @param value the length in points
     * @return the length
     */
    public static SimpleLength pt(final double value) {
        return new SimpleLength(value, Unit.PT);
    }

    /**
     * Create an simple length in picas
     *
     * @param value the length in picas
     * @return the length
     */
    public static SimpleLength pc(final double value) {
        return new SimpleLength(value, Unit.PC);
    }

    /**
     * Create an simple length in em
     *
     * @param value the length in ems
     * @return the length
     */
    public static SimpleLength em(final double value) {
        return new SimpleLength(value, Unit.EM);
    }

    private final double value;
    private final Unit unit;

    /**
     * Create a new simple length
     *
     * @param value the value
     * @param unit  the unit
     */
    SimpleLength(final double value, final Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    /**
     * Warning: this method does not compare different units.
     * {@code SimpleLength.mm(10).equals(SimpleLenght.cm(1))} is false.
     *
     * @param o the object
     * @return true if the object is a SimpleLength with the same unit and same value as this one.
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof SimpleLength)) {
            return false;
        }

        final SimpleLength other = (SimpleLength) o;
        if (this.unit == other.unit) {
            final double delta = this.value - other.value;
            return (delta >= 0.0) ? (delta < MAX_DELTA) : (delta > -MAX_DELTA);
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return Double.valueOf(this.value).hashCode() * 31 + this.unit.hashCode();
    }

    /**
     * @return a representation of this unit
     */
    @Override
    public String toString() {
        return new DecimalFormat("#.###", new DecimalFormatSymbols(Locale.US)).format(this.value) +
                this.unit.toString().toLowerCase(Locale.US);
    }

    @Override
    public boolean isNotNull() {
        return this.value <= -MAX_DELTA || this.value >= MAX_DELTA;
    }

    /**
     * A unit.
     * XSL 5.9 Expressions
     */
    public enum Unit {
        /**
         * millimeter
         */
        MM,
        /**
         * centimeter
         */
        CM,
        /**
         * inch
         */
        IN,
        /**
         * point
         */
        PT,
        /**
         * pica
         */
        PC,
        /**
         * em (the only relative unit
         */
        EM
    }
}
