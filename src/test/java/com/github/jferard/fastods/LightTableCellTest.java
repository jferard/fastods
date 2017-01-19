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

import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.datastyle.LocaleDataStyles;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LightTableCellTest {
	private LightTableCell cell;
	private HeavyTableRow row;

	@Before
	public void setUp() {
		this.row = PowerMock.createMock(HeavyTableRow.class);
		this.cell = new LightTableCell(this.row);
	}

	@Test
	public final void testBoolean() {
		this.row.setBooleanValue(10, true);
		EasyMock.expect(this.row.getColumnCount()).andReturn(20);
		EasyMock.expect(this.row.getBooleanValue(11)).andReturn("false");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setBooleanValue(true);
		this.cell.next();
		Assert.assertEquals("false", this.cell.getBooleanValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testCalendar() {
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(1234567891011l);

		this.row.setDateValue(10, c);
		EasyMock.expect(this.row.getDateValue(10))
				.andReturn("2009-02-14T00:31:31.011");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setDateValue(c);
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.cell.getDateValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyFloat() {
		this.row.setCurrencyValue(10, 10.0f, "€");
		EasyMock.expect(this.row.getCurrency(10)).andReturn("€");
		EasyMock.expect(this.row.getCurrencyValue(10)).andReturn("10.0");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setCurrencyValue(10.0f, "€");
		Assert.assertEquals("€", this.cell.getCurrency());
		Assert.assertEquals("10.0", this.cell.getCurrencyValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyInt() {
		this.row.setCurrencyValue(10, 9, "€");
		EasyMock.expect(this.row.getCurrency(10)).andReturn("€");
		EasyMock.expect(this.row.getCurrencyValue(10)).andReturn("9.0");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setCurrencyValue(9, "€");
		Assert.assertEquals("€", this.cell.getCurrency());
		Assert.assertEquals("9.0", this.cell.getCurrencyValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyNumber() {
		this.row.setCurrencyValue(10, Double.valueOf(10.0), "€");
		EasyMock.expect(this.row.getCurrency(10)).andReturn("€");
		EasyMock.expect(this.row.getCurrencyValue(10)).andReturn("10.0");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setCurrencyValue(Double.valueOf(10.0), "€");
		Assert.assertEquals("€", this.cell.getCurrency());
		Assert.assertEquals("10.0", this.cell.getCurrencyValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testDate() {
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(1234567891011l);
		final Date date = c.getTime();

		this.row.setDateValue(10, date);
		EasyMock.expect(this.row.getDateValue(10))
				.andReturn("2009-02-14T00:31:31.011");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setDateValue(date);
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.cell.getDateValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testDouble() {
		this.row.setFloatValue(10, Double.valueOf(10.999));
		EasyMock.expect(this.row.getFloatValue(10)).andReturn("1000.99");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setFloatValue(Double.valueOf(10.999));
		Assert.assertEquals("1000.99", this.cell.getFloatValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testFloat() {
		this.row.setFloatValue(10, 9.999f);
		EasyMock.expect(this.row.getFloatValue(10)).andReturn("9.999");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setFloatValue(9.999f);
		Assert.assertEquals("9.999", this.cell.getFloatValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testInt() {
		this.row.setFloatValue(10, 999);
		EasyMock.expect(this.row.getFloatValue(10)).andReturn("1000");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setFloatValue(999);
		Assert.assertEquals("1000", this.cell.getFloatValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testMove() {
		final HeavyTableRow row = this.initRealRow();
		final LightTableCell cell = new LightTableCell(row);
		PowerMock.replayAll();
		cell.to(49);
		cell.setStringValue("s");
		cell.to(0);
		Assert.assertTrue(cell.hasNext());
		Assert.assertFalse(cell.hasPrevious());
		cell.next();
		cell.lastCell();
		cell.to(49);
		Assert.assertTrue(cell.hasNext());
		cell.next();
		Assert.assertFalse(cell.hasNext());
		cell.to(100);
		Assert.assertFalse(cell.hasPrevious());
		cell.to(50);
		Assert.assertTrue(cell.hasPrevious());
		cell.to(51);
		Assert.assertFalse(cell.hasPrevious());
		PowerMock.verifyAll();
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public final void testMoveNegative() {
		final HeavyTableRow row = this.initRealRow();
		final LightTableCell cell = new LightTableCell(row);
		PowerMock.replayAll();
		cell.to(-1);
		PowerMock.verifyAll();
	}

	@Test
	@SuppressWarnings("deprecation")
	public final void testObject() {
		this.row.setObjectValue(10, null);
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setObjectValue(null);
		PowerMock.verifyAll();
	}

	@Test
	public final void testPercentageFloat() {
		this.row.setPercentageValue(10, 0.98f);
		EasyMock.expect(this.row.getPercentageValue(10)).andReturn("0.98");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setPercentageValue(0.98f);
		Assert.assertEquals("0.98", this.cell.getPercentageValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testPercentageNumber() {
		this.row.setPercentageValue(10, Double.valueOf(0.98));
		EasyMock.expect(this.row.getPercentageValue(10)).andReturn("0.98");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setPercentageValue(Double.valueOf(0.98));
		Assert.assertEquals("0.98", this.cell.getPercentageValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testSpan() {
		this.row.setColumnsSpanned(10, 2);
		EasyMock.expect(this.row.getColumnsSpanned(10)).andReturn(2);
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setColumnsSpanned(2);
		Assert.assertEquals(2, this.cell.getColumnsSpanned());
		PowerMock.verifyAll();
	}

	@Test
	public final void testString() {
		this.row.setStringValue(10, "value");
		EasyMock.expect(this.row.getStringValue(10)).andReturn("value");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setStringValue("value");
		Assert.assertEquals("value", this.cell.getStringValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testStyle() {
		final TableCellStyle tcs = TableCellStyle.builder("test").build();
		this.row.setStyle(10, tcs);
		EasyMock.expect(this.row.getStyleName(10)).andReturn("test");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setStyle(tcs);
		Assert.assertEquals("test", this.cell.getStyleName());
		PowerMock.verifyAll();
	}

	@Test
	public final void testTime() {
		this.row.setTimeValue(10, 1234567891011l);
		EasyMock.expect(this.row.getTimeValue(10))
				.andReturn("P14288DT23H31M31.11S");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setTimeValue(1234567891011l);
		Assert.assertEquals("P14288DT23H31M31.11S", this.cell.getTimeValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testTooltip() {
		this.row.setTooltip(10, "tooltip");
		EasyMock.expect(this.row.getTooltip(10)).andReturn("t");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setTooltip("tooltip");
		Assert.assertEquals("t", this.cell.getTooltip());
		PowerMock.verifyAll();
	}

	private HeavyTableRow initRealRow() {
		final StylesContainer stc = PowerMock.createMock(StylesContainer.class);
		final PositionUtil positionUtil = new PositionUtil(new EqualityUtil());
		final XMLUtil xmlUtil = new XMLUtil(new FastOdsXMLEscaper());
		final LocaleDataStyles ds = new LocaleDataStyles(
				new DataStyleBuilderFactory(xmlUtil, Locale.US));
		final WriteUtil writeUtil = new WriteUtil();
		return new HeavyTableRow(writeUtil, xmlUtil, stc, ds, null,
				10, 100);
	}
}
