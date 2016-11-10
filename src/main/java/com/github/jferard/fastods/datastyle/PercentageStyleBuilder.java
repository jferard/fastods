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
package com.github.jferard.fastods.datastyle;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public class PercentageStyleBuilder
		extends DataStyleBuilder<PercentageStyle, PercentageStyleBuilder> {
	private final FloatStyleBuilder floatStyleBuilder;

	/**
	 * Create a new number style with the name name, minimum integer digits is
	 * minIntDigits and decimal places is decPlaces.
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

	public PercentageStyleBuilder decimalPlaces(final int decimalPlaces) {
		this.floatStyleBuilder.decimalPlaces(decimalPlaces);
		return this;
	}

	public PercentageStyleBuilder groupThousands(final boolean grouping) {
		this.floatStyleBuilder.groupThousands(grouping);
		return this;
	}

	public PercentageStyleBuilder minIntegerDigits(final int minIntegerDigits) {
		this.floatStyleBuilder.minIntegerDigits(minIntegerDigits);
		return this;
	}

	public PercentageStyleBuilder negativeValueColor(
			final String negativeValueColor) {
		this.floatStyleBuilder.negativeValueColor(negativeValueColor);
		return this;
	}

	public PercentageStyleBuilder negativeValueRed() {
		this.floatStyleBuilder.negativeValueRed();
		return this;
	}
}
