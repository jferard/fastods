/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
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
        final BooleanStyleBuilder booleanDataStyleBuilder =
                new BooleanStyleBuilder("boolean-data", locale);
        final CurrencyStyleBuilder currencyDataStyleBuilder =
                new CurrencyStyleBuilder("currency-data", locale);
        final DateStyleBuilder dateDataStyleBuilder = new DateStyleBuilder("date-data", locale);
        final FloatStyleBuilder floatDataStyleBuilder = null;
        final PercentageStyleBuilder percentageDataStyleBuilder =
                new PercentageStyleBuilder("percentage-data", locale);
        final TimeStyleBuilder timeDataStyleBuilder = new TimeStyleBuilder("time-data", locale);
        return new DataStylesBuilder(locale, booleanDataStyleBuilder, currencyDataStyleBuilder,
                dateDataStyleBuilder,
                floatDataStyleBuilder, percentageDataStyleBuilder, timeDataStyleBuilder);
    }

    private final Locale locale;
    private final BooleanStyleBuilder booleanStyleBuilder;
    private final CurrencyStyleBuilder currencyStyleBuilder;
    private final DateStyleBuilder dateDataStyleBuilder;
    private FloatStyleBuilder floatStyleBuilder;
    private final PercentageStyleBuilder percentageStyleBuilder;
    private final TimeStyleBuilder timeStyleBuilder;

    /**
     * @param locale                 the locale
     * @param booleanStyleBuilder    the style for booleans
     * @param currencyStyleBuilder   the style for currencies
     * @param dateDataStyleBuilder   the style for dates
     * @param floatStyleBuilder      the style for numbers
     * @param percentageStyleBuilder the style for percentages
     * @param timeStyleBuilder       the style for times
     */
    public DataStylesBuilder(final Locale locale, final BooleanStyleBuilder booleanStyleBuilder,
                             final CurrencyStyleBuilder currencyStyleBuilder,
                             final DateStyleBuilder dateDataStyleBuilder,
                             final FloatStyleBuilder floatStyleBuilder,
                             final PercentageStyleBuilder percentageStyleBuilder,
                             final TimeStyleBuilder timeStyleBuilder) {
        this.locale = locale;
        this.booleanStyleBuilder = booleanStyleBuilder;
        this.currencyStyleBuilder = currencyStyleBuilder;
        this.dateDataStyleBuilder = dateDataStyleBuilder;
        this.floatStyleBuilder = floatStyleBuilder;
        this.percentageStyleBuilder = percentageStyleBuilder;
        this.timeStyleBuilder = timeStyleBuilder;
    }

    /**
     * @return a builder for boolean style
     */
    public BooleanStyleBuilder booleanStyleBuilder() {
        return this.booleanStyleBuilder;
    }

    /**
     * @return a builder for currency style
     */
    public CurrencyStyleBuilder currencyStyleBuilder() {
        return this.currencyStyleBuilder;
    }

    /**
     * @return a builder for date style
     */
    public DateStyleBuilder dateStyleBuilder() {
        return this.dateDataStyleBuilder;
    }

    /**
     * Get the float style builder. Note that this has a side effect:
     * * if this method is not called, the {@code FloatDataStyle} attribute of the generated
     * {@code DataStyles} will be null ;
     * * otherwise, the {@code FloatDataStyle} attribute of the generated {@code DataStyles}
     * will be not null.
     *
     * See {@code DataStyles} for more information on null {@code DataStyle} attributes.
     *
     * @return a builder for float style
     */
    public FloatStyleBuilder floatStyleBuilder() {
        if (this.floatStyleBuilder == null) {
            this.floatStyleBuilder = new FloatStyleBuilder("float-data", this.locale);
        }
        return this.floatStyleBuilder;
    }

    /**
     * @return a builder for percentage style
     */
    public PercentageStyleBuilder percentageStyleBuilder() {
        return this.percentageStyleBuilder;
    }

    /**
     * @return a builder for time style
     */
    public TimeStyleBuilder timeStyleBuilder() {
        return this.timeStyleBuilder;
    }

    /**
     * @return the data styles
     */
    public DataStyles build() {
        final FloatStyle floatStyle;
        if (this.floatStyleBuilder == null) {
            floatStyle = null;
        } else {
            floatStyle = this.floatStyleBuilder.build();
        }
        return new DataStyles(this.booleanStyleBuilder.build(), this.currencyStyleBuilder.build(),
                this.dateDataStyleBuilder.build(), floatStyle,
                this.percentageStyleBuilder.build(), this.timeStyleBuilder.build());

    }
}
