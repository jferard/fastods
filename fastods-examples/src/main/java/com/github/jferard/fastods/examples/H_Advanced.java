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
import com.github.jferard.fastods.NamedOdsDocument;
import com.github.jferard.fastods.NamedOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.tool.ResultSetDataWrapper;
import com.github.jferard.fastods.util.SimpleLength;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Logger;

class H_Advanced {
    static void example1() throws IOException, FastOdsException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Advanced Features
        // ## A Named Writer
        // You can create a named writer to write large files. This feature is experimental, because
        // LO will never be able to open large files.
        //
        // Here's a sketch of how it works:
        // * When you create a `NamedOdsFileWriter` (instead of an anonymous one), the writer is
        // registered as an observer by the inner document.
        // * When a new table is added, remaining rows of the previous table are flushed.
        // * When a new row is created, if the buffer of rows is full, rows are flushed.
        //
        // That's why all styles have to be registered before the content is added.
        //
        // In practice, you have to give the name of the file at the writer creation:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("advanced"), Locale.US);
        final NamedOdsFileWriter writer = odsFactory
                .createWriter(new File("generated_files", "h_named_advanced.ods"));

        // Then, get the document and the tables as usual.
        final NamedOdsDocument document = writer.document();
        final Table table = document.addTable("advanced");

        // You have to register all the styles now:

        final TableCellStyle boldCellStyle = TableCellStyle.builder("cell").fontWeightBold()
                .fontSize(SimpleLength.pt(24)).build();
        document.addObjectStyle(boldCellStyle);
        //
        // And, if necessary:
        //
        //     document.addPageStyle(aPageStyle);
        //     document.addObjectStyle(aTableStyle);
        //     document.addObjectStyle(aTableRowStyle);
        //     document.addObjectStyle(aTableColumnStyle);
        //     document.addObjectStyle(aTableCellStyle);
        //     document.addObjectStyle(aTextStyle);
        //
        // An now, you can fill the Spreadsheet as usual.
        final TableRow row = table.getRow(0);
        final TableCellWalker walker = row.getWalker();
        walker.setStringValue("A huge document");
        walker.setStyle(boldCellStyle);

        // When you're finished:
        document.save();

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
    }

    static void example2() throws IOException, SQLException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("advanced"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("advanced");
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## Writing a ResultSet to the Spreadsheet
        // We need a ResultSet. Let's use SQLite:

        final SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite::memory:");
        final Connection conn = dataSource.getConnection();
        try {
            final Statement s = conn.createStatement();
            s.execute("CREATE TABLE document (file_type TEXT, extension TEXT)");
            s.execute("INSERT INTO document VALUES ('Text', '.odt'), ('Spreadsheet', '.ods'), " +
                    "('Presentation', '.odp'), ('Drawing', '.odg'), ('Chart', '.odc'), " +
                    "('Formula', '.odf'), ('Image', '.odi'), ('Master Document', '.odm')" +
                    ", ('Database', '.odb')");
            final ResultSet rs = s.executeQuery("SELECT * FROM document");

            // Now, we can write the result on a document. It will use the current row of the table:
            table.addData(new ResultSetDataWrapper(Logger.getLogger("advanced"), rs, null, -1));

            // FastODS uses the type guess to determine the type of objects.
        } finally {
            conn.close();
        }

        // ## LO features
        // If you know what you are doing, you can play with LO settings, for instance:
        table.setSettings("View1", "ZoomValue", "150");

        // For more doc, see:
        // * [Settings Service Reference](https://api.libreoffice
        // .org/docs/idl/ref/servicecom_1_1sun_1_1star_1_1document_1_1Settings.html)
        // * [ViewSettings Service Reference](https://api.libreoffice
        // .org/docs/idl/ref/servicecom_1_1sun_1_1star_1_1view_1_1ViewSettings.html)
        // * [SpreadsheetViewSettings Service Reference](https://api.libreoffice
        // .org/docs/idl/ref/servicecom_1_1sun_1_1star_1_1sheet_1_1SpreadsheetViewSettings.html)

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "h_advanced.ods"));
    }
}
