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

import com.github.jferard.fastods.datastyle.BooleanStyleBuilder;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;
import com.github.jferard.fastods.util.ZipUTF8WriterImpl;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class OdsDocumentTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private ZipUTF8WriterBuilder builder;

    private OdsElements odsElements;

    private Logger logger;
    private ByteArrayOutputStream os;
    private ZipUTF8Writer writer;
    private WriteUtil writeUtil;
    private XMLUtil xmlUtil;

    @Before
    public final void setUp() {
        this.logger = PowerMock.createNiceMock(Logger.class);
        this.os = new ByteArrayOutputStream();
        this.writer = PowerMock.createMock(ZipUTF8Writer.class);
        this.xmlUtil = XMLUtil.create();
        this.odsElements = PowerMock.createMock(OdsElements.class);
        this.builder = ZipUTF8WriterImpl.builder();
    }

    @Test
    public final void testAddBooleanStyle() throws FastOdsException {
        final DataStyle ds = new BooleanStyleBuilder("b", Locale.US).build();

        PowerMock.resetAll();
        this.initOdsElements();
        this.odsElements.addDataStyle(ds);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addDataStyle(ds);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddPageStyle() {
        final PageStyle ps = PageStyle.builder("p").build();

        PowerMock.resetAll();
        this.initOdsElements();
        this.odsElements.addPageStyle(ps);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addPageStyle(ps);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddTable() throws IOException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expect(this.odsElements.addTableToContent("t1", 100, 100)).andReturn(t);
        this.odsElements.setActiveTable(t);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        final Table t2 = document.addTable("t1", 100, 100);

        PowerMock.verifyAll();
        Assert.assertEquals(t, t2);
    }

    @Test
    public final void testAddTableDefault() throws FastOdsException, IOException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expect(this.odsElements
                .addTableToContent(EasyMock.eq("t1"), EasyMock.anyInt(), EasyMock.anyInt()))
                .andReturn(t);
        this.odsElements.setActiveTable(t);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        final Table t1 = document.addTable("t1");

        PowerMock.verifyAll();
        Assert.assertEquals(t, t1);
    }

    @Test
    public final void testGetTable() throws FastOdsException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expect(this.odsElements.getTables()).andReturn(Arrays.asList(t, t, t, t))
                .anyTimes();
        EasyMock.expect(t.getName()).andReturn("t2").anyTimes();
        EasyMock.expect(this.odsElements.getTable("t2")).andReturn(t).anyTimes();

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        final Table table3 = document.getTable(3);
        final String table3Name = document.getTableName(3);
        document.getTable("t2");
        document.getTableNumber("t2");

        PowerMock.verifyAll();
        Assert.assertEquals(t, table3);
        Assert.assertEquals("t2", table3Name);
    }

    @Test
    public final void testGetOrAddTable() throws FastOdsException, IOException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expect(this.odsElements.getTable("test")).andReturn(null);
        this.odsElements.setActiveTable(t);
        EasyMock.expect(this.odsElements
                .addTableToContent("test", CommonOdsDocument.DEFAULT_ROW_CAPACITY,
                        CommonOdsDocument.DEFAULT_COLUMN_CAPACITY)).andReturn(t);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        final Table test = document.getOrAddTable("test");

        PowerMock.verifyAll();
        Assert.assertEquals(t, test);
    }

    private NamedOdsDocument getDocument() {
        return NamedOdsDocument.create(this.logger, this.xmlUtil, this.odsElements);
    }

    @Test(expected = FastOdsException.class)
    public final void testGetTableByIndexExceptionIOOB() throws FastOdsException {
        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.<Table>emptyList());

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.getTable(3);

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public final void testGetTableByIndexExceptionNegative() throws FastOdsException {
        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.<Table>emptyList());

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.getTable(-3);

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public final void testGetTableByNameException() throws FastOdsException {
        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expect(this.odsElements.getTable("t1")).andReturn(null);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.getTable("t1");

        PowerMock.verifyAll();
    }

    @Test
    public final void testGetTableNumberByNameException() {
        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.<Table>emptyList());

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        final int i = document.getTableNumber("t1");

        PowerMock.verifyAll();
        Assert.assertEquals(-1, i);
    }

    @Test
    public final void testSaveTo() throws IOException {
        PowerMock.resetAll();
        this.initOdsElements();
        this.odsElements.writeMeta(this.xmlUtil, this.writer);
        this.odsElements.writeStyles(this.xmlUtil, this.writer);
        this.odsElements.writeContent(this.xmlUtil, this.writer);
        this.odsElements.writeSettings(this.xmlUtil, this.writer);
        this.writer.close();

        PowerMock.replayAll();
        final NamedOdsDocument d = this.getDocument();
        d.save(this.writer);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSaveToCloseException() throws IOException {
        PowerMock.resetAll();
        this.initOdsElements();
        this.odsElements.createEmptyElements(this.writer);
        this.odsElements.writeImmutableElements(this.xmlUtil, this.writer);
        this.odsElements.writeMeta(this.xmlUtil, this.writer);
        this.odsElements.writeStyles(this.xmlUtil, this.writer);
        this.odsElements.writeContent(this.xmlUtil, this.writer);
        this.odsElements.writeSettings(this.xmlUtil, this.writer);
        this.writer.close();
        EasyMock.expectLastCall().andThrow(new IOException("@"));

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        this.thrown.expect(IOException.class);
        this.thrown.expectMessage("@");
        document.save(this.writer);

        PowerMock.verifyAll();
    }


    @Test
    public final void testSaveWriterException() throws IOException {
        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expectLastCall().andThrow(new RuntimeException("@"));
        this.writer.close();

        PowerMock.replayAll();
        this.thrown.expect(RuntimeException.class);
        this.thrown.expectMessage("@");
        final NamedOdsDocument d = this.getDocument();
        d.save(this.writer);

        PowerMock.verifyAll();
    }

    @Test
    public final void testTableCount() {
        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expect(this.odsElements.getTableCount()).andReturn(11);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        final int tableCount = document.tableCount();

        PowerMock.verifyAll();
        Assert.assertEquals(11, tableCount);
    }

    @Test
    public final void testGetTableNumber() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.singletonList(t))
                .anyTimes();
        EasyMock.expect(t.getName()).andReturn("@t").anyTimes();

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        final int tableNumberAtS = document.getTableNumber("@s");
        final int tableNumberAtT = document.getTableNumber("@t");
        final int tableNumberAtUpperT = document.getTableNumber("@T");
        final List<Table> tables = document.getTables();

        PowerMock.verifyAll();
        Assert.assertEquals(-1, tableNumberAtS);
        Assert.assertEquals(0, tableNumberAtT);
        Assert.assertEquals(-1, tableNumberAtUpperT);
        Assert.assertEquals(Collections.singletonList(t), tables);
    }

    @Test
    public final void testSetActiveTable() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initOdsElements();
        EasyMock.expect(this.odsElements.getTableCount()).andReturn(1).anyTimes();
        EasyMock.expect(this.odsElements.getTable(0)).andReturn(t).anyTimes();
        this.odsElements.setActiveTable(t);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        final boolean setMinus1 = document.setActiveTable(-1);
        final boolean set1 = document.setActiveTable(1);
        final boolean set0 = document.setActiveTable(0);

        PowerMock.verifyAll();
        Assert.assertFalse(setMinus1);
        Assert.assertFalse(set1);
        Assert.assertTrue(set0);
    }

    protected void initOdsElements() {
        TableStyle.DEFAULT_TABLE_STYLE.addToElements(this.odsElements);
        TableRowStyle.DEFAULT_TABLE_ROW_STYLE.addToElements(this.odsElements);
        TableColumnStyle.DEFAULT_TABLE_COLUMN_STYLE.addToElements(this.odsElements);
        TableCellStyle.DEFAULT_CELL_STYLE.addToElements(this.odsElements);
        PageStyle.DEFAULT_PAGE_STYLE.addToElements(this.odsElements);
    }
}
