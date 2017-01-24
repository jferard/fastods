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

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

public class BenchFast extends Bench {

	private final OdsFactory odsFactory;

	public BenchFast(final Logger logger, final int rowCount, final int colCount) {
		super(logger, "FastODS", rowCount, colCount);
		this.odsFactory = new OdsFactory(this.logger, Locale.US);
	}

	@Override
	public long test() throws IOException {
		// Open the file.
		this.logger.info("testFast: filling a " + rowCount + " rows, "
				+ colCount + " columns spreadsheet");
		final long t1 = System.currentTimeMillis();
		final OdsDocument document = odsFactory.createDocument();
		OdsFileWriter writer =
				this.odsFactory.createWriter(document, new File("generated_files", "fastods_benchmark.ods"));
		final Table table = document.addTable("test", rowCount, colCount);

		for (int y = 0; y < rowCount; y++) {
			final HeavyTableRow row = table.nextRow();
			final TableCellWalker walker = row.getWalker();
			for (int x = 0; x < colCount; x++) {
				walker.setFloatValue(this.random.nextInt(1000));
				walker.next();
			}
		}

		writer.save();
		final long t2 = System.currentTimeMillis();
		this.logger.info("Filled in " + (t2 - t1) + " ms");
		return t2 - t1;
	}
}
