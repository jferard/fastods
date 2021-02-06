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

package com.github.jferard.fastods.crypto;

import com.github.jferard.fastods.odselement.EncryptParametersBuilder;
import com.github.jferard.fastods.odselement.StandardOdsEntry;
import com.github.jferard.fastods.odselement.UnregisteredOdsEntry;
import com.github.jferard.fastods.odselement.UnregisteredStoredEntry;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilderImpl;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZipUTF8CryptoWriterTest {
    @Test
    public void testUnregistredStored() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer =
                new ZipUTF8CryptoWriterBuilder(new ZipUTF8WriterBuilderImpl().level(0),
                        new EncryptParametersBuilder(), new char[]{65, 66, 67}).build(bos);
        writer.putNextEntry(new UnregisteredStoredEntry("path", 0L, 0L));
        writer.write(new byte[]{});
        writer.closeEntry();
        writer.flush();
        writer.finish();

        final byte[] bytes = bos.toByteArray();
        Assert.assertEquals(85, bytes.length);
        bytes[10] = 127;
        bytes[11] = 127;
        bytes[44] = 127;
        bytes[45] = 127;
        Assert.assertArrayEquals(
                new byte[]{'P', 'K', 3, 4, 10, 0, 0, 8, 0, 0, 127, 127, 'F', 'R', 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 'p', 'a', 't', 'h', 'P', 'K', 3, 4, 20, 0, 8,
                        8, 8, 0, 127, 127, 'F', 'R', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0,
                        0, 'M', 'E', 'T', 'A', '-', 'I', 'N', 'F', '/', 'm', 'a', 'n', 'i', 'f',
                        'e', 's', 't', '.', 'x', 'm', 'l'}, bytes);
    }

    @Test
    public void testPutUnregistred() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer = ZipUTF8CryptoWriter.builder(new char[]{65, 66, 67}).build(bos);
        writer.putNextEntry(new UnregisteredOdsEntry("path"));
        writer.write(new byte[]{});
        writer.closeEntry();
        writer.flush();
        writer.finish();
        writer.close();
    }

    @Test
    public void testPutAndRegister() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer = ZipUTF8CryptoWriter.builder(new char[]{65, 66, 67}).build(bos);
        writer.putAndRegisterNextEntry(new StandardOdsEntry("path", "", ""));
        writer.write(new byte[]{});
        writer.closeEntry();
        writer.flush();
        writer.finish();
        writer.close();
    }

    @Test
    public void testAppend() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer = ZipUTF8CryptoWriter.builder(new char[]{65, 66, 67}).build(bos);
        writer.putAndRegisterNextEntry(new StandardOdsEntry("path", "", ""));
        writer.append("foo");
        writer.append('b');
        writer.append("bar", 1, 3);
        writer.closeEntry();
        writer.flush();
        writer.finish();
        writer.close();
    }
}