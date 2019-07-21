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

package com.github.jferard.fastods.it;

import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.NamedOdsDocument;
import com.github.jferard.fastods.NamedOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.RowCellWalker;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TableRowImpl;
import com.github.jferard.fastods.odselement.ScriptEventListener;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.OdfToolkitUtil;
import com.github.jferard.fastods.testlib.Util;
import com.github.jferard.fastods.tool.MacroHelper;
import com.github.jferard.fastods.util.ColorHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElementBase;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * This is the test for the README.md
 */
public class ReadmeExampleIT {
    public static final String GENERATED_FILES = "generated_files";
    public static final String README_EXAMPLE_ODS = "readme_example.ods";
    public static final String README_EXAMPLE_WITH_FLUSH_ODS = "readme_example_with_flush.ods";
    public static final String GREEN_CELL_STYLE = "green-cell-style";
    public static final Color GREEN_COLOR = ColorHelper.fromString("#00ff00");

    @BeforeClass
    public static void beforeClass() {
        Util.mkdir(GENERATED_FILES);
    }

    private Logger logger;
    private OdsFactory odsFactory;
    private TableCellStyle style;
    private MacroHelper macroHelper;

    @Before
    public void setUp() {
        this.logger = Logger.getLogger("readme example");
        this.odsFactory = OdsFactory.create(this.logger, Locale.US);
        this.style = TableCellStyle.builder(GREEN_CELL_STYLE).backgroundColor(GREEN_COLOR).build();
        this.macroHelper = new MacroHelper();
    }


    @Test
    public void readmeIT() throws Exception {
        this.readme();
        this.validateReadme(README_EXAMPLE_ODS);
    }

    @Test
    public void readmeWithFlushIT() throws Exception {
        this.readmeWithFlush();
        this.validateReadme(README_EXAMPLE_WITH_FLUSH_ODS);
    }

    private void validateReadme(final String documentName) throws Exception {
        final SpreadsheetDocument document = SpreadsheetDocument
                .loadDocument(new File(GENERATED_FILES, documentName));
        Assert.assertEquals(1, document.getSheetCount());
        final org.odftoolkit.simple.table.Table sheet = document.getSheetByName("test");
        Assert.assertNotNull(sheet);
        Assert.assertEquals(50, sheet.getRowCount());
        final OdfStyle gcs = OdfToolkitUtil
                .getDocumentStyle(document, GREEN_CELL_STYLE, OdfStyleFamily.TableCell);
        Assert.assertEquals("Default", gcs.getStyleParentStyleNameAttribute());
        final Node properties = OdfToolkitUtil.getFirstElement(gcs, "style:table-cell-properties");
        Assert.assertEquals(GREEN_COLOR.hexValue(),
                OdfToolkitUtil.getAttribute(properties, "fo:background-color"));
        for (int y = 0; y < 50; y++) {
            for (int x = 0; x < 5; x++) {
                final Cell cell = sheet.getCellByPosition(x, y);
                Assert.assertEquals(Double.valueOf(x * y), cell.getDoubleValue());
                Assert.assertEquals("float", cell.getValueType());

                final TableTableCellElementBase element = cell.getOdfElement();
                Assert.assertEquals(GREEN_CELL_STYLE + "-_-float-data",
                        OdfToolkitUtil.getStyleName(cell));
                Assert.assertEquals("table-cell", OdfToolkitUtil.getStyleFamilyName(cell));
                Assert.assertEquals(GREEN_CELL_STYLE, OdfToolkitUtil.getParentStyleName(cell));
            }
        }
    }

    private void readme() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final OdsDocument document = writer.document();

        this.createTable(document);
        this.macroHelper.addRefreshMacro(document);
        writer.saveAs(new File(GENERATED_FILES, README_EXAMPLE_ODS));
    }

    private void createTable(final OdsDocument document) throws IOException {
        final Table table = document.addTable("test");
        for (int y = 0; y < 50; y++) {
            final TableRowImpl row = table.nextRow();
            row.setDefaultCellStyle(this.style);
            final RowCellWalker cell = row.getWalker();
            for (int x = 0; x < 5; x++) {
                cell.setFloatValue(x * y);
                cell.next();
            }
        }
    }

    private void readmeWithFlush() throws IOException {
        final NamedOdsFileWriter writer = this.odsFactory
                .createWriter(new File(GENERATED_FILES, README_EXAMPLE_WITH_FLUSH_ODS));
        final NamedOdsDocument document = writer.document();
        this.macroHelper.addRefreshMacro(document);
        document.addContentStyle(this.style);
        document.addCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, TableCell.Type.FLOAT);
        document.addCellStyle(this.style, TableCell.Type.FLOAT);
        document.addEvents(ScriptEventListener.create("dom:load", "Standard.FastODS.Refresh"));
        document.freezeStyles(); // if this crashes, use debugStyles to log the errors

        this.createTable(document);


        document.save();
    }
}
