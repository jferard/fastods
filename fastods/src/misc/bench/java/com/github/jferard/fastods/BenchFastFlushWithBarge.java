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

import com.github.jferard.charbarge.AppendableConsumer;
import com.github.jferard.fastods.testlib.Bench;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;

public class BenchFastFlushWithBarge extends Bench {
	private final Logger logger;
	private final OdsFactory odsFactory;

	public BenchFastFlushWithBarge(final Logger logger, final int rowCount, final int colCount) {
		super(logger, "FastODSFlushWithBarge", rowCount, colCount);
		this.logger = logger;
		this.odsFactory = OdsFactory.create(this.logger, Locale.US);
	}

	@Test
	public void test0() throws IOException  {
		this.test();
	}

	@Override
	public long test() throws IOException {
		try {
			// Open the file.
			this.logger.info("testFastFlushBarge: filling a " + this.getRowCount() + " rows, "
					+ this.getColCount() + " columns spreadsheet");
			final long t1 = System.currentTimeMillis();
			final OdsFileWriterToBarge writerToBarge =
					this.odsFactory.createWriterToBarge(new File("generated_files", "fastods_flush_barge_benchmark" +
							".ods"));
			final OdsDocument document = writerToBarge.document();
			final Producer a = new Producer(document, this.getRowCount(), this.getColCount(), this.getRandom());
			final AppendableConsumer b = writerToBarge.consumer();
			b.start();
			a.start(); // start();
			b.join();
			final long t2 = System.currentTimeMillis();

			this.logger.info("Filled in " + (t2 - t1) + " ms");
			return t2 - t1;
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	class Producer extends Thread {
		private final OdsDocument document;
		private final int rowCount;
		private final int colCount;
		private final Random random;

		public Producer(final OdsDocument document, final int rowCount, final int colCount, final Random random) {
			this.document = document;
			this.rowCount = rowCount;
			this.colCount = colCount;
			this.random = random;
		}

		@Override
		public void run() {
			try {
				final Table table = this.document.addTable("test", this.rowCount, this.colCount);

				for (int y = 0; y < this.rowCount; y++) {
					final TableRow row = table.nextRow();
					final TableCellWalker walker = row.getWalker();
					for (int x = 0; x < this.colCount; x++) {
						walker.setFloatValue(this.random.nextInt(1000));
						walker.next();
					}
				}

				this.document.save();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}
}
