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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.attribute.ScriptEvent;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.testlib.ZipUTF8WriterMockHandler;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.PilotTable;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.zip.ZipEntry;

public class ContentElementTest {
    private StylesContainerImpl container;
    private ContentElement content;
    private DataStyles format;
    private SettingsElement settingsElement;
    private XMLUtil xmlUtil;

    @Before
    public void setUp() {
        this.container = PowerMock.createMock(StylesContainerImpl.class);
        this.format = DataStylesBuilder.create(Locale.US).build();
        this.content =
                new ContentElement(PositionUtil.create(), XMLUtil.create(), WriteUtil.create(),
                        this.format, true, this.container);
        this.settingsElement = PowerMock.createMock(SettingsElement.class);
        this.xmlUtil = XMLUtil.create();
    }

    @Test
    public void testAddChildCellStyle() {
        final TableCellStyle style = PowerMock.createNiceMock(TableCellStyle.class);

        PowerMock.resetAll();
        EasyMock.expect(this.container.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE,
                this.format.getBooleanDataStyle())).andReturn(style);

        PowerMock.replayAll();
        final TableCellStyle actual =
                this.content.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.BOOLEAN);

        PowerMock.verifyAll();
        Assert.assertEquals(style, actual);
    }

    @Test
    public void testAddChildCellStyleOfTypeString() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final TableCellStyle actual =
                this.content.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.STRING);

        PowerMock.verifyAll();
        Assert.assertEquals(TableCellStyle.DEFAULT_CELL_STYLE, actual);
    }

    @Test
    public void testAddChildCellStyleOfTypeVoid() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        final TableCellStyle actual =
                this.content.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.VOID);

        PowerMock.verifyAll();
        Assert.assertEquals(TableCellStyle.DEFAULT_CELL_STYLE, actual);
    }

    @Test
    public void testTable() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        Assert.assertEquals(0, this.content.getTableCount());
        Assert.assertEquals(Collections.emptyList(), this.content.getTables());
        final Table t = this.content.addTable("t1", 1, 1);
        Assert.assertEquals(t, this.content.addTable("t1", 2, 3));
        Assert.assertEquals(t, this.content.getTable("t1"));
        Assert.assertEquals(t, this.content.getTable(0));
        Assert.assertEquals(1, this.content.getTableCount());
        Assert.assertEquals(Collections.singletonList(t), this.content.getTables());
        Assert.assertEquals(t, this.content.getLastTable());

        PowerMock.verifyAll();
    }

    @Test
    public void testAppendAutoFilters() {
        PowerMock.resetAll();

        PowerMock.replayAll();

        PowerMock.verifyAll();
    }

    @Test
    public void testAppendPilotTables() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        PowerMock.verifyAll();
    }

    @Test
    public void testWriteEvents() {
        PowerMock.resetAll();

        PowerMock.replayAll();

        PowerMock.verifyAll();

    }

    @Test
    public void testAddAutoFilter() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);
        writer.putNextEntry(new ZipEntry("a"));
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(t.getName()).andReturn("t");

        PowerMock.replayAll();
        final AutoFilter autoFilter = AutoFilter.builder(t, 1, 2, 3, 4).build();
        this.content.addAutoFilter(autoFilter);
        this.content.writePostamble(this.xmlUtil, writer);

        PowerMock.verifyAll();
        DomTester.assertEquals("<table:database-ranges><table:database-range table:name=\"this\" " +
                "table:display-filter-buttons=\"true\" table:target-range-address=\"t" +
                ".C2:E4\"/></table:database-ranges>", handler.getEntryAsString("a"));
    }

    @Test
    public void testAddPilotTable() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);
        writer.putNextEntry(new ZipEntry("a"));
        final PilotTable pilot =
                PilotTable.builder("n", "s", "t", Collections.<String>emptyList()).build();

        PowerMock.resetAll();

        PowerMock.replayAll();
        this.content.addPilotTable(pilot);
        this.content.writePostamble(this.xmlUtil, writer);

        PowerMock.verifyAll();
        DomTester.assertEquals("<table:data-pilot-tables><table:data-pilot-table " +
                "table:name=\"n\" table:application-data=\"\" " +
                "table:target-range-address=\"t\" " + "table:show-filter-button=\"true\" " +
                "table:drill-down-on-double-click=\"false\"><table:source-cell-range " +
                "table:cell-range-address=\"s\"/></table:data-pilot-table></table:data" +
                "-pilot-tables>", handler.getEntryAsString("a"));

    }

    @Test
    public void testAddEvents() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);

        PowerMock.resetAll();
        this.container
                .writeFontFaceDecls(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));
        this.container
                .writeHiddenDataStyles(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));
        this.container.writeContentAutomaticStyles(EasyMock.eq(this.xmlUtil),
                EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        this.content.addEvents(ScriptEventListener.create(ScriptEvent.ON_LOAD, "func"));
        this.content.writePreamble(this.xmlUtil, writer);


        DomTester.assertEquals("<script:event-listener script:language=\"ooo:script\" " +
                        "script:event-name=\"dom:load\" xlink:href=\"vnd.sun.star" +
                        ".script:func?language=Basic&amp;location=document\" " +
                        "xlink:type=\"simple\"/>",
                handler.getEntryAsString("content.xml"));
    }

    @Test
    public void testWrite() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);

        PowerMock.resetAll();
        this.container
                .writeFontFaceDecls(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));
        this.container
                .writeHiddenDataStyles(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));
        this.container.writeContentAutomaticStyles(EasyMock.eq(this.xmlUtil),
                EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        this.content.addTable("t", 100, 100);
        this.content.write(this.xmlUtil, writer);

        DomTester.assertEquals("<table:table table:name=\"t\" table:style-name=\"ta1\" " +
                        "table:print=\"false\"><office:forms form:automatic-focus=\"false\" " +
                        "form:apply-design-mode=\"false\"/><table:table-column " +
                        "table:style-name=\"co1\" table:number-columns-repeated=\"1024\" " +
                        "table:default-cell-style-name=\"Default\"/></table:table>",
                handler.getEntryAsString("content.xml"));
    }

    private void playWriteHeader(final XMLUtil util) throws IOException {
        this.container.writeFontFaceDecls(EasyMock.eq(util), EasyMock.isA(ZipUTF8Writer.class));
        this.container.writeHiddenDataStyles(EasyMock.eq(util), EasyMock.isA(ZipUTF8Writer.class));
        this.container
                .writeContentAutomaticStyles(EasyMock.eq(util), EasyMock.isA(ZipUTF8Writer.class));
    }
}