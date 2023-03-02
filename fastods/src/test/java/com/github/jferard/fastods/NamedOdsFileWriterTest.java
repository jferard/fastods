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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilderImpl;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NamedOdsFileWriterTest {
    private OdsElements odsElements;
    private Logger logger;
    private XMLUtil xmlUtil;

    @Before
    public final void setUp() {
        this.logger = PowerMock.createNiceMock(Logger.class);
        final ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        this.logger.addHandler(handler);
        this.odsElements = PowerMock.createMock(OdsElements.class);
        this.xmlUtil = XMLUtil.create();
    }

    @Test
    public final void testSaveWriter() throws IOException {
        final ZipUTF8WriterBuilderImpl zb = PowerMock.createMock(ZipUTF8WriterBuilderImpl.class);
        final ZipUTF8Writer z = PowerMock.createMock(ZipUTF8Writer.class);
        final File temp = File.createTempFile("tempfile", ".tmp");

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(zb.build(EasyMock.isA(FileOutputStream.class))).andReturn(z);
        this.odsElements.saveAsync();

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getNamedDocument();
        final NamedOdsFileWriter writer =
                new OdsFileWriterBuilder(this.logger, document).zipBuilder(zb)
                        .file(temp.getAbsolutePath()).build();
        writer.save();

        PowerMock.verifyAll();
    }
    private NamedOdsDocument getNamedDocument() {
        return NamedOdsDocument.create(this.logger, this.xmlUtil, this.odsElements);
    }
}