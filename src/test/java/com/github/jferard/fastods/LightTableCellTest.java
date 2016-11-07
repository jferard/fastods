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

public class LightTableCellTest {
	private HeavyTableRow row;

	@Before
	public void setUp() {
		ContentEntry ce = PowerMock.createMock(ContentEntry.class);
		StylesEntry se = PowerMock.createMock(StylesEntry.class);
		Util util = new Util();
		XMLUtil xmlUtil = new XMLUtil(new FastOdsXMLEscaper());
		DataStyles ds = new LocaleDataStyles(
				new DataStyleBuilderFactory(xmlUtil, Locale.US), xmlUtil);
		this.row = new HeavyTableRow(ce, se, util, xmlUtil, ds, 10, 100);
	}

	@Test
	public final void testBoolean() {
		this.row.setBooleanValue(10, true);
		Assert.assertEquals("true", this.row.getBooleanValue(10));
	}

	@Test
	public final void testSpan() {
		this.row.setColumnsSpanned(10, 2);
		Assert.assertEquals(2, this.row.getColumnsSpanned(10));
	}

	@Test
	public final void testCurrencyFloat() {
		this.row.setCurrencyValue(10, 10.0, "€");
		Assert.assertEquals("€", this.row.getCurrency(10));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(10));
	}

	@Test
	public final void testCurrencyInt() {
		this.row.setCurrencyValue(7, 10, "€");
		Assert.assertEquals("€", this.row.getCurrency(7));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(7));
	}

	@Test
	public final void testCurrencyNumber() {
		this.row.setCurrencyValue(7, Double.valueOf(10.0), "€");
		Assert.assertEquals("€", this.row.getCurrency(7));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(7));
	}

	@Test
	public final void testCalendar() {
		final Calendar d = Calendar.getInstance();
		d.setTimeInMillis(1234567891011l);
		this.row.setDateValue(7, d);
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.row.getDateValue(7));
	}

	@Test
	public final void testDate() {
		final Calendar d = Calendar.getInstance();
		d.setTimeInMillis(1234567891011l);
		this.row.setDateValue(7, d.getTime());
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.row.getDateValue(7));
	}

	@Test
	public final void testFloat() {
		this.row.setFloatValue(7, 9.999);
		Assert.assertEquals("9.999", this.row.getFloatValue(7));
	}

	@Test
	public final void testInt() {
		this.row.setFloatValue(7, 999);
		Assert.assertEquals("999", this.row.getFloatValue(7));
	}

	@Test
	public final void testDouble() {
		this.row.setFloatValue(7, Double.valueOf(10.999));
		Assert.assertEquals("10.999", this.row.getFloatValue(7));
	}

	@Test
	public final void testObject() {
		this.row.setObjectValue(7, null);
	}

	@Test
	public final void testPercentageFloat() {
		this.row.setPercentageValue(7, 0.98);
		Assert.assertEquals("0.98", this.row.getPercentageValue(7));
	}

	@Test
	public final void testPercentageNumber() {
		this.row.setPercentageValue(7, Double.valueOf(0.98));
		Assert.assertEquals("0.98", this.row.getPercentageValue(7));
	}

	@Test
	public final void testString() {
		this.row.setStringValue(7, "value");
		Assert.assertEquals("value", this.row.getStringValue(7));
	}

	@Test
	public final void testTime() {
		this.row.setTimeValue(7, 1234567891011l);
		Assert.assertEquals("P14288DT23H31M31.11S", this.row.getTimeValue(7));
	}

	@Test
	public final void testTooltip() {
		this.row.setTooltip(7, "tooltip");
		Assert.assertEquals("tooltip", this.row.getTooltip(7));
	}

	@Test
	public final void testStyle() {
		TableCellStyle tcs = TableCellStyle.builder("test").build();
		this.row.setStyle(7, tcs);
		Assert.assertEquals("test", this.row.getStyleName(7));
	}
}
