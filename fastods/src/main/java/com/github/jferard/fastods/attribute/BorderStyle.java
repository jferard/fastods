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
 * Extensible Stylesheet Language (XSL)
 * Version 1.0, 7.7.20 "border-top-style"
 * <p>
 * The style of the border
 */
    public enum BorderStyle implements AttributeValue {
    /**
     * No border
     */
    NONE("none"),

    /**
     * Same as 'none', with a little exception
     */
    HIDDEN("hidden"),

    /**
     * Series of dots
     */
    DOTTED("dotted"),

    /**
     * Series of dashes
     */
    DASHED("dashed"),

    /**
     * Solid border
     */
    SOLID("solid"),

    /**
     * Double lined border
     */
    DOUBLE("double"),

    /**
     * Carved in the canvas
     */
    GROOVE("groove"),

    /**
     * Coming out of the canvas
     */
    RIDGE("ridge"),

    /**
     * Box carved in the canvas
     */
    INSET("inset"),

    /**
     * Box coming out of the canvas
     */
    OUTSET("outset");

    private final String attrValue;

    BorderStyle(final String attrValue) {
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
