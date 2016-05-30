package com.github.jferard.fastods.style;

public class FrenchDataStyles implements DataStyles {
	private TableCellStyle boolFr;

	public FrenchDataStyles() {
		final BooleanStyle dataBoolFr = BooleanStyle.builder("data-bool-fr").country("FR").language("fr").build();
		this.boolFr = TableCellStyle.builder("bool-fr").dataStyle(dataBoolFr).build();
	}
	
	@Override
	public TableCellStyle getBooleanStyle() {
		return this.boolFr;
	}

	@Override
	public TableCellStyle getDateStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableCellStyle getNumberStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableCellStyle getPercentageStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableCellStyle getTimeStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableCellStyle getCurrencyStyle() {
		// TODO Auto-generated method stub
		return null;
	}
}
