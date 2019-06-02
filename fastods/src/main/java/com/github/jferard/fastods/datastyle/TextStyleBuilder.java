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

import java.util.Locale;

/**
 * 16.27.25 <number:text-style>
 * @author Julien Férard
 */
public class TextStyleBuilder implements DataStyleBuilder<TextStyle, TextStyleBuilder> {
    private final CoreDataStyleBuilder dataStyleBuilder;
    private String text;

    /**
     * Create a new text style with the name name.
     *
     * @param name   The name of the number style, this name must be unique.
     * @param locale the locale used
     */
    public TextStyleBuilder(final String name, final Locale locale) {
        this.dataStyleBuilder = new CoreDataStyleBuilder(name, locale);
        this.text = "<number:text-content/>";
    }

    @Override
    public TextStyle build() {
        return new TextStyle(this.dataStyleBuilder.build(), this.text);
    }

    public TextStyleBuilder text(final String text) {
        this.text = text;
        return this;
    }

    @Override
    public TextStyleBuilder country(final String countryCode) {
        this.dataStyleBuilder.country(countryCode);
        return this;
    }

    @Override
    public TextStyleBuilder language(final String languageCode) {
        this.dataStyleBuilder.language(languageCode);
        return this;
    }

    @Override
    public TextStyleBuilder locale(final Locale locale) {
        this.dataStyleBuilder.locale(locale);
        return this;
    }

    @Override
    public TextStyleBuilder volatileStyle(final boolean volatileStyle) {
        this.dataStyleBuilder.volatileStyle(volatileStyle);
        return this;
    }

    @Override
    public TextStyleBuilder visible() {
        this.dataStyleBuilder.visible();
        return this;
    }
}
