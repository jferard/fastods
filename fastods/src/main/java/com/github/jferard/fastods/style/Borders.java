/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * The class Borders represents the borders of an element.
 * @author Julien Férard
 */
public class Borders implements TagParameters {
	private final BorderAttribute all;
	private final BorderAttribute bottom;
	private final BorderAttribute left;
	private final BorderAttribute right;
	private final BorderAttribute top;
	private final EqualityUtil equalityUtil;

	/**
	 * @param equalityUtil the util class to compare borders
	 * @param all all borders
	 * @param top the top border
	 * @param right the right border
	 * @param bottom the bottom border
	 * @param left the left border
	 */
	Borders(final EqualityUtil equalityUtil, final BorderAttribute all,
			final BorderAttribute top, final BorderAttribute right,
			final BorderAttribute bottom, final BorderAttribute left) {
		this.equalityUtil = equalityUtil;
		this.all = all;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	/**
	 * @return true if all borders are null
	 */
	public boolean areVoid() {
		return this.all == null && this.top == null && this.right == null && this.bottom == null && this.left == null;
	}

	@Override
	public void appendXMLContent(final XMLUtil util,
								 final Appendable appendable) throws IOException {
		if (this.all == null) {
			if (this.top != null)
				this.top.appendXMLAttribute(util, appendable, "fo:border-top");

			if (this.right != null)
                this.right.appendXMLAttribute(util, appendable, "fo:border-right");

			if (this.bottom != null)
				this.bottom.appendXMLAttribute(util, appendable, "fo:border-bottom");

			if (this.left != null)
				this.left.appendXMLAttribute(util, appendable, "fo:border-left");
		} else { // this.all != null
            this.all.appendXMLAttribute(util, appendable, "fo:border");
            if (this.top != null && !this.top.equals(this.all)) this.top.appendXMLAttribute(util, appendable, "fo:border-top");

            if (this.right != null && !this.right.equals(this.all))
                this.right.appendXMLAttribute(util, appendable, "fo:border-right");

            if (this.bottom != null && !this.bottom.equals(this.all))
                this.bottom.appendXMLAttribute(util, appendable, "fo:border-bottom");

            if (this.left != null && !this.left.equals(this.all))
                this.left.appendXMLAttribute(util, appendable, "fo:border-left");
        }
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Borders))
			return false;

		final Borders other = (Borders) o;
		return this.equalityUtil.equal(this.top, other.top)
				&& this.equalityUtil.equal(this.right, other.right)
				&& this.equalityUtil.equal(this.bottom, other.bottom)
				&& this.equalityUtil.equal(this.left, other.left)
				&& this.equalityUtil.equal(this.all, other.all);
	}

	@Override
	public String toString() {
		return "Borders[top=" + this.top + ", right=" + this.right + ", bottom="
				+ this.bottom + ", left=" + this.left + ", all=" + this.all
				+ "]";

	}

	@Override
	public int hashCode() {
		return this.equalityUtil.hashObjects(this.all, this.bottom, this.left, this.right, this.top);
	}
}
