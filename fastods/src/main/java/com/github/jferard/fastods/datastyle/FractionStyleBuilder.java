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

import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.util.NameChecker;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public class FractionStyleBuilder implements DataStyleBuilder<FractionStyle, FractionStyleBuilder>,
        NumberStyleBuilder<FractionStyle, FractionStyleBuilder> {
    private static final NameChecker checker = new NameChecker();

    /**
     * Create a new number style builder with the name name, minimum integer digits is
     * minIntDigits and decimal places is decPlaces.
     *
     * @param name   The name of the number style, this name must be unique.
     * @param locale The locale used
     * @return the builder
     */
    public static FractionStyleBuilder create(final String name, final Locale locale) {
        checker.checkStyleName(name);
        return new FractionStyleBuilder(name, locale);
    }

    private final NumberStyleHelperBuilder numberStyleHelperBuilder;
    private int minDenominatorDigits;
    private int minNumeratorDigits;

    /**
     * @param name   The name of the number style, this name must be unique.
     * @param locale The locale used
     */
    public FractionStyleBuilder(final String name, final Locale locale) {
        this.numberStyleHelperBuilder = new NumberStyleHelperBuilder(name, locale);
        this.minNumeratorDigits = 0;
        this.minDenominatorDigits = 0;
    }

    @Override
    public FractionStyle build() {
        return new FractionStyle(this.numberStyleHelperBuilder.build(), this.minNumeratorDigits,
                this.minDenominatorDigits);
    }

    /**
     * Add the numerator and denominator values to be shown.<br>
     * The number style is set to NUMBER_FRACTION
     *
     * @param numerator   the number of digits for the numerator
     * @param denominator the number of digits for the denominator
     * @return this for fluent style
     */
    public FractionStyleBuilder fractionValues(final int numerator, final int denominator) {
        this.minNumeratorDigits = numerator;
        this.minDenominatorDigits = denominator;
        return this;
    }

    @Override
    public FractionStyleBuilder groupThousands(final boolean grouping) {
        this.numberStyleHelperBuilder.groupThousands(grouping);
        return this;
    }

    @Override
    public FractionStyleBuilder minIntegerDigits(final int minIntegerDigits) {
        this.numberStyleHelperBuilder.minIntegerDigits(minIntegerDigits);
        return this;
    }

    @Override
    public FractionStyleBuilder negativeValueColor(final Color negativeValueColor) {
        this.numberStyleHelperBuilder.negativeValueColor(negativeValueColor);
        return this;
    }

    @Override
    public FractionStyleBuilder negativeValueRed() {
        this.numberStyleHelperBuilder.negativeValueRed();
        return this;
    }

    @Override
    public FractionStyleBuilder country(final String countryCode) {
        this.numberStyleHelperBuilder.country(countryCode);
        return this;
    }

    @Override
    public FractionStyleBuilder language(final String languageCode) {
        this.numberStyleHelperBuilder.language(languageCode);
        return this;
    }

    @Override
    public FractionStyleBuilder locale(final Locale locale) {
        this.numberStyleHelperBuilder.locale(locale);
        return this;
    }

    @Override
    public FractionStyleBuilder volatileStyle(final boolean volatileStyle) {
        this.numberStyleHelperBuilder.volatileStyle(volatileStyle);
        return this;
    }

    @Override
    public FractionStyleBuilder visible() {
        this.numberStyleHelperBuilder.visible();
        return this;
    }
}
