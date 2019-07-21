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

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;
import com.github.jferard.fastods.util.ZipUTF8WriterImpl;
import com.google.common.collect.Sets;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 */
public class AnonymousOdsFileWriterTest {
    private static final int EMPTY_DOCUMENT_SIZE = 5253;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private ZipUTF8WriterBuilder builder;

    private Logger logger;
    private OdsElements odsElements;
    private OdsFactory odsFactory;
    private ByteArrayOutputStream os;
    private XMLUtil xmlUtil;

    @Before
    public final void setUp() {
        this.logger = PowerMock.createNiceMock(Logger.class);
        this.os = new ByteArrayOutputStream();
        this.xmlUtil = XMLUtil.create();
        this.odsElements = PowerMock.createMock(OdsElements.class);
        this.builder = PowerMock.createMock(ZipUTF8WriterBuilder.class);
        this.odsFactory = OdsFactory.create(this.logger, Locale.US);
    }

    @Test(expected = IOException.class)
    public final void testFileIsDir() throws IOException {
        final Logger l = PowerMock.createNiceMock(Logger.class);
        final OdsFactory of = OdsFactory.create(l, Locale.US);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);

        PowerMock.replayAll();
        of.createWriter().saveAs(".");

        PowerMock.verifyAll();
    }

    @Test
    public final void testSaveEmpyDocumentToStream() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();

        PowerMock.resetAll();
        PowerMock.replayAll();
        try {
            writer.save(this.os);
        } finally {
            this.os.close();
        }

        PowerMock.verifyAll();
        final byte[] buf = this.os.toByteArray();
        final InputStream is = new ByteArrayInputStream(buf);
        final ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry = zis.getNextEntry();
        final Collection<String> names = new HashSet<String>();
        while (entry != null) {
            names.add(entry.getName());
            entry = zis.getNextEntry();
        }

        System.out.println(buf.length);
        Assert.assertTrue(Math.abs(EMPTY_DOCUMENT_SIZE - buf.length) <= 2);
        Assert.assertEquals(Sets.newHashSet("settings.xml", "Configurations2/images/Bitmaps/",
                "Configurations2/toolbar/", "META-INF/manifest.xml", "Thumbnails/",
                "Configurations2/floater/", "Configurations2/menubar/", "mimetype", "meta.xml",
                "Configurations2/accelerator/current.xml", "Configurations2/popupmenu/",
                "styles.xml", "content.xml", "Configurations2/progressbar/",
                "Configurations2/statusbar/"), names);
    }

    @Test
    public final void testSaveEmpyDocumentToStreamAndAddPrePostamble() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();

        PowerMock.resetAll();
        PowerMock.replayAll();
        try {
            this.os.write("preamble".getBytes());
            writer.save(this.os);
            this.os.write("postamble".getBytes());
        } finally {
            this.os.close();
        }

        PowerMock.verifyAll();
        final byte[] buf = this.os.toByteArray();
        final byte[] pre = Arrays.copyOfRange(buf, 0, 8);
        final byte[] body = Arrays.copyOfRange(buf, 8, buf.length - 9);
        final byte[] post = Arrays.copyOfRange(buf, buf.length - 9, buf.length);

        // preamble
        final Charset charset = Charset.forName("US-ASCII");
        Assert.assertArrayEquals(pre, "preamble".getBytes(charset));

        // postamble
        Assert.assertArrayEquals(post, "postamble".getBytes(charset));

        // body
        final InputStream is = new ByteArrayInputStream(body);
        final ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry = zis.getNextEntry();
        final Collection<String> names = new HashSet<String>();
        while (entry != null) {
            names.add(entry.getName());
            entry = zis.getNextEntry();
        }

        Assert.assertEquals(Sets.newHashSet("settings.xml", "Configurations2/images/Bitmaps/",
                "Configurations2/toolbar/", "META-INF/manifest.xml", "Thumbnails/",
                "Configurations2/floater/", "Configurations2/menubar/", "mimetype", "meta.xml",
                "Configurations2/accelerator/current.xml", "Configurations2/popupmenu/",
                "styles.xml", "content.xml", "Configurations2/progressbar/",
                "Configurations2/statusbar/"), names);
    }

    @Test
    public final void testSaveEmpyDocumentToWriterAndAddEntry() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final ZipUTF8Writer zw = ZipUTF8WriterImpl.builder().build(this.os);

        PowerMock.resetAll();
        PowerMock.replayAll();
        try {
            writer.save(zw);
            zw.putNextEntry(new ZipEntry("last"));
            zw.write("last content");
            zw.closeEntry();
        } finally {
            zw.close();
        }

        PowerMock.verifyAll();
        final InputStream is = new ByteArrayInputStream(this.os.toByteArray());
        final ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry = zis.getNextEntry();
        final Collection<String> names = new HashSet<String>();
        while (entry != null) {
            names.add(entry.getName());
            entry = zis.getNextEntry();
        }

        Assert.assertEquals(
                Sets.newHashSet("settings.xml", "last", "Configurations2/images/Bitmaps/",
                        "Configurations2/toolbar/", "META-INF/manifest.xml", "Thumbnails/",
                        "Configurations2/floater/", "Configurations2/menubar/", "mimetype",
                        "meta.xml", "Configurations2/accelerator/current.xml",
                        "Configurations2/popupmenu/", "styles.xml", "content.xml",
                        "Configurations2/progressbar/", "Configurations2/statusbar/"), names);
    }

    @Test
    public final void testSaveTwiceEmpyDocumentToStream() throws IOException {
        /// see https://github.com/jferard/fastods/issues/138
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();

        PowerMock.resetAll();
        PowerMock.replayAll();
        try {
            writer.save(this.os);
            writer.save(this.os);
        } finally {
            this.os.close();
        }

        PowerMock.verifyAll();
        final byte[] buf = this.os.toByteArray();
        final InputStream is = new ByteArrayInputStream(buf);
        final ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry = zis.getNextEntry();
        final List<String> names = new ArrayList<String>();
        while (entry != null) {
            names.add(entry.getName());
            entry = zis.getNextEntry();
        }
        Collections.sort(names);

        Assert.assertTrue(Math.abs(EMPTY_DOCUMENT_SIZE * 2 - buf.length) <= 10);
        // Every element appears twice
        Assert.assertEquals(
                Arrays.asList("Configurations2/accelerator/current.xml", "Configurations2/floater/",
                        "Configurations2/images/Bitmaps/", "Configurations2/menubar/",
                        "Configurations2/popupmenu/", "Configurations2/progressbar/",
                        "Configurations2/statusbar/", "Configurations2/toolbar/",
                        "META-INF/manifest.xml", "Thumbnails/", "content.xml", "meta.xml",
                        "mimetype", "settings.xml", "styles.xml"), names);
    }

    @Test
    public final void testSaveWriter() throws IOException {
        final ZipUTF8WriterBuilder zb = PowerMock.createMock(ZipUTF8WriterBuilder.class);
        final ZipUTF8Writer z = PowerMock.createMock(ZipUTF8Writer.class);
        final File temp = File.createTempFile("tempfile", ".tmp");

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(zb.build(EasyMock.isA(FileOutputStream.class))).andReturn(z);
        this.odsElements.saveAsync();

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getNamedDocument();
        final NamedOdsFileWriter writer = new OdsFileWriterBuilder(this.logger, document)
                .zipBuilder(zb).filename(temp.getAbsolutePath()).build();
        writer.save();

        PowerMock.verifyAll();
    }

    private NamedOdsDocument getNamedDocument() {
        return NamedOdsDocument.create(this.logger, this.xmlUtil, this.odsElements);
    }

    private AnonymousOdsDocument getAnonymousDocument() {
        return AnonymousOdsDocument.create(this.logger, this.xmlUtil, this.odsElements);
    }

    @Test // (expected = IOException.class)
    public final void testSaveWriterWithException() throws IOException {
        final OutputStream outputStream = PowerMock.createMock(OutputStream.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.createEmptyElements(EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeMimeType(EasyMock.eq(this.xmlUtil),
                EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeMeta(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeStyles(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeContent(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeSettings(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeManifest(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeExtras(EasyMock.isA(ZipUTF8Writer.class));
        outputStream.write(EasyMock.isA(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
        EasyMock.expectLastCall().anyTimes();
        outputStream.flush();
        EasyMock.expectLastCall().anyTimes();
        outputStream.close();

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        try {
            new AnonymousOdsFileWriter(this.logger, document).save(outputStream);
        } finally {
            outputStream.close();
        }

        PowerMock.verifyAll();
    }

    @Test(expected = IOException.class)
    public final void testSaveAsNullFile() throws IOException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        new AnonymousOdsFileWriter(this.logger, document).saveAs((File) null);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSaveWithWriter() throws IOException {
        final OutputStream outputStream = PowerMock.createMock(OutputStream.class);
        final ZipUTF8Writer z = PowerMock.createMock(ZipUTF8Writer.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.builder.build(EasyMock.isA(FileOutputStream.class))).andReturn(z);

        this.odsElements.createEmptyElements(EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeMimeType(EasyMock.eq(this.xmlUtil),
                EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeMeta(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeStyles(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeContent(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeSettings(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeManifest(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeExtras(EasyMock.isA(ZipUTF8Writer.class));
        outputStream.write(EasyMock.isA(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
        EasyMock.expectLastCall().anyTimes();
        outputStream.flush();
        EasyMock.expectLastCall().anyTimes();
        z.close();

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        new AnonymousOdsFileWriter(this.logger, document).saveAs("test", this.builder);

        PowerMock.verifyAll();
    }

    @Test(expected = IOException.class)
    public final void testSaveWithWriterDir() throws IOException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        new AnonymousOdsFileWriter(this.logger, document).saveAs(".", this.builder);

        PowerMock.verifyAll();
    }
}