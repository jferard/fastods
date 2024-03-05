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

package com.github.jferard.fastods.crypto;

import com.github.jferard.fastods.odselement.EncryptParameters;
import com.github.jferard.fastods.odselement.EncryptParametersBuilder;
import com.github.jferard.fastods.odselement.StandardOdsEntry;
import com.github.jferard.fastods.odselement.UnregisteredOdsEntry;
import com.github.jferard.fastods.odselement.UnregisteredStoredEntry;
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
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ZipUTF8CryptoWriterTest {
    @Test
    public void testUnregistredStored() throws IOException, NoSuchAlgorithmException {
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
        final byte[] data = "foo".getBytes(StandardCharsets.UTF_8);
        final char[] password = {65, 66, 67};
        final byte[] hashedPassword = Util.getPasswordChecksum(password, "SHA-256");
        final byte[] salt = new byte[16];
        final byte[] iv = new byte[16];

        PowerMock.resetAll();
        EasyMock.expect(encrypter.generateSalt()).andReturn(salt);
        EasyMock.expect(encrypter.generateIV()).andReturn(iv);
        EasyMock.expect(encrypter.compress(data)).andReturn(data);
        EasyMock.expect(encrypter.encrypt(data, hashedPassword, salt, iv
                ))
                .andReturn("bar".getBytes(StandardCharsets.UTF_8));
        EasyMock.expect(encrypter.getDataChecksum(data))
                .andReturn("baz".getBytes(StandardCharsets.UTF_8));
        EasyMock.expect(encrypter.buildParameters(3, 3, 1996459178l,
                "YmF6", "AAAAAAAAAAAAAAAAAAAAAA==", "AAAAAAAAAAAAAAAAAAAAAA==")).andReturn(null);

        PowerMock.replayAll();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer =
                new ZipUTF8CryptoWriter(new ZipUTF8WriterBuilderImpl().build(bos), encrypter,
                        hashedPassword);
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
        final byte[] data = "foobar".getBytes(StandardCharsets.UTF_8);
        final byte[] cdata = "foo".getBytes(StandardCharsets.UTF_8);
        final byte[] cedata = "bar".getBytes(StandardCharsets.UTF_8);
        final char[] password = {65, 66, 67};
        final byte[] hashedPassword = Util.getPasswordChecksum(password, "SHA-256");
        final byte[] salt = new byte[16];
        final byte[] iv = new byte[16];

        PowerMock.resetAll();
        EasyMock.expect(encrypter.generateSalt()).andReturn(salt);
        EasyMock.expect(encrypter.generateIV()).andReturn(iv);
        EasyMock.expect(encrypter.compress(data)).andReturn(cdata);
        EasyMock.expect(encrypter.encrypt(cdata, hashedPassword, salt, iv
        )).andReturn(cedata);
        EasyMock.expect(encrypter.getDataChecksum(cdata))
                .andReturn("baz".getBytes(StandardCharsets.UTF_8));
        EasyMock.expect(encrypter.buildParameters(6, 3, 1996459178l,
                        "YmF6", "AAAAAAAAAAAAAAAAAAAAAA==", "AAAAAAAAAAAAAAAAAAAAAA=="))
                .andReturn(parameters);
        EasyMock.expect(parameters.getCompressedThenEncryptedDataSize()).andReturn(3L).times(2);
        EasyMock.expect(parameters.getPlainDataSize()).andReturn(5).times(2);
        EasyMock.expect(parameters.getCrc32()).andReturn(0x76ff8caaL);
        parameters.appendXMLContent(EasyMock.isA(XMLUtil.class), EasyMock.isA(Appendable.class));


        PowerMock.replayAll();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer =
                new ZipUTF8CryptoWriter(new ZipUTF8WriterBuilderImpl().build(bos), encrypter,
                        hashedPassword);
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
                'f', 'e', 's', 't', '.', 'x', 'm', 'l', 85, -115, 93, 10, -62, 48, 16, -124, -81,
                18, -10, 61, -2, -127, 32, 75, -45, -66, -11, 4, 122, -128, -48, 110, 53, -112, 108,
                74, -109, -118, -11, -12, 70, -95, 105, 124, 89, 118, 103, 102, -25, -85, -102,
                -105, -77, -30, 73, 83, 48, -98, 21, 28, 119, 7, 16, -60, -99, -17, 13, -33, 21,
                -36, -82, -83, -68, 64, 83, 87, 78, -77, 25, 40, 68, 92, 23, -111, -34, 56, -28, 83,
                -63, 60, 49, 122, 29, 76, 64, -42, -114, 2, -58, 14, -3, 72, -36, -5, 110, 118, -60,
                17, -1, -13, -8, 3, -83, 101, 88, -16, 79, 80, -48, 6, 99, 73, -90, -17, 105, 17,
                57, 59, -52, -42, -54, 81, -57, -121, -126, -17, -124, -51, 113, -44, 27, 45, -29,
                50, -110, -126, 66, -50, -27, -123, 22, -52, 59, -123, -50, -119, -75, -33, -118,
                51, -84, 84, 87, -69, -2, 0,
                // Data descriptor
                'P', 'K', 7, 8, 26, -9, 29, -1, -98, 0, 0, 0, 42, 1, 0, 0,
                // Central directory file header
                'P', 'K', 1, 2, 10, 0, 10, 0, 0, 8, 0, 127, 127, 127, 127, 88, -86, -116, -1, 118,
                3, 0, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'p',
                'a', 't', 'h',
                // Central directory file header
                'P', 'K', 1, 2, 20, 0, 20, 0, 8, 8, 8, 127, 127, 127, 127, 88, 26, -9, 29, -1, -98,
                0, 0, 0, 42, 1, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 0, 0, 0, 'M',
                'E', 'T', 'A', '-', 'I', 'N', 'F', '/', 'm', 'a', 'n', 'i', 'f',
                'e', 's', 't', '.', 'x', 'm', 'l',
                // EOCD
                'P', 'K', 5, 6, 0, 0, 0, 0, 2, 0, 2, 0, 117, 0, 0, 0, 6, 1, 0, 0, 0, 0}, bytes);
    }

    @Test
    public void testAppend() throws IOException, NoSuchPaddingException, InvalidKeyException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        final StandardEncrypter encrypter = PowerMock.createMock(StandardEncrypter.class);
        final EncryptParameters parameters = PowerMock.createMock(EncryptParameters.class);
        final byte[] data = "foobar".getBytes(StandardCharsets.UTF_8);
        final byte[] cdata = "foo".getBytes(StandardCharsets.UTF_8);
        final byte[] cedata = "bar".getBytes(StandardCharsets.UTF_8);
        final char[] password = {65, 66, 67};
        final byte[] hashedPassword = Util.getPasswordChecksum(password, "SHA-256");
        final byte[] salt = new byte[16];
        final byte[] iv = new byte[16];

        PowerMock.resetAll();
        EasyMock.expect(encrypter.generateSalt()).andReturn(salt);
        EasyMock.expect(encrypter.generateIV()).andReturn(iv);
        EasyMock.expect(encrypter.compress(data)).andReturn(cdata);
        EasyMock.expect(encrypter.encrypt(cdata, hashedPassword, salt, iv
        )).andReturn(cedata);
        EasyMock.expect(encrypter.getDataChecksum(cdata))
                .andReturn("baz".getBytes(StandardCharsets.UTF_8));
        EasyMock.expect(encrypter.buildParameters(6, 3, 1996459178l,
                        "YmF6", "AAAAAAAAAAAAAAAAAAAAAA==", "AAAAAAAAAAAAAAAAAAAAAA=="))
                .andReturn(parameters);
        EasyMock.expect(parameters.getCompressedThenEncryptedDataSize()).andReturn(3L).times(2);
        EasyMock.expect(parameters.getPlainDataSize()).andReturn(6).times(2);
        EasyMock.expect(parameters.getCrc32()).andReturn(0x76ff8caaL);
        parameters.appendXMLContent(EasyMock.isA(XMLUtil.class), EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer =
                new ZipUTF8CryptoWriter(new ZipUTF8WriterBuilderImpl().build(bos), encrypter,
                        hashedPassword);
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
                'P', 'K', 3, 4, 10, 0, 0, 8, 0, 0, 127, 127, 127, 127, -86, -116, -1, 118, 3, 0, 0,
                0,
                3, 0, 0, 0, 4, 0, 0, 0, 'p', 'a', 't', 'h', 'b', 'a', 'r',
                // Local file header
                'P', 'K', 3, 4, 20, 0, 8, 8, 8, 0, 127, 127, 127, 127, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0,
                0, 21, 0, 0, 0, 'M', 'E', 'T', 'A', '-', 'I', 'N', 'F', '/', 'm', 'a', 'n', 'i',
                'f', 'e', 's', 't', '.', 'x', 'm', 'l', 85, -115, 75, 14, -62, 48, 12, 68, -81, 18,
                121, 31, 126, 11, -124, -84, -90, -35, -11, 4, 112, -128, -88, 117, 33, 82, -30, 84,
                77, -118, 40, -89, 39, 32, 53, 13, 27, -53, -98, 25, -49, -85, -102, -105, -77, -30,
                73, 83, 48, -98, 21, 28, 119, 7, 16, -60, -99, -17, 13, -33, 21, -36, -82, -83, -68,
                64, 83, 87, 78, -77, 25, 40, 68, 92, 23, -111, -34, 56, -28, 83, -63, 60, 49, 122,
                29, 76, 64, -42, -114, 2, -58, 14, -3, 72, -36, -5, 110, 118, -60, 17, -1, -13, -8,
                3, -83, 101, 88, -16, 79, 80, -48, 6, 99, 73, -90, -17, 105, 17, 57, 59, -52, -42,
                -54, 81, -57, -121, -126, -17, -124, -51, 113, -44, 27, 45, -29, 50, -110, -126, 66,
                -50, -27, -123, 22, -52, 59, -123, -50, -119, -75, -33, -118, 51, -84, 84, 87, -69,
                -2, 0,
                // Data descriptor
                'P', 'K', 7, 8, -113, -119, -42, -64, -98, 0, 0, 0, 42, 1, 0, 0,
                // Central directory file header
                'P', 'K', 1, 2, 10, 0, 10, 0, 0, 8, 0, 127, 127, 127, 127, 88, -86, -116, -1, 118,
                3, 0, 0, 0, 3, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'p',
                'a', 't', 'h',
                // Central directory file header
                'P', 'K', 1, 2, 20, 0, 20, 0, 8, 8, 8, 127, 127, 127, 127, 88, -113, -119, -42, -64,
                -98, 0, 0, 0, 42, 1, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 0, 0, 0,
                'M', 'E', 'T', 'A', '-', 'I', 'N', 'F', '/', 'm', 'a', 'n', 'i', 'f', 'e', 's', 't',
                '.', 'x', 'm', 'l',
                // EOCD
                'P', 'K', 5, 6, 0, 0, 0, 0, 2, 0, 2, 0, 117, 0, 0, 0, 6, 1, 0, 0, 0, 0}, bytes);
    }

    @Test
    public void testBuilder()
            throws NoSuchAlgorithmException {
        final ZipUTF8WriterBuilderImpl writerBuilder =
                PowerMock.createMock(ZipUTF8WriterBuilderImpl.class);
        final EncryptParametersBuilder parametersBuilder =
                PowerMock.createMock(EncryptParametersBuilder.class);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        PowerMock.resetAll();
        EasyMock.expect(writerBuilder.build(bos)).andReturn(null);

        PowerMock.replayAll();
        final ZipUTF8CryptoWriterBuilder builder =
                new ZipUTF8CryptoWriterBuilder(writerBuilder, parametersBuilder,
                        "passwd".toCharArray());
        builder.build(bos);

        PowerMock.verifyAll();
    }

    @Test
    public void testExc()
            throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException,
            InvalidKeyException {
        final StandardEncrypter encrypter = PowerMock.createMock(StandardEncrypter.class);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer = PowerMock.createMock(ZipUTF8Writer.class);
        final byte[] salt = "foo".getBytes(StandardCharsets.UTF_8);
        final byte[] iv = "bar".getBytes(StandardCharsets.UTF_8);
        final byte[] data = "baz".getBytes(StandardCharsets.UTF_8);
        final byte[] password = "passwd".getBytes(StandardCharsets.UTF_8);

        PowerMock.resetAll();
        EasyMock.expect(encrypter.generateSalt()).andReturn(salt);
        EasyMock.expect(encrypter.generateIV()).andReturn(iv);
        EasyMock.expect(encrypter.compress(new byte[0])).andReturn(data);
        EasyMock.expect(encrypter.encrypt(data, password, salt, iv))
                .andThrow(new NoSuchAlgorithmException());

        PowerMock.replayAll();
        final ZipUTF8CryptoWriter cryptoWriter =
                new ZipUTF8CryptoWriter(writer, encrypter, password);
        cryptoWriter.putNextEntry(new UnregisteredOdsEntry("path"));
        Assert.assertThrows(IOException.class, () -> cryptoWriter.closeEntry());

        PowerMock.verifyAll();
    }

    @Test
    public void testSetComment() {
        final StandardEncrypter encrypter = PowerMock.createMock(StandardEncrypter.class);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipUTF8Writer writer = PowerMock.createMock(ZipUTF8Writer.class);
        final byte[] password = "passwd".getBytes(StandardCharsets.UTF_8);
        final StandardOdsEntry entry = new StandardOdsEntry("path", "mt", "v");

        PowerMock.resetAll();
        writer.setComment("Comment");
        writer.registerEntry(entry);

        PowerMock.replayAll();
        final ZipUTF8CryptoWriter cryptoWriter =
                new ZipUTF8CryptoWriter(writer, encrypter, password);
        cryptoWriter.setComment("Comment");
        cryptoWriter.registerEntry(entry);

        PowerMock.verifyAll();
    }}