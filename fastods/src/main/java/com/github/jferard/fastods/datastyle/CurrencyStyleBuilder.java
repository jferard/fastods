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

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.datastyle.CurrencyStyle.SymbolPosition;

import java.util.Currency;
import java.util.Locale;

/**
 * @author Julien Férard
 */
public class CurrencyStyleBuilder implements DataStyleBuilder<CurrencyStyle, CurrencyStyleBuilder>,
        NumberStyleBuilder<CurrencyStyle, CurrencyStyleBuilder>,
        DecimalStyleBuilder<CurrencyStyleBuilder> {
    private final FloatStyleBuilder floatStyleBuilder;
    private SymbolPosition currencyPosition;
    private String currencySymbol;

    /**
     * The builder
     *
     * @param name   The name of this style
     * @param locale The locale used
     */
    public CurrencyStyleBuilder(final String name, final Locale locale) {
        this.floatStyleBuilder = new FloatStyleBuilder(name, locale);
        this.currencySymbol = Currency.getInstance(locale).getSymbol(locale);
        this.currencyPosition = CurrencyStyle.SymbolPosition.END;
    }

    @Override
    public CurrencyStyle build() {
        return new CurrencyStyle(this.floatStyleBuilder.build(), this.currencySymbol,
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
     * @param symbolPosition the position of the symbol (before or after the value).
     * @return this for fluent style
     */
    public CurrencyStyleBuilder currencySymbolPosition(final SymbolPosition symbolPosition) {
        this.currencyPosition = symbolPosition;
        return this;
    }

    @Override
    public CurrencyStyleBuilder decimalPlaces(final int decimalPlaces) {
        this.floatStyleBuilder.decimalPlaces(decimalPlaces);
        return this;
    }

    @Override
    public CurrencyStyleBuilder groupThousands(final boolean grouping) {
        this.floatStyleBuilder.groupThousands(grouping);
        return this;
    }

    @Override
    public final CurrencyStyleBuilder locale(final Locale locale) {
        this.floatStyleBuilder.locale(locale);
        this.currencySymbol = Currency.getInstance(locale).getSymbol(locale);
        return this;
    }

    @Override
    public CurrencyStyleBuilder minIntegerDigits(final int minIntegerDigits) {
        this.floatStyleBuilder.minIntegerDigits(minIntegerDigits);
        return this;
    }

    @Override
    public CurrencyStyleBuilder negativeValueColor(final Color negativeValueColor) {
        this.floatStyleBuilder.negativeValueColor(negativeValueColor);
        return this;
    }

    @Override
    public CurrencyStyleBuilder negativeValueRed() {
        this.floatStyleBuilder.negativeValueRed();
        return this;
    }

    @Override
    public CurrencyStyleBuilder country(final String countryCode) {
        this.floatStyleBuilder.country(countryCode);
        return this;
    }

    @Override
    public CurrencyStyleBuilder language(final String languageCode) {
        this.floatStyleBuilder.language(languageCode);
        return this;
    }

    @Override
    public CurrencyStyleBuilder volatileStyle(final boolean volatileStyle) {
        this.floatStyleBuilder.volatileStyle(volatileStyle);
        return this;
    }

    @Override
    public CurrencyStyleBuilder visible() {
        this.floatStyleBuilder.visible();
        return this;
    }
}
