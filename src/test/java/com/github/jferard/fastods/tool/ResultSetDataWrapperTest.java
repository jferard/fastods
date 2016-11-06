package com.github.jferard.fastods.tool;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.DataWrapper;
import com.github.jferard.fastods.HeavyTableRow;
import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.XMLUtil;
import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;

public class ResultSetDataWrapperTest extends BasicJDBCTestCaseAdapter {
	private Logger logger;
	private ResultSet rs;
	private Table table;
	private TableCellStyle tcls;
	private DataWrapper wrapper;

	@Test
	public final void testMax() throws SQLException {
		final List<Object> l = Arrays.<Object> asList(13);
		this.setUpRS(Arrays.<String> asList("number"),
				Arrays.<List<Object>> asList(l, l, l, l, l), 3);
		final SQLException e = new SQLException();
		final HeavyTableRow row = PowerMock.createMock(HeavyTableRow.class);
		final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

		// first row
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(row.getWalker()).andReturn(w);
		w.lastCell();
		w.setStringValue("number");
		w.setStyle(this.tcls);

		// data row 1
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(row.getWalker()).andReturn(w);
		w.lastCell();
		w.setObjectValue(13);

		// data row 2
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(row.getWalker()).andReturn(w);
		w.lastCell();
		w.setObjectValue(13);

		// data row 3
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(row.getWalker()).andReturn(w);
		w.lastCell();
		w.setObjectValue(13);

		// data row 4 is replaced by the number of rows remaining
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(row.getWalker()).andReturn(w);
		w.lastCell();
		w.setStringValue("... (2 rows remaining)");

		PowerMock.replayAll();
		this.wrapper.addToTable(this.table);
		PowerMock.verifyAll();
	}

	@Test
	public final void testMetaDataException() throws SQLException {
		this.setUpMocks();
		final SQLException e = new SQLException();
		EasyMock.expect(this.rs.getMetaData()).andThrow(e);
		this.logger.log(EasyMock.eq(Level.SEVERE), EasyMock.anyString(),
				EasyMock.eq(e));
		PowerMock.replayAll();
		this.wrapper.addToTable(this.table);
		PowerMock.verifyAll();
	}

	@Test
	public final void testNoRow() throws SQLException {
		this.setUpRS(Arrays.<String> asList("number", "word"),
				Arrays.<List<Object>> asList(), 100);
		final SQLException e = new SQLException();
		final HeavyTableRow row = PowerMock.createMock(HeavyTableRow.class);
		final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

		// first row
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(row.getWalker()).andReturn(w);
		w.lastCell();
		w.setStringValue("number");
		w.setStyle(this.tcls);
		w.lastCell();
		w.setStringValue("word");
		w.setStyle(this.tcls);

		// empty row
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(row.getWalker()).andReturn(w);
		w.lastCell();
		w.setStringValue("");
		w.lastCell();
		w.setStringValue("");

		PowerMock.replayAll();
		this.wrapper.addToTable(this.table);
		PowerMock.verifyAll();
	}

	@Test
	public final void testNullValue() throws SQLException {
		final List<Object> l = new ArrayList<Object>(1);
		l.add(null);
		this.setUpRS(Arrays.<String> asList("value"),
				Arrays.<List<Object>> asList(l), 100);
		final SQLException e = new SQLException();
		final HeavyTableRow row = PowerMock.createMock(HeavyTableRow.class);
		final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

		// first row
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(row.getWalker()).andReturn(w);
		w.lastCell();
		w.setStringValue("value");
		w.setStyle(this.tcls);

		// second row
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(row.getWalker()).andReturn(w);
		w.lastCell();
		w.setStringValue("<NULL>");

		PowerMock.replayAll();
		this.wrapper.addToTable(this.table);
		PowerMock.verifyAll();
	}

	@Test
	public final void testOneRow() throws SQLException {
		this.setUpRS(Arrays.<String> asList("number", "word"),
				Arrays.<List<Object>> asList(Arrays.<Object> asList(13, "a")),
				100);
		final SQLException e = new SQLException();
		final HeavyTableRow row = PowerMock.createMock(HeavyTableRow.class);
		final TableCellWalker w = PowerMock.createMock(TableCellWalker.class);

		// first row
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(row.getWalker()).andReturn(w);
		w.lastCell();
		w.setStringValue("number");
		w.setStyle(this.tcls);
		w.lastCell();
		w.setStringValue("word");
		w.setStyle(this.tcls);

		// second row
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(row.getWalker()).andReturn(w);
		w.lastCell();
		w.setObjectValue(13);
		w.lastCell();
		w.setObjectValue("a");

		PowerMock.replayAll();
		this.wrapper.addToTable(this.table);
		PowerMock.verifyAll();
	}

	@Test
	public final void testRealDataSets() {
		final Logger logger = PowerMock.createNiceMock(Logger.class);
		final MockConnection connection = this.getJDBCMockObjectFactory()
				.getMockConnection();
		final StatementResultSetHandler resultSetHandler = connection
				.getStatementResultSetHandler();
		final MockResultSet rs = this.createResultSet(resultSetHandler,
				Arrays.<String> asList("number", "word", "code"),
				Arrays.<List<Object>> asList(
						Arrays.<Object> asList(13, "a", "13a"),
						Arrays.<Object> asList(14, "b", "14b"),
						Arrays.<Object> asList(15, "c", "15c")));
		final MockResultSet rs2 = this.createResultSet(resultSetHandler,
				Arrays.<String> asList("number", "word", "code"),
				Arrays.<List<Object>> asList(
						Arrays.<Object> asList(13, "a", "13a"),
						Arrays.<Object> asList(14, "b", "14b"),
						Arrays.<Object> asList(15, "c", "15c")));

		final OdsFile file = OdsFile.create("7columns.ods");
		final Table table = file.addTable("test", 50, 5);
		final XMLUtil xmlUtil = FastOds.getXMLUtil();
		final TableCellStyle tcls = TableCellStyle.builder("rs-head")
				.backgroundColor("#dddddd").fontWeightBold().build();
		final DataWrapper data = new ResultSetDataWrapper(logger, rs, tcls,
				100);
		final DataWrapper data2 = new ResultSetDataWrapper(logger, rs2, tcls,
				100);

		table.addData(data);
		table.nextRow();
		table.addData(data2);
		file.save();
	}

	@Test
	public final void testRSException() throws SQLException {
		this.setUpMocks();
		final SQLException e = new SQLException();
		final ResultSetMetaData metaData = PowerMock
				.createMock(ResultSetMetaData.class);
		final HeavyTableRow row = PowerMock.createMock(HeavyTableRow.class);

		EasyMock.expect(this.rs.getMetaData()).andReturn(metaData);
		EasyMock.expect(this.table.nextRow()).andReturn(row);
		EasyMock.expect(metaData.getColumnCount()).andReturn(0).anyTimes();
		EasyMock.expect(row.getWalker()).andReturn(null);

		EasyMock.expect(this.rs.next()).andThrow(e);
		this.logger.log(EasyMock.eq(Level.SEVERE), EasyMock.anyString(),
				EasyMock.eq(e));
		PowerMock.replayAll();
		this.wrapper.addToTable(this.table);
		PowerMock.verifyAll();
	}

	private MockResultSet createResultSet(
			final StatementResultSetHandler resultSetHandler,
			final List<String> head, final List<List<Object>> rows) {
		final MockResultSet rs = resultSetHandler.createResultSet();
		for (final String name : head)
			rs.addColumn(name);

		resultSetHandler.prepareGlobalResultSet(rs);
		for (final List<Object> row : rows)
			rs.addRow(row);
		return rs;
	}

	private final void setUpMocks() {
		this.logger = PowerMock.createMock(Logger.class);
		this.rs = PowerMock.createMock(ResultSet.class);
		this.tcls = PowerMock.createNiceMock(TableCellStyle.class);
		this.wrapper = new ResultSetDataWrapper(this.logger, this.rs, this.tcls,
				100);
		this.table = PowerMock.createMock(Table.class);
	}

	private final void setUpRS(final List<String> head,
			final List<List<Object>> rows, final int max) {
		this.logger = PowerMock.createMock(Logger.class);
		final MockConnection connection = this.getJDBCMockObjectFactory()
				.getMockConnection();
		final StatementResultSetHandler resultSetHandler = connection
				.getStatementResultSetHandler();
		this.rs = this.createResultSet(resultSetHandler, head, rows);
		this.tcls = PowerMock.createNiceMock(TableCellStyle.class);
		this.wrapper = new ResultSetDataWrapper(this.logger, this.rs, this.tcls,
				max);
		this.table = PowerMock.createMock(Table.class);
	}
}
