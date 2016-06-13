/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods.datastyle;

import java.util.Locale;

import com.github.jferard.fastods.Color;

/**
 * @author Julien Férard
 */
public abstract class NumberStyleBuilder<S extends NumberStyle, B extends NumberStyleBuilder<S, B>>
		extends DataStyleBuilder<S, B> {
	protected boolean grouping;
	protected int minIntegerDigits;
	protected String negativeValueColor;

	/**
	 * Create a new number style with the name name, minimum integer digits is
	 * minIntDigits and decimal places is decPlaces.
	 *
	 * @param name
	 *            The name of the number style, this name must be unique.
	 * @param locale
	 */
	public NumberStyleBuilder(final String name, final Locale locale) {
		super(name, locale);
		this.grouping = false;
	}

	/**
	 * If this is set to true, the thousands separator is shown.
	 *
	 * @param grouping
	 */
	public B groupThousands(final boolean grouping) {
		this.grouping = grouping;
		return (B) this;
	}

	/**
	 * Set how many leading zeros are present.
	 *
	 * @param minIntegerDigits
	 *            The number of leading zeros
	 */
	public B minIntegerDigits(final int minIntegerDigits) {
		this.minIntegerDigits = minIntegerDigits;
		return (B) this;
	}

	/**
	 * Sets the color for negative values
	 *
	 * @param negativeValueColor
	 *            null if none
	 * @return
	 */
	public B negativeValueColor(final String negativeValueColor) {
		this.negativeValueColor = negativeValueColor;
		return (B) this;
	}

	/**
	 * Sets the color for negative values
	 *
	 * @param negativeValueColor
	 *            null if none
	 * @return
	 */
	public B negativeValueRed() {
		this.negativeValueColor = Color.RED;
		return (B) this;
	}
}
