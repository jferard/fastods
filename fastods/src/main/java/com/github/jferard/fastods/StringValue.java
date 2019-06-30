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

package com.github.jferard.fastods;

/**
 * A cell value of string type
 *
 * @author Julien Férard
 */
public class StringValue implements CellValue {
    public static CellValue from(final Object o) {
		if (o instanceof String) {
			return new StringValue((String) o);
		} else if (o instanceof Text) {
			return new TextValue((Text) o);
		} else if (o instanceof StringValue) {
			return (StringValue) o;
		} else if (o instanceof TextValue) {
			return (TextValue) o;
		} else {
			return new StringValue(o.toString());
		}
    }

    private final String value;

	/**
	 * @param value the string
	 */
	public StringValue(final String value) {
		this.value = value;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (!(o instanceof StringValue))
			return false;

		final StringValue other = (StringValue) o;
		return this.value.equals(other.value);
	}

	@Override
	public final int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public void setToCell(final TableCell cell) {
		cell.setStringValue(this.value);
	}

	@Override
	public final String toString() { return "StringValue["+this.value+"]"; }
}
