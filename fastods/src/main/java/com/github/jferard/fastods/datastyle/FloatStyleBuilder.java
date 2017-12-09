/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
 * @author Julien Férard
 */
public class FloatStyleBuilder implements DataStyleBuilder<FloatStyle, FloatStyleBuilder> {
    private final NumberStyleBuilder numberStyleBuilder;
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
    FloatStyleBuilder(final String name, final Locale locale) {
        this.numberStyleBuilder = new NumberStyleBuilder(name, locale);
        this.decimalPlaces = 2;
    }

    @Override
    public FloatStyle build() {
        return new FloatStyle(this.numberStyleBuilder.build(), this.decimalPlaces);
    }

    @Override
    public FloatStyle buildHidden() {
        return new FloatStyle(this.numberStyleBuilder.buildHidden(), this.decimalPlaces);
    }

    /**
     * Set how many digits are to the right of the decimal symbol.
     *
     * @param decimalPlaces The number of digits
     * @return this for fluent style
     */
    public FloatStyleBuilder decimalPlaces(final int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        return this;
    }

    public FloatStyleBuilder groupThousands(final boolean grouping) {
        this.numberStyleBuilder.groupThousands(grouping);
        return this;
    }

    public FloatStyleBuilder minIntegerDigits(final int minIntegerDigits) {
        this.numberStyleBuilder.minIntegerDigits(minIntegerDigits);
        return this;
    }

    public FloatStyleBuilder negativeValueColor(final String negativeValueColor) {
        this.numberStyleBuilder.negativeValueColor(negativeValueColor);
        return this;
    }

    public FloatStyleBuilder negativeValueRed() {
        this.numberStyleBuilder.negativeValueRed();
        return this;
    }

    @Override
    public FloatStyleBuilder country(final String countryCode) {
        this.numberStyleBuilder.country(countryCode);
        return this;
    }

    @Override
    public FloatStyleBuilder language(final String languageCode) {
        this.numberStyleBuilder.language(languageCode);
        return this;
    }

    @Override
    public FloatStyleBuilder locale(final Locale locale) {
        this.numberStyleBuilder.locale(locale);
        return this;
    }

    @Override
    public FloatStyleBuilder volatileStyle(final boolean volatileStyle) {
        this.numberStyleBuilder.volatileStyle(volatileStyle);
        return this;
    }
}
