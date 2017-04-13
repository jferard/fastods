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

import java.util.Locale;

/**
 * @author Julien Férard
 */
public class FloatStyleBuilder
		extends NumberStyleBuilder<FloatStyle, FloatStyleBuilder> {
	/**
	 * the number of digits after the separator
	 */
	protected int decimalPlaces;

	/**
	 * The builder
	 *
	 * @param name   The name of this style
	 * @param locale The locale used
	 */
	protected FloatStyleBuilder(final String name, final Locale locale) {
		super(name, locale);
		this.decimalPlaces = 2;
		this.minIntegerDigits = 1;
	}

	@Override
	public FloatStyle build() {
		return new FloatStyle(this.name, this.languageCode, this.countryCode,
				this.volatileStyle, this.decimalPlaces, this.grouping,
				this.minIntegerDigits, this.negativeValueColor);
	}

	/**
	 * Set how many digits are to the right of the decimal symbol.
	 *
	 * @param decimalPlaces The number of digits
	 * @return this for fluent style
	 */
	public FloatStyleBuilder decimalPlaces(final int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
		return this;
	}
}
