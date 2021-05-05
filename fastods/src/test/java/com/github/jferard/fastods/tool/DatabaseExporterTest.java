/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.TableCellWalker;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Logger;

public class DatabaseExporterTest {
    @Test
    public void testExportDatabase() throws SQLException, IOException {
        final DataSource source = PowerMock.createMock(DataSource.class);
        final Connection connection = PowerMock.createMock(Connection.class);
        final DatabaseMetaData metaData = PowerMock.createMock(DatabaseMetaData.class);
        final ResultSet rs = PowerMock.createMock(ResultSet.class);
        final ResultSet rsTable1 = PowerMock.createMock(ResultSet.class);
        final ResultSet rsTable2 = PowerMock.createMock(ResultSet.class);
        final Statement statement = PowerMock.createMock(Statement.class);

        final OdsDocument document = PowerMock.createMock(OdsDocument.class);
        final Table table1 = PowerMock.createMock(Table.class);
        final Table table2 = PowerMock.createMock(Table.class);
        final TableCellWalker walker1 = PowerMock.createMock(TableCellWalker.class);
        final TableCellWalker walker2 = PowerMock.createMock(TableCellWalker.class);

        PowerMock.resetAll();
        EasyMock.expect(source.getConnection()).andReturn(connection);
        EasyMock.expect(connection.getMetaData()).andReturn(metaData);
        EasyMock.expect(metaData.getTables(null, "", null, null)).andReturn(rs);
        EasyMock.expect(rs.next()).andReturn(true).times(2);
        EasyMock.expect(rs.getString(3)).andReturn("table1");
        EasyMock.expect(rs.getString(3)).andReturn("table2");
        EasyMock.expect(rs.next()).andReturn(false);
        EasyMock.expect(connection.createStatement()).andReturn(statement);
        EasyMock.expect(statement.executeQuery("SELECT * FROM table1")).andReturn(rsTable1);
        EasyMock.expect(statement.executeQuery("SELECT * FROM table2")).andReturn(rsTable2);

        EasyMock.expect(document.addTable("table1")).andReturn(table1);
        EasyMock.expect(document.addTable("table2")).andReturn(table2);
        EasyMock.expect(table1.getWalker()).andReturn(walker1);
        EasyMock.expect(table2.getWalker()).andReturn(walker2);

        walker1.addData(EasyMock.anyObject(ResultSetDataWrapper.class));
        walker2.addData(EasyMock.anyObject(ResultSetDataWrapper.class));

        connection.close();

        PowerMock.replayAll();

        DatabaseExporter.exportDatabase(source, document);

        PowerMock.verifyAll();
    }

}