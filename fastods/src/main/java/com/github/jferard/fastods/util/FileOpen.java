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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * The file is already open as a stream.
 *
 * @author Julien Férard
 */
public class FileOpen implements FileOpenResult {
    private final OutputStream stream;

    /**
     * @param stream the output stream
     */
    public FileOpen(final OutputStream stream) {
        this.stream = stream;
    }

    /**
     * @param file the file.
     * @return the result of the operation
     * @throws FileNotFoundException if the file does not exist
     */
    public static FileOpenResult openFile(final File file) throws FileNotFoundException {
        if (file.isDirectory()) {
            return FILE_IS_DIR;
        }

        if (file.exists()) {
            return new FileExists(file);
        }

        return new FileOpen(new FileOutputStream(file));
    }

    /**
     * @param filename the name of the file.
     * @return the result of the operation
     * @throws FileNotFoundException if the file does not exist
     */
    public static FileOpenResult openFile(final String filename) throws FileNotFoundException {
        final File f = new File(filename);
        return openFile(f);
    }

    @Override
    public OutputStream getStream() {
        return this.stream;
    }
}
