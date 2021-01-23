/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.odselement.ManifestElement;
import com.github.jferard.fastods.util.CharsetUtil;
import com.github.jferard.fastods.util.FileUtil;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
        final byte[] bytes = this.createArchiveAsBytes();
        final OdsArchiveExplorer ae =
                new OdsArchiveExplorer(FileUtil.create(), new ByteArrayInputStream(bytes));
        final Map<String, OdsArchiveExplorer.OdsFile> fileNyName = ae.explore();
        Assert.assertEquals(3, fileNyName.size());
        Assert.assertEquals(this.getOdsFile("temp1", "media/type1",
                new byte[]{0x01}), fileNyName.get("temp1"));
        Assert.assertEquals(this.getOdsFile("temp2", "media/type2",
                new byte[]{0x02}), fileNyName.get("temp2"));
        Assert.assertEquals(this.getOdsFile(ManifestElement.META_INF_MANIFEST_XML, null,
                MANIFEST.getBytes(CharsetUtil.UTF_8)),
                fileNyName.get(ManifestElement.META_INF_MANIFEST_XML));
    }

    private byte[] createArchiveAsBytes() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        final ZipOutputStream zos = new ZipOutputStream(bos);
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.setLevel(0);
        zos.putNextEntry(new ZipEntry("temp1"));
        zos.write(1);
        zos.putNextEntry(new ZipEntry(ManifestElement.META_INF_MANIFEST_XML));
        zos.write(MANIFEST.getBytes(CharsetUtil.UTF_8));
        zos.putNextEntry(new ZipEntry("temp2"));
        zos.write(2);
        zos.finish();
        zos.close();
        return bos.toByteArray();
    }

    private OdsArchiveExplorer.OdsFile getOdsFile(final String name, final String mediaType,
                                                  final byte[] bytes) {
        final OdsArchiveExplorer.OdsFile expectedTemp1 = new OdsArchiveExplorer.OdsFile(name);
        expectedTemp1.setMediaType(mediaType);
        expectedTemp1.setBytes(bytes);
        return expectedTemp1;
    }

}