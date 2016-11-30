package com.github.jferard.fastods;

import java.io.IOException;
import java.util.List;
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
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.google.common.collect.Lists;

public class TableTest {
	private StylesContainer stc;
	private DataStyles ds;
	private Table table;
	private XMLUtil xmlUtil;

	@Before
	public void setUp() {
		this.stc = PowerMock.createMock(StylesContainer.class);
		final PositionUtil positionUtil = new PositionUtil();
		final XMLUtil xmlUtil = XMLUtil.create();
		this.ds = new LocaleDataStyles(
				new DataStyleBuilderFactory(xmlUtil, Locale.US), xmlUtil);
		this.table = new Table(positionUtil, new WriteUtil(), xmlUtil, this.stc,
				this.ds, "mytable", 10, 100);
		this.xmlUtil = xmlUtil;
	}

	@Test
	public final void testColumnStyles() throws FastOdsException {
		final List<TableColumnStyle> tcss = Lists.newArrayList();
		for (int c = 0; c < 10; c++) {
			final TableColumnStyle tcs = TableColumnStyle
					.builder("test" + Integer.toString(c)).build();
			this.table.setColumnStyle(c, tcs);
			tcss.add(tcs);
		}
		Assert.assertEquals(tcss, this.table.getColumnStyles());
	}

	@Test
	public final void testContentEntry() throws IOException, FastOdsException {
		for (int c = 0; c < 3; c++) {
			final TableColumnStyle tcs = TableColumnStyle
					.builder("test" + Integer.toString(c)).build();
			this.table.setColumnStyle(c, tcs);
		}
		this.table.getRow(100);

		final StringBuilder sb = new StringBuilder();
		this.table.appendXMLToContentEntry(this.xmlUtil, sb);

		Assert.assertEquals(
				"<table:table table:name=\"mytable\" table:style-name=\"ta1\" table:print=\"false\">"
						+ "<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>"
						+ "<table:table-column table:style-name=\"test0\" table:default-cell-style-name=\"Default\"/>"
						+ "<table:table-column table:style-name=\"test1\" table:default-cell-style-name=\"Default\"/>"
						+ "<table:table-column table:style-name=\"test2\" table:default-cell-style-name=\"Default\"/>"
						+ "<table:table-column table:style-name=\"co1\" table:default-cell-style-name=\"Default\"/>"
						+ "<table:table-row table:number-rows-repeated=\"100\" table:style-name=\"ro1\">"
						+ "<table:table-cell/>" + "</table:table-row>"
						+ "<table:table-row table:style-name=\"ro1\">"
						+ "</table:table-row>" + "</table:table>",
				sb.toString());

	}

	@Test
	public final void testDataWrapper() {
		final DataWrapper data = PowerMock.createMock(DataWrapper.class);
		EasyMock.expect(data.addToTable(this.table)).andReturn(true);
		PowerMock.replayAll();
		this.table.addData(data);
		PowerMock.verifyAll();
	}

	@Test
	public final void testGetRow() throws FastOdsException {
		final List<HeavyTableRow> rows = Lists.newArrayList();
		for (int r = 0; r < 7; r++) { // 8 times
			rows.add(this.table.nextRow());
		}

		for (int r = 0; r < 7; r++) { // 8 times
			Assert.assertEquals(rows.get(r), this.table.getRow(r));
		}
	}

	@Test
	public final void testGetRowFromStringPos() throws FastOdsException {
		final List<HeavyTableRow> rows = Lists.newArrayList();
		for (int r = 0; r < 7; r++) { // 8 times
			rows.add(this.table.nextRow());
		}

		Assert.assertEquals(rows.get(4), this.table.getRow("A5"));
	}

	@Test
	public final void testGetRowHundred() throws FastOdsException {
		for (int r = 0; r < 7; r++) { // 8 times
			this.table.nextRow();
		}
		this.table.getRow(100);
		Assert.assertEquals(100, this.table.getLastRowNumber());
	}

	@Test(expected = FastOdsException.class)
	public final void testGetRowNegative() throws FastOdsException {
		this.table.getRow(-1);
	}

	@Test
	public final void testLastRow() {
		Assert.assertEquals(-1, this.table.getLastRowNumber());
		for (int r = 0; r < 7; r++) { // 8 times
			this.table.nextRow();
		}
		Assert.assertEquals(6, this.table.getLastRowNumber());
	}

	@Test
	public final void testNameAndStyle() {
		this.table.setName("tname");
		this.table.setStyle(TableStyle.builder("b").build());

		Assert.assertEquals("tname", this.table.getName());
		Assert.assertEquals("b", this.table.getStyleName());
	}

	@Test
	public final void testSettingsEntry() throws IOException {
		final StringBuilder sb = new StringBuilder();
		this.table.appendXMLToSettingsEntry(this.xmlUtil, sb);

		Assert.assertEquals(
				"<config:config-item-map-entry config:name=\"mytable\">"
						+ "<config:config-item config:name=\"CursorPositionX\" config:type=\"int\">0</config:config-item>"
						+ "<config:config-item config:name=\"cursorPositionY\" config:type=\"int\">0</config:config-item>"
						+ "<config:config-item config:name=\"horizontalSplitMode\" config:type=\"short\">0</config:config-item>"
						+ "<config:config-item config:name=\"verticalSplitMode\" config:type=\"short\">0</config:config-item>"
						+ "<config:config-item config:name=\"horizontalSplitMode\" config:type=\"short\">0</config:config-item>"
						+ "<config:config-item config:name=\"verticalSplitMode\" config:type=\"short\">0</config:config-item>"
						+ "<config:config-item config:name=\"horizontalSplitPosition\" config:type=\"int\">0</config:config-item>"
						+ "<config:config-item config:name=\"verticalSplitPosition\" config:type=\"int\">0</config:config-item>"
						+ "<config:config-item config:name=\"activeSplitRange\" config:type=\"short\">2</config:config-item>"
						+ "<config:config-item config:name=\"positionLeft\" config:type=\"int\">0</config:config-item>"
						+ "<config:config-item config:name=\"PositionRight\" config:type=\"int\">0</config:config-item>"
						+ "<config:config-item config:name=\"PositionTop\" config:type=\"int\">0</config:config-item>"
						+ "<config:config-item config:name=\"positionBottom\" config:type=\"int\">0</config:config-item>"
						+ "<config:config-item config:name=\"zoomType\" config:type=\"short\">0</config:config-item>"
						+ "<config:config-item config:name=\"zoomValue\" config:type=\"int\">100</config:config-item>"
						+ "<config:config-item config:name=\"pageViewZoomValue\" config:type=\"int\">60</config:config-item>"
						+ "</config:config-item-map-entry>",
				sb.toString());

	}

	@Test
	public final void testMerge() throws IOException {
		// PLAY
		PowerMock.replayAll();
		HeavyTableRow row = this.table.nextRow();
		row.setStringValue(0, "x");
		row.setStringValue(1, "x");
		row.setStringValue(2, "x");
		row.setStringValue(3, "x");
		row = this.table.nextRow();
		row.setStringValue(0, "x");
		row.setStringValue(1, "x");
		row.setStringValue(2, "x");
		row.setStringValue(3, "x");
		row.setCellMerge(1, 2, 2);
		row = this.table.nextRow();
		row.setStringValue(0, "x");
		row.setStringValue(1, "x");
		row.setStringValue(2, "x");
		row.setStringValue(3, "x");
		row = this.table.nextRow();
		row.setStringValue(0, "x");
		row.setStringValue(1, "x");
		row.setStringValue(2, "x");
		row.setStringValue(3, "x");

		final StringBuilder sb = new StringBuilder();
		this.table.appendXMLToContentEntry(this.xmlUtil, sb);
		DomTester.assertEquals(
				"<table:table table:name=\"mytable\" table:style-name=\"ta1\" table:print=\"false\">"
						+ "<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>"
						+ "<table:table-row table:style-name=\"ro1\">"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "</table:table-row>"
						+ "<table:table-row table:style-name=\"ro1\">"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\" table:number-columns-spanned=\"2\" table:number-rows-spanned=\"2\"/>"
						+ "<table:covered-table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "</table:table-row>"
						+ "<table:table-row table:style-name=\"ro1\">"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "<table:covered-table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "<table:covered-table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "</table:table-row>"
						+ "<table:table-row table:style-name=\"ro1\">"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "<table:table-cell office:value-type=\"string\" office:string-value=\"x\"/>"
						+ "</table:table-row>" + "</table:table>",
				sb.toString());
		PowerMock.verifyAll();
	}

}
