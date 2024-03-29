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

import com.github.jferard.fastods.util.FileOpen;
import com.github.jferard.fastods.util.FileOpenResult;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilderImpl;
import com.github.jferard.fastods.util.ZipUTF8WriterImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class OdsFileWriterBuilder {
    private final Logger logger;
    private final NamedOdsDocument document;
    private OutputStream out;
    private ZipUTF8WriterBuilderImpl builder;

    /**
     * Create a new ODS file.
     *
     * @param logger   the logger
     * @param document the document to write
     */
    OdsFileWriterBuilder(final Logger logger, final NamedOdsDocument document) {
        this.logger = logger;
        this.document = document;
        this.builder = ZipUTF8WriterImpl.builder();
    }

    /**
     * @return the writer for the ods file
     */
    public NamedOdsFileWriter build() {
        final ZipUTF8Writer writer = this.builder.build(this.out);
        return new OdsFileDirectWriter(this.logger, XMLUtil.create(), this.document, writer);
    }

    /**
     * Beware: this locks the file until the writer is closed
     *
     * @param filename the name of the destination file
     * @return this for fluent style
     * @throws FileNotFoundException if the file does not exist.
     */
    public OdsFileWriterBuilder file(final String filename) throws FileNotFoundException {
        return this.openResult(FileOpen.openFile(filename));
    }

    /**
     * Beware: this locks the file until the writer is closed
     *
     * @param file the destination file
     * @return this for fluent style
     * @throws FileNotFoundException if the file does not exist
     */
    public OdsFileWriterBuilder file(final File file) throws FileNotFoundException {
        return this.openResult(FileOpen.openFile(file));
    }

    /**
     * @param out where to write
     * @return this for fluent style
     */
    public OdsFileWriterBuilder outputStream(final OutputStream out) {
        this.out = out;
        return this;
    }

    /**
     * Locks the file
     *
     * @param lockResult the result of a file lock
     * @return this for fluent style
     * @throws FileNotFoundException the file exists but is a directory
     *                               rather than a regular file, does not exist but cannot
     *                               be created, or cannot be opened for any other reason
     */
    public OdsFileWriterBuilder openResult(final FileOpenResult lockResult)
            throws FileNotFoundException {
        this.out = lockResult.getStream();
        return this;
    }

    /**
     * @param builder a builder for the ZipOutputStream and the Writer (buffers,
     *                level, ...)
     * @return this for fluent style
     */
    public OdsFileWriterBuilder zipBuilder(final ZipUTF8WriterBuilderImpl builder) {
        this.builder = builder;
        return this;
    }
}
