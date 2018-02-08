/*
 * FastODS - a Martin Schulz's SimpleODS fork
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

import java.io.IOException;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * 16.27.7 number:currency-style
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public class CurrencyStyle implements DataStyle {
	/**
	 * A simple space a text
	 */
	public static final String SPACE_TEXT = "<number:text> </number:text>";

    /**
	 * A currency symbol may be at the beginning or the end of the expression
	 */
	public enum SymbolPosition {
		/**
		 * the symbol is before the value
		 */
		BEGIN,
		/**
		 * the symbol is after the value
		 */
		END
	}

	private final SymbolPosition currencyPosition;
	private final String currencySymbol;
	private final FloatStyle floatStyle;

	/**
	 * Create a new CurrencyStyle.
	 * @param floatStyle the embedded float style
	 * @param currencySymbol the symbol
	 * @param currencyPosition the position of the symbol
	 */
	CurrencyStyle(final FloatStyle floatStyle,
			final String currencySymbol,
			final SymbolPosition currencyPosition) {
		this.floatStyle = floatStyle;
		this.currencySymbol = currencySymbol;
		this.currencyPosition = currencyPosition;
	}

	@Override
	public void appendXMLRepresentation(final XMLUtil util, final Appendable appendable)
			throws IOException {
		final CharSequence number = this.computeCurrency(util);
		this.floatStyle.appendXMLHelper(util, appendable, "currency-style", number);
	}

	@Override
	public boolean isHidden() {
		return this.floatStyle.isHidden();
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
	 * @return either CurrencyStyle.SymbolPosition.BEGIN or
	 *         CurrencyStyle.SymbolPosition.END
	 */
	public SymbolPosition getCurrencySymbolPosition() {
		return this.currencyPosition;
	}

	private StringBuilder computeCurrency(final XMLUtil util)
			throws IOException {
		final StringBuilder number = new StringBuilder();

		// Check where the currency symbol should be positioned
		if (this.currencyPosition == SymbolPosition.END) {
			this.floatStyle.appendNumberTag(util, number);
			number.append(SPACE_TEXT);
			this.appendCurrencySymbol(util, number);
		} else { // BEGIN
			this.appendCurrencySymbol(util, number);
			this.floatStyle.appendNumberTag(util, number);
		}
		return number;
	}

	private void appendCurrencySymbol(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<number:currency-symbol");
		// this.appendLocaleAttributes(util, appendable);
		appendable.append(">")
				.append(util.escapeXMLContent(this.currencySymbol))
				.append("</number:currency-symbol>");
	}

	@Override
	public String getName() {
		return this.floatStyle.getName();
	}

	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addDataStyle(this);
	}

	@Override
	public String getCountryCode() {
		return null;
	}

	@Override
	public String getLanguageCode() {
		return null;
	}

    @Override
    public boolean isVolatileStyle() {
        return this.floatStyle.isVolatileStyle();
    }
}
