/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.datastyle.CurrencyStyle.SymbolPosition;

import java.util.Currency;
import java.util.Locale;

/**
 * @author Julien Férard
 */
public class CurrencyStyleBuilder
		extends DataStyleBuilder<CurrencyStyle, CurrencyStyleBuilder> {
	private final FloatStyleBuilder floatStyleBuilder;
	private SymbolPosition currencyPosition;
	private String currencySymbol;

	/**
	 * The builder
	 *
	 * @param name   The name of this style
	 * @param locale The locale used
	 */
	protected CurrencyStyleBuilder(final String name, final Locale locale) {
		super(name, locale);
		this.floatStyleBuilder = new FloatStyleBuilder(name, locale);
		this.locale(locale);
		this.currencyPosition = CurrencyStyle.SymbolPosition.END;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CurrencyStyle build() {
		return new CurrencyStyle(this.name, this.languageCode, this.countryCode,
				this.volatileStyle, this.floatStyleBuilder.decimalPlaces,
				this.floatStyleBuilder.grouping,
				this.floatStyleBuilder.minIntegerDigits,
				this.floatStyleBuilder.negativeValueColor, this.currencySymbol,
				this.currencyPosition);
	}

	/**
	 * Change the currency symbol, e.g. '$'.
	 *
	 * @param currencySymbol 16.27.8 number:currency-symbol
	 * @return this for fluent style
	 */
	public CurrencyStyleBuilder currencySymbol(final String currencySymbol) {
		this.currencySymbol = currencySymbol;
		return this;
	}

	/**
	 * Set the position of the currency symbol
	 *
	 * @param symbolPosition either
	 *                       CurrencyStyle.SYMBOLPOSITION_BEGIN or CurrencyStyle.SYMBOLPOSITION_END.
	 * @return this for fluent style
	 */
	public CurrencyStyleBuilder currencySymbolPosition(
			final SymbolPosition symbolPosition) {
		this.currencyPosition = symbolPosition;
		return this;
	}

	public CurrencyStyleBuilder decimalPlaces(final int decimalPlaces) {
		this.floatStyleBuilder.decimalPlaces(decimalPlaces);
		return this;
	}

	public CurrencyStyleBuilder groupThousands(final boolean grouping) {
		this.floatStyleBuilder.groupThousands(grouping);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final CurrencyStyleBuilder locale(final Locale locale) {
		super.locale(locale);
		this.floatStyleBuilder.locale(locale);
		this.currencySymbol = Currency.getInstance(locale).getSymbol();
		return this;
	}

	public CurrencyStyleBuilder minIntegerDigits(final int minIntegerDigits) {
		this.floatStyleBuilder.minIntegerDigits(minIntegerDigits);
		return this;
	}

	public CurrencyStyleBuilder negativeValueColor(
			final String negativeValueColor) {
		this.floatStyleBuilder.negativeValueColor(negativeValueColor);
		return this;
	}

	public CurrencyStyleBuilder negativeValueRed() {
		this.floatStyleBuilder.negativeValueRed();
		return this;
	}
}
