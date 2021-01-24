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

import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.DataWrapper;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.javamcsv.CurrencyDecimalFieldDescription;
import com.github.jferard.javamcsv.DataType;
import com.github.jferard.javamcsv.MetaCSVDataException;
import com.github.jferard.javamcsv.MetaCSVMetaData;
import com.github.jferard.javamcsv.MetaCSVParseException;
import com.github.jferard.javamcsv.MetaCSVReadException;
import com.github.jferard.javamcsv.MetaCSVReader;

import com.github.jferard.javamcsv.MetaCSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class CSVUtil {
    public static void csvToOds(final InputStream is, final InputStream metaIs,
                                final OutputStream os, final String tableName)
            throws MetaCSVReadException, MetaCSVDataException, MetaCSVParseException, IOException {
        final DataWrapper wrapper = CSVDataWrapper.builder(is).metaCSVInputStream(metaIs).build();
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("csv-import"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable(tableName);
        final TableCellWalker walker = table.getWalker();
        wrapper.addToTable(walker);
        writer.save(os);
    }
}
