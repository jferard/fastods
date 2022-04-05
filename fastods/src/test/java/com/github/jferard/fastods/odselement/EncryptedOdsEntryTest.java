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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.zip.ZipEntry;

public class EncryptedOdsEntryTest {
    @Test
    public void testXML() throws IOException {
        final EncryptedOdsEntry entry = new EncryptedOdsEntry("path", "text/xml", "1.0",
                EncryptParameters.builder().build(1, 2, 3L, "CS", "salt", "vector"));
        TestHelper.assertXMLEquals("<manifest:file-entry manifest:full-path=\"path\" " +
                "manifest:media-type=\"text/xml\" manifest:version=\"1.0\" manifest:size=\"1\">" +
                "<manifest:encryption-data manifest:checksum-type=" +
                "\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0#sha256-1k\" " +
                "manifest:checksum=\"CS\">" +
                "<manifest:algorithm " +
                "manifest:algorithm-name=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" " +
                "manifest:initialisation-vector=\"vector\"/>" +
                "<manifest:start-key-generation " +
                "manifest:start-key-generation-name=\"http://www.w3.org/2000/09/xmldsig#sha256\" " +
                "manifest:key-size=\"32\"/>" +
                "<manifest:key-derivation manifest:key-derivation-name=\"PBKDF2\" " +
                "manifest:key-size=\"32\" manifest:iteration-count=\"100000\" " +
                "manifest:salt=\"salt\"/>" +
                "</manifest:encryption-data>" +
                "</manifest:file-entry>", entry);
    }

    @Test
    public void test() throws IOException {
        final EncryptParameters parameters =
                EncryptParameters.builder().build(1, 2, 3L, "CS", "salt", "vector");
        final EncryptedOdsEntry entry = new EncryptedOdsEntry("path", "text/xml", "1.0",
                parameters);
        Assert.assertTrue(entry.neverEncrypt());
        Assert.assertThrows(IllegalArgumentException.class,
                () -> entry.encryptParameters(parameters));
    }

    @Test
    public void testAsZipEntry() throws IOException {
        final EncryptParameters parameters =
                EncryptParameters.builder().build(1, 2, 3L, "CS", "salt", "vector");
        final EncryptedOdsEntry entry = new EncryptedOdsEntry("path", "text/xml", "1.0",
                parameters);
        final ZipEntry ze = entry.asZipEntry();
        Assert.assertEquals("path", ze.getName());
        Assert.assertEquals(2, ze.getCompressedSize());
        Assert.assertEquals(3L, ze.getCrc());
        Assert.assertNull(ze.getComment());
        Assert.assertNull(ze.getExtra());
        Assert.assertEquals(ZipEntry.STORED, ze.getMethod());
        Assert.assertEquals(2, ze.getSize());
    }

}