package com.github.jferard.fastods.datastyle;

import java.util.Locale;

import com.github.jferard.fastods.util.XMLUtil;

public class DataStyleBuilderFactory {
	private final Locale locale;
	private XMLUtil util;

	public DataStyleBuilderFactory(final XMLUtil util, final Locale locale) {
		this.util = util;
		this.locale = locale;
	}

	public BooleanStyleBuilder booleanStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new BooleanStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public CurrencyStyleBuilder currencyStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new CurrencyStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public DateStyleBuilder dateStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new DateStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public FloatStyleBuilder floatStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new FloatStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public FractionStyleBuilder fractionStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new FractionStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public PercentageStyleBuilder percentageStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new PercentageStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}

	public ScientificNumberStyleBuilder scientificNumberStyleBuilder(
			final String name) {
		if (name == null)
			throw new IllegalArgumentException();
		return new ScientificNumberStyleBuilder(
				this.util.escapeXMLAttribute(name), this.locale);
	}

	public TimeStyleBuilder timeStyleBuilder(final String name) {
		return new TimeStyleBuilder(this.util.escapeXMLAttribute(name),
				this.locale);
	}
}
