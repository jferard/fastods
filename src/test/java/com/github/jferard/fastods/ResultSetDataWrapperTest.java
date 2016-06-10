package com.github.jferard.fastods;

import org.junit.Test;

import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;
import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;

public class ResultSetDataWrapperTest extends BasicJDBCTestCaseAdapter {

	@Test
	public final void test() {
		MockConnection connection = this.getJDBCMockObjectFactory()
				.getMockConnection();
		StatementResultSetHandler resultSetHandler = connection
				.getStatementResultSetHandler();
		MockResultSet rs = resultSetHandler.createResultSet();
		rs.addColumn("number");
		rs.addColumn("word");
		rs.addColumn("code");
		resultSetHandler.prepareGlobalResultSet(rs);
		rs.addRow(new Object[] { 13, "a", "13a" });
		rs.addRow(new Object[] { 14, "b", "13b" });
		rs.addRow(new Object[] { 15, "c", "13c" });

		OdsFile file = OdsFile.create("7columns.ods");
		final Table table = file.addTable("test", 50, 5);
		FastOdsXMLEscaper xmlEscaper = new FastOdsXMLEscaper();
		XMLUtil xmlUtil = new XMLUtil(xmlEscaper);
		
		TableCellStyle tcls = TableCellStyle.builder(xmlUtil, "cc")
				.backgroundColor("#dddddd").fontWeightBold().build();
		DataWrapper data = new ResultSetDataWrapper(rs, tcls, 100);
		
		table.addData(data);
		file.save();
	}
}
