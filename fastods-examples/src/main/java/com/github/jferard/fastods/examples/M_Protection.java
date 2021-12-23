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
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRowImpl;
import com.github.jferard.fastods.crypto.ProtectionFactory;
import com.github.jferard.fastods.crypto.ZipUTF8CryptoWriterBuilder;
import com.github.jferard.fastods.util.Validation;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;

public class M_Protection {
    // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
    // # Protection
    // There are basically two level of protections: protection against user clumsiness and
    // protection against user curiosity or malice. The former requires a mechanism to prevent
    // user from entering wrong data or modifying existing data, the latter requires encryption.
    //
    // FastODS provides a `fastods-crypto` module (that has a dependency to Bouncy Castle APIs)
    // to encrypt an ODS file. *This module is still in beta version.*
    //
    // For Maven users:
    //
    // ``̀`
    //     <dependency>
    //        <groupId>com.github.jferard</groupId>
    //        <artifactId>fastods-crypto</artifactId>
    //        <version>0.8.1</version>
    //     </dependency>
    // ``̀`
    //
    // << END TUTORIAL (directive to extract part of a tutorial from this file)
    /**
     * @throws IOException if the file can't be written
     */
    static void validationExample() throws IOException {
        // As usual:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("protection"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        //
        // ## Validate the content of a cell
        // *Validation **does not** require `fastods-crypto`*
        //
        // When it comes to user input, it's usually a good idea to check the validity of
        // the data.
        //
        // In LibreOffice, we are accustomed to drop down lists, but the validation of
        // OpenDocument may involve more complex formulas. Currently, FastODS implements
        // drop down lists of "hard" values, that is values that are not part of the document
        // but manually created.
        //
        // Create the table:
        final Table table = document.addTable("validation");

        // Here's our validation:
        final Validation validation =
                Validation.builder("val1").listCondition(Arrays.asList("foo", "bar", "baz"))
                        .build();

        // And how to add the validation to a cell.
        final TableCellWalker walker = table.getWalker();
        walker.setStringValue("A value");
        walker.next();
        walker.setValidation(validation);

        // That's all.

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        final File destFile = new File("generated_files", "m_protect_validation.ods");
        writer.saveAs(destFile);
        ExamplesTestHelper.validate(destFile);
    }

    public static void protectionExample() throws IOException, NoSuchAlgorithmException {
        // As usual:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("protection"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        //
        // ## Protect a table
        // *Protection **does** require `fastods-crypto` because JRE 6 doesn't provide a Base64
        // encoder.*
        //
        // LibreOffice provides the "Tools > Protect Sheet" option. This is only a protection
        // against clumsiness, because the data is still stored in plain text. The protection
        // is easy to bypass.
        //
        // *For a stronger protection, see next section.*
        //
        // Create the protected table:
        final Table table = document.addTable("validation");
        final char[] password = {'p', 'a', 's', 's', 'w', 'd'};
        table.protect(ProtectionFactory.createSha256(password));

        // And add the content as usual:
        final TableCellWalker walker = table.getWalker();
        walker.setStringValue("Try to delete me!");

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        final File destFile = new File("generated_files", "m_protect_sheet.ods");
        writer.saveAs(destFile);
        ExamplesTestHelper.validate(destFile);
    }

    public static void cryptoExample() throws IOException, NoSuchAlgorithmException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        //
        // ## Encrypt an ODS file
        // *Encryption **does** require `fastods-crypto`.*
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
        final ZipUTF8WriterBuilder builder = ZipUTF8CryptoWriterBuilder.create(password);
        //
        // Note that this method automatically voids the password to clear sensitive data.
        //
        // Once the builder is created, let's pass it to the `saveAs` method.
        writer.saveAs(new File("generated_files", "m_hello_world_crypto_example.ods"),
                builder);
       //
        // (The password in this example is "123", but you'd better choose another password.)
        //
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        final File destFile = new File("generated_files", "m_hello_world_crypto_example.ods");
        ExamplesTestHelper.validate(destFile);
    }
}
