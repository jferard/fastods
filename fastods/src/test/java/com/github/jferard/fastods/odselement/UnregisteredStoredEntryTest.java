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

public class UnregisteredStoredEntryTest {
    @Test
    public void test() {
        final UnregisteredStoredEntry entry = new UnregisteredStoredEntry("path", 10, 50L);
        Assert.assertEquals(entry, entry.encryptParameters(
                EncryptParameters.builder().build(1, 2, 3L, "CS", "salt", "vector")));
        Assert.assertTrue(entry.neverEncrypt());
    }

    @Test
    public void testAppendXML() throws IOException {
        final UnregisteredStoredEntry entry = new UnregisteredStoredEntry("path", 10, 50L);
        TestHelper.assertXMLEquals("", entry);
    }

    @Test
    public void testZipEntry() throws IOException {
        final UnregisteredStoredEntry entry = new UnregisteredStoredEntry("path", 10, 50L);
        final ZipEntry ze = entry.asZipEntry();
        Assert.assertEquals("path", ze.getName());
        Assert.assertEquals(10, ze.getCompressedSize());
        Assert.assertEquals(50L, ze.getCrc());
        Assert.assertNull(ze.getComment());
        Assert.assertNull(ze.getExtra());
        Assert.assertEquals(ZipEntry.STORED, ze.getMethod());
        Assert.assertEquals(10, ze.getSize());
    }
}