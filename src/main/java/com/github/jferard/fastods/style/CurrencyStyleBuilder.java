/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods.style;

import java.util.Currency;
import java.util.Locale;

import com.github.jferard.fastods.style.CurrencyStyle.SymbolPosition;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file CurrencyStyleBuilder.java is part of FastODS.
 *
 */
public class CurrencyStyleBuilder extends FloatStyleBuilder {
	private SymbolPosition currencyPosition;
	private String currencySymbol;

	/**
	 * The builder
	 * 
	 * @param name
	 *            - The name of this style
	 * @param locale
	 */
	protected CurrencyStyleBuilder(final String name, final Locale locale) {
		super(name, locale);
		this.locale(locale);
		this.currencyPosition = CurrencyStyle.SymbolPosition.END;
	}

	/** {@inheritDoc} */
	@Override
	public CurrencyStyle build() {
		return new CurrencyStyle(this.name, this.languageCode,
				this.countryCode, this.volatileStyle, this.decimalPlaces,
				this.grouping, this.minIntegerDigits, this.negativeValueColor,
				this.currencySymbol, this.currencyPosition);
	}

	/**
	 * Change the currency symbol, e.g. '$'.
	 *
	 * @param currencySymbol
	 */
	public CurrencyStyleBuilder currencySymbol(final String currencySymbol) {
		this.currencySymbol = currencySymbol;
		return this;
	}

	/**
	 * Set the position of the currency symbol, either
	 * CurrencyStyle.SYMBOLPOSITION_BEGIN or CurrencyStyle.SYMBOLPOSITION_END.
	 *
	 * @param symbolPosition
	 */
	public CurrencyStyleBuilder currencySymbolPosition(
			final SymbolPosition symbolPosition) {
		this.currencyPosition = symbolPosition;
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public final CurrencyStyleBuilder locale(final Locale locale) {
		super.locale(locale);
		if (this.currencySymbol == null)
			this.currencySymbol = Currency.getInstance(locale).getSymbol();
		return this;
	}
}