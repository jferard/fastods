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
package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.CellValue;
import com.github.jferard.fastods.DataWrapper;
import com.github.jferard.fastods.ObjectToCellValueConverter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.StringValue;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;
import com.github.jferard.fastods.ToCellValueConverter;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.ColorHelper;
import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultSetDataWrapperTest extends BasicJDBCTestCaseAdapter {
    @BeforeClass
    public static void beforeClass() {
        final File generated_files = new File("generated_files");
        if (generated_files.exists()) return;

        generated_files.mkdir();
    }

    private static MockResultSet createResultSet(final StatementResultSetHandler resultSetHandler,
                                                 final List<String> head, final List<List<Object>> rows) {
        final MockResultSet rs = resultSetHandler.createResultSet();
        for (final String name : head)
            rs.addColumn(name);

        resultSetHandler.prepareGlobalResultSet(rs);
        for (final List<Object> row : rows)
            rs.addRow(row);
        return rs;
    }
    private Logger logger;
    private OdsFactory odsFactory;
    private ResultSet rs;
    private Table table;
    private TableCellStyle tcls;
    private DataWrapper wrapper;
    private ToCellValueConverter converter;

    @Override
    @Before
    public final void setUp() {
        this.converter = new ObjectToCellValueConverter("USD");
        this.odsFactory = OdsFactory.create(Logger.getLogger(""), Locale.US);
        PowerMock.resetAll();
    }

    @Override
    @After
    public final void tearDown() {
        PowerMock.verifyAll();
    }

    @Test
    @SuppressWarnings("deprecated")
    public final void testMax() throws IOException {
        final List<List<Object>> r = new ArrayList<List<Object>>();
        for (int v = 13; v < 18; v++) {
            final List<Object> l = Collections.<Object>singletonList(v);
            r.add(l);
        }

        this.setUpRS(Collections.singletonList("number"), r, 3);
        final TableRow row = PowerMock.createMock(TableRow.class);
        final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

        // REPLAY
        // first row
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setStringValue("number");
        w.setStyle(this.tcls);

        for (int v = 13; v < 16; v++) {
            // data row
            EasyMock.expect(this.table.nextRow()).andReturn(row);
            EasyMock.expect(row.getWalker()).andReturn(w);
            w.next();
            w.setCellValue(this.converter.from(v));
        }

        // data row 4 is replaced by the number of rows remaining
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setStringValue("... (2 rows remaining)");

        PowerMock.replayAll();
        this.wrapper.addToTable(this.table);
    }

    @Test(expected = RuntimeException.class)
    public final void testMetaDataException() throws SQLException, IOException {
        this.setUpMocks();
        final SQLException e = new SQLException();

        // play
        EasyMock.expect(this.rs.getMetaData()).andThrow(e);
        this.logger.log(EasyMock.eq(Level.SEVERE), EasyMock.anyString(), EasyMock.eq(e));

        PowerMock.replayAll();
        this.wrapper.addToTable(this.table);
    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testNoRow() throws IOException {
        this.setUpRS(Arrays.asList("number", "word"), Collections.<List<Object>>emptyList(), 100);
        final TableRow row = PowerMock.createMock(TableRow.class);
        final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

        // play
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setStringValue("number");
        w.setStyle(this.tcls);
        w.next();
        w.setStringValue("word");
        w.setStyle(this.tcls);

        // empty row
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setStringValue("");
        w.next();
        w.setStringValue("");

        PowerMock.replayAll();
        this.wrapper.addToTable(this.table);
    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testNullValue() throws IOException {
        final List<Object> l = new ArrayList<Object>(1);
        l.add(null);
        this.setUpRS(Collections.singletonList("value"), Collections.singletonList(l), 100);
        final TableRow row = PowerMock.createMock(TableRow.class);
        final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

        // play
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setStringValue("value");
        w.setStyle(this.tcls);

        // second row
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setCellValue(new StringValue("<NULL>"));

        PowerMock.replayAll();
        this.wrapper.addToTable(this.table);
    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testOneRow() throws IOException {
        this.setUpRS(Arrays.asList("number", "word"), Collections.singletonList(Arrays.<Object>asList(13, "a")), 100);
        final TableRow row = PowerMock.createMock(TableRow.class);
        final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

        // PLAY
        // first row
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setStringValue("number");
        w.setStyle(this.tcls);
        w.next();
        w.setStringValue("word");
        w.setStyle(this.tcls);

        // second row
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setCellValue(this.converter.from(13));
        w.next();
        w.setCellValue(this.converter.from("a"));

        PowerMock.replayAll();
        this.wrapper.addToTable(this.table);
    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testRealDataSets() throws IOException {
        final Logger logger = PowerMock.createNiceMock(Logger.class);
        final MockConnection connection = this.getJDBCMockObjectFactory().getMockConnection();
        final StatementResultSetHandler resultSetHandler = connection.getStatementResultSetHandler();
        final MockResultSet rs = ResultSetDataWrapperTest
                .createResultSet(resultSetHandler, Arrays.asList("number", "word", "code"),
                        Arrays.asList(Arrays.<Object>asList(13, "a", "13a"), Arrays.<Object>asList(14, "b", "14b"),
                                Arrays.<Object>asList(15, "c", "15c")));
        final MockResultSet rs2 = ResultSetDataWrapperTest
                .createResultSet(resultSetHandler, Arrays.asList("number", "word", "code"),
                        Arrays.asList(Arrays.<Object>asList(13, "a", "13a"), Arrays.<Object>asList(14, "b", "14b"),
                                Arrays.<Object>asList(15, "c", "15c")));

        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("test", 50, 5);
        final TableCellStyle tcls = TableCellStyle.builder("rs-head").backgroundColor(ColorHelper.fromString("#dddddd"))
                .fontWeightBold().build();
        final DataWrapper data = ResultSetDataWrapper.builder(rs).logger(logger).headStyle(tcls).max(100).build();
        final DataWrapper data2 = ResultSetDataWrapper.builder(rs2).logger(logger).headStyle(tcls).max(100).build();

        table.addData(data);
        table.nextRow();
        table.addData(data2);
        writer.saveAs(new File("generated_files", "7columns.ods"));
        PowerMock.replayAll();
    }

    @Test(expected =  RuntimeException.class)
    public final void testRSException() throws SQLException, IOException {
        this.setUpMocks();
        final SQLException e = new SQLException();
        final ResultSetMetaData metaData = PowerMock.createMock(ResultSetMetaData.class);
        final TableRow row = PowerMock.createMock(TableRow.class);

        EasyMock.expect(this.rs.getMetaData()).andReturn(metaData);
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(metaData.getColumnCount()).andReturn(0).anyTimes();
        EasyMock.expect(row.getWalker()).andReturn(null);

        EasyMock.expect(this.rs.next()).andThrow(e);
        this.logger.log(EasyMock.eq(Level.SEVERE), EasyMock.anyString(), EasyMock.eq(e));

        PowerMock.replayAll();
        this.wrapper.addToTable(this.table);
    }

    private void setUpMocks() {
        this.logger = PowerMock.createMock(Logger.class);
        this.rs = PowerMock.createMock(ResultSet.class);
        this.tcls = PowerMock.createNiceMock(TableCellStyle.class);
        this.wrapper = ResultSetDataWrapper.builder(this.rs).logger(this.logger).headStyle(this.tcls).max(100).noAutoFilter().build();
        this.table = PowerMock.createMock(Table.class);
    }

    private void setUpRS(final List<String> head, final List<List<Object>> rows, final int max) {
        this.logger = PowerMock.createMock(Logger.class);
        final MockConnection connection = this.getJDBCMockObjectFactory().getMockConnection();
        final StatementResultSetHandler resultSetHandler = connection.getStatementResultSetHandler();
        this.rs = ResultSetDataWrapperTest.createResultSet(resultSetHandler, head, rows);
        this.tcls = PowerMock.createNiceMock(TableCellStyle.class);
        this.wrapper = ResultSetDataWrapper.builder(this.rs).logger(this.logger).headStyle(this.tcls).max(max).noAutoFilter().build();
        this.table = PowerMock.createMock(Table.class);
    }
}
