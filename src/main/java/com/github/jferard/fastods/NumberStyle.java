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
 *         This file NumberStyle.java is part of FastODS.
 * 
 *         WHERE ?
 *         content.xml/office:document-content/office:automatic-styles/number:
 *         number-style
 *         content.xml/office:document-content/office:automatic-styles/number:
 *         percentage-style
 *         styles.xml/office:document-styles/office:styles/number:number-style
 *         styles.xml/office:document-styles/office:styles/number:percentage-
 *         style
 */
public class NumberStyle implements NamedObject {

	public final static int NUMBER_NORMAL = 1;
	public final static int NUMBER_SCIENTIFIC = 2;
	public final static int NUMBER_FRACTION = 3;
	public final static int NUMBER_PERCENTAGE = 4;

	public static NumberStyleBuilder builder() {
		return new NumberStyleBuilder();
	}

	private final String sName;
	private final String sNegativeValueColor;
	private final String sLanguage;
	private final String sCountry;
	private final int nNumberType;
	private final int nDecimalPlaces;
	private final int nMinIntegerDigits;
	private final int nMinExponentDigits;
	private final int nMinNumeratorDigits;
	private final int nMinDenominatorDigits;
	private final boolean bGrouping;
	private final boolean bVolatile;
	private final boolean bNegativeValuesRed;
	private String xml;

	/**
	 * Create a new number style with the name sName, minimum integer digits is
	 * nMinIntDigits and decimal places is nDecPlaces. The number style is
	 * NumberStyle.NUMBER_NORMAL
	 * 
	 * @param sStyleName
	 *            The name of the number style, this name must be unique.
	 * @param nMinIntDigits
	 *            The minimum integer digits to be shown.
	 * @param nDecPlaces
	 *            The number of decimal places to be shown.
	 */
	NumberStyle(String sName, String sNegativeValueColor, String sLanguage,
			String sCountry, int nNumberType, int nDecimalPlaces,
			int nMinIntegerDigits, int nMinExponentDigits,
			int nMinNumeratorDigits, int nMinDenominatorDigits,
			boolean bGrouping, boolean bVolatile, boolean bNegativeValuesRed) {
		this.sName = sName;
		this.sNegativeValueColor = sNegativeValueColor;
		this.sLanguage = sLanguage;
		this.sCountry = sCountry;
		this.nNumberType = nNumberType;
		this.nDecimalPlaces = nDecimalPlaces;
		this.nMinIntegerDigits = nMinIntegerDigits;
		this.nMinExponentDigits = nMinExponentDigits;
		this.nMinNumeratorDigits = nMinNumeratorDigits;
		this.nMinDenominatorDigits = nMinDenominatorDigits;
		this.bGrouping = bGrouping;
		this.bVolatile = bVolatile;
		this.bNegativeValuesRed = bNegativeValuesRed;
	}

	public void addToFile(OdsFile odsFile) {
		odsFile.getStyles().addNumberStyle(this);
	}

	/**
	 * @return The two letter country code, e.g. 'US'
	 */
	public String getCountry() {
		return this.sCountry;
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
	 * @return The two letter language code, e.g. 'en'
	 */
	public String getLanguage() {
		return this.sLanguage;
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
	 * Get how many leading zeros are present.
	 * 
	 * @return The number of leading zeros
	 */
	public int getMinIntegerDigits() {
		return this.nMinIntegerDigits;
	}

	/**
	 * @return The name of this style.
	 */
	public String getName() {
		return this.sName;
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
	 * Check if this style shows a red color for negative numbers.
	 * 
	 * @return true - for negative numbers the font is red<br>
	 *         false - for negative numbers the font is not red
	 */
	public boolean isNegativeValuesRed() {
		return this.bNegativeValuesRed;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @param util
	 */
	@Override
	public void appendXML(Util util, Appendable appendable) throws IOException {
		if (this.nNumberType == NUMBER_PERCENTAGE) {
			appendable.append("<number:percentage-style");
		} else {
			appendable.append("<number:number-style");
		}

		// Only change the given name if bNegativeValuesRed is true and use
		// this
		// style as default style for positive numbers

		if (this.bNegativeValuesRed)
			util.appendAttribute(appendable, "style:name", this.sName + "nn");
		else
			util.appendAttribute(appendable, "style:name", this.sName);

		if (this.sLanguage.length() > 0) {
			util.appendAttribute(appendable, "number:language", this.sLanguage);
		}
		if (this.sCountry.length() > 0) {
			util.appendAttribute(appendable, "number:country", this.sCountry);
		}
		if (this.bVolatile)
			appendable.append(" style:volatile=\"true\"");
		
		appendable.append(">");
		this.appendNumberType(util, appendable);
		util.appendAttribute(appendable, "number:min-integer-digits", this.nMinIntegerDigits);

		if (this.bGrouping) {
			util.appendAttribute(appendable, "number:grouping", this.bGrouping);
		}
		appendable.append("/>");

		if (this.nNumberType == NUMBER_PERCENTAGE) {
			appendable.append("<number:text>%</number:text>");
			appendable.append("</number:percentage-style>");
		} else {
			appendable.append("</number:number-style>");
		}

		// --------------------------------------------------------------------------
		// For negative values, this is the default style and
		// this.sName+'nn' is
		// the style for positive values
		// --------------------------------------------------------------------------
		if (this.bNegativeValuesRed) {
			if (this.nNumberType == NUMBER_PERCENTAGE) {
				appendable.append("<number:percentage-style");
			} else {
				appendable.append("<number:number-style");
			}

			util.appendAttribute(appendable, "style:name", this.sName);
			
			if (this.sLanguage.length() > 0) {
				util.appendAttribute(appendable, "number:language", this.sLanguage);
			}
			if (this.sCountry.length() > 0) {
				util.appendAttribute(appendable, "number:country", this.sCountry);
			}
			appendable.append(">");
			appendable.append("<style:text-properties");
			util.appendAttribute(appendable, "fo:color", this.sNegativeValueColor);
			appendable.append("/>");
			appendable.append("<number:text>-</number:text>");

			this.appendNumberType(util, appendable);
			util.appendAttribute(appendable, "number:min-integer-digits", this.nMinIntegerDigits);
			if (this.bGrouping) {
				util.appendAttribute(appendable, "number:grouping", this.bGrouping);
			}
			appendable.append("/>");

			if (this.nNumberType == NUMBER_PERCENTAGE) {
				appendable.append("<number:text>%</number:text>");
			}

			appendable
					.append("<style:map style:condition=\"value()&gt;=0\"");
			util.appendAttribute(appendable, "style:apply-style-name", this.sName+"nn");
			appendable.append("/>");

			if (this.nNumberType == NUMBER_PERCENTAGE) {
				appendable.append("</number:percentage-style>");
			} else {
				appendable.append("</number:number-style>");
			}

		}
	}

	/**
	 * Add the number type in XML format to the StringBuilder sb.
	 * 
	 * @param appendable
	 *            The StringBuilder to which the number format is appended.
	 * @throws IOException 
	 */
	private void appendNumberType(Util util, final Appendable appendable) throws IOException {

		switch (this.nNumberType) {
		case NUMBER_SCIENTIFIC:
			appendable.append("<number:scientific-number");
			util.appendAttribute(appendable, "number:min-exponent-digits", this.nMinExponentDigits);
			util.appendAttribute(appendable, "number:decimal-places", this.nDecimalPlaces);
			break;
		case NUMBER_FRACTION:
			appendable.append("<number:fraction");
			util.appendAttribute(appendable, "number:min-numerator-digits", this.nMinNumeratorDigits);
			util.appendAttribute(appendable, "number:min-denominator-digits", this.nMinDenominatorDigits);
		case NUMBER_NORMAL:
		case NUMBER_PERCENTAGE:
		default:
			appendable.append("<number:number");
			util.appendAttribute(appendable, "number:decimal-places", this.nDecimalPlaces);
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
