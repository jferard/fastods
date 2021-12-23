/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * Converts a table address [filename#][$]tablename to a table ref
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class TableAddressParser {
    /**
     * A single quote
     */
    public static final int SINGLE_QUOTE = '\'';

    private static final int HASH = '#';

    private final TableNameUtil tableNameUtil;
    private int status;

    /**
     * Create a new position parser
     *
     * @param tableNameUtil to check/escape table names
     */
    public TableAddressParser(final TableNameUtil tableNameUtil) {
        this.tableNameUtil = tableNameUtil;
        this.status = CellRef.RELATIVE;
    }

    /**
     * @param tableAddress [filename#][$]tablename
     * @return a new position
     * @throws ParseException If the address can't be parsed.
     */
    public TableRef parse(final String tableAddress)
            throws ParseException, UnsupportedEncodingException {
        final int hashIndex = tableAddress.indexOf(HASH);
        final String filename;
        final String escapedTableName;
        if (hashIndex == -1) {
            filename = null;
            escapedTableName = tableAddress;
        } else {
            final String escapedFilename = tableAddress.substring(0, hashIndex);
            filename = this.tableNameUtil.unescapeFilename(escapedFilename);
            escapedTableName = tableAddress.substring(hashIndex + 1);
        }
        String tableName = this.tableNameUtil.unescapeQuotes(escapedTableName);
        if (tableName.charAt(0) == '$') {
            this.status |= CellRef.ABSOLUTE_TABLE;
            tableName = tableName.substring(1);
        }
        return new TableRef(this.tableNameUtil, filename, tableName, this.status);
    }
}