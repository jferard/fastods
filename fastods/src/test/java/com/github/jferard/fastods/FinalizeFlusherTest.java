/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.odselement.ContentElement;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

/**
 * Created by jferard on 07/05/17.
 */
public class FinalizeFlusherTest {
    @Test
    public final void testFlush() throws IOException {
        final XMLUtil util = XMLUtil.create();
        final ContentElement contentElement = PowerMock.createMock(ContentElement.class);
        final OdsElements odsElements = PowerMock.createMock(OdsElements.class);
        final ZipUTF8Writer w = PowerMock.createMock(ZipUTF8Writer.class);

        PowerMock.resetAll();
        contentElement.writePostamble(util, w);
        odsElements.writeSettings(util, w);
        odsElements.writeExtras(util, w);
        w.finish();
        w.close();

        PowerMock.replayAll();
        final OdsAsyncFlusher flusher = new FinalizeFlusher(odsElements, contentElement);
        flusher.flushInto(util, w);

        PowerMock.verifyAll();
    }

    @Test
    public final void testEnd() throws IOException {
        final ContentElement contentElement = PowerMock.createMock(ContentElement.class);
        final OdsElements odsElements = PowerMock.createMock(OdsElements.class);

        PowerMock.resetAll();

        PowerMock.replayAll();
        final OdsAsyncFlusher flusher = new FinalizeFlusher(odsElements, contentElement);
        PowerMock.verifyAll();
        Assert.assertTrue(flusher.isEnd());
    }
}