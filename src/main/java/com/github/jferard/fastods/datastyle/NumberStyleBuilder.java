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

import com.github.jferard.fastods.Color;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public abstract class NumberStyleBuilder<S extends NumberStyle, B extends NumberStyleBuilder<S, B>>
		extends DataStyleBuilder<S, B> {
	protected boolean grouping;
	protected int minIntegerDigits;
	protected String negativeValueColor;

	/**
	 * Create a new number style with a name and a locale.
	 *
	 * @param name The name of the number style, this name must be unique.
	 * @param locale the locale to use
	 */
	NumberStyleBuilder(final String name, final Locale locale) {
		super(name, locale);
		this.grouping = false;
	}

	/**
	 * @param grouping if true, the thousands separator is shown.
	 * @return this for fluent style
	 */
	@SuppressWarnings("unchecked")
	public B groupThousands(final boolean grouping) {
		this.grouping = grouping;
		return (B) this;
	}

	/**
	 * @param minIntegerDigits
	 *            The number of digits for integer part
	 * @return this for fluent style
	 */
	@SuppressWarnings("unchecked")
	public B minIntegerDigits(final int minIntegerDigits) {
		this.minIntegerDigits = minIntegerDigits;
		return (B) this;
	}

	/**
	 * @param negativeValueColor the color for negative values, null if none
	 * @return this for fluent style
	 */
	@SuppressWarnings("unchecked")
	public B negativeValueColor(final String negativeValueColor) {
		this.negativeValueColor = negativeValueColor;
		return (B) this;
	}

	/**
	 * Sets the red color for negative values
	 * @return this for fluent style
	 */
	@SuppressWarnings("unchecked")
	public B negativeValueRed() {
		this.negativeValueColor = Color.RED;
		return (B) this;
	}
}
