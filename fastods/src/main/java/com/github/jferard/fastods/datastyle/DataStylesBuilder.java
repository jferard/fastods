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

import java.util.Locale;

/**
 * The data style for the current locale
 *
 * @author Julien Férard
 */
public class DataStylesBuilder {
    /**
     * @param locale the locale
     * @return the DataStyles associated with this locale
     */
    public static DataStylesBuilder create(final Locale locale) {
        final BooleanStyleBuilder booleanDataStyle = new BooleanStyleBuilder("boolean-data", locale);
        final CurrencyStyleBuilder currencyDataStyle = new CurrencyStyleBuilder("currency-data", locale);
        final DateStyleBuilder dateDataStyle = new DateStyleBuilder("date-data", locale);
        final FloatStyleBuilder numberDataStyle = new FloatStyleBuilder("float-data", locale);
        final PercentageStyleBuilder percentageDataStyle = new PercentageStyleBuilder("percentage-data", locale);
        final TimeStyleBuilder timeDataStyle = new TimeStyleBuilder("time-data", locale);
        return new DataStylesBuilder(booleanDataStyle, currencyDataStyle, dateDataStyle, numberDataStyle,
                percentageDataStyle, timeDataStyle);
    }

    private final BooleanStyleBuilder booleanStyleBuilder;
    private final CurrencyStyleBuilder currencyStyleBuilder;
    private final DateStyleBuilder dateDataStyleBuilder;
    private final FloatStyleBuilder floatStyleBuilder;
    private final PercentageStyleBuilder percentageStyleBuilder;
    private final TimeStyleBuilder timeStyleBuilder;

    /**
     * @param booleanStyleBuilder    the style for booleans
     * @param currencyStyleBuilder   the style for currencies
     * @param dateDataStyleBuilder   the style for dates
     * @param floatStyleBuilder      the style for numbers
     * @param percentageStyleBuilder the style for percentages
     * @param timeStyleBuilder       the style for times
     */
    public DataStylesBuilder(final BooleanStyleBuilder booleanStyleBuilder,
                             final CurrencyStyleBuilder currencyStyleBuilder,
                             final DateStyleBuilder dateDataStyleBuilder, final FloatStyleBuilder floatStyleBuilder,
                             final PercentageStyleBuilder percentageStyleBuilder,
                             final TimeStyleBuilder timeStyleBuilder) {
        this.booleanStyleBuilder = booleanStyleBuilder;
        this.currencyStyleBuilder = currencyStyleBuilder;
        this.dateDataStyleBuilder = dateDataStyleBuilder;
        this.floatStyleBuilder = floatStyleBuilder;
        this.percentageStyleBuilder = percentageStyleBuilder;
        this.timeStyleBuilder = timeStyleBuilder;
    }

    public BooleanStyleBuilder booleanStyleBuilder() {
        return this.booleanStyleBuilder;
    }

    public CurrencyStyleBuilder currencyStyleBuilder() {
        return this.currencyStyleBuilder;
    }

    public DateStyleBuilder dateStyleBuilder() {
        return this.dateDataStyleBuilder;
    }

    public FloatStyleBuilder floatStyleBuilder() {
        return this.floatStyleBuilder;
    }

    public PercentageStyleBuilder percentageStyleBuilder() {
        return this.percentageStyleBuilder;
    }

    public TimeStyleBuilder timeStyleBuilder() {
        return this.timeStyleBuilder;
    }

    public DataStyles build() {
        return new DataStyles(this.booleanStyleBuilder.buildHidden(), this.currencyStyleBuilder.buildHidden(),
                this.dateDataStyleBuilder.buildHidden(), this.floatStyleBuilder.buildHidden(),
                this.percentageStyleBuilder.buildHidden(), this.timeStyleBuilder.buildHidden());

    }
}
