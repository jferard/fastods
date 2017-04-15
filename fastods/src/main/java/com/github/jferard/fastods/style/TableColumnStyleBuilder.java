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

import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.SimpleLength;

/**
 * @author Julien Férard
 */
public class TableColumnStyleBuilder {
	private Length columnWidth;
	private TableCellStyle defaultCellStyle;
	private final String name;

	/**
	 * @param name
	 *            A unique name for this style
	 */
	TableColumnStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();

		this.name = name;
		this.columnWidth = SimpleLength.cm(2.5); // 0.5.0 changed from 2,500cm to 2.5cm
		this.defaultCellStyle = TableCellStyle.getDefaultCellStyle();
	}

	public TableColumnStyle build() {
		return new TableColumnStyle(this.name, this.columnWidth,
				this.defaultCellStyle);

	}

	/**
	 * Set the column width of a table column.<br>
	 * width is a length value.
	 *
	 * @param width
	 *            - The width of a column as a length
	 * @return true - The width was set, false - this object is no table column,
	 *         you can not set the default cell to it
	 */
	public TableColumnStyleBuilder columnWidth(final Length width) {
		this.columnWidth = width;
		return this;
	}

	public TableColumnStyleBuilder defaultCellStyle(
			final TableCellStyle defaultCellStyle) {
		this.defaultCellStyle = defaultCellStyle;
		return this;
	}

}
