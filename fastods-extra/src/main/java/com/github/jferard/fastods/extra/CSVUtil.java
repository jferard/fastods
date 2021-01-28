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
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.ref.TableNameUtil;
import com.github.jferard.javamcsv.MetaCSVDataException;
import com.github.jferard.javamcsv.MetaCSVParseException;
import com.github.jferard.javamcsv.MetaCSVReadException;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

public class CSVUtil {
    /**
     * Export the content of a CSV file to an ODS file. The CSV file must have a MetaCSV file that
     * describes its content. See https://github.com/jferard/MetaCSV
     *
     * @param csvFile the source CSV File. The MetaCSV file must have the same path but a .mcsv extension
     * @param odsFile the destination ODS File
     * @throws MetaCSVReadException  if the CSV file can't be read
     * @throws MetaCSVDataException  if the MetaCSV file is inconsistent
     * @throws MetaCSVParseException if the MetaCSV file can't be parser
     * @throws IOException           if an I/O error occurs
     */
    public static void csvToOds(final File csvFile, final File odsFile)
            throws MetaCSVReadException, MetaCSVDataException, MetaCSVParseException, IOException {
        final CSVDataWrapper dataWrapper =
                CSVDataWrapper.builder(csvFile).autoFilterRangeName("csv-import").build();
        final String tableName = new TableNameUtil().escapeTableName(csvFile.getName());
        csvToOds(dataWrapper, odsFile, tableName);
    }

    /**
     * Copy the wrapped data to an ODS file.
     *
     * @param dataWrapper the data wrapper
     * @param tableName the table name
     * @param odsFile the destination ODS File
     * @throws IOException           if an I/O error occurs
     */
    public static void csvToOds(final CSVDataWrapper dataWrapper, final File odsFile,
                                final String tableName)
            throws IOException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("csv-import"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable(tableName);
        final TableCellWalker walker = table.getWalker();
        dataWrapper.addToTable(walker);
        writer.saveAs(odsFile);
    }
}
