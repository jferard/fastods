/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods;

/**
 * The value in this cell will be void
 *
 * @author Julien Férard
 */
public final class VoidValue implements CellValue {
    /**
     * The one and only instance of void.
     */
    public static final VoidValue INSTANCE = new VoidValue();

    /**
     * Converts a value to a void value
     *
     * @param o the value
     * @return the void value
     * @throws FastOdsException if the cast was not possible
     */
    public static VoidValue from(final Object o) throws FastOdsException {
        if (o == null || o instanceof VoidValue) {
            return INSTANCE;
        } else {
            throw new FastOdsException("Can't cast " + o + " to Void");
        }
    }

    private VoidValue() {
    }

    @Override
    public void setToCell(final TableCell tableCell) {
        tableCell.setVoidValue();
    }
}
