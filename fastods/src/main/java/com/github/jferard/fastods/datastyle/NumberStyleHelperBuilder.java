/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.attribute.SimpleColor;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public final class NumberStyleHelperBuilder
        implements NumberStyleBuilder<NumberStyleHelper, NumberStyleHelperBuilder> {
    private final CoreDataStyleBuilder dataStyleBuilder;
    private boolean grouping;
    private int minIntegerDigits;
    private Color negativeValueColor;

    /**
     * Create a new number style with a name and a locale.
     *
     * @param name   The name of the number style, this name must be unique.
     * @param locale the locale to use
     */
    NumberStyleHelperBuilder(final String name, final Locale locale) {
        this.dataStyleBuilder = new CoreDataStyleBuilder(name, locale);
        this.minIntegerDigits = 1;
        this.grouping = false;
    }

    @Override
    public NumberStyleHelperBuilder groupThousands(final boolean grouping) {
        this.grouping = grouping;
        return this;
    }

    @Override
    public NumberStyleHelperBuilder minIntegerDigits(final int minIntegerDigits) {
        this.minIntegerDigits = minIntegerDigits;
        return this;
    }

    @Override
    public NumberStyleHelperBuilder negativeValueColor(final Color negativeValueColor) {
        this.negativeValueColor = negativeValueColor;
        return this;
    }

    @Override
    public NumberStyleHelperBuilder negativeValueRed() {
        this.negativeValueColor = SimpleColor.RED;
        return this;
    }

    @Override
    public NumberStyleHelperBuilder country(final String countryCode) {
        this.dataStyleBuilder.country(countryCode);
        return this;
    }

    @Override
    public NumberStyleHelperBuilder language(final String languageCode) {
        this.dataStyleBuilder.language(languageCode);
        return this;
    }

    @Override
    public NumberStyleHelperBuilder locale(final Locale locale) {
        this.dataStyleBuilder.locale(locale);
        return this;
    }

    @Override
    public NumberStyleHelperBuilder volatileStyle(final boolean volatileStyle) {
        this.dataStyleBuilder.volatileStyle(volatileStyle);
        return this;
    }

    @Override
    public NumberStyleHelperBuilder visible() {
        this.dataStyleBuilder.visible();
        return this;
    }

    @Override
    public NumberStyleHelper build() {
        return new NumberStyleHelper(this.dataStyleBuilder.build(), this.grouping,
                this.minIntegerDigits, this.negativeValueColor);
    }

}
