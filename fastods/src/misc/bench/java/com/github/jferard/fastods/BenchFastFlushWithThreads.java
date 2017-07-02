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
import org.junit.Test;
import com.github.jferard.fastods.testlib.Bench;
import java.util.Random;

public class BenchFastFlushWithThreads extends Bench {
	private final Logger logger;
	private final OdsFactory odsFactory;

	public BenchFastFlushWithThreads(final Logger logger, final int rowCount, final int colCount) {
		super(logger, "FastODSFlushWithThreads", rowCount, colCount);
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
			this.logger.info("testFastFlushThread: filling a " + this.getRowCount() + " rows, "
					+ this.getColCount() + " columns spreadsheet");
			final long t1 = System.currentTimeMillis();
			final OdsFileWriterAdapter writerAdapter =
					this.odsFactory.createWriterAdapter(new File("generated_files", "fastods_flush_thread_benchmark" +
							".ods"));
			final OdsDocument document = writerAdapter.document();
			Producer a = new Producer(document, this.getRowCount(), this.getColCount(), this.getRandom());
			Consumer b = new Consumer(writerAdapter);
//			b.start();
			a.run(); // start();
//			a.join();
			b.join();
			final long t2 = System.currentTimeMillis();

			this.logger.info("Filled in " + (t2 - t1) + " ms");
			return t2 - t1;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	class Consumer extends Thread {
		private final OdsFileWriterAdapter writerAdapter;

		public Consumer(final OdsFileWriterAdapter writerAdapter) {
			this.writerAdapter = writerAdapter;
		}

		@Override
		public void run() {
			long t = 0;
			try {
				while (this.writerAdapter.isNotStopped()) {
					this.writerAdapter.waitForData();
					final long t1 = System.currentTimeMillis();
					this.writerAdapter.flushAdaptee();
					final long t2 = System.currentTimeMillis();
					t += t2 - t1;
				}
				final long t1 = System.currentTimeMillis();
				this.writerAdapter.flushAdaptee();
				final long t2 = System.currentTimeMillis();
				t += t2 - t1;
			} catch (IOException e) {
			}
			System.out.println(">> Write time " + t + " ms");
		}
	}

	class Producer extends Thread {
		private final OdsDocument document;
		private int rowCount;
		private int colCount;
		private Random random;

		public Producer(final OdsDocument document, final int rowCount, final int colCount, final Random random) {
			this.document = document;
			this.rowCount = rowCount;
			this.colCount = colCount;
			this.random = random;
		}

		@Override
		public void run() {
			try {
				final Table table = document.addTable("test", this.rowCount, this.colCount);

				for (int y = 0; y < this.rowCount; y++) {
					final TableRow row = table.nextRow();
					final TableCellWalker walker = row.getWalker();
					for (int x = 0; x < this.colCount; x++) {
						walker.setFloatValue(this.random.nextInt(1000));
						walker.next();
					}
				}

				document.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
