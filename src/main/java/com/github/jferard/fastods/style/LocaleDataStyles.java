package com.github.jferard.fastods.style;

public class LocaleDataStyles implements DataStyles {
	private TableCellStyle booleanCellStyle;
	private TableCellStyle currencyCellStyle;
	private TableCellStyle dateCellStyle;
	private TableCellStyle numberCellStyle;
	private TableCellStyle percentageCellStyle;
	private TableCellStyle timeCellStyle;

	public LocaleDataStyles() {
		final BooleanStyle booleanDataStyle = BooleanStyle.builder("boolean-data").build();
		this.booleanCellStyle = TableCellStyle.builder("boolean-style").dataStyle(booleanDataStyle).build();
		final CurrencyStyle currencyDataStyle = CurrencyStyle.builder("currency-data").build();
		this.currencyCellStyle = TableCellStyle.builder("currency-style").dataStyle(currencyDataStyle).build();
		final DateStyle dateDataStyle = DateStyle.builder("date-data").build();
		this.dateCellStyle = TableCellStyle.builder("date-style").dataStyle(dateDataStyle).build();
		final NumberStyle numberDataStyle = NumberStyle.builder("number-data").build();
		this.numberCellStyle = TableCellStyle.builder("number-style").dataStyle(numberDataStyle).build();
		final PercentageStyle percentageDataStyle = PercentageStyle.builder("percentage-data").build();
		this.percentageCellStyle = TableCellStyle.builder("percentage-style").dataStyle(percentageDataStyle).build();
		final TimeStyle timeDataStyle = TimeStyle.builder("time-data").build();
		this.timeCellStyle = TableCellStyle.builder("time-style").dataStyle(timeDataStyle).build();
	}
	
	@Override
	public TableCellStyle getBooleanStyle() {
		return this.booleanCellStyle;
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

	@Override
	public TableCellStyle getCurrencyStyle() {
		return this.currencyCellStyle;
	}
}
