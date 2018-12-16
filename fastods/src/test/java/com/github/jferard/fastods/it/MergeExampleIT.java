/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.NamedOdsDocument;
import com.github.jferard.fastods.NamedOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;
import com.github.jferard.fastods.testlib.OdfToolkitUtil;
import com.github.jferard.fastods.testlib.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odftoolkit.odfdom.dom.element.table.TableCoveredTableCellElement;
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
public class MergeExampleIT {
    public static final String GENERATED_FILES = "generated_files";
    public static final String MERGE_EXAMPLE_ODS = "merge_example.ods";

    @BeforeClass
    public static void beforeClass() {
        Util.mkdir(GENERATED_FILES);
    }
    private Logger logger;
    private OdsFactory odsFactory;

    @Before
    public void setUp() {
        this.logger = Logger.getLogger("merge example");
        this.odsFactory = OdsFactory.create(this.logger, Locale.US);
    }

    @Test
    public void mergeIT() throws Exception {
        this.merge();
        this.validateMerge(MERGE_EXAMPLE_ODS);
    }

    private void validateMerge(final String documentName) throws Exception {
        final SpreadsheetDocument document = SpreadsheetDocument.loadDocument(new File(GENERATED_FILES, documentName));
        Assert.assertEquals(1, document.getSheetCount());
        final org.odftoolkit.simple.table.Table sheet = document.getSheetByName("test");
        Assert.assertNotNull(sheet);
        Assert.assertEquals(13, sheet.getRowCount());
        final Cell cell1 = sheet.getCellByPosition(0, 0);
        Assert.assertEquals(5, cell1.getColumnSpannedNumber());
        Assert.assertEquals(1, cell1.getRowSpannedNumber());

        final Cell cell2 = sheet.getCellByPosition(0, 1);
        Assert.assertEquals(1, cell2.getColumnSpannedNumber());
        Assert.assertEquals(2, cell2.getRowSpannedNumber());

        final Cell cell3 = sheet.getCellByPosition(1, 1);
        Assert.assertEquals(4, cell3.getColumnSpannedNumber());
        Assert.assertEquals(2, cell3.getRowSpannedNumber());

        final Cell cell4 = sheet.getCellByPosition(0, 3);
        Assert.assertEquals(1, cell4.getColumnSpannedNumber());
        Assert.assertEquals(10, cell4.getRowSpannedNumber());

        for (int c=0; c<5; c++) {
            for (int r = 0; r < 3; r++) {
                if (c == 0 && r == 0 || c <= 1 && r == 1) {
                    continue;
                }

                final Cell cell = sheet.getCellByPosition(c, r);
                Assert.assertTrue(cell.getOdfElement() instanceof TableCoveredTableCellElement);
            }
        }
    }

    private void merge() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final OdsDocument document = writer.document();

        this.createTable(document);

        writer.saveAs(new File(GENERATED_FILES, MERGE_EXAMPLE_ODS));
    }

    private void createTable(final OdsDocument document) throws IOException {
        final Table table = document.addTable("test");

        // 1st row
        final TableRow row1 = table.nextRow();
        final TableCellWalker cell1 = row1.getWalker();
        for (int c=0; c<5; c++) {
            cell1.setFloatValue(c+1);
            cell1.next();
        }
        row1.setCellMerge(0, 1, 5);

        // 2nd row
        final TableRow row2 = table.nextRow();
        final TableCellWalker cell2 = row2.getWalker();
        for (int c=0; c<5; c++) {
            cell2.setFloatValue(c+6);
            cell2.next();
        }

        // 3rd row
        final TableRow row3 = table.nextRow();
        final TableCellWalker cell3 = row3.getWalker();
        for (int c=0; c<5; c++) {
            cell3.setFloatValue(c+11);
            cell3.next();
        }

        row2.setCellMerge(0, 2, 1);
        row2.setCellMerge(1, 2, 4);

        // 4th row
        final TableRow row4 = table.nextRow();
        final TableCellWalker cell4 = row4.getWalker();
        cell4.setFloatValue(15);
        cell4.setRowsSpanned(10);
    }
}
