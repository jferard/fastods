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

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.odselement.MetaElement;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilderImpl;
import com.github.jferard.fastods.util.ZipUTF8WriterImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

/**
 * An OdsFactory is the entry point for creating ODS documents.
 *
 * @author Julien Férard
 */
public class OdsFactory {
    private final Logger logger;
    private final PositionUtil positionUtil;
    private final WriteUtil writeUtil;
    private final XMLUtil xmlUtil;
    private final Map<String, String> additionalNamespaceByPrefix;
    private DataStyles format;
    private boolean libreOfficeMode;
    private MetaElement metaElement;
    /**
     * Create a new OdsFactory
     *
     * @param logger                      the logger
     * @param positionUtil                an util
     * @param writeUtil                   an util
     * @param xmlUtil                     an util
     * @param additionalNamespaceByPrefix a map prefix -> namespace
     * @param format                      the data styles
     * @param libreOfficeMode             try to get full compatibility with LO if true
     * @param metaElement                 the meta element
     */
    OdsFactory(final Logger logger, final PositionUtil positionUtil, final WriteUtil writeUtil,
               final XMLUtil xmlUtil, final Map<String, String> additionalNamespaceByPrefix,
               final DataStyles format, final boolean libreOfficeMode,
               final MetaElement metaElement) {
        this.logger = logger;
        this.positionUtil = positionUtil;
        this.writeUtil = writeUtil;
        this.xmlUtil = xmlUtil;
        this.additionalNamespaceByPrefix = additionalNamespaceByPrefix;
        this.format = format;
        this.libreOfficeMode = libreOfficeMode;
        this.metaElement = metaElement;
    }

    /**
     * Create an ods factory builder
     *
     * @param logger the logger
     * @param locale the locale
     * @return the factory builder
     */
    public static OdsFactoryBuilder builder(final Logger logger, final Locale locale) {
        return new OdsFactoryBuilder(logger, locale);
    }

    /**
     * @return a default ods factory
     */
    public static OdsFactory create() {
        return OdsFactory
                .create(Logger.getLogger(NamedOdsDocument.class.getName()), Locale.getDefault());
    }

    /**
     * create an ods factory
     *
     * @param logger the logger
     * @param locale the locale
     * @return the factory
     */
    public static OdsFactory create(final Logger logger, final Locale locale) {
        return new OdsFactoryBuilder(logger, locale).build();
    }

    /**
     * Set the data styles
     *
     * @param ds the data styles
     * @return this for fluent style
     * @deprecated use OdsFactory.builder
     */
    @Deprecated
    public OdsFactory dataStyles(final DataStyles ds) {
        this.format = ds;
        return this;
    }

    /**
     * Disable the LibreOffice mode. The LibreOffice mode adds a style to every cell, to force
     * LibreOffice to render the cell styles correctly.
     * This mode is set by default, and might slow down the generation of the file.
     *
     * @return this for fluent style
     * @deprecated use OdsFactory.builder
     */
    @Deprecated
    public OdsFactory noLibreOfficeMode() {
        this.libreOfficeMode = false;
        return this;
    }

    /**
     * Use a custom meta element
     *
     * @param metaElement the meta element.
     * @return this for fluent style
     * @deprecated use OdsFactory.builder
     */
    @Deprecated
    public OdsFactory metaElement(final MetaElement metaElement) {
        this.metaElement = metaElement;
        return this;
    }

    /**
     * Use custom namespace prefixes in content element.
     *
     * @param additionalNamespaceByPrefix a map prefix -> namespace
     * @return this for fluent style
     */
    @Deprecated
    public OdsFactory addNamespaceByPrefix(final Map<String, String> additionalNamespaceByPrefix) {
        this.additionalNamespaceByPrefix.putAll(additionalNamespaceByPrefix);
        return this;
    }

    /**
     * Create a new, empty document for an anonymous writer. Use addTable to add tables.
     *
     * @return a new document
     */
    private AnonymousOdsDocument createAnonymousDocument() {
        final OdsElements odsElements = OdsElements
                .create(this.positionUtil, this.xmlUtil, this.writeUtil, this.format,
                        this.libreOfficeMode, this.metaElement, this.additionalNamespaceByPrefix);
        return AnonymousOdsDocument.create(this.logger, this.xmlUtil, odsElements);
    }

    /**
     * Create a new, empty document for a normal writer. Use addTable to add tables.
     *
     * @return a new document
     */
    private NamedOdsDocument createNamedDocument() {
        final OdsElements odsElements = OdsElements
                .create(this.positionUtil, this.xmlUtil, this.writeUtil, this.format,
                        this.libreOfficeMode, this.metaElement, this.additionalNamespaceByPrefix);
        return NamedOdsDocument.create(this.logger, this.xmlUtil, odsElements);
    }

    /**
     * @return a new writer, but with no actual name
     */
    public AnonymousOdsFileWriter createWriter() {
        final AnonymousOdsDocument document = this.createAnonymousDocument();
        return new AnonymousOdsFileWriter(this.logger, document);
    }

    /**
     * Create a new ODS file writer from a document. Be careful: this method opens immediately a
     * stream.
     *
     * @param filename the name of the destination file
     * @return the ods writer
     * @throws FileNotFoundException if the file can't be found
     */
    public NamedOdsFileWriter createWriter(final String filename) throws IOException {
        final NamedOdsDocument document = this.createNamedDocument();
        final NamedOdsFileWriter writer = OdsFileDirectWriter.builder(this.logger, document)
                .file(filename).build();
        document.addObserver(writer);
        document.prepare();
        return writer;
    }

    /**
     * Create a new ODS file writer from a document. Be careful: this method opens immediately a
     * stream.
     *
     * @param file the destination file
     * @return the ods writer
     * @throws IOException if an I/O error occurs
     */
    public NamedOdsFileWriter createWriter(final File file) throws IOException {
        final NamedOdsDocument document = this.createNamedDocument();
        final NamedOdsFileWriter writer =
                OdsFileDirectWriter.builder(this.logger, document).file(file)
                        .build();
        document.addObserver(writer);
        document.prepare();
        return writer;
    }

    /**
     * Create an adapter for a writer.
     *
     * @param file the file
     * @return the adapter
     * @throws IOException if an I/O error occurs
     */
    public OdsFileWriterAdapter createWriterAdapter(final File file) throws IOException {
        final NamedOdsDocument document = this.createNamedDocument();
        final ZipUTF8WriterBuilderImpl zipUTF8Writer = ZipUTF8WriterImpl.builder().noWriterBuffer();
        final OdsFileWriterAdapter writerAdapter = OdsFileWriterAdapter.create(this.logger,
                OdsFileDirectWriter.builder(this.logger, document).file(file)
                        .zipBuilder(zipUTF8Writer).build());
        document.addObserver(writerAdapter);
        document.prepare();
        return writerAdapter;
    }

    /**
     * the file state
     *
     * @deprecated use ??
     */
    @Deprecated
    public enum FileState {
        /**
         * the file is a directory
         */
        IS_DIRECTORY,
        /**
         * the file already exists
         */
        FILE_EXISTS,
        /**
         * the file may be written
         */
        OK
    }
}
