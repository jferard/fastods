/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.CellValue;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.HeavyTableRow;
import com.github.jferard.fastods.StringValue;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.PositionUtil;

public class TableHelperTest {

	private TableCell cell;
	private PositionUtil positionUtil;
	private HeavyTableRow row;
	private Table table;
	private TableHelper tableHelper;
	private TableCellWalker walker;

	@Before
	public void setUp() throws Exception {
		this.positionUtil = new PositionUtil(new EqualityUtil());
		this.table = PowerMock.createMock(Table.class);
		this.row = PowerMock.createMock(HeavyTableRow.class);
		this.walker = PowerMock.createMock(TableCellWalker.class);
		this.cell = PowerMock.createMock(TableCell.class);
		this.tableHelper = new TableHelper(this.positionUtil);
	}

	@Test
	public final void testSetCellMerge() throws FastOdsException {
		final CellValue value = new StringValue("@");
		final TableCellStyle ts = TableCellStyle.builder("b").build();

		EasyMock.expect(this.table.getRow(6)).andReturn(this.row);
		EasyMock.expect(this.row.getWalker()).andReturn(this.walker);
		this.walker.to(2);
		this.walker.setColumnsSpanned(3);
		this.walker.setRowsSpanned(9);
		PowerMock.replayAll();
		this.tableHelper.setCellMerge(this.table, "C7", 9, 3);
		PowerMock.verifyAll();
	}

	@Test
	public final void testSetCellValue() throws FastOdsException {
		final CellValue value = new StringValue("@");
		final TableCellStyle ts = TableCellStyle.builder("b").build();

		EasyMock.expect(this.table.getRow(6)).andReturn(this.row);
		EasyMock.expect(this.row.getWalker()).andReturn(this.walker);
		this.walker.to(2);
		this.walker.setCellValue(value);
		this.walker.setStyle(ts);
		PowerMock.replayAll();
		this.tableHelper.setCellValue(this.table, "C7", value, ts);
		PowerMock.verifyAll();
	}
}
