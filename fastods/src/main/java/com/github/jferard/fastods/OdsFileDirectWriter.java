/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods;

import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * The OdsFileDirectWriter class represents a direct writer. It asks the flusher to flush its data directly into the
 * zip writer.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class OdsFileDirectWriter implements OdsFileWriter {
    public static OdsFileWriterBuilder builder(final Logger logger, final OdsDocument document) {
        return new OdsFileWriterBuilder(logger, document);
    }

    private final OdsDocument document;
    private final Logger logger;
    private final ZipUTF8Writer writer;
    private final XMLUtil xmlUtil;

    /**
     * Create a new ODS file.
     *
     * @param logger   the logger
     * @param document the document to write
     * @param xmlUtil
     * @param writer   The writer for this file
     */
    OdsFileDirectWriter(final Logger logger, final OdsDocument document, final XMLUtil xmlUtil, final ZipUTF8Writer writer)
            throws FileNotFoundException {
        this.logger = logger;
        this.document = document;
        this.xmlUtil = xmlUtil;
        this.writer = writer;
    }

    @Override
    public void close() throws IOException {
        this.writer.flush();
        this.writer.close();
    }

    @Override
    public OdsDocument document() {
        return this.document;
    }

    @Override
    public void save() throws IOException {
        this.document.save(this.writer);
    }

    @Override
    public void update(final OdsFlusher flusher) throws IOException {
        flusher.flushInto(this.xmlUtil, this.writer);
    }
}