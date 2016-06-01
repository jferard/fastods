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

import java.io.IOException;
import java.util.Locale;

import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file CurrencyStyle.java is part of FastODS.
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:automatic-styles/number:
 *         currency-style
 *         styles.xml/office:document-styles/office:styles/number:currency-style
 */
public class CurrencyStyle implements DataStyle {
	// public final static int NUMBER_CURRENCY = 1;
	public static enum SymbolPosition {
		BEGIN, END;
	}

	public static CurrencyStyleBuilder builder(String sName) {
		if (sName == null)
			throw new IllegalArgumentException();
		return new CurrencyStyleBuilder(sName, Locale.getDefault());
	}

	private final boolean bGrouping;
	private final boolean bNegativeValuesRed;
	private final boolean bVolatile;
	private final SymbolPosition currencyPosition;
	private final int nDecimalPlaces;
	private final int nMinIntegerDigits;
	private final String sCountry;
	private final String sLanguage;
	private final String sCurrencySymbol;
	private final String sName;
	private final String sNegativeValueColor;

	protected CurrencyStyle(final String sName, final String sCurrencySymbol,
			final String sNegativeValueColor, final String sLanguage,
			final String sCountry, final int nDecimalPlaces,
			final int nMinIntegerDigits, final boolean bGrouping,
			final boolean bVolatile, final boolean bNegativeValuesRed,
			final SymbolPosition currencyPosition) {
		this.sName = sName;
		this.sCurrencySymbol = sCurrencySymbol;
		this.sNegativeValueColor = sNegativeValueColor;
		this.sLanguage = sLanguage;
		this.sCountry = sCountry;
		this.nDecimalPlaces = nDecimalPlaces;
		this.nMinIntegerDigits = nMinIntegerDigits;
		this.bGrouping = bGrouping;
		this.bVolatile = bVolatile;
		this.bNegativeValuesRed = bNegativeValuesRed;
		this.currencyPosition = currencyPosition;
	}

	@Override
	public void addToFile(final OdsFile odsFile) {
		odsFile.addDataStyle(this);
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @return The XML string for this object.
	 */
	@Override
	public void appendXMLToStylesEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		final StringBuilder currency = this.currencyToXML(util);

		appendable.append("<number:currency-style");
		util.appendAttribute(appendable, "style:name", this.sName + "nn");
		util.appendEAttribute(appendable, "style:volatile", true);
		if (this.sLanguage != null)
			util.appendAttribute(appendable, "number:language", this.sLanguage);
		if (this.sCountry != null)
			util.appendAttribute(appendable, "number:country", this.sCountry);
		appendable.append(">");
		appendable.append(currency);
		appendable.append("</number:currency-style>");

		// For negative values, this is the default style and
		// this.sName+'nn' is
		// the style for positive values
		appendable.append("<number:currency-style");
		util.appendAttribute(appendable, "style:name", this.sName);
		if (this.sLanguage != null)
			util.appendAttribute(appendable, "number:language", this.sLanguage);
		if (this.sCountry != null)
			util.appendAttribute(appendable, "number:country", this.sCountry);
		appendable.append(">");
		appendable.append("<style:text-properties");
		util.appendAttribute(appendable, "fo:color", this.sNegativeValueColor);
		appendable.append("/>");
		appendable.append("<number:text>-</number:text>");

		appendable.append(currency);
		appendable.append("<style:map");
		util.appendAttribute(appendable, "style:condition", "value()>=0");
		util.appendAttribute(appendable, "style:apply-style-name",
				this.sName + "nn");
		appendable.append("/></number:currency-style>");
	}

	/**
	 * @return The two letter country code, e.g. 'US'
	 */
	public String getCountry() {
		return this.sCountry;
	}

	/**
	 * @return The currency symbol that is used. e.g. '$'.
	 */
	public String getCurrencySymbol() {
		return this.sCurrencySymbol;
	}

	/**
	 * Get the position of the currency symbol.
	 *
	 * @return either CurrencyStyle.SYMBOLPOSITION_BEGIN or
	 *         CurrencyStyle.SYMBOLPOSITION_END
	 */
	public SymbolPosition getCurrencySymbolPosition() {
		return this.currencyPosition;
	}

	/**
	 * Get how many digits are to the right of the decimal symbol.
	 *
	 * @return The number of digits
	 */
	public int getDecimalPlaces() {
		return this.nDecimalPlaces;
	}

	/**
	 * @return The two letter language code, e.g. 'en'.
	 */
	public String getLanguage() {
		return this.sLanguage;
	}

	/**
	 * Get how many leading zeros are present.
	 *
	 * @return The number of leading zeros
	 */
	public int getMinIntegerDigits() {
		return this.nMinIntegerDigits;
	}

	/**
	 * Get the name of this currency style.
	 *
	 * @return The currency style name
	 */
	@Override
	public String getName() {
		return this.sName;
	}

	public String getNegativeValueColor() {
		return this.sNegativeValueColor;
	}

	public boolean getThousandsSeparator() {
		return this.bGrouping;
	}

	private void appendCurrencyNumber(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<number:number");
		util.appendEAttribute(appendable, "number:decimal-places",
				this.nDecimalPlaces);
		util.appendEAttribute(appendable, "number:min-integer-digits",
				this.nMinIntegerDigits);
		if (this.bGrouping)
			util.appendEAttribute(appendable, "number:grouping", this.bGrouping);
		appendable.append("/>");
	}

	private void appendCurrencySymbol(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<number:currency-symbol");
		if (this.sLanguage != null)
			util.appendAttribute(appendable, "number:language", this.sLanguage);
		if (this.sCountry != null)
			util.appendAttribute(appendable, "number:country", this.sCountry);
		if (this.sCurrencySymbol != null) {
			appendable.append(">\"")
					.append(util.escapeXMLContent(this.sCurrencySymbol))
					.append("\"</number:currency-symbol>");
		} else
			appendable.append("/>");
	}

	private StringBuilder currencyToXML(final XMLUtil util) throws IOException {
		final StringBuilder sbReturn = new StringBuilder();
		// Check where the currency symbol should be positioned
		if (this.currencyPosition == SymbolPosition.END) {
			this.appendCurrencyNumber(util, sbReturn);
			sbReturn.append("<number:text> </number:text>");
			this.appendCurrencySymbol(util, sbReturn);
		} else { // SYMBOLPOSITION_BEGIN
			this.appendCurrencySymbol(util, sbReturn);
			this.appendCurrencyNumber(util, sbReturn);
		}
		return sbReturn;
	}
}