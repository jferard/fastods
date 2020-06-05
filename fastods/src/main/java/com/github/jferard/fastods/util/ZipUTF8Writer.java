/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.odselement.ManifestEntry;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * A writer for a zip file/ It's a writer and a zipper
 *
 * @author Julien Férard
 */
public interface ZipUTF8Writer extends Closeable, Flushable, Appendable {
    /**
     * Add a comment to the zip
     *
     * @param comment the comment
     */
    void setComment(final String comment);


    /**
     * Put a new entry into the zip stream. This becomes the current entry
     *
     * @param entry the entry
     * @throws IOException if an I/O error occurs
     */
    void putNextEntry(ManifestEntry entry) throws IOException;

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
     * Register an entry.
     * Put a new entry into the zip. This becomes the current entry
     *
     * @param entry the entry
     * @throws IOException if an I/O error occurs
     */
    void putAndRegisterNextEntry(final ManifestEntry entry) throws IOException;

    /**
     * Put a new entry into the manifest.
     *
     * @param entry the entry
     * @throws IOException if an I/O error occurs
     */
    void registerEntry(ManifestEntry entry);

    /**
     * Write raw bytes to the output stream
     *
     * @param bytes the bytes to write
     * @throws IOException if an I/O error occurs
     */
    void write(byte[] bytes) throws IOException;
}
