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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A helper class to copy files
 */
public class FileUtil {
    private static final int BUFFER_SIZE = 16 * 4096;
    private static final int START_SIZE = 16 * BUFFER_SIZE;
    private final int bufferSize;
    private final int startSize;
    /**
     * @param bufferSize the size of the copy buffer
     * @param startSize  the initial size of the byte array
     */
    public FileUtil(final int bufferSize, final int startSize) {
        this.bufferSize = bufferSize;
        this.startSize = startSize;
    }

    /**
     * @return a new FileUtil
     */
    public static FileUtil create() {
        return new FileUtil(BUFFER_SIZE, START_SIZE);
    }

    /**
     * Fills a byte array with the content of a file.
     *
     * @param file the file to read
     * @return the byte array
     * @throws IOException if an I/O occurs
     */
    public byte[] readFile(final File file) throws IOException {
        final InputStream is = new FileInputStream(file);
        final int length = (int) file.length();
        try {
            return this.readStream(is, length);
        } finally {
            is.close();
        }
    }

    /**
     * Fills a byte array with the content of a stream.
     *
     * @param is the stream to read
     * @return the byte array
     * @throws IOException if an I/O occurs
     */
    public byte[] readStream(final InputStream is) throws IOException {
        return this.readStream(is, this.startSize);
    }

    /**
     * Fills a byte array with the content of a stream.
     *
     * @param is              the stream to read
     * @param customStartSize the initial size of the byte array, e.g the length of the file
     * @return the byte array
     * @throws IOException if an I/O occurs
     */
    public byte[] readStream(final InputStream is, final int customStartSize) throws IOException {
        byte[] bytes =
                new byte[customStartSize <= this.bufferSize ? this.bufferSize : customStartSize];
        int totalCount = 0;
        while (true) {
            bytes = this.ensureBytes(totalCount, bytes);
            final int count = is.read(bytes, totalCount, this.bufferSize);
            if (count == -1) {
                break;
            }
            totalCount += count;
        }
        final byte[] new_bytes = new byte[totalCount];
        System.arraycopy(bytes, 0, new_bytes, 0, totalCount);
        return new_bytes;
    }

    private byte[] ensureBytes(final int totalCount, final byte[] curBytes) {
        if (curBytes.length > totalCount + this.bufferSize) {
            return curBytes;
        }
        // find the next power of two.
        int len = curBytes.length;
        while (len <= totalCount + this.bufferSize) {
            len *= 2;
        }
        final byte[] new_bytes = new byte[len];
        System.arraycopy(curBytes, 0, new_bytes, 0, totalCount);
        return new_bytes;
    }
}
