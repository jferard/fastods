/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
package com.github.jferard.fastods.datastyle;

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
public class ScientificNumberStyle extends NumberStyle {
	private final int decimalPlaces;
	private final int minExponentDigits;

	/**
	 * Create a new number style with the name name, minimum integer digits is
	 * minIntDigits and decimal places is decPlaces.
	 *
	 * @param styleName
	 *            The name of the number style, this name must be unique.
	 * @param minIntDigits
	 *            The minimum integer digits to be shown.
	 * @param decPlaces
	 *            The number of decimal places to be shown.
	 */
	ScientificNumberStyle(final String name, final String languageCode,
			final String countryCode, final boolean volatileStyle,
			final boolean grouping, final int minIntegerDigits,
			final String negativeValueColor, final int decimalPlaces,
			final int minExponentDigits) {
		super(name, languageCode, countryCode, volatileStyle, grouping,
				minIntegerDigits, negativeValueColor);
		this.decimalPlaces = decimalPlaces;
		this.minExponentDigits = minExponentDigits;
	}

	public int getDecimalPlaces() {
		return this.decimalPlaces;
	}

	/**
	 * Get the current number of leading zeros.
	 *
	 * @return The current number of leading zeros.
	 */
	public int getMinExponentDigits() {
		return this.minExponentDigits;
	}

	@Override
	protected void appendNumber(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<number:scientific-number");
		util.appendEAttribute(appendable, "number:min-exponent-digits",
				this.minExponentDigits);
		util.appendEAttribute(appendable, "number:decimal-places",
				this.getDecimalPlaces());
		this.appendNumberAttribute(util, appendable);
		appendable.append("/>");
	}
}
