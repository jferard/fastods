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

import com.github.jferard.fastods.util.XMLUtil;

import java.util.Locale;

/**
 * The data style for the current locale
 * @author Julien Férard
 */
public class DataStylesFactory {
	/**
	 * @param locale a Locale
	 * @return the DataStyles associated with this locale
	 */
	public static DataStyles create(final Locale locale) {
		final DataStyleBuilderFactory dataStyleBuilderFactory = new DataStyleBuilderFactory(
				XMLUtil.create(), locale);
		return DataStylesFactory.create(dataStyleBuilderFactory);
	}

	/**
	 * @param util a utility instance to write XML
	 * @param locale a locale
	 * @return the DataStyles associated with this locale
	 */
	public static DataStyles create(final XMLUtil util, final Locale locale) {
		final DataStyleBuilderFactory dataStyleBuilderFactory = new DataStyleBuilderFactory(
				util, locale);
		return DataStylesFactory.create(dataStyleBuilderFactory);
	}

	/**
	 * @param builderFactory a builder factory
	 * @return the DataStyles associated with this locale
	 */
	public static DataStyles create(final DataStyleBuilderFactory builderFactory) {
		final BooleanStyle booleanDataStyle = builderFactory
				.booleanStyleBuilder("boolean-data").buildHidden();
		final CurrencyStyle currencyDataStyle = builderFactory
				.currencyStyleBuilder("currency-data").buildHidden();
		final DateStyle dateDataStyle = builderFactory
				.dateStyleBuilder("date-data").buildHidden();
		final FloatStyle numberDataStyle = builderFactory
				.floatStyleBuilder("float-data").buildHidden();
		final PercentageStyle percentageDataStyle = builderFactory
				.percentageStyleBuilder("percentage-data").buildHidden();
		final TimeStyle timeDataStyle = builderFactory
				.timeStyleBuilder("time-data").buildHidden();
		return new DataStyles(booleanDataStyle, currencyDataStyle, dateDataStyle, numberDataStyle,
				percentageDataStyle, timeDataStyle);
	}
}
