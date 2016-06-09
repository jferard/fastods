package com.github.jferard.fastods;

import org.junit.Test;

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
		rs.addColumn("chiffre");
		resultSetHandler.prepareGlobalResultSet(rs);
		rs.addRow(new Integer[] { new Integer(13) });

		OdsFile file = OdsFile.create("7columns.ods");
		final Table table = file.addTable("test", 50, 5);
		FastOdsXMLEscaper xmlEscaper = new FastOdsXMLEscaper();
		XMLUtil xmlUtil = new XMLUtil(xmlEscaper);
		
		DataWrapper data = new ResultSetDataWrapper(rs, 100);
		
		table.addData(data);
		file.save();
	}
}
