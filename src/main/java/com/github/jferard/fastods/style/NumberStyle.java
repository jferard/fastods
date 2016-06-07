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

import com.github.jferard.fastods.util.XMLUtil;

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
public abstract class NumberStyle extends DataStyle {
	protected final String negativeValueColor;
	/**
	 * 19.348 number:grouping
	 */
	private final boolean grouping;
	/**
	 * 19.352 number:min-integer-digits
	 */
	private final int minIntegerDigits;

	/**
	 * Create a new number style with the name name, minimum integer digits is
	 * minIntDigits and decimal places is nDecPlaces. The number style is
	 * NumberStyle.NUMBER_NORMAL
	 * 
	 * @param sStyleName
	 *            The name of the number style, this name must be unique.
	 * @param minIntDigits
	 *            The minimum integer digits to be shown.
	 * @param nDecPlaces
	 *            The number of decimal places to be shown.
	 */
	NumberStyle(final String name, final String languageCode,
			final String countryCode, final boolean volatileStyle,
			final boolean grouping, final int minIntegerDigits,
			final String negativeValueColor) {
		super(name, languageCode, countryCode, volatileStyle);
		this.grouping = grouping;
		this.negativeValueColor = negativeValueColor;
		this.minIntegerDigits = minIntegerDigits;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @param util
	 */
	@Override
	public void appendXMLToStylesEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<number:number-style");
		util.appendAttribute(appendable, "style:name", this.name);
		this.appendLVAttributes(util, appendable);
		appendable.append(">");
		this.appendNumber(util, appendable);
		appendable.append("</number:number-style>");

		if (this.negativeValueColor != null) {
			appendable.append("<number:number-style");
			util.appendAttribute(appendable, "style:name", this.name + "-neg");
			this.appendLVAttributes(util, appendable);
			appendable.append(">");
			appendStyleColor(util, appendable);
			appendable.append("<number:text>-</number:text>");
			this.appendNumber(util, appendable);
			this.appendStyleMap(util, appendable);
			appendable.append("</number:number-style>");
		}
	}

	abstract protected void appendNumber(final XMLUtil util,
			final Appendable appendable) throws IOException;

	/**
	 * Appends 16.3 <style:map> tag.
	 * 
	 * @param util
	 *            XML util for escaping
	 * @param appendable
	 *            where to write
	 * @throws IOException
	 */
	protected void appendStyleMap(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<style:map");
		util.appendAttribute(appendable, "style:condition", "value()>=0");
		util.appendAttribute(appendable, "style:apply-style-name", this.name);
		appendable.append("/>");
	}

	/**
	 * Appends the style color.
	 * 
	 * @param util
	 *            XML util
	 * @param appendable
	 *            where to write
	 * @throws IOException
	 */
	protected void appendStyleColor(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:text-properties");
		util.appendAttribute(appendable, "fo:color", this.negativeValueColor);
		appendable.append("/>");
	}

	/**
	 * Get the color if negative value. If none, null
	 * 
	 * @return the color in hex format
	 */
	public String getNegativeValueColor() {
		return this.negativeValueColor;
	}

	/**
	 * Append the 19.348 number:grouping attribute. Default = false.
	 * 
	 * @param util
	 * @param appendable
	 * @throws IOException
	 */
	protected void appendGroupingAttribute(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.grouping)
			util.appendEAttribute(appendable, "number:grouping", "true");
	}

	/**
	 * @return true if the digits are grouped
	 */
	public boolean getGroupThousands() {
		return this.grouping;
	}

	protected void appendMinIntegerDigitsAttribute(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.minIntegerDigits > 0)
			util.appendEAttribute(appendable, "number:min-integer-digits",
					this.minIntegerDigits);
	}

	/**
	 * Get how many leading zeros are present.
	 *
	 * @return The number of leading zeros
	 */
	public int getMinIntegerDigits() {
		return this.minIntegerDigits;
	}
	
	protected void appendNumberAttribute(final XMLUtil util,
			final Appendable appendable) throws IOException {
		this.appendMinIntegerDigitsAttribute(util, appendable);
		this.appendGroupingAttribute(util, appendable);
	}
}
