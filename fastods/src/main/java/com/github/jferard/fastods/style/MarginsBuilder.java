/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.attribute.Length;

/**
 * A builder for margins
 *
 * @author Julien Férard
 */
public class MarginsBuilder {
    private Length all;
    private Length bottom;
    private Length left;
    private Length right;
    private Length top;

    /**
     * Set the margin at the top, bottom, left and right.
     *
     * @param size the margin size
     * @return this for fluent style
     */
    public MarginsBuilder all(final Length size) {
        this.all = size;
        return this;
    }

    /**
     * Set the bottom margin. margin is a length value
     *
     * @param size the margin size
     * @return this for fluent style
     */
    public MarginsBuilder bottom(final Length size) {
        this.bottom = size;
        return this;
    }

    /**
     * @return the margins
     */
    public Margins build() {
        return new Margins(this.all, this.top, this.right, this.bottom,
                this.left);
    }

    /**
     * Set the left margin. Margin is a length value
     *
     * @param size the margin size
     * @return this for fluent style
     */
    public MarginsBuilder left(final Length size) {
        this.left = size;
        return this;
    }

    /**
     * Set the right margin. Margin is a length value
     *
     * @param size the margin size
     * @return this for fluent style
     */
    public MarginsBuilder right(final Length size) {
        this.right = size;
        return this;
    }

    /**
     * Set the top margin. Margin is a length value
     *
     * @param size the margin size
     * @return this for fluent style
     */
    public MarginsBuilder top(final Length size) {
        this.top = size;
        return this;
    }
}
