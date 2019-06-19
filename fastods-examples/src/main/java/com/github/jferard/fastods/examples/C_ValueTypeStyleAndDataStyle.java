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
import com.github.jferard.fastods.CellValue;
import com.github.jferard.fastods.CurrencyValue;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.PercentageValue;
import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.datastyle.DateStyleBuilder;
import com.github.jferard.fastods.datastyle.DateStyleFormat;
import com.github.jferard.fastods.datastyle.FloatStyleBuilder;
import com.github.jferard.fastods.datastyle.TimeStyleBuilder;
import com.github.jferard.fastods.style.BorderAttribute;
import com.github.jferard.fastods.style.LOFonts;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.util.Angle;
import com.github.jferard.fastods.util.SimpleLength;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

class C_ValueTypeStyleAndDataStyle {
    static void example1() throws IOException, FastOdsException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("cells"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Value, type, style and data style
        // Four elements define the content of a cell:
        // * the value
        // * the type (string, float, boolean, ...)
        // * the style (font, background color, border, ...)
        // * the format, or data style (number of digits for a float, date format for a date, ...)
        //
        // So far, we just created cells of type string, with neither style nor data style. Now,
        // we will create cells with a more varied content.

        // ## Types
        // Now, we will create cells of different types. First, a table:
        Table table = document.addTable("types");

        // We add a header:
        TableRow tableRow = table.nextRow();
        TableCellWalker cellWalker = tableRow.getWalker();
        cellWalker.setStringValue("Type");
        cellWalker.next();
        cellWalker.setStringValue("Example");

        // The first row contains a boolean:
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();
        cellWalker.setStringValue("Boolean");
        cellWalker.next();
        cellWalker.setBooleanValue(true);

        // The second row contains a currency:
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();
        cellWalker.setStringValue("Currency");
        cellWalker.next();
        cellWalker.setCurrencyValue(10.5, "USD");

        // The third row contains a date:
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();
        cellWalker.setStringValue("Date");
        cellWalker.next();
        cellWalker.setDateValue(new GregorianCalendar(2014, 9, 17, 9, 0, 0));

        // The fourth row contains a float:
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();
        cellWalker.setStringValue("Float");
        cellWalker.next();
        cellWalker.setFloatValue(3.14159);

        // The fifth row contains a percentage:
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();
        cellWalker.setStringValue("Percentage");
        cellWalker.next();
        cellWalker.setPercentageValue(0.545);

        // The sixth row contains...
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();
        cellWalker.setStringValue("String");
        cellWalker.next();
        cellWalker.setStringValue("A String");

        // The seventh row contains a time (that mean a duration):
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();
        cellWalker.setStringValue("Time");
        cellWalker.next();
        cellWalker.setTimeValue(3600);

        // The eighth row contains nothing
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();
        cellWalker.setStringValue("Void");
        cellWalker.next();
        cellWalker.setVoidValue();

        // ### Type guess
        // FastODS can guess types, based on Java object types. It's useful when we try to auto
        // import typed data, e.g. from a `ResultSet`. We can use this ability to reduce the
        // boilerplate code.
        //
        // Let's define two lists:
        final List<String> A = Arrays
                .asList("Type", "Boolean", "Currency", "Date", "Float", "Percentage", "String",
                        "Void");
        final List<Object> B = Arrays.<Object>asList("Type guess example", true,
                new CurrencyValue(10.5f, "USD"), new GregorianCalendar(2014, 9, 17, 9, 0, 0),
                3.14159, new PercentageValue(0.545f), "A String", null);

        // As you can see, some types are not guessable: is `0.545` a float or a percentage? For
        // FastODS, it is a float. What Java type will map a currency value? We have to use specific
        // types...
        //
        // We skip a row for readability:
        table.nextRow();

        // Now, we can use `setValue` to take advantage of the type guess:
        for (int r = 0; r < A.size(); r++) {
            tableRow = table.nextRow();
            cellWalker = tableRow.getWalker();
            cellWalker.setStringValue(A.get(r));
            cellWalker.next();
            cellWalker.setCellValue(CellValue.fromObject(B.get(r)));
        }

        // ## Styles
        // Let's try to add some shapes and colors. First, we have to create a style for the header:
        final TableCellStyle grayStyle = TableCellStyle.builder("gray")
                .backgroundColor(SimpleColor.GRAY64).fontWeightBold().build();

        // The functions calls are chained in a fluent style.
        //
        // We create a table and get the first cell:
        table = document.addTable("styles");
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();

        // Now, we add a value and set the style
        cellWalker.setStringValue("A1");
        cellWalker.setStyle(grayStyle);

        // ### Common styles and automatic styles
        // In LO, you'll see a new style named "gray" in the "Styles" window. That's because
        // `TableCellStyle`s are visible by default. We can make a style hidden by adding a
        // `hidden()` call:
        final TableCellStyle hiddenGrayStyle = TableCellStyle.builder("hiddenGray")
                .backgroundColor(SimpleColor.GRAY64).fontWeightBold().hidden().build();

        cellWalker.next();
        cellWalker.setStringValue("A2");
        cellWalker.setStyle(grayStyle);

        // The "gray2" style is not present in the Style window of LO. This distinction between
        // "visible" and "hidden" styles matches the distinction between common and automatic
        // styles in the OpenDocument specification (3.15.3):
        //
        // > Note: Common and automatic styles behave differently in OpenDocument editing consumers.
        // > Common styles are presented to the user as a named set of formatting properties.
        // > The formatting properties of an automatic style are presented to a user as
        // > properties of the object to which the style is applied.
        //
        // This distinction is sometimes hard to handle. FastODS tries to make things easy:
        // each style is, by default, either visible or hidden (depending on the kind of the style),
        // but you can always override the default choice. For instance, `TableCellStyle`s,
        // are visible by default, but `TableRowStyle`s and data styles are hidden by default.
        // In most of the cases, you can simply ignore the distinction.
        //
        // Let's continue with a new style:
        final TableCellStyle rotateStyle = TableCellStyle.builder("rotate")
                .fontColor(SimpleColor.RED).textRotating(Angle.deg(37)).build();

        cellWalker.next();
        cellWalker.setStringValue("A3");
        cellWalker.setStyle(rotateStyle);

        // You can explore the `TableCellStyle` to create the style you need. A last example:
        final TableCellStyle borderStyle = TableCellStyle.builder("border").fontName(LOFonts.DEJAVU_SANS).fontSize(SimpleLength.pt(24))
                .borderAll(SimpleLength.mm(2), SimpleColor.BLUE, BorderAttribute.Style.OUTSET).build();

        cellWalker.next();
        cellWalker.setStringValue("A4");
        cellWalker.setStyle(borderStyle);
        // I think you get it now.

        // ### Rows and columns styles
        // What do we see? Yes, the last cell is ugly. But it is also partially hidden because
        // the height of the row was not adapted. You have to adapt it yourself. Let's try with
        // another row:
        final TableRowStyle tallRowStyle = TableRowStyle.builder("tall-row").rowHeight(SimpleLength.cm(3)).
                build();

        tableRow = table.nextRow();
        tableRow.setStyle(tallRowStyle);
        cellWalker = tableRow.getWalker();
        cellWalker.setStringValue("B1");
        cellWalker.setStyle(borderStyle);

        // You have to set the height of the row manually. There's nothing like an Optimal
        // height/width in the OpenDocument specification, and FastODS won't provide those
        // features. (Maybe one day I'll write a tool to compute the width/height of a text.)

        // You can also add a column style:
        final TableColumnStyle wideColumn = TableColumnStyle.builder("wide-col")
                .columnWidth(SimpleLength.cm(9)).build();
        table.setColumnStyle(0, wideColumn);

        // ## Data Styles
        // Data styles are what we call "formats": is your date in plain text or in US format? how
        // many digits have your number? are the negative numbers in red?
        //
        // A cell may have a style, and this style may have a data style. The OpenDocument
        // specification states that data style can't be attached directly to a cell, but must be
        // embedded in a cell. That's not easy to handle for FastODS, but should not be too
        // complicated for you.

        // As usual, we create a table and get the first cell:
        table = document.addTable("data styles");

        // We'll place a float with the standard format, and a float with a custom format side by side
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();

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
                .dateFormat(new DateStyleFormat(DateStyleFormat.DAY, DateStyleFormat.DOT,
                        DateStyleFormat.MONTH, DateStyleFormat.DOT, DateStyleFormat.YEAR)).visible()
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
                .timeFormat(new DateStyleFormat(DateStyleFormat.text("Hour: "),
                        DateStyleFormat.LONG_HOURS)).visible().build();

        cellWalker.next();
        cellWalker.setTimeValue(10000000);
        cellWalker.setDataStyle(timeDataStyle);

        // Obvioulsy, you can combine a style and a data style:
        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();
        cellWalker.setTimeValue(10000000);
        cellWalker.setStyle(rotateStyle);
        cellWalker.setDataStyle(timeDataStyle);

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "c_value_type1.ods"));
    }

    public static void example2() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)

        // ### Change the default data styles
        // Setting the data style for every cell may become cumbersome. Happily, you can decide of
        // the default data styles at the creation of the document.
        //
        // First, create a `DataStyles` object (note the "s") with a builder:

        final DataStylesBuilder dsb = DataStylesBuilder.create(Locale.US);
        dsb.floatStyleBuilder().decimalPlaces(0);
        dsb.dateStyleBuilder().dateFormat(
                new DateStyleFormat(DateStyleFormat.LONG_DAY, DateStyleFormat.SLASH,
                        DateStyleFormat.LONG_MONTH, DateStyleFormat.SLASH,
                        DateStyleFormat.LONG_YEAR));

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
        writer.saveAs(new File("generated_files", "c_value_type2.ods"));
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
    }
}
