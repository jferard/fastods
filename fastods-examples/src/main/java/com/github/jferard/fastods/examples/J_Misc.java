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

package com.github.jferard.fastods.examples;

import com.github.jferard.fastods.AbstractTableCell;
import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.NamedOdsDocument;
import com.github.jferard.fastods.NamedOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.Text;
import com.github.jferard.fastods.TimeValue;
import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.odselement.config.ConfigElement;
import com.github.jferard.fastods.style.LOFonts;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.tool.DatabaseExporter;
import com.github.jferard.fastods.tool.ResultSetDataWrapper;
import com.github.jferard.fastods.tool.ResultSetDataWrapperBuilder;
import com.github.jferard.fastods.tool.SQLToCellValueConverter;
import com.github.jferard.fastods.util.Validation;
import com.github.jferard.fastods.util.XMLUtil;
import org.h2.api.Interval;
import org.h2.jdbcx.JdbcDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Section 8 of the tutorial
 *
 * @author J. Férard
 */
class J_Misc {

    /**
     * Number of nanoseconds in a second
     */
    private static final int NANOSECONDS_PER_SECONDS = 1000000000;

    /**
     * @throws IOException if the file can't be written
     */
    static void namedWriterExample() throws IOException {
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
        final NamedOdsFileWriter writer =
                odsFactory.createWriter(new File("generated_files", "j_named_misc.ods"));

        // Then, get the document and the tables as usual.
        final NamedOdsDocument document = writer.document();

        // You have to register all the styles now:
        final TableCellStyle boldCellStyle =
                TableCellStyle.builder("cell").fontWeightBold().fontSize(SimpleLength.pt(24))
                        .build();
        document.addContentStyle(boldCellStyle);
        document.freezeStyles();
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
        final Table table = document.addTable("advanced");

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
    static void resultSetExample() throws IOException, SQLException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("misc"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("rs");
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
            final ResultSet rs1 = s.executeQuery("SELECT * FROM document");

            // Now, we can write the result on a document. It will use the current row of the table:
            walker.addData(ResultSetDataWrapper.builder("1", rs1).build());

            // It's possible to add multiple ResultSets:
            walker.toRow(0);
            walker.to(3);
            final ResultSet rs2 = s.executeQuery(
                    "SELECT file_type as file_type7, extension FROM document WHERE LENGTH" +
                            "(file_type) > 7");
            walker.addData(ResultSetDataWrapper.builder("2", rs2).build());

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
                                        return new TimeValue(neg, interval.getLeading(), 0, 0, 0, 0,
                                                0);
                                    case MONTH:
                                        return new TimeValue(neg, 0, interval.getLeading(), 0, 0, 0,
                                                0);
                                    case YEAR_TO_MONTH:
                                        return new TimeValue(neg, interval.getLeading(),
                                                interval.getRemaining(), 0, 0, 0, 0);
                                    case DAY:
                                        return new TimeValue(neg, 0, 0, interval.getLeading(), 0, 0,
                                                0);
                                    case HOUR:
                                        return new TimeValue(neg, 0, 0, 0, interval.getLeading(), 0,
                                                0);
                                    case MINUTE:
                                        return new TimeValue(neg, 0, 0, 0, 0, interval.getLeading(),
                                                0);
                                    case SECOND:
                                        return new TimeValue(neg, 0, 0, 0, 0, 0,
                                                interval.getLeading());
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

            // We pass the converter to the builder.
            final ResultSetDataWrapperBuilder builder =
                    ResultSetDataWrapper.builder("3", rs3).converter(converter);

            // Note that the wrapper accepts type hints to improve the presentation.
            // Without hint, the columns 2 (the `price`) and 3 (the `tax`) are floats.
            // Let the column 2 be a currency
            builder.typeValue(2, CellType.CURRENCY);
            // and the column 3 be a percentage.
            builder.typeValue(3, CellType.PERCENTAGE);

            // To be complete, we let the column 0 be a string. It's useless because `id` has the type `CHAR(12)` and is
            // already converted to a string.
            builder.typeValue(0, CellType.STRING);

            // Build the wrapper.
            final ResultSetDataWrapper wrapper = builder.build();

            // Now, skip another row, then write the result:
            walker.toRow(12);
            walker.addData(wrapper);
        } finally {
            conn.close();
        }
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "j_misc_rs.ods"));
    }

    /**
     * @throws IOException  if the file can't be written
     * @throws SQLException in something goes wrong with the local database
     */
    static void databaseExample() throws IOException, SQLException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("misc"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## Writing a Database to the Spreadsheet
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
            s.execute("CREATE TABLE document_repr (root TEXT, filename TEXT)");
            s.execute("INSERT INTO document_repr VALUES ('<office:document-content>', " +
                    "'content.xml'), ('<office:document-styles>', 'styles.xml'), " +
                    "('<office:document-meta>', 'meta.xml'), ('<office:document-settings>', " +
                    "'settings.xml')");
        } finally {
            conn.close();
        }

        // Then just use the raw exporter:

        DatabaseExporter.exportDatabase(dataSource, document);

        // There is a command line version of `DatabaseExporter`. See this class for more
        // information.
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "j_misc_db.ods"));
    }


    /**
     * @throws IOException  if the file can't be written
     * @throws SQLException in something goes wrong with the local database
     */
    static void freezeCellExample() throws IOException, SQLException {
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("misc"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("rs");
        final TableCellWalker walker = table.getWalker();
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## LO features
        // ### Freeze cells
        // Let's create some content:
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
        //
        // It's easy to freeze the first row:
        //
        document.freezeCells(table, 1, 0);
        //
        // ### Other features
        // If you know what you are doing, you can play with LO settings, for instance:
        table.updateConfigItem(ConfigElement.ZOOM_VALUE, "150");
        //
        // You can discover the configuration attributes in the `ConfigElement` enum.
        //
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "j_misc_features.ods"));
    }

    /**
     * @throws IOException if the file can't be written
     */
    static void customAttributeExample() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## A custom attribute
        // FastODS can't provide support for every conceivable attribute. But you can add easily a
        // custom attribute to a cell.
        //
        // First, you may need to add a namespace to the `content.xml` file:

        final Map<String, String> namespaceByPrefix = new HashMap<String, String>();
        namespaceByPrefix.put("xmlns:myns", "http://myns.xyz/my/namespace");
        final OdsFactory odsFactory = OdsFactory.builder(Logger.getLogger("misc"), Locale.US)
                .addNamespaceByPrefix(namespaceByPrefix).build();

        // Now, create the document as usual:

        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("custom");

        // And add a cell.
        final TableCellWalker walker = table.getWalker();
        final TextStyle sourceStyle = TextStyle.builder("source").fontName(
                LOFonts.LIBERATION_MONO).build();
        walker.setText(Text.builder().par().span("Check custom attributes in the ")
                .styledSpan("content.xml", sourceStyle).span(" file!").build());

        // Then you can add a custom attribute to the `table:table`, `table:table-row`,
        // `table:table-column` or `table:table-cell` tags.
        table.setAttribute("myns:my-table-attr", "my-table-value");
        walker.setRowAttribute("myns:my-row-attr", "my-row-value");
        walker.setColumnAttribute("myns:my-col-attr", "my-col-value");
        walker.setAttribute("myns:my-cell-attr", "my-cell-value");
        //
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "j_misc_custom_attr.ods"));
    }

    /**
     * @throws IOException if the file can't be written
     */
    static void customCell() throws IOException {
        final Map<String, String> namespaceByPrefix = new HashMap<String, String>();
        namespaceByPrefix.put("xmlns:myns", "http://myns.xyz/my/namespace");
        final OdsFactory odsFactory = OdsFactory.builder(Logger.getLogger("misc"), Locale.US)
                .addNamespaceByPrefix(namespaceByPrefix).build();
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("custom");
        final TableCellWalker walker = table.getWalker();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        // ## A custom cell (experimented users only)
        // Writing a custom cell may be interesting. All you have to do is to derive
        // `AbstractTableCell` and provide the implementation of `appendXMLToTableRow`:
        //
        walker.set(new AbstractTableCell() {
            @Override
            public void appendXMLToTableRow(final XMLUtil util, final Appendable appendable)
                    throws IOException {
                appendable.append("<table:table-cell");
                util.appendAttribute(appendable, "office:value-type", "string");
                util.appendAttribute(appendable, "office:string-value", "A CUSTOM CELL");
                appendable.append("/>");
            }
        });
        //
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "j_misc_custom_cell.ods"));
    }


    /**
     * @throws IOException if the file can't be written
     */
    static void namedSpacesExample() throws IOException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        //
        // The previous example might not be really impressive, because the attributes should be
        // part of the declared namespaces (`xmlns:office`, `xmlns:ooo`, ...). If you want to
        // use an external namespace, you have to declare it in the `OdsFactory`:
        //
        final Map<String, String> namespaceByPrefix = new HashMap<String, String>();
        namespaceByPrefix.put("xmlns:myns", "http://myns.xyz/my/namespace");
        final OdsFactory odsFactory = OdsFactory.builder(Logger.getLogger("misc"), Locale.US)
                .addNamespaceByPrefix(namespaceByPrefix).build();

        // Now, create the writer, the document, the table and the walker as usual:

        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("rs");
        final TableCellWalker walker = table.getWalker();

        // And add the attribute:

        walker.set(new AbstractTableCell() {
            @Override
            public void appendXMLToTableRow(final XMLUtil util, final Appendable appendable)
                    throws IOException {
                appendable.append("<table:table-cell");
                util.appendAttribute(appendable, "office:value-type", "string");
                util.appendAttribute(appendable, "office:string-value", "A custom cell");
                util.appendAttribute(appendable, "myns:tag", "a tag value");
                appendable.append("/>");
            }
        });

        walker.nextRow();
        // The generated XML will be inserted as a child of the current `table:row` tag, hence you
        // are not limited to one cell:

        walker.set(new AbstractTableCell() {
            @Override
            public void appendXMLToTableRow(final XMLUtil util, final Appendable appendable)
                    throws IOException {
                appendable.append("<table:table-cell");
                util.appendAttribute(appendable, "office:value-type", "string");
                util.appendAttribute(appendable, "office:string-value", "First custom cell");
                util.appendAttribute(appendable, "myns:tag", "a tag value");
                appendable.append("/>");
                appendable.append("<table:table-cell");
                util.appendAttribute(appendable, "office:value-type", "string");
                util.appendAttribute(appendable, "office:string-value", "Second custom cell");
                util.appendAttribute(appendable, "myns:another-tag", "another tag value");
                appendable.append("/>");
            }
        });
        //
        // Two remarks:
        //
        // 1. Use this with caution: if the XML is corrupted, your document will be unreadable.
        // 2. If you need something that might interest other users, please [fill an issue
        // ](https://github.com/jferard/fastods/issues/new) or propose a pull request.
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "j_misc_custom_cell2.ods"));
    }

    /**
     * @throws IOException if the file can't be written
     */
    static void validationExample() throws IOException {
        // As usual:
        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("misc"), Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        //
        // # Validate the content of a cell
        // When it comes to user input, it's usually a good idea to check the validity of
        // the data.
        //
        // In LibreOffice, we are accustomed to drop down lists, but the validation of
        // OpenDocument may involve more complex formulas. Currently, FastODS implements
        // drop down lists of "hard" values, that is values that are not part of the document
        // but manually created.
        //
        // Create the table:
        final Table table = document.addTable("validation");

        // Here's our validation:
        final Validation validation =
                Validation.builder("val1").listCondition(Arrays.asList("foo", "bar", "baz"))
                        .build();

        // And how to add the validation to a cell.
        final TableCellWalker walker = table.getWalker();
        walker.setStringValue("A value");
        walker.next();
        walker.setValidation(validation);

        // That's all.

        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        // And save the file.
        writer.saveAs(new File("generated_files", "j_misc_validation.ods"));
    }
}
