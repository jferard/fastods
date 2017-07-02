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

import com.github.jferard.fastods.util.StyleBuilder;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public class ScientificNumberStyleBuilder implements StyleBuilder<ScientificNumberStyle> {
	private final FloatStyleBuilder floatStyleBuilder;
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
		this.floatStyleBuilder = new FloatStyleBuilder(name, locale);
	}

	@Override
	public ScientificNumberStyle build() {
		return new ScientificNumberStyle(this.floatStyleBuilder.build(), this.minExponentDigits);
	}

	@Override
	public ScientificNumberStyle buildHidden() {
		return new ScientificNumberStyle(this.floatStyleBuilder.buildHidden(), this.minExponentDigits);
	}

	/**
	 * @param decimalPlaces The number of decimal places to be shown.
	 * @return this for fluent style
	 */
	public ScientificNumberStyleBuilder decimalPlaces(final int decimalPlaces) {
		this.floatStyleBuilder.decimalPlaces(decimalPlaces);
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

	public ScientificNumberStyleBuilder groupThousands(final boolean grouping) {
		this.floatStyleBuilder.groupThousands(grouping);
		return this;
	}

	public ScientificNumberStyleBuilder minIntegerDigits(final int minIntegerDigits) {
		this.floatStyleBuilder.minIntegerDigits(minIntegerDigits);
		return this;
	}

	public ScientificNumberStyleBuilder negativeValueColor(final String negativeValueColor) {
		this.floatStyleBuilder.negativeValueColor(negativeValueColor);
		return this;
	}

	public ScientificNumberStyleBuilder negativeValueRed() {
		this.floatStyleBuilder.negativeValueRed();
		return this;
	}

	public ScientificNumberStyleBuilder country(final String countryCode) {
		this.floatStyleBuilder.country(countryCode);
		return this;
	}

	public ScientificNumberStyleBuilder language(final String languageCode) {
		this.floatStyleBuilder.language(languageCode);
		return this;
	}

	public ScientificNumberStyleBuilder locale(final Locale locale) {
		this.floatStyleBuilder.locale(locale);
		return this;
	}

	public ScientificNumberStyleBuilder volatileStyle(final boolean volatileStyle) {
		this.floatStyleBuilder.volatileStyle(volatileStyle);
		return this;
	}
}
