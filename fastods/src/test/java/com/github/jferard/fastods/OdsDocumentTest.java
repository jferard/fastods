/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.attribute.ScriptEvent;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.ScriptEventListener;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.Container;
import com.github.jferard.fastods.util.PilotTable;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class OdsDocumentTest<E extends OdsDocument> {
    OdsElements odsElements;

    @Test
    public final void testAddTableDefault() throws IOException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements
                .createTable(EasyMock.eq("t1"), EasyMock.anyInt(), EasyMock.anyInt())).andReturn(t);
        EasyMock.expect(this.odsElements.addTableToContent(t)).andReturn(true);
        this.odsElements.setActiveTable(t);

        PowerMock.replayAll();
        final E document = this.getDocument();
        final Table ret = document.addTable("t1");

        PowerMock.verifyAll();
        Assert.assertEquals(t, ret);
    }

    @Test
    public final void testAddTableWithCapacity() throws IOException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.createTable("t1", 100, 100)).andReturn(t);
        EasyMock.expect(this.odsElements.addTableToContent(t)).andReturn(true);
        this.odsElements.setActiveTable(t);

        PowerMock.replayAll();
        final E document = this.getDocument();
        final Table ret = document.addTable("t1", 100, 100);

        PowerMock.verifyAll();
        Assert.assertEquals(t, ret);
    }

    @Test(expected = FastOdsException.class)
    public void testGetTableByNameFail() throws FastOdsException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTable("ok")).andReturn(null);

        PowerMock.replayAll();
        final E document = this.getDocument();
        document.getTable("ok");

        PowerMock.verifyAll();
    }

    @Test
    public void testGetTableByNameSuccess() throws FastOdsException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTable("ok")).andReturn(t);

        PowerMock.replayAll();
        final E document = this.getDocument();
        final Table ret = document.getTable("ok");

        PowerMock.verifyAll();
        Assert.assertEquals(t, ret);
    }

    @Test
    public void testGetTableName() throws FastOdsException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.singletonList(t));
        EasyMock.expect(t.getName()).andReturn("ok");

        PowerMock.replayAll();
        final E document = this.getDocument();
        final String tableName = document.getTableName(0);

        PowerMock.verifyAll();
        Assert.assertEquals("ok", tableName);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetTables() {
        final List<Table> l = PowerMock.createMock(List.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(l);

        PowerMock.replayAll();
        final E document = this.getDocument();
        final List<Table> tables = document.getTables();

        PowerMock.verifyAll();
        Assert.assertEquals(l, tables);
    }

    @Test
    public void testTableCount() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTableCount()).andReturn(2);

        PowerMock.replayAll();
        final E document = this.getDocument();
        final int actual = document.tableCount();

        PowerMock.verifyAll();
        Assert.assertEquals(2, actual);
    }

    @Test(expected = FastOdsException.class)
    public void testGetTableByIndexNeg() throws FastOdsException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.<Table>emptyList());

        PowerMock.replayAll();
        final E document = this.getDocument();
        document.getTable(-1);

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public void testGetTableByIndexOutOfBounds() throws FastOdsException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.<Table>emptyList());

        PowerMock.replayAll();
        final E document = this.getDocument();
        document.getTable(10);

        PowerMock.verifyAll();
    }

    @Test
    public void testGetTableByIndex() throws FastOdsException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.singletonList(t));

        PowerMock.replayAll();
        final E document = this.getDocument();
        final Table ret = document.getTable(0);

        PowerMock.verifyAll();
        Assert.assertEquals(t, ret);
    }

    @Test
    public void testGetOrAddTableAdd() throws IOException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTable("ok")).andReturn(null);
        EasyMock.expect(this.odsElements
                .createTable(EasyMock.eq("ok"), EasyMock.anyInt(), EasyMock.anyInt())).andReturn(t);
        EasyMock.expect(this.odsElements.addTableToContent(t)).andReturn(true);
        this.odsElements.setActiveTable(t);

        PowerMock.replayAll();
        final E document = this.getDocument();
        final Table tok = document.getOrAddTable("ok");

        PowerMock.verifyAll();
        Assert.assertEquals(t, tok);
    }

    @Test
    public void testGetOrAddTableGet() throws IOException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTable("ok")).andReturn(t);

        PowerMock.replayAll();
        final E document = this.getDocument();
        final Table ret = document.getOrAddTable("ok");

        PowerMock.verifyAll();
        Assert.assertEquals(t, ret);
    }

    @Test
    public final void testAddEvents() {
        final ScriptEventListener script =
                ScriptEventListener.create(ScriptEvent.ON_ALPHA_CHAR_INPUT, "script");

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.addEvents(script);

        PowerMock.replayAll();
        final E document = this.getDocument();
        document.addEvents(script);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddExtraDir() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.addExtraDir("dir");

        PowerMock.replayAll();
        final E document = this.getDocument();
        document.addExtraDir("dir");

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddExtraFile() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        final byte[] bytes = {'c', 'o', 'n', 't', 'e', 'n', 't'};
        this.odsElements.addExtraFile("path", "mt", bytes);

        PowerMock.replayAll();
        final E document = this.getDocument();
        document.addExtraFile("path", "mt", bytes);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddPilotTable() {
        final PilotTable pt = PowerMock.createMock(PilotTable.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.addPilotTable(pt);

        PowerMock.replayAll();
        final E document = this.getDocument();
        document.addPilotTable(pt);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddAutoFilter() {
        final AutoFilter af = PowerMock.createMock(AutoFilter.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.addAutoFilter(af);

        PowerMock.replayAll();
        final E document = this.getDocument();
        document.addAutoFilter(af);

        PowerMock.verifyAll();
    }

    /*
    @Test
    public final void testAddAutoFilterTableRowCol() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.addAutoFilter(EasyMock.isA(AutoFilter.class));

        PowerMock.replayAll();
        final E document = this.getDocument();
        t.addAutoFilter("range", 0, 1, 2, 3);

        PowerMock.verifyAll();
    }
    */


    @Test
    public final void testFreezeCells() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.freezeCells(t, 5, 7);

        PowerMock.replayAll();
        final E document = this.getDocument();
        document.freezeCells(t, 5, 7);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetModes() {
        final Container.Mode mode = Container.Mode.UPDATE;

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.setDataStylesMode(mode);
        this.odsElements.setMasterPageStyleMode(mode);
        this.odsElements.setPageLayoutStyleMode(mode);
        this.odsElements.setPageStyleMode(mode);
        this.odsElements.setObjectStyleMode(mode);

        PowerMock.replayAll();
        final E document = this.getDocument();
        document.setDataStylesMode(mode);
        document.setMasterPageStyleMode(mode);
        document.setPageLayoutStyleMode(mode);
        document.setPageStyleMode(mode);
        document.setObjectStyleMode(mode);

        PowerMock.verifyAll();
    }

    @Test
    public void testGetTableNumberFail() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.singletonList(t));
        EasyMock.expect(t.getName()).andReturn("not ok");

        PowerMock.replayAll();
        final E document = this.getDocument();
        final int tokNum = document.getTableNumber("ok");

        PowerMock.verifyAll();
        Assert.assertEquals(-1, tokNum);
    }

    @Test
    public void testGetTableNumberSuccess() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.singletonList(t));
        EasyMock.expect(t.getName()).andReturn("ok");

        PowerMock.replayAll();
        final E document = this.getDocument();
        final int tokNum = document.getTableNumber("ok");

        PowerMock.verifyAll();
        Assert.assertEquals(0, tokNum);
    }

    @Test
    public void testSetViewSetting() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.setViewSetting("1", "2", "3");

        PowerMock.replayAll();
        final E document = this.getDocument();
        document.setViewSetting("1", "2", "3");
    }

    @Test
    public void testSetActiveTableFailNeg() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);

        PowerMock.replayAll();
        final E document = this.getDocument();
        final boolean activeMin1 = document.setActiveTable(-1);

        PowerMock.verifyAll();
        Assert.assertFalse(activeMin1);
    }

    @Test
    public void testSetActiveTableFailIndexOutOfBounds() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTableCount()).andReturn(1);

        PowerMock.replayAll();
        final E document = this.getDocument();
        final boolean active2 = document.setActiveTable(2);

        PowerMock.verifyAll();
        Assert.assertFalse(active2);
    }

    @Test
    public void testSetActiveTableSuccess() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTableCount()).andReturn(2);
        EasyMock.expect(this.odsElements.getTable(1)).andReturn(t);
        this.odsElements.setActiveTable(t);

        PowerMock.replayAll();
        final E document = this.getDocument();
        final boolean active1 = document.setActiveTable(1);

        PowerMock.verifyAll();
        Assert.assertTrue(active1);
    }

    /*
        @Test
    public final void testGetTable() throws FastOdsException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
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

    @Test(expected = FastOdsException.class)
    public final void testGetTableByIndexExceptionIOOB() throws FastOdsException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.<Table>emptyList());

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.getTable(3);

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public final void testGetTableByIndexExceptionNegative() throws FastOdsException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.<Table>emptyList());

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.getTable(-3);

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public final void testGetTableByNameException() throws FastOdsException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTable("t1")).andReturn(null);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.getTable("t1");

        PowerMock.verifyAll();
    }

    @Test
    public final void testGetTableNumberByNameException() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.getTables()).andReturn(Collections.<Table>emptyList());

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        final int i = document.getTableNumber("t1");

        PowerMock.verifyAll();
        Assert.assertEquals(-1, i);
    }

    @Test
    public final void testTableCount() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
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
        TestHelper.initMockDocument(this.odsElements);
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
    public void testAddAutoFilter() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(t.getName()).andReturn("table");
        this.odsElements.addAutoFilter(EasyMock.isA(AutoFilter.class));

        PowerMock.replayAll();
        this.document = this.getDocument();
        this.document.addAutoFilter(t, 0, 0, 1, 1);

        PowerMock.verifyAll();
    }

    @Test
    public final void testSetActiveTable() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
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

     */

    abstract E getDocument();
}
