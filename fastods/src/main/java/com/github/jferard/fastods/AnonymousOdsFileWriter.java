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

import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilderImpl;
import com.github.jferard.fastods.util.ZipUTF8WriterImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An ods file writer. It contains a document and is responsible for the recording.
 * Anonymous means that the destination file is not set.
 * The content of the document is only flushed once, when the document is saved.
 * That means that one doesn't have to define the style early.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class AnonymousOdsFileWriter {
    private final AnonymousOdsDocument document;
    private final Logger logger;

    /**
     * Create a new ODS file.
     *
     * @param logger   the logger
     * @param document the document to write
     */
    AnonymousOdsFileWriter(final Logger logger, final AnonymousOdsDocument document) {
        this.logger = logger;
        this.document = document;
    }

    /**
     * @return the underlying document, under the interface
     */
    public OdsDocument document() {
        return this.document;
    }

    /**
     * Writes the document to a stream.
     * WARNING: The user shall close the stream (since 0.6.1).
     *
     * @param out The OutputStream that should be used.
     * @throws IOException The file can't be saved.
     */
    public void save(final OutputStream out) throws IOException {
        final ZipUTF8Writer writer = ZipUTF8WriterImpl.builder().build(out);
        this.save(writer);
        writer.finish();
        writer.flush();
    }

    /**
     * Writes the document to a stream.
     * WARNING: The user shall close the stream (since 0.6.1).
     *
     * @param out The OutputStream that should be used.
     * @param builder  a builder for the ZipOutputStream and the Writer (buffers,
     *                 level, ...)
     * @throws IOException The file can't be saved.
     * @deprecated use `save(ZipUTF8Writer writer)`
     */
    @Deprecated
    public void save(final OutputStream out, final ZipUTF8WriterBuilderImpl builder) throws IOException {
        final ZipUTF8Writer writer = builder.build(out);
        this.save(writer);
        writer.finish();
        writer.flush();
    }

    /**
     * Writes the document to a writer. The use shall close the writer.
     * WARNING: The user shall `finish` and `flush` the writer (since 0.6.1).
     *
     * @param writer the ZipUTF8WriterImpl that should be used
     * @throws IOException If an I/O error occurs during the save
     */
    public void save(final ZipUTF8Writer writer) throws IOException {
        this.document.save(writer);
    }

    /**
     * Save the new file.
     *
     * @param filename the name of the destination file
     * @throws IOException If an I/O error occurs during the save
     */
    public void saveAs(final String filename) throws IOException {
        this.saveAs(Paths.get(filename));
    }

    /**
     * Save the new file.
     *
     * @param file the destination file
     * @throws IOException If an I/O error occurs
     * @deprecated use `saveAs(path)`
     */
    @Deprecated
    public void saveAs(final File file) throws IOException {
        if (file == null) {
            throw new IOException("File is null");
        }
        this.saveAs(file.toPath());
    }

    /**
     * Save the new file.
     *
     * @param path the destination path
     * @throws IOException If an I/O error occurs
     */
    public void saveAs(final Path path) throws IOException {
        try {
            final OutputStream out = Files.newOutputStream(path);
            try {
                this.save(out);
            } finally {
                out.flush();
                out.close();
            }
        } catch (final IOException e) {
            this.logger.log(Level.SEVERE, "Can't open " + path, e);
            throw e;
        }
    }

    /**
     * Save the document to filename.
     *
     * @param filename the name of the destination file
     * @param builder  a builder for the ZipOutputStream and the Writer (buffers,
     *                 level, ...)
     * @throws IOException if the file was not saved
     */
    public void saveAs(final String filename, final ZipUTF8WriterBuilder builder)
            throws IOException {
        this.saveAs(Paths.get(filename), builder);
    }

    /**
     * Save the document to file.
     *
     * @param file the destination file
     * @param builder  a builder for the ZipOutputStream and the Writer (buffers,
     *                 level, ...)
     * @throws IOException if the file was not saved
     * @deprecated use `saveAs(path, builder)`
     */
    @Deprecated
    public void saveAs(final File file, final ZipUTF8WriterBuilder builder) throws IOException {
        if (file == null) {
            throw new IOException("File is null");
        }
        this.saveAs(file.toPath(), builder);
    }

    public void saveAs(final Path path, final ZipUTF8WriterBuilder builder) throws IOException {
        try {
            final OutputStream out = Files.newOutputStream(path);
            final ZipUTF8Writer writer = builder.build(out);
            try {
                this.save(writer);
            } finally {
                writer.finish();
                writer.close();
            }
        } catch (final IOException e) {
            this.logger.log(Level.SEVERE, "Can't open " + path, e);
            throw e;
        }
    }
}