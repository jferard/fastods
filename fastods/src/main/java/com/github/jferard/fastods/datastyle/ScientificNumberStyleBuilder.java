/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
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

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.FastOdsException;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public class ScientificNumberStyleBuilder implements DataStyleBuilder<ScientificNumberStyle, ScientificNumberStyleBuilder>,
		NumberStyleBuilder<ScientificNumberStyle, ScientificNumberStyleBuilder>, DecimalStyleBuilder<ScientificNumberStyleBuilder> {
	private final FloatStyleBuilder floatStyleBuilder;
	private int minExponentDigits;

	/**
	 * Create a new number style with the name name, minimum integer digits is
	 * minIntDigits and decimal places is decPlaces.
	 *
	 * @param name   The name of the number style, this name must be unique.
	 * @param locale the locale used
	 */
	public ScientificNumberStyleBuilder(final String name,
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

	@Override
	public ScientificNumberStyleBuilder decimalPlaces(final int decimalPlaces) {
		this.floatStyleBuilder.decimalPlaces(decimalPlaces);
		return this;
	}

	/**
	 * Set the number of exponent digits.
	 *
	 * @param minExponentDigits The minimum of exponent digits to be used
	 * @return this for fluent style
	 */
	public ScientificNumberStyleBuilder minExponentDigits(
			final int minExponentDigits) {
		this.minExponentDigits = minExponentDigits;
		return this;
	}

	@Override
	public ScientificNumberStyleBuilder groupThousands(final boolean grouping) {
		this.floatStyleBuilder.groupThousands(grouping);
		return this;
	}

	@Override
	public ScientificNumberStyleBuilder minIntegerDigits(final int minIntegerDigits) {
		this.floatStyleBuilder.minIntegerDigits(minIntegerDigits);
		return this;
	}

	@Override
	public ScientificNumberStyleBuilder negativeValueColor(final Color negativeValueColor) {
		this.floatStyleBuilder.negativeValueColor(negativeValueColor);
		return this;
	}

	@Override
	public ScientificNumberStyleBuilder negativeValueRed() {
		this.floatStyleBuilder.negativeValueRed();
		return this;
	}

	@Override
	public ScientificNumberStyleBuilder country(final String countryCode) {
		this.floatStyleBuilder.country(countryCode);
		return this;
	}

	@Override
	public ScientificNumberStyleBuilder language(final String languageCode) {
		this.floatStyleBuilder.language(languageCode);
		return this;
	}

	@Override
	public ScientificNumberStyleBuilder locale(final Locale locale) {
		this.floatStyleBuilder.locale(locale);
		return this;
	}

	@Override
	public ScientificNumberStyleBuilder volatileStyle(final boolean volatileStyle) {
		this.floatStyleBuilder.volatileStyle(volatileStyle);
		return this;
	}
}
