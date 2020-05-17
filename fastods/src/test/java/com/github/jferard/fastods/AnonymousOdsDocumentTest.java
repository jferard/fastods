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

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnonymousOdsDocumentTest extends OdsDocumentTest<AnonymousOdsDocument> {
    private AnonymousOdsDocument document;
    private Logger logger;
    private XMLUtil xmlUtil;

    @Before
    public void setUp() {
        this.logger = PowerMock.createMock(Logger.class);
        this.xmlUtil = XMLUtil.create();
        this.odsElements = PowerMock.createMock(OdsElements.class);
    }

    @Test
    public void testCreate() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);

        PowerMock.replayAll();
        this.document = this.getDocument();

        PowerMock.verifyAll();
    }

    @Test
    public void testSave() throws IOException {
        final ZipUTF8Writer writer = PowerMock.createMock(ZipUTF8Writer.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.createEmptyElements(writer);
        this.odsElements.writeMimeType(this.xmlUtil, writer);
        this.odsElements.writeMeta(this.xmlUtil, writer);
        this.odsElements.writeStyles(this.xmlUtil, writer);
        this.odsElements.writeContent(this.xmlUtil, writer);
        this.odsElements.writeSettings(this.xmlUtil, writer);
        this.odsElements.writeManifest(this.xmlUtil, writer);
        this.odsElements.writeExtras(writer);
        this.logger.log(Level.FINE, "file saved");
        writer.close();

        PowerMock.replayAll();
        try {
            this.document = this.getDocument();
            this.document.save(writer);
        } finally {
            writer.close();
        }

        PowerMock.verifyAll();
    }

    @Override
    AnonymousOdsDocument getDocument() {
        return AnonymousOdsDocument.create(this.logger, this.xmlUtil, this.odsElements);
    }
}