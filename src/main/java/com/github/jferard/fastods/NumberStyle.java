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
 * TODO : clean code
 * @author Martin Schulz<br>
 * 
 * Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net><br>
 * 
 * This file NumberStyle.java is part of SimpleODS.
 *
 */
/**
 * @author martin
 *
 */
public class NumberStyle implements NamedObject {

	public final static int NUMBER_NORMAL = 1;
	public final static int NUMBER_SCIENTIFIC = 2;
	public final static int NUMBER_FRACTION = 3;
	public final static int NUMBER_PERCENTAGE = 4;

	private String sName = "";
	private String sNegativeValueColor = "#FF0000";
	private String sLanguage = "";
	private String sCountry = "";
	private int nNumberType = NUMBER_NORMAL;
	private int nDecimalPlaces = 2;
	private int nMinIntegerDigits = 1;
	private int nMinExponentDigits = 0;
	private int nMinNumeratorDigits = 0;
	private int nMinDenominatorDigits = 0;
	private boolean bGrouping = false;
	private boolean bVolatile = false;
	private boolean bNegativeValuesRed = false;

	/**
	 * The OdsFile where this object belong to.
	 */
	private OdsFile o;

	/**
	 * Create a new number style with the name sName, default minimum integer
	 * digits is 1 and default decimal places is 2.<br>
	 * Version 0.5.0 Added parameter OdsFile o
	 * 
	 * @param sStyleName
	 *            The name of the number style, this name must be unique.
	 * @param odsFile
	 *            The OdsFile to which this style belongs to.
	 */
	public NumberStyle(final String sStyleName, final OdsFile odsFile) {
		this.setName(sStyleName);
		this.o = odsFile;
		this.o.getStyles().addNumberStyle(this);
	}

	/**
	 * Create a new number style with the name sName, minimum integer digits is
	 * nMinIntDigits and decimal places is nDecPlaces. The number style is
	 * NumberStyle.NUMBER_NORMAL<br>
	 * Version 0.5.0 Added parameter OdsFile o
	 * 
	 * @param sStyleName
	 *            The name of the number style, this name must be unique.
	 * @param nMinIntDigits
	 *            The minimum integer digits to be shown.
	 * @param nDecPlaces
	 *            The number of decimal places to be shown.
	 * @param odsFile
	 *            The OdsFile to which this style belongs to.
	 */
	public NumberStyle(final String sStyleName, final int nMinIntDigits,
			final int nDecPlaces, final OdsFile odsFile) {
		this.setName(sStyleName);
		this.setMinIntegerDigits(nMinIntDigits);
		this.setDecimalPlaces(nDecPlaces);
		this.o = odsFile;
		this.o.getStyles().addNumberStyle(this);
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
	public final void setDecimalPlaces(final int decimalPlaces) {
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
	public final void setMinIntegerDigits(final int minIntegerDigits) {
		this.nMinIntegerDigits = minIntegerDigits;
	}

	/**
	 * Get the current number of leading zeros.
	 * 
	 * @return The current number of leading zeros.
	 */
	public int getMinExponentDigits() {
		return this.nMinExponentDigits;
	}

	/**
	 * Set the number of exponent digits.<br>
	 * The number style is set to NUMBER_SCIENTIFIC.
	 * 
	 * @param minExponentDigits
	 *            The minimum of exponent digits to be used
	 */
	public void setMinExponentDigits(final int minExponentDigits) {
		this.nMinExponentDigits = minExponentDigits;
		this.nNumberType = NUMBER_SCIENTIFIC;
	}

	/**
	 * Add the numerator and denominator values to be shown.<br>
	 * The number style is set to NUMBER_FRACTION
	 * 
	 * @param nNumerator
	 * @param nDenominator
	 */
	public void setFractionValues(final int nNumerator,
			final int nDenominator) {
		this.nMinNumeratorDigits = nNumerator;
		this.nMinDenominatorDigits = nDenominator;
		this.nNumberType = NUMBER_FRACTION;
	}

	/**
	 * Set the number type for this style.<br>
	 * Valid is one of the following:<br>
	 * NumberStyle.NUMBER_NORMAL<br>
	 * NumberStyle.NUMBER_SCIENTIFIC<br>
	 * NumberStyle.NUMBER_FRACTION<br>
	 * NumberStyle.NUMBER_PERCENTAGE<br>
	 * 
	 * @param nType
	 *            The number type to be used.
	 */
	public void setNumberType(final int nType) {
		this.nNumberType = nType;
	}

	/**
	 * Get the current status of the thousands separator.
	 * 
	 * @return true The thousands separator will be shown.
	 */
	public boolean getThousandsSeparator() {
		return this.bGrouping;
	}

	/**
	 * If this is set to true, the thousands separator is shown.<br>
	 * The default is false.
	 * 
	 * @param grouping
	 *            true, the thousands separator is shown<br>
	 *            false, the thousands separator is not shown
	 */
	public void setThousandsSeparator(final boolean grouping) {
		this.bGrouping = grouping;
	}

	/**
	 * @return The name of this style.
	 */
	public String getName() {
		return this.sName;
	}

	/**
	 * Set the name of this style to sName, this name must be unique.
	 * 
	 * @param name
	 *            - The name of this style.
	 */
	public final void setName(final String name) {
		this.sName = name;
	}

	/**
	 * @return The two letter language code, e.g. 'en'
	 */
	public String getLanguage() {
		return this.sLanguage;
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
	public void setCountry(final String country) {
		this.sCountry = country.toUpperCase();
	}

	/**
	 * Check if this style shows a red color for negative numbers.
	 * 
	 * @return true - for negative numbers the font is red<br>
	 *         false - for negative numbers the font is not red
	 */
	public boolean isNegativeValuesRed() {
		return this.bNegativeValuesRed;
	}

	/**
	 * Set to true if negative values should be shown in red color.
	 * 
	 * @param bValue
	 *            true negative numbers will be shown in red color.
	 */
	public void setNegativeValuesRed(final boolean bValue) {
		this.bNegativeValuesRed = bValue;
		this.bVolatile = bValue;
	}

	/**
	 * Set the number format to percentage.
	 */
	public void setToPercentageStyle() {
		this.nNumberType = NUMBER_PERCENTAGE;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	public String toXML() {
		StringBuilder sbReturn = new StringBuilder();

		if (this.nNumberType == NUMBER_PERCENTAGE) {
			sbReturn.append("<number:percentage-style ");
		} else {
			sbReturn.append("<number:number-style ");
		}

		// Only change the given name if bNegativeValuesRed is true and use this
		// style as default style for positive numbers
		if (this.bNegativeValuesRed) {
			sbReturn.append("style:name=\"").append(this.sName).append("nn\" ");
		} else {
			sbReturn.append("style:name=\"").append(this.sName).append("\" ");
		}
		if (this.sLanguage.length() > 0) {
			sbReturn.append("number:language=\"").append(this.sLanguage)
					.append("\" ");
		}
		if (this.sCountry.length() > 0) {
			sbReturn.append("number:country=\"").append(this.sCountry)
					.append("\" ");
		}

		if (this.bVolatile) {
			sbReturn.append("style:volatile=\"true\">");
		} else {
			sbReturn.append(">");
		}

		this.appendNumberType(sbReturn);

		sbReturn.append("number:min-integer-digits=\"")
				.append(this.nMinIntegerDigits).append("\" ");

		if (this.bGrouping) {
			sbReturn.append("number:grouping=\"").append(this.bGrouping)
					.append("\"");
		}
		sbReturn.append("/>");

		if (this.nNumberType == NUMBER_PERCENTAGE) {
			sbReturn.append("<number:text>%</number:text>");
			sbReturn.append("</number:percentage-style>");
		} else {
			sbReturn.append("</number:number-style>");
		}

		// --------------------------------------------------------------------------
		// For negative values, this is the default style and this.sName+'nn' is
		// the style for positive values
		// --------------------------------------------------------------------------
		if (this.bNegativeValuesRed) {

			if (this.nNumberType == NUMBER_PERCENTAGE) {
				sbReturn.append("<number:percentage-style ");
			} else {
				sbReturn.append("<number:number-style ");
			}

			sbReturn.append("style:name=\"").append(this.sName).append("\" ");
			if (this.sLanguage.length() > 0) {
				sbReturn.append("number:language=\"").append(this.sLanguage)
						.append("\" ");
			}
			if (this.sCountry.length() > 0) {
				sbReturn.append("number:country=\"").append(this.sCountry)
						.append("\" ");
			}
			sbReturn.append(">");
			sbReturn.append("<style:text-properties fo:color=\"")
					.append(this.sNegativeValueColor).append("\"/>");
			sbReturn.append("<number:text>-</number:text>");

			this.appendNumberType(sbReturn);

			sbReturn.append("number:min-integer-digits=\""
					+ this.nMinIntegerDigits + "\" ");
			if (this.bGrouping) {
				sbReturn.append("number:grouping=\"").append(this.bGrouping)
						.append("\"");
			}
			sbReturn.append("/>");

			if (this.nNumberType == NUMBER_PERCENTAGE) {
				sbReturn.append("<number:text>%</number:text>");
			}

			sbReturn.append(
					"<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"")
					.append(this.sName).append("nn\"/>");

			if (this.nNumberType == NUMBER_PERCENTAGE) {
				sbReturn.append("</number:percentage-style>");
			} else {
				sbReturn.append("</number:number-style>");
			}

		}

		return sbReturn.toString();
	}

	/**
	 * Add the number type in XML format to the StringBuilder sb.
	 * 
	 * @param sb
	 *            The StringBuilder to which the number format is appended.
	 */
	private void appendNumberType(final StringBuilder sb) {

		switch (this.nNumberType) {
		case NUMBER_NORMAL:
		case NUMBER_PERCENTAGE:
			sb.append("<number:number ").append("number:decimal-places=\"")
					.append(this.nDecimalPlaces).append("\" ");
			break;
		case NUMBER_SCIENTIFIC:
			sb.append("<number:scientific-number ")
					.append("number:min-exponent-digits=\"")
					.append(this.nMinExponentDigits).append("\" ")
					.append("number:decimal-places=\"")
					.append(this.nDecimalPlaces).append("\" ");
			break;
		case NUMBER_FRACTION:
			sb.append("<number:fraction ")
					.append("number:min-numerator-digits=\"")
					.append(this.nMinNumeratorDigits).append("\" ")
					.append("number:min-denominator-digits=\"")
					.append(this.nMinDenominatorDigits).append("\" ");
		default:
			sb.append("<number:number ");
			sb.append("number:decimal-places=\"").append(this.nDecimalPlaces).append("\" ");
			break;

		/*
		 * <number:date-style style:name="N37"
		 * number:automatic-order="true"> <number:day number:style="long"/>
		 * <number:text>.</number:text> <number:month number:style="long"/>
		 * <number:text>.</number:text> <number:year/></number:date-style>
		 */

		}

	}

}
