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

import java.util.Locale;

/**
 * @author Julien Férard
 */
public class DateStyleBuilder implements DataStyleBuilder<DateStyle, DateStyleBuilder> {
    private final CoreDataStyleBuilder dataStyleBuilder;
    private boolean automaticOrder;
    private DateTimeStyleFormat dateFormat;

    /**
     * Create a new date style with the name name.<br>
     * Version 0.5.1 Added.
     *
     * @param name   The name of the number style.
     * @param locale The locale used
     */
    public DateStyleBuilder(final String name, final Locale locale) {
        this.dataStyleBuilder = new CoreDataStyleBuilder(name, locale);
        this.automaticOrder = false;
    }

    /**
     * The automatic-order attribute can be used to automatically order data to
     * match the default order<br>
     * for the language and country of the date style.
     *
     * @param automatic specifies whether data is ordered to match the default
     *                  order for the language and country of a data style (19.340
     *                  number:automatic-order).
     * @return this for fluent style
     */
    public DateStyleBuilder automaticOrder(final boolean automatic) {
        this.automaticOrder = automatic;
        return this;
    }

    @Override
    public DateStyle build() {
        return new DateStyle(this.dataStyleBuilder.build(), this.dateFormat, this.automaticOrder);
    }

    /**
     * Set the date format.<br>
     * Valid is one of the following:<br>
     * DateStyle.DATEFORMAT_DDMMYYYY<br>
     * DateStyle.DATEFORMAT_DDMMYY<br>
     * DateStyle.DATEFORMAT_TMMMMYYYY<br>
     * DateStyle.DATEFORMAT_MMMM<br>
     * *
     *
     * @param format The date format to be used.
     * @return this for fluent style
     */
    public DateStyleBuilder dateFormat(final DateTimeStyleFormat format) {
        this.dateFormat = format;
        return this;
    }

    @Override
    public DateStyleBuilder country(final String countryCode) {
        this.dataStyleBuilder.country(countryCode);
        return this;
    }

    @Override
    public DateStyleBuilder language(final String languageCode) {
        this.dataStyleBuilder.language(languageCode);
        return this;
    }

    @Override
    public DateStyleBuilder locale(final Locale locale) {
        this.dataStyleBuilder.locale(locale);
        return this;
    }

    @Override
    public DateStyleBuilder volatileStyle(final boolean volatileStyle) {
        this.dataStyleBuilder.volatileStyle(volatileStyle);
        return this;
    }

    @Override
    public DateStyleBuilder visible() {
        this.dataStyleBuilder.visible();
        return this;
    }
}
