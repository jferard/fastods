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
 * @author Martin Schulz<br>
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file CurrencyStyle.java is part of SimpleODS.
 *
 */
public class CurrencyStyle {
	private Util u = Util.getInstance();
	public final static int NUMBER_CURRENCY = 1;

	public final static int SYMBOLPOSITION_BEGIN = 0;
	public final static int SYMBOLPOSITION_END = 1;

	private String sName = "";
	private String sCurrencySymbol = "€";
	private String sNegativeValueColor = "#FF0000";
	private String sLanguage = "";
	private String sCountry = "";
	private int nNumberType = NUMBER_CURRENCY;
	private int nDecimalPlaces = 2;
	private int nMinIntegerDigits = 1;
	private boolean bGrouping = false;
	private boolean bVolatile = true;
	private boolean bNegativeValuesRed = true;
	private int bCurrencyPosition = SYMBOLPOSITION_END;

	/**
	 * The OdsFile where this object belong to.
	 */
	private OdsFile o;

	/**
	 * Create a new number style with the name sName, default minimum integer
	 * digits is 1 and default decimal places is 2.<br>
	 * Version 0.5.0 Added parameter OdsFile o
	 * 
	 * @param sName
	 *            The name of the number style.
	 * @param odsFile
	 *            The OdsFile to which this style belongs
	 */
	public CurrencyStyle(final String sName, OdsFile odsFile) {
		this.setName(sName);
		this.o = odsFile;
		this.o.getStyles().addCurrencyStyle(this);
	}

	/**
	 * Create a new number style with the name sName, minimum integer digits is
	 * nMinIntDigits and decimal places is nDecPlaces. The number style is
	 * NumberStyle.NUMBER_NORMAL<br>
	 * Version 0.5.0 Added parameter OdsFile o
	 * 
	 * @param sName
	 *            The name of the number style.
	 * @param nMinIntDigits
	 *            The minimum integer digits to be shown.
	 * @param nDecPlaces
	 *            The number of decimal places to be shown.
	 * @param odsFile
	 *            The OdsFile to which this style belongs
	 */
	public CurrencyStyle(final String sName, int nMinIntDigits, int nDecPlaces,
			OdsFile odsFile) {
		this.setName(sName);
		this.setMinIntegerDigits(nMinIntDigits);
		this.setDecimalPlaces(nDecPlaces);
		this.o = odsFile;
		this.o.getStyles().addCurrencyStyle(this);
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
	 * Set how many digits are to the right of the decimal symbol.
	 * 
	 * @param decimalPlaces
	 *            - The number of digits
	 */
	public void setDecimalPlaces(final int decimalPlaces) {
		this.nDecimalPlaces = decimalPlaces;
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
	 * Set how many leading zeros are present.
	 * 
	 * @param minIntegerDigits
	 *            The number of leading zeros
	 */
	public void setMinIntegerDigits(final int minIntegerDigits) {
		this.nMinIntegerDigits = minIntegerDigits;
	}

	public boolean getThousandsSeparator() {
		return this.bGrouping;
	}

	/**
	 * If this is set to true, the thousands separator is shown.
	 * 
	 * @param grouping
	 */
	public void setThousandsSeparator(final boolean grouping) {
		this.bGrouping = grouping;
	}

	/**
	 * Get the name of this currency style.
	 * 
	 * @return The currency style name
	 */
	public String getName() {
		return this.sName;
	}

	/**
	 * Set the name of this style to sName.
	 * 
	 * @param name
	 *            - The name of this style
	 */
	public void setName(final String name) {
		this.sName = name;
	}

	/**
	 * Get the position of the currency symbol.
	 * 
	 * @return either CurrencyStyle.SYMBOLPOSITION_BEGIN or
	 *         CurrencyStyle.SYMBOLPOSITION_END
	 */
	public int getCurrencySymbolPosition() {
		return (this.bCurrencyPosition);
	}

	/**
	 * Set the position of the currency symbol, either
	 * CurrencyStyle.SYMBOLPOSITION_BEGIN or CurrencyStyle.SYMBOLPOSITION_END.
	 * 
	 * @param nPos
	 */
	public void setCurrencySymbolPosition(int nPos) {
		this.bCurrencyPosition = nPos;
	}

	public String getNegativeValueColor() {
		return this.sNegativeValueColor;
	}

	public void setNegativeValueColor(String negativeValueColor) {
		this.sNegativeValueColor = negativeValueColor;
	}

	/**
	 * @return The currency symbol that is used. e.g. '$'.
	 */
	public String getCurrencySymbol() {
		return this.sCurrencySymbol;
	}

	/**
	 * Change the currency symbol, e.g. '$'.
	 * 
	 * @param currencySymbol
	 */
	public void setCurrencySymbol(String currencySymbol) {
		this.sCurrencySymbol = this.u.toXmlString(currencySymbol);
	}

	/**
	 * @return The two letter language code, e.g. 'en'.
	 */
	public String getLanguage() {
		return this.sLanguage;
	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 * 
	 * @param language
	 *            The two letter language code, e.g. 'en'
	 */
	public void setLanguage(String language) {
		this.sLanguage = language.toLowerCase();
	}

	/**
	 * @return The two letter country code, e.g. 'US'
	 */
	public String getCountry() {
		return this.sCountry;
	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 * 
	 * @param country
	 *            The two letter country code, e.g. 'US'
	 */
	public void setCountry(String country) {
		this.sCountry = country.toUpperCase();
	}

	private void appendCurrencySymbol(StringBuilder sb) {
		sb.append("<number:currency-symbol ");
		if (this.sLanguage.length() > 0)
			sb.append("number:language=\"" + this.sLanguage + "\" ");
		if (this.sCountry.length() > 0)
			sb.append("number:country=\"" + this.sCountry + "\" ");
		sb.append(">");
		sb.append("\"" + this.getCurrencySymbol()
				+ "\"</number:currency-symbol>");

		return;
	}

	private void appendCurrencyNumber(StringBuilder sb) {
		sb.append("<number:number number:decimal-places=\""
				+ this.nDecimalPlaces + "\" ");
		sb.append("number:min-integer-digits=\"" + this.nMinIntegerDigits
				+ "\" ");
		if (this.bGrouping) {
			sb.append("number:grouping=\"" + this.bGrouping + "\"");
		}
		sb.append("/>");

		return;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	public String toXML() {
		StringBuilder sbReturn = new StringBuilder();

		// Check where the currency symbol should be positioned
		if (this.bCurrencyPosition == SYMBOLPOSITION_END) {
			sbReturn.append("<number:currency-style ");
			sbReturn.append("style:name=\"" + this.sName + "nn" + "\" ");
			sbReturn.append("style:volatile=\"true\">");
			this.appendCurrencyNumber(sbReturn);
			sbReturn.append("<number:text> </number:text>");
			this.appendCurrencySymbol(sbReturn);
			sbReturn.append("</number:currency-style>");

			// For negative values, this is the default style and
			// this.sName+'nn' is
			// the style for positive values
			sbReturn.append("<number:currency-style ");
			sbReturn.append("style:name=\"" + this.sName + "\">");
			sbReturn.append("<style:text-properties fo:color=\""
					+ this.sNegativeValueColor + "\"/>");
			sbReturn.append("<number:text>-</number:text>");
			this.appendCurrencyNumber(sbReturn);
			sbReturn.append("<number:text> </number:text>");
			this.appendCurrencySymbol(sbReturn);
			sbReturn.append(
					"<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\""
							+ this.sName + "nn" + "\"/>");
			sbReturn.append("</number:currency-style>");
		} else { // SYMBOLPOSITION_BEGIN
			sbReturn.append("<number:currency-style ");
			sbReturn.append("style:name=\"" + this.sName + "nn" + "\" ");
			sbReturn.append("style:volatile=\"true\">");
			this.appendCurrencySymbol(sbReturn);
			this.appendCurrencyNumber(sbReturn);
			sbReturn.append("</number:currency-style>");

			// For negative values, this is the default style and
			// this.sName+'nn' is
			// the style for positive values
			sbReturn.append("<number:currency-style ");
			sbReturn.append("style:name=\"" + this.sName + "\">");
			sbReturn.append("<style:text-properties fo:color=\""
					+ this.sNegativeValueColor + "\"/>");
			sbReturn.append("<number:text>-</number:text>");
			this.appendCurrencySymbol(sbReturn);
			this.appendCurrencyNumber(sbReturn);
			sbReturn.append(
					"<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\""
							+ this.sName + "nn" + "\"/>");
			sbReturn.append("</number:currency-style>");
		}

		return (sbReturn.toString());
	}

}