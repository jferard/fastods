/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
 */

package com.github.jferard.fastods.datastyle;

import java.io.IOException;

import com.github.jferard.fastods.odselement.OdsElements;
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
public class ScientificNumberStyle implements DataStyle {
	private final FloatStyle floatStyle;
	private final int minExponentDigits;

	/**
	 * Create a new number style with the name name, minimum integer digits is
	 * minIntDigits and decimal places is decPlaces.
	 */
	ScientificNumberStyle(final FloatStyle floatStyle,
			final int minExponentDigits) {
		this.floatStyle = floatStyle;
		this.minExponentDigits = minExponentDigits;
	}

	/**
	 * Get the current number of leading zeros.
	 *
	 * @return The current number of leading zeros.
	 */
	public int getMinExponentDigits() {
		return this.minExponentDigits;
	}

	protected void appendNumber(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<number:scientific-number");
		util.appendEAttribute(appendable, "number:min-exponent-digits",
				this.minExponentDigits);
		util.appendEAttribute(appendable, "number:decimal-places",
				this.floatStyle.getDecimalPlaces());
		this.floatStyle.appendNumberAttribute(util, appendable);
		appendable.append("/>");
	}

	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addDataStyle(this);
	}

	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable) throws IOException {
		final StringBuilder number = new StringBuilder();
		this.appendNumber(util, number);
		this.floatStyle.appendXML(util, appendable, "number-style", number);

	}

	public int getDecimalPlaces() {
		return this.floatStyle.getDecimalPlaces();
	}

	@Override
	public String getName() {
		return this.floatStyle.getName();
	}

	public String getCountryCode() {
		return this.floatStyle.getCountryCode();
	}

	public String getLanguageCode() {
		return this.floatStyle.getLanguageCode();
	}

	public boolean getGroupThousands() {
		return this.floatStyle.getGroupThousands();
	}

	public int getMinIntegerDigits() {
		return this.floatStyle.getMinIntegerDigits();
	}

	public String getNegativeValueColor() {
		return this.floatStyle.getNegativeValueColor();
	}

	public boolean isVolatileStyle() {
		return this.floatStyle.isVolatileStyle();
	}
}
