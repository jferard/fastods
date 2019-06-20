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
import com.github.jferard.fastods.CellValue;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;
import com.github.jferard.fastods.tool.TableHelper;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Logger;

class B_AccessingTablesRowsAndCells {
    public static void example() throws IOException, FastOdsException {
        //
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Accessing Tables, Rows and Cells
        //
        // Now, we want to write values in other cells that the A1 cell.
        //
        // We start with the (now) usual boilerplate code to get a document:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("accessing"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // ## Direct Access to Cells
        //
        // You know how to create a table:
        final Table table1 = document.addTable("direct-access");

        // Get the first row and the first cell of the first row:
        TableRow row = table1.getRow(0);
        TableCell cell = row.getOrCreateCell(0);

        // And set a value.
        cell.setStringValue("A1");

        // *Note that the access is a write only access. You can't read the content of a cell.*

        // You can have direct access to any cell, but it has a cost. If the row doesn't exist
        // yet in the list of rows, it  will create all rows between the last row and the actual
        // row.
        row = table1.getRow(6);

        // You can have direct access to cells
        cell = row.getOrCreateCell(1);
        cell.setStringValue("B7");

        // With a TableHelper, direct access might be easier to write...
        final TableHelper tableHelper = TableHelper.create();
        // ...but note that the cell is referred by row *then* column (as matrices in maths).
        // To access the cell "F2", you'll use:
        cell = tableHelper.getCell(table1, 1, 5);
        cell.setStringValue("F2");

        // You can use an address, but there is the cost of parsing that address and a risk of
        // malformed address:
        try {
            cell = tableHelper.getCell(table1, "E3");
            cell.setStringValue("E3");
        } catch (final ParseException e) {
            /* this won't happen here! */
        }

        // To be (almost) complete, there is another way to write a value to a cell:
        try {
            tableHelper.setCellValue(table1, "D4", CellValue.fromObject("D4"));
        } catch (final ParseException e) {
            /* this won't happen here! */
        }

        // ## Relative Access
        //
        // Direct access may be useful, but FastODS was designed for a relative access
        // Create a new table:
        final Table table2 = document.addTable("relative-access");

        // We want ten rows of data
        for (int r = 0; r < 10; r++) {
            // The Table object has an internal row index (that is updated by the `getRow` method).
            // Just call `nextRow` to make the index advance by one (you have to call `nextRow`
            // before you write data, to get the first row):
            final TableRow tableRow = table2.nextRow();

            // And then create a "walker" for this row:
            final TableCellWalker cellWalker = tableRow.getWalker();

            // Now, we want nine columns for each row:
            for (int c = 0; c < 9; c++) {

                // Add the value to each cell
                cellWalker.setStringValue(String.valueOf((char) (c + 'A')) + String.valueOf(r + 1));

                // And then push one cell right.
                cellWalker.next();
            }
        }
        // And save the file.
        writer.saveAs(new File("generated_files", "b_accessing_example.ods"));
        //
        // *Note 1:* There is a slight inconsistency between `table.newtRow` (before using
        // the row) and `cellWalker.next` (after using the cell). Maybe I'll fix it before
        // version 1.0...
        //
        // *Note 2:* We will see how to merge cells in the Advanced part of this tutorial.
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
    }
}
