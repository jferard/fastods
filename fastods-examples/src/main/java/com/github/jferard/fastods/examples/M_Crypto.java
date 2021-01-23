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

package com.github.jferard.fastods.examples;

import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TableRowImpl;
import com.github.jferard.fastods.crypto.ZipUTF8CryptoWriterBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;

public class M_Crypto {
    public static void cryptoExample() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Encrypt an ODS file
        //
        // In LibreOffice, you can protect a file with a password. This is the "Save with password"
        // option in the *Save As* dialog box. Note that this has nothing to do with the "Tools >
        // Protect Spreadsheet/Protect Sheet" options. The "Save with password" option really
        // encrypts the data, while the "Protect Spreadsheet/Protect Sheet" options are easy to
        // bypass.
        //
        // The file encryption is regarded as robust: the key derivation function is PBKDF2 using
        // HMAC-SHA-1 and 100,000 iterations; the encryption algorithm is AES256 (Cipher block
        // chaining mode). To make a long story short: if you use a strong password and loose it,
        //
        // FastODS provides a `fastods-crypto` module (that has a dependency to Bouncy Castle APIs)
        // to encrypt an ODS file. *This module is still in beta version.*
        //
        // First, we create a simple document, similar to the "Hello, world!" example:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("hello-world"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("hello-world-crypto");
        final TableRowImpl row = table.getRow(0);
        final TableCell cell = row.getOrCreateCell(0);
        cell.setStringValue("Hello, world!");
        //
        // Second, we save it with encryption. This is easy: the module provides a class
        // `ZipUTF8CryptoWriterBuilder` that implements `ZipUTF8WriterBuilder` and encrypts the
        // data:
        //
        final char[] password = {'1', '2', '3'};
        writer.saveAs(new File("generated_files", "m_hello_world_crypto_example.ods"),
                new ZipUTF8CryptoWriterBuilder(password));
        Arrays.fill(password, '\0'); // clear sensitive data.
        //
        // (The password in this example is "123", but you'd better choose another password.)
        //
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
    }

}
