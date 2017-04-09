/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
 */
public class SimpleLength implements Length {
    public enum Unit {
        MM, CM, IN, PT, PC
    }

    private final double value;
    private final Unit unit;

    SimpleLength(final double value, final Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public static SimpleLength mm(final double value) {
        return new SimpleLength(value, Unit.MM);
    }

    public static SimpleLength cm(final double value) {
        return new SimpleLength(value, Unit.CM);
    }

    public static SimpleLength in(final double value) {
        return new SimpleLength(value, Unit.IN);
    }

    public static SimpleLength pt(final double value) {
        return new SimpleLength(value, Unit.PT);
    }

    public static SimpleLength pc(final double value) {
        return new SimpleLength(value, Unit.PC);
    }

    public boolean equals(final Object o) {
        if (!(o instanceof SimpleLength))
            return false;

        final SimpleLength other = (SimpleLength) o;
        if (this.unit == other.unit) {
            final double delta = this.value - other.value;
            return (delta >= 0.0) ? (delta < 0.001) : (delta > -0.001);
        } else
            return false;

    }

    public String toString() {
        return new DecimalFormat("#.###", new DecimalFormatSymbols(Locale.US)).format(this.value)+this.unit.toString().toLowerCase(Locale.US);
    }
}
