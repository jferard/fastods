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
 * WHERE ? content.xml/office:document-content/office:automatic-styles/number:
 * currency-style
 * styles.xml/office:document-styles/office:styles/number:currency-style
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public class CurrencyStyle extends DataStyle {

	public static enum SymbolPosition {
		BEGIN, END;
	}

	private final SymbolPosition currencyPosition;
	private final String currencySymbol;
	private final FloatStyle floatStyle;

	protected CurrencyStyle(final String name, final String languageCode,
			final String countryCode, final boolean volatileStyle,
			final int decimalPlaces, final boolean grouping,
			final int minIntegerDigits, final String negativeValueColor,
			final String currencySymbol,
			final SymbolPosition currencyPosition) {
		super(name, languageCode, countryCode, volatileStyle);
		this.floatStyle = new FloatStyle(name, languageCode, countryCode,
				volatileStyle, decimalPlaces, grouping, minIntegerDigits,
				negativeValueColor);
		this.currencySymbol = currencySymbol;
		this.currencyPosition = currencyPosition;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @return The XML string for this object.
	 */
	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable)
			throws IOException {
		// For negative values, this is the default style name + "-neg"

		appendable.append("<number:currency-style");
		util.appendAttribute(appendable, "style:name", this.name);
		// this.appendVolatileAttribute(util, appendable);
		appendable.append(">");
		this.appendCurrency(util, appendable);
		appendable.append("</number:currency-style>");

		if (this.floatStyle.negativeValueColor != null) {
			appendable.append("<number:currency-style");
			util.appendAttribute(appendable, "style:name", this.name + "-neg");
			// this.appendVolatileAttribute(util, appendable);
			appendable.append(">");
			this.floatStyle.appendStyleColor(util, appendable);
			appendable.append("<number:text>-</number:text>");
			this.appendCurrency(util, appendable);
			this.floatStyle.appendStyleMap(util, appendable);
			appendable.append("</number:currency-style>");
		}
	}

	/**
	 * @return The currency symbol that is used. e.g. '$'.
	 */
	public String getCurrencySymbol() {
		return this.currencySymbol;
	}

	/**
	 * Get the position of the currency symbol.
	 *
	 * @return either CurrencyStyle.SYMBOLPOSITION_BEGIN or
	 *         CurrencyStyle.SYMBOLPOSITION_END
	 */
	public SymbolPosition getCurrencySymbolPosition() {
		return this.currencyPosition;
	}

	private void appendCurrency(final XMLUtil util, final Appendable appendable)
			throws IOException {
		// Check where the currency symbol should be positioned
		if (this.currencyPosition == SymbolPosition.END) {
			this.floatStyle.appendNumber(util, appendable);
			appendable.append("<number:text> </number:text>");
			this.appendCurrencySymbol(util, appendable);
		} else { // SYMBOLPOSITION_BEGIN
			this.appendCurrencySymbol(util, appendable);
			this.floatStyle.appendNumber(util, appendable);
		}
	}

	private void appendCurrencySymbol(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<number:currency-symbol");
		// this.appendLocaleAttributes(util, appendable);
		appendable.append(">")
				.append(util.escapeXMLContent(this.currencySymbol))
				.append("</number:currency-symbol>");
	}
}
