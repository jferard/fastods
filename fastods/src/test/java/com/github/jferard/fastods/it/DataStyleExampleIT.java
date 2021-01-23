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

package com.github.jferard.fastods.it;

import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.OdsFactoryBuilder;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.datastyle.DateStyleBuilder;
import com.github.jferard.fastods.datastyle.DateTimeStyleFormat;
import com.github.jferard.fastods.datastyle.FloatStyle;
import com.github.jferard.fastods.datastyle.FloatStyleBuilder;
import com.github.jferard.fastods.datastyle.TimeStyleBuilder;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
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
    private OdsFactoryBuilder odsFactoryBuilder;
    private TableCellStyle style;
    private Locale locale;

    @Before
    public void setUp() {
        this.logger = Logger.getLogger("readme example");
        this.locale = Locale.US;
        this.odsFactoryBuilder = OdsFactory.builder(this.logger, this.locale);
    }


    @Test
    public void datastyleIT() throws Exception {
        this.dataStyle();
        this.validateDatastyle(DATASTYLE_EXAMPLE_ODS);
    }

    private void validateDatastyle(final String documentName) throws Exception {
        final SpreadsheetDocument document =
                SpreadsheetDocument.loadDocument(new File(GENERATED_FILES, documentName));
        Assert.assertEquals(1, document.getSheetCount());
        final org.odftoolkit.simple.table.Table sheet = document.getSheetByName("test");
        Assert.assertNotNull(sheet);
        Assert.assertEquals(8, sheet.getRowCount());
        // TODO: Add more validation tests"
    }

    private void dataStyle() throws IOException {
        // Create a new default "data styles"
        final DataStylesBuilder dsb = DataStylesBuilder.create(Locale.US);
        dsb.floatStyleBuilder().decimalPlaces(0);
        dsb.dateStyleBuilder().dateFormat(
                new DateTimeStyleFormat(DateTimeStyleFormat.LONG_DAY, DateTimeStyleFormat.SLASH,
                        DateTimeStyleFormat.LONG_MONTH, DateTimeStyleFormat.SLASH,
                        DateTimeStyleFormat.LONG_YEAR));
        final DataStyles ds = dsb.build();

        // Pass the created "data styles" to the factory
        final AnonymousOdsFileWriter writer = this.odsFactoryBuilder.dataStyles(ds).build()
                .createWriter();
        final OdsDocument document = writer.document();

        this.createTable(document);

        final File dest = new File(GENERATED_FILES, DATASTYLE_EXAMPLE_ODS);
        writer.saveAs(dest);
    }

    private void createTable(final OdsDocument document) throws IOException {
        final GregorianCalendar cal = new GregorianCalendar(this.locale);

        // Define some styles
        final TableCellStyle cellStyle =
                TableCellStyle.builder("wrapped-cell").fontWrap(true).hidden().build();
        final TableRowStyle rowStyle =
                TableRowStyle.builder("row").rowHeight(SimpleLength.cm(1.5)).build();

        final Table table = document.addTable("test");
        final TableColumnStyle columnStyle0 =
                TableColumnStyle.builder("wrapped-col").build();
        table.setColumnStyle(0, columnStyle0);
        table.setColumnDefaultCellStyle(0, cellStyle);
        final TableColumnStyle columnStyle1 =
                TableColumnStyle.builder("col").columnWidth(SimpleLength.cm(5)).build();
        table.setColumnStyle(1, columnStyle1);

        // a column datastyle
        final FloatStyle floatStyle =
                new FloatStyleBuilder("second-custom-int-datastyle", this.locale).decimalPlaces(8)
                        .build();
        final TableCellStyle cellStyle1 =
                TableCellStyle.builder("datastyle0").dataStyle(floatStyle).build();
        final TableColumnStyle columnDataStyle =
                TableColumnStyle.builder("col-datastyle").columnWidth(SimpleLength.cm(5)).build();
        table.setColumnStyle(2, columnDataStyle);
        table.setColumnDefaultCellStyle(2, cellStyle1);

        final FloatStyle floatStyle2 =
                new FloatStyleBuilder("third-custom-int-datastyle", this.locale).decimalPlaces(6)
                        .build();
        final TableCellStyle cellStyle2 =
                TableCellStyle.builder("datastyle1").dataStyle(floatStyle2).build();

        // FIRST ROW
        final TableCellWalker walker = table.getWalker();
        walker.setRowStyle(rowStyle);
        walker.setStringValue("An int with the new default format: ");
        walker.next();
        walker.setFloatValue(123456.789);

        // SECOND ROW
        walker.nextRow();
        walker.setRowStyle(rowStyle);
        walker.setStringValue("An int with a custom format: ");
        walker.next();
        walker.setFloatValue(789654.321);
        // Now add a custom format.
        final DataStyle intStyle =
                new FloatStyleBuilder("custom-int-datastyle", this.locale).decimalPlaces(8)
                        .groupThousands(true).build();
        // This operation may be slow because the default data style was already added
        walker.setDataStyle(intStyle);

        // THIRD ROW
        walker.nextRow();
        walker.setRowStyle(rowStyle);
        walker.setStringValue("An date with the new default format: ");
        walker.next();
        cal.set(2018, 1, 1, 0, 0, 0);
        walker.setDateValue(cal);

        // FOURTH ROW
        walker.nextRow();
        walker.setRowStyle(rowStyle);
        walker.setStringValue("An date with a custom format: ");
        walker.next();
        cal.set(2017, 12, 1, 0, 0, 0);
        walker.setDateValue(cal);
        // Add a custom format
        final DataStyle dateStyle = new DateStyleBuilder("custom-date-datastyle", this.locale)
                .dateFormat(
                        new DateTimeStyleFormat(DateTimeStyleFormat.DAY, DateTimeStyleFormat.DOT,
                                DateTimeStyleFormat.MONTH, DateTimeStyleFormat.DOT,
                                DateTimeStyleFormat.YEAR)).visible().build();
        walker.setDataStyle(dateStyle);

        // 5TH ROW
        walker.nextRow();
        walker.setRowStyle(rowStyle);
        walker.setStringValue("A time with the default format: ");
        walker.next();
        walker.setTimeValue(10000000);

        // 6TH ROW
        walker.nextRow();
        walker.setRowStyle(rowStyle);
        walker.setStringValue("A time with a custom format: ");
        walker.next();
        walker.setTimeValue(10000000);
        // Add a custom format
        final DataStyle timeStyle = new TimeStyleBuilder("custom-time-datastyle", this.locale)
                .timeFormat(new DateTimeStyleFormat(DateTimeStyleFormat.text("Hour: "),
                        DateTimeStyleFormat.LONG_HOURS)).visible().build();
        walker.setDataStyle(timeStyle);

        // 7TH ROW: same as FOURTH, but the datastyle is put before the value
        walker.nextRow();
        walker.setRowStyle(rowStyle);        walker.setStringValue("An date with a custom format (datastyle set before the value): ");
        walker.next();
        walker.setDataStyle(dateStyle);
        walker.setFloatValue(10);
        walker.next();
        walker.setFloatValue(10);

        // 8TH ROW
        walker.nextRow();
        walker.setRowStyle(rowStyle);
        walker.setRowDefaultCellStyle(cellStyle2);
        walker.setFloatValue(100000);
        walker.next();
        walker.setFloatValue(100000);
        walker.next();
        walker.setFloatValue(100000);
        walker.next();
        walker.setFloatValue(100000);
    }
}
