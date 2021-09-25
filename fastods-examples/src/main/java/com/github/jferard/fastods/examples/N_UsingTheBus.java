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

package com.github.jferard.fastods.examples;

import com.github.jferard.fastods.NamedOdsDocument;
import com.github.jferard.fastods.NamedOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.tool.FastOdsBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;

public class N_UsingTheBus {
    static void example() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Using the FastODS Bus
        // # Goal
        // Imagine you have a database, a bunch of predefined group queries, and you want to write
        // the result of each group of queries in a new table of an ODS document. You take the first
        // group. You create the first table with the name of the group, and run the first query.
        // When the result is available, you add this result to the table (you know
        // `ResultSetDataWrapper`, don't you?). You run the second query, and wait again for the
        // result. When the first group of queries is run and all the results are added to the
        // table, then you continue with the second group. And so on.
        //
        // At the end of the last result of the last group of queries, you launch a
        // `writer.saveAs` and the document is written on disk.
        //
        // If you have already heard of concurrency, you might want to use a producer-consumer
        // method. The producer queries the database, and when the result is available, puts
        // this result in a queue. The consumer retrieves a result from the queue and writes
        // it to the disk. Let's try.
        //
        // Don't expect a full production code, this will be a simple sketch. Since I don't have
        // a huge database to provide, I will generate random numbers. And I will use some kind
        // of global variable to maintain the state of the producer.
        //
        // ## Implementation
        // ### The styles problem
        // Since the document needs to be written before it's fully created, we can't use
        // FastODS usual mechanism to store the styles as they are used and write them
        // at the end.
        //
        // We need to create a "named" document
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("bus"), Locale.US);
        final NamedOdsFileWriter writer =
                odsFactory.createWriter(new File("generated_files", "n_using_the_bus.ods"));
        final NamedOdsDocument document = writer.document();

        // And to register the styles:
        final TableCellStyle bold =
                TableCellStyle.builder("bold").fontWeightBold().build();
        document.addCellStyle(bold);
        document.freezeStyles(); // any style used that was not registered will throw an error
        //
        // ### The communication
        //
        // First, we need an interface Element to wrap our objects:
        // ```
        // private static interface Element {
        //    void write();
        // }
        // ```
        // (You may create regular classes that implement this interface, but I'll just use
        // anonymous classes.)
        //

        // Now, we create a simple bus:
        final FastOdsBus<Element> bus = new FastOdsBus<Element>();
        //
        // ### The consumer
        // The consumer is not very hard to understand: take the next element and write it,
        // until the bus is closed.
        final Thread consumer = new Thread() {
            @Override
            public void run() {
                try {
                    while (!bus.isClosed()) {
                        final Element element = bus.get(); // this method is blocking: we'll wait...
                        if (element == null) { // this is the flag.
                            break;
                        }
                        element.write();
                    }
                    writer.save();
                    writer.close();
                } catch (final Exception e) {
                    /* you'll do something better here, like log the error */
                }
            }
        };
        //
        // We can start the consumer now.
        consumer.start();

        //
        // ### The producer
        // The producer is a bit more difficult. First, we need to keep track of the current
        // table and cell. **I do not recommend this, but I'll use a "global state"**:
        //
        // ```
        //     private static class State {
        //        public Table table;
        //        public TableCellWalker walker;
        //    }
        // ```
        //
        // Let's create the instance:
        final State state = new State();

        // An we need random numbers.
        final Random random = new Random();

        // Now, let's start:
        for (int i = 0; i < 20; i++) {
            final String tableName = "Group " + i;
            bus.put(new Element() { // title element
                @Override
                public void write() throws IOException {
                    state.table = document.addTable(tableName);
                    state.walker = state.table.getWalker();
                }
            });
            for (int j = 0; j < 10; j++) {
                // Our fake query
                final String title = "> Query " + j;
                final List<List<Integer>> intsRows = new ArrayList<List<Integer>>();
                for (int r=0; r<25; r++) {
                    final List<Integer> ints = new ArrayList<Integer>(10);
                    for (int c = 0; c < 10; c++) {
                        ints.add(random.nextInt());
                    }
                    intsRows.add(ints);
                }

                // Put the result:
                bus.put(new Element() { // result element
                    @Override
                    public void write() throws IOException {
                        state.walker.setStringValue(title);
                        state.walker.setStyle(bold);
                        state.walker.nextRow();
                        state.walker.nextRow();
                        for (final List<Integer> row : intsRows) {
                            for (final Integer v : row) {
                                state.walker.setFloatValue(v);
                                state.walker.next();
                            }
                            state.walker.nextRow();
                        }
                        state.walker.nextRow();
                    }
                });
            }
        }

        // Now we are done.
        bus.put(null);
        bus.close();

        // Just wait for the consumer to finish his job.
        try {
            consumer.join();
        } catch (final InterruptedException e) {
            /* log me... */
        }
        // And that's all.
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        final File destFile = new File("generated_files", "n_using_the_bus.ods");
        ExamplesTestHelper.validate(destFile);
    }

    private interface Element {
        void write() throws IOException;
    }

    private static class State {
        public Table table;
        public TableCellWalker walker;
    }
}
