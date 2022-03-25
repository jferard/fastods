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

import com.github.jferard.fastods.DataWrapper;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.FloatValue;
import com.github.jferard.fastods.ObjectToCellValueConverter;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.PercentageValue;
import com.github.jferard.fastods.StringValue;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.ToCellValueConverter;
import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.ResultSetTester;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.easymock.EasyMock;
import org.junit.Assert;
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
    private TableCellWalker walker;
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
        this.walker = PowerMock.createMock(TableCellWalker.class);
    }

    @Test
    @SuppressWarnings("deprecated")
    public final void testMax() throws IOException {
        final Collection<List<Object>> r = new ArrayList<List<Object>>();
        for (int v = FROM; v < TO; v++) {
            final List<Object> l = Collections.<Object>singletonList(v);
            r.add(l);
        }

        final DataWrapper wrapper = this.createWrapper(Collections.singletonList("number"), r, 3,
                false);

        PowerMock.resetAll();
        EasyMock.expect(this.walker.rowIndex()).andReturn(0);
        EasyMock.expect(this.walker.colIndex()).andReturn(10);
        // header row
        this.walker.setStringValue("number");
        this.walker.setStyle(this.tcls);
        this.walker.next();

        // data rows
        for (int v = FROM; v < Math.min(FROM + 3, TO); v++) {
            this.walker.nextRow();
            this.walker.to(10);
            this.walker.setCellValue(this.converter.from(v));
            this.walker.next();
        }

        // data row 4 is replaced by the number of rows remaining
        this.walker.nextRow();
        this.walker.to(10);
        this.walker.setStringValue("... (2 rows remaining)");
        this.walker.next();
        this.walker.nextRow();

        PowerMock.replayAll();
        wrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public final void testMetaDataException() throws SQLException, IOException {
        final ResultSet rs = PowerMock.createMock(ResultSet.class);
        final ResultSetDataWrapper wrapper =
                ResultSetDataWrapper.builder("range", rs).logger(this.logger).headerStyle(this.tcls)
                        .max(100).noAutoFilter().build();
        final SQLException e = new SQLException();

        PowerMock.resetAll();
        EasyMock.expect(rs.getMetaData()).andThrow(e);
        this.logger.log(EasyMock.eq(Level.SEVERE), EasyMock.anyString(), EasyMock.eq(e));

        PowerMock.replayAll();

        Assert.assertThrows(RuntimeException.class, () -> wrapper.addToTable(this.walker));

        PowerMock.verifyAll();
    }

    @Test
    public final void testNoRow() throws IOException {
        final DataWrapper wrapper = this.createWrapper(Arrays.asList("number", "word"),
                Collections.<List<Object>>emptyList(), 100, false);

        PowerMock.resetAll();
        EasyMock.expect(this.walker.rowIndex()).andReturn(0);
        EasyMock.expect(this.walker.colIndex()).andReturn(0);
        this.walker.setStringValue("number");
        this.walker.setStyle(this.tcls);
        this.walker.next();
        this.walker.setStringValue("word");
        this.walker.setStyle(this.tcls);
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);

        // empty row
        this.walker.setStringValue("");
        this.walker.next();
        this.walker.setStringValue("");
        this.walker.next();
        this.walker.nextRow();

        PowerMock.replayAll();
        wrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public final void testNullValue() throws IOException {
        final List<Object> l = new ArrayList<Object>(1);
        l.add(null);
        final DataWrapper wrapper =
                this.createWrapper(Collections.singletonList("value"), Collections.singletonList(l),
                        100, false);

        PowerMock.resetAll();
        EasyMock.expect(this.walker.rowIndex()).andReturn(0);
        EasyMock.expect(this.walker.colIndex()).andReturn(0);
        // header
        this.walker.setStringValue("value");
        this.walker.setStyle(this.tcls);
        this.walker.next();

        // data row
        this.walker.nextRow();
        this.walker.to(0);
        this.walker.setCellValue(new StringValue("<NULL>"));
        this.walker.next();
        this.walker.nextRow();

        PowerMock.replayAll();
        wrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public final void testOneRow() throws IOException {
        final DataWrapper wrapper = this.createWrapper(Arrays.asList("number", "word"),
                Collections.singletonList(Arrays.<Object>asList(7, "a")), 100, true);
        final Table table = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(this.walker.rowIndex()).andReturn(0).times(2);
        EasyMock.expect(this.walker.colIndex()).andReturn(0);

        // header
        this.walker.setStringValue("number");
        this.walker.setStyle(this.tcls);
        this.walker.next();
        this.walker.setStringValue("word");
        this.walker.setStyle(this.tcls);
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);

        // data row
        this.walker.setCellValue(this.converter.from(7));
        this.walker.next();
        this.walker.setCellValue(this.converter.from("a"));
        this.walker.next();
        this.walker.nextRow();

        EasyMock.expect(this.walker.getTable()).andReturn(table);
        table.addAutoFilter("range", 0, 0, 0, 1);

        PowerMock.replayAll();
        wrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testRealDataSets() throws IOException, FastOdsException {
        final Logger logger = PowerMock.createNiceMock(Logger.class);
        final MockResultSet rs = this.tester
                .createResultSet(Arrays.asList("number", "word", "code"),
                        Arrays.asList(Arrays.<Object>asList(13, "a", "13a"),
                                Arrays.<Object>asList(14, "b", "14b"),
                                Arrays.<Object>asList(15, "c", "15c")));
        final ResultSetDataWrapper wrapper =
                ResultSetDataWrapper.builder("range", rs).logger(this.logger).noAutoFilter()
                        .build();

        PowerMock.resetAll();
        EasyMock.expect(this.walker.rowIndex()).andReturn(0);
        EasyMock.expect(this.walker.colIndex()).andReturn(0);

        this.walker.setStringValue("number");
        this.walker.setStyle(EasyMock.isA(TableCellStyle.class));
        this.walker.next();
        this.walker.setStringValue("word");
        this.walker.setStyle(EasyMock.isA(TableCellStyle.class));
        this.walker.next();
        this.walker.setStringValue("code");
        this.walker.setStyle(EasyMock.isA(TableCellStyle.class));
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);
        this.walker.setCellValue(FloatValue.from(13));
        this.walker.next();
        this.walker.setCellValue(StringValue.from("a"));
        this.walker.next();
        this.walker.setCellValue(StringValue.from("13a"));
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);
        this.walker.setCellValue(FloatValue.from(14));
        this.walker.next();
        this.walker.setCellValue(StringValue.from("b"));
        this.walker.next();
        this.walker.setCellValue(StringValue.from("14b"));
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);
        this.walker.setCellValue(FloatValue.from(15));
        this.walker.next();
        this.walker.setCellValue(StringValue.from("c"));
        this.walker.next();
        this.walker.setCellValue(StringValue.from("15c"));
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);
        this.walker.setStringValue("");
        this.walker.next();
        this.walker.setStringValue("");
        this.walker.next();
        this.walker.setStringValue("");
        this.walker.next();
        this.walker.nextRow();

        PowerMock.replayAll();
        wrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public final void testRSException() throws SQLException, IOException {
        final ResultSet rs = PowerMock.createMock(ResultSet.class);
        final ResultSetDataWrapper wrapper =
                ResultSetDataWrapper.builder("range", rs).logger(this.logger).headerStyle(this.tcls)
                        .max(100).noAutoFilter().build();
        final SQLException e = new SQLException();
        final ResultSetMetaData metaData = PowerMock.createMock(ResultSetMetaData.class);

        PowerMock.resetAll();
        EasyMock.expect(this.walker.rowIndex()).andReturn(0);
        EasyMock.expect(this.walker.colIndex()).andReturn(0);
        EasyMock.expect(rs.getMetaData()).andReturn(metaData);
        EasyMock.expect(metaData.getColumnCount()).andReturn(0).anyTimes();

        EasyMock.expect(rs.next()).andThrow(e);
        this.logger.log(EasyMock.eq(Level.SEVERE), EasyMock.anyString(), EasyMock.eq(e));

        PowerMock.replayAll();
        Assert.assertThrows(RuntimeException.class, () -> wrapper.addToTable(this.walker));

        PowerMock.verifyAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testWrapperWithColumnHints()
            throws IOException, FastOdsException {
        final Logger logger = PowerMock.createNiceMock(Logger.class);
        final MockResultSet rs = this.tester
                .createResultSet(Arrays.asList("number", "word", "code"),
                        Arrays.asList(Arrays.<Object>asList(0.13, "a", "13a")));
        final ResultSetDataWrapper wrapper =
                ResultSetDataWrapper.builder("range", rs).logger(this.logger).noAutoFilter()
                        .typeValue(0, CellType.PERCENTAGE).build();

        PowerMock.resetAll();
        EasyMock.expect(this.walker.rowIndex()).andReturn(0);
        EasyMock.expect(this.walker.colIndex()).andReturn(0);

        this.walker.setStringValue("number");
        this.walker.setStyle(EasyMock.isA(TableCellStyle.class));
        this.walker.next();
        this.walker.setStringValue("word");
        this.walker.setStyle(EasyMock.isA(TableCellStyle.class));
        this.walker.next();
        this.walker.setStringValue("code");
        this.walker.setStyle(EasyMock.isA(TableCellStyle.class));
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);
        this.walker.setCellValue(PercentageValue.from(0.13));
        this.walker.next();
        this.walker.setCellValue(StringValue.from("a"));
        this.walker.next();
        this.walker.setCellValue(StringValue.from("13a"));
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);
        this.walker.setStringValue("");
        this.walker.next();
        this.walker.setStringValue("");
        this.walker.next();
        this.walker.setStringValue("");
        this.walker.next();
        this.walker.nextRow();

        PowerMock.replayAll();
        wrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    private DataWrapper createWrapper(final Iterable<String> head,
                                      final Iterable<List<Object>> rows, final int max,
                                      final boolean autofilter) {
        final MockResultSet rs = this.tester.createResultSet(head, rows);
        final ResultSetDataWrapperBuilder builder =
                ResultSetDataWrapper.builder("range", rs).logger(this.logger).headerStyle(this.tcls)
                        .max(max);
        if (!autofilter) {
            builder.noAutoFilter();
        }
        return builder.build();
    }
}
