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
package com.github.jferard.fastods.datastyle;

import java.io.IOException;

import com.github.jferard.fastods.util.XMLUtil;

/**
 * WHERE ? content.xml/office:document-content/office:automatic-styles/number:
 * number-style
 * content.xml/office:document-content/office:automatic-styles/number:
 * percentage-style
 * styles.xml/office:document-styles/office:styles/number:number-style
 * styles.xml/office:document-styles/office:styles/number:percentage- style
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public class FractionStyle extends NumberStyle {
	private final int minDenominatorDigits;
	private final int minNumeratorDigits;

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
	FractionStyle(final String name, final String languageCode,
			final String countryCode, final boolean volatileStyle,
			final boolean grouping, final int minIntegerDigits,
			final String negativeValueColor, final int minNumeratorDigits,
			final int minDenominatorDigits) {
		super(name, languageCode, countryCode, volatileStyle, grouping,
				minIntegerDigits, negativeValueColor);
		this.minNumeratorDigits = minNumeratorDigits;
		this.minDenominatorDigits = minDenominatorDigits;
	}

	@Override
	protected void appendNumber(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<number:fraction");
		util.appendEAttribute(appendable, "number:min-numerator-digits",
				this.minNumeratorDigits);
		util.appendEAttribute(appendable, "number:min-denominator-digits",
				this.minDenominatorDigits);
		this.appendNumberAttribute(util, appendable);
		appendable.append("/>");
	}
}
