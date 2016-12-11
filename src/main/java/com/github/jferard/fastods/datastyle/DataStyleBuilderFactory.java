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

import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard
 */
public class DataStyleBuilderFactory {
	private final Locale locale;
	private final XMLUtil util;

	public DataStyleBuilderFactory(final XMLUtil util, final Locale locale) {
		this.util = util;
		this.locale = locale;
	}

	public BooleanStyleBuilder booleanStyleBuilder(final String name) {
		return new BooleanStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public CurrencyStyleBuilder currencyStyleBuilder(final String name) {
		return new CurrencyStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public DateStyleBuilder dateStyleBuilder(final String name) {
		return new DateStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public FloatStyleBuilder floatStyleBuilder(final String name) {
		return new FloatStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public FractionStyleBuilder fractionStyleBuilder(final String name) {
		return new FractionStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public PercentageStyleBuilder percentageStyleBuilder(final String name) {
		return new PercentageStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public ScientificNumberStyleBuilder scientificNumberStyleBuilder(
			final String name) {
		return new ScientificNumberStyleBuilder(
				this.util.escapeXMLAttribute(name), this.locale);
	}

	public TimeStyleBuilder timeStyleBuilder(final String name) {
		return new TimeStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}
}
