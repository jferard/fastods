/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.OdsEntry;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.StylesContainerImpl;
import com.github.jferard.fastods.odselement.UnregisteredOdsEntry;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.testlib.ZipUTF8WriterMockHandler;
import com.github.jferard.fastods.util.FastFullList;
import com.github.jferard.fastods.util.Protection;
import com.github.jferard.fastods.util.SVGRectangle;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.apache.jena.ext.com.google.common.collect.ImmutableMap;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.Logger;

public class TableAppenderTest {
    private DataStyles ds;
    private StylesContainer stc;
    private TableAppender tableAppender;
    private XMLUtil xmlUtil;
    private TableModel tm;
    private int rowIndex;
    private StylesContainer stylesContainer;

    @Before
    public void setUp()
            throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        final Constructor<?> constructor =
                StylesContainerImpl.class.getDeclaredConstructor(Logger.class);
        constructor.setAccessible(true);
        this.stylesContainer =
                (StylesContainer) constructor.newInstance(new Object[]{Logger.getLogger("")});
        this.stc = PowerMock.createMock(StylesContainerImpl.class);
        this.tm = PowerMock.createMock(TableModel.class);
        final XMLUtil xmlUtil = XMLUtil.create();
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.tableAppender = new TableAppender(this.tm);
        this.xmlUtil = xmlUtil;
    }

    @Test
    public void appendEmptyPreambleTest() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("table1");
        EasyMock.expect(this.tm.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList());
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.<TableColumnImpl>newListWithCapacity(1));
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList());
        EasyMock.expect(this.tm.getForms()).andReturn(Collections.<XMLConvertible>singletonList(
                new XMLConvertible() {
                    @Override
                    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
                            throws IOException {
                        appendable.append("FOO");
                    }
                }));

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\"><office:forms form:automatic-focus=\"false\" " +
                        "form:apply-design-mode=\"false\">FOO</office:forms><table:table-column " +
                        "table:style-name=\"co1\" table:number-columns-repeated=\"1024\" " +
                        "table:default-cell-style-name=\"Default\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public void appendShapesTest() throws IOException {
        final DrawFrame drawFrame =
                DrawFrame.builder("a", new DrawImage("href"), SVGRectangle.cm(0, 1, 2, 3)).build();

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("table1");
        EasyMock.expect(this.tm.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList());
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>singletonList(drawFrame));
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.<TableColumnImpl>newListWithCapacity(1));
        EasyMock.expect(this.tm.getForms()).andReturn(Collections.<XMLConvertible>emptyList());

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" +
                        "<table:shapes><draw:frame draw:name=\"a\" " +
                        "draw:z-index=\"0\" svg:width=\"2cm\" svg:height=\"3cm\" svg:x=\"0cm\" " +
                        "svg:y=\"1cm\"><draw:image xlink:href=\"href\" xlink:type=\"simple\" " +
                        "xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>" +
                        "</draw:frame></table:shapes>" +
                        "<table:table-column table:style-name=\"co1\" " +
                        "table:number-columns-repeated=\"1024\" " +
                        "table:default-cell-style-name=\"Default\"/>"
        );

        PowerMock.verifyAll();
    }

    @Test
    public void appendOneElementPreambleTest() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("table1");
        EasyMock.expect(this.tm.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList());
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getHeaderColumnsCount()).andReturn(0);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.newList(this.newTC("x")));
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList());
        EasyMock.expect(this.tm.getForms()).andReturn(Collections.<XMLConvertible>emptyList());

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"co1\" " +
                        "table:number-columns-repeated=\"1023\" " +
                        "table:default-cell-style-name=\"Default\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public void appendTwoElementsPreambleTest() throws IOException {
        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("table1");
        EasyMock.expect(this.tm.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList());
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getHeaderColumnsCount()).andReturn(0);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.newList(this.newTC("x"), this.newTC("x")));
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList());
        EasyMock.expect(this.tm.getForms()).andReturn(Collections.<XMLConvertible>emptyList());

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"co1\"" + " table:number-columns-repeated=\"1022\" " +
                        "table:default-cell-style-name=\"Default\"/>");

        PowerMock.verifyAll();
    }

    @Test
    public void appendFourElementsPreambleTest() throws IOException {
        final TableColumnImpl x = this.newTC("x");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("table1");
        EasyMock.expect(this.tm.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList());
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getHeaderColumnsCount()).andReturn(0);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.newList(x, x, this.newTC("y"), x));
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList());
        EasyMock.expect(this.tm.getForms()).andReturn(Collections.<XMLConvertible>emptyList());

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"y\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"x\"" + " " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"co1\"" + " " +
                        "table:number-columns-repeated=\"1020\" " +
                        "table:default-cell-style-name=\"Default\"/>");
        PowerMock.verifyAll();
    }

    @Test
    public void appendTenColumnsPreambleTest() throws IOException {
        final TableColumnImpl x = this.newTC("x");
        final TableColumnImpl y = this.newTC("y");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("table1");
        EasyMock.expect(this.tm.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList());
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getHeaderColumnsCount()).andReturn(0);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.newList(x, x, x, x, x, y, y, y, x, x));
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList());
        EasyMock.expect(this.tm.getForms()).andReturn(Collections.<XMLConvertible>emptyList());

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"5\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"y\" " +
                        "table:number-columns-repeated=\"3\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"x\" " +
                        "table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"co1\"" + " table:number-columns-repeated=\"1014\" " +
                        "table:default-cell-style-name=\"Default\"/>");
        PowerMock.verifyAll();
    }

    @Test
    public final void testName() throws IOException {
        final StringBuilder sb = new StringBuilder();

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("tb");
        EasyMock.expect(this.tm.getStyleName()).andReturn("tb-style");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList());
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.<TableColumnImpl>builder().build());
        EasyMock.expect(this.tm.getTableRowsUsedSize()).andReturn(0);
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList());
        EasyMock.expect(this.tm.getForms()).andReturn(Collections.<XMLConvertible>emptyList());
        EasyMock.expect(this.tm.getHeaderRowsCount()).andReturn(0);

        PowerMock.replayAll();
        this.tableAppender.appendAllAvailableRows(this.xmlUtil, sb);

        PowerMock.verifyAll();
        sb.append("</table:table>");
        DomTester.assertEquals("<table:table table:name=\"tb\" table:style-name=\"tb-style\" " +
                "table:print=\"false\"><table:table-column " +
                "table:style-name=\"co1\" " + "table:number-columns-repeated=\"1024\" " +
                "table:default-cell-style-name=\"Default\"/></table:table>", sb.toString());
    }

    @Test
    public final void testAppendTwoWriters() throws IOException {
        final StringBuilder sb1 = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        final FastFullList<TableColumnImpl> emptyFullList =
                FastFullList.<TableColumnImpl>builder().build();

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("tb").times(2);
        EasyMock.expect(this.tm.getStyleName()).andReturn("tb-style").times(2);
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList())
                .times(2);
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null).times(2);
        EasyMock.expect(this.tm.getProtection()).andReturn(null).times(2);
        EasyMock.expect(this.tm.getColumns()).andReturn(emptyFullList).times(2);
        EasyMock.expect(this.tm.getTableRowsUsedSize()).andReturn(0).times(2);
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList()).times(2);
        EasyMock.expect(this.tm.getForms()).andReturn(
                Collections.<XMLConvertible>emptyList()).times(2);
        EasyMock.expect(this.tm.getHeaderRowsCount()).andReturn(0).times(2);

        PowerMock.replayAll();
        this.tableAppender.appendXMLToContentEntry(this.xmlUtil, sb1);
        this.tableAppender.appendXMLToContentEntry(this.xmlUtil, sb2);

        PowerMock.verifyAll();
        DomTester.assertEquals(sb1.toString(), sb2.toString());
    }

    @Test
    public final void testAppendRows() throws IOException {
        final StringBuilder sb = new StringBuilder();
        final FastFullList<TableColumnImpl> emptyFullList =
                FastFullList.<TableColumnImpl>builder().build();
        PowerMock.resetAll();
        final TableRowImpl tr0 = this.newTR("tr0");
        final TableRowImpl tr1 = this.newTR("tr1");
        final TableRowImpl tr2 = this.newTR("tr2");
        final TableRowImpl tr3 = this.newTR("tr3");
        final TableRowImpl tr4 = this.newTR("tr4");
        EasyMock.expect(this.tm.getName()).andReturn("tb");
        EasyMock.expect(this.tm.getStyleName()).andReturn("tb-style");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList());
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getColumns()).andReturn(emptyFullList);
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList());
        EasyMock.expect(this.tm.getForms()).andReturn(
                Collections.<XMLConvertible>emptyList());
        EasyMock.expect(this.tm.getHeaderRowsCount()).andReturn(0);
        EasyMock.expect(this.tm.getTableRow(0)).andReturn(tr0);
        EasyMock.expect(this.tm.getTableRow(1)).andReturn(tr1);
        EasyMock.expect(this.tm.getTableRow(2)).andReturn(tr2);
        EasyMock.expect(this.tm.getTableRow(3)).andReturn(tr3);
        EasyMock.expect(this.tm.getTableRow(4)).andReturn(tr4);
        EasyMock.expect(this.tm.getTableRowsUsedSize()).andReturn(5);

        PowerMock.replayAll();
        this.tableAppender.appendXMLToContentEntry(this.xmlUtil, sb);

        PowerMock.verifyAll();
        DomTester.assertEquals("<table:table table:name=\"tb\" " +
                "table:style-name=\"tb-style\" " +
                "table:print=\"false\">" +
                "<table:table-column table:style-name=\"co1\" " +
                "table:number-columns-repeated=\"1024\" " +
                "table:default-cell-style-name=\"Default\"/>" +
                "<table:table-row table:style-name=\"tr0\">" +
                "<table:table-cell/>" +
                "</table:table-row><table:table-row table:style-name=\"tr1\">" +
                "<table:table-cell/>" +
                "</table:table-row><table:table-row table:style-name=\"tr2\">" +
                "<table:table-cell/>" +
                "</table:table-row><table:table-row table:style-name=\"tr3\">" +
                "<table:table-cell/>" +
                "</table:table-row><table:table-row table:style-name=\"tr4\">" +
                "<table:table-cell/>" +
                "</table:table-row>" +
                "</table:table>", sb.toString());
    }

    @Test
    public final void testAppendRowsWithHeaderRows() throws IOException {
        final StringBuilder sb = new StringBuilder();
        final FastFullList<TableColumnImpl> emptyFullList =
                FastFullList.<TableColumnImpl>builder().build();
        final TableRowImpl tr0 = this.newTR("tr0");
        final TableRowImpl tr1 = this.newTR("tr1");
        final TableRowImpl tr2 = this.newTR("tr2");
        final TableRowImpl tr3 = this.newTR("tr3");
        final TableRowImpl tr4 = this.newTR("tr4");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("tb");
        EasyMock.expect(this.tm.getStyleName()).andReturn("tb-style");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList());
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getColumns()).andReturn(emptyFullList);
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList());
        EasyMock.expect(this.tm.getForms()).andReturn(
                Collections.<XMLConvertible>emptyList());
        EasyMock.expect(this.tm.getHeaderRowsCount()).andReturn(2);
        EasyMock.expect(this.tm.getTableRow(0)).andReturn(tr0);
        EasyMock.expect(this.tm.getTableRow(1)).andReturn(tr1);
        EasyMock.expect(this.tm.getTableRow(2)).andReturn(tr2);
        EasyMock.expect(this.tm.getTableRow(3)).andReturn(tr3);
        EasyMock.expect(this.tm.getTableRow(4)).andReturn(tr4);
        EasyMock.expect(this.tm.getTableRowsUsedSize()).andReturn(5);

        PowerMock.replayAll();
        this.tableAppender.appendXMLToContentEntry(this.xmlUtil, sb);

        PowerMock.verifyAll();
        DomTester.assertEquals("<table:table table:name=\"tb\" " +
                "table:style-name=\"tb-style\" " +
                "table:print=\"false\">" +
                "<table:table-column table:style-name=\"co1\" " +
                "table:number-columns-repeated=\"1024\" " +
                "table:default-cell-style-name=\"Default\"/>" +
                "<table:table-header-rows>" +
                "<table:table-row table:style-name=\"tr0\">" +
                "<table:table-cell/>" +
                "</table:table-row><table:table-row table:style-name=\"tr1\">" +
                "<table:table-cell/>" +
                "</table:table-row>" +
                "</table:table-header-rows>" +
                "<table:table-row table:style-name=\"tr2\">" +
                "<table:table-cell/>" +
                "</table:table-row><table:table-row table:style-name=\"tr3\">" +
                "<table:table-cell/>" +
                "</table:table-row><table:table-row table:style-name=\"tr4\">" +
                "<table:table-cell/>" +
                "</table:table-row>" +
                "</table:table>", sb.toString());
    }

    @Test
    public final void testAppendRemainingRowsWithHeader() throws IOException {
        final StringBuilder sb = new StringBuilder();
        final TableRowImpl tr2 = this.newTR("tr2");
        final TableRowImpl tr3 = this.newTR("tr3");
        final TableRowImpl tr4 = this.newTR("tr4");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getHeaderRowsCount()).andReturn(2);
        EasyMock.expect(this.tm.getTableRow(2)).andReturn(tr2);
        EasyMock.expect(this.tm.getTableRow(3)).andReturn(tr3);
        EasyMock.expect(this.tm.getTableRow(4)).andReturn(tr4);
        EasyMock.expect(this.tm.getTableRowsUsedSize()).andReturn(5);

        PowerMock.replayAll();
        sb.append("<table:table><table:table-header-rows>");
        this.tableAppender.appendRemainingRowsFrom(this.xmlUtil, sb, 2);

        PowerMock.verifyAll();
        DomTester.assertEquals(
                "<table:table><table:table-header-rows></table:table-header-rows>" +
                        "<table:table-row table:style-name=\"tr2\"><table:table-cell/>" +
                        "</table:table-row><table:table-row table:style-name=\"tr3\">" +
                        "<table:table-cell/></table:table-row>" +
                        "<table:table-row table:style-name=\"tr4\"><table:table-cell/>" +
                        "</table:table-row>" +
                        "</table:table>",
                sb.toString());
    }

    @Test
    public final void testAppendRemainingRowsWithHeader2() throws IOException {
        final StringBuilder sb = new StringBuilder();
        final TableRowImpl tr3 = this.newTR("tr3");
        final TableRowImpl tr4 = this.newTR("tr4");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getHeaderRowsCount()).andReturn(2);
        EasyMock.expect(this.tm.getTableRow(3)).andReturn(tr3);
        EasyMock.expect(this.tm.getTableRow(4)).andReturn(tr4);
        EasyMock.expect(this.tm.getTableRowsUsedSize()).andReturn(5);

        PowerMock.replayAll();
        sb.append("<table:table>");
        this.tableAppender.appendRemainingRowsFrom(this.xmlUtil, sb, 3);

        PowerMock.verifyAll();
        DomTester.assertEquals(
                "<table:table><table:table-row table:style-name=\"tr3\">" +
                        "<table:table-cell/></table:table-row>" +
                        "<table:table-row table:style-name=\"tr4\"><table:table-cell/>" +
                        "</table:table-row>" +
                        "</table:table>",
                sb.toString());
    }

    @Test
    public final void testAppendSomeAvailableRowsFrom() throws IOException {
        final StringBuilder sb = new StringBuilder();
        final TableRowImpl tr0 = this.newTR("tr0");
        final TableRowImpl tr1 = this.newTR("tr1");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("table");
        EasyMock.expect(this.tm.getStyleName()).andReturn("style");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.emptyList());
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getForms()).andReturn(null);
        EasyMock.expect(this.tm.getShapes()).andReturn(null);
        EasyMock.expect(this.tm.getColumns()).andReturn(FastFullList.newListWithCapacity(0));
        EasyMock.expect(this.tm.getHeaderRowsCount()).andReturn(0);
        EasyMock.expect(this.tm.getTableRow(0)).andReturn(tr0);
        EasyMock.expect(this.tm.getTableRow(1)).andReturn(tr1);
        EasyMock.expect(this.tm.getTableRowsUsedSize()).andReturn(2);

        PowerMock.replayAll();
        this.tableAppender.appendSomeAvailableRowsFrom(this.xmlUtil, sb, 0);
        sb.append("</table:table>");

        PowerMock.verifyAll();
        DomTester.assertEquals(
                "<table:table table:name=\"table\" table:style-name=\"style\" " +
                        "table:print=\"false\">" +
                        "<table:table-column table:style-name=\"co1\" " +
                        "table:number-columns-repeated=\"1024\" " +
                        "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-row table:style-name=\"tr0\">" +
                        "<table:table-cell/>" +
                        "</table:table-row><table:table-row table:style-name=\"tr1\">" +
                        "<table:table-cell/></table:table-row>" +
                        "</table:table>",
                sb.toString());
    }

    @Test
    public final void testAppendRowsWithHeaderRows2() throws IOException {
        final StringBuilder sb = new StringBuilder();
        final FastFullList<TableColumnImpl> emptyFullList =
                FastFullList.<TableColumnImpl>builder().build();
        final TableRowImpl tr0 = this.newTR("tr0");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("tb");
        EasyMock.expect(this.tm.getStyleName()).andReturn("tb-style");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList());
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getColumns()).andReturn(emptyFullList);
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList());
        EasyMock.expect(this.tm.getForms()).andReturn(
                Collections.<XMLConvertible>emptyList());
        EasyMock.expect(this.tm.getHeaderRowsCount()).andReturn(2);
        EasyMock.expect(this.tm.getTableRow(0)).andReturn(tr0);
        EasyMock.expect(this.tm.getTableRow(1)).andReturn(null);
        EasyMock.expect(this.tm.getTableRow(2)).andReturn(null);
        EasyMock.expect(this.tm.getTableRow(3)).andReturn(null);
        EasyMock.expect(this.tm.getTableRow(4)).andReturn(tr0);
        EasyMock.expect(this.tm.getTableRowsUsedSize()).andReturn(5).anyTimes();

        PowerMock.replayAll();
        this.tableAppender.appendXMLToContentEntry(this.xmlUtil, sb);

        PowerMock.verifyAll();
        DomTester.assertEquals("<table:table table:name=\"tb\" " +
                "table:style-name=\"tb-style\" " +
                "table:print=\"false\">" +
                "<table:table-column table:style-name=\"co1\" " +
                "  table:number-columns-repeated=\"1024\" " +
                "  table:default-cell-style-name=\"Default\"/>" +
                "<table:table-header-rows>" +
                "<table:table-row table:style-name=\"tr0\">" +
                "<table:table-cell/>" +
                "</table:table-row>" +
                "<table:table-row table:style-name=\"ro1\">" +
                "<table:table-cell/>" +
                "</table:table-row>" +
                "</table:table-header-rows>" +
                "<table:table-row table:style-name=\"ro1\" " +
                "  table:number-rows-repeated=\"2\">" +
                "<table:table-cell/>" +
                "</table:table-row>" +
                "<table:table-row table:style-name=\"tr0\">" +
                "<table:table-cell/>" +
                "</table:table-row>" +
                "</table:table>", sb.toString());
    }

    @Test
    public final void testFlushRows() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);

        final TableRowImpl tr3 = this.newTR("tr3");
        final TableRowImpl tr4 = this.newTR("tr4");

        PowerMock.resetAll();

        PowerMock.replayAll();
        final UnregisteredOdsEntry entry = new UnregisteredOdsEntry("entrypath");
        writer.putNextEntry(entry);
        this.tableAppender.flushRows(this.xmlUtil, writer, Arrays.asList(tr3, tr4));

        PowerMock.verifyAll();
        Assert.assertEquals(Collections.singleton(entry.toString()), handler.getEntryNames());
        DomTester.assertEquals(
                "<table:table-row table:style-name=\"tr3\">" +
                        "<table:table-cell/></table:table-row>" +
                        "<table:table-row table:style-name=\"tr4\"><table:table-cell/>" +
                        "</table:table-row>",
                handler.getEntryAsString(entry.toString()));
    }

    @Test
    public void testHeaderColums() throws IOException {
        final TableColumnImpl x = this.newTC("x");
        final TableColumnImpl y = this.newTC("y");

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("table1");
        EasyMock.expect(this.tm.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Collections.<String>emptyList());
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(null);
        EasyMock.expect(this.tm.getProtection()).andReturn(null);
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.newList(x, x, x, x, x, y, y, y, x, x));
        EasyMock.expect(this.tm.getHeaderColumnsCount()).andReturn(2);
        EasyMock.expect(this.tm.getForms()).andReturn(Collections.<XMLConvertible>emptyList());
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList());

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print=\"false\">" +
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
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"co1\"" + " table:number-columns-repeated=\"1014\" " +
                        "table:default-cell-style-name=\"Default\"/>");
        PowerMock.verifyAll();
    }

    @Test
    public final void testAppendOpenTag() throws IOException {
        final StringBuilder sb = new StringBuilder();

        PowerMock.resetAll();
        EasyMock.expect(this.tm.getName()).andReturn("table1");
        EasyMock.expect(this.tm.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.tm.getPrintRanges()).andReturn(Arrays.asList("A5:E10", "A17:E27"));
        EasyMock.expect(this.tm.getProtection()).andReturn(new Protection("a", "b"));
        EasyMock.expect(this.tm.getCustomValueByAttribute()).andReturn(
                ImmutableMap.<String, CharSequence>of("attr", "value"));
        EasyMock.expect(this.tm.getForms()).andReturn(Collections.<XMLConvertible>emptyList());
        EasyMock.expect(this.tm.getShapes()).andReturn(Collections.<Shape>emptyList());
        EasyMock.expect(this.tm.getColumns())
                .andReturn(FastFullList.<TableColumnImpl>newListWithCapacity(1));

        PowerMock.replayAll();
        this.tableAppender.appendOpenTagAndPreamble(this.xmlUtil, sb);

        PowerMock.verifyAll();
        sb.append("</table:table>");
        DomTester.assertEquals("<table:table " +
                        "table:name=\"table1\" table:style-name=\"table-style1\" " +
                        "table:print-ranges=\"A5:E10 A17:E27\" " +
                        "table:protected=\"true\" table:protection-key=\"a\" " +
                        "table:protection-key-digest-algorithm=\"b\" attr=\"value\">" +
                        "<table:table-column table:style-name=\"co1\" " +
                        "table:number-columns-repeated=\"1024\" " +
                        "table:default-cell-style-name=\"Default\"/></table:table>",
                sb.toString());
    }

    private void assertPreambleXMLEquals(final String xml) throws IOException {
        final StringBuilder sb = new StringBuilder();
        this.tableAppender.appendOpenTagAndPreambleOnce(this.xmlUtil, sb);
        sb.append("</table:table>");
        DomTester.assertEquals(xml + "</table:table>", sb.toString());
    }

    private TableColumnImpl newTC(final String name) {
        final TableColumnStyle tcs = TableColumnStyle.builder(name).build();
        final TableColumnImpl tc = new TableColumnImpl();
        tc.setColumnStyle(tcs);
        return tc;
    }

    private TableRowImpl newTR(final String styleName) {
        final TableRowImpl tr =
                new TableRowImpl(null, null, this.stylesContainer, null, true, null, this.rowIndex,
                        10, null);
        // return PowerMock.createMock(TableRowImpl.class);
        this.rowIndex++;
        tr.setRowStyle(TableRowStyle.builder(styleName).build());
        return tr;
    }
}
