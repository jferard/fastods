/*
 * FastODS - a Martin Schulz's SimpleODS fork
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

import com.github.jferard.fastods.*;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DateStyleBuilder;
import com.github.jferard.fastods.datastyle.DateStyleFormat;
import com.github.jferard.fastods.datastyle.FloatStyleBuilder;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.Util;
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
public class DataStyleExampleIT {
    public static final String GENERATED_FILES = "generated_files";
    public static final String DATASTYLE_EXAMPLE_ODS = "datastyle_example.ods";

    @BeforeClass
    public static void beforeClass() {
        Util.mkdir(GENERATED_FILES);
    }

    private Logger logger;
    private OdsFactory odsFactory;
    private TableCellStyle style;

    @Before
    public void setUp() {
        this.logger = Logger.getLogger("readme example");
        this.odsFactory = OdsFactory.create(this.logger, Locale.US);
    }


    @Test
    public void datastyleIT() throws Exception {
        this.dataStyle();
        this.validateDatastyle(DATASTYLE_EXAMPLE_ODS);
    }

    private void validateDatastyle(final String documentName) throws Exception {
        final SpreadsheetDocument document = SpreadsheetDocument.loadDocument(new File(GENERATED_FILES, documentName));
        Assert.assertEquals(1, document.getSheetCount());
        final org.odftoolkit.simple.table.Table sheet = document.getSheetByName("test");
        Assert.assertNotNull(sheet);
        Assert.assertEquals(2, sheet.getRowCount());
        // TODO: Add more validation tests"
    }

    private void dataStyle() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final OdsDocument document = writer.document();

        this.createTable(document);

        writer.saveAs(new File(GENERATED_FILES, DATASTYLE_EXAMPLE_ODS));
    }

    private void createTable(final OdsDocument document) throws IOException {
        final GregorianCalendar cal = new GregorianCalendar(Locale.US);

        final Table table = document.addTable("test");
        TableRow row = table.nextRow();
        TableCellWalker cell = row.getWalker();
        cell.setStringValue("An int: ");
        cell.next();
        cell.setFloatValue(123456.789);
        final DataStyle intStyle = new FloatStyleBuilder("custom-int-datastyle", Locale.US).decimalPlaces(0)
                .groupThousands(true).buildHidden();
        cell.setDataStyle(intStyle);
        row = table.nextRow();
        cell = row.getWalker();
        cell.setStringValue("An date: ");
        cell.next();
        cal.set(2018, 1, 1, 0, 0, 0);
        cell.setDateValue(cal);
        final DataStyle dateStyle = new DateStyleBuilder("custom-date-datastyle", Locale.US).dateFormat(
                new DateStyleFormat(DateStyleFormat.LONG_DAY, DateStyleFormat.HASH, DateStyleFormat.LONG_MONTH,
                        DateStyleFormat.HASH, DateStyleFormat.LONG_YEAR)).buildHidden();
        cell.setDataStyle(dateStyle);
    }
}
