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

import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.StringValue;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.style.TableCellStyle;
import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ResultSetDataWrapperBuilderTest {
    @Test
    public void test() throws IOException, SQLException {
        final TableCellWalker walker = PowerMock.createMock(TableCellWalker.class);
        final ResultSet rs = PowerMock.createMock(ResultSet.class);
        final SQLToCellValueConverter.IntervalConverter converter = PowerMock
                .createMock(SQLToCellValueConverter.IntervalConverter.class);
        final Logger logger = PowerMock.createMock(Logger.class);
        final ResultSetMetaData md = PowerMock.createMock(ResultSetMetaData.class);

        final ResultSetDataWrapper b = ResultSetDataWrapper.builder("range", rs).converter(converter)
                .charset(Charset.forName("US-ASCII")).currency("€").nullValue(new StringValue(""))
                .noHeaderStyle().noAutoFilter().typeValue(0, CellType.VOID).max(10)
                .headerStyle(TableCellStyle.builder("dummy").build()).logger(logger).build();

        PowerMock.resetAll();
        EasyMock.expect(walker.rowIndex()).andReturn(0);
        EasyMock.expect(walker.colIndex()).andReturn(0);
        EasyMock.expect(rs.getMetaData()).andReturn(md);
        EasyMock.expect(md.getColumnCount()).andReturn(0).anyTimes();
        EasyMock.expect(rs.next()).andReturn(false);
        walker.nextRow();
        walker.to(0);
        walker.nextRow();

        PowerMock.replayAll();
        b.addToTable(walker);

        PowerMock.verifyAll();
    }
}