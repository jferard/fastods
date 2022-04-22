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

package com.github.jferard.fastods.crypto;

import com.github.jferard.fastods.odselement.EncryptParameters;
import com.github.jferard.fastods.util.CharsetUtil;
import org.bouncycastle.util.encoders.Base64;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class StandardEncrypterTest {
    @Test
    public void test() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException {
        final StandardEncrypter encrypter = StandardEncrypter.create(EncryptParameters.builder());
        final byte[] expectedChecksum = Base64.decode("/UdU2OKZn04r0e9O047PaWNqi7LGaHYN9mURmvMCM60=");
        final byte[] expectedIV = Base64.decode("ZEk8JHG3bHu8kZw0VGOT+g==");
        final byte[] salt = Base64.decode("jGIagiBnlFdvQctdCkYfRQ==");
        final byte[] expectedHashedPassword = Base64.decode("pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=");

        char[] password = new char[]{'1', '2', '3'};

        final String plainText =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE script:module PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\" \"module.dtd\">\n<script:module xmlns:script=\"http://openoffice.org/2000/script\" script:name=\"Module1\" script:language=\"StarBasic\" script:moduleType=\"normal\">REM  *****  BASIC  *****\nREM Hello, world!\n</script:module>";
        final byte[] source = plainText.getBytes(StandardCharsets.UTF_8);
        Assert.assertEquals(332, source.length);
        final byte[] compressed = encrypter.compress(source);
        Assert.assertArrayEquals(expectedChecksum, encrypter.getDataChecksum(compressed));
        Assert.assertEquals(225, compressed.length);

        final byte[] hashedPassword = Util.getPasswordChecksum(password, "SHA-256");
        Assert.assertArrayEquals(expectedHashedPassword, hashedPassword);
        Assert.assertEquals(32, hashedPassword.length);

        // password was filled with 0's
        password = new char[]{'1', '2', '3'};
        final byte[] encrypted = encrypter.encrypt(compressed,
                Util.getPasswordChecksum(password, "SHA-256"), salt, expectedIV
        );
        Assert.assertArrayEquals(
                new byte[]{-93, -12, 97, -104, -63, -56, -24, -79, -45, -6, -80, -68, -17, 81, -121,
                        -38, 76, -40, -110, -62, 9, 127, 18, 25, 71, 68, -81, 59, 50, -99, 74, 51,
                        -21, -85, -64, 69, -105, 0, 39, 96, -49, -77, 73, 85, 118, 70, -30, 60, 53,
                        -96, -89, -87, -118, -81, -93, -51, 60, -13, 32, 95, -125, -119, -92, -100,
                        -39, -75, -90, -11, -37, 104, 10, -76, -48, 21, 62, 109, -81, -58, 22, 120,
                        41, 121, 66, -53, 86, -29, -79, -51, -63, -90, -96, 19, -111, 22, -29, -119,
                        -88, -58, -44, 105, -24, -22, -121, -23, -99, 9, -69, 3, -96, 110, -96, 41,
                        55, -123, -102, 89, -5, 71, 58, 114, 29, -123, 37, -80, -110, 55, 85, -92,
                        -21, -34, 3, -21, -34, -31, -74, -13, -7, 123, 58, 9, 44, -83, -114, -1, 30,
                        -94, 121, 99, 18, 4, -109, 103, 61, 89, 108, -24, -86, -82, 55, 126, 102,
                        -49, -103, 84, 99, -91, -22, 49, 120, 68, -79, 84, -66, 90, -81, 63, 13,
                        -65, -75, -50, -104, -56, 122, 68, 97, -44, 118, 105, 59, 1, 111, 39, -85,
                        95, -94, -80, -104, 50, 82, 12, -100, 8, 12, 106, 12, 84, -32, -125, -36,
                        -48, -83, 58, 15, 15, 117, 111, -26, 13, -37, -37, 80, -92, 43, -45},
                Arrays.copyOfRange(encrypted, 0, 224));
    }

    @Test
    public void testIVandSalt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        final SecureRandom sr = PowerMock.createMock(SecureRandom.class);
        final StandardEncrypter encrypter = new StandardEncrypter(
                sr, Cipher.getInstance("AES/CBC/ISO10126Padding"),
                100000, 32, 32, EncryptParameters.builder()
        );

        PowerMock.resetAll();
        sr.nextBytes(EasyMock.isA(byte[].class));
        EasyMock.expectLastCall().times(2);

        PowerMock.replayAll();
        final byte[] ivBytes = encrypter.generateIV();
        Assert.assertArrayEquals(new byte[16], ivBytes);
        final byte[] saltBytes = encrypter.generateSalt();
        Assert.assertArrayEquals(new byte[16], saltBytes);

        PowerMock.verifyAll();
    }
}