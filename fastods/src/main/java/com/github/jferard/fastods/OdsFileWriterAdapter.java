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

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * The OdsFileWriterAdapter class represents an adapter to a writer. It stores a queue of
 * flushers. Usage:
 * <ul>
 * <li>A producer thread that writes on a OdsFileWriterAdapter.document()</li>
 * <li>A consumer thread that uses the following structure to flush the data</li>
 * </ul>
 * <p>
 * <pre>
 * while (this.writerAdapter.isNotStopped()) {
 *     this.writerAdapter.waitForData();
 *     this.writerAdapter.flushAdaptee();
 * }
 * this.writerAdapter.flushAdaptee();
 * </pre>
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class OdsFileWriterAdapter implements NamedOdsFileWriter {
    /**
     * @param logger  the logger
     * @param adaptee the adaptee writer
     * @return the new adapter
     */
    public static OdsFileWriterAdapter create(final Logger logger,
                                              final NamedOdsFileWriter adaptee) {
        return new OdsFileWriterAdapter(logger, adaptee, new LinkedList<OdsAsyncFlusher>());
    }

    private final Logger logger;
    private final NamedOdsFileWriter adaptee;
    private final Queue<OdsAsyncFlusher> flushers;
    private boolean stopped;

    /**
     * Create an new adapter
     *
     * @param logger   the logger
     * @param adaptee  the adaptee writer
     * @param flushers the queue of flushers
     */
    OdsFileWriterAdapter(final Logger logger, final NamedOdsFileWriter adaptee,
                         final Queue<OdsAsyncFlusher> flushers) {
        this.logger = logger;
        this.adaptee = adaptee;
        this.flushers = flushers;
    }

    @Override
    public void close() {
    }

    @Override
    public NamedOdsDocument document() {
        return this.adaptee.document();
    }

    @Override
    public synchronized void save() {
    }

    @Override
    public synchronized void update(final OdsAsyncFlusher flusher) {
        this.flushers.add(flusher);
        this.logger.fine("Add new flusher: " + flusher);
        this.notifyAll();
    }

    /**
     * Flushes all available flushers to the adaptee writer.
     * The thread falls asleep if we reach the end of the queue without a FinalizeFlusher.
     *
     * @throws IOException if the adaptee throws an IOException
     */
    public synchronized void flushAdaptee() throws IOException {
        OdsAsyncFlusher flusher = this.flushers.poll();
        this.logger.fine("Retrieve first flusher: " + flusher);
        if (flusher == null) {
            this.notifyAll(); // wakes up other threads: no flusher available
            return;
        }

        while (flusher != null) {
            this.adaptee.update(flusher);
            if (flusher.isEnd()) {
                this.stopped = true;
                this.notifyAll(); // wakes up other threads: end of game
                return;
            }
            flusher = this.flushers.poll();
            this.logger.fine("Retrieve next flusher: " + flusher);
        }
        this.notifyAll(); // wakes up other threads: no flusher left
    }

    /**
     * @return true if the adapter is stopped
     */
    public synchronized boolean isNotStopped() {
        return !this.stopped;
    }

    /**
     * wait for the data
     */
    public synchronized void waitForData() {
        while (this.flushers.isEmpty() && this.isNotStopped()) {
            try {
                this.wait();
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

}
