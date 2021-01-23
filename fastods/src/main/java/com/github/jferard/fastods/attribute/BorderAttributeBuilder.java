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
 * @author Julien Férard
 */
public class BorderAttributeBuilder {
    /**
     * The border color
     */
    private Color borderColor;

    /**
     * The border size.
     */
    private Length borderSize;

    /**
     * The border style. Either BorderAttribute.BORDER_SOLID or
     * BorderAttribute.BORDER_DOUBLE.<br>
     * Default is BorderAttribute.BORDER_SOLID.
     */
    private BorderStyle style;

    /**
     * A new builder
     */
    public BorderAttributeBuilder() {
        this.style = BorderAttribute.DEFAULT_STYLE;
        this.borderColor = SimpleColor.NONE;
    }

    /**
     * Set the currently set border color.
     *
     * @param borderColor The color in format #rrggbb
     * @return this for fluent style
     */
    public BorderAttributeBuilder borderColor(final Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
     * Sets the current value of border size in pt.
     *
     * @param size The size as int, in pt
     * @return this for fluent style
     */
    public BorderAttributeBuilder borderSize(final int size) {
        this.borderSize = SimpleLength.pt(size);
        return this;
    }

    /**
     * Sets the current value of border size.
     *
     * @param borderSize The size as length
     * @return this for fluent style
     */
    public BorderAttributeBuilder borderSize(final Length borderSize) {
        this.borderSize = borderSize;
        return this;
    }

    /**
     * Sets the current border NamedObject.
     *
     * @param style BorderAttribute.BORDER_SOLID or BorderAttribute.BORDER_DOUBLE
     * @return this for fluent style
     */
    public BorderAttributeBuilder borderStyle(final BorderStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Builds a border style
     *
     * @return ths BorderAttribute
     */
    public BorderAttribute build() {
        return new BorderAttribute(this.borderSize, this.borderColor, this.style);
    }
}
