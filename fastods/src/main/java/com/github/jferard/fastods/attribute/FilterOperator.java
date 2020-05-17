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

package com.github.jferard.fastods.attribute;

/**
 * 19.684table:operator
 */
public enum FilterOperator implements AttributeValue {
    /**
     * matches
     */
    MATCHES("match"),

    /**
     * does not match
     */
    N_MATCH("!match"),

    /**
     * Equal to
     */
    EQ("="),

    /**
     * Not equal to
     */
    N_EQ("!="),

    /**
     * Less than
     */
    LT("<"),

    /**
     * Greater than
     */
    GT(">"),

    /**
     * Less than or equal to
     */
    LTE("<="),

    /**
     * Greater than or equal to
     */
    GTE(">="),

    /**
     * begins with
     */
    BEGINS("begins"),

    /**
     * contains
     */
    CONTAINS("contains"),

    /**
     * does not contain
     */
    N_CONTAINS("!contains"),

    /**
     * ends with
     */
    ENDS("ends"),

    /**
     * does not begin with
     */
    N_BEGINS("!begins"),

    /**
     * does not end with
     */
    N_ENDS("!ends"),

    /**
     * like bottom values, except that the office:value attribute specifies the number of
     * cells for which the condition is true as a percentage
     */
    BOTTOM_PERCENT("bottom percent"),

    /**
     * true for the n cells that have the smallest value, where n is the value of the
     * office:value attribute
     */
    BOTTOM_VALUES("bottom values"),

    /**
     * true for empty cells
     */
    EMPTY("empty"),

    /**
     * true for non-empty cells
     */
    N_EMPTY("!empty"),

    /**
     * like bottom percent, but for the largest values
     */
    TOP_PERCENT("top percent"),

    /**
     * like bottom values, but for the largest values
     */
    TOP_VALUES("top values");

    private final String operatorValue;

    FilterOperator(final String operatorValue) {
        this.operatorValue = operatorValue;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    @Override
    public String getValue() {
        return this.operatorValue;
    }
}
