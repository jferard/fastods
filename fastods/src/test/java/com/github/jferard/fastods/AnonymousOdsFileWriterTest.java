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

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StandardOdsEntry;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.PageLayoutStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilderImpl;
import com.github.jferard.fastods.util.ZipUTF8WriterImpl;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 */
public class AnonymousOdsFileWriterTest {
    public static final int EMPTY_DOCUMENT_SIZE_LEVEL0 = 12784;
    private static final int EMPTY_DOCUMENT_SIZE = 5214; // 5226;
    private static final int DELTA = 50;
    private ZipUTF8WriterBuilderImpl builder;

    private Logger logger;
    private OdsElements odsElements;
    private OdsFactory odsFactory;
    private ByteArrayOutputStream os;
    private XMLUtil xmlUtil;

    @Before
    public final void setUp() {
        this.logger = PowerMock.createNiceMock(Logger.class);
        final ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        this.logger.addHandler(handler);
        this.os = new ByteArrayOutputStream();
        this.xmlUtil = XMLUtil.create();
        this.odsElements = PowerMock.createMock(OdsElements.class);
        this.builder = PowerMock.createMock(ZipUTF8WriterBuilderImpl.class);
        this.odsFactory = OdsFactory.create(this.logger, Locale.US);
    }

    @Test
    public final void testFileIsDir() {
        final Logger l = PowerMock.createNiceMock(Logger.class);
        final OdsFactory of = OdsFactory.create(l, Locale.US);

        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertThrows(IOException.class, () -> of.createWriter().saveAs("."));

        PowerMock.verifyAll();
    }

    @Test
    public final void testSaveEmptyDocumentToStream() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();

        PowerMock.resetAll();
        PowerMock.replayAll();
        try {
            writer.save(this.os);
        } finally {
            this.os.close();
        }

        PowerMock.verifyAll();
        this.checkEmptyDocument();
    }

    private void checkEmptyDocument() throws IOException {
        final byte[] buf = this.os.toByteArray();
        final InputStream is = new ByteArrayInputStream(buf);
        final ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry = zis.getNextEntry();
        final Collection<String> names = new HashSet<>();
        while (entry != null) {
            names.add(entry.getName());
            entry = zis.getNextEntry();
        }

        if (Math.abs(EMPTY_DOCUMENT_SIZE - buf.length) > DELTA) {
            System.out.printf("Expected size: %d, actual size: %d%n", EMPTY_DOCUMENT_SIZE,
                    buf.length);
            Assert.fail();
        }
        Assert.assertEquals(
                TestHelper.newSet("settings.xml", "Configurations2/images/Bitmaps/",
                        "Configurations2/toolbar/", "META-INF/manifest.xml", "Thumbnails/",
                        "Configurations2/floater/", "Configurations2/menubar/", "mimetype",
                        "meta.xml", "Configurations2/accelerator/current.xml",
                        "Configurations2/popupmenu/", "styles.xml", "content.xml",
                        "Configurations2/progressbar/", "Configurations2/statusbar/"), names);
    }

    @Test
    public final void testSaveEmptyDocumentToStreamAndBuilder() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();

        PowerMock.resetAll();
        PowerMock.replayAll();
        try {
            final ZipUTF8WriterBuilderImpl builder = ZipUTF8WriterImpl.builder();
            writer.save(this.os, builder);
        } finally {
            this.os.close();
        }

        PowerMock.verifyAll();
        this.checkEmptyDocument();
    }

    @Test
    public final void testSaveEmptyDocumentToWriter() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();

        PowerMock.resetAll();
        PowerMock.replayAll();
        final ZipUTF8Writer zipWriter = ZipUTF8WriterImpl.builder().build(this.os);
        try {
            writer.save(zipWriter);
        } finally {
            this.os.close();
        }
        zipWriter.finish();
        zipWriter.flush();

        PowerMock.verifyAll();
        this.checkEmptyDocument();
    }

    @Test
    public final void testSaveEmptyDocumentToStreamAndAddPrePostamble() throws IOException {
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
        final Charset charset = StandardCharsets.US_ASCII;
        Assert.assertArrayEquals(pre, "preamble".getBytes(charset));

        // postamble
        Assert.assertArrayEquals(post, "postamble".getBytes(charset));

        // body
        final InputStream is = new ByteArrayInputStream(body);
        final ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry = zis.getNextEntry();
        final Collection<String> names = new HashSet<>();
        while (entry != null) {
            names.add(entry.getName());
            entry = zis.getNextEntry();
        }

        Assert.assertEquals(
                TestHelper.newSet("settings.xml", "Configurations2/images/Bitmaps/",
                        "Configurations2/toolbar/", "META-INF/manifest.xml", "Thumbnails/",
                        "Configurations2/floater/", "Configurations2/menubar/", "mimetype",
                        "meta.xml", "Configurations2/accelerator/current.xml",
                        "Configurations2/popupmenu/",
                        "styles.xml", "content.xml", "Configurations2/progressbar/",
                        "Configurations2/statusbar/"), names);
    }

    @Test
    public final void testSaveEmptyDocumentToWriterAndAddEntry() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final ZipUTF8Writer zw = ZipUTF8WriterImpl.builder().build(this.os);

        PowerMock.resetAll();
        PowerMock.replayAll();
        try {
            writer.save(zw);
            zw.putAndRegisterNextEntry(new StandardOdsEntry("last", null, null));
            zw.append("last content");
            zw.closeEntry();
        } finally {
            zw.finish();
            zw.close();
        }

        PowerMock.verifyAll();
        final InputStream is = new ByteArrayInputStream(this.os.toByteArray());
        final ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry = zis.getNextEntry();
        final Collection<String> names = new HashSet<>();
        while (entry != null) {
            names.add(entry.getName());
            entry = zis.getNextEntry();
        }

        Assert.assertEquals(
                TestHelper.newSet("settings.xml", "last",
                        "Configurations2/images/Bitmaps/",
                        "Configurations2/toolbar/", "META-INF/manifest.xml", "Thumbnails/",
                        "Configurations2/floater/", "Configurations2/menubar/", "mimetype",
                        "meta.xml", "Configurations2/accelerator/current.xml",
                        "Configurations2/popupmenu/", "styles.xml", "content.xml",
                        "Configurations2/progressbar/", "Configurations2/statusbar/"), names);
    }

    @Test
    public final void testSaveTwiceEmptyDocumentToStream() throws IOException {
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
        final Set<String> names = new HashSet<>();
        while (entry != null) {
            names.add(entry.getName());
            entry = zis.getNextEntry();
        }

        if (Math.abs(EMPTY_DOCUMENT_SIZE * 2 - buf.length) > 2 * DELTA) {
            System.out.printf("Expected size: %d, actual size: %d%n", EMPTY_DOCUMENT_SIZE * 2,
                    buf.length);
            Assert.fail();
        }
        // Every element appears twice
        Assert.assertEquals(
                TestHelper.newSet("Configurations2/accelerator/current.xml",
                        "Configurations2/floater/",
                        "Configurations2/images/Bitmaps/", "Configurations2/menubar/",
                        "Configurations2/popupmenu/", "Configurations2/progressbar/",
                        "Configurations2/statusbar/", "Configurations2/toolbar/",
                        "META-INF/manifest.xml", "Thumbnails/", "content.xml", "meta.xml",
                        "mimetype", "settings.xml", "styles.xml"), names);
    }

    private AnonymousOdsDocument getAnonymousDocument() {
        return AnonymousOdsDocument.create(this.logger, this.xmlUtil, this.odsElements);
    }

    @Test // (expected = IOException.class)
    public final void testSaveWriterWithException() throws IOException {
        final OutputStream outputStream = PowerMock.createMock(OutputStream.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements
                .createEmptyElements(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeMimeType(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeMeta(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeStyles(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeContent(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeSettings(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeExtras(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
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

    @Test
    public final void testSaveAsNullFile() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        Assert.assertThrows(IOException.class,
                () -> new AnonymousOdsFileWriter(this.logger, document).saveAs((File) null));

        PowerMock.verifyAll();
    }

    @Test
    public final void testSaveAsNullFileBuilder() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        Assert.assertThrows(IOException.class,
                () -> new AnonymousOdsFileWriter(this.logger, document).saveAs((File) null, this.builder));

        PowerMock.verifyAll();
    }

    @Test
    public final void testSaveWithWriter() throws IOException {
        final OutputStream outputStream = PowerMock.createMock(OutputStream.class);
        final ZipUTF8Writer z = PowerMock.createMock(ZipUTF8Writer.class);
        final File temp = File.createTempFile("temp-fastods", ".tmp");

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.builder.build(EasyMock.isA(OutputStream.class))).andReturn(z);

        this.odsElements
                .createEmptyElements(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeMimeType(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeMeta(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeStyles(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeContent(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeSettings(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeExtras(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        outputStream.write(EasyMock.isA(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
        EasyMock.expectLastCall().anyTimes();
        outputStream.flush();
        EasyMock.expectLastCall().anyTimes();
        z.finish();
        z.close();

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        new AnonymousOdsFileWriter(this.logger, document).saveAs(temp, this.builder);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSaveAsPath() throws IOException {
        final OutputStream outputStream = PowerMock.createMock(OutputStream.class);
        final Path path = PowerMock.createMock(Path.class);
        final FileSystem fs = PowerMock.createMock(FileSystem.class);
        final FileSystemProvider p = PowerMock.createMock(FileSystemProvider.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(path.getFileSystem()).andReturn(fs);
        EasyMock.expect(fs.provider()).andReturn(p);
        EasyMock.expect(p.newOutputStream(path)).andReturn(outputStream);
        outputStream.flush();
        outputStream.close();
        this.odsElements
                .createEmptyElements(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeMimeType(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeMeta(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeStyles(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeContent(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeSettings(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeExtras(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        outputStream.write(EasyMock.isA(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
        EasyMock.expectLastCall().anyTimes();
        outputStream.flush();
        EasyMock.expectLastCall().anyTimes();

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        new AnonymousOdsFileWriter(this.logger, document).saveAs(path);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSaveAsFile() throws IOException {
        final OutputStream outputStream = PowerMock.createMock(OutputStream.class);
        final File file = PowerMock.createMock(File.class);
        final Path path = PowerMock.createMock(Path.class);
        final FileSystem fs = PowerMock.createMock(FileSystem.class);
        final FileSystemProvider p = PowerMock.createMock(FileSystemProvider.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(file.toPath()).andReturn(path);
        EasyMock.expect(path.getFileSystem()).andReturn(fs);
        EasyMock.expect(fs.provider()).andReturn(p);
        EasyMock.expect(p.newOutputStream(path)).andReturn(outputStream);
        outputStream.flush();
        outputStream.close();
        this.odsElements
                .createEmptyElements(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeMimeType(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeMeta(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeStyles(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeContent(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements
                .writeSettings(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        this.odsElements.writeExtras(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
        outputStream.write(EasyMock.isA(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
        EasyMock.expectLastCall().anyTimes();
        outputStream.flush();
        EasyMock.expectLastCall().anyTimes();

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        new AnonymousOdsFileWriter(this.logger, document).saveAs(file);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSaveAsPathException() throws IOException {
        final Path path = PowerMock.createMock(Path.class);
        final FileSystem fs = PowerMock.createMock(FileSystem.class);
        final FileSystemProvider p = PowerMock.createMock(FileSystemProvider.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(path.getFileSystem()).andReturn(fs);
        EasyMock.expect(fs.provider()).andReturn(p);
        EasyMock.expect(p.newOutputStream(path)).andThrow(new NoSuchFileException("e"));

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        final AnonymousOdsFileWriter writer = new AnonymousOdsFileWriter(this.logger, document);
        Assert.assertThrows(NoSuchFileException.class, () -> {
            writer.saveAs(path);
        });

        PowerMock.verifyAll();
    }

    @Test
    public final void testSaveAsPathBuilderException() throws IOException {
        final Path path = PowerMock.createMock(Path.class);
        final FileSystem fs = PowerMock.createMock(FileSystem.class);
        final FileSystemProvider p = PowerMock.createMock(FileSystemProvider.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(path.getFileSystem()).andReturn(fs);
        EasyMock.expect(fs.provider()).andReturn(p);
        EasyMock.expect(p.newOutputStream(path)).andThrow(new NoSuchFileException("e"));

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        final AnonymousOdsFileWriter writer = new AnonymousOdsFileWriter(this.logger, document);
        Assert.assertThrows(NoSuchFileException.class, () -> {
            writer.saveAs(path, this.builder);
        });

        PowerMock.verifyAll();
    }

    @Test
    public final void testDocument() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(this.odsElements.addContentStyle(EasyMock.isA(TableStyle.class))).andReturn(true);
        EasyMock.expect(this.odsElements.addContentStyle(EasyMock.isA(TableRowStyle.class))).andReturn(true);
        EasyMock.expect(this.odsElements.addContentStyle(EasyMock.isA(TableColumnStyle.class))).andReturn(true);
        EasyMock.expect(this.odsElements.addContentStyle(EasyMock.isA(TableCellStyle.class))).andReturn(true);
        EasyMock.expect(this.odsElements.addMasterPageStyle(EasyMock.isA(MasterPageStyle.class))).andReturn(true).times(2);
        EasyMock.expect(this.odsElements.addPageLayoutStyle(EasyMock.isA(PageLayoutStyle.class))).andReturn(true).times(2);

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        final AnonymousOdsFileWriter writer = new AnonymousOdsFileWriter(this.logger, document);

        PowerMock.verifyAll();
        Assert.assertEquals(document, writer.document());
    }

    @Test
    public final void testSaveWithWriterDir() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);

        PowerMock.replayAll();
        final AnonymousOdsDocument document = this.getAnonymousDocument();
        Assert.assertThrows(IOException.class,
                () -> new AnonymousOdsFileWriter(this.logger, document).saveAs(".", this.builder));

        PowerMock.verifyAll();
    }
}