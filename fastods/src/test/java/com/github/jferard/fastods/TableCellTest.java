/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.datastyle.CurrencyStyle;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.datastyle.FloatStyle;
import com.github.jferard.fastods.datastyle.FloatStyleBuilder;
import com.github.jferard.fastods.datastyle.TimeStyle;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.StylesContainerImpl;
import com.github.jferard.fastods.style.DrawFillImage;
import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.testlib.DomTester;
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
    private static final long TIME_IN_MILLIS = 1234567891011L;
    private static final int COLUMN_INDEX = 11;
    private static final int ROW_INDEX = 10;
    private Locale locale;
    private DataStyles ds;
    private TableRowImpl row;
    private StylesContainer stc;
    private Table table;
    private TableCellStyle tcs;
    private XMLUtil xmlUtil;
    private TableCellImpl cell;
    private TableColdCell tcc;
    private ToCellValueConverter converter;

    @Before
    public void setUp() {
        this.converter = new ObjectToCellValueConverter("USD");
        this.locale = Locale.US;
        this.stc = PowerMock.createMock(StylesContainerImpl.class);
        this.table = PowerMock.createMock(Table.class);
        final WriteUtil writeUtil = WriteUtil.create();
        this.xmlUtil = XMLUtil.create();

        this.tcc = TableColdCell.create(this.xmlUtil);
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.row = new TableRowImpl(writeUtil, this.xmlUtil, this.stc, this.ds, false, this.table,
                ROW_INDEX, 100);
        this.cell = new TableCellImpl(writeUtil, this.xmlUtil, this.stc, this.ds, false, this.row,
                COLUMN_INDEX);
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
    public final void testBooleanFalse() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle booleanDataStyle = this.ds.getBooleanDataStyle();

        PowerMock.resetAll();
        this.playAddStyle(cs, booleanDataStyle);

        PowerMock.replayAll();
        this.cell.setBooleanValue(false);

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"boolean\" " +
                        "office:boolean-value=\"false\"/>");
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
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc)
                .anyTimes();

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
                "<table:table-cell table:style-name=\"name\" office:value-type=\"currency\" " +
                        "office:value=\"10.0\" " + "office:currency=\"€\" />");
    }

    @Test
    public final void testCurrencyInt() throws IOException {
        this.recordAndReplayCurrency();
        this.cell.setCurrencyValue(10, "€");

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"currency\" " +
                        "office:value=\"10\" " + "office:currency=\"€\" />");
    }

    @Test
    public final void testCurrencyNumber() throws IOException {
        this.recordAndReplayCurrency();
        this.cell.setCurrencyValue(10.0, "€");

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"currency\" " +
                        "office:value=\"10.0\" " + "office:currency=\"€\" />");
    }

    @Test
    public final void testDate() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle dateDataStyle = this.ds.getDateDataStyle();

        PowerMock.resetAll();
        this.playAddStyle(cs, dateDataStyle);

        PowerMock.replayAll();
        final Calendar d = Calendar.getInstance(this.locale);
        d.setTimeInMillis(TIME_IN_MILLIS);
        this.cell.setDateValue(d.getTime());

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"date\" " +
                        "office:date-value=\"2009-02-13T23:31:31.011Z\"/>");
    }

    @Test
    public final void testFloatNumber() throws IOException {
        this.playAndReplayFloat();
        this.cell.setFloatValue(10.999);

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" " +
                        "office:value=\"10.999\"/>");
    }

    @Test
    public final void testFloatDouble() throws IOException {
        this.playAndReplayFloat();
        this.cell.setFloatValue(9.999d);

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" " +
                        "office:value=\"9.999\"/>");
    }

    private void playAndReplayFloat() {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle floatDataStyle = this.ds.getFloatDataStyle();

        PowerMock.resetAll();
        this.playAddStyle(cs, floatDataStyle);

        PowerMock.replayAll();
    }

    @Test
    public final void testFloatFloat() throws IOException {
        this.playAndReplayFloat();
        this.cell.setFloatValue(9.999f);

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" " +
                        "office:value=\"9.999\"/>");
    }

    @Test
    public final void testFloatInt() throws IOException {
        this.playAndReplayFloat();
        this.cell.setFloatValue(999);

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" " +
                        "office:value=\"999\"/>");
    }

    @Test
    public final void testTime() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle timeDataStyle = this.ds.getTimeDataStyle();

        PowerMock.resetAll();
        this.playAddStyle(cs, timeDataStyle);

        PowerMock.replayAll();
        this.cell.setTimeValue(999);

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"time\" " +
                        "office:time-value=\"PT0.999S\"/>");
    }

    @Test
    public final void testObject() throws IOException {
        this.playAndReplayFloat();
        this.cell.setCellValue(this.converter.from(1));

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" " +
                        "office:value=\"1\"/>");
    }

    @Test
    public final void testPercentage() throws IOException {
        this.playAndReplayPercentage();
        this.cell.setPercentageValue(75.7);

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"percentage\" " +
                        "office:value=\"75.7\"/>");
    }

    private void playAndReplayPercentage() {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle percentageDataStyle = this.ds.getPercentageDataStyle();

        PowerMock.resetAll();
        this.playAddStyle(cs, percentageDataStyle);

        PowerMock.replayAll();
    }

    @Test
    public final void testPercentageInt() throws IOException {
        this.playAndReplayPercentage();
        this.cell.setPercentageValue(75);

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"percentage\" " +
                        "office:value=\"75\"/>");
    }

    @Test
    public final void testCurrency() throws IOException {
        this.recordAndReplayCurrency();
        this.cell.setCurrencyValue(75.7, "€");

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"currency\" " +
                        "office:value=\"75.7\" " + "office:currency=\"€\"/>");
    }

    @Test
    public final void testLargeTooltip() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);

        PowerMock.replayAll();
        this.cell.setTooltip("tooltip", SimpleLength.cm(1), SimpleLength.cm(2), true);

        PowerMock.verifyAll();
        this.assertCellXMLEquals("<table:table-cell>" +
                "<office:annotation office:display=\"true\" svg:width=\"1cm\" " +
                "svg:height=\"2cm\" svg:x=\"\">" + "<text:p>tooltip</text:p>" +
                "</office:annotation>" + "</table:table-cell>");
    }

    @Test
    public final void testFullTooltip() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);

        PowerMock.replayAll();
        this.cell.setTooltip(Tooltip.builder(this.xmlUtil, "tooltip").build());

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell>" + "<office:annotation>" + "<text:p>tooltip</text:p>" +
                        "</office:annotation>" + "</table:table-cell>");
    }

    @Test
    public final void testFullTooltipWithGS() throws IOException {
        final DrawFillImage dfi = new DrawFillImage("n", "h");
        final GraphicStyle gs = GraphicStyle.builder("gs").fillImage(dfi).build();

        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);
        EasyMock.expect(this.stc.addStylesStyle(dfi)).andReturn(true);
        EasyMock.expect(this.stc.addContentStyle(gs)).andReturn(true);

        PowerMock.replayAll();
        this.cell.setTooltip(Tooltip.builder(this.xmlUtil, "tooltip").graphicStyle(gs).build());

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell>" + "<office:annotation draw:style-name=\"gs\">" +
                        "<text:p>tooltip</text:p>" + "</office:annotation>" +
                        "</table:table-cell>");
    }

    @Test
    public final void testTooltip() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);

        PowerMock.replayAll();
        this.cell.setTooltip("tooltip");

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell>" + "<office:annotation>" + "<text:p>tooltip</text:p>" +
                        "</office:annotation>" + "</table:table-cell>");
    }

    @Test
    public final void testTextWithStyle() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);
        EasyMock.expect(this.stc.addContentFontFaceContainerStyle(TextStyle.DEFAULT_TEXT_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        this.cell.setText(Text.styledContent("text", TextStyle.DEFAULT_TEXT_STYLE));

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell office:value-type=\"string\" office:string-value=\"\">" +
                        "<text:p><text:span " +
                        "text:style-name=\"Default\">text</text:span></text:p>" +
                        "</table:table-cell>");
    }

    @Test
    public final void testString() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.cell.setStringValue("<NULL>");

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell office:value-type=\"string\" office:string-value=\"&lt;" +
                        "NULL&gt;\"/>");
    }

    @Test
    public final void testNullStyle() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.cell.setStyle(null);

        PowerMock.verifyAll();
        this.assertCellXMLEquals("<table:table-cell/>");
    }

    @Test
    public final void testTwoDataStyles() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle floatDataStyle = this.ds.getFloatDataStyle();
        final DataStyle percentageDataStyle = this.ds.getPercentageDataStyle();

        PowerMock.resetAll();
        this.playAddStyle(cs, percentageDataStyle);
        EasyMock.expect(this.stc.addDataStyle(floatDataStyle)).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(EasyMock.isA(TableCellStyle.class),
                EasyMock.eq(floatDataStyle))).andReturn(this.tcs);

        PowerMock.replayAll();
        this.cell.setPercentageValue(9.999f);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"percentage\" " +
                        "office:value=\"9.999\"/>");
        this.cell.setFloatValue(9.999f);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" " +
                        "office:value=\"9.999\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public final void testTwoStyles() throws IOException {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle floatDataStyle = this.ds.getFloatDataStyle();
        final TableCellStyle style = TableCellStyle.builder("x").fontStyleItalic().build();

        PowerMock.resetAll();
        EasyMock.expect(this.table.findDefaultCellStyle(COLUMN_INDEX)).andReturn(cs);
        EasyMock.expect(cs.getDataStyle()).andReturn(null);
        EasyMock.expect(this.stc.addDataStyle(floatDataStyle)).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(EasyMock.isA(TableCellStyle.class),
                EasyMock.eq(floatDataStyle))).andReturn(this.tcs);

        // second style
        EasyMock.expect(this.stc.addContentFontFaceContainerStyle(style)).andReturn(true);

        PowerMock.replayAll();
        this.cell.setFloatValue(9.999f);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"name\" office:value-type=\"float\" " +
                        "office:value=\"9.999\"/>");
        this.cell.setStyle(style);
        this.assertCellXMLEquals(
                "<table:table-cell table:style-name=\"x\" office:value-type=\"float\" " +
                        "office:value=\"9.999\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public final void testText() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);

        PowerMock.replayAll();
        this.cell.setText(Text.content("text"));

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:table-cell office:value-type=\"string\" office:string-value=\"\">" +
                        "<text:p>text</text:p>" + "</table:table-cell>");
    }

    @Test
    public final void testTextCovered() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);

        PowerMock.replayAll();
        this.cell.setCovered();
        this.cell.setText(Text.content("text"));

        PowerMock.verifyAll();
        this.assertCellXMLEquals(
                "<table:covered-table-cell office:value-type=\"string\" office:string-value=\"\">" +
                        "<text:p>text</text:p>" + "</table:covered-table-cell>");
    }

    @Test
    public final void testVoid() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.cell.setVoidValue();

        PowerMock.verifyAll();
        this.assertCellXMLEquals("<table:table-cell office:value-type=\"\" office-value=\"\"/>");
    }

    @Test
    public final void testFormula() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);

        PowerMock.replayAll();
        this.cell.setFormula("1");

        PowerMock.verifyAll();
        this.assertCellXMLEquals("<table:table-cell table:formula=\"of:=1\"/>");
    }

    @Test
    public final void testColumnsSpanned() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc)
                .times(9);

        PowerMock.replayAll();
        this.cell.setColumnsSpanned(8);
        this.cell.markColumnsSpanned(8);

        PowerMock.verifyAll();
        this.assertCellXMLEquals("<table:table-cell table:number-columns-spanned=\"8\"/>");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testMarkColumnsSpannedNeg() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.cell.markColumnsSpanned(-8);

        PowerMock.verifyAll();
    }

    @Test
    public final void testMarkColumnsSpanned1() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.cell.markColumnsSpanned(1);

        PowerMock.verifyAll();
    }

    @Test
    public final void testNoColumnsSpanned() throws IOException {
        PowerMock.replayAll();
        this.cell.setColumnsSpanned(1);

        PowerMock.verifyAll();
        this.assertCellXMLEquals("<table:table-cell/>");

        PowerMock.verifyAll();
    }

    @Test
    public final void testRowsSpanned() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);
        this.table.setRowsSpanned(ROW_INDEX, COLUMN_INDEX, 2);

        PowerMock.replayAll();
        this.cell.setRowsSpanned(1);
        this.cell.setRowsSpanned(2);
        this.cell.markRowsSpanned(2);

        PowerMock.verifyAll();
        this.assertCellXMLEquals("<table:table-cell table:number-rows-spanned=\"2\"/>");
    }

    @Test
    public final void testRowsSpannedTwice() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);
        this.table.setRowsSpanned(ROW_INDEX, COLUMN_INDEX, 2);
        this.table.setRowsSpanned(ROW_INDEX, COLUMN_INDEX, 4);

        PowerMock.replayAll();
        this.cell.setRowsSpanned(2);
        this.cell.setRowsSpanned(4);
        this.cell.markRowsSpanned(4);

        PowerMock.verifyAll();
        this.assertCellXMLEquals("<table:table-cell table:number-rows-spanned=\"4\"/>");
    }

    @Test
    public final void testNoRowsSpanned() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.cell.setRowsSpanned(1);

        PowerMock.verifyAll();
        this.assertCellXMLEquals("<table:table-cell/>");
    }

    @Test
    public final void testMarkRowsSpanned1() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.cell.markColumnsSpanned(1);

        PowerMock.verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testMarkRowsSpannedNeg() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.cell.markRowsSpanned(-8);

        PowerMock.verifyAll();
    }

    @Test
    public final void testMerge() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc)
                .anyTimes();
        this.table.setCellMerge(ROW_INDEX, COLUMN_INDEX, 7, 12);

        PowerMock.replayAll();
        this.cell.setStringValue("value");
        this.cell.setCellMerge(7, 12);

        PowerMock.verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testMergeNeg() throws IOException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.cell.setCellMerge(-10, 10);

        PowerMock.verifyAll();
    }

    @Test
    public final void testMerge1() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc)
                .anyTimes();

        PowerMock.replayAll();
        this.cell.setStringValue("value");
        this.cell.setCellMerge(1, 1);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyle() {
        final FloatStyle fs = new FloatStyleBuilder("fs", Locale.US).build();

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addDataStyle(fs)).andReturn(true);
        EasyMock.expect(this.table.findDefaultCellStyle(11)).andReturn(this.tcs);
        EasyMock.expect(this.stc.addChildCellStyle(this.tcs, fs)).andReturn(this.tcs);

        PowerMock.replayAll();
        this.cell.setDataStyle(fs);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyleNonNull() {
        final FloatStyle dataStyle = this.ds.getFloatDataStyle();

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addDataStyle(dataStyle)).andReturn(true);
        EasyMock.expect(this.table.findDefaultCellStyle(11))
                .andReturn(TableCellStyle.DEFAULT_CELL_STYLE);
        EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, dataStyle))
                .andReturn(null);

        PowerMock.replayAll();
        this.cell.setDataStyle(dataStyle);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyleNonNullPreviousStyle() {
        final DataStyle dataStyle = this.ds.getFloatDataStyle();
        final TableCellStyle cellStyle = TableCellStyle.builder("a").dataStyle(dataStyle).build();
        final CurrencyStyle newDs = this.ds.getCurrencyDataStyle();

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addContentFontFaceContainerStyle(cellStyle)).andReturn(true);
        EasyMock.expect(this.stc.addDataStyle(newDs)).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, newDs))
                .andReturn(null);

        PowerMock.replayAll();
        this.cell.setStyle(cellStyle);
        this.cell.setDataStyle(newDs);

        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyleNull() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        this.cell.setDataStyle(null);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetTimeValueMillis() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(this.table.findDefaultCellStyle(11))
                .andReturn(TableCellStyle.DEFAULT_CELL_STYLE);
        EasyMock.expect(this.stc.addDataStyle(this.ds.getTimeDataStyle())).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE,
                this.ds.getTimeDataStyle())).andReturn(null);

        PowerMock.replayAll();
        this.cell.setTimeValue(123456);

        PowerMock.verifyAll();
        Assert.assertEquals("<table:table-cell office:value-type=\"time\" " +
                "office:time-value=\"PT123.456S\"/>", this.getCellXML());
    }

    @Test
    public final void testSetTimeValueMillis0() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(this.table.findDefaultCellStyle(11))
                .andReturn(TableCellStyle.DEFAULT_CELL_STYLE);
        EasyMock.expect(this.stc.addDataStyle(this.ds.getTimeDataStyle())).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE,
                this.ds.getTimeDataStyle())).andReturn(null);

        PowerMock.replayAll();
        this.cell.setTimeValue(0);

        PowerMock.verifyAll();
        Assert.assertEquals(
                "<table:table-cell office:value-type=\"time\" " + "office:time-value=\"P0Y\"/>",
                this.getCellXML());
    }

    @Test
    public final void testSetTimeValueMillisNeg() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(this.table.findDefaultCellStyle(11))
                .andReturn(TableCellStyle.DEFAULT_CELL_STYLE);
        EasyMock.expect(this.stc.addDataStyle(this.ds.getTimeDataStyle())).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE,
                this.ds.getTimeDataStyle())).andReturn(null);

        PowerMock.replayAll();
        this.cell.setTimeValue(-987654);

        PowerMock.verifyAll();
        Assert.assertEquals(
                "<table:table-cell office:value-type=\"time\" office:time-value=\"-PT987.654S\"/>",
                this.getCellXML());
    }

    @Test
    public final void testSetTimeValue() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(this.table.findDefaultCellStyle(11))
                .andReturn(TableCellStyle.DEFAULT_CELL_STYLE);
        EasyMock.expect(this.stc.addDataStyle(this.ds.getTimeDataStyle())).andReturn(true);
        EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE,
                this.ds.getTimeDataStyle())).andReturn(null);

        PowerMock.replayAll();
        this.cell.setTimeValue(1, 2, 3, 4, 5, 6.7);

        PowerMock.verifyAll();
        Assert.assertEquals("<table:table-cell office:value-type=\"time\" " +
                "office:time-value=\"P1Y2M3DT4H5M6.7S\"/>", this.getCellXML());
    }

    @Test
    public final void testSetNegTimeValue() throws IOException {
        final FloatStyle floatDataStyle = this.ds.getFloatDataStyle();
        final TimeStyle timeDataStyle = this.ds.getTimeDataStyle();

        PowerMock.resetAll();
        EasyMock.expect(this.stc.addDataStyle(floatDataStyle)).andReturn(true);
        EasyMock.expect(this.table.findDefaultCellStyle(11))
                .andReturn(TableCellStyle.DEFAULT_CELL_STYLE);
        EasyMock.expect(
                this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, floatDataStyle))
                .andReturn(null);

        // setNeg
        EasyMock.expect(this.table.findDefaultCellStyle(11))
                .andReturn(TableCellStyle.DEFAULT_CELL_STYLE);
        EasyMock.expect(this.stc.addDataStyle(timeDataStyle)).andReturn(true);
        EasyMock.expect(
                this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, timeDataStyle))
                .andReturn(null);

        PowerMock.replayAll();
        this.cell.setDataStyle(floatDataStyle);
        this.cell.setNegTimeValue(1, 2, 3, 4, 5, 6.7);

        PowerMock.verifyAll();
        Assert.assertEquals("<table:table-cell office:value-type=\"time\" " +
                "office:time-value=\"-P1Y2M3DT4H5M6.7S\"/>", this.getCellXML());
    }

    @Test
    public void testHasValue() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertFalse(this.cell.hasValue());
        PowerMock.verifyAll();
    }

    @Test
    public final void testSetMatrix() throws IOException {
        final TableColdCell cold = PowerMock.createMock(TableColdCell.class);

        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(this.xmlUtil)).andReturn(cold);
        cold.setFormula("f");
        cold.setMatrixRowsSpanned(1);
        cold.setMatrixColumnsSpanned(1);
        EasyMock.expect(cold.isCovered()).andReturn(false);
        cold.appendXMLToTable(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        this.cell.setMatrixFormula("f");
        final String cellXML = this.getCellXML();

        PowerMock.verifyAll();
        Assert.assertEquals("<table:table-cell", cellXML);
    }

    @Test
    public final void testSetMatrixSpanned() throws IOException {
        final TableColdCell cold = PowerMock.createMock(TableColdCell.class);

        PowerMock.resetAll();
        EasyMock.expect(TableColdCell.create(this.xmlUtil)).andReturn(cold);
        cold.setFormula("f");
        cold.setMatrixRowsSpanned(2);
        cold.setMatrixColumnsSpanned(3);
        EasyMock.expect(cold.isCovered()).andReturn(true);
        cold.appendXMLToTable(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        this.cell.setMatrixFormula("f", 2, 3);
        final String cellXML = this.getCellXML();

        PowerMock.verifyAll();
        Assert.assertEquals("<table:covered-table-cell", cellXML);
    }

    @Test
    public void testAppendXML() throws IOException {
        PowerMock.resetAll();
        final StringBuilder sb = new StringBuilder();

        PowerMock.replayAll();
        this.cell.appendXMLToTableRow(this.xmlUtil, sb);

        PowerMock.verifyAll();
        DomTester.assertEquals("<table:table-cell/>", sb.toString());
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

    private void recordAndReplayCurrency() {
        final TableCellStyle cs = PowerMock.createMock(TableCellStyle.class);
        final DataStyle currencyDataStyle = this.ds.getCurrencyDataStyle();

        PowerMock.resetAll();
        this.playAddStyle(cs, currencyDataStyle);

        EasyMock.expect(TableColdCell.create(EasyMock.eq(this.xmlUtil))).andReturn(this.tcc);
        PowerMock.replayAll();
    }

}
