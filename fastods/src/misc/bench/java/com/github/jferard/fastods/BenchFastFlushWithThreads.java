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
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BenchFastFlushWithThreads extends Bench {
    static class Consumer extends Thread {
        private final Logger logger;
        private final OdsFileWriterAdapter writerAdapter;

        public Consumer(final Logger logger, final OdsFileWriterAdapter writerAdapter) {
            this.logger = logger;
            this.writerAdapter = writerAdapter;
        }

        @Override
        public void run() {
            long t = 0;
            try {
                while (this.writerAdapter.isNotStopped()) {
                    this.writerAdapter.waitForData();
                    t += this.flushAdaptee();
                }
                t += this.flushAdaptee();
            } catch (final IOException e) {
                this.logger.log(Level.SEVERE, "", e);
            }
            System.out.println(">> Write time " + t + " ms");
        }

        public long flushAdaptee() throws IOException {
            final long t1 = System.currentTimeMillis();
            this.writerAdapter.flushAdaptee();
            final long t2 = System.currentTimeMillis();
            final long t = t2 - t1;
            return t;
        }
    }

    /**
     * The Producer ads data to the document
     */
    static class Producer extends Thread {
        private final NamedOdsDocument document;
        private final int rowCount;
        private final int colCount;
        private final Random random;

        public Producer(final NamedOdsDocument document, final int rowCount, final int colCount,
                        final Random random) {
            this.document = document;
            this.rowCount = rowCount;
            this.colCount = colCount;
            this.random = random;
        }

        @Override
        public void run() {
            try {
                final Table table = this.document.addTable("test", this.rowCount, this.colCount);
                final TableCellWalker walker = table.getWalker();

                for (int y = 0; y < this.rowCount; y++) {
                    for (int x = 0; x < this.colCount; x++) {
                        walker.setFloatValue(this.random.nextInt(1000));
                        walker.next();
                    }
                    walker.nextRow();
                }

                this.document.save();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final Logger logger;
    private final OdsFactory odsFactory;

    public BenchFastFlushWithThreads(final Logger logger, final int rowCount, final int colCount) {
        super(logger, "FastODSFlushWithThreads", rowCount, colCount);
        this.logger = logger;
        this.odsFactory = OdsFactory.create(this.logger, Locale.US);
    }

    @Test
    public void test0() throws IOException {
        this.test();
    }

    @Override
    public long test() throws IOException {
        try {
            // Open the file.
            this.logger.info("testFastFlushThread: filling a " + this.getRowCount() + " rows, " +
                    this.getColCount() + " columns spreadsheet");
            final long t1 = System.currentTimeMillis();
            final OdsFileWriterAdapter writerAdapter = this.odsFactory.createWriterAdapter(
                    new File("generated_files", "fastods_flush_thread_benchmark" + ".ods"));
            final NamedOdsDocument document = writerAdapter.document();
            final Producer producer = this.createProducer(document);
            final Consumer consumer = this.createConsumer(writerAdapter);
            consumer.start();
            producer.start();
            producer.join();
            consumer.join();
            final long t2 = System.currentTimeMillis();

            this.logger.info("Filled in " + (t2 - t1) + " ms");
            return t2 - t1;
        } catch (final InterruptedException e) {
            this.logger.log(Level.SEVERE, "", e);
        }
        return 0;
    }

    public Consumer createConsumer(OdsFileWriterAdapter writerAdapter) {
        return new Consumer(this.logger, writerAdapter);
    }

    private Producer createProducer(NamedOdsDocument document) {
        return new Producer(document, this.getRowCount(), this.getColCount(),
                this.getRandom());
    }
}
