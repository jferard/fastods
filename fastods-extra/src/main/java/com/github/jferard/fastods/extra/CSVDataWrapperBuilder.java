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

package com.github.jferard.fastods.extra;

import com.github.jferard.javamcsv.MetaCSVDataException;
import com.github.jferard.javamcsv.MetaCSVParseException;
import com.github.jferard.javamcsv.MetaCSVParser;
import com.github.jferard.javamcsv.MetaCSVReadException;
import com.github.jferard.javamcsv.MetaCSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CSVDataWrapperBuilder {
    private final File csvFile;
    private final InputStream is;
    private File metaCSVFile;
    private String[] metaCSVDirectives;
    private MetaCSVParser metaCSVParser;
    private AnyProcessorFactory anyProcessorFactory;
    private InputStream metaIs;

    public CSVDataWrapperBuilder(final File csvFile) {
        this.csvFile = csvFile;
        this.metaCSVFile = null;
        this.metaCSVDirectives = null;
        this.is = null;
        this.metaCSVParser = null;
        this.anyProcessorFactory = AnyProcessorFactory.DEFAULT;
    }

    public CSVDataWrapperBuilder(final InputStream is) {
        this.is = is;
        this.csvFile = null;
        this.metaCSVFile = null;
        this.metaCSVDirectives = null;
        this.metaCSVParser = null;
        this.anyProcessorFactory = AnyProcessorFactory.DEFAULT;
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
            return new CSVDataWrapper(reader, this.anyProcessorFactory);
    }

    public CSVDataWrapperBuilder metaCSVFile(final File metaCSVFile) {
        this.metaCSVFile = metaCSVFile;
        this.metaIs = null;
        this.metaCSVDirectives = null;
        this.metaCSVParser = null;
        return this;
    }

    public CSVDataWrapperBuilder metaCSVDirectives(final String... metaCSVDirectives) {
        this.metaCSVDirectives = metaCSVDirectives;
        this.metaIs = null;
        this.metaCSVFile = null;
        this.metaCSVParser = null;
        return this;
    }

    public CSVDataWrapperBuilder metaCSVInputStream(final InputStream metaIs) {
        this.metaIs = metaIs;
        this.metaCSVFile = null;
        this.metaCSVDirectives = null;
        this.metaCSVParser = null;
        return this;
    }

    public CSVDataWrapperBuilder metaCSVParser(final MetaCSVParser metaCSVParser) {
        this.metaCSVParser = metaCSVParser;
        this.metaCSVFile = null;
        this.metaCSVDirectives = null;
        return this;
    }

    public CSVDataWrapperBuilder anyProcessorFactory(final AnyProcessorFactory anyProcessorFactory) {
        this.anyProcessorFactory = anyProcessorFactory;
        return this;
    }
}
