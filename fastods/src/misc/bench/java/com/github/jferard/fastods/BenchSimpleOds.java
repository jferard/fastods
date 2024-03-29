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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods;

import com.github.jferard.fastods.testlib.Bench;
import org.simpleods.ObjectQueue;
import org.simpleods.OdsFile;
import org.simpleods.SimpleOdsException;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class BenchSimpleOds extends Bench {
    private final Logger logger;

    public BenchSimpleOds(final Logger logger, final int rowCount, final int colCount) {
        super(logger, "SimpleODS", rowCount, colCount);
        this.logger = logger;
    }

    @Override
    public long test() throws IOException {
        try {
            // Open the file.
            this.logger.info("testSimpleOds: filling a " + this.getRowCount() + " rows, " +
                    this.getColCount() + " columns spreadsheet");
            final long t1 = System.currentTimeMillis();
            final OdsFile file =
                    new OdsFile(new File("generated_files", "simpleods_benchmark.ods").getPath());
            file.addTable("test");
            final org.simpleods.Table table =
                    (org.simpleods.Table) file.getContent().getTableQueue().get(0);

            final ObjectQueue rows = table.getRows();
            for (int y = 0; y < this.getRowCount(); y++) {
                final org.simpleods.TableRow row = new org.simpleods.TableRow();
                rows.add(row);
                for (int x = 0; x < this.getColCount(); x++) {
                    row.setCell(x, String.valueOf(this.getRandom().nextInt(1000)));
                }
            }

            file.save();
            final long t2 = System.currentTimeMillis();
            this.logger.info("Filled in " + (t2 - t1) + " ms");
            return t2 - t1;
        } catch (final SimpleOdsException e) {
            throw new IOException(e);
        }
    }
}
