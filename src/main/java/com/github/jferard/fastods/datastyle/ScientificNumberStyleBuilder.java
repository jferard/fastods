/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods.datastyle;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public class ScientificNumberStyleBuilder
		extends NumberStyleBuilder<NumberStyle, ScientificNumberStyleBuilder> {
	private int decimalPlaces;
	private int minExponentDigits;

	/**
	 * Create a new number style with the name name, minimum integer digits is
	 * minIntDigits and decimal places is decPlaces.
	 *
	 * @param name   The name of the number style, this name must be unique.
	 * @param locale the locale used
	 */
	ScientificNumberStyleBuilder(final String name,
								 final Locale locale) {
		super(name, locale);
	}

	@Override
	public ScientificNumberStyle build() {
		return new ScientificNumberStyle(this.name, this.languageCode,
				this.countryCode, this.volatileStyle, this.grouping,
				this.minIntegerDigits, this.negativeValueColor,
				this.decimalPlaces, this.minExponentDigits);
	}

	/**
	 * @param decimalPlaces The number of decimal places to be shown.
	 * @return this for fluent style
	 */
	public ScientificNumberStyleBuilder decimalPlaces(final int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
		return this;
	}

	/**
	 * Set the number of exponent digits.<br>
	 * The number style is set to NUMBER_SCIENTIFIC.
	 *
	 * @param minExponentDigits The minimum of exponent digits to be used
	 * @return this for fluent style
	 */
	public ScientificNumberStyleBuilder minExponentDigits(
			final int minExponentDigits) {
		this.minExponentDigits = minExponentDigits;
		return this;
	}
}
