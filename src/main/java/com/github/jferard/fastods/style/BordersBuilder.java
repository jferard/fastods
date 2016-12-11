/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.util.EqualityUtil;

public class BordersBuilder {
	private BorderAttribute all;
	private BorderAttribute bottom;
	private BorderAttribute left;
	private BorderAttribute right;
	private BorderAttribute top;

	public BordersBuilder all(final BorderAttribute attribute) {
		this.all = attribute;
		return this;
	}

	public BordersBuilder all(final String size, final String color,
			final BorderAttribute.Style style) {
		return this.all(new BorderAttribute(size, color, style));
	}

	public BordersBuilder bottom(final BorderAttribute attribute) {
		this.bottom = attribute;
		return this;
	}

	public BordersBuilder bottom(final String size, final String color,
			final BorderAttribute.Style style) {
		return this.bottom(new BorderAttribute(size, color, style));
	}

	public Borders build() {
		return new Borders(new EqualityUtil(), this.all, this.top, this.right, this.bottom,
				this.left);
	}

	public BordersBuilder left(final BorderAttribute attribute) {
		this.left = attribute;
		return this;
	}

	public BordersBuilder left(final String size, final String color,
			final BorderAttribute.Style style) {
		return this.left(new BorderAttribute(size, color, style));
	}

	public BordersBuilder right(final BorderAttribute attribute) {
		this.right = attribute;
		return this;
	}

	public BordersBuilder right(final String size, final String color,
			final BorderAttribute.Style style) {
		return this.right(new BorderAttribute(size, color, style));
	}

	public BordersBuilder top(final BorderAttribute attribute) {
		this.top = attribute;
		return this;
	}

	public BordersBuilder top(final String size, final String color,
			final BorderAttribute.Style style) {
		return this.top(new BorderAttribute(size, color, style));
	}
}
