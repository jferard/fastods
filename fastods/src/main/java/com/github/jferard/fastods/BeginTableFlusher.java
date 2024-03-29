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

import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;

/**
 * An async flusher to append the preamble of the table.
 * Used when user gets the first row of a table.
 *
 * @author Julien Férard
 */
public class BeginTableFlusher implements OdsAsyncFlusher {
    private final TableAppender appender;

    /**
     * @param appender the table to flush
     */
    public BeginTableFlusher(final TableAppender appender) {
        this.appender = appender;
    }

    @Override
    public void flushInto(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
        this.appender.appendOpenTagAndPreambleOnce(xmlUtil, writer);
    }

    @Override
    public boolean isEnd() {
        return false;
    }
}
