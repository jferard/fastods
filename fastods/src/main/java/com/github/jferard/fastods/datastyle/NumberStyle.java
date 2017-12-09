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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.util.Hidable;
import com.github.jferard.fastods.util.NamedObject;

/**
 * A number style
 * @author Julien Férard
 */
public interface NumberStyle extends NamedObject, Hidable, Localized, IsVolatile {
    /**
     * 19.348 number:grouping
     * @return true if the digits are grouped
     */
    boolean getGroupThousands();

    /**
     * 19.352 number:min-integer-digits
     * @return the minimum number of digits
     */
    int getMinIntegerDigits();

    /**
     * Get the color if negative value. If none, null
     * @return the color in hex format
     */
    String getNegativeValueColor();
}
