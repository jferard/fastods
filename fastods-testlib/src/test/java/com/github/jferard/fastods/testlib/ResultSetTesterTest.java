/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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

import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResultSetTesterTest {
    @Test
    @SuppressWarnings("unchecked")
    public void test() throws SQLException {
        final ResultSetTester tester = ResultSetTester.create();
        final ResultSet rs = tester.createResultSet(Collections.singletonList("number"),
                Arrays.asList(Collections.<Object>singletonList(1),
                        Collections.<Object>singletonList(2),
                        Collections.<Object>singletonList(3)));

        final ResultSetMetaData metaData = rs.getMetaData();
        Assert.assertEquals(1, metaData.getColumnCount());
        Assert.assertEquals("number", metaData.getColumnName(1));
        Assert.assertTrue(rs.next());
        Assert.assertEquals(1, rs.getInt(1));
        Assert.assertTrue(rs.next());
        Assert.assertEquals(2, rs.getInt(1));
        Assert.assertTrue(rs.next());
        Assert.assertEquals(3, rs.getInt(1));
        Assert.assertFalse(rs.next());
    }

}