/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods.style;

import java.io.IOException;

import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.XMLUtil;

public class Borders {
	private final BorderAttribute top;
	private final BorderAttribute right;
	private final BorderAttribute bottom;
	private final BorderAttribute left;
	private BorderAttribute all;

	Borders(final BorderAttribute all, final BorderAttribute top,
			final BorderAttribute right, final BorderAttribute bottom,
			final BorderAttribute left) {
		this.all = all;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	public BorderAttribute getTop() {
		return this.top;
	}

	public BorderAttribute getAll() {
		return this.all;
	}

	public BorderAttribute getRight() {
		return this.right;
	}

	public BorderAttribute getBottom() {
		return this.bottom;
	}

	public BorderAttribute getLeft() {
		return this.left;
	}

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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Borders))
			return false;

		Borders other = (Borders) o;
		return (this.top == null ? other.top == null
				: this.top.equals(other.top))
				&& (this.right == null ? other.right == null
						: this.right.equals(other.right))
				&& (this.bottom == null ? other.bottom == null
						: this.bottom.equals(other.bottom))
				&& (this.left == null ? other.left == null
						: this.left.equals(other.left))
				&& (this.all == null ? other.all == null
						: this.all.equals(other.all));
	}

	@Override
	public String toString() {
		return "Borders[top=" + this.top + ", right=" + this.right + ", bottom="
				+ this.bottom + ", left=" + this.left + ", all=" + this.all
				+ "]";

	}
}
