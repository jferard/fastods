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

import com.github.jferard.fastods.TagParameters;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * The margins of a page
 *
 * @author Julien Férard
 */
public class Margins implements TagParameters {
    private final Length all;
    private final Length bottom;
    private final Length left;
    private final Length right;
    private final Length top;

    /**
     * Create the margins
     *
     * @param all    the length of all margin
     * @param top    the length of top margin
     * @param right  the length of right margin
     * @param bottom the length of bottom margin
     * @param left   the length of left margin
     */
    Margins(final Length all, final Length top, final Length right, final Length bottom,
            final Length left) {
        this.all = all;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    /**
     * @return true if all margins are void
     */
    public boolean areVoid() {
        return this.all == null && this.top == null && this.right == null && this.bottom == null &&
                this.left == null;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        if (this.all == null) {
            if (this.top != null) {
                util.appendAttribute(appendable, "fo:margin-top", this.top.toString());
            }

            if (this.right != null) {
                util.appendAttribute(appendable, "fo:margin-right", this.right.toString());
            }

            if (this.bottom != null) {
                util.appendAttribute(appendable, "fo:margin-bottom", this.bottom.toString());
            }

            if (this.left != null) {
                util.appendAttribute(appendable, "fo:margin-left", this.left.toString());
            }
        } else { // this.all != null
            util.appendAttribute(appendable, "fo:margin", this.all.toString());
            if (this.top != null && !this.top.equals(this.all)) {
                util.appendAttribute(appendable, "fo:margin-top", this.top.toString());
            }

            if (this.right != null && !this.right.equals(this.all)) {
                util.appendAttribute(appendable, "fo:margin-right", this.right.toString());
            }

            if (this.bottom != null && !this.bottom.equals(this.all)) {
                util.appendAttribute(appendable, "fo:margin-bottom", this.bottom.toString());
            }

            if (this.left != null && !this.left.equals(this.all)) {
                util.appendAttribute(appendable, "fo:margin-left", this.left.toString());
            }
        }

    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Margins)) {
            return false;
        }

        final Margins other = (Margins) o;
        return EqualityUtil.equal(this.top, other.top) &&
                EqualityUtil.equal(this.right, other.right) &&
                EqualityUtil.equal(this.bottom, other.bottom) &&
                EqualityUtil.equal(this.left, other.left) &&
                EqualityUtil.equal(this.all, other.all);
    }

    @Override
    public String toString() {
        return "Margins[top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom +
                ", left=" + this.left + ", all=" + this.all + "]";

    }

    @Override
    public int hashCode() {
        return EqualityUtil.hashObjects(this.all, this.bottom, this.left, this.right, this.top);
    }
}
