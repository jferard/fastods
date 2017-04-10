/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * The class Borders represents the borders of an element.
 * @author Julien Férard
 */
public class Borders {
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

	/**
	 * @param util the util for writing XML
	 * @param appendable where to write
	 * @throws IOException If an I/O error occurs
	 */
	public void appendXMLToTableCellStyle(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.all == null) {
			if (this.top != null)
				util.appendAttribute(appendable, "fo:border-top",
						this.top.toXMLAttributeValue());

			if (this.right != null)
				util.appendAttribute(appendable, "fo:border-right",
						this.right.toXMLAttributeValue());

			if (this.bottom != null)
				util.appendAttribute(appendable, "fo:border-bottom",
						this.bottom.toXMLAttributeValue());

			if (this.left != null)
				util.appendAttribute(appendable, "fo:border-left",
						this.left.toXMLAttributeValue());
		} else { // this.all != null
			util.appendAttribute(appendable, "fo:border",
					this.all.toXMLAttributeValue());
			if (this.top != null && !this.top.equals(this.all))
				util.appendAttribute(appendable, "fo:border-top",
						this.top.toXMLAttributeValue());

			if (this.right != null && !this.right.equals(this.all))
				util.appendAttribute(appendable, "fo:border-right",
						this.right.toXMLAttributeValue());

			if (this.bottom != null && !this.bottom.equals(this.all))
				util.appendAttribute(appendable, "fo:border-bottom",
						this.bottom.toXMLAttributeValue());

			if (this.left != null && !this.left.equals(this.all))
				util.appendAttribute(appendable, "fo:border-left",
						this.left.toXMLAttributeValue());
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

	/**
	 * @return the border attribute for all borders, null if none
	 */
	public BorderAttribute getAll() {
		return this.all;
	}

	/**
	 * @return the border attribute for the bottom border, null if none
	 */
	public BorderAttribute getBottom() {
		return this.bottom;
	}

	/**
	 * @return the border attribute for the left border, null if none
	 */
	public BorderAttribute getLeft() {
		return this.left;
	}

	/**
	 * @return the border attribute for the right border, null if none
	 */
	public BorderAttribute getRight() {
		return this.right;
	}

	/**
	 * @return the border attribute for the top border, null if none
	 */
	public BorderAttribute getTop() {
		return this.top;
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
