/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.datastyle;

/**
 * A builder for decimal styles
 * @param <T> the concrete builder
 * @author Julien Férard
 */
public interface DecimalStyleBuilder<T> {
    /**
     * 20.250 style:decimal-places
     * Set how many digits are to the right of the decimal symbol.
     * @param decimalPlaces "the maximum number of decimal places that are displayed if numbers are formatted by a data style that has no setting for number of decimal places itself"
     * @return this for fluent style
     */
    T decimalPlaces(int decimalPlaces);
}
