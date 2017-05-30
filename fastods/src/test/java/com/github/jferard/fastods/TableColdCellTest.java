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

import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class TableColdCellTest {
	private TableColdCell coldCell;
	private TableRow row;
	private XMLUtil xmlUtil;

	@Before
	public void setUp() {
		this.row = PowerMock.createMock(TableRow.class);
		this.xmlUtil = XMLUtil.create();
		this.coldCell = new TableColdCell(this.row, this.xmlUtil);
	}

	@Test
	public final void testCreate() throws IOException {
		PowerMock.replayAll();
		this.coldCell = TableColdCell.create(this.row, this.xmlUtil);
		DomTester.assertEquals("<table:table-cell/>", this.getXML());
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrency() throws IOException {
		PowerMock.replayAll();
		this.coldCell.setCurrency("€");
		Assert.assertEquals("€", this.coldCell.getCurrency());
		DomTester.assertEquals("<table:table-cell/>", this.getXML());
		PowerMock.verifyAll();
	}

	@Test
	public final void testCovered() throws IOException {
		PowerMock.replayAll();
		Assert.assertFalse(this.coldCell.isCovered());
		this.coldCell.setCovered();
		Assert.assertTrue(this.coldCell.isCovered());
		DomTester.assertEquals("<table:table-cell/>", this.getXML());
		PowerMock.verifyAll();
	}

	@Test
	public final void testText() throws IOException {
		PowerMock.replayAll();
		final Text t0 = Text.content("text0");
		this.coldCell.setText(t0);
		DomTester.assertEquals("<table:table-cell><text:p>text0</text:p></table:table-cell>", this.getXML());
		PowerMock.verifyAll();
	}

	@Test
	public final void testFormula() throws IOException {
		PowerMock.replayAll();
		this.coldCell.setFormula("1");
		DomTester.assertEquals("<table:table-cell table:formula=\"=1\"/>", this.getXML());
		PowerMock.verifyAll();
	}

	@Test
	public final void testColSpan() throws IOException {
		PowerMock.replayAll();
		this.coldCell.setColumnsSpanned(2);
		DomTester.assertEquals("<table:table-cell table:number-columns-spanned=\"2\"/>", this.getXML());
		PowerMock.verifyAll();
	}

	@Test
	public final void testRowSpan() throws IOException {
		PowerMock.replayAll();
		this.coldCell.setRowsSpanned(8);
		DomTester.assertEquals("<table:table-cell table:number-rows-spanned=\"8\"/>", this.getXML());
		PowerMock.verifyAll();
	}

	@Test
	public final void testTooltip() throws IOException {
		PowerMock.replayAll();
		this.coldCell.setTooltip("tooltip");
		DomTester.assertEquals("<table:table-cell><office:annotation>" +
				"<text:p>tooltip</text:p>" +
				"</office:annotation></table:table-cell>", this.getXML());
		PowerMock.verifyAll();
	}

	@Test
	public final void testTooltipWithSize() throws IOException {
		PowerMock.replayAll();
		this.coldCell.setTooltip("tooltip", SimpleLength.cm(1), SimpleLength.cm(2), true);
		DomTester.assertEquals("<table:table-cell>" +
				"<office:annotation office:display=\"true\" svg:width=\"1cm\" svg:height=\"2cm\" svg:x=\"\">" +
				"<text:p>tooltip</text:p>" +
				"</office:annotation>" +
				"</table:table-cell>", this.getXML());
		PowerMock.verifyAll();
	}

	@Test
	public final void testTooltipWithSpecialChars() throws IOException {
		PowerMock.replayAll();
		this.coldCell.setTooltip("<tooltip>");
		DomTester.assertEquals("<table:table-cell><office:annotation>" +
				"<text:p>&lt;tooltip&gt;</text:p>" +
				"</office:annotation></table:table-cell>", this.getXML());
		PowerMock.verifyAll();
	}

	@Test
	public final void testTooltipWithCR() throws IOException {
		PowerMock.replayAll();
		this.coldCell.setTooltip("tooltip\nline 1\nline2");
		DomTester.assertEquals("<table:table-cell><office:annotation>" +
				"<text:p>tooltip</text:p><text:p>line 1</text:p><text:p>line2</text:p>" +
				"</office:annotation></table:table-cell>", this.getXML());
		PowerMock.verifyAll();
	}

	@Test
	public final void testAppendTextAndTooltip() throws IOException {
		// PLAY
		PowerMock.replayAll();
		this.coldCell.setText(Text.content("c"));
		this.coldCell.setTooltip("tooltip");
		DomTester.assertEquals("<table:table-cell>" +
				"<text:p>c</text:p>" +
				"<office:annotation><text:p>tooltip</text:p></office:annotation>" +
				"</table:table-cell>", this.getXML());
		PowerMock.verifyAll();

	}

	/*
	@Test
	public final void testColumnsSpanned() {
		PowerMock.replayAll();
		Assert.assertEquals(0, this.coldCell.getColumnsSpanned(0));
		this.coldCell.setColumnsSpanned(0, 1); // does nothing
		Assert.assertEquals(0, this.coldCell.getColumnsSpanned(0));
		this.coldCell.setColumnsSpanned(0, 10);
		Assert.assertEquals(10, this.coldCell.getColumnsSpanned(0));
		for (int i = 1; i < 10; i++)
			Assert.assertEquals(-1, this.coldCell.getColumnsSpanned(i));

		Assert.assertEquals(0, this.coldCell.getColumnsSpanned(10));
		this.coldCell.setColumnsSpanned(1, 4); // does nothing since cell is already
											// covered
		Assert.assertEquals(-1, this.coldCell.getColumnsSpanned(1));
		PowerMock.verifyAll();
	}

	@Test
	public final void testRowsSpanned() throws IOException {
		final HeavyTableRow r2 = PowerMock.createMock(HeavyTableRow.class);
		EasyMock.expect(this.table.getRowSecure(EasyMock.anyInt(), EasyMock.eq(true)))
				.andReturn(r2).anyTimes();
		r2.setCovered(0);
		EasyMock.expectLastCall().times(9);

		PowerMock.replayAll();
		Assert.assertEquals(0, this.coldCell.getRowsSpanned(0));
		this.coldCell.setRowsSpanned(0, 1); // does nothing
		Assert.assertEquals(0, this.coldCell.getRowsSpanned(0));
		this.coldCell.setRowsSpanned(0, 10);
		Assert.assertEquals(10, this.coldCell.getRowsSpanned(0));
		PowerMock.verifyAll();
	}

	/*
	@Test
	public final void testMerge2() throws IOException {
		final HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);

		// PLAY
		for (int c = 11; c < 20; c++) {
			EasyMock.expect(this.table.getRowSecure(c, false)).andReturn(row2);
			row2.setCovered(5, 8);
		}
		PowerMock.replayAll();
		this.coldCell.setCellMerge(5, 10, 8);
		final StringBuilder sbt = new StringBuilder();
		this.coldCell.appendXMLToTable(this.xmlUtil, sbt, 5, false);
		Assert.assertEquals(
				" table:number-columns-spanned=\"8\" table:number-rows-spanned=\"10\"/>",
				sbt.toString());
		PowerMock.verifyAll();
	}
	*/

	private String getXML() throws IOException {
		final StringBuilder sb = new StringBuilder("<table:table-cell");
		this.coldCell.appendXMLToTable(this.xmlUtil, sb, false);
		return sb.toString();
	}
}
