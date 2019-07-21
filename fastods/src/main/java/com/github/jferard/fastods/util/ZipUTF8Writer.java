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

package com.github.jferard.fastods.util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;

/**
 * A writer for a zip file/ It's a writer and a zipper
 *
 * @author Julien Férard
 */
public interface ZipUTF8Writer extends Closeable, Flushable, Appendable {
    /**
     * the utf-8 encoding
     */
    Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * Close the current entry
     *
     * @throws IOException if an I/O error occurs
     */
    void closeEntry() throws IOException;

    /**
     * finish the zip file
     *
     * @throws IOException if an I/O error occurs
     */
    void finish() throws IOException;

    /**
     * Put a new entry into the zip. This becomes the current entry
     *
     * @param entry the entry
     * @throws IOException if an I/O error occurs
     */
    void putNextEntry(final ZipEntry entry) throws IOException;

    /**
     * Add a comment to the zip
     *
     * @param comment the comment
     */
    void setComment(final String comment);

    /**
     * Write a string to the writer
     *
     * @param sequence the string
     * @throws IOException if an I/O error occurs
     */
    void write(final CharSequence sequence) throws IOException;
}
