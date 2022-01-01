/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.ref;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * Converts a cell address to a position
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class CellAddressParser {
    /**
     * Create a new position parser
     *
     * @param tableNameUtil to check/escape table names
     * @return the cell address parser
     */
    public static CellAddressParser create(final TableNameUtil tableNameUtil) {
        return new CellAddressParser(new TableAddressParser(tableNameUtil),
                new LocalCellAddressParser());
    }

    private final TableAddressParser tableAddressParser;
    private final LocalCellAddressParser localCellAddressParser;

    /**
     * Create a new position parser
     */
    public CellAddressParser(final TableAddressParser tableAddressParser,
                             final LocalCellAddressParser localCellAddressParser) {
        this.tableAddressParser = tableAddressParser;
        this.localCellAddressParser = localCellAddressParser;
    }


    /**
     * @param address [[filename#][$]tablename.]col row
     * @return a new position
     * @throws ParseException               If the address can't be parsed.
     * @throws UnsupportedEncodingException if the encoding of the file is not correct
     */
    public CellRef parse(final String address) throws ParseException, UnsupportedEncodingException {
        final int dotIndex = address.lastIndexOf(CellRef.TABLE_CELL_SEP);
        final String localCellAddress;
        final TableRef tableRef;
        if (dotIndex == -1) {
            tableRef = null;
            localCellAddress = address;
        } else {
            final String tableAddress = address.substring(0, dotIndex);
            tableRef = this.tableAddressParser.parse(tableAddress);
            localCellAddress = address.substring(dotIndex + 1);
        }
        final LocalCellRef localCellRef = this.localCellAddressParser.parse(localCellAddress);

        return new CellRef(tableRef, localCellRef);
    }
}