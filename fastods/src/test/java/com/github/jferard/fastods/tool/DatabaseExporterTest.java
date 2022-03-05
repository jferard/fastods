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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.Permission;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Logger;

@RunWith(PowerMockRunner.class)
public class DatabaseExporterTest {
    @Test
    // See https://stackoverflow.com/questions/54055881/cant-mock-java-lang-systemexitint-method-with-powermock
    public void testMainExit() throws SQLException, IOException {
        final SecurityManager initialSecurityManager = System.getSecurityManager();
        try {
            System.setSecurityManager(new NoExitSecurityManager());

            DatabaseExporter.main(new String[] {"a", "b", "c"});
            Assert.fail("Should've thrown ExitException");
        } catch (final ExitException e) {
            Assert.assertEquals(e.status, -1);
        } finally {
            System.setSecurityManager(initialSecurityManager);
        }
    }

    @Test
    @PrepareForTest({DatabaseExporter.class, OdsFactory.class})
    public void testMain() throws SQLException, IOException {
        final Connection connection = PowerMock.createMock(Connection.class);
        final OdsFactory fact = PowerMock.createMock(OdsFactory.class);
        final AnonymousOdsFileWriter writer = PowerMock.createMock(AnonymousOdsFileWriter.class);
        final OdsDocument document = PowerMock.createMock(OdsDocument.class);
        final DatabaseMetaData metaData = PowerMock.createMock(DatabaseMetaData.class);
        final ResultSet rs = PowerMock.createMock(ResultSet.class);
        final ResultSet rsTable1 = PowerMock.createMock(ResultSet.class);
        final ResultSet rsTable2 = PowerMock.createMock(ResultSet.class);
        final Statement statement = PowerMock.createMock(Statement.class);

        final Table table1 = PowerMock.createMock(Table.class);
        final Table table2 = PowerMock.createMock(Table.class);
        final TableCellWalker walker1 = PowerMock.createMock(TableCellWalker.class);
        final TableCellWalker walker2 = PowerMock.createMock(TableCellWalker.class);
        PowerMock.mockStatic(DriverManager.class);
        PowerMock.mockStatic(OdsFactory.class);

        PowerMock.resetAll();

        EasyMock.expect(DriverManager.getConnection("a")).andReturn(connection);
        EasyMock.expect(OdsFactory.create(EasyMock.isA(Logger.class), EasyMock.isA(Locale.class))).andReturn(fact);
        EasyMock.expect(fact.createWriter()).andReturn(writer);
        EasyMock.expect(writer.document()).andReturn(document);
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

        writer.saveAs("b");
        connection.close();

        PowerMock.replayAll();
        DatabaseExporter.main(new String[] {"a", "b"});

        PowerMock.verifyAll();
    }

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

    // exception to be thrown by security manager when System.exit is called
    public static class ExitException extends SecurityException {
        public final int status;

        public ExitException(final int status) {
            this.status = status;
        }
    }

    // custom security manager
    public static class NoExitSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(final Permission perm) {
        }

        @Override
        public void checkPermission(final Permission perm, final Object context) {
        }

        @Override
        public void checkExit(final int status) {
            super.checkExit(status);
            throw new ExitException(status);
        }
    }
}