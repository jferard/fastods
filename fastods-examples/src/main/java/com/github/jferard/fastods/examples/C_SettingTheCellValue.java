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
import com.github.jferard.fastods.CurrencyValue;
import com.github.jferard.fastods.ObjectToCellValueConverter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.PercentageValue;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRowImpl;
import com.github.jferard.fastods.TimeValue;
import com.github.jferard.fastods.ToCellValueConverter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Section 3 of the tutorial
 *
 * @author J. Férard
 */
class C_SettingTheCellValue {
    /**
     * @throws IOException if the file can't be written
     */
    static void example() throws IOException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("cells"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Setting the cell value
        // Three elements define the content of a cell:
        // * the value and its the type (string, float, boolean, ...)
        // * the style (font, background color, border, ...)
        // * the format, or data style (number of digits for a float, date format for a date, ...)
        //
        // So far, we just created cells of type string, with neither style nor data style. Now,
        // we will create cells with a more varied content.
        final Table table = document.addTable("types");

        // We add a header:
        TableRowImpl row = table.nextRow();
        TableCellWalker walker = row.getWalker();
        walker.setStringValue("Type");
        walker.next();
        walker.setStringValue("Example");

        // The first row contains a boolean:
        row = table.nextRow();
        walker = row.getWalker();
        walker.setStringValue("Boolean");
        walker.next();
        walker.setBooleanValue(true);

        // The second row contains a currency:
        row = table.nextRow();
        walker = row.getWalker();
        walker.setStringValue("Currency");
        walker.next();
        walker.setCurrencyValue(10.5, "USD");

        // The third row contains a date:
        row = table.nextRow();
        walker = row.getWalker();
        walker.setStringValue("Date");
        walker.next();
        walker.setDateValue(new GregorianCalendar(2014, 9, 17, 9, 0, 0));

        // The fourth row contains a float:
        row = table.nextRow();
        walker = row.getWalker();
        walker.setStringValue("Float");
        walker.next();
        walker.setFloatValue(3.14159);

        // The fifth row contains a percentage:
        row = table.nextRow();
        walker = row.getWalker();
        walker.setStringValue("Percentage");
        walker.next();
        walker.setPercentageValue(0.545);

        // The sixth row contains...
        row = table.nextRow();
        walker = row.getWalker();
        walker.setStringValue("String");
        walker.next();
        walker.setStringValue("A String");

        // The seventh row contains a time (that mean a duration):
        row = table.nextRow();
        walker = row.getWalker();
        walker.setStringValue("Time");
        walker.next();
        walker.setTimeValue(3600);

        // The eighth row contains nothing
        row = table.nextRow();
        walker = row.getWalker();
        walker.setStringValue("Void");
        walker.next();
        walker.setVoidValue();

        // ### Type Guess
        // FastODS can guess types, based on Java object types. It's useful when we try to auto
        // import typed data, e.g. from a `ResultSet`. We can use this ability to reduce the
        // boilerplate code.
        //
        // Let's define two lists:
        final List<String> A = Arrays
                .asList("Type", "Boolean", "Currency", "Date", "Float", "Percentage", "String",
                        "Void");
        final List<Object> B = Arrays
                .asList("Type guess example", true, new CurrencyValue(10.5, "USD"),
                        new GregorianCalendar(2014, 9, 17, 9, 0, 0), 3.14159,
                        new PercentageValue(0.545), new TimeValue(false, 0, 0, 0, 0, 0, 3.6),
                        "A String", null);

        // And a converter:
        final ToCellValueConverter converter = new ObjectToCellValueConverter("USD");

        // As you can see, some types are not guessable: is `0.545` a float or a percentage? For
        // FastODS, it is a float. What Java type will map a currency value? We have to use specific
        // types...
        //
        // We skip a row for readability:
        table.nextRow();

        // Now, we can use `setValue` to take advantage of the type guess:
        for (int r = 0; r < A.size(); r++) {
            row = table.nextRow();
            walker = row.getWalker();
            walker.setStringValue(A.get(r));
            walker.next();
            walker.setCellValue(converter.from(B.get(r)));
        }

        // *Note:* We saw all the cell type available in the OpenDocument specification.
        // We'll see that FastODS has another kind of String value in the Text value section.

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "c_cell_value.ods"));
    }
}
