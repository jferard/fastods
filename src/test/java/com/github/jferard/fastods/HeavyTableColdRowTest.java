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
package com.github.jferard.fastods;

import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class HeavyTableColdRowTest {
	private HeavyTableColdRow row;
	private Table table;
	private XMLUtil xmlUtil;

	@Before
	public void setUp() {
		this.table = PowerMock.createMock(Table.class);
		final XMLUtil xmlUtil = new XMLUtil(new FastOdsXMLEscaper());
		this.row = new HeavyTableColdRow(this.table, 10, 100);
		this.xmlUtil = XMLUtil.create();
	}

	@Test
	public final void testCurrency() {
		PowerMock.replayAll();
		Assert.assertEquals(null, this.row.getCurrency(10));
		this.row.setCurrency(10, "€");
		Assert.assertEquals("€", this.row.getCurrency(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge() {
		final HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);

		// PLAY
		for (int r = 11; r < 20; r++) {
			EasyMock.expect(this.table.getRowSecure(r)).andReturn(row2);
			row2.setCovered(7, 8);
		}
		PowerMock.replayAll();
		this.row.setCellMerge(7, 10, 8);
		Assert.assertEquals(10, this.row.getRowsSpanned(7));
		Assert.assertEquals(8, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1b() {
		// PLAY
		PowerMock.replayAll();
		this.row.setCellMerge(7, -1, 8);
		Assert.assertEquals(0, this.row.getRowsSpanned(7));
		Assert.assertEquals(0, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1c() {
		final HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);

		// PLAY
		PowerMock.replayAll();
		this.row.setCellMerge(7, 10, -1);
		Assert.assertEquals(0, this.row.getRowsSpanned(7));
		Assert.assertEquals(0, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1d() {
		// PLAY
		PowerMock.replayAll();
		this.row.setCellMerge(7, -1, -1);
		Assert.assertEquals(0, this.row.getRowsSpanned(7));
		Assert.assertEquals(0, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1e() {
		final HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);

		// PLAY
		for (int r = 11; r < 12; r++) {
			EasyMock.expect(this.table.getRowSecure(r)).andReturn(row2);
			row2.setCovered(0, 2);
		}
		for (int r = 11; r < 13; r++) {
			EasyMock.expect(this.table.getRowSecure(r)).andReturn(row2);
			row2.setCovered(10, 3);
		}

		PowerMock.replayAll();
		this.row.setCellMerge(0, 2, 2);
		this.row.setCellMerge(10, 3, 3);
		Assert.assertEquals(2, this.row.getRowsSpanned(0));
		Assert.assertEquals(2, this.row.getColumnsSpanned(0));
		Assert.assertEquals(3, this.row.getRowsSpanned(10));
		Assert.assertEquals(3, this.row.getColumnsSpanned(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1f() {
		final HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);

		// PLAY
		for (int r = 11; r < 30; r++) {
			EasyMock.expect(this.table.getRowSecure(r)).andReturn(row2);
			row2.setCovered(0, 20);
		}
		for (int r = 11; r < 4; r++) {
			EasyMock.expect(this.table.getRowSecure(r)).andReturn(row2);
			row2.setCovered(18, 4);
		}

		PowerMock.replayAll();
		this.row.setCellMerge(0, 20, 20);
		Assert.assertEquals(20, this.row.getRowsSpanned(0));
		Assert.assertEquals(20, this.row.getColumnsSpanned(0));
		Assert.assertEquals(-1, this.row.getColumnsSpanned(10));

		this.row.setCellMerge(18, 4, 4);
		Assert.assertEquals(-1, this.row.getColumnsSpanned(10));
		Assert.assertEquals(0, this.row.getColumnsSpanned(21));
		Assert.assertEquals(0, this.row.getColumnsSpanned(21));
		PowerMock.verifyAll();
	}

	@Test
	public final void testText() {
		// PLAY
		PowerMock.replayAll();
		Assert.assertNull(this.row.getText(0));
		final Text t0 = Text.content("text0");
		final Text t1 = Text.content("text1");
		this.row.setText(0, t0);
		this.row.setText(1, t1);
		Assert.assertEquals(t0, this.row.getText(0));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge2() throws IOException {
		final HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);

		// PLAY
		for (int c = 11; c < 20; c++) {
			EasyMock.expect(this.table.getRowSecure(c)).andReturn(row2);
			row2.setCovered(5, 8);
		}
		PowerMock.replayAll();
		this.row.setCellMerge(5, 10, 8);
		final StringBuilder sbt = new StringBuilder();
		this.row.appendXMLToTable(this.xmlUtil, sbt, 5, false);
		Assert.assertEquals(
				" table:number-columns-spanned=\"8\" table:number-rows-spanned=\"10\"/>",
				sbt.toString());
		PowerMock.verifyAll();
	}

	@Test
	public final void testSpan() {
		PowerMock.replayAll();
		this.row.setColumnsSpanned(10, 2);
		Assert.assertEquals(2, this.row.getColumnsSpanned(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testTooltip() {
		PowerMock.replayAll();
		this.row.setTooltip(7, "tooltip");
		Assert.assertEquals("tooltip", this.row.getTooltip(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testColumnsSpanned() {
		PowerMock.replayAll();
		Assert.assertEquals(0, this.row.getColumnsSpanned(0));
		this.row.setColumnsSpanned(0, 1); // does nothing
		Assert.assertEquals(0, this.row.getColumnsSpanned(0));
		this.row.setColumnsSpanned(0, 10);
		Assert.assertEquals(10, this.row.getColumnsSpanned(0));
		for (int i = 1; i < 10; i++)
			Assert.assertEquals(-1, this.row.getColumnsSpanned(i));

		Assert.assertEquals(0, this.row.getColumnsSpanned(10));
		this.row.setColumnsSpanned(1, 4); // does nothing since cell is already
											// covered
		Assert.assertEquals(-1, this.row.getColumnsSpanned(1));
		PowerMock.verifyAll();
	}

	@Test
	public final void testRowsSpanned() {
		final HeavyTableRow r2 = PowerMock.createMock(HeavyTableRow.class);
		EasyMock.expect(this.table.getRowSecure(EasyMock.anyInt()))
				.andReturn(r2).anyTimes();
		r2.setCovered(0);
		EasyMock.expectLastCall().times(9);

		PowerMock.replayAll();
		Assert.assertEquals(0, this.row.getRowsSpanned(0));
		this.row.setRowsSpanned(0, 1); // does nothing
		Assert.assertEquals(0, this.row.getRowsSpanned(0));
		this.row.setRowsSpanned(0, 10);
		Assert.assertEquals(10, this.row.getRowsSpanned(0));
		PowerMock.verifyAll();
	}

	@Test
	public final void testAppendTextAndTooltip() throws IOException {
		final StringBuilder sb = new StringBuilder();

		// PLAY
		PowerMock.replayAll();
		this.row.setText(0, Text.content("c"));
		this.row.setTooltip(0, "tooltip");
		this.row.appendXMLToTable(this.xmlUtil, sb, 0, false);
		Assert.assertEquals(">" + "<text:p>c</text:p>"
				+ "<office:annotation><text:p>tooltip</text:p></office:annotation>"
				+ "</table:table-cell>", sb.toString());
		PowerMock.verifyAll();
	}
}
