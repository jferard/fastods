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
import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.PageSection;
import com.github.jferard.fastods.PageSectionContent;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;
import com.github.jferard.fastods.Text;
import com.github.jferard.fastods.TextBuilder;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.util.SimpleLength;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

class D_PageFormat {
    static void example() throws IOException, FastOdsException {
        // As usual:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("format-page"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Page Format
        // We know how to access to cells and how to format those cells. We still have to format
        // the pages.

        // Let's start with a new table:
        Table table = document.addTable("format-page");
        TableRow row = table.getRow(0);
        TableCellWalker walker = row.getWalker();
        walker.setStringValue("Text");

        // We will add a footer and a header.
        // ## Header
        // First, we build the three parts of a simple header:
        final Text leftHeaderContent = Text.content("left header");
        final Text centerHeaderContent = Text.builder().par().span("center header, page ")
                .span(Text.TEXT_PAGE_NUMBER).build();
        final Text rightHeaderContent = Text.content("right header");

        // Then we build the header itself:
        final Header header = PageSection.regionBuilder().region(PageSectionContent.Region.LEFT)
                .text(leftHeaderContent).region(PageSectionContent.Region.CENTER)
                .text(centerHeaderContent).region(PageSectionContent.Region.RIGHT)
                .text(rightHeaderContent).allMargins(SimpleLength.cm(2))
                .minHeight(SimpleLength.cm(5)).buildHeader();

        // ## Footer
        // For the footer, let's use the one part format:
        final Footer footer = PageSection.simpleBuilder().text(Text.content("footer"))
                .buildFooter();

        // We now insert the header and the footer in a page style:
        PageStyle pageStyle = PageStyle.builder("page-style").header(header).footer(footer)
                .build();

        // And add the page style into the table style:
        TableStyle tableStyle = TableStyle.builder("table-style").pageStyle(pageStyle)
                .build();

        // And set this table style:
        table.setStyle(tableStyle);

        // ## Styles
        // Create another table:
        table = document.addTable("format-page2");
        row = table.getRow(0);
        walker = row.getWalker();
        walker.setStringValue("Text");

        // We can create a very simple header:
        final Header minimalHeader = PageSection.simpleBuilder().content("minimal header").buildHeader();

        // Or a complex footer:
        final TextBuilder textBuilder = Text.builder();

        // `par()` means a new paragraph, `span` a new portion of text:
        textBuilder.par().span("complex");

        // Both can be used in one call:
        textBuilder.parContent("footer");

        // Text can be styled:
        textBuilder.par().styledSpan("date is:",
                TextProperties.builder().fontWeightBold().buildHiddenStyle("footer1"));

        // In one call:
        textBuilder.parStyledContent(Text.TEXT_DATE,
                TextProperties.builder().fontSize(SimpleLength.pt(25)).fontWeightBold().buildHiddenStyle("footer2")
        );

        // And build the text:
        final Text footerContent = textBuilder.build();
        final Footer complexFooter = PageSection.simpleBuilder().text(footerContent).buildFooter();

        // As above:
        pageStyle = PageStyle.builder("page-style2").header(minimalHeader).footer(complexFooter).build();
        tableStyle = TableStyle.builder("table-style2").pageStyle(pageStyle)
                .build();
        table.setStyle(tableStyle);

        // ## LO features
        // If you know what you are doing, you can play with LO settings, for instance:
        table.setSettings("View1", "ZoomValue", "150");

        // For more doc, see:
        // * [Settings Service Reference](https://api.libreoffice.org/docs/idl/ref/servicecom_1_1sun_1_1star_1_1document_1_1Settings.html)
        // * [ViewSettings Service Reference](https://api.libreoffice.org/docs/idl/ref/servicecom_1_1sun_1_1star_1_1view_1_1ViewSettings.html)
        // * [SpreadsheetViewSettings Service Reference](https://api.libreoffice.org/docs/idl/ref/servicecom_1_1sun_1_1star_1_1sheet_1_1SpreadsheetViewSettings.html)

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "d_page_format1.ods"));
    }
}
