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

import com.github.jferard.fastods.TableCell;

import java.util.EnumMap;

/**
 * The data style for the current locale
 * @author Julien Férard
 */
public class LocaleDataStyles implements DataStyles {
	private final BooleanStyle booleanDataStyle;
	private final CurrencyStyle currencyDataStyle;
	private final EnumMap<TableCell.Type, DataStyle> dataStyleByType;
	private final DateStyle dateDataStyle;
	private final FloatStyle numberDataStyle;
	private final PercentageStyle percentageDataStyle;
	private final TimeStyle timeDataStyle;

	public LocaleDataStyles(final DataStyleBuilderFactory builderFactory) {
		this.dataStyleByType = new EnumMap<TableCell.Type, DataStyle>(TableCell.Type.class);

		this.booleanDataStyle = builderFactory
				.booleanStyleBuilder("boolean-data").build();
		this.dataStyleByType.put(TableCell.Type.BOOLEAN, this.booleanDataStyle);

		this.currencyDataStyle = builderFactory
				.currencyStyleBuilder("currency-data").build();
		this.dataStyleByType.put(TableCell.Type.CURRENCY, this.currencyDataStyle);

		this.dateDataStyle = builderFactory
				.dateStyleBuilder("date-data").build();
		this.dataStyleByType.put(TableCell.Type.DATE, this.dateDataStyle);

		this.numberDataStyle = builderFactory
				.floatStyleBuilder("float-data").build();
		this.dataStyleByType.put(TableCell.Type.FLOAT, this.numberDataStyle);

		this.percentageDataStyle = builderFactory
				.percentageStyleBuilder("percentage-data").build();
		this.dataStyleByType.put(TableCell.Type.PERCENTAGE, this.percentageDataStyle);

		this.timeDataStyle =
				builderFactory
						.timeStyleBuilder("time-data").build();
		this.dataStyleByType.put(TableCell.Type.TIME, this.timeDataStyle);
	}

	@Override
	public BooleanStyle getBooleanDataStyle() {
		return this.booleanDataStyle;
	}

	@Override
	public CurrencyStyle getCurrencyDataStyle() {
		return this.currencyDataStyle;
	}

	@Override
	public DataStyle getDataStyle(final TableCell.Type type) {
		return this.dataStyleByType.get(type);
	}

	@Override
	public DateStyle getDateDataStyle() {
		return this.dateDataStyle;
	}

	@Override
	public FloatStyle getNumberDataStyle() {
		return this.numberDataStyle;
	}

	@Override
	public PercentageStyle getPercentageDataStyle() {
		return this.percentageDataStyle;
	}

	@Override
	public TimeStyle getTimeDataStyle() {
		return this.timeDataStyle;
	}
}
