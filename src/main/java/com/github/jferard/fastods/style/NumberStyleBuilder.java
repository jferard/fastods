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

import java.util.Locale;

import com.github.jferard.fastods.style.NumberStyle.Type;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file NumberStyleBuilder.java is part of FastODS.
 */
public class NumberStyleBuilder {
	private boolean bGrouping;
	private boolean bNegativeValuesRed;
	private boolean bVolatile;
	private int nDecimalPlaces;
	private int nMinDenominatorDigits;
	private int nMinExponentDigits;
	private int nMinIntegerDigits;
	private int nMinNumeratorDigits;
	private Type numberType;
	private String sCountry;
	private String sLanguage;
	private final String sName;
	private final String sNegativeValueColor;

	/**
	 * Create a new number style with the name sName, minimum integer digits is
	 * nMinIntDigits and decimal places is nDecPlaces.
	 *
	 * @param sName
	 *            The name of the number style, this name must be unique.
	 * @param locale 
	 */
	public NumberStyleBuilder(final String sName, Locale locale) {
		if (sName == null)
			throw new IllegalArgumentException();
		
		this.sName = sName;
		this.sNegativeValueColor = "#FF0000";
		this.sCountry = locale.getCountry();
		this.sLanguage = locale.getLanguage();
		this.numberType = NumberStyle.DEFAULT_TYPE;
		this.nDecimalPlaces = 2;
		this.nMinIntegerDigits = 1;
		this.nMinExponentDigits = 0;
		this.nMinNumeratorDigits = 0;
		this.nMinDenominatorDigits = 0;
		this.bGrouping = false;
		this.bVolatile = false;
		this.bNegativeValuesRed = false;
	}

	public NumberStyle build() {
		return new NumberStyle(this.sName, this.sNegativeValueColor,
				this.sLanguage, this.sCountry, this.numberType,
				this.nDecimalPlaces, this.nMinIntegerDigits,
				this.nMinExponentDigits, this.nMinNumeratorDigits,
				this.nMinDenominatorDigits, this.bGrouping, this.bVolatile,
				this.bNegativeValuesRed);
	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 *
	 * @param country
	 *            The two letter country code, e.g. 'US'
	 * @return this for fluent style
	 */
	public NumberStyleBuilder country(final String country) {
		this.sCountry = country.toUpperCase();
		return this;
	}

	/**
	 * Set how many digits are to the right of the decimal symbol.
	 *
	 * @param decimalPlaces
	 *            - The number of digits
	 * @return this for fluent style
	 */
	public final NumberStyleBuilder decimalPlaces(final int decimalPlaces) {
		this.nDecimalPlaces = decimalPlaces;
		return this;
	}

	/**
	 * Add the numerator and denominator values to be shown.<br>
	 * The number style is set to NUMBER_FRACTION
	 *
	 * @param nNumerator
	 * @param nDenominator
	 * @return this for fluent style
	 */
	public NumberStyleBuilder fractionValues(final int nNumerator,
			final int nDenominator) {
		this.nMinNumeratorDigits = nNumerator;
		this.nMinDenominatorDigits = nDenominator;
		this.numberType = NumberStyle.Type.FRACTION;
		return this;
	}

	/**
	 * Set to true if negative values should be shown in red color.
	 *
	 * @param bValue
	 *            true negative numbers will be shown in red color.
	 * @return
	 * @return this for fluent style
	 *
	 */
	public NumberStyleBuilder negativeValuesRed(final boolean bValue) {
		this.bNegativeValuesRed = bValue;
		this.bVolatile = bValue;
		return this;
	}

	/**
	 * Set the number type for this style.<br>
	 * Valid is one of the following:<br>
	 * NumberStyle.NUMBER_NORMAL<br>
	 * NumberStyle.NUMBER_SCIENTIFIC<br>
	 * NumberStyle.NUMBER_FRACTION<br>
	 * NumberStyle.NUMBER_PERCENTAGE<br>
	 *
	 * @param numberType
	 *            The number type to be used.
	 * @return this for fluent style
	 */
	public NumberStyleBuilder numberType(final Type numberType) {
		this.numberType = numberType;
		return this;
	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * <br>
	 * countries. E.g. set it to country='US' and language='en'
	 *
	 * @param language
	 *            The two letter language code, e.g. 'en'
	 */
	public void setLanguage(final String language) {
		this.sLanguage = language.toLowerCase();
	}

	/**
	 * Set the number of exponent digits.<br>
	 * The number style is set to NUMBER_SCIENTIFIC.
	 *
	 * @param minExponentDigits
	 *            The minimum of exponent digits to be used
	 * @return this for fluent style
	 */
	public NumberStyleBuilder setMinExponentDigits(
			final int minExponentDigits) {
		this.nMinExponentDigits = minExponentDigits;
		this.numberType = NumberStyle.Type.SCIENTIFIC;
		return this;
	}

	/**
	 * Set how many leading zeros are present.
	 *
	 * @param minIntegerDigits
	 *            The number of leading zeros
	 * @return this for fluent style
	 */
	public final NumberStyleBuilder setMinIntegerDigits(
			final int minIntegerDigits) {
		this.nMinIntegerDigits = minIntegerDigits;
		return this;
	}

	/**
	 * If this is set to true, the thousands separator is shown.<br>
	 * The default is false.
	 *
	 * @param grouping
	 *            true, the thousands separator is shown<br>
	 *            false, the thousands separator is not shown
	 * @return this for fluent style
	 */
	public NumberStyleBuilder thousandsSeparator(final boolean grouping) {
		this.bGrouping = grouping;
		return this;
	}
}
