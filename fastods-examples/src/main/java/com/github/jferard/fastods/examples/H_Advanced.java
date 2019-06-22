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
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

class H_Advanced {
    static void example() throws IOException, FastOdsException {
        // As usual:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("advanced"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Advanced Features

        // Let's start with a new page:
        final Table table = document.addTable("advanced");
        TableRow row = table.getRow(0);
        TableCellWalker walker = row.getWalker();

        // ## Links
        // Links can be absolute or relative. For instance, an absolute Link may be an URL:
        document.addTable("links");
        row = table.getRow(0);
        walker = row.getWalker();
        walker.setStringValue("");

        // ## Some Tools

        // ## A Named Writer

        // ## Tooltips

        // ## LO features
        // If you know what you are doing, you can play with LO settings, for instance:
        table.setSettings("View1", "ZoomValue", "150");

        // For more doc, see:
        // * [Settings Service Reference](https://api.libreoffice.org/docs/idl/ref/servicecom_1_1sun_1_1star_1_1document_1_1Settings.html)
        // * [ViewSettings Service Reference](https://api.libreoffice.org/docs/idl/ref/servicecom_1_1sun_1_1star_1_1view_1_1ViewSettings.html)
        // * [SpreadsheetViewSettings Service Reference](https://api.libreoffice.org/docs/idl/ref/servicecom_1_1sun_1_1star_1_1sheet_1_1SpreadsheetViewSettings.html)

        // And save the file.
        writer.saveAs(new File("generated_files", "h_advanced.ods"));

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
    }
}
