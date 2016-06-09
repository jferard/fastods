package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.XMLUtil;

public class LocaleDataStyles implements DataStyles {
	private final TableCellStyle booleanCellStyle;
	private final TableCellStyle currencyCellStyle;
	private final TableCellStyle dateCellStyle;
	private final TableCellStyle numberCellStyle;
	private final TableCellStyle percentageCellStyle;
	private final TableCellStyle timeCellStyle;

	public LocaleDataStyles(final DataStyleBuilderFactory builderFactory,
			final XMLUtil util) {

		final BooleanStyle booleanDataStyle = builderFactory
				.booleanStyleBuilder("boolean-data").build();
		this.booleanCellStyle = TableCellStyle.builder(util, "boolean-style")
				.dataStyle(booleanDataStyle).build();
		final CurrencyStyle currencyDataStyle = builderFactory
				.currencyStyleBuilder("currency-data").build();
		this.currencyCellStyle = TableCellStyle.builder(util, "currency-style")
				.dataStyle(currencyDataStyle).build();
		final DateStyle dateDataStyle = builderFactory
				.dateStyleBuilder("date-data").build();
		this.dateCellStyle = TableCellStyle.builder(util, "date-style")
				.dataStyle(dateDataStyle).build();
		final FloatStyle numberDataStyle = builderFactory
				.floatStyleBuilder("float-data").build();
		this.numberCellStyle = TableCellStyle.builder(util, "number-style")
				.dataStyle(numberDataStyle).build();
		final PercentageStyle percentageDataStyle = builderFactory
				.percentageStyleBuilder("percentage-data").build();
		this.percentageCellStyle = TableCellStyle
				.builder(util, "percentage-style")
				.dataStyle(percentageDataStyle).build();
		final TimeStyle timeDataStyle = builderFactory
				.timeStyleBuilder("time-data").build();
		this.timeCellStyle = TableCellStyle.builder(util, "time-style")
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
