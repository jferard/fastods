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
public class PercentageStyle implements DataStyle {
	private final FloatStyle floatStyle;

	/**
	 */
	PercentageStyle(final FloatStyle floatStyle) {
		this.floatStyle = floatStyle;
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
		final StringBuilder percentage = new StringBuilder();
		this.floatStyle.appendNumberTag(util, percentage);
		percentage.append("<number:text>%</number:text>");
		this.floatStyle.appendXML(util, appendable, "percentage-style", percentage);
	}

	@Override
	public boolean isHidden() {
		return this.floatStyle.isHidden();
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

	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addDataStyle(this);
	}
}
