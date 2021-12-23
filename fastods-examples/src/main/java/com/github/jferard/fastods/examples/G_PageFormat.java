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
import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.PageSection;
import com.github.jferard.fastods.PageSectionContent;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.Text;
import com.github.jferard.fastods.TextBuilder;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.PaperFormat;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.style.TextStyle;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Section 7 of the tutorial
 *
 * @author J. Férard
 */
class G_PageFormat {
    /**
     * @throws IOException if the file can't be written
     */
    static void example1() throws IOException {
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        {
            // As usual:
            final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("format-page"), Locale.US);
            final AnonymousOdsFileWriter writer = odsFactory.createWriter();
            final OdsDocument document = writer.document();
            // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
            // # Page Format
            // We know how to access to cells and how to format those cells. We still have to format
            // the pages.
            //
            // Let's start with a new table:

            final Table table = document.addTable("format-page");
            final TableCellWalker walker = table.getWalker();
            walker.setStringValue("Text");

            // ## Page size and margins
            // It's easy to set the page size to a standard format:
            final PageStyle pageStyle =
                    PageStyle.builder("page-style").paperFormat(PaperFormat.A3).build();

            // And add the page style into the table style:
            final TableStyle tableStyle =
                    TableStyle.builder("table-style").pageStyle(pageStyle).build();

            // And set this table style:
            table.setStyle(tableStyle);

            // << END TUTORIAL (directive to extract part of a tutorial from this file)
            // And save the file.
            final File destFile = new File("generated_files", "g_page_format_A3.ods");
            writer.saveAs(destFile);
            ExamplesTestHelper.validate(destFile);
        }

        // Next example:
        {
            final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("format-page"), Locale.US);
            final AnonymousOdsFileWriter writer = odsFactory.createWriter();
            final OdsDocument document = writer.document();
            final Table table = document.addTable("format-page");
            final TableCellWalker walker = table.getWalker();
            walker.setStringValue("Text");

            // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
            // You can use a custom format:
            final PageStyle pageStyle =
                    PageStyle.builder("page-style").pageWidth(SimpleLength.cm(10)).pageHeight(
                            SimpleLength.cm(50)).build();

            // << END TUTORIAL (directive to extract part of a tutorial from this file)
            final TableStyle tableStyle =
                    TableStyle.builder("table-style").pageStyle(pageStyle).build();
            table.setStyle(tableStyle);
            final File destFile = new File("generated_files", "g_page_format_custom.ods");
            writer.saveAs(destFile);
            ExamplesTestHelper.validate(destFile);
        }

        // Next example:
        {
            final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("format-page"), Locale.US);
            final AnonymousOdsFileWriter writer = odsFactory.createWriter();
            final OdsDocument document = writer.document();
            final Table table = document.addTable("format-page");
            final TableCellWalker walker = table.getWalker();
            walker.setStringValue("Text");

            // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
            // Or change the margins:
            final PageStyle pageStyle =
                    PageStyle.builder("page-style").allMargins(SimpleLength.cm(4)).build();

            // You can combine margins and page size customization. Please check the
            // `PageStyleBuilder` class for other options.
            //
            // << END TUTORIAL (directive to extract part of a tutorial from this file)
            final TableStyle tableStyle =
                    TableStyle.builder("table-style").pageStyle(pageStyle).build();
            table.setStyle(tableStyle);
            final File destFile = new File("generated_files", "g_page_format_margins.ods");
            writer.saveAs(destFile);
            ExamplesTestHelper.validate(destFile);

        }
    }

    static void example2() throws IOException {
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // As usual:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("format-page"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        //
        // Now, something a little bit harder: we will add a footer and a header.
        //
        // ## Header
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        final Table table = document.addTable("format-page");
        final TableCellWalker walker = table.getWalker();
        walker.setStringValue("Text");
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)

        // First, we build the three parts of a simple header:
        final Text leftHeaderContent = Text.content("left header");
        final Text centerHeaderContent =
                Text.builder().par().span("center header, page ").span(Text.TEXT_PAGE_NUMBER)
                        .build();
        final Text rightHeaderContent = Text.content("right header");

        // Then we build the header itself:
        final Header header = PageSection.regionBuilder().region(PageSectionContent.Region.LEFT)
                .text(leftHeaderContent).region(PageSectionContent.Region.CENTER)
                .text(centerHeaderContent).region(PageSectionContent.Region.RIGHT)
                .text(rightHeaderContent).allMargins(SimpleLength.cm(2))
                .minHeight(SimpleLength.cm(5)).buildHeader();

        // ## Footer
        // For the footer, let's use the one part format:
        final Footer footer =
                PageSection.simpleBuilder().text(Text.content("footer")).buildFooter();

        // We now insert the header and the footer in a page style:
        final PageStyle pageStyle =
                PageStyle.builder("page-style").header(header).footer(footer).build();

        // And add the page style into the table style:
        final TableStyle tableStyle =
                TableStyle.builder("table-style").pageStyle(pageStyle).build();

        // And set this table style:
        table.setStyle(tableStyle);
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        final File destFile = new File("generated_files", "g_page_format_header_footer.ods");
        writer.saveAs(destFile);
        ExamplesTestHelper.validate(destFile);
    }

    static void example3() throws IOException {
        // As usual
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("format-page"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## Styles
        // Create another table:
        final Table table = document.addTable("format-page2");
        final TableCellWalker walker = table.getWalker();
        walker.setStringValue("Text");

        // We can create a very simple header:
        final Header minimalHeader =
                PageSection.simpleBuilder().content("minimal header").buildHeader();

        // Or a complex footer:
        final TextBuilder textBuilder = Text.builder();

        // `par()` means a new paragraph, `span` a new portion of text:
        textBuilder.par().span("complex");

        // Both can be used in one call:
        textBuilder.parContent("footer");

        // Text can be styled:
        textBuilder.par().styledSpan("date is:",
                TextStyle.builder("footer1").fontWeightBold().build());

        // In one call:
        textBuilder.parStyledContent(Text.TEXT_DATE,
                TextStyle.builder("footer2").fontSize(SimpleLength.pt(25)).fontWeightBold()
                        .build());

        // And build the text:
        final Text footerContent = textBuilder.build();
        final Footer complexFooter =
                PageSection.simpleBuilder().text(footerContent).buildFooter();

        // As above:
        final PageStyle pageStyle =
                PageStyle.builder("page-style2").header(minimalHeader).footer(complexFooter)
                        .build();
        final TableStyle tableStyle =
                TableStyle.builder("table-style2").pageStyle(pageStyle).build();
        table.setStyle(tableStyle);
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        final File destFile =
                new File("generated_files", "g_page_format_styled_header_footer.ods");
        writer.saveAs(destFile);
        ExamplesTestHelper.validate(destFile);
    }

    static void example4() throws IOException {
        // As usual
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("format-page"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## Printing
        // This topic is slightly different from page formatting, but usually we format a page
        // in order to print it. Let's see how to define print ranges and repeat rows or columns.
        //
        // First, create a large table and fill it with data:
        final Table table = document.addTable("print me");
        final TableCellWalker walker = table.getWalker();
        for (int i=0; i<8; i++) {
            walker.setStringValue(String.format("Column %s", i));
            walker.next();
        }
        walker.nextRow();
        for (int j=0; j<1000; j++) {
            for (int i = 0; i < 8; i++) {
                walker.setStringValue(String.format("Value %s.%s", j, i));
                walker.next();
            }
            walker.nextRow();
        }

        // We set the first row as a header row.
        table.setHeaderRowsCount(1);

        // Now, we can set the print range. We can leave the 100 last rows out this range, if we
        // want (900 is enough !).
        table.addPrintRange(0, 0, 900, 7);

        final PageStyle pageStyle =
                PageStyle.builder("page-style").scaleToX(1).build();
        // And add the page style into the table style:
        final TableStyle tableStyle =
                TableStyle.builder("table-style").pageStyle(pageStyle).build();

        // And set this table style:
        table.setStyle(tableStyle);

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        final File destFile =
                new File("generated_files", "g_print.ods");
        writer.saveAs(destFile);
        ExamplesTestHelper.validate(destFile);
    }
}
