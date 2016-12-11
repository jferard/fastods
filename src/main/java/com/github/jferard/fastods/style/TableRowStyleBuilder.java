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

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableRowStyleBuilder {

	private final String name;
	private String rowHeight;

	/**
	 * @param name
	 *            A unique name for this style
	 */
	public TableRowStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();

		this.name = name;
		this.rowHeight = "0.45cm";
	}

	public TableRowStyle build() {
		return new TableRowStyle(this.name, this.rowHeight);

	}

	/**
	 * Set the row height to a table row.<br>
	 * height is a length value expressed as a number followed by a unit of
	 * measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @param height
	 *            The table row height to be used, e.g. '1.0cm'
	 * @return true - The height was set,<br>
	 *         false - his object is no table row, you can not set the height to
	 *         it
	 */
	public TableRowStyleBuilder rowHeight(final String height) {
		this.rowHeight = height;
		return this;
	}
}
