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
public class PercentageStyle extends DataStyle {
	private final FloatStyle floatStyle;

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
	PercentageStyle(final String name, final String languageCode,
			final String countryCode, final boolean volatileStyle,
			final int decimalPlaces, final boolean grouping,
			final int minIntegerDigits, final String negativeValueColor) {
		super(name, languageCode, countryCode, volatileStyle);
		this.floatStyle = new FloatStyle(name, languageCode, countryCode,
				volatileStyle, decimalPlaces, grouping, minIntegerDigits,
				negativeValueColor);
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
		appendable.append("<number:percentage-style");
		util.appendAttribute(appendable, "style:name", this.name);
		this.appendLVAttributes(util, appendable);
		appendable.append(">");
		this.floatStyle.appendNumber(util, appendable);
		appendable.append("<number:text>%</number:text>");
		appendable.append("</number:percentage-style>");

		if (this.floatStyle.negativeValueColor != null) {
			appendable.append("<number:percentage-style");
			util.appendAttribute(appendable, "style:name", this.name + "-neg");
			this.appendLVAttributes(util, appendable);
			appendable.append(">");
			this.floatStyle.appendStyleColor(util, appendable);
			appendable.append("<number:text>-</number:text>");
			this.floatStyle.appendNumber(util, appendable);
			appendable.append("<number:text>%</number:text>");
			this.floatStyle.appendStyleMap(util, appendable);
			appendable.append("</number:percentage-style>");
		}
	}
}
