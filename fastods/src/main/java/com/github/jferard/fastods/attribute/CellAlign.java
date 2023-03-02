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
 * An horizontal alignment.
 * 20.216 fo:text-align. See https://www.w3.org/TR/2001/REC-xsl-20011015/slice7.html#text-align
 */
public enum CellAlign implements AttributeValue {
    /**
     * The text is centered
     */
    CENTER("center"),

    /**
     * The text is justified
     */
    JUSTIFY("justify"),

    /**
     * The text is left aligned
     */
    LEFT("start"),

    /**
     * The text is right aligned
     */
    RIGHT("end");

    private final String attrValue;

    /**
     * A new horizontal align
     *
     * @param attrValue the align attribute
     */
    CellAlign(final String attrValue) {
        this.attrValue = attrValue;
    }

    @Override
    public String getValue() {
        return this.attrValue;
    }
}
