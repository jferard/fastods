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
import com.github.jferard.fastods.util.StyleBuilder;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public final class NumberStyleBuilder {
	private final CoreDataStyleBuilder dataStyleBuilder;
	private boolean grouping;
	private int minIntegerDigits;
	private String negativeValueColor;

	/**
	 * Create a new number style with a name and a locale.
	 *
	 * @param name The name of the number style, this name must be unique.
	 * @param locale the locale to use
	 */
	NumberStyleBuilder(final String name, final Locale locale) {
		this.dataStyleBuilder = new CoreDataStyleBuilder(name, locale);
		this.minIntegerDigits = 1;
		this.grouping = false;
	}

	/**
	 * @param grouping if true, the thousands separator is shown.
	 * @return this for fluent style
	 */
	public NumberStyleBuilder groupThousands(final boolean grouping) {
		this.grouping = grouping;
		return this;
	}

	/**
	 * @param minIntegerDigits
	 *            The number of digits for integer part
	 * @return this for fluent style
	 */
	public NumberStyleBuilder minIntegerDigits(final int minIntegerDigits) {
		this.minIntegerDigits = minIntegerDigits;
		return this;
	}

	/**
	 * @param negativeValueColor the color for negative values, null if none
	 * @return this for fluent style
	 */
	public NumberStyleBuilder negativeValueColor(final String negativeValueColor) {
		this.negativeValueColor = negativeValueColor;
		return this;
	}

	/**
	 * Sets the red color for negative values
	 * @return this for fluent style
	 */
	public NumberStyleBuilder negativeValueRed() {
		this.negativeValueColor = Color.RED;
		return this;
	}

	public NumberStyleBuilder country(final String countryCode) {
		this.dataStyleBuilder.country(countryCode);
		return this;
	}

	public NumberStyleBuilder language(final String languageCode) {
		this.dataStyleBuilder.language(languageCode);
		return this;
	}

	public NumberStyleBuilder locale(final Locale locale) {
		this.dataStyleBuilder.locale(locale);
		return this;
	}

	public NumberStyleBuilder volatileStyle(final boolean volatileStyle) {
		this.dataStyleBuilder.volatileStyle(volatileStyle);
		return this;
	}

	/**
	 * @return a number style
	 */
	public NumberStyle build() {
		return new NumberStyle(this.dataStyleBuilder.build(), this.grouping, this.minIntegerDigits, this.negativeValueColor);
	}

	/**
	 * @return a number style
	 */
	public NumberStyle buildHidden() {
		return new NumberStyle(this.dataStyleBuilder.buildHidden(), this.grouping, this.minIntegerDigits, this.negativeValueColor);
	}
}
