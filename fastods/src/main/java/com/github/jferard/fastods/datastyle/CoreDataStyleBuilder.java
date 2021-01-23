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

import com.github.jferard.fastods.style.ShowableBuilder;
import com.github.jferard.fastods.util.NameChecker;
import com.github.jferard.fastods.util.StyleBuilder;

import java.util.Locale;


/**
 * A CoreDataStyle builder
 *
 * @author Julien Férard
 */
final class CoreDataStyleBuilder
        implements StyleBuilder<CoreDataStyle>, LocalizedBuilder<CoreDataStyleBuilder>,
        IsVolatileBuilder<CoreDataStyleBuilder>, ShowableBuilder<CoreDataStyleBuilder> {
    private static final NameChecker checker = new NameChecker();
    /**
     * the name of a data style (19.498.2)
     */
    private final String name;
    /**
     * 19.342 number:country : "The number:country attribute specifies a country code for a data
     * style"
     */
    private String countryCode;
    /**
     * 19.349 number:language : "The number:language attribute specifies a language code"
     */
    private String languageCode;
    /**
     * 19.517 : "The style:volatile attribute specifies whether unused style in
     * a document are retained or discarded by consumers." and "false: consumers should discard
     * the unused styles,
     * true: consumers should keep unused styles."
     */
    private boolean volatileStyle;
    private boolean hidden;

    /**
     * The builder. The style is hidden by default.
     *
     * @param name   The name of this style
     * @param locale The locale used
     */
    CoreDataStyleBuilder(final String name, final Locale locale) {
        this.name = checker.checkStyleName(name);
        this.countryCode = locale.getCountry();
        this.languageCode = locale.getLanguage();
        this.volatileStyle = true;
        this.hidden = true;
    }

    @Override
    public CoreDataStyle build() {
        return new CoreDataStyle(this.name, this.hidden, this.languageCode, this.countryCode,
                this.volatileStyle);

    }

    @Override
    public CoreDataStyleBuilder country(final String countryCode) {
        this.countryCode = countryCode.toUpperCase();
        return this;
    }

    @Override
    public CoreDataStyleBuilder language(final String languageCode) {
        this.languageCode = languageCode.toLowerCase();
        return this;
    }

    @Override
    public CoreDataStyleBuilder locale(final Locale locale) {
        this.countryCode = locale.getCountry();
        this.languageCode = locale.getLanguage();
        return this;
    }

    @Override
    public CoreDataStyleBuilder volatileStyle(final boolean volatileStyle) {
        this.volatileStyle = volatileStyle;
        return this;
    }

    @Override
    public CoreDataStyleBuilder visible() {
        this.hidden = false;
        return this;
    }
}
