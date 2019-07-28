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
import com.github.jferard.fastods.NamedOdsDocument;
import com.github.jferard.fastods.NamedOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TimeValue;
import com.github.jferard.fastods.odselement.config.ConfigElement;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.tool.MacroHelper;
import com.github.jferard.fastods.tool.ResultSetDataWrapper;
import com.github.jferard.fastods.tool.SQLToCellValueConverter;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.FilterEnumerate;
import com.github.jferard.fastods.util.SimpleLength;
import org.h2.api.Interval;
import org.h2.jdbcx.JdbcDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Section 8 of the tutorial
 *
 * @author J. Férard
 */
class I_Misc {

    /**
     * Number of nanoseconds in a second
     */
    private static final int NANOSECONDS_PER_SECONDS = 1000000000;

    /**
     * @throws IOException if the file can't be written
     */
    static void example1() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // # Miscellanous Features
        // ## A Named Writer
        // You can create a named writer to write large files. This feature is experimental, because
        // LO will never be able to open large files.
        //
        // Here's a sketch of how it works:
        // * When you create a `NamedOdsFileWriter` (instead of an anonymous one), the writer is
        // registered as an observer by the inner document.
        // * When a new table is added, remaining rows of the previous table are flushed.
        // * When a new row is created, if the buffer of rows is full, rows are flushed.
        //
        // That's why all styles have to be registered before the content is added.
        //
        // In practice, you have to give the name of the file at the writer creation:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("advanced"), Locale.US);
        final NamedOdsFileWriter writer = odsFactory
                .createWriter(new File("generated_files", "h_named_advanced.ods"));

        // Then, get the document and the tables as usual.
        final NamedOdsDocument document = writer.document();
        final Table table = document.addTable("advanced");

        // You have to register all the styles now:

        final TableCellStyle boldCellStyle = TableCellStyle.builder("cell").fontWeightBold()
                .fontSize(SimpleLength.pt(24)).build();
        document.addContentStyle(boldCellStyle);
        //
        // And, if necessary:
        //
        //     document.addPageStyle(aPageStyle);
        //     document.addContentStyle(aTableStyle);
        //     document.addContentStyle(aTableRowStyle);
        //     document.addContentStyle(aTableColumnStyle);
        //     document.addContentStyle(aTableCellStyle);
        //     document.addContentStyle(aTextStyle);
        //
        // An now, you can fill the Spreadsheet as usual.
        final TableCellWalker walker = table.getWalker();
        walker.setStringValue("A huge document");
        walker.setStyle(boldCellStyle);

        // When you're finished:
        document.save();

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
    }

    /**
     * @throws IOException  if the file can't be written
     * @throws SQLException in something goes wrong with the local database
     */
    static void example2() throws IOException, SQLException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("advanced"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("advanced");
        final TableCellWalker walker = table.getWalker();
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## Writing a ResultSet to the Spreadsheet
        // We need a ResultSet. Let's use H2:

        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test");
        final Connection conn = dataSource.getConnection();
        try {
            final Statement s = conn.createStatement();
            s.execute("CREATE TABLE document (file_type TEXT, extension TEXT)");
            s.execute("INSERT INTO document VALUES ('Text', '.odt'), ('Spreadsheet', '.ods'), " +
                    "('Presentation', '.odp'), ('Drawing', '.odg'), ('Chart', '.odc'), " +
                    "('Formula', '.odf'), ('Image', '.odi'), ('Master Document', '.odm')" +
                    ", ('Database', '.odb')");
            final ResultSet rs = s.executeQuery("SELECT * FROM document");

            // Now, we can write the result on a document. It will use the current row of the table:
            walker.addData(ResultSetDataWrapper.builder(rs).build());

            // It's possible to add multiple ResultSets:
            walker.toRow(0);
            walker.to(3);
            final ResultSet rs2 = s.executeQuery(
                    "SELECT file_type as file_type7, extension FROM document WHERE LENGTH" +
                            "(file_type) > 7");
            walker.addData(ResultSetDataWrapper.builder(rs2).build());

            // Let's create another table to test data types:
            s.execute("CREATE TABLE item (id CHAR(12), name TEXT, price DECIMAL, tax DECIMAL, " +
                    "high_quality BOOLEAN, lifespan INTERVAL DAY TO HOUR, image BLOB, " +
                    "creation_date TIMESTAMP)");
            s.execute("INSERT INTO item VALUES ('01234789', 'toothbrush', 3, 0.6, True, '30 8', " +
                    "RAWTOHEX('FastODS'), '2019-01-01')");
            final ResultSet rs3 = s.executeQuery("SELECT * FROM item");

            // FastODS uses the type guess to determine the type of objects. But the jdbc API
            // does not provide a class for SQL's INTERVAL object. Hence, FastODS provides a class
            // to define a cast try to an INTERVAL.
            final SQLToCellValueConverter.IntervalConverter converter =
                    new SQLToCellValueConverter.IntervalConverter() {
                @Override
                public TimeValue castToInterval(final Object o) {
                    if (o instanceof Interval) {
                        final Interval interval = (Interval) o;
                        final boolean neg = interval.isNegative();
                        switch (interval.getQualifier()) {
                            case YEAR:
                                return new TimeValue(neg, interval.getLeading(), 0, 0, 0, 0, 0);
                            case MONTH:
                                return new TimeValue(neg, 0, interval.getLeading(), 0, 0, 0, 0);
                            case YEAR_TO_MONTH:
                                return new TimeValue(neg, interval.getLeading(),
                                        interval.getRemaining(), 0, 0, 0, 0);
                            case DAY:
                                return new TimeValue(neg, 0, 0, interval.getLeading(), 0, 0, 0);
                            case HOUR:
                                return new TimeValue(neg, 0, 0, 0, interval.getLeading(), 0, 0);
                            case MINUTE:
                                return new TimeValue(neg, 0, 0, 0, 0, interval.getLeading(), 0);
                            case SECOND:
                                return new TimeValue(neg, 0, 0, 0, 0, 0, interval.getLeading());
                            case DAY_TO_HOUR:
                                return new TimeValue(neg, 0, 0, interval.getLeading(),
                                        interval.getRemaining(), 0, 0);
                            case DAY_TO_MINUTE:
                                return new TimeValue(neg, 0, 0, interval.getLeading(), 0,
                                        interval.getRemaining(), 0);
                            case DAY_TO_SECOND:
                                return new TimeValue(neg, 0, 0, interval.getLeading(), 0, 0,
                                        interval.getRemaining() / NANOSECONDS_PER_SECONDS);
                            case HOUR_TO_MINUTE:
                                return new TimeValue(neg, 0, 0, 0, interval.getLeading(),
                                        interval.getRemaining(), 0);
                            case HOUR_TO_SECOND:
                                return new TimeValue(neg, 0, 0, 0, interval.getLeading(), 0,
                                        interval.getRemaining() / NANOSECONDS_PER_SECONDS);
                            case MINUTE_TO_SECOND:
                                return new TimeValue(neg, 0, 0, 0, 0, interval.getLeading(),
                                        interval.getRemaining() / NANOSECONDS_PER_SECONDS);
                        }
                    }
                    return null;
                }
            };

            // And skip another row, then write the result:
            walker.toRow(12);
            walker.addData(ResultSetDataWrapper.builder(rs3).converter(converter).build());
        } finally {
            conn.close();
        }

        // ## LO features
        // If you know what you are doing, you can play with LO settings, for instance:
        table.updateConfigItem(ConfigElement.ZOOM_VALUE, "150");
        //
        // You can discover the configuration attributes in the `ConfigElement` enum.
        //
        // ## Add files to the ods archive
        // Remember thie method to add an auto update to the document? That was:
        //
        //     new MacroHelper().addRefreshMacro(document);
        //
        // Under the hood, this function adds some files to the ods archive. The ods
        // archive contains a `manifest.xml` that lists the files. If a file was added
        // without a matching entry in the manifest, LibreOffice will bark and refuse to
        // open the file.
        //
        // Let's add a file for the fun:
        document.addExtraDir("FastODS");
        document.addExtraFile("FastODS/fast.txt", "text/plain", "Hello from FastODS!");
        //
        // You can check that the file was added with your favorite file achive viewer.
        //
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "h_advanced_rs.ods"));
    }
}