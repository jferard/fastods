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

import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class UtilTest {
    @Test
    public void testPassword() throws NoSuchAlgorithmException {
        final byte[] passwordChecksum = Util.getPasswordChecksum("foo".toCharArray(), "SHA-256");
        Assert.assertArrayEquals(
                new byte[]{44, 38, -76, 107, 104, -1, -58, -113, -7, -101, 69, 60, 29, 48, 65, 52,
                        19, 66, 45, 112, 100, -125, -65, -96, -7, -118, 94, -120, 98, 102, -25,
                        -82}, passwordChecksum);
        Assert.assertEquals("2C26B46B68FFC68FF99B453C1D30413413422D706483BFA0F98A5E886266E7AE",
                javax.xml.bind.DatatypeConverter.printHexBinary(
                        passwordChecksum));
        Assert.assertEquals("LCa0a2j/xo/5m0U8HTBBNBNCLXBkg7+g+YpeiGJm564=",
                Util.toBase64String(
                        passwordChecksum));
    }
}