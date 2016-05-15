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
package com.github.jferard.fastods;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file CurrencyStyleBuilder.java is part of FastODS.
 *
 */
class CurrencyStyleBuilder {
	private String sName;
	private String sCurrencySymbol;
	private String sNegativeValueColor;
	private String sLanguage;
	private String sCountry;
	private int nNumberType;
	private int nDecimalPlaces;
	private int nMinIntegerDigits;
	private boolean bGrouping;
	private boolean bVolatile;
	private boolean bNegativeValuesRed;
	private int bCurrencyPosition;

	/**
	 * The builder
	 */
	protected CurrencyStyleBuilder() {
		this.sCurrencySymbol = "€";
		this.sNegativeValueColor = "#FF0000";
		this.sLanguage = "";
		this.sCountry = "";
		this.nNumberType = CurrencyStyle.NUMBER_CURRENCY;
		this.nDecimalPlaces = 2;
		this.nMinIntegerDigits = 1;
		this.bGrouping = false;
		this.bVolatile = true;
		this.bNegativeValuesRed = true;
		this.bCurrencyPosition = CurrencyStyle.SYMBOLPOSITION_END;
	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 * 
	 * @param country
	 *            The two letter country code, e.g. 'US'
	 */
	public CurrencyStyleBuilder country(String country) {
		this.sCountry = country.toUpperCase();
		return this;
	}

	/**
	 * Change the currency symbol, e.g. '$'.
	 * 
	 * @param currencySymbol
	 */
	public CurrencyStyleBuilder currencySymbol(String currencySymbol) {
		this.sCurrencySymbol = currencySymbol;
		return this;
	}

	/**
	 * Set the position of the currency symbol, either
	 * CurrencyStyle.SYMBOLPOSITION_BEGIN or CurrencyStyle.SYMBOLPOSITION_END.
	 * 
	 * @param nPos
	 */
	public CurrencyStyleBuilder currencySymbolPosition(int nPos) {
		this.bCurrencyPosition = nPos;
		return this;
	}

	/**
	 * Set how many digits are to the right of the decimal symbol.
	 * 
	 * @param decimalPlaces
	 *            - The number of digits
	 */
	public CurrencyStyleBuilder decimalPlaces(final int decimalPlaces) {
		this.nDecimalPlaces = decimalPlaces;
		return this;
	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 * 
	 * @param language
	 *            The two letter language code, e.g. 'en'
	 */
	public CurrencyStyleBuilder language(String language) {
		this.sLanguage = language.toLowerCase();
		return this;
	}

	/**
	 * Set how many leading zeros are present.
	 * 
	 * @param minIntegerDigits
	 *            The number of leading zeros
	 */
	public CurrencyStyleBuilder minIntegerDigits(final int minIntegerDigits) {
		this.nMinIntegerDigits = minIntegerDigits;
		return this;
	}

	/**
	 * Set the name of this style to sName.
	 * 
	 * @param name
	 *            - The name of this style
	 */
	public CurrencyStyleBuilder name(final String name) {
		this.sName = name;
		return this;
	}

	public CurrencyStyleBuilder negativeValueColor(String negativeValueColor) {
		this.sNegativeValueColor = negativeValueColor;
		return this;
	}

	/**
	 * If this is set to true, the thousands separator is shown.
	 * 
	 * @param grouping
	 */
	public CurrencyStyleBuilder thousandsSeparator(final boolean grouping) {
		this.bGrouping = grouping;
		return this;
	}

	public CurrencyStyle build() {
		if (this.sName == null)
			throw new IllegalArgumentException();
		
		return new CurrencyStyle(
				this.sName,
				this.sCurrencySymbol,
				this.sNegativeValueColor,
				this.sLanguage,
				this.sCountry,
				this.nNumberType,
				this.nDecimalPlaces,
				this.nMinIntegerDigits,
				this.bGrouping,
				this.bVolatile,
				this.bNegativeValuesRed,
				this.bCurrencyPosition
				);
	}
}