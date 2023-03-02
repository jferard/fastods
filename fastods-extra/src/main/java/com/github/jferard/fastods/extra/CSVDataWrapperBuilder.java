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

package com.github.jferard.fastods.extra;

import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.javamcsv.MetaCSVDataException;
import com.github.jferard.javamcsv.MetaCSVParseException;
import com.github.jferard.javamcsv.MetaCSVParser;
import com.github.jferard.javamcsv.MetaCSVReadException;
import com.github.jferard.javamcsv.MetaCSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class CSVDataWrapperBuilder {
    private static final TableCellStyle HEADER_STYLE =
            TableCellStyle.builder("csv-data-wrapper").backgroundColor(SimpleColor.GRAY64)
                    .fontWeightBold().build();

    private final File csvFile;
    private final InputStream is;
    private File metaCSVFile;
    private String[] metaCSVDirectives;
    private ObjectProcessorFactory objectProcessorFactory;
    private InputStream metaIs;
    private Logger logger;
    private int max;
    private TableCellStyle headerStyle;
    private String rangeName;

    public CSVDataWrapperBuilder(final File csvFile) {
        this.csvFile = csvFile;
        this.is = null;
        this.headerStyle = HEADER_STYLE;
        this.objectProcessorFactory = ObjectProcessorFactory.DEFAULT;
        this.max = -1;
    }

    public CSVDataWrapperBuilder(final InputStream is) {
        this.is = is;
        this.csvFile = null;
        this.headerStyle = HEADER_STYLE;
        this.objectProcessorFactory = ObjectProcessorFactory.DEFAULT;
        this.max = -1;
    }

    public CSVDataWrapper build()
            throws MetaCSVReadException, MetaCSVDataException, MetaCSVParseException, IOException {
        final MetaCSVReader reader;
        if (this.csvFile != null) {
            if (this.metaCSVFile != null) {
                reader = MetaCSVReader.create(this.csvFile, this.metaCSVFile);
            } else if (this.metaIs != null) {
                final InputStream is = new FileInputStream(this.csvFile);
                reader = MetaCSVReader.create(is, this.metaIs);
            } else if (this.metaCSVDirectives != null) {
                reader = MetaCSVReader.create(this.csvFile, this.metaCSVDirectives);
            } else {
                reader = MetaCSVReader.create(this.csvFile);
            }
        } else {
            assert this.is != null;
            if (this.metaCSVFile != null) {
                final InputStream metaIs = new FileInputStream(this.metaCSVFile);
                reader = MetaCSVReader.create(this.is, metaIs);
            } else if (this.metaIs != null) {
                reader = MetaCSVReader.create(this.is, this.metaIs);
            } else if (this.metaCSVDirectives != null) {
                reader = MetaCSVReader.create(this.is, this.metaCSVDirectives);
            } else {
                throw new AssertionError();
            }
        }
        return new CSVDataWrapper(this.logger, this.objectProcessorFactory, this.rangeName, reader,
                this.headerStyle, this.max);
    }

    public CSVDataWrapperBuilder metaCSVFile(final File metaCSVFile) {
        this.metaCSVFile = metaCSVFile;
        this.metaIs = null;
        this.metaCSVDirectives = null;
        return this;
    }

    public CSVDataWrapperBuilder metaCSVDirectives(final String... metaCSVDirectives) {
        this.metaCSVDirectives = metaCSVDirectives;
        this.metaIs = null;
        this.metaCSVFile = null;
        return this;
    }

    public CSVDataWrapperBuilder metaCSVInputStream(final InputStream metaIs) {
        this.metaIs = metaIs;
        this.metaCSVFile = null;
        this.metaCSVDirectives = null;
        return this;
    }

    public CSVDataWrapperBuilder objectProcessorFactory(
            final ObjectProcessorFactory objectProcessorFactory) {
        this.objectProcessorFactory = objectProcessorFactory;
        return this;
    }

    /**
     * Set a logger
     *
     * @param logger the logger
     * @return this for fluent style
     */
    public CSVDataWrapperBuilder logger(final Logger logger) {
        this.logger = logger;
        return this;
    }

    /**
     * Set a header style
     *
     * @param headerStyle the cell style for the header
     * @return this for fluent style
     */
    public CSVDataWrapperBuilder headerStyle(final TableCellStyle headerStyle) {
        this.headerStyle = headerStyle;
        return this;
    }

    /**
     * Remove the default header style
     *
     * @return this for fluent style
     */
    public CSVDataWrapperBuilder noHeaderStyle() {
        this.headerStyle = null;
        return this;
    }

    /**
     * Set a limit to the number of rows
     *
     * @param max the last line written
     * @return this for fluent style
     */
    public CSVDataWrapperBuilder max(final int max) {
        this.max = max;
        return this;
    }

    /**
     * Set the auto filter
     *
     * @return this for fluent style
     */
    public CSVDataWrapperBuilder autoFilterRangeName(final String rangeName) {
        this.rangeName = rangeName;
        return this;
    }
}
