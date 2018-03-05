/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnonymousOdsDocumentTest {
    private AnonymousOdsDocument document;
    private Logger logger;
    private OdsElements odsElements;
    private XMLUtil xmlUtil;

    @Before
    public void setUp() {
        this.logger = PowerMock.createMock(Logger.class);
        this.xmlUtil = XMLUtil.create();
        this.odsElements = PowerMock.createMock(OdsElements.class);
    }

    @Test
    public void testCreate() {
        PowerMock.resetAll();
        this.initStyles(this.odsElements);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddTable() throws IOException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.addTableToContent("ok", 1024, 32)).andReturn(t);
        this.odsElements.setActiveTable(t);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        this.document.addTable("ok");

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public void testGetTableByNameFail() throws IOException, FastOdsException {
        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTable("ok")).andReturn(null);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        this.document.getTable("ok");

        PowerMock.verifyAll();
    }

    @Test
    public void testGetTableByNameSuccess() throws IOException, FastOdsException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTable("ok")).andReturn(t);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final Table t2 = this.document.getTable("ok");

        PowerMock.verifyAll();
        Assert.assertEquals(t, t2);
    }

    @Test
    public void testGetTableName() throws IOException, FastOdsException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.singletonList(t));
        EasyMock.expect(t.getName()).andReturn("ok");

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final String tableName = this.document.getTableName(0);

        PowerMock.verifyAll();
        Assert.assertEquals("ok", tableName);
    }

    @Test
    public void testGetTables() throws IOException, FastOdsException {
        @SuppressWarnings("rawtypes") final List<Table> l = PowerMock.createMock(List.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(l);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final List<Table> tables = this.document.getTables();

        PowerMock.verifyAll();
        Assert.assertEquals(l, tables);
    }

    @Test
    public void testTableCount() throws IOException, FastOdsException {
        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTableCount()).andReturn(20);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final int actual = this.document.tableCount();

        PowerMock.verifyAll();
        Assert.assertEquals(20, actual);
    }

    @Test(expected = FastOdsException.class)
    public void testGetTableByIndexNeg() throws IOException, FastOdsException {
        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.<Table>emptyList());

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        this.document.getTable(-1);

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public void testGetTableByIndex10() throws IOException, FastOdsException {
        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.<Table>emptyList());

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        this.document.getTable(10);

        PowerMock.verifyAll();
    }

    @Test
    public void testGetTableByIndex0() throws IOException, FastOdsException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.singletonList(t));

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final Table t0 = this.document.getTable(0);

        PowerMock.verifyAll();
        Assert.assertEquals(t, t0);
    }

    @Test
    public void testGetOrAddTable() throws IOException, FastOdsException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTable("ok")).andReturn(null);
        EasyMock.expect(this.odsElements.addTableToContent("ok", 1024, 32)).andReturn(t);
        this.odsElements.setActiveTable(t);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final Table tok = this.document.getOrAddTable("ok");

        PowerMock.verifyAll();
        Assert.assertEquals(t, tok);
    }

    @Test
    public void testGetOrAddTable2() throws IOException, FastOdsException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTable("ok")).andReturn(t);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final Table tok = this.document.getOrAddTable("ok");

        PowerMock.verifyAll();
        Assert.assertEquals(t, tok);
    }

    @Test
    public void testSetActiveTableFailNeg() {
        PowerMock.resetAll();
        this.initStyles(this.odsElements);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final boolean activeMin1 = this.document.setActiveTable(-1);

        PowerMock.verifyAll();
        Assert.assertFalse(activeMin1);
    }

    @Test
    public void testSetActiveTableFail2() {
        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTableCount()).andReturn(1);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final boolean active2 = this.document.setActiveTable(2);

        PowerMock.verifyAll();
        Assert.assertFalse(active2);
    }

    @Test
    public void testSetActiveTableFailSuccess() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTableCount()).andReturn(2);
        EasyMock.expect(this.odsElements.getTable(1)).andReturn(t);
        this.odsElements.setActiveTable(t);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final boolean active1 = this.document.setActiveTable(1);

        PowerMock.verifyAll();
        Assert.assertTrue(active1);
    }

    @Test
    public void testSave() throws IOException {
        final ZipUTF8Writer writer = PowerMock.createMock(ZipUTF8Writer.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        this.odsElements.createEmptyElements(writer);
        this.odsElements.writeImmutableElements(this.xmlUtil, writer);
        this.odsElements.writeMeta(this.xmlUtil, writer);
        this.odsElements.writeStyles(this.xmlUtil, writer);
        this.odsElements.writeContent(this.xmlUtil, writer);
        this.odsElements.writeSettings(this.xmlUtil, writer);
        writer.close();
        this.logger.log(Level.FINE, "file saved");

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        this.document.save(writer);

        PowerMock.verifyAll();
    }

    @Test
    public void testGetTableNumberFail() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.singletonList(t));
        EasyMock.expect(t.getName()).andReturn("not ok");

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final int tokNum = this.document.getTableNumber("ok");

        PowerMock.verifyAll();
        Assert.assertEquals(-1, tokNum);
    }

    @Test
    public void testGetTableNumberSuccess() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.singletonList(t));
        EasyMock.expect(t.getName()).andReturn("ok");

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        final int tokNum = this.document.getTableNumber("ok");

        PowerMock.verifyAll();
        Assert.assertEquals(0, tokNum);
    }

    @Test
    public void testSetViewSetting() {
        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        this.odsElements.setViewSetting("1", "2", "3");

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        this.document.setViewSetting("1", "2", "3");
    }

    @Test
    public void addAutoFilter() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.initStyles(this.odsElements);
        this.odsElements.addAutofilter(t, 0, 0, 1, 1);

        PowerMock.replayAll();
        this.document = new AnonymousOdsDocument(this.logger, this.odsElements, this.xmlUtil);
        this.document.addAutofilter(t, 0, 0, 1, 1);

        PowerMock.verifyAll();
    }

    private void initStyles(final OdsElements odsElements) {
        TableStyle.DEFAULT_TABLE_STYLE.addToElements(odsElements);
        TableRowStyle.DEFAULT_TABLE_ROW_STYLE.addToElements(odsElements);
        TableColumnStyle.DEFAULT_TABLE_COLUMN_STYLE.addToElements(odsElements);
        TableCellStyle.DEFAULT_CELL_STYLE.addToElements(odsElements);
        PageStyle.DEFAULT_PAGE_STYLE.addToElements(odsElements);
    }

}