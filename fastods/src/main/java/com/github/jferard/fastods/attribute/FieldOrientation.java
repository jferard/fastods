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
 * 19.686.2 table:data-pilot-field
 *
 * @author J. Férard
 */
public enum FieldOrientation implements AttributeValue {
    /**
     * field specifies a category column
     */
    COLUMN("column"),

    /**
     * field specifies a data column
     */
    DATA("data"),

    /**
     * field has a corresponding column in a data pilot's source but are not visible in the data
     * pilot table.
     */
    HIDDEN("hidden"),

    /**
     * specifies that an automatic filter (one that allows to choose one of the values that are
     * contained in the column) should be generated for the corresponding column. In that case,
     * an additional field with row, column or data orientation shall exist for the column. The
     * table:selected-page attribute specifies which value is selected for the filter.
     */
    PAGE("page"),

    /**
     * specifies a category row
     */
    ROW("row");

    private final String attr;

    /**
     * @param attr the attribute
     */
    FieldOrientation(final String attr) {
        this.attr = attr;
    }

    @Override
    public String toString() {
        return this.getValue().toString();
    }

    @Override
    public CharSequence getValue() {
        return this.attr;
    }
}
