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

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
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
@PrepareForTest(TableColdCell.class)
public class TableCellTest {
    public static final long TIME_IN_MILLIS = 1234567891011L;
    private static final int COLUMN_INDEX = 11;
    private static final int ROW_INDEX = 10;
    private Locale locale;
    private DataStyles ds;
    private TableRow row;
    private StylesContainer stc;
    private Table table;
    private TableCellStyle tcs;
    private XMLUtil xmlUtil;
    private TableCell cell;
    private TableColdCell tcc;

    @Before
    public void setUp() throws FastOdsException {
        this.locale = Locale.US;
        this.stc = PowerMock.createMock(StylesContainer.class);
        this.table = PowerMock.createMock(Table.class);
        final WriteUtil writeUtil = WriteUtil.create();
        this.xmlUtil = XMLUtil.create();

        this.tcc = TableColdCell.create(this.xmlUtil);
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.row = new TableRow(writeUtil, this.xmlUtil, this.stc, this.ds, false, this.table, ROW_INDEX, 100);
        this.cell = new TableCellImpl(writeUtil, this.xmlUtil, this.stc, this.ds, false,
                this.row, COLUMN_INDEX);
        this.tcs = TableCellStyle.builder("name").build();
        PowerMock.mockStatic(TableColdCell.class);
    }

    @Test
    public final void testBoolean() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle booleanDataStyle = this.ds.getBooleanDataStyle();

        PowerMock.resetAll();
        this.playAddStyle(cs, booleanDataStyle);

        PowerMock.replayAll();
        this.cell.setBooleanValue(true);

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"boolean\" " +
                        "office:boolean-value=\"true\"/>");
    }

    @Test
    public final void testCalendar() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle dateDataStyle = this.ds.getDateDataStyle();
        final Calendar d = Calendar.getInstance(this.locale);
        d.setTimeInMillis(TIME_IN_MILLIS);

        PowerMock.resetAll();
        this.playAddStyle(cs, dateDataStyle);

        PowerMock.replayAll();
        this.cell.setDateValue(d);

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"date\" " +
                        "office:date-value=\"2009-02-13T23:31:31.011Z\"/>");
    }

    @Test
    public final void testCovered() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc).anyTimes();

        PowerMock.replayAll();
        Assert.assertFalse(this.cell.isCovered());
        this.cell.setCovered();
        Assert.assertTrue(this.cell.isCovered());
        this.cell.setCovered();

        PowerMock.verifyAll();
        this.assertCellXMLEquals("<table:covered-table-cell/>");
    }

    @Test
    public final void testCurrencyFloat() throws IOException {
        this.recordAndReplayCurrency();
        this.cell.setCurrencyValue(10.0f, "€");

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"currency\" office:value=\"10.0\" "
                        + "office:currency=\"€\" />");
    }

    private void recordAndReplayCurrency() {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle currencyDataStyle = this.ds.getCurrencyDataStyle();

        PowerMock.resetAll();
        this.playAddStyle(cs, currencyDataStyle);

        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);
        PowerMock.replayAll();
    }

    @Test
    public final void testCurrencyInt() throws IOException {
        this.recordAndReplayCurrency();
        this.cell.setCurrencyValue(10, "€");
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"currency\" office:value=\"10\" " +
                        "office:currency=\"€\" />");
    }

    @Test
    public final void testCurrencyNumber() throws IOException {
        this.recordAndReplayCurrency();
        this.cell.setCurrencyValue(10.0, "€");
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"currency\" office:value=\"10.0\" "
                        + "office:currency=\"€\" />");
    }

    @Test
    public final void testDate() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle dateDataStyle = this.ds.getDateDataStyle();

        // PLAY
        this.playAddStyle(cs, dateDataStyle);

        PowerMock.replayAll();
        final Calendar d = Calendar.getInstance(this.locale);
        d.setTimeInMillis(TIME_IN_MILLIS);
        this.cell.setDateValue(d.getTime());
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"date\" " +
                        "office:date-value=\"2009-02-13T23:31:31.011Z\"/>");
    }

    @Test
    public final void testDouble() throws IOException {
        this.playAndReplayFloat();
        this.cell.setFloatValue(10.999);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" office:value=\"10.999\"/>");
    }

    @Test
    public final void testFloatDouble() throws IOException {
        this.playAndReplayFloat();
        this.cell.setFloatValue(9.999d);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" office:value=\"9.999\"/>");
    }

    private void playAndReplayFloat() {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle numberDataStyle = this.ds.getNumberDataStyle();

        // PLAY
        this.playAddStyle(cs, numberDataStyle);

        PowerMock.replayAll();
    }

    @Test
    public final void testFloatFloat() throws IOException {
        this.playAndReplayFloat();
        this.cell.setFloatValue(9.999f);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" office:value=\"9.999\"/>");
    }

    @Test
    public final void testInt() throws IOException {
        this.playAndReplayFloat();
        this.cell.setFloatValue(999);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" office:value=\"999\"/>");
    }

    @Test
    public final void testTime() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle timeDataStyle = this.ds.getTimeDataStyle();

        // PLAY
        this.playAddStyle(cs, timeDataStyle);

        PowerMock.replayAll();
        this.cell.setTimeValue(999);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"time\" " +
                        "office:time-value=\"PT00H00M00.999S\"/>");
    }

    @Test
    public final void testObject() throws IOException {
        this.playAndReplayFloat();
        this.cell.setObjectValue(1);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" office:value=\"1\"/>");
    }

    @Test
    public final void testPercentage() throws IOException {
        this.playAndReplayPercentage();
        this.cell.setPercentageValue(75.7);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"percentage\" " +
                        "office:value=\"75.7\"/>");
    }

    private void playAndReplayPercentage() {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle percentageDataStyle = this.ds.getPercentageDataStyle();

        // PLAY
        this.playAddStyle(cs, percentageDataStyle);

        PowerMock.replayAll();
    }

    @Test
    public final void testPercentageInt() throws IOException {
        this.playAndReplayPercentage();
        this.cell.setPercentageValue(75);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"percentage\" office:value=\"75\"/>");
    }

    @Test
    public final void testCurrency() throws IOException {
        this.recordAndReplayCurrency();
        this.cell.setCurrencyValue(75.7, "€");
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"currency\" office:value=\"75.7\" "
                        + "office:currency=\"€\"/>");
    }

    @Test
    public final void testFullTooltip() throws IOException {
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);

        // PLAY
        PowerMock.replayAll();
        this.cell.setTooltip("tooltip", SimpleLength.cm(1), SimpleLength.cm(2), true);
        this.assertCellXMLEquals(
                "<table:table-cell>" + "<office:annotation office:display=\"true\" svg:width=\"1cm\" " +
                        "svg:height=\"2cm\" svg:x=\"\">" + "<text:p>tooltip</text:p>" + "</office:annotation>" +
                        "</table:table-cell>");
    }

    @Test
    public final void testTooltip() throws IOException {
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);

        // PLAY
        PowerMock.replayAll();
        this.cell.setTooltip("tooltip");
        this.assertCellXMLEquals(
                "<table:table-cell>" + "<office:annotation>" + "<text:p>tooltip</text:p>" + "</office:annotation>" +
                        "</table:table-cell>");
    }

    @Test
    public final void testTextWithStyle() throws IOException {
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);
        EasyMock.expect(this.stc.addContentFontFaceContainerStyle(TextStyle.DEFAULT_TEXT_STYLE)).andReturn(true);

        PowerMock.replayAll();
        this.cell.setText(Text.styledContent("text", TextStyle.DEFAULT_TEXT_STYLE));
        this.assertCellXMLEquals(
                "<table:table-cell office:value-type=\"string\" office:string-value=\"\">" + "<text:p><text:span " +
                        "text:style-name=\"Default\">text</text:span></text:p>" + "</table:table-cell>");
    }

    @Test
    public final void testString() throws IOException {
        PowerMock.replayAll();
        this.cell.setStringValue("<NULL>");
        this.assertCellXMLEquals(
                "<table:table-cell office:value-type=\"string\" office:string-value=\"&lt;NULL&gt;\"/>");
    }

    @Test
    public final void testNullStyle() throws IOException {
        PowerMock.replayAll();
        this.cell.setStyle(null);
        this.assertCellXMLEquals("<table:table-cell/>");
    }

    @Test
    public final void testTwoDataStyles() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle numberDataStyle = this.ds.getNumberDataStyle();
        final DataStyle percentageDataStyle = this.ds.getPercentageDataStyle();

        // PLAY
        this.playAddStyle(cs, percentageDataStyle);
        EasyMock.expect(this.stc.addDataStyle(numberDataStyle)).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(EasyMock.isA(TableCellStyle.class), EasyMock.eq(numberDataStyle)))
                .andReturn(this.tcs);

        PowerMock.replayAll();
        this.cell.setPercentageValue(9.999f);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"percentage\" " +
                        "office:value=\"9.999\"/>");
        this.cell.setFloatValue(9.999f);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" office:value=\"9.999\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public final void testTwoStyles() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle numberDataStyle = this.ds.getNumberDataStyle();
        final TableCellStyle style = TableCellStyle.builder("x").fontStyleItalic().build();

        // PLAY
        EasyMock.expect(this.table.findDefaultCellStyle(COLUMN_INDEX)).andReturn(cs);
        EasyMock.expect(cs.getDataStyle()).andReturn(null);
        EasyMock.expect(this.stc.addDataStyle(numberDataStyle)).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(EasyMock.isA(TableCellStyle.class), EasyMock.eq(numberDataStyle)))
                .andReturn(this.tcs);

        // second style
        EasyMock.expect(this.stc.addContentFontFaceContainerStyle(style)).andReturn(true);

        PowerMock.replayAll();
        this.cell.setFloatValue(9.999f);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" office:value=\"9.999\"/>");
        this.cell.setStyle(style);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"x\" office:value-type=\"float\" office:value=\"9.999\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public final void testText() throws IOException {
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);

        PowerMock.replayAll();
        this.cell.setText(Text.content("text"));
        this.assertCellXMLEquals(
                "<table:table-cell office:value-type=\"string\" office:string-value=\"\">" + "<text:p>text</text:p>"
                        + "</table:table-cell>");
    }

    @Test
    public final void testVoid() throws IOException {
        PowerMock.replayAll();
        this.cell.setVoidValue();
        this.assertCellXMLEquals("<table:table-cell office:value-type=\"\" office-value=\"\"/>");
    }

    @Test
    public final void testFormula() throws IOException {
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);

        PowerMock.replayAll();
        this.cell.setFormula("1");
        this.assertCellXMLEquals("<table:table-cell table:formula=\"=1\"/>");
    }

    @Test
    public final void testColumnsSpanned() throws IOException {
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc).times(9);

        PowerMock.replayAll();
        this.cell.setColumnsSpanned(8);
        this.cell.markColumnsSpanned(8);
        this.assertCellXMLEquals("<table:table-cell table:number-columns-spanned=\"8\"/>");
    }

    @Test
    public final void testNoColumnsSpanned() throws IOException {
        PowerMock.replayAll();
        this.cell.setColumnsSpanned(1);
        this.assertCellXMLEquals("<table:table-cell/>");
    }

    @Test
    public final void testRowsSpanned() throws IOException {
        // PLAY
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);
        this.table.setRowsSpanned(ROW_INDEX, COLUMN_INDEX, 2);

        PowerMock.replayAll();
        this.cell.setRowsSpanned(1);
        this.cell.setRowsSpanned(2);
        this.cell.markRowsSpanned(2);
        this.assertCellXMLEquals("<table:table-cell table:number-rows-spanned=\"2\"/>");
    }

    @Test
    public final void testRowsSpannedTwice() throws IOException {
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);
        this.table.setRowsSpanned(ROW_INDEX, COLUMN_INDEX, 2);
        this.table.setRowsSpanned(ROW_INDEX, COLUMN_INDEX, 4);

        PowerMock.replayAll();
        this.cell.setRowsSpanned(2);
        this.cell.setRowsSpanned(4);
        this.cell.markRowsSpanned(4);
        this.assertCellXMLEquals("<table:table-cell table:number-rows-spanned=\"4\"/>");
    }

    @Test
    public final void testNoRowsSpanned() throws IOException {
        PowerMock.replayAll();
        this.cell.setRowsSpanned(1);
        this.assertCellXMLEquals("<table:table-cell/>");
    }

    @Test
    public final void testMerge() throws IOException, FastOdsException {
        // PLAY
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc).anyTimes();
        this.table.setCellMerge(ROW_INDEX, COLUMN_INDEX, 7, 12);

        PowerMock.replayAll();
        this.cell.setStringValue("value");
        this.cell.setCellMerge(7, 12);
    }


    private void assertCellXMLEquals(final String xml) throws IOException {
        DomTester.assertEquals(xml, this.getCellXML());
    }

    private String getCellXML() throws IOException {
        final StringBuilder sb = new StringBuilder();
        this.cell.appendXMLToTableRow(this.xmlUtil, sb);
        return sb.toString();
    }

    private void playAddStyle(final TableCellStyle cs, final DataStyle dataStyle) {
        EasyMock.expect(this.table.findDefaultCellStyle(COLUMN_INDEX)).andReturn(cs);
        EasyMock.expect(this.stc.addDataStyle(dataStyle)).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(cs, dataStyle)).andReturn(this.tcs);
        EasyMock.expect(cs.getDataStyle()).andReturn(null);
    }
}
