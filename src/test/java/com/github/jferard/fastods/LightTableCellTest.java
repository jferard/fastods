package com.github.jferard.fastods;

import static org.easymock.EasyMock.expect;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.datastyle.LocaleDataStyles;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.XMLUtil;

public class LightTableCellTest {
	private HeavyTableRow row;
	private LightTableCell cell;

	@Before
	public void setUp() {
		this.row = PowerMock.createMock(HeavyTableRow.class);
		this.cell = new LightTableCell(this.row);
	}

	@Test
	public final void testMove() {
		HeavyTableRow row = initRealRow();
		LightTableCell cell = new LightTableCell(row);
		PowerMock.replayAll();
		cell.to(49);
		cell.setStringValue("s");
		cell.to(0);
		Assert.assertTrue(cell.hasNext());
		Assert.assertFalse(cell.hasPrevious());
		cell.next();
		cell.lastCell();
		cell.to(48);
		Assert.assertTrue(cell.hasNext());
		cell.next();
		Assert.assertFalse(cell.hasNext());
		cell.to(100);
		Assert.assertFalse(cell.hasPrevious());
		cell.to(50);
		Assert.assertTrue(cell.hasPrevious());
		PowerMock.verifyAll();
	}

	private HeavyTableRow initRealRow() {
		ContentEntry ce = PowerMock.createMock(ContentEntry.class);
		StylesEntry se = PowerMock.createMock(StylesEntry.class);
		Util util = Util.create();
		XMLUtil xmlUtil = new XMLUtil(new FastOdsXMLEscaper());
		LocaleDataStyles ds = new LocaleDataStyles(
				new DataStyleBuilderFactory(xmlUtil, Locale.US), xmlUtil);
		return new HeavyTableRow(ce, se, util, xmlUtil, ds, 10, 100);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public final void testMoveNegative() {
		HeavyTableRow row = this.initRealRow();
		LightTableCell cell = new LightTableCell(row);
		PowerMock.replayAll();
		cell.to(-1);
		PowerMock.verifyAll();
	}

	@Test
	public final void testBoolean() {
		this.row.setBooleanValue(10, true);
		expect(this.row.getColumnCount()).andReturn(20);
		expect(this.row.getBooleanValue(11)).andReturn("false");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setBooleanValue(true);
		this.cell.next();
		Assert.assertEquals("false", this.cell.getBooleanValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testSpan() {
		this.row.setColumnsSpanned(10, 2);
		expect(this.row.getColumnsSpanned(10)).andReturn(2);
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setColumnsSpanned(2);
		Assert.assertEquals(2, this.cell.getColumnsSpanned());
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyFloat() {
		this.row.setCurrencyValue(10, 10.0f, "€");
		expect(this.row.getCurrency(10)).andReturn("€");
		expect(this.row.getCurrencyValue(10)).andReturn("10.0");
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
		expect(this.row.getCurrency(10)).andReturn("€");
		expect(this.row.getCurrencyValue(10)).andReturn("9.0");
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
		expect(this.row.getCurrency(10)).andReturn("€");
		expect(this.row.getCurrencyValue(10)).andReturn("10.0");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setCurrencyValue(Double.valueOf(10.0), "€");
		Assert.assertEquals("€", this.cell.getCurrency());
		Assert.assertEquals("10.0", this.cell.getCurrencyValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testCalendar() {
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(1234567891011l);
		
		this.row.setDateValue(10, c);
		expect(this.row.getDateValue(10)).andReturn("2009-02-14T00:31:31.011");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setDateValue(c);
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.cell.getDateValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testDate() {
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(1234567891011l);
		Date date = c.getTime();
		
		this.row.setDateValue(10, date);
		expect(this.row.getDateValue(10)).andReturn("2009-02-14T00:31:31.011");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setDateValue(date);
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.cell.getDateValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testFloat() {
		this.row.setFloatValue(10, 9.999f);
		expect(this.row.getFloatValue(10)).andReturn("9.999");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setFloatValue(9.999f);
		Assert.assertEquals("9.999", this.cell.getFloatValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testInt() {
		this.row.setFloatValue(10, 999);
		expect(this.row.getFloatValue(10)).andReturn("1000");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setFloatValue(999);
		Assert.assertEquals("1000", this.cell.getFloatValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testDouble() {
		this.row.setFloatValue(10, Double.valueOf(10.999));
		expect(this.row.getFloatValue(10)).andReturn("1000.99");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setFloatValue(Double.valueOf(10.999));
		Assert.assertEquals("1000.99", this.cell.getFloatValue());
		PowerMock.verifyAll();
	}

	@Test
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
		expect(this.row.getPercentageValue(10)).andReturn("0.98");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setPercentageValue(0.98f);
		Assert.assertEquals("0.98", this.cell.getPercentageValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testPercentageNumber() {
		this.row.setPercentageValue(10, Double.valueOf(0.98));
		expect(this.row.getPercentageValue(10)).andReturn("0.98");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setPercentageValue(Double.valueOf(0.98));
		Assert.assertEquals("0.98", this.cell.getPercentageValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testString() {
		this.row.setStringValue(10, "value");
		expect(this.row.getStringValue(10)).andReturn("value");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setStringValue("value");
		Assert.assertEquals("value", this.cell.getStringValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testTime() {
		this.row.setTimeValue(10, 1234567891011l);
		expect(this.row.getTimeValue(10)).andReturn("P14288DT23H31M31.11S");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setTimeValue(1234567891011l);
		Assert.assertEquals("P14288DT23H31M31.11S", this.cell.getTimeValue());
		PowerMock.verifyAll();
	}

	@Test
	public final void testTooltip() {
		this.row.setTooltip(10, "tooltip");
		expect(this.row.getTooltip(10)).andReturn("t");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setTooltip("tooltip");
		Assert.assertEquals("t", this.cell.getTooltip());
		PowerMock.verifyAll();
	}

	@Test
	public final void testStyle() {
		TableCellStyle tcs = TableCellStyle.builder("test").build();
		this.row.setStyle(10, tcs);
		expect(this.row.getStyleName(10)).andReturn("test");
		PowerMock.replayAll();
		this.cell.to(10);
		this.cell.setStyle(tcs);
		Assert.assertEquals("test", this.cell.getStyleName());
		PowerMock.verifyAll();
	}
}
