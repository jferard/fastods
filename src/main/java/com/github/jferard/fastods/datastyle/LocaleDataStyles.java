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

import com.github.jferard.fastods.style.TableCellStyle;

/**
 * @author Julien Férard
 */
public class LocaleDataStyles implements DataStyles {
	private final TableCellStyle booleanCellStyle;
	private final BooleanStyle booleanDataStyle;
	private final TableCellStyle currencyCellStyle;
	private final CurrencyStyle currencyDataStyle;
	private final TableCellStyle dateCellStyle;
	private final DateStyle dateDataStyle;
	private final TableCellStyle numberCellStyle;
	private final FloatStyle numberDataStyle;
	private final TableCellStyle percentageCellStyle;
	private final PercentageStyle percentageDataStyle;
	private final TableCellStyle timeCellStyle;
	private final TimeStyle timeDataStyle;

	public LocaleDataStyles(final DataStyleBuilderFactory builderFactory) {
		this.booleanDataStyle = builderFactory
				.booleanStyleBuilder("boolean-data").build();
		this.booleanCellStyle = TableCellStyle.builder("boolean-style")
				.dataStyle(this.booleanDataStyle).build();
		this.currencyDataStyle = builderFactory
				.currencyStyleBuilder("currency-data").build();
		this.currencyCellStyle = TableCellStyle.builder("currency-style")
				.dataStyle(this.currencyDataStyle).build();
		this.dateDataStyle = builderFactory
				.dateStyleBuilder("date-data").build();
		this.dateCellStyle = TableCellStyle.builder("date-style")
				.dataStyle(this.dateDataStyle).build();
		this.numberDataStyle = builderFactory
				.floatStyleBuilder("float-data").build();
		this.numberCellStyle = TableCellStyle.builder("number-style")
				.dataStyle(this.numberDataStyle).build();
		this.percentageDataStyle = builderFactory
				.percentageStyleBuilder("percentage-data").build();
		this.percentageCellStyle = TableCellStyle.builder("percentage-style")
				.dataStyle(this.percentageDataStyle).build();
		this.timeDataStyle =
				builderFactory
						.timeStyleBuilder("time-data").build();
		this.timeCellStyle = TableCellStyle.builder("time-style")
				.dataStyle(this.timeDataStyle).build();
	}

	@Override
	public TableCellStyle getBooleanCellStyle() {
		return this.booleanCellStyle;
	}

	@Override
	public BooleanStyle getBooleanDataStyle() {
		return this.booleanDataStyle;
	}

	@Override
	public TableCellStyle getCurrencyCellStyle() {
		return this.currencyCellStyle;
	}

	@Override
	public CurrencyStyle getCurrencyDataStyle() {
		return this.currencyDataStyle;
	}

	@Override
	public TableCellStyle getDateCellStyle() {
		return this.dateCellStyle;
	}

	@Override
	public DateStyle getDateDataStyle() {
		return this.dateDataStyle;
	}

	@Override
	public TableCellStyle getNumberCellStyle() {
		return this.numberCellStyle;
	}

	@Override
	public FloatStyle getNumberDataStyle() {
		return this.numberDataStyle;
	}

	@Override
	public TableCellStyle getPercentageCellStyle() {
		return this.percentageCellStyle;
	}

	@Override
	public PercentageStyle getPercentageDataStyle() {
		return this.percentageDataStyle;
	}

	@Override
	public TableCellStyle getTimeCellStyle() {
		return this.timeCellStyle;
	}

	@Override
	public TimeStyle getTimeDataStyle() {
		return this.timeDataStyle;
	}
}
