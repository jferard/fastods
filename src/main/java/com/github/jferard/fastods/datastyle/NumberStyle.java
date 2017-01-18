/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * ****************************************************************************/
package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 16.27.2 number:number-style
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public abstract class NumberStyle extends DataStyle {
	/**
	 * 19.348 number:grouping
	 */
	private final boolean grouping;
	/**
	 * 19.352 number:min-integer-digits
	 */
	private final int minIntegerDigits;
	protected final String negativeValueColor;

	/**
	 * Create a new number style with the name name, minimum integer digits is
	 * minIntDigits and decimal places is decPlaces. The number style is
	 * NumberStyle.NUMBER_NORMAL
	 *
	 * @param name
	 *            The name of the number style, this name must be unique.
	 * @param minIntegerDigits
	 *            The minimum integer digits to be shown.
	 * @param decPlaces
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
	 * @param util a util for XML writing
	 */
	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable)
			throws IOException {
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
			this.appendStyleColor(util, appendable);
			appendable.append("<number:text>-</number:text>");
			this.appendNumber(util, appendable);
			this.appendStyleMap(util, appendable);
			appendable.append("</number:number-style>");
		}
	}

	/**
	 * @return true if the digits are grouped
	 */
	public boolean getGroupThousands() {
		return this.grouping;
	}

	/**
	 * Get how many leading zeros are present.
	 *
	 * @return The number of leading zeros
	 */
	public int getMinIntegerDigits() {
		return this.minIntegerDigits;
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
	 * @param util a util for XML writing
	 * @param appendable where to write
	 * @throws IOException If an I/O error occurs
	 */
	protected void appendGroupingAttribute(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.grouping)
			util.appendEAttribute(appendable, "number:grouping", "true");
	}

	protected void appendMinIntegerDigitsAttribute(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.minIntegerDigits > 0)
			util.appendEAttribute(appendable, "number:min-integer-digits",
					this.minIntegerDigits);
	}

	abstract protected void appendNumber(final XMLUtil util,
			final Appendable appendable) throws IOException;

	protected void appendNumberAttribute(final XMLUtil util,
			final Appendable appendable) throws IOException {
		this.appendMinIntegerDigitsAttribute(util, appendable);
		this.appendGroupingAttribute(util, appendable);
	}

	/**
	 * Appends the style color.
	 *
	 * @param util
	 *            XML util
	 * @param appendable
	 *            where to write
	 * @throws IOException If an I/O error occurs
	 */
	protected void appendStyleColor(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:text-properties");
		util.appendAttribute(appendable, "fo:color", this.negativeValueColor);
		appendable.append("/>");
	}

	/**
	 * Appends 16.3 style:map tag.
	 *
	 * @param util
	 *            XML util for escaping
	 * @param appendable
	 *            where to write
	 * @throws IOException If an I/O error occurs
	 */
	protected void appendStyleMap(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:map");
		util.appendAttribute(appendable, "style:condition", "value()>=0");
		util.appendAttribute(appendable, "style:apply-style-name", this.name);
		appendable.append("/>");
	}
}
