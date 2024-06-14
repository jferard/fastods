/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods;


import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.FastFullList;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class PreambleAppenderTest {
    private PreambleAppender preambleAppender;
    private XMLUtil xmlUtil;
    private TableModel tm;

    @Before
    public void setUp() {
        this.tm = PowerMock.createMock(TableModel.class);
        this.preambleAppender = new PreambleAppender(this.tm);
        this.xmlUtil = XMLUtil.create();
    }

    @Test
    public void appendTenColumnsTest() throws IOException {
        final TableColumnImpl x = this.newTC("x");
        final TableColumnImpl y = this.newTC("y");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getHeaderColumnsCount()).andReturn(0);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.newList(x, x, x, x, x, y, y, y, x, x));

        PowerMock.replayAll();
        final StringBuilder sb = new StringBuilder();
        this.preambleAppender.appendColumns(this.xmlUtil, sb);
        DomTester.assertEquals(
                "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"5\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"y\" " +
                        "table:number-columns-repeated=\"3\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"co1\"" +
                        " table:number-columns-repeated=\"1014\" " +
                        "table:default-cell-style-name=\"Default\"/>", sb.toString());
        PowerMock.verifyAll();
    }

    @Test
    public void appendEmptyColumnsTest() throws IOException {
        final TableColumnImpl x = this.newTC("x");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.<TableColumnImpl>newList());
        EasyMock.expect(this.tm.getColumnCapacity())
                .andReturn(42);

        PowerMock.replayAll();
        final StringBuilder sb = new StringBuilder();
        this.preambleAppender.appendColumns(this.xmlUtil, sb);
        DomTester.assertEquals(
                        "<table:table-column table:style-name=\"co1\"" +
                        " table:number-columns-repeated=\"42\" " +
                        "table:default-cell-style-name=\"Default\"/>", sb.toString());
        PowerMock.verifyAll();
    }

    @Test
    public void appendMissingColumnsTest() throws IOException {
        final TableColumnImpl x = this.newTC("x");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getHeaderColumnsCount()).andReturn(0);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.newList(null, null, x));

        PowerMock.replayAll();
        final StringBuilder sb = new StringBuilder();
        this.preambleAppender.appendColumns(this.xmlUtil, sb);
        DomTester.assertEquals(
                "<table:table-column table:style-name=\"co1\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"co1\" " +
                        "table:number-columns-repeated=\"1021\" " +
                        "table:default-cell-style-name=\"Default\"/>", sb.toString());
        PowerMock.verifyAll();
    }

    @Test
    public void testHeaderColumns() throws IOException {
        final TableColumnImpl x = this.newTC("x");
        final TableColumnImpl y = this.newTC("y");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getHeaderColumnsCount()).andReturn(2);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.newList(x, x, x, x, x, y, y, y, x, x));

        PowerMock.replayAll();
        final StringBuilder sb = new StringBuilder();
        this.preambleAppender.appendColumns(this.xmlUtil, sb);
        DomTester.assertEquals(
                "<table:table-header-columns>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "</table:table-header-columns>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"3\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"y\" " +
                        "table:number-columns-repeated=\"3\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"co1\"" +
                        " table:number-columns-repeated=\"1014\" " +
                        "table:default-cell-style-name=\"Default\"/>", sb.toString());
        PowerMock.verifyAll();
    }

    @Test
    public void testFiveHeaderColumns() throws IOException {
        final TableColumnImpl x = this.newTC("x");
        final TableColumnImpl y = this.newTC("y");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getHeaderColumnsCount()).andReturn(5);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.newList(x, x, x, x, x, y, y, y, x, x));

        PowerMock.replayAll();
        final StringBuilder sb = new StringBuilder();
        this.preambleAppender.appendColumns(this.xmlUtil, sb);
        DomTester.assertEquals(
                "<table:table-header-columns>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"5\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "</table:table-header-columns>" +
                        "<table:table-column table:style-name=\"y\" " +
                        "table:number-columns-repeated=\"3\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"co1\"" +
                        " table:number-columns-repeated=\"1014\" " +
                        "table:default-cell-style-name=\"Default\"/>", sb.toString());
        PowerMock.verifyAll();
    }

    @Test
    public void testTenHeaderColumns() throws IOException {
        final TableColumnImpl x = this.newTC("x");
        final TableColumnImpl y = this.newTC("y");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getHeaderColumnsCount()).andReturn(10);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.newList(x, x, x, x, x, y, y, y, x, x));

        PowerMock.replayAll();
        final StringBuilder sb = new StringBuilder();
        this.preambleAppender.appendColumns(this.xmlUtil, sb);
        DomTester.assertEquals(
                "<table:table-header-columns>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"5\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"y\" " +
                        "table:number-columns-repeated=\"3\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "</table:table-header-columns>" +
                        "<table:table-column table:style-name=\"co1\"" +
                        " table:number-columns-repeated=\"1014\" " +
                        "table:default-cell-style-name=\"Default\"/>", sb.toString());
        PowerMock.verifyAll();
    }

    @Test
    public void testElevenHeaderColumns() throws IOException {
        final TableColumnImpl x = this.newTC("x");
        final TableColumnImpl y = this.newTC("y");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getHeaderColumnsCount()).andReturn(11);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.newList(x, x, x, x, x, y, y, y, x, x));

        PowerMock.replayAll();
        final StringBuilder sb = new StringBuilder();
        this.preambleAppender.appendColumns(this.xmlUtil, sb);
        DomTester.assertEquals(
                "<table:table-header-columns>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"5\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"y\" " +
                        "table:number-columns-repeated=\"3\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"co1\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "</table:table-header-columns>" +
                        "<table:table-column table:style-name=\"co1\" " +
                        "table:number-columns-repeated=\"1013\" " +
                        "table:default-cell-style-name=\"Default\"/>", sb.toString());
        PowerMock.verifyAll();
    }

    private TableColumnImpl newTC(final String name) {
        final TableColumnStyle tcs = TableColumnStyle.builder(name).build();
        final TableColumnImpl tc = new TableColumnImpl();
        tc.setColumnStyle(tcs);
        return tc;
    }
}