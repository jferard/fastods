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
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.datastyle.DateStyleBuilder;
import com.github.jferard.fastods.datastyle.DateTimeStyleFormat;
import com.github.jferard.fastods.datastyle.FloatStyleBuilder;
import com.github.jferard.fastods.datastyle.TimeStyleBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Logger;

class D_SettingTheCellDataStyle {
    static void example1() throws IOException, FastOdsException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("cells"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Setting the Cell Data Style
        // Data styles are what we call "formats": is your date in plain text or in US format? how
        // many digits have your number? are the negative numbers in red?
        //
        // A cell may have a style, and this style may have a data style. The OpenDocument
        // specification states that data style can't be attached directly to a cell, but must be
        // embedded in a cell. That's not easy to handle for FastODS, but should not be too
        // complicated for you.

        // As usual, we create a table and get the first cell:
        final Table table = document.addTable("data styles");

        // We'll place a float with the standard format, and a float with a custom format side by side
        TableRow tableRow = table.nextRow();
        TableCellWalker cellWalker = tableRow.getWalker();

        // Standard format:
        cellWalker.setFloatValue(123456.789);

        // And now create a custom data style:
        final DataStyle floatDataStyle = new FloatStyleBuilder("float-datastyle", Locale.US)
                .decimalPlaces(8).groupThousands(true).build();
        cellWalker.next();
        cellWalker.setFloatValue(123456.789);
        cellWalker.setDataStyle(floatDataStyle);

        // We can do the same with dates:
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();

        // A date with the standard format:
        final Calendar cal = new GregorianCalendar(2018, 1, 1, 0, 0, 0);
        cellWalker.setDateValue(cal);

        // And a custom format:
        final DataStyle dateDataStyle = new DateStyleBuilder("date-datastyle", Locale.US)
                .dateFormat(new DateTimeStyleFormat(DateTimeStyleFormat.DAY, DateTimeStyleFormat.DOT,
                        DateTimeStyleFormat.MONTH, DateTimeStyleFormat.DOT, DateTimeStyleFormat.YEAR)).visible()
                .build();
        cellWalker.next();
        cellWalker.setDateValue(cal);
        cellWalker.setDataStyle(dateDataStyle);

        // A last try with a time (duration):
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();
        cellWalker.setTimeValue(10000000);

        // And:
        final DataStyle timeDataStyle = new TimeStyleBuilder("time-datastyle", Locale.US)
                .timeFormat(new DateTimeStyleFormat(DateTimeStyleFormat.text("Hour: "),
                        DateTimeStyleFormat.LONG_HOURS)).visible().build();

        cellWalker.next();
        cellWalker.setTimeValue(10000000);
        cellWalker.setDataStyle(timeDataStyle);

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "d_data_style1.ods"));
    }

    public static void example2() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)

        // ### Changing the Default Data Styles
        // Setting the data style for every cell may become cumbersome. Happily, you can decide of
        // the default data styles at the creation of the document.
        //
        // First, create a `DataStyles` object (note the "s") with a builder:

        final DataStylesBuilder dsb = DataStylesBuilder.create(Locale.US);
        dsb.floatStyleBuilder().decimalPlaces(0);
        dsb.dateStyleBuilder().dateFormat(
                new DateTimeStyleFormat(DateTimeStyleFormat.LONG_DAY, DateTimeStyleFormat.SLASH,
                        DateTimeStyleFormat.LONG_MONTH, DateTimeStyleFormat.SLASH,
                        DateTimeStyleFormat.LONG_YEAR));

        // You can use the other data style builders if you want, and then build all the data style
        // in one shot:

        final DataStyles ds = dsb.build();

        // Now, create the factory
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("cells2"), Locale.US);

        // and pass the created "data styles" to the factory:
        odsFactory.dataStyles(ds);

        // We can continue as usual:
        final AnonymousOdsFileWriter writer = odsFactory.dataStyles(ds).createWriter();
        final OdsDocument document = writer.document();

        // And create the same cells as above:
        final Table table = document.addTable("data styles");
        final TableRow tableRow = table.nextRow();
        final TableCellWalker cellWalker = tableRow.getWalker();

        cellWalker.setFloatValue(123456.789);
        final Calendar cal = new GregorianCalendar(2018, 1, 1, 0, 0, 0);
        cellWalker.next();
        cellWalker.setDateValue(cal);

        // And save the file.
        writer.saveAs(new File("generated_files", "d_data_style2.ods"));
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
    }
}
