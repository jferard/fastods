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

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesFactory;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.google.common.collect.Lists;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TableTest {
    private DataStyles ds;
    private StylesContainer stc;
    private Table table;
    private XMLUtil xmlUtil;

    @Before
    public void setUp() {
        this.stc = PowerMock.createMock(StylesContainer.class);
        final PositionUtil positionUtil = new PositionUtil(new EqualityUtil());
        final XMLUtil xmlUtil = XMLUtil.create();
        this.ds = DataStylesFactory.create(xmlUtil, Locale.US);
        this.table = new Table(positionUtil, WriteUtil.create(), xmlUtil,
                this.stc, this.ds, "mytable", 10, 100);
        this.xmlUtil = xmlUtil;
    }

    @Test
    public final void testColumnStyles() throws FastOdsException {
        final List<TableColumnStyle> tcss = Lists.newArrayList();
        for (int c = 0; c < 10; c++) {
            final TableColumnStyle tcs = TableColumnStyle
                    .builder("test" + Integer.toString(c)).build();
            tcss.add(tcs);
        }

        // PLAY
        for (int c = 0; c < 10; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            this.stc.addStyleToContentAutomaticStyles(tcs);
        }
        PowerMock.replayAll();

        for (int c = 0; c < 10; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            this.table.setColumnStyle(c, tcs);
        }
        Assert.assertEquals(tcss, this.table.getColumnStyles());
        PowerMock.verifyAll();
    }

    @Test
    public final void testContentEntry() throws IOException, FastOdsException {
        final List<TableColumnStyle> tcss = new ArrayList<TableColumnStyle>(4);
        for (int c = 0; c < 3; c++) {
            final TableColumnStyle tcs = TableColumnStyle
                    .builder("test" + Integer.toString(c)).build();
            tcss.add(tcs);
        }
        final StringBuilder sb = new StringBuilder();


        // PLAY
        for (int c = 0; c < 3; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            this.stc.addStyleToContentAutomaticStyles(tcs);
        }

        PowerMock.replayAll();
        for (int c = 0; c < 3; c++) {
            final TableColumnStyle tcs = tcss.get(c);
            this.table.setColumnStyle(c, tcs);
        }
        this.table.getRow(100);

        this.table.appendXMLToContentEntry(this.xmlUtil, sb);

        DomTester.assertEquals(
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
        PowerMock.verifyAll();
    }

    @Test
    public final void testDataWrapper() throws IOException {
        final DataWrapper data = PowerMock.createMock(DataWrapper.class);
        EasyMock.expect(data.addToTable(this.table)).andReturn(true);
        PowerMock.replayAll();
        this.table.addData(data);
        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRow() throws FastOdsException, IOException {
        PowerMock.replayAll();
        final List<TableRow> rows = Lists.newArrayList();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.table.nextRow());
        }

        for (int r = 0; r < 7; r++) { // 8 times
            Assert.assertEquals(rows.get(r), this.table.getRow(r));
        }
        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRowFromStringPos() throws FastOdsException, IOException {
        PowerMock.replayAll();
        final List<TableRow> rows = Lists.newArrayList();
        for (int r = 0; r < 7; r++) { // 8 times
            rows.add(this.table.nextRow());
        }

        Assert.assertEquals(rows.get(4), this.table.getRow("A5"));
        PowerMock.verifyAll();
    }

    @Test
    public final void testGetRowHundred() throws FastOdsException, IOException {
        PowerMock.replayAll();
        for (int r = 0; r < 7; r++) { // 8 times
            this.table.nextRow();
        }
        this.table.getRow(100);
        Assert.assertEquals(100, this.table.getLastRowNumber());
        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public final void testGetRowNegative() throws FastOdsException, IOException {
        PowerMock.replayAll();
        this.table.getRow(-1);
        PowerMock.verifyAll();
    }

    @Test
    public final void testLastRow() throws IOException {
        PowerMock.replayAll();
        Assert.assertEquals(-1, this.table.getLastRowNumber());
        for (int r = 0; r < 7; r++) { // 8 times
            this.table.nextRow();
        }
        Assert.assertEquals(6, this.table.getLastRowNumber());
        PowerMock.verifyAll();
    }

    @Test
    public final void testMerge() throws IOException {
        final StringBuilder sb = new StringBuilder();

        // PLAY
        PowerMock.replayAll();
        TableRow row = this.table.nextRow();
        final TableCellWalker walker = row.getWalker();

        for (int i=0; i<4; i++) {
            walker.setStringValue("x");
            walker.next();
        }
        row = this.table.nextRow();
        for (int i=0; i<4; i++) {
            walker.setStringValue("x");
            walker.next();
        }
        row.setCellMerge(1, 2, 2);
        row = this.table.nextRow();
        for (int i=0; i<4; i++) {
            walker.setStringValue("x");
            walker.next();
        }
        row = this.table.nextRow();
        for (int i=0; i<4; i++) {
            walker.setStringValue("x");
            walker.next();
        }

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

    @Test
    public final void testNameAndStyle() {
        // PLAY
        final TableStyle ts = TableStyle.builder("b").build();
        this.stc.addStyleToContentAutomaticStyles(ts);
        this.stc.addPageStyle(ts.getPageStyle());

        PowerMock.replayAll();

        this.table.setName("tname");
        this.table.setStyle(ts);
        Assert.assertEquals("tname", this.table.getName());
        Assert.assertEquals("b", this.table.getStyleName());
        PowerMock.verifyAll();

    }

    @Test
    public final void testSettingsEntry() throws IOException {
        final StringBuilder sb = new StringBuilder();
        PowerMock.replayAll();

        this.table.appendXMLToSettingsElement(this.xmlUtil, sb);

        DomTester.assertUnsortedEquals(
                "<config:config-item-map-entry config:name=\"mytable\">"
                        + "<config:config-item config:name=\"CursorPositionX\" config:type=\"int\">0</config:config-item>"
                        + "<config:config-item config:name=\"CursorPositionY\" config:type=\"int\">0</config:config-item>"
                        + "<config:config-item config:name=\"HorizontalSplitMode\" config:type=\"short\">0</config:config-item>"
                        + "<config:config-item config:name=\"VerticalSplitMode\" config:type=\"short\">0</config:config-item>"
                        + "<config:config-item config:name=\"HorizontalSplitPosition\" config:type=\"int\">0</config:config-item>"
                        + "<config:config-item config:name=\"VerticalSplitPosition\" config:type=\"int\">0</config:config-item>"
                        + "<config:config-item config:name=\"ActiveSplitRange\" config:type=\"short\">2</config:config-item>"
                        + "<config:config-item config:name=\"PositionLeft\" config:type=\"int\">0</config:config-item>"
                        + "<config:config-item config:name=\"PositionRight\" config:type=\"int\">0</config:config-item>"
                        + "<config:config-item config:name=\"PositionTop\" config:type=\"int\">0</config:config-item>"
                        + "<config:config-item config:name=\"PositionBottom\" config:type=\"int\">0</config:config-item>"
                        + "<config:config-item config:name=\"ZoomType\" config:type=\"short\">0</config:config-item>"
                        + "<config:config-item config:name=\"ZoomValue\" config:type=\"int\">100</config:config-item>"
                        + "<config:config-item config:name=\"PageViewZoomValue\" config:type=\"int\">60</config:config-item>"
                        + "</config:config-item-map-entry>",
                sb.toString());
        PowerMock.verifyAll();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetColumnStyleAfterPreamble() throws IOException, FastOdsException {
        final StringBuilder sb = new StringBuilder();
        PowerMock.replayAll();
        this.table.appendPreamble(this.xmlUtil, sb);
        this.table.setColumnStyle(0, TableColumnStyle.builder("a").build());
        PowerMock.verifyAll();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetNameAfterPreamble() throws IOException {
        final StringBuilder sb = new StringBuilder();
        PowerMock.replayAll();
        this.table.appendPreamble(this.xmlUtil, sb);
        this.table.setName("t");
        PowerMock.verifyAll();
    }
}
