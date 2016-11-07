package com.github.jferard.fastods;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.LocaleDataStyles;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.XMLUtil;

public class HeavyTableRowTest {
	private HeavyTableRow row;
	private LightTableCell cell;

	@Before
	public void setUp() {
		ContentEntry ce = PowerMock.createMock(ContentEntry.class);
		StylesEntry se = PowerMock.createMock(StylesEntry.class);
		Util util = new Util();
		XMLUtil xmlUtil = new XMLUtil(new FastOdsXMLEscaper());
		DataStyles ds = new LocaleDataStyles(
				new DataStyleBuilderFactory(xmlUtil, Locale.US), xmlUtil);
		this.row = new HeavyTableRow(ce, se, util, xmlUtil, ds, 10, 100);
		this.cell = new LightTableCell(this.row);
	}

	@Test
	public final void testMove() {
		this.cell.to(49);
		this.cell.setStringValue("s");
		this.cell.to(0);
		Assert.assertTrue(this.cell.hasNext());
		Assert.assertFalse(this.cell.hasPrevious());
		this.cell.next();
		this.cell.lastCell();
		this.cell.to(48);
		Assert.assertTrue(this.cell.hasNext());
		this.cell.next();
		Assert.assertFalse(this.cell.hasNext());
		this.cell.to(100);
		Assert.assertFalse(this.cell.hasPrevious());
		this.cell.to(50);
		Assert.assertTrue(this.cell.hasPrevious());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public final void testMoveNegative() {
		this.cell.to(-1);
	}

	@Test
	public final void testBoolean() {
		this.cell.to(10);
		this.cell.setBooleanValue(true);
		Assert.assertEquals("true", this.cell.getBooleanValue());
	}

	@Test
	public final void testSpan() {
		this.cell.to(10);
		this.cell.setColumnsSpanned(2);
		Assert.assertEquals(2, this.cell.getColumnsSpanned());
	}

	@Test
	public final void testCurrencyFloat() {
		this.cell.to(10);
		this.cell.setCurrencyValue(10.0, "€");
		Assert.assertEquals("€", this.cell.getCurrency());
		Assert.assertEquals("10.0", this.cell.getCurrencyValue());
	}

	@Test
	public final void testCurrencyInt() {
		this.cell.to(10);
		this.cell.setCurrencyValue(10, "€");
		Assert.assertEquals("€", this.cell.getCurrency());
		Assert.assertEquals("10.0", this.cell.getCurrencyValue());
	}

	@Test
	public final void testCurrencyNumber() {
		this.cell.to(10);
		this.cell.setCurrencyValue(Double.valueOf(10.0), "€");
		Assert.assertEquals("€", this.cell.getCurrency());
		Assert.assertEquals("10.0", this.cell.getCurrencyValue());
	}

	@Test
	public final void testCalendar() {
		this.cell.to(10);
		final Calendar d = Calendar.getInstance();
		d.setTimeInMillis(1234567891011l);
		this.cell.setDateValue(d);
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.cell.getDateValue());
	}

	@Test
	public final void testDate() {
		this.cell.to(10);
		final Calendar d = Calendar.getInstance();
		d.setTimeInMillis(1234567891011l);
		this.cell.setDateValue(d.getTime());
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.cell.getDateValue());
	}

	@Test
	public final void testFloat() {
		this.cell.to(10);
		this.cell.setFloatValue(9.999);
		Assert.assertEquals("9.999", this.cell.getFloatValue());
	}

	@Test
	public final void testInt() {
		this.cell.to(10);
		this.cell.setFloatValue(999);
		Assert.assertEquals("999", this.cell.getFloatValue());
	}

	@Test
	public final void testDouble() {
		this.cell.to(10);
		this.cell.setFloatValue(Double.valueOf(10.999));
		Assert.assertEquals("10.999", this.cell.getFloatValue());
	}

	@Test
	public final void testObject() {
		this.cell.to(10);
		this.cell.setObjectValue(null);
	}

	@Test
	public final void testPercentageFloat() {
		this.cell.to(10);
		this.cell.setPercentageValue(0.98);
		Assert.assertEquals("0.98", this.cell.getPercentageValue());
	}

	@Test
	public final void testPercentageNumber() {
		this.cell.to(10);
		this.cell.setPercentageValue(Double.valueOf(0.98));
		Assert.assertEquals("0.98", this.cell.getPercentageValue());
	}

	@Test
	public final void testString() {
		this.cell.to(10);
		this.cell.setStringValue("value");
		Assert.assertEquals("value", this.cell.getStringValue());
	}

	@Test
	public final void testTime() {
		this.cell.to(10);
		this.cell.setTimeValue(1234567891011l);
		Assert.assertEquals("P14288DT23H31M31.11S", this.cell.getTimeValue());
	}

	@Test
	public final void testTooltip() {
		this.cell.to(10);
		this.cell.setTooltip("tooltip");
		Assert.assertEquals("tooltip", this.cell.getTooltip());
	}

	@Test
	public final void testStyle() {
		TableCellStyle tcs = TableCellStyle.builder("test").build();
		this.cell.to(10);
		this.cell.setStyle(tcs);
		Assert.assertEquals("test", this.cell.getStyleName());
	}
}
