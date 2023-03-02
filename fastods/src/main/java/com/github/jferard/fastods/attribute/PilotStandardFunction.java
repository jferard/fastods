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

package com.github.jferard.fastods.attribute;


/**
 * 19.643table:function
 *
 * @author J. Férard
 */
public enum PilotStandardFunction implements PilotFunction {
    /**
     * the average of all numeric values.
     */
    AVERAGE("average"),

    /**
     * the count of all non-empty values, including text.
     */
    COUNT("count"),

    /**
     * the count of all numeric values.
     */
    COUNT_NUMS("countnums"),

    /**
     * the maximum of all numeric values.
     */
    MAX("max"),

    /**
     * the minimum of all numeric values.
     */
    MIN("min"),

    /**
     * the product of all numeric values.
     */
    PRODUCT("product"),

    /**
     * the standard deviation, treating all numeric values as a sample from a population.
     */
    ST_DEV("stdev"),

    /**
     * the standard deviation, treating all numeric values as a whole population.
     */
    ST_DEVP("stdevp"),

    /**
     * the sum of all numeric values.
     */
    SUM("sum"),

    /**
     * the variance, treating all numeric values as a sample from a population.
     */
    VAR("var"),

    /**
     * the variance, treating all numeric values as a whole population.
     */
    VARP("varp"),

    /**
     * no function is applied to that category field.
     */
    AUTO("auto");

    private final String attr;

    /**
     * @param attr the attribute
     */
    PilotStandardFunction(final String attr) {
        this.attr = attr;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    @Override
    public String getValue() {
        return this.attr;
    }
}
