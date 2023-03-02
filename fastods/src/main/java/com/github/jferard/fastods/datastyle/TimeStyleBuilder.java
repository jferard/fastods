/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.attribute.FormatSource;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public class TimeStyleBuilder implements DataStyleBuilder<TimeStyle, TimeStyleBuilder> {
    private final CoreDataStyleBuilder dataStyleBuilder;
    /**
     * The date format.
     */
    private DateTimeStyleFormat timeFormat;
    private FormatSource formatSource;

    /**
     * Create a new date style with the name name.
     *
     * @param name   The name of the number style.
     * @param locale the locale used
     */
    public TimeStyleBuilder(final String name, final Locale locale) {
        this.dataStyleBuilder = new CoreDataStyleBuilder(name, locale);
        this.formatSource = FormatSource.FIXED;
        this.timeFormat = TimeStyle.Format.HHMMSS;
    }

    @Override
    public TimeStyle build() {
        return new TimeStyle(this.dataStyleBuilder.build(), this.formatSource, this.timeFormat);
    }

    /**
     * @param formatSource the new number:format-source
     * @return this for fluent style
     */
    public TimeStyleBuilder formatSource(final FormatSource formatSource) {
        this.formatSource = formatSource;
        return this;
    }

    /**
     * Set the time format
     *
     * @param format The date format to be used.
     * @return this for fluent style
     */
    public TimeStyleBuilder timeFormat(final DateTimeStyleFormat format) {
        this.timeFormat = format;
        return this;
    }

    @Override
    public TimeStyleBuilder country(final String countryCode) {
        this.dataStyleBuilder.country(countryCode);
        return this;
    }

    @Override
    public TimeStyleBuilder language(final String languageCode) {
        this.dataStyleBuilder.language(languageCode);
        return this;
    }

    @Override
    public TimeStyleBuilder locale(final Locale locale) {
        this.dataStyleBuilder.locale(locale);
        return this;
    }

    @Override
    public TimeStyleBuilder volatileStyle(final boolean volatileStyle) {
        this.dataStyleBuilder.volatileStyle(volatileStyle);
        return this;
    }

    @Override
    public TimeStyleBuilder visible() {
        this.dataStyleBuilder.visible();
        return this;
    }
}
