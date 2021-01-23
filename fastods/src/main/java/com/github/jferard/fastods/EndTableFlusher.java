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
 * An async flusher for the end of the table. Writes remaining rows and the table postamble.
 *
 * @author Julien Férard
 */
public class EndTableFlusher implements OdsAsyncFlusher {
    private final TableAppender appender;
    private final List<TableRowImpl> rows;

    /**
     * @param appender the table to end
     * @param rows     a view on the remaining rows.
     */
    public EndTableFlusher(final TableAppender appender, final List<TableRowImpl> rows) {
        this.appender = appender;
        this.rows = rows;
    }

    @Override
    public void flushInto(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
        for (final TableRowImpl row : this.rows) {
            TableRowImpl.appendXMLToTable(row, xmlUtil, writer);
        }
        // free rows
        Collections.fill(this.rows, null);
        this.appender.appendPostamble(writer);
    }

    @Override
    public boolean isEnd() {
        return false;
    }
}
