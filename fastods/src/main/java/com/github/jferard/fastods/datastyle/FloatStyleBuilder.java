/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.attribute.Color;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public class FloatStyleBuilder implements DataStyleBuilder<FloatStyle, FloatStyleBuilder>,
        NumberStyleBuilder<FloatStyle, FloatStyleBuilder>, DecimalStyleBuilder<FloatStyleBuilder> {
    private final NumberStyleHelperBuilder numberStyleHelperBuilder;
    /**
     * the number of digits after the separator
     */
    private int decimalPlaces;

    /**
     * The builder
     *
     * @param name   The name of this style
     * @param locale The locale used
     */
    public FloatStyleBuilder(final String name, final Locale locale) {
        this.numberStyleHelperBuilder = new NumberStyleHelperBuilder(name, locale);
        this.decimalPlaces = 2;
    }

    @Override
    public FloatStyle build() {
        return new FloatStyle(this.numberStyleHelperBuilder.build(), this.decimalPlaces);
    }

    @Override
    public FloatStyleBuilder visible() {
        this.numberStyleHelperBuilder.visible();
        return this;
    }

    @Override
    public FloatStyleBuilder decimalPlaces(final int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        return this;
    }

    @Override
    public FloatStyleBuilder groupThousands(final boolean grouping) {
        this.numberStyleHelperBuilder.groupThousands(grouping);
        return this;
    }

    @Override
    public FloatStyleBuilder minIntegerDigits(final int minIntegerDigits) {
        this.numberStyleHelperBuilder.minIntegerDigits(minIntegerDigits);
        return this;
    }

    @Override
    public FloatStyleBuilder negativeValueColor(final Color negativeValueColor) {
        this.numberStyleHelperBuilder.negativeValueColor(negativeValueColor);
        return this;
    }

    @Override
    public FloatStyleBuilder negativeValueRed() {
        this.numberStyleHelperBuilder.negativeValueRed();
        return this;
    }

    @Override
    public FloatStyleBuilder country(final String countryCode) {
        this.numberStyleHelperBuilder.country(countryCode);
        return this;
    }

    @Override
    public FloatStyleBuilder language(final String languageCode) {
        this.numberStyleHelperBuilder.language(languageCode);
        return this;
    }

    @Override
    public FloatStyleBuilder locale(final Locale locale) {
        this.numberStyleHelperBuilder.locale(locale);
        return this;
    }

    @Override
    public FloatStyleBuilder volatileStyle(final boolean volatileStyle) {
        this.numberStyleHelperBuilder.volatileStyle(volatileStyle);
        return this;
    }
}
