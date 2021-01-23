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

package com.github.jferard.fastods.attribute;

/**
 * 19.385 office:value-type.
 * Javadoc text below is taken from Open Document Format for Office Applications
 * (OpenDocument) Version 1.2
 */
public enum CellType implements AttributeValue {
    /**
     * a boolean: "true or false"
     */
    BOOLEAN("boolean", "office:boolean-value"),
    /**
     * a currency: "Numeric value and currency symbol"
     */
    CURRENCY("currency", "office:value"),
    /**
     * a date: "Date value as specified in §3.2.9 of [xmlschema-2], or date and time value as
     * specified in §3.2.7 of [xmlschema-2]"
     */
    DATE("date", "office:date-value"),
    /**
     * a float: "Numeric value"
     */
    FLOAT("float", "office:value"),
    /**
     * a percentage: "Numeric value"
     */
    PERCENTAGE("percentage", "office:value"),
    /**
     * a string: "String"
     */
    STRING("string", "office:string-value"),
    /**
     * a time: "Duration, as specified in §3.2.6 of [xmlschema-2]"
     */
    TIME("time", "office:time-value"),
    /**
     * a void cell value: nothing.
     */
    VOID("", "office-value");

    private final String valueType;
    private final String valueAttribute;

    /**
     * @param valueType      the value type. Will produce office:value-type="float"
     * @param valueAttribute the value attribute. Will store the value as in:
     *                       office:string-value="xyz".
     */
    CellType(final String valueType, final String valueAttribute) {
        this.valueAttribute = valueAttribute;
        this.valueType = valueType;
    }

    /**
     * @return the value attribute ("office:xxx-value")
     */
    public String getValueAttribute() {
        return this.valueAttribute;
    }

    /**
     * @return the value type attribute ("float", ...)
     */
    @Override
    public String getValue() {
        return this.valueType;
    }
}
