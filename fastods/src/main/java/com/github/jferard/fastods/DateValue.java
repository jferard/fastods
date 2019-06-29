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

import java.util.Calendar;
import java.util.Date;

/**
 * A CellValue that contains a date
 * @author Julien Férard
 */
public class DateValue implements CellValue {
	public static DateValue from(final Object o) throws FastOdsException {
		if (o instanceof Date) {
			return new DateValue((Date) o);
		} else if (o instanceof Calendar) {
			return new DateValue(((Calendar) o).getTime());
		} else if (o instanceof Number) {
			return new DateValue(new Date(((Number) o).longValue()));
		} else if (o instanceof DateValue) {
			return (DateValue) o;
		} else{
			throw new FastOdsException("Can't cast " + o + " to Date");
		}
	}

    private final Date value;

	/**
	 * @param value the date
	 */
	public DateValue(final Date value) {
		this.value = new Date(value.getTime());
	}

	@Override
	public void setToCell(final TableCell cell) {
		cell.setDateValue(this.value);
	}
}