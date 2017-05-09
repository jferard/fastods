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

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

/**
 * Created by jferard on 09/05/17.
 */
public class OdsFileWriterAdapterTest {
    private OdsFileWriter w;
    private OdsFlusher f;
    private OdsFileWriterAdapter wa;
    private Queue<OdsFlusher> flushers;
    private OdsDocument d;

    @Before
    public void setUp() {
        this.w = PowerMock.createMock(OdsFileWriter.class);
        this.f = PowerMock.createMock(OdsFlusher.class);
        this.flushers = new LinkedList<OdsFlusher>();
        this.wa = new OdsFileWriterAdapter(this.w, this.flushers);
        this.d = PowerMock.createMock(OdsDocument.class);
    }

    @Test
    public void close() throws Exception {
        PowerMock.replayAll();
        this.wa.close();
        PowerMock.verifyAll();
    }

    @Test
    public void create() throws Exception {
        Assert.assertEquals(OdsFileWriterAdapter.class, OdsFileWriterAdapter.create(this.w).getClass());
    }

    @Test
    public void document() throws Exception {
        EasyMock.expect(this.w.document()).andReturn(this.d);
        PowerMock.replayAll();
        Assert.assertEquals(this.d, this.wa.document());
        PowerMock.verifyAll();
    }

    @Test
    public void save() throws Exception {
        PowerMock.replayAll();
        this.wa.save();
        PowerMock.verifyAll();
    }

    @Test
    public void update() throws Exception {
        PowerMock.replayAll();
        this.wa.update(this.f);
        Assert.assertEquals(Arrays.asList(this.f), this.flushers);
        PowerMock.verifyAll();
    }

    @Test
    public void flushAdapteeWithEmptyQueue() throws Exception {
        PowerMock.replayAll();
        this.wa.flushAdaptee();
        PowerMock.verifyAll();
    }

    @Test
    public void flushAdapteeWithFinalize() throws Exception {
        final OdsFileWriterAdapter wal = this.wa;

        final FinalizeFlusher ff = PowerMock.createMock(FinalizeFlusher.class);
        this.flushers.add(ff);
        // PLAY
        this.w.update(ff);
        this.w.save();

        PowerMock.replayAll();
        this.wa.flushAdaptee();
        PowerMock.verifyAll();
    }

    @Test
    public void flushAdaptee() throws Exception {
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

        // PLAY
        this.w.update(this.f);
        this.w.update(ff);
        this.w.save();

        PowerMock.replayAll();
        t.start();
        this.wa.flushAdaptee();
        PowerMock.verifyAll();
    }

    @Test
    public void isNotStopped() throws Exception {
        PowerMock.replayAll();
        this.wa.isNotStopped();
        PowerMock.verifyAll();
    }

    @Test
    public void waitForData() throws Exception {
        this.flushers.add(this.f);
        PowerMock.replayAll();
        this.wa.waitForData();
        PowerMock.verifyAll();
    }
}