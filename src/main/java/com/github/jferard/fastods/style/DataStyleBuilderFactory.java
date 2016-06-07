package com.github.jferard.fastods.style;

import java.util.Locale;

public class DataStyleBuilderFactory {
	private final Locale locale;

	public DataStyleBuilderFactory(Locale locale) {
		this.locale = locale;
	}

	public BooleanStyleBuilder booleanStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new BooleanStyleBuilder(name, this.locale);
	}

	public CurrencyStyleBuilder currencyStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new CurrencyStyleBuilder(name, this.locale);
	}

	public DateStyleBuilder dateStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new DateStyleBuilder(name, this.locale);
	}

	public PercentageStyleBuilder percentageStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new PercentageStyleBuilder(name, this.locale);
	}

	public FloatStyleBuilder floatStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new FloatStyleBuilder(name, this.locale);
	}
	
	public FractionStyleBuilder fractionStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new FractionStyleBuilder(name, this.locale);
	}

	public ScientificNumberStyleBuilder scientificNumberStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new ScientificNumberStyleBuilder(name, this.locale);
	}
	
	public TimeStyleBuilder timeStyleBuilder(final String name) {
		return new TimeStyleBuilder(name, this.locale);
	}
	

}
