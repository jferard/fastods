/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
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

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * The section 1 of the tutorial
 *
 * @author J. Férard
 */
class A_HelloWorld {
    /**
     * @throws IOException if the file can't be written
     */
    static void example() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Hello, world!
        //
        // Let's start with the famous "Hello, World!" example.
        //
        // As stated in the javadoc, "An OdsFactory is the entry point for creating ODS documents."
        // Every time you want to create an ODS document, you'll start with something like that:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("hello-world"), Locale.US);

        // Now, you can create an ODS writer. You have the choice: either you give the filename
        // now, or you keep it for the end. Let's create an anonymous writer: the content will live
        // in memory until we save it to a file:
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();

        // The writer owns a document that we need to create a spreadsheet:
        final OdsDocument document = writer.document();

        // Okay, let's go. We create a new table (a new sheet indeed) named "hello-world":
        final Table table = document.addTable("hello-world");

        // We get the first row:
        final TableRowImpl row = table.getRow(0);

        // And the first cell of the first row:
        final TableCell cell = row.getOrCreateCell(0);

        // Note that we could have chained the calls:
        // `TableCell cell = document.addTable("hello-world").getRow(0).getOrCreateCell(0)`
        //
        // Finally, we put the famous sentence in this cell A1
        cell.setStringValue("Hello, world!");

        // And save the file.
        writer.saveAs(new File("generated_files", "a_hello_world_example.ods"));

        // With a `mvn clean verify` at the root of the project, you can check the result in the
        // `fastods-examples/generated-files` directory.
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
    }
}
