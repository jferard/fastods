/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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
import java.util.HashMap;
import java.util.Locale;

public class ContentElementTest {
    private static final String CONTENT_OPEN_TAG =
            "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" xmlns:ooow=\"http://openoffice.org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:of=\"urn:oasis:names:tc:opendocument:xmlns:of:1.2\" office:version=\"1.2\">";
    private static final String PREAMBLE_BODY =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + CONTENT_OPEN_TAG +
                    "<office:automatic-styles></office:automatic-styles><office:body>";
    private static final String POSTAMBLE_BODY = "</office:body></office:document-content>";
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
                        this.format, true, this.container,
                        new HashMap<String, String>());
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
        final Table t1 = this.createTable("t1", 1, 1);
        final Table t2 = this.createTable("t1", 2, 3);

        PowerMock.replayAll();
        Assert.assertEquals(0, this.content.getTableCount());
        Assert.assertEquals(Collections.emptyList(), this.content.getTables());
        Assert.assertTrue(this.content.addTable(t1));
        Assert.assertFalse(this.content.addTable(t2));
        Assert.assertEquals(t1, this.content.getTable("t1"));
        Assert.assertEquals(t1, this.content.getTable(0));
        Assert.assertEquals(1, this.content.getTableCount());
        Assert.assertEquals(Collections.singletonList(t1), this.content.getTables());
        Assert.assertEquals(t1, this.content.getLastTable());

        PowerMock.verifyAll();
    }

    private Table createTable(final String name, final int rowCapacity, final int columnCapacity) {
        return Table.create(this.content, PositionUtil.create(), WriteUtil.create(),
                XMLUtil.create(), name, rowCapacity, columnCapacity, null, null, false);
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
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(t.getName()).andReturn("t");
        this.container
                .writeFontFaceDecls(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));
        this.container
                .writeHiddenDataStyles(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));
        this.container.writeContentAutomaticStyles(EasyMock.eq(this.xmlUtil),
                EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        final AutoFilter autoFilter = AutoFilter.builder("range", t, 1, 2, 3, 4).build();
        this.content.writePreamble(this.xmlUtil, writer);
        this.content.addAutoFilter(autoFilter);
        this.content.writePostamble(this.xmlUtil, writer);

        PowerMock.verifyAll();
        DomTester.assertEquals(PREAMBLE_BODY + "<office:spreadsheet>" +
                        "<table:database-ranges>" +
                        "<table:database-range table:name=\"range\" table:display-filter-buttons=\"true\" " +
                        "table:target-range-address=\"t.C2:E4\"/>" +
                        "</table:database-ranges></office:spreadsheet>" + POSTAMBLE_BODY,
                this.getString(handler));
    }

    @Test
    public void testAddPilotTable() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);
        final PilotTable pilot =
                PilotTable.builder("n", "s", "t", Collections.<String>emptyList()).build();

        PowerMock.resetAll();
        this.container
                .writeFontFaceDecls(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));
        this.container
                .writeHiddenDataStyles(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));
        this.container.writeContentAutomaticStyles(EasyMock.eq(this.xmlUtil),
                EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        this.content.writePreamble(this.xmlUtil, writer);
        this.content.addPilotTable(pilot);
        this.content.writePostamble(this.xmlUtil, writer);

        PowerMock.verifyAll();
        final String actual = this.getString(handler);
        DomTester.assertEquals(PREAMBLE_BODY +
                "<office:spreadsheet><table:data-pilot-tables><table:data-pilot-table table:name=\"n\" table:application-data=\"\" table:target-range-address=\"t\" table:show-filter-button=\"true\" table:drill-down-on-double-click=\"false\"><table:source-cell-range table:cell-range-address=\"s\"/></table:data-pilot-table></table:data-pilot-tables>" +
                "</office:spreadsheet>" +
                POSTAMBLE_BODY, actual);
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
        this.content.writePostamble(this.xmlUtil, writer);

        DomTester.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + CONTENT_OPEN_TAG +
                        "<office:scripts><office:event-listeners><script:event-listener script:language=\"ooo:script\" script:event-name=\"dom:load\" xlink:href=\"vnd.sun.star.script:func?language=Basic&amp;location=document\" xlink:type=\"simple\"/></office:event-listeners></office:scripts><office:automatic-styles></office:automatic-styles><office:body><office:spreadsheet></office:spreadsheet>" +
                        POSTAMBLE_BODY,
                this.getString(handler));
    }

    private String getString(final ZipUTF8WriterMockHandler handler) {
        return handler.getEntryAsString("OdsEntry[path=content.xml]");
    }

    @Test
    public void testAdditionalNamespace() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);
        final HashMap<String, String> additionalNamespaceByPrefix = new HashMap<String, String>();
        additionalNamespaceByPrefix.put("xmlns:myns", "my/namespace");
        final ContentElement contentElement =
                new ContentElement(PositionUtil.create(), XMLUtil.create(), WriteUtil.create(),
                        this.format, true, this.container,
                        additionalNamespaceByPrefix);

        PowerMock.resetAll();
        this.container
                .writeFontFaceDecls(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));
        this.container
                .writeHiddenDataStyles(EasyMock.eq(this.xmlUtil), EasyMock.isA(Appendable.class));
        this.container.writeContentAutomaticStyles(EasyMock.eq(this.xmlUtil),
                EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        contentElement.writePreamble(this.xmlUtil, writer);
        contentElement.writePostamble(this.xmlUtil, writer);

        DomTester.assertEquals(
                XMLUtil.XML_PROLOG +
                        "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" xmlns:ooow=\"http://openoffice.org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:of=\"urn:oasis:names:tc:opendocument:xmlns:of:1.2\" xmlns:myns=\"my/namespace\" office:version=\"1.2\">" +
                        "<office:automatic-styles></office:automatic-styles><office:body><office:spreadsheet></office:spreadsheet>" +
                        POSTAMBLE_BODY,
                this.getString(handler));
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
        final Table t = this.createTable("t", 100, 100);
        this.content.addTable(t);
        this.content.write(this.xmlUtil, writer);

        DomTester.assertEquals(PREAMBLE_BODY +
                        "<office:spreadsheet>" +
                        "<table:table table:name=\"t\" table:style-name=\"ta1\" " +
                        "table:print=\"false\"><table:table-column table:style-name=\"co1\" " +
                        "table:number-columns-repeated=\"1024\" " +
                        "table:default-cell-style-name=\"Default\"/></table:table>" +
                        "</office:spreadsheet>" +
                        POSTAMBLE_BODY,
                this.getString(handler));
    }

    private void playWriteHeader(final XMLUtil util) throws IOException {
        this.container.writeFontFaceDecls(EasyMock.eq(util), EasyMock.isA(ZipUTF8Writer.class));
        this.container.writeHiddenDataStyles(EasyMock.eq(util), EasyMock.isA(ZipUTF8Writer.class));
        this.container
                .writeContentAutomaticStyles(EasyMock.eq(util), EasyMock.isA(ZipUTF8Writer.class));
    }
}