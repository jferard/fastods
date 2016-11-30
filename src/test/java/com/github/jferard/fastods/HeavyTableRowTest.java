package com.github.jferard.fastods;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.LocaleDataStyles;
import com.github.jferard.fastods.entry.StylesContainer;
import com.github.jferard.fastods.entry.StylesEntry;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

public class HeavyTableRowTest {
	private StylesContainer stc;
	private DataStyles ds;
	private HeavyTableRow row;
	private StylesEntry se;
	private XMLUtil xmlUtil;
	private Table table;

	@Before
	public void setUp() {
		this.stc = PowerMock.createMock(StylesContainer.class);
		this.table = PowerMock.createMock(Table.class);
		final PositionUtil positionUtil = new PositionUtil();
		final WriteUtil writeUtil = new WriteUtil();
		final XMLUtil xmlUtil = new XMLUtil(new FastOdsXMLEscaper());
		this.ds = new LocaleDataStyles(
				new DataStyleBuilderFactory(xmlUtil, Locale.US), xmlUtil);
		this.row = new HeavyTableRow(positionUtil, writeUtil, xmlUtil, this.stc,
				this.ds, this.table, 10, 100);
		this.xmlUtil = XMLUtil.create();
	}

	@Test
	public final void testBoolean() {
		final TableCellStyle booleanStyle = this.ds.getBooleanStyle();
		this.stc.addDataStyle(booleanStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(booleanStyle);
		PowerMock.replayAll();
		this.row.setBooleanValue(10, true);
		Assert.assertEquals("true", this.row.getBooleanValue(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testCalendar() {
		final TableCellStyle dateStyle = this.ds.getDateStyle();
		this.stc.addDataStyle(dateStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(dateStyle);
		PowerMock.replayAll();
		final Calendar d = Calendar.getInstance();
		d.setTimeInMillis(1234567891011l);
		this.row.setDateValue(7, d);
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.row.getDateValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyFloat() {
		final TableCellStyle currencyStyle = this.ds.getCurrencyStyle();
		this.stc.addDataStyle(currencyStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(currencyStyle);
		PowerMock.replayAll();
		this.row.setCurrencyValue(10, 10.0, "€");
		Assert.assertEquals("€", this.row.getCurrency(10));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyInt() {
		final TableCellStyle currencyStyle = this.ds.getCurrencyStyle();
		this.stc.addDataStyle(currencyStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(currencyStyle);
		PowerMock.replayAll();
		this.row.setCurrencyValue(7, 10, "€");
		Assert.assertEquals("€", this.row.getCurrency(7));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyNumber() {
		final TableCellStyle currencyStyle = this.ds.getCurrencyStyle();
		this.stc.addDataStyle(currencyStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(currencyStyle);
		PowerMock.replayAll();
		this.row.setCurrencyValue(7, Double.valueOf(10.0), "€");
		Assert.assertEquals("€", this.row.getCurrency(7));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testDate() {
		final TableCellStyle dateStyle = this.ds.getDateStyle();
		this.stc.addDataStyle(dateStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(dateStyle);
		PowerMock.replayAll();
		final Calendar d = Calendar.getInstance();
		d.setTimeInMillis(1234567891011l);
		this.row.setDateValue(7, d.getTime());
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.row.getDateValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testDouble() {
		final TableCellStyle numberStyle = this.ds.getNumberStyle();
		this.stc.addDataStyle(numberStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(numberStyle);
		PowerMock.replayAll();
		this.row.setFloatValue(7, Double.valueOf(10.999));
		Assert.assertEquals("10.999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testFloatDouble() {
		final TableCellStyle numberStyle = this.ds.getNumberStyle();
		this.stc.addDataStyle(numberStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(numberStyle);
		PowerMock.replayAll();
		this.row.setFloatValue(7, 9.999d);
		Assert.assertEquals("9.999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testFloatFloat() {
		final TableCellStyle numberStyle = this.ds.getNumberStyle();
		this.stc.addDataStyle(numberStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(numberStyle);
		PowerMock.replayAll();
		this.row.setFloatValue(7, 9.999f);
		Assert.assertEquals("9.999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testInt() {
		final TableCellStyle numberStyle = this.ds.getNumberStyle();
		this.stc.addDataStyle(numberStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(numberStyle);
		PowerMock.replayAll();
		this.row.setFloatValue(7, 999);
		Assert.assertEquals("999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge() {
		HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);
		
		// PLAY
		for (int c = 11; c< 20; c++)
			EasyMock.expect(this.table.getRowSecure(c)).andReturn(row2);
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(7, 10, 8);
		Assert.assertEquals(10, this.row.getRowsSpanned(7));
		Assert.assertEquals(8, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge2() throws IOException {
		HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);
		
		// PLAY
		for (int c = 11; c< 20; c++)
			EasyMock.expect(this.table.getRowSecure(c)).andReturn(row2);
		PowerMock.replayAll();
		this.row.setStringValue(5, "value");
		this.row.setCellMerge(5, 10, 8);
		final StringBuilder sbt = new StringBuilder();
		this.row.appendXMLToTable(this.xmlUtil, sbt);
		Assert.assertEquals("<table:table-row table:style-name=\"ro1\">"
				+ "<table:table-cell table:number-columns-repeated=\"5\"/>"
				+ "<table:table-cell office:value-type=\"string\" office:string-value=\"value\" table:number-columns-spanned=\"8\" table:number-rows-spanned=\"10\"/>"
				+ "</table:table-row>", sbt.toString());
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testObject() {
		PowerMock.replayAll();
		this.row.setObjectValue(7, null);
		PowerMock.verifyAll();
	}

	@Test
	public final void testPercentageFloat() {
		final TableCellStyle percentageStyle = this.ds.getPercentageStyle();
		this.stc.addDataStyle(percentageStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(percentageStyle);
		PowerMock.replayAll();
		this.row.setPercentageValue(7, 0.98);
		Assert.assertEquals("0.98", this.row.getPercentageValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testPercentageNumber() {
		final TableCellStyle percentageStyle = this.ds.getPercentageStyle();
		this.stc.addDataStyle(percentageStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(percentageStyle);
		PowerMock.replayAll();
		this.row.setPercentageValue(7, Double.valueOf(0.98));
		Assert.assertEquals("0.98", this.row.getPercentageValue(7));
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
		this.row.setStyle(7, tcs);
		Assert.assertEquals("test", this.row.getStyleName(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testTime() {
		final TableCellStyle timeStyle = this.ds.getTimeStyle();
		this.stc.addDataStyle(timeStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(timeStyle);
		PowerMock.replayAll();
		this.row.setTimeValue(7, 1234567891011l);
		Assert.assertEquals("P14288DT23H31M31.11S", this.row.getTimeValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testTooltip() {
		PowerMock.replayAll();
		this.row.setTooltip(7, "tooltip");
		Assert.assertEquals("tooltip", this.row.getTooltip(7));
		PowerMock.verifyAll();
	}
}
