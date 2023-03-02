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

package com.github.jferard.fastods.util;

import java.io.FileNotFoundException;
import java.io.OutputStream;

/**
 * A FileOpenResult is the result of an attempt to lock a file. There are three cases : 1. the
 * file is a dir:
 * can't get the stream; 2. the file exists: getStream() will get the lock; 3. the file doesn't
 * exist: thee lock is
 * immediately put on it.
 *
 * @author Julien Férard
 */
public interface FileOpenResult {
    /**
     * A constant to return when the file is a dir
     */
    FileOpenResult FILE_IS_DIR = new FileIsDir();

    /**
     * @return a stream if the file exists and can be opened
     * @throws FileNotFoundException if the file doesn't exist or is locked
     */
    OutputStream getStream() throws FileNotFoundException;
}