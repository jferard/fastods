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

import java.util.Locale;

/**
 * 16.27.25 <number:text-style>
 *
 * @author Julien Férard
 */
public class TextDataStyleBuilder implements DataStyleBuilder<TextDataStyle, TextDataStyleBuilder> {
    private final CoreDataStyleBuilder dataStyleBuilder;
    private String text;

    /**
     * Create a new text style with the name name.
     *
     * @param name   The name of the number style, this name must be unique.
     * @param locale the locale used
     */
    public TextDataStyleBuilder(final String name, final Locale locale) {
        this.dataStyleBuilder = new CoreDataStyleBuilder(name, locale);
        this.text = "<number:text-content/>";
    }

    @Override
    public TextDataStyle build() {
        return new TextDataStyle(this.dataStyleBuilder.build(), this.text);
    }


    /**
     * @param text the XML representation of the content
     * @return this for fluent style
     */
    public TextDataStyleBuilder text(final String text) {
        this.text = text;
        return this;
    }

    @Override
    public TextDataStyleBuilder country(final String countryCode) {
        this.dataStyleBuilder.country(countryCode);
        return this;
    }

    @Override
    public TextDataStyleBuilder language(final String languageCode) {
        this.dataStyleBuilder.language(languageCode);
        return this;
    }

    @Override
    public TextDataStyleBuilder locale(final Locale locale) {
        this.dataStyleBuilder.locale(locale);
        return this;
    }

    @Override
    public TextDataStyleBuilder volatileStyle(final boolean volatileStyle) {
        this.dataStyleBuilder.volatileStyle(volatileStyle);
        return this;
    }

    @Override
    public TextDataStyleBuilder visible() {
        this.dataStyleBuilder.visible();
        return this;
    }
}
