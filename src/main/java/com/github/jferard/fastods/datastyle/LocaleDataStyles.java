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

import com.github.jferard.fastods.style.TableCellStyle;

/**
 * @author Julien Férard
 *
 */
public class LocaleDataStyles implements DataStyles {
	private final TableCellStyle booleanCellStyle;
	private final TableCellStyle currencyCellStyle;
	private final TableCellStyle dateCellStyle;
	private final TableCellStyle numberCellStyle;
	private final TableCellStyle percentageCellStyle;
	private final TableCellStyle timeCellStyle;

	public LocaleDataStyles(final DataStyleBuilderFactory builderFactory) {
		final BooleanStyle booleanDataStyle = builderFactory
				.booleanStyleBuilder("boolean-data").build();
		this.booleanCellStyle = TableCellStyle.builder("boolean-style")
				.dataStyle(booleanDataStyle).build();
		final CurrencyStyle currencyDataStyle = builderFactory
				.currencyStyleBuilder("currency-data").build();
		this.currencyCellStyle = TableCellStyle.builder("currency-style")
				.dataStyle(currencyDataStyle).build();
		final DateStyle dateDataStyle = builderFactory
				.dateStyleBuilder("date-data").build();
		this.dateCellStyle = TableCellStyle.builder("date-style")
				.dataStyle(dateDataStyle).build();
		final FloatStyle numberDataStyle = builderFactory
				.floatStyleBuilder("float-data").build();
		this.numberCellStyle = TableCellStyle.builder("number-style")
				.dataStyle(numberDataStyle).build();
		final PercentageStyle percentageDataStyle = builderFactory
				.percentageStyleBuilder("percentage-data").build();
		this.percentageCellStyle = TableCellStyle.builder("percentage-style")
				.dataStyle(percentageDataStyle).build();
		final TimeStyle timeDataStyle = builderFactory
				.timeStyleBuilder("time-data").build();
		this.timeCellStyle = TableCellStyle.builder("time-style")
				.dataStyle(timeDataStyle).build();
	}

	@Override
	public TableCellStyle getBooleanStyle() {
		return this.booleanCellStyle;
	}

	@Override
	public TableCellStyle getCurrencyStyle() {
		return this.currencyCellStyle;
	}

	@Override
	public TableCellStyle getDateStyle() {
		return this.dateCellStyle;
	}

	@Override
	public TableCellStyle getNumberStyle() {
		return this.numberCellStyle;
	}

	@Override
	public TableCellStyle getPercentageStyle() {
		return this.percentageCellStyle;
	}

	@Override
	public TableCellStyle getTimeStyle() {
		return this.timeCellStyle;
	}
}
