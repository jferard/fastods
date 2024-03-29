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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.attribute.BorderAttribute;
import com.github.jferard.fastods.attribute.BorderStyle;
import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.attribute.Length;

/**
 * The builder class for borders.
 *
 * @author Julien Férard
 */
public class BordersBuilder {
    private BorderAttribute all;
    private BorderAttribute bottom;
    private BorderAttribute left;
    private BorderAttribute right;
    private BorderAttribute top;


    /**
     * Reserved to Borders.toBuilder()
     * @param all    all borders
     * @param top    the top border
     * @param right  the right border
     * @param bottom the bottom border
     * @param left   the left border
     */
    public BordersBuilder(final BorderAttribute all, final BorderAttribute top, final BorderAttribute right, final BorderAttribute bottom, final BorderAttribute left) {
        this.all = all;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    /**
     * Main constructor
     */
    public BordersBuilder() {
    }

    /**
     * Set all borders
     *
     * @param attribute the attribute value of the borders
     * @return this for fluent style
     */
    public BordersBuilder all(final BorderAttribute attribute) {
        this.all = attribute;
        return this;
    }

    /**
     * Set all borders
     *
     * @param size  the size of the borders as a length
     * @param color the color fo the borders
     * @param style the style of the borders
     * @return this for fluent style
     */
    public BordersBuilder all(final Length size, final Color color, final BorderStyle style) {
        return this.all(new BorderAttribute(size, color, style));
    }

    /**
     * Set the bottom border
     *
     * @param attribute the attribute value of the border
     * @return this for fluent style
     */
    public BordersBuilder bottom(final BorderAttribute attribute) {
        this.bottom = attribute;
        return this;
    }

    /**
     * Set the bottom border
     *
     * @param size  the size of the border as a length
     * @param color the color fo the border
     * @param style the style of the border
     * @return this for fluent style
     */
    public BordersBuilder bottom(final Length size, final Color color, final BorderStyle style) {
        return this.bottom(new BorderAttribute(size, color, style));
    }

    /**
     * Build the borders
     *
     * @return the borders
     */
    public Borders build() {
        return new Borders(this.all, this.top, this.right, this.bottom, this.left);
    }

    /**
     * Set the left border
     *
     * @param attribute the attribute value of the border
     * @return this for fluent style
     */
    public BordersBuilder left(final BorderAttribute attribute) {
        this.left = attribute;
        return this;
    }

    /**
     * Set the left border
     *
     * @param size  the size of the border as a length
     * @param color the color fo the border
     * @param style the style of the border
     * @return this for fluent style
     */
    public BordersBuilder left(final Length size, final Color color, final BorderStyle style) {
        return this.left(new BorderAttribute(size, color, style));
    }

    /**
     * Set the right border
     *
     * @param attribute the attribute value of the border
     * @return this for fluent style
     */
    public BordersBuilder right(final BorderAttribute attribute) {
        this.right = attribute;
        return this;
    }

    /**
     * Set the right border
     *
     * @param size  the size of the border as a length
     * @param color the color fo the border
     * @param style the style of the border
     * @return this for fluent style
     */
    public BordersBuilder right(final Length size, final Color color, final BorderStyle style) {
        return this.right(new BorderAttribute(size, color, style));
    }

    /**
     * Set the top border
     *
     * @param attribute the attribute value of the border
     * @return this for fluent style
     */
    public BordersBuilder top(final BorderAttribute attribute) {
        this.top = attribute;
        return this;
    }

    /**
     * Set the top border
     *
     * @param size  the size of the border as a length
     * @param color the color fo the border
     * @param style the style of the border
     * @return this for fluent style
     */
    public BordersBuilder top(final Length size, final Color color, final BorderStyle style) {
        return this.top(new BorderAttribute(size, color, style));
    }
}
