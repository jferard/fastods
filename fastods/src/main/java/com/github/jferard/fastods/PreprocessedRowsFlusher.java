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

package com.github.jferard.fastods;

import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * An async flusher for a collection of rows
 * <p>
 * Sent when a table with a row index that is a multiple of 1024 is used.
 *
 * @author Julien Férard
 */
class PreprocessedRowsFlusher implements OdsAsyncFlusher {
    private static final int STRING_BUILDER_SIZE = 1024 * 32;

    /**
     * Create an new rows flusher
     * Warning, consume the rows by removing the refs.
     *
     * @param xmlUtil   an util
     * @param tableRows a view on the rows
     * @return the flusher
     * @throws IOException if an I/O error occurs
     */
    public static PreprocessedRowsFlusher create(final XMLUtil xmlUtil,
                                                 final List<TableRowImpl> tableRows)
            throws IOException {
        // create a char sequence
        final StringBuilder sb = new StringBuilder(STRING_BUILDER_SIZE);
        for (final TableRowImpl row : tableRows) {
            TableRowImpl.appendXMLToTable(row, xmlUtil, sb);
        }
        // free rows
        Collections.fill(tableRows, null);

        return new PreprocessedRowsFlusher(sb);
    }

    private final CharSequence cs;

    /**
     * @param cs the destination
     * @throws IOException if an I/O error occurs
     */
    PreprocessedRowsFlusher(final CharSequence cs) throws IOException {
        this.cs = cs;
    }

    @Override
    public void flushInto(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
        writer.append(this.cs);
    }

    @Override
    public boolean isEnd() {
        return false;
    }
}