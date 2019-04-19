/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.it;

import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;
import com.github.jferard.fastods.style.LOFonts;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.testlib.Util;
import com.github.jferard.fastods.util.SimpleLength;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odftoolkit.simple.SpreadsheetDocument;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * This is the test for the README.md
 */
public class StyleExampleIT {
    public static final String GENERATED_FILES = "generated_files";
    public static final String STYLE_EXAMPLE_ODS = "style_example.ods";

    @BeforeClass
    public static void beforeClass() {
        Util.mkdir(GENERATED_FILES);
    }

    private Logger logger;
    private OdsFactory odsFactory;
    private TableCellStyle style;
    private Locale locale;

    @Before
    public void setUp() {
        this.logger = Logger.getLogger("readme example");
        this.locale = Locale.US;
        this.odsFactory = OdsFactory.create(this.logger, this.locale);
    }


    @Test
    public void styleIT() throws Exception {
        this.style();
        this.validateStyle(STYLE_EXAMPLE_ODS);
    }

    private void validateStyle(final String documentName) throws Exception {
        final SpreadsheetDocument document = SpreadsheetDocument
                .loadDocument(new File(GENERATED_FILES, documentName));
        Assert.assertEquals(1, document.getSheetCount());
        final org.odftoolkit.simple.table.Table sheet = document.getSheetByName("test_style");
        Assert.assertNotNull(sheet);
        Assert.assertEquals(2, sheet.getRowCount());
        // TODO: Add more validation tests"
    }

    private void style() throws IOException, FastOdsException {
        // Pass the created "data styles" to the factory
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final OdsDocument document = writer.document();

        this.createTable(document);

        final File dest = new File(GENERATED_FILES, STYLE_EXAMPLE_ODS);
        writer.saveAs(dest);
    }

    private void createTable(final OdsDocument document) throws IOException, FastOdsException {
        final Table table = document.addTable("test_style");

        // Define some styles
        // first column is wrapped
        final TableCellStyle cellStyle = TableCellStyle.builder("wrapped-cell").fontWrap(true)
                .hidden().build();
        final TableColumnStyle columnStyleA = TableColumnStyle.builder("wrapped-col")
                .defaultCellStyle(cellStyle).columnWidth(SimpleLength.cm(2)).build();
        table.setColumnStyle(0, columnStyleA);

        // second column has OpenSymbol font
        final TableCellStyle symbolStyle = TableCellStyle.builder("symbol-cell").fontName(LOFonts.OPENSYMBOL).hidden().build();
        final TableColumnStyle columnStyleB = TableColumnStyle.builder("symbol-col")
                .columnWidth(SimpleLength.cm(5)).defaultCellStyle(symbolStyle).build();
        table.setColumnStyle(1, columnStyleB);

        // second row has title Liberation Font
        final TableCellStyle liberationStyle = TableCellStyle.builder("libe-cell").fontName(
                LOFonts.LIBERATION_MONO)
                .hidden().build();
        final TableRowStyle rowStyle2 = TableRowStyle.builder("libe-row").rowHeight(SimpleLength.cm(1.5))
                .hidden().build();

        // lonely style
        final TableCellStyle boldStyle = TableCellStyle.builder("bold").fontWeightBold().build();

        // FIRST ROW
        TableRow row = table.nextRow();
        TableCellWalker cell = row.getWalker();
        cell.setStringValue("Cell A1. Default text, wrapped. I said: default text, wrapped.");
        cell.next();
        cell.setStringValue("Cell B1. OpenSymbol font");

        // SECOND ROW
        row = table.nextRow();
        row.setStyle(rowStyle2);
        row.setDefaultCellStyle(liberationStyle);
        cell = row.getWalker();
        cell.setStyle(boldStyle);
        cell.setStringValue("Cell A2. Default text, wrapped and bold");
        cell.next();
        cell.setStringValue("Cell B2. Liberation Mono font");
    }
}
