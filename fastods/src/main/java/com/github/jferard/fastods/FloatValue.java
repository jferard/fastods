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

package com.github.jferard.fastods;

/**
 * A value of type float
 *
 * @author Julien Férard
 */
public class FloatValue extends CellValue {
	private final Number value;

	/**
	 * @param value the float
	 */
	FloatValue(final Number value) {
		this.value = value;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (!(o instanceof FloatValue))
			return false;

		final FloatValue other = (FloatValue) o;
		return this.value.equals(other.value);
	}

	@Override
	public final int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public void setToCell(final TableCell tableCell) {
		tableCell.setFloatValue(this.value);
	}
}
