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
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;
import com.github.jferard.fastods.Text;
import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Logger;

class F_MoreOnCells {
    static void example() throws IOException, URISyntaxException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("cells"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # More on Cells
        // We know how to access a cell, set a value, a data style (format) and a style.
        // But there is more on cells: first, we sometimes need to merge cells; second, some cells
        // contains a formatted text.
        final Table table = document.addTable("more");

        // We add a header:
        TableRow row = table.nextRow();
        TableCellWalker walker = row.getWalker();

        // ## Merging Cells
        // Cells can be merged easily:
        walker.setStringValue("A1 (merged cells)");
        walker.setCellMerge(2, 3);

        // Here, the cells A2, A3, B1, B2 and B3 are covered. You can assign value to those cells,
        // but the values are not visible:
        walker.next();
        walker.setStringValue("A2 (covered)");
        walker.next();
        walker.setStringValue("A3 (covered)");
        walker.next();
        walker.setStringValue("A4 (not covered)");
        row = table.nextRow();
        walker = row.getWalker();
        walker.setStringValue("B1 (covered)");
        walker.next();
        walker.setStringValue("B2 (covered)");
        walker.next();
        walker.setStringValue("B3 (covered)");
        walker.next();
        walker.setStringValue("B4 (not covered)");
        row = table.nextRow();
        walker = row.getWalker();
        walker.setStringValue("C1 (not covered)");
        walker.next();
        walker.setStringValue("C2 (not covered)");
        walker.next();
        walker.setStringValue("C3 (not covered)");
        walker.next();
        walker.setStringValue("B4 (not covered)");

        // If you open the document in LO, you'll see something like this:
        //
        // ```
        //    ------------------------------------------------------------------------------
        //    | A1 (merged cells)                                       | A4 (not covered) |
        //    |                                                         |-------------------
        //    |                                                         | B4 (not covered) |
        //    ------------------------------------------------------------------------------
        //    | C1 (not covered)  | C2 (not covered) | C3 (not covered) | B4 (not covered) |
        //    ------------------------------------------------------------------------------
        // ```
        //
        // If you split the A1 cell:
        //
        // ```
        //    ------------------------------------------------------------------------------
        //    | A1 (merged cells) | A2 (covered)     | A3 (covered)     | A4 (not covered) |
        //    ------------------------------------------------------------------------------
        //    | B1 (covered)      | B2 (covered)     | B3 (covered)     | B4 (not covered) |
        //    ------------------------------------------------------------------------------
        //    | C1 (not covered)  | C2 (not covered) | C3 (not covered) | B4 (not covered) |
        //    ------------------------------------------------------------------------------
        // ```
        //
        // It's possible to merge only one cells one one row or one column with `walker
        // .setRowsSpanned(m)`
        // or `walker.setColumnsSpanned(n)` (see below).
        //
        // You can also merge cells from the row with: `row.setCellMerge(cell_index, m, n)`, `row
        // .setRowsSpanned(cell_index, m)` or
        // `row.setColumnsSpanned(cell_index, n)`.
        //
        // ## Formatted text in a cell
        // I listed the cell types in a previous section. But the String cell type is not limited
        // to a plain String. It can contain formatted text. Let's learn how it works.
        //
        // ### Multiline text
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // We skip a few rows:
        for (int i = 0; i < 3; i++) {
            row = table.nextRow();
        }
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)

        // We need some room:
        walker = row.getWalker();
        walker.setRowsSpanned(3);

        // Let's start with something simple. First, we build a text:
        Text text = Text.builder().parContent("This is a").parContent("multiline")
                .parContent("cell").build();

        // Second, we set the text:
        walker.setText(text);

        // We can use some styles:
        final TextStyle boldStyle = TextProperties.builder().fontWeightBold()
                .buildHiddenStyle("bold");
        text = Text.builder().par().span("This is a ").styledSpan("bold", boldStyle)
                .span(" example").build();
        walker.to(2);
        walker.setColumnsSpanned(2);
        walker.setText(text);

        // ### Links
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // We skip a few rows:
        for (int i = 0; i < 5; i++) {
            row = table.nextRow();
        }
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // Links can be absolute or relative. For instance, an absolute Link may be an absolute URL:
        walker = row.getWalker();
        walker.setText(Text.builder().par().span("Hello, ")
                .link("FastODS", new URL("https://www.github.com/jferard/fastods")).span("!")
                .build());

        // A relative link:
        walker.to(2);
        walker.setText(Text.builder().par().span("Check ")
                .link("Hello World example", new URI("../a_hello_world_example.ods")).build());

        // Or a link to a table:
        final Table table2 = document.addTable("target");
        walker.to(4);
        walker.setText(Text.builder().par().span("A link to ")
                .link("target table", table2).build());

        // ### Tooltips
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // We skip a few rows:
        for (int i = 0; i < 5; i++) {
            row = table.nextRow();
        }
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // Tooltips are LO dependent:
        walker = row.getWalker();
        walker.setStringValue("A Cell with a tooltip");
        walker.setTooltip("The Tooltip");
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "f_more.ods"));
    }
}
