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
public class AbsoluteLength implements Length {
    private final double mm;

    AbsoluteLength(final double mm) {
        this.mm = mm;
    }

    public static AbsoluteLength mm(final double value) {
        return new AbsoluteLength(value);
    }

    public static AbsoluteLength cm(final double value) {
        return new AbsoluteLength(value*10.0);
    }

    public static AbsoluteLength in(final double value) {
        return new AbsoluteLength(value*25.4); // inch -> cm = *2.54, cm -> mm = *10
    }

    public static AbsoluteLength pt(final double value) {
        return new AbsoluteLength(value*25.4/72.0); // pt -> inch = /72 inch -> cm = *2.54, cm -> mm = *10
    }

    public static AbsoluteLength pc(final double value) {
        return new AbsoluteLength(value*25.4/6.0); // pc -> pt = *12, pt -> inch = /72 inch -> cm = *2.54, cm -> mm = *10
    }

    public boolean equals(final Object o) {
        if (!(o instanceof AbsoluteLength))
            return false;

        final AbsoluteLength other = (AbsoluteLength) o;
        return this.mm - other.mm < 0.001 || other.mm - this.mm < 0.001;
    }

    public String toString() {
        return new DecimalFormat("#.###", new DecimalFormatSymbols(Locale.US)).format(this.mm)+"mm";
    }
}
