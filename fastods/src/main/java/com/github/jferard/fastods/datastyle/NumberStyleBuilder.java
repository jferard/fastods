/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.style.ShowableBuilder;
import com.github.jferard.fastods.util.StyleBuilder;

/**
 * @param <S> the concrete number style
 * @param <T> the concrete number style builder
 * @author Julien Férard
 */
public interface NumberStyleBuilder<S, T extends NumberStyleBuilder<S, T>>
        extends StyleBuilder<S>, LocalizedBuilder<T>, IsVolatileBuilder<T>, ShowableBuilder<T> {
    /**
     * @param grouping if true, the thousands separator is shown.
     * @return this for fluent style
     */
    T groupThousands(boolean grouping);

    /**
     * @param minIntegerDigits The number of digits for integer part
     * @return this for fluent style
     */
    T minIntegerDigits(int minIntegerDigits);

    /**
     * @param negativeValueColor the color for negative values, null if none
     * @return this for fluent style
     */
    T negativeValueColor(Color negativeValueColor);

    /**
     * Sets the red color for negative values
     *
     * @return this for fluent style
     */
    T negativeValueRed();
}
