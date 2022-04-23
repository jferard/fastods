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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class EncryptParametersTest {
    @Test
    public void testStd() throws IOException {
        final EncryptParameters parameters =
                EncryptParameters.builder().build(10, 0, 0L, "CS", "SALT", "IV");
        TestHelper.assertXMLEquals(
                "<manifest:encryption-data manifest:checksum-type=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0#sha256-1k\" manifest:checksum=\"CS\">" +
                        "<manifest:algorithm manifest:algorithm-name=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" manifest:initialisation-vector=\"IV\"/>" +
                        "<manifest:start-key-generation manifest:start-key-generation-name=\"http://www.w3.org/2000/09/xmldsig#sha256\" manifest:key-size=\"32\"/>" +
                        "<manifest:key-derivation manifest:key-derivation-name=\"PBKDF2\" manifest:key-size=\"32\" manifest:iteration-count=\"100000\" manifest:salt=\"SALT\"/>" +
                        "</manifest:encryption-data>",
                parameters);
        Assert.assertEquals(10, parameters.getPlainDataSize());
    }

    @Test
    public void testBuilder() throws IOException {
        final EncryptParameters parameters = EncryptParameters.builder()
                .compressedCheckSumType("ccst")
                .startKeyGenerationName("skgn")
                .startKeySize(10)
                .keyDerivationName("kdn")
                .derivedKeySize(20)
                .derivationIterationCount(72)
                .algorithmName("an")
                .build(10, 0, 0L, "CS", "SALT", "IV");
        TestHelper.assertXMLEquals(
                "<manifest:encryption-data manifest:checksum-type=\"ccst\" " +
                        "manifest:checksum=\"CS\"><manifest:algorithm " +
                        "manifest:algorithm-name=\"an\" manifest:initialisation-vector=\"IV\"/>" +
                        "<manifest:start-key-generation manifest:start-key-generation-name=\"skgn\" " +
                        "manifest:key-size=\"10\"/><manifest:key-derivation " +
                        "manifest:key-derivation-name=\"kdn\" manifest:key-size=\"20\" " +
                        "manifest:iteration-count=\"72\" manifest:salt=\"SALT\"/>" +
                        "</manifest:encryption-data>", parameters);
        Assert.assertEquals(10, parameters.getPlainDataSize());
    }
}