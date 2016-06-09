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

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file NumberStyleBuilder.java is part of FastODS.
 */
public class PercentageStyleBuilder
		extends DataStyleBuilder<PercentageStyle, PercentageStyleBuilder> {
	private final FloatStyleBuilder floatStyleBuilder;

	/**
	 * Create a new number style with the name name, minimum integer digits is
	 * minIntDigits and decimal places is nDecPlaces.
	 *
	 * @param name
	 *            The name of the number style, this name must be unique.
	 * @param locale
	 */
	public PercentageStyleBuilder(final String name, final Locale locale) {
		super(name, locale);
		this.floatStyleBuilder = new FloatStyleBuilder(name, locale);
	}

	@Override
	public PercentageStyle build() {
		return new PercentageStyle(this.name, this.languageCode,
				this.countryCode, this.volatileStyle,
				this.floatStyleBuilder.decimalPlaces,
				this.floatStyleBuilder.grouping,
				this.floatStyleBuilder.minIntegerDigits,
				this.floatStyleBuilder.negativeValueColor);
	}

	public FloatStyleBuilder decimalPlaces(final int decimalPlaces) {
		return this.floatStyleBuilder.decimalPlaces(decimalPlaces);
	}

	public FloatStyleBuilder groupThousands(final boolean grouping) {
		return this.floatStyleBuilder.groupThousands(grouping);
	}

	public FloatStyleBuilder minIntegerDigits(final int minIntegerDigits) {
		return this.floatStyleBuilder.minIntegerDigits(minIntegerDigits);
	}

	public FloatStyleBuilder negativeValueColor(
			final String negativeValueColor) {
		return this.floatStyleBuilder.negativeValueColor(negativeValueColor);
	}

	public FloatStyleBuilder negativeValueRed() {
		return this.floatStyleBuilder.negativeValueRed();
	}
}
