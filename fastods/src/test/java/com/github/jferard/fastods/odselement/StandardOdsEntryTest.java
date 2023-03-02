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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.zip.ZipEntry;

public class StandardOdsEntryTest {
    @Test
    public void testEncrypt() {
        final StandardOdsEntry entry = new StandardOdsEntry("dir/foo", null, null);
        Assert.assertEquals("OdsEntry[path=dir/foo]", entry.toString());
        Assert.assertFalse(entry.neverEncrypt());
    }

    @Test
    public void testNeverEncrypt() {
        final StandardOdsEntry entry = new StandardOdsEntry("dir/", null, null);
        Assert.assertEquals("OdsEntry[path=dir/]", entry.toString());
        Assert.assertTrue(entry.neverEncrypt());
    }

    @Test
    public void testAppendXML() throws IOException {
        final StandardOdsEntry entry = new StandardOdsEntry("dir/foo.xml", "text/xml", "1.0");
        TestHelper.assertXMLEquals("<manifest:file-entry manifest:full-path=\"dir/foo.xml\" " +
                "manifest:media-type=\"text/xml\" manifest:version=\"1.0\"/>", entry);
    }

    @Test
    public void testEncryptParameters() throws IOException {
        final StandardOdsEntry entry = new StandardOdsEntry("dir/foo.xml", "text/xml", "1.0");
        final EncryptParameters parameters = EncryptParameters.builder().build(10, 3, 0L, "CS", "SALT", "IV");
        final OdsEntry entry1 = entry.encryptParameters(parameters);
        TestHelper.assertXMLEquals("<manifest:file-entry manifest:full-path=\"dir/foo.xml\" " +
                "manifest:media-type=\"text/xml\" manifest:version=\"1.0\" manifest:size=\"10\">" +
                "<manifest:encryption-data " +
                "manifest:checksum-type=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0#sha256-1k\" " +
                "manifest:checksum=\"CS\"><manifest:algorithm " +
                "manifest:algorithm-name=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" " +
                "manifest:initialisation-vector=\"IV\"/>" +
                "<manifest:start-key-generation " +
                "manifest:start-key-generation-name=\"http://www.w3.org/2000/09/xmldsig#sha256\" " +
                "manifest:key-size=\"32\"/><manifest:key-derivation " +
                "manifest:key-derivation-name=\"PBKDF2\" manifest:key-size=\"32\" " +
                "manifest:iteration-count=\"100000\" manifest:salt=\"SALT\"/>" +
                "</manifest:encryption-data></manifest:file-entry>", entry1);
    }

    @Test
    public void testAsZip() throws IOException {
        final StandardOdsEntry entry = new StandardOdsEntry("dir/foo.xml", "text/xml", "1.0");
        final ZipEntry ze = entry.asZipEntry();
        Assert.assertEquals("dir/foo.xml", ze.getName());
        Assert.assertEquals(-1, ze.getCompressedSize());
        Assert.assertEquals(-1, ze.getCrc());
        Assert.assertNull(ze.getComment());
        Assert.assertNull(ze.getExtra());
        Assert.assertEquals(-1, ze.getMethod());
        Assert.assertEquals(-1, ze.getSize());
    }
}