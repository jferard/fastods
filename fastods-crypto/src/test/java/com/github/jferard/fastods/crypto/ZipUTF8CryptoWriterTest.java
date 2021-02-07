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

import com.github.jferard.fastods.odselement.EncryptParameters;
import com.github.jferard.fastods.odselement.StandardOdsEntry;
import com.github.jferard.fastods.odselement.UnregisteredOdsEntry;
import com.github.jferard.fastods.odselement.UnregisteredStoredEntry;
import com.github.jferard.fastods.util.CharsetUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilderImpl;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ZipUTF8CryptoWriterTest {
    @Test
    public void testUnregistredStored() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer = ZipUTF8CryptoWriter.builder(new char[]{65, 66, 67}).build(bos);
        writer.putNextEntry(new UnregisteredStoredEntry("path", 0L, 0L));
        writer.write(new byte[]{});
        writer.closeEntry();
        writer.flush();
        writer.finish();

        final byte[] bytes = bos.toByteArray();
        Assert.assertEquals(85, bytes.length);
        this.overrideLastMod(bytes, 10);
        this.overrideLastMod(bytes, 44);
        Assert.assertArrayEquals(new byte[]{
                // Local file header
                'P', 'K', 3, 4, 10, 0, 0, 8, 0, 0, 127, 127, 127, 127, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 4, 0, 0, 0, 'p', 'a', 't', 'h',
                // Local file header
                'P', 'K', 3, 4, 20, 0, 8, 8, 8, 0, 127, 127, 127, 127, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 21, 0, 0, 0, 'M', 'E', 'T', 'A', '-', 'I', 'N', 'F', '/', 'm', 'a', 'n', 'i',
                'f', 'e', 's', 't', '.', 'x', 'm', 'l'}, bytes);
    }

    private void overrideLastMod(final byte[] bytes, final int offset) {
        for (int i = offset; i < offset + 4; i++) { // override last mod
            bytes[i] = 127;
        }
    }

    @Test
    public void testPutUnregistred()
            throws IOException, NoSuchPaddingException, InvalidKeyException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        final StandardEncrypter encrypter = PowerMock.createMock(StandardEncrypter.class);
        final byte[] data = "foo".getBytes(CharsetUtil.UTF_8);
        final char[] password = {65, 66, 67};
        final byte[] salt = new byte[16];
        final byte[] iv = new byte[16];

        PowerMock.resetAll();
        EasyMock.expect(encrypter.generateSalt()).andReturn(salt);
        EasyMock.expect(encrypter.generateIV()).andReturn(iv);
        EasyMock.expect(encrypter.compress(data)).andReturn(data);
        EasyMock.expect(encrypter.encrypt(data, salt, password, iv))
                .andReturn("bar".getBytes(CharsetUtil.UTF_8));
        EasyMock.expect(encrypter.getDataChecksum(data))
                .andReturn("baz".getBytes(CharsetUtil.UTF_8));
        EasyMock.expect(encrypter.buildParameters(3, 3, 1996459178l,
                "YmF6", "AAAAAAAAAAAAAAAAAAAAAA==", "AAAAAAAAAAAAAAAAAAAAAA==")).andReturn(null);

        PowerMock.replayAll();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer =
                new ZipUTF8CryptoWriter(new ZipUTF8WriterBuilderImpl().build(bos), encrypter,
                        password);
        writer.putNextEntry(new UnregisteredOdsEntry("path"));
        writer.write(data);
        writer.closeEntry();
        writer.flush();
        writer.finish();
        writer.close();
        final byte[] bytes = bos.toByteArray();
        this.overrideLastMod(bytes, 10);
        this.overrideLastMod(bytes, 65);
        this.overrideLastMod(bytes, 244);
        this.overrideLastMod(bytes, 294);
        PowerMock.verifyAll();

        Assert.assertArrayEquals(new byte[]{
                // Local file header
                'P', 'K', 3, 4, 20, 0, 8, 8, 8, 0, 127, 127, 127, 127, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 4, 0, 0, 0, 'p', 'a', 't', 'h',
                75, 74, 44, 2, 0,
                // Data descriptor
                'P', 'K', 7, 8, -86, -116, -1, 118, 5, 0, 0, 0, 3, 0, 0, 0,
                // Local file header
                'P', 'K', 3, 4, 20, 0, 8, 8, 8, 0, 127, 127, 127, 127, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 21, 0, 0, 0, 'M', 'E', 'T', 'A', '-', 'I', 'N', 'F', '/', 'm', 'a', 'n', 'i',
                'f', 'e', 's', 't', '.', 'x', 'm', 'l',
                85, -115, 75, 10, 2, 49, 16, 68, -81, 18, 122, -17, 119, 37, 77, -110, -39, 121,
                -126, -15, 0, 33, -45, 74, -64, 84, -53, 116, 70, 60, -66, 34, 100, -48, -35, 43,
                -22, -25, -121, 87, -67, -69, -89, -52, 86, 20, -127, 14, -37, 61, 57, 65, -42, -87,
                -32, 22, -24, 50, -98, 55, 39, 26, -94, -81, 9, -27, 42, -42, -72, -125, -5, -44,
                96, -85, 12, -76, -52, 96, 77, 86, -116, -111, -86, 24, -73, -52, -6, 16, 76, -102,
                -105, 42, 104, -4, -97, -25, -17, 81, 31, -29, -97, -1, 35, 69, -65, 91, -115, 14,
                -15, 13,
                // Data descriptor
                'P', 'K', 7, 8, 80, 0, -56, 72, 110, 0, 0, 0, -88, 0, 0, 0,
                // Central directory file header
                'P', 'K', 1, 2, 20, 0, 20, 0, 8, 8, 8, 0, 127, 127, 127, 127, -86, -116, -1, 118, 5,
                0, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'p', 'a',
                't', 'h',
                // Central directory file header
                'P', 'K', 1, 2, 20, 0, 20, 0, 8, 8, 8, 0, 127, 127, 127, 127, 80, 0, -56, 72, 110,
                0, 0, 0, -88, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                55, 0, 0, 0, 'M', 'E', 'T', 'A', '-', 'I', 'N', 'F', '/', 'm', 'a', 'n', 'i', 'f',
                'e', 's', 't', '.', 'x', 'm', 'l',
                // EOCD
                'P', 'K', 5, 6, 0, 0, 0, 0, 2, 0, 2, 0, 117, 0, 0, 0, -24, 0, 0, 0, 0, 0}, bytes);
    }

    @Test
    public void testPutAndRegister()
            throws IOException, NoSuchPaddingException, InvalidKeyException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        final StandardEncrypter encrypter = PowerMock.createMock(StandardEncrypter.class);
        final EncryptParameters parameters = PowerMock.createMock(EncryptParameters.class);
        final byte[] data = "foo".getBytes(CharsetUtil.UTF_8);
        final char[] password = {65, 66, 67};
        final byte[] salt = new byte[16];
        final byte[] iv = new byte[16];

        PowerMock.resetAll();
        EasyMock.expect(encrypter.generateSalt()).andReturn(salt);
        EasyMock.expect(encrypter.generateIV()).andReturn(iv);
        EasyMock.expect(encrypter.compress(data)).andReturn(data);
        EasyMock.expect(encrypter.encrypt(data, salt, password, iv))
                .andReturn("bar".getBytes(CharsetUtil.UTF_8));
        EasyMock.expect(encrypter.getDataChecksum(data))
                .andReturn("baz".getBytes(CharsetUtil.UTF_8));
        EasyMock.expect(encrypter.buildParameters(3, 3, 1996459178l,
                "YmF6", "AAAAAAAAAAAAAAAAAAAAAA==", "AAAAAAAAAAAAAAAAAAAAAA=="))
                .andReturn(parameters);
        EasyMock.expect(parameters.getCompressedSize()).andReturn(3L).times(2);
        EasyMock.expect(parameters.getUncompressedSize()).andReturn(3);
        EasyMock.expect(parameters.getCrc32()).andReturn(0x76ff8caaL);
        parameters.appendXMLContent(EasyMock.isA(XMLUtil.class), EasyMock.isA(Appendable.class));


        PowerMock.replayAll();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer =
                new ZipUTF8CryptoWriter(new ZipUTF8WriterBuilderImpl().build(bos), encrypter,
                        password);
        writer.putAndRegisterNextEntry(new StandardOdsEntry("path", "", ""));
        writer.write(data);
        writer.closeEntry();
        writer.flush();
        writer.finish();
        writer.close();
        final byte[] bytes = bos.toByteArray();
        this.overrideLastMod(bytes, 10);
        this.overrideLastMod(bytes, 47);
        this.overrideLastMod(bytes, 273);
        this.overrideLastMod(bytes, 323);
        Assert.assertArrayEquals(new byte[]{
                // Local file header
                'P', 'K', 3, 4, 10, 0, 0, 8, 0, 0, 127, 127, 127, 127, -86, -116, -1, 118, 3, 0, 0,
                0, 3, 0, 0, 0, 4, 0, 0, 0, 'p', 'a', 't', 'h',
                'b', 'a', 'r',
                // Local file header
                'P', 'K', 3, 4, 20, 0, 8, 8, 8, 0, 127, 127, 127, 127, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 21, 0, 0, 0, 'M', 'E', 'T', 'A', '-', 'I', 'N', 'F', '/', 'm', 'a', 'n', 'i',
                'f', 'e', 's', 't', '.', 'x', 'm', 'l', 85, -115, 75, 14, -62, 48, 12, 68, -81, 18,
                121, 31, -66, 27, 100, 53, -19, -82, 39, -128, 3, 68, -83, 11, -111, 18, -89, 106,
                82, 68, 57, 61, 1, -87, 105, -40, 88, -10, -52, 120, 94, -43, -68, -100, 21, 79,
                -102, -126, -15, -84, -32, -72, 59, -128, 32, -18, 124, 111, -8, -82, -32, 118, 109,
                -27, 5, -102, -70, 114, -102, -51, 64, 33, -30, -70, -120, -12, -58, 33, -97, 10,
                -26, -119, -47, -21, 96, 2, -78, 118, 20, 48, 118, -24, 71, -30, -34, 119, -77, 35,
                -114, -8, -97, -57, 31, 104, 45, -61, -126, 127, -126, -126, 54, 24, 75, 50, 125,
                79, -117, -56, -39, 97, -74, 86, -114, 58, 62, 20, 124, 39, 108, -114, -93, -34,
                104, 25, -105, -111, 20, 20, 114, 46, 47, -76, 96, -34, 41, 116, 78, -84, -3, 86,
                -100, 97, -91, -70, -38, -11, 7,
                // Data descriptor
                'P', 'K', 7, 8, 48, 10, -117, -128, -99, 0, 0, 0, 42, 1, 0, 0,
                // Central directory file header
                'P', 'K', 1, 2, 10, 0, 10, 0, 0, 8, 0, 0, 127, 127, 127, 127, -86, -116, -1, 118, 3,
                0, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'p', 'a',
                't', 'h',
                // Central directory file header
                'P', 'K', 1, 2, 20, 0, 20, 0, 8, 8, 8, 0, 127, 127, 127, 127, 48, 10, -117,
                -128, -99, 0, 0, 0, 42, 1, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 0,
                0, 0, 'M', 'E', 'T', 'A', '-', 'I', 'N', 'F', '/', 'm', 'a', 'n', 'i', 'f',
                'e', 's', 't', '.', 'x', 'm', 'l',
                // EOCD
                'P', 'K', 5, 6, 0, 0, 0, 0, 2, 0, 2, 0, 117, 0, 0, 0, 5, 1, 0, 0, 0, 0}, bytes);
    }

    @Test
    public void testAppend() throws IOException, NoSuchPaddingException, InvalidKeyException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        final StandardEncrypter encrypter = PowerMock.createMock(StandardEncrypter.class);
        final EncryptParameters parameters = PowerMock.createMock(EncryptParameters.class);
        final byte[] data = "foobar".getBytes(CharsetUtil.UTF_8);
        final char[] password = {65, 66, 67};
        final byte[] salt = new byte[16];
        final byte[] iv = new byte[16];

        PowerMock.resetAll();
        EasyMock.expect(encrypter.generateSalt()).andReturn(salt);
        EasyMock.expect(encrypter.generateIV()).andReturn(iv);
        EasyMock.expect(encrypter.compress(data)).andReturn(data);
        EasyMock.expect(encrypter.encrypt(data, salt, password, iv))
                .andReturn("bar".getBytes(CharsetUtil.UTF_8));
        EasyMock.expect(encrypter.getDataChecksum(data))
                .andReturn("baz".getBytes(CharsetUtil.UTF_8));
        EasyMock.expect(encrypter.buildParameters(6, 3, 1996459178l,
                "YmF6", "AAAAAAAAAAAAAAAAAAAAAA==", "AAAAAAAAAAAAAAAAAAAAAA=="))
                .andReturn(parameters);
        EasyMock.expect(parameters.getCompressedSize()).andReturn(3L).times(2);
        EasyMock.expect(parameters.getUncompressedSize()).andReturn(3);
        EasyMock.expect(parameters.getCrc32()).andReturn(0x76ff8caaL);
        parameters.appendXMLContent(EasyMock.isA(XMLUtil.class), EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer =
                new ZipUTF8CryptoWriter(new ZipUTF8WriterBuilderImpl().build(bos), encrypter,
                        password);
        writer.putAndRegisterNextEntry(new StandardOdsEntry("path", "", ""));
        writer.append("foo");
        writer.append('b');
        writer.append("bar", 1, 3);
        writer.closeEntry();
        writer.flush();
        writer.finish();
        writer.close();
        final byte[] bytes = bos.toByteArray();
        this.overrideLastMod(bytes, 10);
        this.overrideLastMod(bytes, 47);
        this.overrideLastMod(bytes, 273);
        this.overrideLastMod(bytes, 323);
        Assert.assertArrayEquals(new byte[]{
                // Local file header
                'P', 'K', 3, 4, 10, 0, 0, 8, 0, 0, 127, 127, 127, 127, -86, -116, -1, 118, 3, 0, 0, 0,
                3, 0, 0, 0, 4, 0, 0, 0, 'p', 'a', 't', 'h', 'b', 'a', 'r',
                // Local file header
                'P', 'K', 3, 4, 20, 0, 8, 8, 8, 0, 127, 127, 127, 127, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 21, 0, 0, 0, 'M', 'E', 'T', 'A', '-', 'I', 'N', 'F', '/', 'm', 'a', 'n', 'i',
                'f', 'e', 's', 't', '.', 'x', 'm', 'l', 85, -115, 75, 14, -62, 48, 12, 68, -81, 18,
                121, 31, -66, 27, 100, 53, -19, -82, 39, -128, 3, 68, -83, 11, -111, 18, -89, 106,
                82, 68, 57, 61, 1, -87, 105, -40, 88, -10, -52, 120, 94, -43, -68, -100, 21, 79,
                -102, -126, -15, -84, -32, -72, 59, -128, 32, -18, 124, 111, -8, -82, -32, 118, 109,
                -27, 5, -102, -70, 114, -102, -51, 64, 33, -30, -70, -120, -12, -58, 33, -97, 10,
                -26, -119, -47, -21, 96, 2, -78, 118, 20, 48, 118, -24, 71, -30, -34, 119, -77, 35,
                -114, -8, -97, -57, 31, 104, 45, -61, -126, 127, -126, -126, 54, 24, 75, 50, 125,
                79, -117, -56, -39, 97, -74, 86, -114, 58, 62, 20, 124, 39, 108, -114, -93, -34,
                104, 25, -105, -111, 20, 20, 114, 46, 47, -76, 96, -34, 41, 116, 78, -84, -3, 86,
                -100, 97, -91, -70, -38, -11, 7,
                // Data descriptor
                'P', 'K', 7, 8, 48, 10, -117, -128, -99, 0, 0, 0, 42, 1, 0, 0,
                // Central directory file header
                'P', 'K', 1, 2, 10, 0, 10, 0, 0, 8, 0, 0, 127, 127, 127, 127, -86, -116, -1, 118, 3, 0,
                0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'p', 'a',
                't', 'h',
                // Central directory file header
                'P', 'K', 1, 2, 20, 0, 20, 0, 8, 8, 8, 0, 127, 127, 127, 127, 48, 10, -117, -128, -99,
                0, 0, 0, 42, 1, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 0, 0, 0, 'M',
                'E', 'T', 'A', '-', 'I', 'N', 'F', '/', 'm', 'a', 'n', 'i', 'f', 'e', 's', 't', '.',
                'x', 'm', 'l',
                // EOCD
                'P', 'K', 5, 6, 0, 0, 0, 0, 2, 0, 2, 0, 117, 0, 0, 0, 5, 1, 0, 0, 0, 0}, bytes);
    }
}