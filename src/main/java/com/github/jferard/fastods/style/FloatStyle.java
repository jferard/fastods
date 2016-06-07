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
 *         This file CurrencyStyle.java is part of FastODS.
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:automatic-styles/number:
 *         currency-style
 *         styles.xml/office:document-styles/office:styles/number:currency-style
 */
public class FloatStyle extends NumberStyle {
	protected final int decimalPlaces;
	
	protected FloatStyle(final String name, final String languageCode,
			final String countryCode, final boolean volatileStyle,
			final int decimalPlaces, final boolean grouping,
			final int minIntegerDigits, final String negativeValueColor) {
		super(name, languageCode, countryCode, volatileStyle, grouping, minIntegerDigits, negativeValueColor);
		this.decimalPlaces = decimalPlaces;
	}

	/**
	 * Get how many digits are to the right of the decimal symbol.
	 *
	 * @return The number of digits
	 */
	public int getDecimalPlaces() {
		return this.decimalPlaces;
	}

	/**
	 * Appends the number:number element
	 * @param util XML escaping util
	 * @param appendable where  to append data
	 * @throws IOException
	 */
	@Override
	protected void appendNumber(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<number:number");
		util.appendEAttribute(appendable, "number:decimal-places",
				this.decimalPlaces);
		this.appendNumberAttribute(util, appendable);
		appendable.append("/>");
	}
}