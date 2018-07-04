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
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.FastFullList;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.TableNameUtil;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Locale;

public class TableAppenderTest {
    private DataStyles ds;
    private StylesContainer stc;
    private TableAppender tableAppender;
    private XMLUtil xmlUtil;
    private TableBuilder builder;

    @Before
    public void setUp() {
        this.stc = PowerMock.createMock(StylesContainer.class);
        this.builder = PowerMock.createMock(TableBuilder.class);
        final PositionUtil positionUtil = new PositionUtil(new EqualityUtil(), new TableNameUtil());
        final XMLUtil xmlUtil = XMLUtil.create();
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.tableAppender = new TableAppender(this.builder);
        this.xmlUtil = xmlUtil;
        PowerMock.resetAll();
    }

    @Test
    public void appendEmptyPreambleTest() throws IOException {
        EasyMock.expect(this.builder.getName()).andReturn("table1");
        EasyMock.expect(this.builder.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.builder.getColumnStyles()).andReturn(FastFullList.<TableColumnStyle>newListWithCapacity(1));

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" table:print=\"false\">" +
                        "<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"co1\" table:number-columns-repeated=\"1024\" " +
                        "table:default-cell-style-name=\"Default\"/>");

    }

    @Test
    public void appendOneElementPreambleTest() throws IOException {
        EasyMock.expect(this.builder.getName()).andReturn("table1");
        EasyMock.expect(this.builder.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.builder.getColumnStyles()).andReturn(FastFullList.newList(this.newTCS("x")));

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" table:print=\"false\">" +
                        "<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"x\" table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"co1\" table:number-columns-repeated=\"1023\" " +
                        "table:default-cell-style-name=\"Default\"/>");
        PowerMock.verifyAll();
    }

    private TableColumnStyle newTCS(final String name) {
        return TableColumnStyle.builder(name).build();
    }

    @Test
    public void appendTwoElementsPreambleTest() throws IOException {
        EasyMock.expect(this.builder.getName()).andReturn("table1");
        EasyMock.expect(this.builder.getStyleName()).andReturn("table-style1");
        EasyMock.expect(this.builder.getColumnStyles()).andReturn(
                FastFullList.newList(this.newTCS("x"), this.newTCS("x")));

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" table:print=\"false\">" +
                        "<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"x\" table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"co1\"" + " table:number-columns-repeated=\"1022\" " +
                        "table:default-cell-style-name=\"Default\"/>");
        PowerMock.verifyAll();
    }

    @Test
    public void appendFourElementsPreambleTest() throws IOException {
        EasyMock.expect(this.builder.getName()).andReturn("table1");
        EasyMock.expect(this.builder.getStyleName()).andReturn("table-style1");
        final TableColumnStyle x = this.newTCS("x");
        EasyMock.expect(this.builder.getColumnStyles()).andReturn(
                FastFullList.newList(x, x, this.newTCS("y"), x));

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" table:print=\"false\">" +
                        "<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"x\" table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column table:style-name=\"y\" "
                        + "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"x\"" + " " + "table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column " + "table:style-name=\"co1\"" + " " +
                        "table:number-columns-repeated=\"1020\" " + "table:default-cell-style-name=\"Default\"/>");
        PowerMock.verifyAll();
    }

    @Test
    public void appendTenElementsPreambleTest() throws IOException {
        EasyMock.expect(this.builder.getName()).andReturn("table1");
        EasyMock.expect(this.builder.getStyleName()).andReturn("table-style1");
        final TableColumnStyle x = this.newTCS("x");
        final TableColumnStyle y = this.newTCS("y");
        EasyMock.expect(this.builder.getColumnStyles()).andReturn(
                FastFullList.newList(x, x, x, x, x, y, y, y, x, x));

        PowerMock.replayAll();
        this.assertPreambleXMLEquals(
                "<table:table table:name=\"table1\" table:style-name=\"table-style1\" table:print=\"false\">" +
                        "<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>" +
                        "<table:table-column table:style-name=\"x\" table:number-columns-repeated=\"5\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column table:style-name=\"y\" "
                        + "table:number-columns-repeated=\"3\" table:default-cell-style-name=\"Default\"/>" +
                        "<table:table-column table:style-name=\"x\" table:number-columns-repeated=\"2\" " +
                        "table:default-cell-style-name=\"Default\"/>" + "<table:table-column " +
                        "table:style-name=\"co1\"" + " table:number-columns-repeated=\"1014\" " +
                        "table:default-cell-style-name=\"Default\"/>");
        PowerMock.verifyAll();
    }

    private void assertPreambleXMLEquals(final String xml) throws IOException {
        final StringBuilder sb = new StringBuilder();
        this.tableAppender.appendPreamble(this.xmlUtil, sb);
        Assert.assertEquals(xml, sb.toString());
    }

}
