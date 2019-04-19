/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.testlib.Bench;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class BenchSimpleOdf extends Bench {
    private final Logger logger;

    public BenchSimpleOdf(final Logger logger, final int rowCount, final int colCount) {
        super(logger, "SimpleODF", rowCount, colCount);
        this.logger = logger;
    }

    @Override
    public long test() throws IOException {
        try {
            // Open the file.
            this.logger.info("testSimpleOdf: filling a " + this.getRowCount() + " rows, " + this
                    .getColCount() + " columns spreadsheet");
            final long t1 = System.currentTimeMillis();
            final SpreadsheetDocument document = SpreadsheetDocument.newSpreadsheetDocument();
            final Table table = document.appendSheet("test");
            for (int y = 0; y < this.getRowCount(); y++) {
                final Row row = table.appendRow();
                for (int x = 0; x < this.getColCount(); x++) {
                    row.getCellByIndex(x).setStringValue(String.valueOf(this.getRandom().nextInt(1000)));
                }
            }

            document.save(new File("generated_files", "simpleodf_benchmark.ods").getPath());
            final long t2 = System.currentTimeMillis();
            this.logger.info("Filled in " + (t2 - t1) + " ms");
            return t2 - t1;
        } catch (final Exception e) {
            throw new IOException(e);
        }
    }
}
