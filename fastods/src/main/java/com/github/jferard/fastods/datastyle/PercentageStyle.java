/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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
	 * A percentage style
     * @param floatStyle the embedded float style
	 */
	PercentageStyle(final FloatStyle floatStyle) {
		this.floatStyle = floatStyle;
	}

	@Override
	public void appendXMLContent(final XMLUtil util, final Appendable appendable)
			throws IOException {
		final StringBuilder percentage = new StringBuilder();
		this.floatStyle.appendNumberTag(util, percentage);
		percentage.append("<number:text>%</number:text>");
		this.floatStyle.appendXMLHelper(util, appendable, "percentage-style", percentage);
	}

	@Override
	public String getName() {
		return this.floatStyle.getName();
	}

	@Override
	public boolean isHidden() {
		return this.floatStyle.isHidden();
	}

	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addDataStyle(this);
	}
}
