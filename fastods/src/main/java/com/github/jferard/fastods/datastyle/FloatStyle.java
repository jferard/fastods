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

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * WHERE ? content.xml/office:document-content/office:automatic-styles/number:
 * currency-style
 * styles.xml/office:document-styles/office:styles/number:currency-style
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class FloatStyle implements DataStyle {
	private final NumberStyle numberStyle;
	private final int decimalPlaces;

	/**
	 * Create a float style
	 * @param numberStyle
	 * @param decimalPlaces the number of digits after the separator
	 */
	public FloatStyle(final NumberStyle numberStyle,
						 final int decimalPlaces) {
		this.numberStyle = numberStyle;
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

	@Override
	public String getName() {
		return this.numberStyle.getName();
	}

	public String getCountryCode() {
		return this.numberStyle.getCountryCode();
	}

	public String getLanguageCode() {
		return this.numberStyle.getLanguageCode();
	}

	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addDataStyle(this);
	}

	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable) throws IOException {
		final CharSequence number = this.computeNumberTag(util);
		this.numberStyle.appendXML(util, appendable, "number-style", number);
	}

	@Override
	public boolean isHidden() {
		return this.numberStyle.isHidden();
	}

	StringBuilder computeNumberTag(final XMLUtil util)
			throws IOException {
		final StringBuilder number = new StringBuilder();
		this.appendNumberTag(util, number);
		return number;
	}

	public void appendNumberTag(final XMLUtil util, final Appendable appendable) throws IOException {
		appendable.append("<number:number");
		util.appendEAttribute(appendable, "number:decimal-places",
				this.decimalPlaces);
		this.numberStyle.appendNumberAttribute(util, appendable);
		appendable.append("/>");
	}

	public void appendXML(final XMLUtil util, final Appendable appendable, final String numberStyleName, final CharSequence number) throws IOException {
		this.numberStyle.appendXML(util, appendable, numberStyleName, number);
	}

	public boolean getGroupThousands() {
		return this.numberStyle.getGroupThousands();
	}

	public int getMinIntegerDigits() {
		return this.numberStyle.getMinIntegerDigits();
	}

	public String getNegativeValueColor() {
		return this.numberStyle.getNegativeValueColor();
	}

	public boolean isVolatileStyle() {
		return this.numberStyle.isVolatileStyle();
	}

	void appendNumberAttribute(final XMLUtil util, final Appendable appendable) throws IOException {
		this.numberStyle.appendNumberAttribute(util, appendable);
	}
}
