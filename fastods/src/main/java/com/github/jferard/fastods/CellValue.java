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

import com.github.jferard.fastods.tool.FastOds;

import java.util.Calendar;
import java.util.Date;

/**
 * The value of a cell
 *
 * @author Julien Férard
 */
public abstract class CellValue {
    /**
     * Converts a value to a CellValue
     *
     * @param o the value
     * @return the CellValue
     */
    public static CellValue fromObject(final Object o) {
        if (o == null) return new VoidValue();
        else if (o instanceof CellValue) return (CellValue) o;
        else if (o instanceof String) return new StringValue((String) o);
        else if (o instanceof Text) return new TextValue((Text) o);
        else if (o instanceof Number) // BigDecimal, Byte, Short, Integer,
            // Long, Float, Double
            return new FloatValue((Number) o);
        else if (o instanceof Boolean) return new BooleanValue((Boolean) o);
        else if (o instanceof Date) // java.util.Date & java.sql.Date,
            // java.sql.Time, java.sql.Timestamp
            return new DateValue((Date) o);
        else if (o instanceof Calendar) return new DateValue(((Calendar) o).getTime());
        else // Byte[], ...
            return new StringValue(o.toString());
    }

    /**
     * Converts a value to a CellValue
     *
     * @param type a hint
     * @param o    the value
     * @return the CellValue
     */
    public static CellValue fromTypeAndObject(final TableCell.Type type, final Object o, final String currency)
            throws FastOdsException {
        switch (type) {
            case BOOLEAN: return BooleanValue.from(o);
			case CURRENCY: return CurrencyValue.from(o, currency);
			case DATE: return DateValue.from(o);
			case FLOAT: return FloatValue.from(o);
			case PERCENTAGE: return PercentageValue.from(o);
			case STRING: return StringValue.from(o);
			case TIME: return TimeValue.from(o);
            case VOID: return VoidValue.from(o);
        }
        throw new FastOdsException("Should not happen!");
    }

    /**
     * Set this value in the cell
     *
     * @param tableCell the cell where to add this value
     */
    public abstract void setToCell(TableCell tableCell);
}
