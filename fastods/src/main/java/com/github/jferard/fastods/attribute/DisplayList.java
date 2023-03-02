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
 * 19.621 table:display-list
 */
public enum DisplayList implements AttributeValue {
    /**
     * none: "the list values are not displayed"
     */
    NONE("none"),
    /**
     * sort-ascending: "the list values are displayed in ascending order."
     */
    SORT_ASCENDING("sort-ascending"),
    /**
     * unsorted: "the list values are displayed in the order they occur in the condition."
     */
    UNSORTED("unsorted");

    private final String attrValue;

    DisplayList(final String attrValue) {
        this.attrValue = attrValue;
    }

    /**
     * @return the value of the attribute for XML use
     */
    @Override
    public String getValue() {
        return this.attrValue;
    }
}
