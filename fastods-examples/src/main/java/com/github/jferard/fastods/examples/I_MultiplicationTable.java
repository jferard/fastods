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
import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.PageSection;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.Text;
import com.github.jferard.fastods.style.LOFonts;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

class I_MultiplicationTable {
    static void example() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        //
        // # A multiplication table
        // Let's create a new document and a new table:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("multiplication"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("multiplication-table");

        // ## The spreadsheet
        // First, we need to set a default font that is monospaced and a text that is centered,
        // to ensure a
        // nice alignment of the operations:
        final TableCellStyle tableCellStyle = TableCellStyle.builder("c2")
                .fontName(LOFonts.LIBERATION_MONO).textAlign(TableCellStyle.Align.CENTER)
                .fontSize(SimpleLength.pt(10)).build();

        // All columns will have the same format:
        final TableColumnStyle tableColumnStyle = TableColumnStyle.builder("co2")
                .defaultCellStyle(tableCellStyle).columnWidth(SimpleLength.cm(3.5)).build();
        for (int c = 0; c < 6; c++) {
            table.setColumnStyle(c, tableColumnStyle);
        }

        // Now, we need a little maths (this is not surprising for a multiplication table).
        // We want to display 12 x 12 operations `i x j = k`:
        final int MAX = 12;
        // Operations are grouped by `j`, e.g. `1 x 1, 2 x 1, 3 x 1, ...` on the first column,
        // `1 x 2, 2 x 2, 3 x 2, ...` on the second column, etc. and displayed in two stacked
        // blocks:
        final int BLOCK_COUNT = 2;
        // Thus, we need 6 columns:
        final int COLS = MAX / BLOCK_COUNT;

        // We use two imbricated loops:
        for (int i = 1; i <= MAX; i++) {
            for (int j = 1; j <= MAX; j++) {

                // The value of `j` is used to find the column. Since operations are in two
                // stacked blocks
                // (1-6 and 7-12), the column is `j-1 % 6`, that is:
                // `1->0, 2->1, ..., 6->5, 7->0, 8->1, ..., 12->5`.

                final int c = (j - 1) % COLS;

                // The row is: `i-1` if this is the first block, or `13 + i-1` (13 for 12
                // operations + a
                // blank line). The block is `(j-1) / 6`:

                final int r = ((j - 1) / COLS) * (MAX + 1) + (i - 1);

                // We use `String.fromat` and set a width (2 for operands, 3 for the result
                table.getRow(r).getOrCreateCell(c)
                        .setStringValue(String.format("%2d \u00D7 %2d = %3d", i, j, i * j));
            }
        }

        // ## Footer and header
        // A multiplication table should be printable. We'll add a header and a footer.
        //
        // For the header, we need a style for the title and another for a discreet dedication
        // (note the use of `buildHiddenStyle`: common styles in footer/header are ignored by LO):
        final TextStyle titleStyle = TextProperties.builder().fontWeightBold()
                .fontSize(SimpleLength.pt(24)).buildHiddenStyle("title");
        final TextStyle dedicationStyle = TextProperties.builder().fontSize(SimpleLength.pt(8))
                .fontStyleItalic().buildHiddenStyle("dedication");

        // Now, we create the text of the header:
        final Text headerText = Text.builder().parStyledContent("Multiplication Table", titleStyle)
                .parStyledContent("For Léon", dedicationStyle).build();
        // And the header itself:
        final Header header = PageSection.simpleBuilder().text(headerText)
                .minHeight(SimpleLength.cm(2)).buildHeader();

        // The footer is simple, but we need to escape the content because of the `<` and `>`:
        final String footerText = XMLUtil.create().escapeXMLContent(
                "Copyright (C) 2019 J. Férard <https://github.com/jferard> " +
                        "Creative Commons BY-SA / created with FastODS " +
                        "(https://github.com/jferard/fastods)");
        final Footer footer = PageSection.simpleBuilder().styledContent(footerText, dedicationStyle)
                .buildFooter();

        // Let's gather the footer and the header in a page style. We center the table and set a
        // zoom:
        final PageStyle pageStyle = PageStyle.builder("page").header(header).footer(footer)
                .printOrientationHorizontal().scaleTo(125).centering(PageStyle.Centering.BOTH)
                .build();

        // We set set the style of the current table.
        final TableStyle tableStyle = TableStyle.builder("table").pageStyle(pageStyle).build();
        table.setStyle(tableStyle);

        // And save the file.
        writer.saveAs(new File("generated_files", "i_multiplication_table.ods"));

        // As you see, it's possible to create a nice document in roughly 40 lines of code (I don't
        // count the imports)
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
    }
}
