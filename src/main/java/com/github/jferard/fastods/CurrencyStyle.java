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

import java.io.IOException;

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
public class CurrencyStyle implements NamedObject<StylesEntry> {
	// public final static int NUMBER_CURRENCY = 1;
	public static enum SymbolPosition {
		BEGIN, END;
	}

	public static CurrencyStyleBuilder builder() {
		return new CurrencyStyleBuilder();
	}

	private final String sName;
	private final String sCurrencySymbol;
	private final String sNegativeValueColor;
	private final String sLanguage;
	private final String sCountry;
	private final int nDecimalPlaces;
	private final int nMinIntegerDigits;
	private final boolean bGrouping;
	private final boolean bVolatile;
	private final boolean bNegativeValuesRed;
	private final SymbolPosition currencyPosition;

	/**
	 * The OdsFile where this object belong to.
	 */
	private String xml;

	protected CurrencyStyle(String sName, String sCurrencySymbol,
			String sNegativeValueColor, String sLanguage, String sCountry,
			int nDecimalPlaces, int nMinIntegerDigits, boolean bGrouping,
			boolean bVolatile, boolean bNegativeValuesRed,
			SymbolPosition currencyPosition) {
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

	public void addToFile(OdsFile odsFile) {
		odsFile.getStyles().addCurrencyStyle(this);
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

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	@Override
	public void appendXML(Util util, Appendable appendable, StylesEntry where) throws IOException {
		final StringBuilder currency = this.currencyToXML(util);

		appendable.append("<number:currency-style");
		util.appendAttribute(appendable, "style:name", this.sName + "nn");
		appendable.append(" style:volatile=\"true\">");
		appendable.append(currency);
		appendable.append("</number:currency-style>");

		// For negative values, this is the default style and
		// this.sName+'nn' is
		// the style for positive values
		appendable.append("<number:currency-style");
		util.appendAttribute(appendable, "style:name", this.sName);
		appendable.append(">");
		appendable.append("<style:text-properties");
		util.appendAttribute(appendable, "fo:color", this.sNegativeValueColor);
		appendable.append("/>");
		appendable.append("<number:text>-</number:text>");

		appendable.append(currency);
		appendable.append("<style:map style:condition=\"value()&gt;=0\"");
		util.appendAttribute(appendable, "style:apply-style-name",
				this.sName + "nn");
		appendable.append("/></number:currency-style>");
	}

	private void appendCurrencyNumber(StringBuilder sb) {
		sb.append("<number:number number:decimal-places=\"")
				.append(this.nDecimalPlaces).append("\" ")
				.append("number:min-integer-digits=\"")
				.append(this.nMinIntegerDigits).append("\" ");
		if (this.bGrouping)
			sb.append("number:grouping=\"").append(this.bGrouping).append("\"");

		sb.append("/>");
	}

	private void appendCurrencySymbol(Util util, StringBuilder sb)
			throws IOException {
		sb.append("<number:currency-symbol");
		if (this.sLanguage.length() > 0)
			util.appendAttribute(sb, "number:language", this.sLanguage);
		if (this.sCountry.length() > 0)
			util.appendAttribute(sb, "number:country", this.sCountry);
		sb.append(">");
		sb.append("\"").append(util.escapeXMLContent(this.sCurrencySymbol))
				.append("\"");
		sb.append("</number:currency-symbol>");
	}

	private StringBuilder currencyToXML(Util util) throws IOException {
		StringBuilder sbReturn = new StringBuilder();
		// Check where the currency symbol should be positioned
		if (this.currencyPosition == SymbolPosition.END) {
			this.appendCurrencyNumber(sbReturn);
			sbReturn.append("<number:text> </number:text>");
			this.appendCurrencySymbol(util, sbReturn);
		} else { // SYMBOLPOSITION_BEGIN
			this.appendCurrencySymbol(util, sbReturn);
			this.appendCurrencyNumber(sbReturn);
		}
		return sbReturn;
	}
}