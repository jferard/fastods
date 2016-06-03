package com.github.jferard.fastods.style;

import java.util.Locale;

public class DataStyleBuilderFactory {
	private final Locale locale;

	public DataStyleBuilderFactory(Locale locale) {
		this.locale = locale;
	}

	public BooleanStyleBuilder booleanStyleBuilder(final String sName) {
		if (sName == null)
			throw new IllegalArgumentException();
		return new BooleanStyleBuilder(sName, this.locale);
	}

	public CurrencyStyleBuilder currencyStyleBuilder(final String sName) {
		if (sName == null)
			throw new IllegalArgumentException();
		return new CurrencyStyleBuilder(sName, this.locale);
	}

	public DateStyleBuilder dateStyleBuilder(final String sName) {
		if (sName == null)
			throw new IllegalArgumentException();
		return new DateStyleBuilder(sName, this.locale);
	}

	public PercentageStyleBuilder percentageStyleBuilder(final String sName) {
		if (sName == null)
			throw new IllegalArgumentException();
		return new PercentageStyleBuilder(sName, this.locale);
	}

	public NumberStyleBuilder numberStyleBuilder(final String sName) {
		if (sName == null)
			throw new IllegalArgumentException();
		return new NumberStyleBuilder(sName, this.locale);
	}

	public TimeStyleBuilder timeStyleBuilder(final String sName) {
		return new TimeStyleBuilder(sName, this.locale);
	}
	

}
