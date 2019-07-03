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

import com.github.jferard.fastods.DataWrapper;
import com.github.jferard.fastods.ObjectToCellValueConverter;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.StringValue;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRow;
import com.github.jferard.fastods.ToCellValueConverter;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.ResultSetTester;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.easymock.EasyMock;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultSetDataWrapperTest {
    private static final int FROM = 13;
    private static final int TO = 18;

    @BeforeClass
    public static void beforeClass() {
        final File generated_files = new File("generated_files");
        if (generated_files.exists()) {
            return;
        }

        assert generated_files.mkdir();
    }

    private Logger logger;
    private OdsFactory odsFactory;
    private Table table;
    private TableCellStyle tcls;
    private ToCellValueConverter converter;
    private ResultSetTester tester;

    @Before
    public final void setUp() {
        this.converter = new ObjectToCellValueConverter("USD");
        this.odsFactory = OdsFactory.create(Logger.getLogger(""), Locale.US);
        this.tester = ResultSetTester.create();
        this.logger = PowerMock.createMock(Logger.class);
        this.tcls = PowerMock.createNiceMock(TableCellStyle.class);
        this.table = PowerMock.createMock(Table.class);
    }

    @Test
    @SuppressWarnings("deprecated")
    public final void testMax() throws IOException {
        final Collection<List<Object>> r = new ArrayList<List<Object>>();
        for (int v = FROM; v < TO; v++) {
            final List<Object> l = Collections.<Object>singletonList(v);
            r.add(l);
        }

        final DataWrapper wrapper = this.createWrapper(Collections.singletonList("number"), r, 3);
        final TableRow row = PowerMock.createMock(TableRow.class);
        final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

        PowerMock.resetAll();
        // header row
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setStringValue("number");
        w.setStyle(this.tcls);

        // data rows
        for (int v = FROM; v < Math.min(FROM + 3, TO); v++) {
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
        wrapper.addToTable(this.table);

        PowerMock.verifyAll();
    }

    @Test(expected = RuntimeException.class)
    public final void testMetaDataException() throws SQLException, IOException {
        final ResultSet rs = PowerMock.createMock(ResultSet.class);
        final ResultSetDataWrapper wrapper = ResultSetDataWrapper.builder(rs).logger(this.logger)
                .headStyle(this.tcls).max(100).noAutoFilter().build();
        final SQLException e = new SQLException();

        PowerMock.resetAll();
        EasyMock.expect(rs.getMetaData()).andThrow(e);
        this.logger.log(EasyMock.eq(Level.SEVERE), EasyMock.anyString(), EasyMock.eq(e));

        PowerMock.replayAll();
        wrapper.addToTable(this.table);

        PowerMock.verifyAll();
    }

    @Test
    public final void testNoRow() throws IOException {
        final DataWrapper wrapper = this.createWrapper(Arrays.asList("number", "word"),
                Collections.<List<Object>>emptyList(), 100);
        final TableRow row = PowerMock.createMock(TableRow.class);
        final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

        PowerMock.resetAll();
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
        wrapper.addToTable(this.table);

        PowerMock.verifyAll();
    }

    @Test
    public final void testNullValue() throws IOException {
        final List<Object> l = new ArrayList<Object>(1);
        l.add(null);
        final DataWrapper wrapper = this
                .createWrapper(Collections.singletonList("value"), Collections.singletonList(l),
                        100);
        final TableRow row = PowerMock.createMock(TableRow.class);
        final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

        PowerMock.resetAll();
        // header
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setStringValue("value");
        w.setStyle(this.tcls);

        // data row
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setCellValue(new StringValue("<NULL>"));

        PowerMock.replayAll();
        wrapper.addToTable(this.table);

        PowerMock.verifyAll();
    }

    @Test
    public final void testOneRow() throws IOException {
        final DataWrapper wrapper = this.createWrapper(Arrays.asList("number", "word"),
                Collections.singletonList(Arrays.<Object>asList(7, "a")), 100);
        final TableRow row = PowerMock.createMock(TableRow.class);
        final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

        PowerMock.resetAll();
        // header
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setStringValue("number");
        w.setStyle(this.tcls);
        w.next();
        w.setStringValue("word");
        w.setStyle(this.tcls);

        // data row
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(row.getWalker()).andReturn(w);
        w.next();
        w.setCellValue(this.converter.from(7));
        w.next();
        w.setCellValue(this.converter.from("a"));

        PowerMock.replayAll();
        wrapper.addToTable(this.table);

        PowerMock.verifyAll();
    }

    /*
    @Test
    public final void testRealDataSets() throws IOException {
        final Logger logger = PowerMock.createNiceMock(Logger.class);
        final MockResultSet rs = this.tester
                .createResultSet(Arrays.asList("number", "word", "code"),
                        Arrays.asList(Arrays.<Object>asList(13, "a", "13a"),
                                Arrays.<Object>asList(14, "b", "14b"),
                                Arrays.<Object>asList(15, "c", "15c")));
        final MockResultSet rs2 = this.tester
                .createResultSet(Arrays.asList("number", "word", "code"),
                        Arrays.asList(Arrays.<Object>asList(13, "a", "13a"),
                                Arrays.<Object>asList(14, "b", "14b"),
                                Arrays.<Object>asList(15, "c", "15c")));

        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("test", 50, 5);
        final TableCellStyle tcls = TableCellStyle.builder("rs-head")
                .backgroundColor(ColorHelper.fromString("#dddddd")).fontWeightBold().build();
        final DataWrapper data = ResultSetDataWrapper.builder(rs).logger(logger).headStyle(tcls)
                .max(100).build();
        final DataWrapper data2 = ResultSetDataWrapper.builder(rs2).logger(logger).headStyle(tcls)
                .max(100).build();

        table.addData(data);
        table.nextRow();
        table.addData(data2);
        writer.saveAs(new File("generated_files", "7columns.ods"));
        PowerMock.replayAll();
        PowerMock.verifyAll();
    }

     */

    @Test(expected = RuntimeException.class)
    public final void testRSException() throws SQLException, IOException {
        final ResultSet rs = PowerMock.createMock(ResultSet.class);
        final ResultSetDataWrapper wrapper = ResultSetDataWrapper.builder(rs).logger(this.logger)
                .headStyle(this.tcls).max(100).noAutoFilter().build();
        final SQLException e = new SQLException();
        final ResultSetMetaData metaData = PowerMock.createMock(ResultSetMetaData.class);
        final TableRow row = PowerMock.createMock(TableRow.class);

        PowerMock.resetAll();
        EasyMock.expect(rs.getMetaData()).andReturn(metaData);
        EasyMock.expect(this.table.nextRow()).andReturn(row);
        EasyMock.expect(metaData.getColumnCount()).andReturn(0).anyTimes();
        EasyMock.expect(row.getWalker()).andReturn(null);

        EasyMock.expect(rs.next()).andThrow(e);
        this.logger.log(EasyMock.eq(Level.SEVERE), EasyMock.anyString(), EasyMock.eq(e));

        PowerMock.replayAll();
        wrapper.addToTable(this.table);

        PowerMock.replayAll();
    }

    private DataWrapper createWrapper(final Iterable<String> head,
                                      final Iterable<List<Object>> rows, final int max) {
        final MockResultSet rs = this.tester.createResultSet(head, rows);
        return ResultSetDataWrapper.builder(rs).logger(this.logger).headStyle(this.tcls).max(max)
                .noAutoFilter().build();
    }
}
