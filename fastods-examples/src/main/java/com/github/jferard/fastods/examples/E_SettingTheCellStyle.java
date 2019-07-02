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
import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DateTimeStyleFormat;
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
import java.util.Locale;
import java.util.logging.Logger;

class E_SettingTheCellStyle {
    static void example() throws IOException, FastOdsException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("cells"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Setting the Cell Style
        // Let's try to add some shapes and colors. First, we have to create a style for the header:
        final TableCellStyle grayStyle = TableCellStyle.builder("gray")
                .backgroundColor(SimpleColor.GRAY64).fontWeightBold().build();

        // The functions calls are chained in a fluent style.
        //
        // We create a table and get the first cell:
        final Table table = document.addTable("styles");
        TableRow tableRow = table.nextRow();
        TableCellWalker cellWalker = tableRow.getWalker();

        // Now, we add a value and set the style
        cellWalker.setStringValue("A1");
        cellWalker.setStyle(grayStyle);

        // ### Common Styles and Automatic Styles
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

        // ### Rows and Columns Styles
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

        // Obvioulsy, you can combine a style and a data style:
        final DataStyle timeDataStyle = new TimeStyleBuilder("time-datastyle", Locale.US)
                .timeFormat(new DateTimeStyleFormat(DateTimeStyleFormat.text("Hour: "),
                        DateTimeStyleFormat.LONG_HOURS)).visible().build();

        tableRow = table.nextRow();
        cellWalker = tableRow.getWalker();
        cellWalker.setTimeValue(10000000);
        cellWalker.setStyle(rotateStyle);
        cellWalker.setDataStyle(timeDataStyle);

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "e_cell_styles.ods"));
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
    }
}
