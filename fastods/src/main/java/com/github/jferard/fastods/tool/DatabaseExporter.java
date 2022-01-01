/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * A tool to export databases.
 */
public class DatabaseExporter {
    /**
     * Usage:
     * <p>
     * $ java -cp path/to/fastods/jar:path/to/jdbc/driver/jar [connection string] [target.ods]
     * <p>
     * Example:
     * <p>
     * $ java -cp $HOME/.m2/repository/com/github/jferard/fastods/0.8.1/fastods-0.8.1.jar:$HOME/.m2/repository/org/xerial/sqlite-jdbc/3.27.2.1/sqlite-jdbc-3.27.2.1.jar com.github.jferard.fastods.tool.DatabaseExporter "jdbc:sqlite:$HOME/test.sqlite3" "$HOME/export.ods"
     * <p>
     * Please avoid exporting huge databases: LibreOffice won't open huge documents.
     *
     * @param args the parameters: connection string and target
     * @throws SQLException if a SQL exception occurs
     * @throws IOException  if an I/O error occurs
     */
    public static void main(final String[] args) throws SQLException, IOException {
        if (args.length != 2) {
            System.err.println(
                    "Usage: java -cp path/to/fastods/jar:path/to/jdbc/driver/jar <connection string> <target.ods>");
        }
        final String connectionString = args[0];
        final String documentName = args[1];
        final Connection connection = DriverManager.getConnection(connectionString);
        try {
            final OdsFactory odsFactory =
                    OdsFactory.create(Logger.getLogger("database"), Locale.US);
            final AnonymousOdsFileWriter writer = odsFactory.createWriter();
            try {
                final OdsDocument document = writer.document();
                DatabaseExporter.exportDatabase(connection, document);
            } finally {
                writer.saveAs(documentName);
            }
        } finally {
            connection.close();
        }

    }

    /**
     * Create a sheet per table, and write the data.
     *
     * @param dataSource the data source
     * @param document   the document
     * @throws SQLException if there is a SQL exception
     * @throws IOException  if there is an I/O exception
     */
    public static void exportDatabase(final DataSource dataSource, final OdsDocument document)
            throws SQLException, IOException {
        final Connection conn = dataSource.getConnection();
        try {
            exportDatabase(conn, document);
        } finally {
            conn.close();
        }
    }

    public static void exportDatabase(final Connection conn, final OdsDocument document)
            throws SQLException, IOException {
        final DatabaseMetaData metaData = conn.getMetaData();
        final ResultSet rs = metaData.getTables(null, "", null, null);
        final List<String> tableNames = new ArrayList<String>();
        while (rs.next()) {
            tableNames.add(rs.getString(3));
        }

        final Statement statement = conn.createStatement();
        for (final String tableName : tableNames) {
            final ResultSet tableRs = statement
                    .executeQuery("SELECT * FROM " + tableName);
            final Table table = document.addTable(tableName);
            final ResultSetDataWrapper wrapper =
                    ResultSetDataWrapper.builder(tableName, tableRs).build();
            table.getWalker().addData(wrapper);
        }
    }
}
