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

package com.github.jferard.fastods;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * Created by jferard on 09/05/17.
 */
public class OdsFileWriterAdapterTest {
    private NamedOdsFileWriter w;
    private OdsAsyncFlusher f;
    private OdsFileWriterAdapter wa;
    private Queue<OdsAsyncFlusher> flushers;
    private NamedOdsDocument d;
    private Logger logger;

    @Before
    public void setUp() {
        this.w = PowerMock.createMock(NamedOdsFileWriter.class);
        this.f = PowerMock.createMock(OdsAsyncFlusher.class);
        this.flushers = new LinkedList<OdsAsyncFlusher>();
        this.logger = PowerMock.createMock(Logger.class);
        this.wa = new OdsFileWriterAdapter(this.logger, this.w, this.flushers);
        this.d = PowerMock.createMock(NamedOdsDocument.class);
    }

    @Test
    public void testClose() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.wa.close();
        PowerMock.verifyAll();
    }

    @Test
    public void testCreate() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        final OdsFileWriterAdapter odsFileWriterAdapter =
                OdsFileWriterAdapter.create(this.logger, this.w);

        PowerMock.verifyAll();
        Assert.assertEquals(OdsFileWriterAdapter.class, odsFileWriterAdapter.getClass());
    }

    @Test
    public void testDocument() {
        PowerMock.resetAll();
        EasyMock.expect(this.w.document()).andReturn(this.d);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.wa.document();

        PowerMock.verifyAll();
        Assert.assertEquals(this.d, document);
    }

    @Test
    public void testSave() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.wa.save();

        PowerMock.verifyAll();
    }

    @Test
    public void testUpdate() {
        PowerMock.resetAll();
        this.logger
                .fine("Add new flusher: EasyMock for interface com.github.jferard.fastods" +
                        ".OdsAsyncFlusher");

        PowerMock.replayAll();
        this.wa.update(this.f);

        PowerMock.verifyAll();
        Assert.assertEquals(Collections.singletonList(this.f), this.flushers);
    }

    @Test
    public void testFlushAdapteeWithEmptyQueue() throws Exception {
        PowerMock.resetAll();
        this.logger.fine("Retrieve first flusher: null");

        PowerMock.replayAll();
        this.wa.flushAdaptee();

        PowerMock.verifyAll();
    }

    @Test
    public void testFlushAdapteeWithFinalize() throws Exception {
        final FinalizeFlusher ff = PowerMock.createMock(FinalizeFlusher.class);
        this.flushers.add(ff);

        PowerMock.resetAll();
        this.logger
                .fine("Retrieve first flusher: EasyMock for class com.github.jferard.fastods" +
                        ".FinalizeFlusher");
        EasyMock.expect(ff.isEnd()).andReturn(true);
        this.w.update(ff);

        PowerMock.replayAll();
        this.wa.flushAdaptee();

        PowerMock.verifyAll();
    }

    @Test
    public void testFlushAdaptee() throws Exception {
        final OdsFileWriterAdapter wal = this.wa;
        final FinalizeFlusher ff = PowerMock.createMock(FinalizeFlusher.class);
        this.flushers.add(this.f);
        this.flushers.add(ff);

        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException e) {
                    Assert.fail();
                }
                synchronized (wal) {
                    wal.notify();
                }
            }
        };

        PowerMock.resetAll();
        this.logger
                .fine("Retrieve first flusher: EasyMock for interface com.github.jferard.fastods" +
                        ".OdsAsyncFlusher");
        this.logger
                .fine("Retrieve next flusher: EasyMock for class com.github.jferard.fastods" +
                        ".FinalizeFlusher");
        EasyMock.expect(this.f.isEnd()).andReturn(false);
        EasyMock.expect(ff.isEnd()).andReturn(true);
        this.w.update(this.f);
        this.w.update(ff);

        PowerMock.replayAll();
        t.start();
        this.wa.flushAdaptee();

        PowerMock.verifyAll();
    }

    @Test
    public void testFlushAdapteeWithoutEnd() throws Exception {
        final OdsFileWriterAdapter wal = this.wa;
        final OdsAsyncFlusher fl = this.f;
        final NamedOdsFileWriter wl = this.w;
        final Logger l = this.logger;
        this.flushers.add(this.f);

        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    PowerMock.resetAll();
                    l.fine("Retrieve first flusher: EasyMock for interface com.github.jferard" +
                            ".fastods.OdsAsyncFlusher");
                    wl.update(fl);
                    EasyMock.expect(fl.isEnd()).andReturn(true);

                    PowerMock.replayAll();
                    wal.flushAdaptee();
                    PowerMock.verifyAll();
                } catch (final IOException e) {
                    Assert.fail();
                }
            }
        };

        t.start();
        t.join();
    }

    @Test
    public void testInterruptAdapterInFlush() throws Exception {
        final OdsFileWriterAdapter wal = this.wa;
        final OdsAsyncFlusher fl = this.f;
        final NamedOdsFileWriter wl = this.w;
        final Logger l = this.logger;
        this.flushers.add(fl);

        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    PowerMock.resetAll();
                    l.fine("Retrieve first flusher: EasyMock for interface com.github.jferard" +
                            ".fastods.OdsAsyncFlusher");
                    l.fine("Retrieve next flusher: null");
                    wl.update(fl);
                    EasyMock.expect(fl.isEnd()).andReturn(false);

                    PowerMock.replayAll();
                    wal.flushAdaptee();

                    PowerMock.verifyAll();
                } catch (final IOException e) {
                    Assert.fail();
                } catch (final RuntimeException e) {
                    Assert.fail();
                }
            }
        };

        t.start();
        Thread.sleep(100);
        t.interrupt();
        t.join();
    }

    @Test
    public void testInterruptAdapterInWait() throws Exception {
        final OdsFileWriterAdapter wal = this.wa;

        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    PowerMock.resetAll();
                    PowerMock.replayAll();
                    wal.waitForData();
                    PowerMock.verifyAll();
                } catch (final RuntimeException e) {
                    return;
                }
                Assert.fail();
            }
        };

        t.start();
        Thread.sleep(100);
        t.interrupt();
        t.join();
    }

    @Test
    public void testInterruptFlushAdatee() throws Exception {
        final OdsFileWriterAdapter wal = this.wa;
        final Logger l = this.logger;

        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    PowerMock.resetAll();
                    l.fine("Retrieve first flusher: null");

                    PowerMock.replayAll();
                    wal.flushAdaptee();

                    PowerMock.verifyAll();
                } catch (final RuntimeException e) {
                    return;
                } catch (final IOException e) {
                    Assert.fail();
                }
            }
        };

        t.start();
        Thread.sleep(100);
        t.join();
    }

    @Test
    public void testIsNotStopped() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.wa.isNotStopped();
        PowerMock.verifyAll();
    }

    @Test
    public void testWaitForData() {
        this.flushers.add(this.f);
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.wa.waitForData();
        PowerMock.verifyAll();
    }
}