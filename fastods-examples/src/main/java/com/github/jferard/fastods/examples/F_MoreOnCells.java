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
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.Text;
import com.github.jferard.fastods.style.TextStyle;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Section 6 of the tutorial
 *
 * @author J. Férard
 */
class F_MoreOnCells {
    /**
     * @throws IOException        if the file can't be written
     * @throws URISyntaxException if the uri is not valid
     */
    static void example1() throws IOException, URISyntaxException {
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
        final TableCellWalker walker = table.getWalker();

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

        walker.nextRow();
        walker.setStringValue("B1 (covered)");
        walker.next();
        walker.setStringValue("B2 (covered)");
        walker.next();
        walker.setStringValue("B3 (covered)");
        walker.next();
        walker.setStringValue("B4 (not covered)");

        walker.nextRow();
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
            walker.nextRow();
        }
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)

        // We need some room:
        walker.setRowsSpanned(3);

        // Let's start with something simple. First, we build a text:
        Text text =
                Text.builder().parContent("This is a").parContent("multiline").parContent("cell")
                        .build();

        // Second, we set the text:
        walker.setText(text);

        // We can use some styles:
        final TextStyle boldStyle = TextStyle.builder("bold").fontWeightBold().build();
        text = Text.builder().par().span("This is a ").styledSpan("bold", boldStyle)
                .span(" example").build();
        walker.to(2);
        walker.setColumnsSpanned(2);
        walker.setText(text);

        // ### Links
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // We skip a few rows:
        for (int i = 0; i < 5; i++) {
            walker.nextRow();
        }
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // Links can be absolute or relative. For instance, an absolute Link may be an absolute URL:
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
        walker.setText(
                Text.builder().par().span("A link to ").link("target table", table2).build());

        // ### Tooltips
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // We skip a few rows:
        for (int i = 0; i < 5; i++) {
            walker.nextRow();
        }
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // Tooltips are LO dependent:
        walker.setStringValue("A Cell with a tooltip");
        walker.setTooltip("The Tooltip");

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        final File destFile = new File("generated_files", "f_more.ods");
        writer.saveAs(destFile);
        ExamplesTestHelper.validate(destFile);
    }

    /**
     * @throws IOException        if the file can't be written
     * @throws URISyntaxException if the uri is not valid
     */
    static void example2() throws IOException, URISyntaxException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("cells"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("formulas");
        final TableCellWalker walker = table.getWalker();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## Formulas
        // ### Warning
        // First, FastODS won't parse formulas, check syntax or semantic, evaluate results or
        // anything like that. FastODS will write your formula in the document
        // and that's all, even if your formula is "I'm the King of the world!". That may be
        // frustrating, but FastODS is not an OpenDocument consumer, just a producer.
        //
        // Second, it's important to understand that LibreOffice, as OpenOffice and Excel before,
        // have made the choice to *translate* the formula language in various human languages.
        // That's a stupid yet perfectly understandable idea: *anyone should be able to create
        // Excel formulas, but it's sooo hard to understand and remember a hundred basic
        // english words*.
        //
        // Let's think about that: imagine that Sun decided to translate the Java keywords
        // in every language, and then to translate the libraries in every language.
        // I'm not talking about internationalization of localization, but about *translation*!
        // Programming would be harder, a lot harder...
        //
        // But, as you may know, no matter what interface language you have selected, formulas are
        // always stored in a document in the [Recalculated Formula (OpenFormula)
        // Format](https://docs.oasis-open.org/office/v1.2/os/OpenDocument-v1.2-os-part2.html),
        // which is basically the syntax of english written LibreOffice formulas. In
        // French, we write `SOMME.SI` but the internal name of the function is `SUMIF` whereas
        // `SOMME.SI` is the display name. And the formula attribute of the cell will
        // contain `SUMIF`, not `SOMME.SI`.
        //
        // As stated above, FastODS does not care about the content of the formula. FastODS won't
        // complain if you write formulas in french, dutch or chinese, but LibreOffice will! If you
        // write a formula in a language that is not english, the LibreOffice engine won't
        // understand your formula and will return an error.
        //
        /// To summarize: **you are responsible for writing your formulas in english and to write
        // them correctly.**
        //
        // ### Some basic examples
        // Let's start!
        //
        // We have to remember the address of the current cell. It's easy here: A1.
        walker.setStringValue("1");
        walker.next();
        walker.setFormula("IF(A1=1;1;0)");
        walker.next();
        walker.setFormula("IF(A1=\"1\";1;0)");
        //
        // Formula are typed, hence you have the value 0 in B1 and 1 in C1.
        //
        // Now, something more interesting with a matrix formula:
        walker.nextRow();
        walker.setFloatValue(1);
        walker.nextRow();
        walker.setFloatValue(2);
        walker.nextRow();
        walker.setFloatValue(3);
        walker.nextRow();
        walker.setFloatValue(4);
        walker.nextRow();
        walker.setFloatValue(5);
        walker.nextRow();
        walker.setFloatValue(6);
        walker.nextRow();
        walker.setFloatValue(7);
        walker.nextRow();
        walker.setFloatValue(8);
        walker.nextRow();
        walker.setMatrixFormula("SUM((MOD(A2:A9;2)=0)*(A2:A9))");
        //
        // The formula sums the cell A2:A9 with an even value.
        //
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        final File destFile = new File("generated_files", "f_formulas.ods");
        writer.saveAs(destFile);
        ExamplesTestHelper.validate(destFile);
    }
}
