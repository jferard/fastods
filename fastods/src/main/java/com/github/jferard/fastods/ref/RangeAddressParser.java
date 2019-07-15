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

package com.github.jferard.fastods.ref;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import static com.github.jferard.fastods.ref.CellRef.TABLE_CELL_SEP;
import static com.github.jferard.fastods.ref.RangeRef.RANGE_SEP;

/**
 * Converts a cell address to a position
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class RangeAddressParser {

    /**
     * Create a new position parser
     *
     * @param tableNameUtil to check/escape table names
     */
    public static RangeAddressParser create(final TableNameUtil tableNameUtil) {
        return new RangeAddressParser(new TableAddressParser(tableNameUtil),
                new LocalCellAddressParser());
    }

    private final TableAddressParser tableAddressParser;
    private final LocalCellAddressParser localCellAddressParser;

    /**
     * Create a new position parser
     */
    public RangeAddressParser(final TableAddressParser tableAddressParser,
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
    public RangeRef parse(final String address)
            throws ParseException, UnsupportedEncodingException {
        final int tableSepIndex = address.lastIndexOf(TABLE_CELL_SEP);
        final String localRangeAddress;
        final TableRef tableRef;
        if (tableSepIndex == -1) {
            tableRef = null;
            localRangeAddress = address;
        } else {
            final String tableAddress = address.substring(0, tableSepIndex);
            tableRef = this.tableAddressParser.parse(tableAddress);
            localRangeAddress = address.substring(tableSepIndex + 1);
        }
        final int colonSepIndex = localRangeAddress.indexOf(RANGE_SEP);
        if (colonSepIndex == -1) {
            throw new ParseException("Expected a `:` symbol: " + localRangeAddress, 0);
        }
        final LocalCellRef localFromCellRef = this.localCellAddressParser
                .parse(localRangeAddress.substring(0, colonSepIndex));
        final LocalCellRef localToCellRef = this.localCellAddressParser
                .parse(localRangeAddress.substring(colonSepIndex + 1));

        return new RangeRef(tableRef, localFromCellRef, localToCellRef);
    }
}