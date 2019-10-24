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
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.attribute.FieldOrientation;
import com.github.jferard.fastods.attribute.PilotStandardFunction;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.tool.MacroHelper;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.FilterEnumerate;
import com.github.jferard.fastods.util.PilotTable;
import com.github.jferard.fastods.util.PilotTableField;
import com.github.jferard.fastods.util.PilotTableLevel;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Section 8 of the tutorial
 *
 * @author J. Férard
 */
class H_AutofiltersAndDataPilotTables {

    /**
     * Number of nanoseconds in a second
     */
    private static final int NANOSECONDS_PER_SECONDS = 1000000000;

    /**
     * @throws IOException if the file can't be written
     */
    static void example1() throws IOException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("autofilter"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("autofilter");
        final TableCellWalker walker = table.getWalker();
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Auto filters and Data Pilot tables
        // Auto filters and Data Pilot tables are defined in the OpenDocument specification.
        // However,
        // settings a specific filter in an Auto Filter, or defining an Data Pilot table is not
        // sufficient:
        // the line filtered lines won't be hidden by magic, the Data Pilot table won't be filled
        // out
        // by magic.
        //
        // FastODS is not a data processor and won't become one. Hence, it won't compute the
        // result of
        // a filter or a Pilot table. There's a workaround: let LibreOffice do the job. The idea is
        // to trigger a refresh of the data that will update the filters and the Pilot tables.
        //
        // ## A Simple Auto filter
        // It's easy to add manually an autofilter. Let's create some content:
        walker.setStringValue("File Type");
        walker.next();
        walker.setStringValue("Extension");
        walker.nextRow();
        walker.setStringValue("Text");
        walker.next();
        walker.setStringValue(".odt");
        walker.nextRow();
        walker.setStringValue("Spreadsheet");
        walker.next();
        walker.setStringValue(".ods");
        walker.nextRow();
        walker.setStringValue("Presentation");
        walker.next();
        walker.setStringValue(".odp");
        walker.nextRow();
        walker.setStringValue("Drawing");
        walker.next();
        walker.setStringValue(".odg");
        walker.nextRow();
        walker.setStringValue("Chart");
        walker.next();
        walker.setStringValue(".odc");
        walker.nextRow();
        walker.setStringValue("Formula");
        walker.next();
        walker.setStringValue(".odf");
        walker.nextRow();
        walker.setStringValue("Image");
        walker.next();
        walker.setStringValue(".odi");
        walker.nextRow();
        walker.setStringValue("Master Document");
        walker.next();
        walker.setStringValue(".odm");
        walker.nextRow();
        walker.setStringValue("Database");
        walker.next();
        walker.setStringValue(".odb");

        // Now we need to set the filter. It's possible to preset some filter with the `filter`
        // method of the builder.
        document.addAutoFilter(
                AutoFilter.builder("my_range", table, 0, 0, walker.rowIndex(), walker.colIndex())
                        .filter(new FilterEnumerate(0, "Spreadsheet", "Presentation",
                                "Master Document")).build());

        // The filter will be set (the little square appears and if you click on the arrow,
        // only Spreadsheet", "Presentation" and "Master Document" are checked. But... the rows
        // remain visible, making the function of very limited interest.
        //
        // As written in the introduction of this section, to hide the filtered rows, FastODS should
        // apply (and not just declare) the filter to
        // mark the rows as "filtered". But that's really overkill. There's an alternative solution:
        // it's possible to add a macro to the document, and to trigger that macro on document load.
        new MacroHelper().addRefreshMacro(document);

        // This macro will refresh all autofilters and hide the columns. (Note that adding this
        // macro is not mandatory.)
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "h_autofilter.ods"));
    }


    /**
     * @throws IOException if the file can't be written
     */
    static void example2() throws IOException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("pilottable"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table dataTable = document.addTable("pilottable");
        final TableCellWalker walker = dataTable.getWalker();
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## A Data Pilot table
        // Let's start with some data:

        walker.setStringValue("File Type");
        walker.next();
        walker.setStringValue("Extension");
        walker.next();
        walker.setStringValue("Length of type");
        walker.next();
        walker.setStringValue("Long or short");
        walker.nextRow();
        walker.setStringValue("Text");
        walker.next();
        walker.setStringValue(".odt");
        walker.next();
        walker.setFormula("LEN(A2)");
        walker.next();
        walker.setFormula("IF(C2 >= 8;\"long\";\"short\")");
        walker.nextRow();
        walker.setStringValue("Spreadsheet");
        walker.next();
        walker.setStringValue(".ods");
        walker.next();
        walker.setFormula("LEN(A3)");
        walker.next();
        walker.setFormula("IF(C3 >= 8;\"long\";\"short\")");
        walker.nextRow();
        walker.setStringValue("Presentation");
        walker.next();
        walker.setStringValue(".odp");
        walker.next();
        walker.setFormula("LEN(A4)");
        walker.next();
        walker.setFormula("IF(C4 >= 8;\"long\";\"short\")");
        walker.nextRow();
        walker.setStringValue("Drawing");
        walker.next();
        walker.setStringValue(".odg");
        walker.next();
        walker.setFormula("LEN(A5)");
        walker.next();
        walker.setFormula("IF(C5 >= 8;\"long\";\"short\")");
        walker.nextRow();
        walker.setStringValue("Chart");
        walker.next();
        walker.setStringValue(".odc");
        walker.next();
        walker.setFormula("LEN(A6)");
        walker.next();
        walker.setFormula("IF(C6 >= 8;\"long\";\"short\")");
        walker.nextRow();
        walker.setStringValue("Formula");
        walker.next();
        walker.setStringValue(".odf");
        walker.next();
        walker.setFormula("LEN(A7)");
        walker.next();
        walker.setFormula("IF(C7 >= 8;\"long\";\"short\")");
        walker.nextRow();
        walker.setStringValue("Image");
        walker.next();
        walker.setStringValue(".odi");
        walker.next();
        walker.setFormula("LEN(A8)");
        walker.next();
        walker.setFormula("IF(C8 >= 8;\"long\";\"short\")");
        walker.nextRow();
        walker.setStringValue("Master Document");
        walker.next();
        walker.setStringValue(".odm");
        walker.next();
        walker.setFormula("LEN(A9)");
        walker.next();
        walker.setFormula("IF(C9 >= 8;\"long\";\"short\")");
        walker.nextRow();
        walker.setStringValue("Database");
        walker.next();
        walker.setStringValue(".odb");
        walker.next();
        walker.setFormula("LEN(A10)");
        walker.next();
        walker.setFormula("IF(C10 >= 8;\"long\";\"short\")");

        // and a simple sheet `pilot` to host the Data Pilot table:
        final Table pilotTable = document.addTable("pilot");
        document.setActiveTable(1);

        // Now the we have to build the Data Pilot table. This is not an trivial task.
        // A Data Pilot table has several attributes, mainly:
        // * a name
        // * a source range
        // * a target range
        final PositionUtil positionUtil = PositionUtil.create();
        final PilotTable pilot = PilotTable
                .builder("DataPilot1", positionUtil.toRangeAddress(dataTable, 0, 0, 9, 3),
                        positionUtil.toRangeAddress(pilotTable, 0, 0, 0, 0),
                        Arrays.asList(positionUtil.toCellAddress(pilotTable, 1, 0),
                                positionUtil.toCellAddress(pilotTable, 0, 1)))

                // And some field. First, the column and row fields. The `isDataLayout` sets the
                // orientation of the table.
                .field(new PilotTableField("", FieldOrientation.COLUMN, -1, true,
                        PilotStandardFunction.AUTO, new PilotTableLevel(true)))
                .field(new PilotTableField("Long or short", FieldOrientation.ROW, 0, false,
                        PilotStandardFunction.AUTO, new PilotTableLevel(false)))

                // Then the data fields:
                .field(new PilotTableField("Length of type", FieldOrientation.DATA, 0, false,
                        PilotStandardFunction.COUNT, new PilotTableLevel(false)))
                .field(new PilotTableField("Length of type", FieldOrientation.DATA, 0, false,
                        PilotStandardFunction.SUM, new PilotTableLevel(false)))
                .field(new PilotTableField("Length of type", FieldOrientation.DATA, 0, false,
                        PilotStandardFunction.AVERAGE, new PilotTableLevel(false))).build();

        // Add the Data Pilot table to the document
        document.addPilotTable(pilot);

        // And force the refresh at start (not mandatory):
        new MacroHelper().addRefreshMacro(document);
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "h_data_pilot.ods"));
    }
}
