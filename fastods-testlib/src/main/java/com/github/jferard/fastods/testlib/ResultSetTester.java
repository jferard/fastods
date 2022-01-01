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

package com.github.jferard.fastods.testlib;

import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;

import java.util.List;

/**
 * A test for ResultSets
 *
 * @author J. Férard
 */
public class ResultSetTester {
    /**
     * @return a new ResultSetTester
     */
    public static ResultSetTester create() {
        final MockConnection connection = new JDBCMockObjectFactory().getMockConnection();
        return new ResultSetTester(connection.getStatementResultSetHandler());
    }

    private final StatementResultSetHandler resultSetHandler;

    /**
     * Create a new tester
     *
     * @param resultSetHandler the handler
     */
    ResultSetTester(final StatementResultSetHandler resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
    }

    /**
     * @param head the header
     * @param rows the rows of the RS
     * @return a RS object
     */
    public MockResultSet createResultSet(final Iterable<String> head,
                                         final Iterable<List<Object>> rows) {
        final MockResultSet rs = this.resultSetHandler.createResultSet();
        for (final String name : head) {
            rs.addColumn(name);
        }

        this.resultSetHandler.prepareGlobalResultSet(rs);
        for (final List<Object> row : rows) {
            rs.addRow(row);
        }
        return rs;
    }
}
