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

package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.odselement.ManifestElement;
import com.github.jferard.fastods.util.FileUtil;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class OdsArchiveExplorerTest {
    private static final String MANIFEST = XMLUtil.XML_PROLOG + "\n" +
            "<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\" manifest:version=\"1.2\" xmlns:loext=\"urn:org:documentfoundation:names:experimental:office:xmlns:loext:1.0\">\n" +
            "    <manifest:file-entry manifest:full-path=\"temp1\" manifest:version=\"1.2\" manifest:media-type=\"media/type1\"/>\n" +
            "    <manifest:file-entry manifest:full-path=\"temp2\" manifest:version=\"1.2\" manifest:media-type=\"media/type2\"/>\n" +
            "</manifest:manifest>";

    @Test
    public void test() throws IOException {
        final byte[] bytes = this.createArchiveAsBytes(MANIFEST);
        final OdsArchiveExplorer ae =
                new OdsArchiveExplorer(FileUtil.create(), new ByteArrayInputStream(bytes));
        final Map<String, OdsArchiveExplorer.OdsFile> fileNyName = ae.explore();
        Assert.assertEquals(3, fileNyName.size());
        Assert.assertEquals(this.getOdsFile("temp1", "media/type1",
                new byte[]{0x01}), fileNyName.get("temp1"));
        Assert.assertEquals(this.getOdsFile("temp2", "media/type2",
                new byte[]{0x02}), fileNyName.get("temp2"));
        Assert.assertEquals(this.getOdsFile(ManifestElement.META_INF_MANIFEST_XML, null,
                        MANIFEST.getBytes(StandardCharsets.UTF_8)),
                fileNyName.get(ManifestElement.META_INF_MANIFEST_XML));
    }

    private byte[] createArchiveAsBytes(final String manifest) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        final ZipOutputStream zos = new ZipOutputStream(bos);
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.setLevel(0);
        zos.putNextEntry(new ZipEntry("temp1"));
        zos.write(1);
        zos.putNextEntry(new ZipEntry(ManifestElement.META_INF_MANIFEST_XML));
        zos.write(manifest.getBytes(StandardCharsets.UTF_8));
        zos.putNextEntry(new ZipEntry("temp2"));
        zos.write(2);
        zos.finish();
        zos.close();
        return bos.toByteArray();
    }

    @Test
    public void testBadManifest() throws IOException {
        final byte[] bytes = this.createArchiveAsBytes("foo");
        final OdsArchiveExplorer ae =
                new OdsArchiveExplorer(FileUtil.create(), new ByteArrayInputStream(bytes));
        final Map<String, OdsArchiveExplorer.OdsFile> fileNyName = ae.explore();
        Assert.assertEquals(3, fileNyName.size());
        Assert.assertEquals(this.getOdsFile("temp1", null,
                new byte[]{0x01}), fileNyName.get("temp1"));
        Assert.assertEquals(this.getOdsFile("temp2", null,
                new byte[]{0x02}), fileNyName.get("temp2"));
        Assert.assertEquals(this.getOdsFile(ManifestElement.META_INF_MANIFEST_XML, null,
                        "foo".getBytes(StandardCharsets.UTF_8)),
                fileNyName.get(ManifestElement.META_INF_MANIFEST_XML));
    }

    @Test
    public void testAddOdsFileToDocument() {
        final OdsDocument document = PowerMock.createMock(OdsDocument.class);
        final OdsArchiveExplorer.OdsFile f = new OdsArchiveExplorer.OdsFile("foo");
        final byte[] bytes = "bar".getBytes(StandardCharsets.UTF_8);
        f.setBytes(bytes);
        f.setMediaType("mediatype");

        PowerMock.resetAll();
        document.addExtraFile("pfx/foo", "mediatype", bytes);

        PowerMock.replayAll();
        f.addToDocument(document, "pfx/");

        PowerMock.verifyAll();
    }

    @Test
    public void testAddEmptyOdsFileToDocument() {
        final OdsDocument document = PowerMock.createMock(OdsDocument.class);
        final OdsArchiveExplorer.OdsFile f = new OdsArchiveExplorer.OdsFile("foo");
        f.setMediaType("mediatype");

        PowerMock.resetAll();
        document.addExtraObjectReference("pfx/foo", "mediatype", null);

        PowerMock.replayAll();
        f.addToDocument(document, "pfx/");

        PowerMock.verifyAll();
    }

    @Test
    public void testEqualsOdsFile() {
        final byte[] bytes = "bar".getBytes(StandardCharsets.UTF_8);
        final OdsArchiveExplorer.OdsFile f1 = new OdsArchiveExplorer.OdsFile("foo");
        f1.setBytes(bytes);
        f1.setMediaType("mediatype");
        final OdsArchiveExplorer.OdsFile f2 = new OdsArchiveExplorer.OdsFile("foo");
        f2.setBytes(bytes);
        f2.setMediaType("mediatype");
        final OdsArchiveExplorer.OdsFile f3 = new OdsArchiveExplorer.OdsFile("baz");
        f3.setBytes(bytes);
        f3.setMediaType("mediatype");
        final OdsArchiveExplorer.OdsFile f4 = new OdsArchiveExplorer.OdsFile("foo");
        f4.setBytes(bytes);
        f4.setMediaType("mediatype2");
        final OdsArchiveExplorer.OdsFile f5 = new OdsArchiveExplorer.OdsFile("foo");
        f5.setBytes(null);
        f5.setMediaType("mediatype");

        Assert.assertEquals(f1, f1);
        Assert.assertNotEquals(f1, new Object());
        Assert.assertEquals(f1, f2);
        Assert.assertEquals(f2, f1);
        Assert.assertNotEquals(f1, f3);
        Assert.assertNotEquals(f3, f1);
        Assert.assertNotEquals(f1, f4);
        Assert.assertNotEquals(f4, f1);
        Assert.assertNotEquals(f1, f5);
        Assert.assertNotEquals(f5, f1);
    }

    @Test
    public void testHashOdsFile() {
        final byte[] bytes = "bar".getBytes(StandardCharsets.UTF_8);
        final OdsArchiveExplorer.OdsFile f1 = new OdsArchiveExplorer.OdsFile("foo");
        f1.setBytes(bytes);
        f1.setMediaType("mediatype");
        Assert.assertEquals(2144566489, f1.hashCode());
    }

    private OdsArchiveExplorer.OdsFile getOdsFile(final String name, final String mediaType,
                                                  final byte[] bytes) {
        final OdsArchiveExplorer.OdsFile expectedTemp1 = new OdsArchiveExplorer.OdsFile(name);
        expectedTemp1.setMediaType(mediaType);
        expectedTemp1.setBytes(bytes);
        return expectedTemp1;
    }

}