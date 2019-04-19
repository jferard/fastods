/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
	private final NumberStyleHelper numberStyle;
	private final int decimalPlaces;

	/**
	 * Create a float style
	 * @param numberStyle the embedded core data style
	 * @param decimalPlaces the number of digits after the separator
	 */
	public FloatStyle(final NumberStyleHelper numberStyle,
						 final int decimalPlaces) {
		this.numberStyle = numberStyle;
		this.decimalPlaces = decimalPlaces;
	}

	@Override
	public void appendXMLContent(final XMLUtil util, final Appendable appendable) throws IOException {
		final CharSequence number = this.computeNumberTag(util);
		this.numberStyle.appendXMLHelper(util, appendable, "number-style", number);
	}

	/**
     * @param util an util
     * @return the numbre:number tag
     * @throws IOException if an I/O error occurs
     */
    StringBuilder computeNumberTag(final XMLUtil util)
			throws IOException {
		final StringBuilder number = new StringBuilder();
		this.appendNumberTag(util, number);
		return number;
	}

	/**
	 * Append the number:number tag
	 * @param util an util
	 * @param appendable the destination
	 * @throws IOException if an I/O error occurs
	 */
	public void appendNumberTag(final XMLUtil util, final Appendable appendable) throws IOException {
		appendable.append("<number:number");
		this.appendXMLAttributes(util, appendable);
		appendable.append("/>");
	}

	/**
	 * Append number:decimal-places, number:min-integer-digits and number:grouping
	 * @param util an XML util
	 * @param appendable the appendable
	 * @throws IOException if an I/O error occurs
	 */
	void appendXMLAttributes(final XMLUtil util, final Appendable appendable) throws IOException {
		util.appendAttribute(appendable, "number:decimal-places",
				this.decimalPlaces);
		this.numberStyle.appendNumberAttribute(util, appendable);
	}

	/**
     * A helper to create the XML representation of the float style
     * @param util a util
     * @param appendable the destination
     * @param numberStyleName the style name ("currency-style", ...)
     * @param number the number itslef
     * @throws IOException if an I/O error occurs
     */
    void appendXMLHelper(final XMLUtil util, final Appendable appendable, final String numberStyleName, final CharSequence number) throws IOException {
		this.numberStyle.appendXMLHelper(util, appendable, numberStyleName, number);
	}

	@Override
	public String getName() {
		return this.numberStyle.getName();
	}

	@Override
	public boolean isHidden() {
		return this.numberStyle.isHidden();
	}

    @Override
    public void addToElements(final OdsElements odsElements) {
        odsElements.addDataStyle(this);
    }
}
