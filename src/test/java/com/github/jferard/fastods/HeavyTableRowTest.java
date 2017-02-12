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

import com.github.jferard.fastods.TableCell.Type;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.LocaleDataStyles;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.testutil.DomTester;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HeavyTableColdRow.class)
public class HeavyTableRowTest {
	public static final long TIME_IN_MILLIS = 1234567891011L;
	private DataStyles ds;
	private HeavyTableRow row;
	private StylesContainer stc;
	private Table table;
	private TableCellStyle tcs;
	private XMLUtil xmlUtil;

	@Before
	public void setUp() {
		this.stc = PowerMock.createMock(StylesContainer.class);
		this.table = PowerMock.createMock(Table.class);
		final WriteUtil writeUtil = new WriteUtil();
		final XMLUtil xmlUtil = new XMLUtil(new FastOdsXMLEscaper());
		this.ds = new LocaleDataStyles(
				new DataStyleBuilderFactory(xmlUtil, Locale.US));
		this.row = new HeavyTableRow(writeUtil, xmlUtil, this.stc, this.ds,
				this.table, 10, 100);
		this.xmlUtil = XMLUtil.create();
		this.tcs = TableCellStyle.builder("---").build();
		PowerMock.mockStatic(HeavyTableColdRow.class);
	}

	@Test
	public final void testBoolean() {
		final DataStyle booleanDataStyle = this.ds.getBooleanDataStyle();
		this.stc.addDataStyle(booleanDataStyle);
		EasyMock.expectLastCall().times(2);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), booleanDataStyle))
				.andReturn(this.tcs);
		EasyMock.expectLastCall().times(2);

		PowerMock.replayAll();
		this.row.setBooleanValue(10, true);
		this.row.setBooleanValue(11, false);
		Assert.assertEquals("true", this.row.getBooleanValue(10));
		Assert.assertEquals("false", this.row.getBooleanValue(11));
		PowerMock.verifyAll();
	}

	@Test
	public final void testCalendar() {
		// PLAY
		final DataStyle dateDataStyle = this.ds.getDateDataStyle();
		this.stc.addDataStyle(dateDataStyle);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), dateDataStyle))
				.andReturn(this.tcs);

		PowerMock.replayAll();
		final Calendar d = Calendar.getInstance();
		d.setTimeInMillis(1234567891011L);
		this.row.setDateValue(7, d);
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.row.getDateValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testColumnsSpanned() {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setColumnsSpanned(0, 10);
		EasyMock.expect(htcr.getColumnsSpanned(0)).andReturn(10);
		for (int i = 1; i < 10; i++)
			EasyMock.expect(htcr.getColumnsSpanned(i)).andReturn(-1);
		EasyMock.expect(htcr.getColumnsSpanned(10)).andReturn(0);
		htcr.setColumnsSpanned(1, 4);
		EasyMock.expect(htcr.getColumnsSpanned(1)).andReturn(-1);

		PowerMock.replayAll();
		Assert.assertEquals(0, this.row.getColumnsSpanned(0)); // no call
		this.row.setColumnsSpanned(0, 1); // no call
		Assert.assertEquals(0, this.row.getColumnsSpanned(0)); // no call
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
	public final void testCovered() {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setCovered(1);
		htcr.setCovered(2);

		PowerMock.replayAll();
		this.row.setCovered(1);
		this.row.setCovered(2);
		PowerMock.verifyAll();

	}

	@Test
	public final void testCovered2() {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setCovered(1, 1);
		htcr.setCovered(2, 1);

		PowerMock.replayAll();
		this.row.setCovered(1, 1);
		this.row.setCovered(2, 1);
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyFloat() {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();

		final DataStyle currencyDataStyle = this.ds.getCurrencyDataStyle();
		this.stc.addDataStyle(currencyDataStyle);
		EasyMock.expectLastCall().times(2);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), currencyDataStyle))
				.andReturn(this.tcs);
		EasyMock.expectLastCall().times(2);

		htcr.setCurrency(10, "€");
		htcr.setCurrency(11, "$");
		EasyMock.expect(htcr.getCurrency(10)).andReturn("@€");

		PowerMock.replayAll();
		Assert.assertNull(this.row.getCurrency(10));
		this.row.setCurrencyValue(10, 10.0, "€");
		this.row.setCurrencyValue(11, 10.0, "$");
		Assert.assertEquals("@€", this.row.getCurrency(10));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyInt() {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();

		final DataStyle currencyDataStyle = this.ds.getCurrencyDataStyle();
		this.stc.addDataStyle(currencyDataStyle);
		EasyMock.expectLastCall().times(2);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), currencyDataStyle))
				.andReturn(this.tcs);
		EasyMock.expectLastCall().times(2);
		htcr.setCurrency(7, "€");
		htcr.setCurrency(8, "$");
		EasyMock.expect(htcr.getCurrency(7)).andReturn("@€");

		PowerMock.replayAll();
		this.row.setCurrencyValue(7, 10, "€");
		this.row.setCurrencyValue(8, 10, "$");
		Assert.assertEquals("@€", this.row.getCurrency(7));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyNumber() {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		final DataStyle currencyDataStyle = this.ds.getCurrencyDataStyle();
		this.stc.addDataStyle(currencyDataStyle);
		EasyMock.expectLastCall().times(2);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), currencyDataStyle))
				.andReturn(this.tcs);
		EasyMock.expectLastCall().times(2);
		htcr.setCurrency(7, "€");
		htcr.setCurrency(8, "$");
		EasyMock.expect(htcr.getCurrency(7)).andReturn("@€");

		PowerMock.replayAll();
		this.row.setCurrencyValue(7, Double.valueOf(10.0), "€");
		this.row.setCurrencyValue(8, Double.valueOf(10.0), "$");
		Assert.assertEquals("@€", this.row.getCurrency(7));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testDate() {
		// PLAY
		final DataStyle dateDataStyle = this.ds.getDateDataStyle();
		this.stc.addDataStyle(dateDataStyle);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), dateDataStyle))
				.andReturn(this.tcs);

		PowerMock.replayAll();
		final Calendar d = Calendar.getInstance();
		d.setTimeInMillis(TIME_IN_MILLIS);
		this.row.setDateValue(7, d.getTime());
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.row.getDateValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testDefaultCellStyle() {
		final TableCellStyle cs = TableCellStyle.builder("a").build();
		final DataStyles ds = new LocaleDataStyles(
				new DataStyleBuilderFactory(this.xmlUtil, Locale.US));

		// PLAY
		this.stc.addStyleToStylesCommonStyles(cs);

		PowerMock.replayAll();
		this.row.setDefaultCellStyle(cs);
		this.row.setFormat(ds);
		PowerMock.verifyAll();
	}

	@Test
	public final void testDouble() {
		// PLAY
		final DataStyle numberDataStyle = this.ds.getNumberDataStyle();
		this.stc.addDataStyle(numberDataStyle);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), numberDataStyle))
				.andReturn(this.tcs);

		PowerMock.replayAll();
		this.row.setFloatValue(7, Double.valueOf(10.999));
		Assert.assertEquals("10.999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testFloatDouble() {
		// PLAY
		final DataStyle numberDataStyle = this.ds.getNumberDataStyle();
		this.stc.addDataStyle(numberDataStyle);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), numberDataStyle))
				.andReturn(this.tcs);

		PowerMock.replayAll();
		this.row.setFloatValue(7, 9.999d);
		Assert.assertEquals("9.999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testFloatFloat() {
		// PLAY
		final DataStyle numberDataStyle = this.ds.getNumberDataStyle();
		this.stc.addDataStyle(numberDataStyle);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), numberDataStyle))
				.andReturn(this.tcs);

		PowerMock.replayAll();
		this.row.setFloatValue(7, 9.999f);
		Assert.assertEquals("9.999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testGet() {
		final TableRowStyle trs = TableRowStyle.builder("a").build();

		// PLAY
		this.stc.addStyleToContentAutomaticStyles(trs);
		PowerMock.replayAll();
		this.row.setStyle(trs);
		this.row.setStringValue(0, "v1");

		Assert.assertEquals("a", this.row.getRowStyleName());
		Assert.assertNull("a", this.row.getTooltip(0));
		Assert.assertEquals(Type.STRING, this.row.getValueType(0));
		Assert.assertNull(this.row.getValueType(1));
		PowerMock.verifyAll();
	}

	@Test
	public final void testInt() {
		// PLAY
		final DataStyle numberDataStyle = this.ds.getNumberDataStyle();
		this.stc.addDataStyle(numberDataStyle);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), numberDataStyle))
				.andReturn(this.tcs);

		PowerMock.replayAll();
		this.row.setFloatValue(7, 999);
		Assert.assertEquals("999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge() throws IOException {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setCellMerge(7, 10, 8);
		EasyMock.expect(htcr.getRowsSpanned(7)).andReturn(-10);
		EasyMock.expect(htcr.getColumnsSpanned(7)).andReturn(-8);

		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(7, 10, 8);
		Assert.assertEquals(-10, this.row.getRowsSpanned(7));
		Assert.assertEquals(-8, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	/*
	@Test
	public final void testMerge1g() {
		// PLAY
		EasyMock.expect(this.table.getRowSecure(EasyMock.anyInt())).andReturn(row2);
		EasyMock.expectLastCall().anyTimes();
		
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(0, 20, 20);
		Assert.assertEquals(20, this.row.getRowsSpanned(0));
		Assert.assertEquals(20, this.row.getColumnsSpanned(0));
		Assert.assertEquals(-1, this.row.getColumnsSpanned(10));
	
		this.row.setCellMerge(10, 3, 3);
		Assert.assertEquals(-1, this.row.getColumnsSpanned(10));
		PowerMock.verifyAll();
	}*/

	@Test
	public final void testMerge1b() throws IOException {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setCellMerge(7, -1, 8);
		EasyMock.expect(htcr.getRowsSpanned(7)).andReturn(0);
		EasyMock.expect(htcr.getColumnsSpanned(7)).andReturn(8);

		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(7, -1, 8);
		Assert.assertEquals(0, this.row.getRowsSpanned(7)); // no call
		Assert.assertEquals(8, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1c() throws IOException {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setCellMerge(7, 10, -1);
		EasyMock.expect(htcr.getRowsSpanned(7)).andReturn(10);
		EasyMock.expect(htcr.getColumnsSpanned(7)).andReturn(0);

		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(7, 10, -1);
		Assert.assertEquals(10, this.row.getRowsSpanned(7));
		Assert.assertEquals(0, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1d() throws IOException {
		// PLAY
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(7, -1, -1);
		Assert.assertEquals(0, this.row.getRowsSpanned(7));
		Assert.assertEquals(0, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1e() throws IOException {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setCellMerge(0, 2, 2);
		htcr.setCellMerge(10, 3, 3);
		EasyMock.expect(htcr.getRowsSpanned(0)).andReturn(2);
		EasyMock.expect(htcr.getColumnsSpanned(0)).andReturn(2);
		EasyMock.expect(htcr.getRowsSpanned(10)).andReturn(3);
		EasyMock.expect(htcr.getColumnsSpanned(10)).andReturn(3);

		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(0, 2, 2);
		this.row.setCellMerge(10, 3, 3);
		Assert.assertEquals(2, this.row.getRowsSpanned(0));
		Assert.assertEquals(2, this.row.getColumnsSpanned(0));
		Assert.assertEquals(3, this.row.getRowsSpanned(10));
		Assert.assertEquals(3, this.row.getColumnsSpanned(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1f() throws IOException {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setCellMerge(0, 20, 20);
		EasyMock.expect(htcr.getRowsSpanned(0)).andReturn(-20);
		EasyMock.expect(htcr.getColumnsSpanned(0)).andReturn(-20);
		EasyMock.expect(htcr.getColumnsSpanned(10)).andReturn(-1000);

		htcr.setCellMerge(10, 3, 3);
		EasyMock.expect(htcr.getColumnsSpanned(10)).andReturn(-1000);

		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(0, 20, 20);
		Assert.assertEquals(-20, this.row.getRowsSpanned(0));
		Assert.assertEquals(-20, this.row.getColumnsSpanned(0));
		Assert.assertEquals(-1000, this.row.getColumnsSpanned(10));

		this.row.setCellMerge(10, 3, 3);
		Assert.assertEquals(-1000, this.row.getColumnsSpanned(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge2() throws IOException {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);
		final StringBuilder sbt = new StringBuilder();

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setCellMerge(5, 10, 8);
		EasyMock.expect(htcr.isCovered(5)).andReturn(false);
		htcr.appendXMLToTable(this.xmlUtil, sbt, 5, false);
		EasyMock.expectLastCall().andAnswer(new IAnswer<Void>() {
			@Override
			public Void answer() {
				((StringBuilder) EasyMock.getCurrentArguments()[1])
						.append(" htcr=\"@\" />"); // cold row has to return an valid closing tag
				return null;
			}
		});

		PowerMock.replayAll();
		this.row.setStringValue(5, "value");
		this.row.setCellMerge(5, 10, 8);
		this.row.appendXMLToTable(this.xmlUtil, sbt);
		DomTester.assertEquals("<table:table-row table:style-name=\"ro1\">"
				+ "<table:table-cell table:number-columns-repeated=\"5\" />"
				+ "<table:table-cell office:value-type=\"string\" office:string-value=\"value\" htcr=\"@\" />"
				+ "</table:table-row>", sbt.toString());
		PowerMock.verifyAll();
	}

	@Test
	public final void testNullFieldCounter() throws IOException {
		PowerMock.replayAll();
		this.row.setStringValue(0, "v1");
		this.row.setStringValue(2, "v2");
		final StringBuilder sb = new StringBuilder();
		this.row.appendXMLToTable(this.xmlUtil, sb);
		DomTester.assertEquals("<table:table-row  table:style-name=\"ro1\">"
				+ "<table:table-cell office:value-type=\"string\" office:string-value=\"v1\"/>"
				+ "<table:table-cell/>"
				+ "<table:table-cell office:value-type=\"string\" office:string-value=\"v2\"/>"
				+ "</table:table-row>", sb.toString());
		PowerMock.verifyAll();
	}

	@Test
	@SuppressWarnings("deprecation")
	public final void testObject() {
		PowerMock.replayAll();
		this.row.setObjectValue(7, null);
		PowerMock.verifyAll();
	}

	@Test
	public final void testPercentageDouble() {
		// PLAY
		final DataStyle percentageDataStyle = this.ds.getPercentageDataStyle();
		this.stc.addDataStyle(percentageDataStyle);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), percentageDataStyle))
				.andReturn(this.tcs);

		PowerMock.replayAll();
		this.row.setPercentageValue(7, 0.98d);
		Assert.assertEquals("0.98", this.row.getPercentageValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testPercentageFloat() {
		// PLAY
		final DataStyle percentageDataStyle = this.ds.getPercentageDataStyle();
		this.stc.addDataStyle(percentageDataStyle);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), percentageDataStyle))
				.andReturn(this.tcs);

		PowerMock.replayAll();
		this.row.setPercentageValue(7, 0.98f);
		Assert.assertEquals("0.98", this.row.getPercentageValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testPercentageNumber() {
		// PLAY
		final DataStyle percentageDataStyle = this.ds.getPercentageDataStyle();
		this.stc.addDataStyle(percentageDataStyle);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), percentageDataStyle))
				.andReturn(this.tcs);

		PowerMock.replayAll();
		this.row.setPercentageValue(7, Double.valueOf(0.98));
		Assert.assertEquals("0.98", this.row.getPercentageValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testRowOpenTag() throws IOException {
		final TableRowStyle trs = TableRowStyle.builder("a").build();
		final TableCellStyle cs = TableCellStyle.builder("b").build();
		final DataStyles ds = new LocaleDataStyles(
				new DataStyleBuilderFactory(this.xmlUtil, Locale.US));
		final StringBuilder sb = new StringBuilder();

		// PLAY
		this.stc.addStyleToStylesCommonStyles(cs);
		this.stc.addStyleToContentAutomaticStyles(trs);

		PowerMock.replayAll();
		this.row.setStyle(trs);
		this.row.setDefaultCellStyle(cs);
		this.row.setFormat(ds);

		this.row.appendXMLToTable(this.xmlUtil, sb);
		DomTester
				.equals("<table:table-row table:style-name=\"a\" table:default-cell-style-name=\"b\">"
						+ "</table:table-row>", sb.toString());
		PowerMock.verifyAll();
	}

	@Test
	public final void testRowsSpanned() throws IOException {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setRowsSpanned(0, 10);
		EasyMock.expect(htcr.getRowsSpanned(0)).andReturn(10);

		PowerMock.replayAll();
		Assert.assertEquals(0, this.row.getRowsSpanned(0)); // no call
		this.row.setRowsSpanned(0, 1); // no call
		this.row.setRowsSpanned(0, 10);
		Assert.assertEquals(10, this.row.getRowsSpanned(0));
		PowerMock.verifyAll();
	}

	@Test
	public final void testRowsSpanned2() throws IOException {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setRowsSpanned(0, 10);
		htcr.setRowsSpanned(1, 10);
		EasyMock.expect(htcr.getRowsSpanned(0)).andReturn(10);

		PowerMock.replayAll();
		Assert.assertEquals(0, this.row.getRowsSpanned(0)); // no call
		this.row.setRowsSpanned(0, 1); // no call
		this.row.setRowsSpanned(0, 10);
		this.row.setRowsSpanned(1, 10);
		Assert.assertEquals(10, this.row.getRowsSpanned(0));
		PowerMock.verifyAll();
	}

	@Test
	public final void testSpan() {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setColumnsSpanned(10, 2);
		EasyMock.expect(htcr.getColumnsSpanned(10)).andReturn(-2);

		PowerMock.replayAll();
		this.row.setColumnsSpanned(10, 2);
		Assert.assertEquals(-2, this.row.getColumnsSpanned(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testString() {
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		Assert.assertEquals("value", this.row.getStringValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testStyle() {
		final TableCellStyle tcs = TableCellStyle.builder("test").build();
		this.stc.addStyleToStylesCommonStyles(tcs);
		PowerMock.replayAll();
		this.row.setStyle(7, null);
		this.row.setStyle(7, tcs);
		Assert.assertEquals("test", this.row.getStyleName(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testText() {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);
		final Text t0 = Text.content("text0");
		final Text t1 = Text.content("text1");
		final Text t_0 = Text.content("@text");

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setText(0, t0);
		htcr.setText(1, t1);
		EasyMock.expect(htcr.getText(0)).andReturn(t_0);

		PowerMock.replayAll();
		Assert.assertNull(this.row.getText(0));
		this.row.setText(0, t0);
		this.row.setText(1, t1);
		Assert.assertEquals(t_0, this.row.getText(0));
		PowerMock.verifyAll();
	}

	@Test
	public final void testTime() {
		final DataStyle timeDataStyle = this.ds.getTimeDataStyle();

		// PLAY
		this.stc.addDataStyle(timeDataStyle);
		EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), timeDataStyle))
				.andReturn(this.tcs);

		PowerMock.replayAll();
		this.row.setTimeValue(7, 1234567891011l);
		Assert.assertEquals("P14288DT23H31M31.11S", this.row.getTimeValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testTooltip() {
		final HeavyTableColdRow htcr = PowerMock.createMock(HeavyTableColdRow.class);

		// PLAY
		EasyMock.expect(HeavyTableColdRow.create(EasyMock.eq(this.table),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(htcr)
				.anyTimes();
		htcr.setTooltip(7, "tooltip");
		htcr.setTooltip(8, "tooltip2");
		EasyMock.expect(htcr.getTooltip(7)).andReturn("@tooltip");

		PowerMock.replayAll();
		this.row.setTooltip(7, "tooltip");
		this.row.setTooltip(8, "tooltip2");
		Assert.assertEquals("@tooltip", this.row.getTooltip(7));
		PowerMock.verifyAll();
	}
}
