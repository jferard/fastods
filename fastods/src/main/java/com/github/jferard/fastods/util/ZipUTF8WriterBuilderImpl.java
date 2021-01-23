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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.odselement.ManifestElement;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.Deflater;
import java.util.zip.ZipOutputStream;

/**
 * A builder for ZipUTF8Writer
 *
 * @author Julien Férard
 */
public class ZipUTF8WriterBuilderImpl implements ZipUTF8WriterBuilder {
    private static final int DEFAULT_BUFFER = -1;
    private static final int NO_BUFFER = -2;
    private final XMLUtil xmlUtil;

    private int level;
    private int writerBufferSize;
    private int zipBufferSize;

    /**
     * Create a new builder
     */
    public ZipUTF8WriterBuilderImpl() {
        this.level = Deflater.BEST_SPEED;
        this.writerBufferSize = ZipUTF8WriterBuilderImpl.DEFAULT_BUFFER;
        this.zipBufferSize = ZipUTF8WriterBuilderImpl.DEFAULT_BUFFER;
        this.xmlUtil = XMLUtil.create();
    }

    @Override
    public ZipUTF8Writer build(final OutputStream out) {
        final OutputStream bufferedOut;
        switch (this.zipBufferSize) {
            case NO_BUFFER:
                bufferedOut = out;
                break;
            case DEFAULT_BUFFER:
                bufferedOut = new BufferedOutputStream(out);
                break;
            default:
                bufferedOut = new BufferedOutputStream(out, this.zipBufferSize);
                break;
        }
        final ZipOutputStream zipOut = new ZipOutputStream(bufferedOut);
        zipOut.setMethod(ZipOutputStream.DEFLATED);
        zipOut.setLevel(this.level);
        final Writer writer = new OutputStreamWriter(zipOut, CharsetUtil.UTF_8);
        final Writer bufferedWriter;
        switch (this.writerBufferSize) {
            case NO_BUFFER:
                bufferedWriter = writer;
                break;
            case DEFAULT_BUFFER:
                bufferedWriter = new BufferedWriter(writer);
                break;
            default:
                bufferedWriter = new BufferedWriter(writer, this.writerBufferSize);
                break;
        }
        return new ZipUTF8WriterImpl(this.xmlUtil, zipOut, bufferedWriter,
                ManifestElement.create());
    }

    /**
     * Set the zip level
     *
     * @param level the level
     * @return this for fluent style
     */
    public ZipUTF8WriterBuilderImpl level(final int level) {
        this.level = level;
        return this;
    }

    /**
     * Set the buffer size for the writer to 0
     *
     * @return this for fluent style
     */
    public ZipUTF8WriterBuilderImpl noWriterBuffer() {
        this.writerBufferSize = ZipUTF8WriterBuilderImpl.NO_BUFFER;
        return this;
    }

    /**
     * Set the buffer size for the zipper to 0
     *
     * @return this for fluent style
     */
    public ZipUTF8WriterBuilderImpl noZipBuffer() {
        this.zipBufferSize = ZipUTF8WriterBuilderImpl.NO_BUFFER;
        return this;
    }

    /**
     * Set the buffer size for the writer
     *
     * @param size the size of the buffer
     * @return this for fluent style
     */
    public ZipUTF8WriterBuilderImpl writerBuffer(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }

        this.writerBufferSize = size;
        return this;
    }

    /**
     * Set the buffer size for the zipper
     *
     * @param size the size of the buffer
     * @return this for fluent style
     */
    public ZipUTF8WriterBuilderImpl zipBuffer(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }

        this.zipBufferSize = size;
        return this;
    }
}
