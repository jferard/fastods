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
package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.FinalizeFlusher;
import com.github.jferard.fastods.NamedOdsFileWriter;
import com.github.jferard.fastods.PrepareContentFlusher;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.datastyle.BooleanStyle;
import com.github.jferard.fastods.datastyle.BooleanStyleBuilder;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.ZipUTF8WriterMockHandler;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.Container;
import com.github.jferard.fastods.util.PilotTable;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OdsElementsTest {
    private static final int TABLE_INDEX = 9;
    private ContentElement contentElement;
    private Locale locale;
    private ManifestElement manifestElement;
    private MetaElement metaElement;
    private MimetypeElement mimetypeElement;
    private OdsElements odsElements;
    private SettingsElement settingsElement;
    private StylesContainerImpl stylesContainer;
    private StylesElement stylesElement;
    private XMLUtil util;
    private Logger logger;

    @Before
    public void setUp() {
        this.logger = PowerMock.createMock(Logger.class);
        this.mimetypeElement = PowerMock.createMock(MimetypeElement.class);
        this.manifestElement = PowerMock.createMock(ManifestElement.class);
        this.settingsElement = PowerMock.createMock(SettingsElement.class);
        this.metaElement = PowerMock.createMock(MetaElement.class);
        this.contentElement = PowerMock.createMock(ContentElement.class);
        this.stylesElement = PowerMock.createMock(StylesElement.class);
        this.stylesContainer = PowerMock.createMock(StylesContainerImpl.class);

        this.odsElements = new OdsElements(this.logger, this.stylesContainer, this.mimetypeElement,
                this.manifestElement, this.settingsElement, this.metaElement, this.contentElement,
                this.stylesElement);
        this.util = XMLUtil.create();
        this.locale = Locale.US;
    }

    @Test
    public final void testGetTable() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(this.contentElement.getTable(TABLE_INDEX)).andReturn(t);
        EasyMock.expect(this.contentElement.getTable("nine")).andReturn(t);
        EasyMock.expect(this.contentElement.getTableCount()).andReturn(TABLE_INDEX + 1);

        PowerMock.replayAll();
        Assert.assertEquals(t, this.odsElements.getTable(TABLE_INDEX));
        Assert.assertEquals(t, this.odsElements.getTable("nine"));
        Assert.assertEquals(TABLE_INDEX + 1, this.odsElements.getTableCount());

        PowerMock.verifyAll();
    }

    @Test
    public void testFreezeStyles() {
        PowerMock.resetAll();
        this.stylesContainer.freeze();
        EasyMock.expect(this.stylesContainer.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                .andThrow(new IllegalStateException());

        PowerMock.replayAll();
        this.odsElements.freezeStyles();
        Assert.assertThrows(IllegalStateException.class,
                () -> this.odsElements.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE));

        PowerMock.verifyAll();
    }

    @Test
    public void testDebugStyles() {
        PowerMock.resetAll();
        this.stylesContainer.debug();
        EasyMock.expect(this.stylesContainer.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        this.odsElements.debugStyles();
        this.odsElements.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testViewSettings() {
        PowerMock.resetAll();
        this.settingsElement.setViewSetting("view1", "ShowZeroValues", "false");

        PowerMock.replayAll();
        this.odsElements.setViewSetting("view1", "ShowZeroValues", "false");

        PowerMock.verifyAll();
    }

    @Test
    public void testAutofilter() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(t.getName()).andReturn("table").times(2);
        this.contentElement.addAutoFilter(EasyMock.isA(AutoFilter.class));

        PowerMock.replayAll();
        final AutoFilter autoFilter = AutoFilter.builder("range", t, 0, 0, 1, 1).build();
        this.odsElements.addAutoFilter(autoFilter);

        PowerMock.verifyAll();
    }

    @Test
    public void testFreezeCells() {
        final Table t = PowerMock.createMock(Table.class);
        final ConfigItemMapEntry tableConfig = PowerMock.createMock(ConfigItemMapEntry.class);

        PowerMock.resetAll();
        EasyMock.expect(t.getConfigEntry()).andReturn(tableConfig);
        EasyMock.expect(tableConfig.put(EasyMock.isA(ConfigItem.class))).andReturn(null).anyTimes();

        PowerMock.replayAll();
        this.odsElements.freezeCells(t, 1, 1);

        PowerMock.verifyAll();
    }

    @Test
    public void testSaveAsyncWoPrevious() throws IOException {
        final NamedOdsFileWriter w = PowerMock.createMock(NamedOdsFileWriter.class);

        PowerMock.resetAll();

        PowerMock.resetAll();
        EasyMock.expect(this.contentElement.getLastTable()).andReturn(null);
        w.update(EasyMock.isA(PrepareContentFlusher.class));
        w.update(EasyMock.isA(FinalizeFlusher.class));

        PowerMock.replayAll();
        this.odsElements.addObserver(w);
        this.odsElements.saveAsync();

        PowerMock.verifyAll();
    }

    @Test
    public void testSaveAsyncWithPrevious() throws IOException {
        final Table t = PowerMock.createMock(Table.class);
        final NamedOdsFileWriter w = PowerMock.createMock(NamedOdsFileWriter.class);

        PowerMock.resetAll();

        PowerMock.resetAll();
        EasyMock.expect(this.contentElement.getLastTable()).andReturn(t);
        t.asyncFlushEndTable();
        w.update(EasyMock.isA(FinalizeFlusher.class));

        PowerMock.replayAll();
        this.odsElements.addObserver(w);
        this.odsElements.saveAsync();

        PowerMock.verifyAll();
    }

    @Test
    public void testAddStyleToContentAutomaticStyles() {
        PowerMock.resetAll();
        EasyMock.expect(this.stylesContainer.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        this.odsElements.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddStyleToStylesStyles() {
        PowerMock.resetAll();
        EasyMock.expect(this.stylesContainer.addStylesStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        this.odsElements.addStylesStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddChildCellStyle() {
        PowerMock.resetAll();
        EasyMock.expect(this.contentElement
                        .addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.STRING))
                .andReturn(null);
        EasyMock.expect(this.stylesContainer.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        this.odsElements.addCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.STRING);

        PowerMock.verifyAll();
    }

    @Test
    public void testDirectAddChildCellStyle() {
        final BooleanStyle b = new BooleanStyleBuilder("b",
                Locale.US).build();
        final TableCellStyle ns = PowerMock.createMock(TableCellStyle.class);

        PowerMock.resetAll();
        EasyMock.expect(
                        this.stylesContainer.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, b))
                .andReturn(ns);

        PowerMock.replayAll();
        this.odsElements.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, b);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddPageStyle() {
        PowerMock.resetAll();
        EasyMock.expect(this.stylesContainer.addPageStyle(PageStyle.DEFAULT_PAGE_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        this.odsElements.addPageStyle(PageStyle.DEFAULT_PAGE_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddContentFontFaceContainerStyle() {
        PowerMock.resetAll();
        EasyMock.expect(this.stylesContainer.addContentFontFaceContainerStyle(
                        TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        this.odsElements.addContentFontFaceContainerStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddStylesFontFaceContainerStyle() {
        PowerMock.resetAll();
        EasyMock.expect(this.stylesContainer.addStylesFontFaceContainerStyle(
                        TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        this.odsElements.addStylesFontFaceContainerStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public final void testCreateAndAddTableToContent() throws IOException {
        final Table t = PowerMock.createMock(Table.class);
        final NamedOdsFileWriter w = PowerMock.createMock(NamedOdsFileWriter.class);
        final ConfigItemMapEntry ce = PowerMock.createMock(ConfigItemMapEntry.class);

        PowerMock.resetAll();
        EasyMock.expect(this.contentElement.createTable("foo", 10, 20)).andReturn(t);
        EasyMock.expect(this.contentElement.getLastTable()).andReturn(null);
        EasyMock.expect(this.contentElement.addTable(t)).andReturn(true);
        EasyMock.expect(t.getConfigEntry()).andReturn(ce);
        this.settingsElement.addTableConfig(ce);
        t.addObserver(w);
        w.update(EasyMock.isA(PrepareContentFlusher.class));

        PowerMock.replayAll();
        this.odsElements.addObserver(w);
        final Table table = this.odsElements.createTable("foo", 10, 20);
        this.odsElements.addTableToContent(table);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddTableToContent() throws IOException {
        final Table t = PowerMock.createMock(Table.class);
        final NamedOdsFileWriter w = PowerMock.createMock(NamedOdsFileWriter.class);
        final ConfigItemMapEntry ce = PowerMock.createMock(ConfigItemMapEntry.class);

        PowerMock.resetAll();
        EasyMock.expect(this.contentElement.createTable("foo", 10, 20)).andReturn(t);
        EasyMock.expect(this.contentElement.getLastTable()).andReturn(null);
        EasyMock.expect(this.contentElement.addTable(t)).andReturn(true);
        EasyMock.expect(t.getConfigEntry()).andReturn(ce);
        this.settingsElement.addTableConfig(ce);
        t.addObserver(w);
        w.update(EasyMock.isA(PrepareContentFlusher.class));

        PowerMock.replayAll();
        this.odsElements.addObserver(w);
        final Table ret = this.odsElements.addTableToContent("foo", 10, 20);

        PowerMock.verifyAll();
        Assert.assertEquals(t, ret);
    }

    @Test
    public void testAddTableToContentNull() throws IOException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        EasyMock.expect(this.contentElement.createTable("foo", 10, 20)).andReturn(t);
        EasyMock.expect(this.contentElement.getLastTable()).andReturn(null);
        EasyMock.expect(this.contentElement.addTable(t)).andReturn(false);

        PowerMock.replayAll();
        final Table ret = this.odsElements.addTableToContent("foo", 10, 20);

        PowerMock.verifyAll();
        Assert.assertNull(ret);
    }

    @Test
    public void testAddTableToContentWithPreviousTable() throws IOException {
        final NamedOdsFileWriter w = PowerMock.createMock(NamedOdsFileWriter.class);
        final Table lt = PowerMock.createMock(Table.class);
        final Table t = PowerMock.createMock(Table.class);
        final ConfigItemMapEntry e = PowerMock.createMock(ConfigItemMapEntry.class);

        PowerMock.resetAll();
        EasyMock.expect(this.contentElement.getLastTable()).andReturn(lt);
        EasyMock.expect(this.contentElement.addTable(t)).andReturn(true);
        EasyMock.expect(t.getConfigEntry()).andReturn(e);
        this.settingsElement.addTableConfig(e);
        lt.asyncFlushEndTable();
        t.addObserver(w);

        PowerMock.replayAll();
        this.odsElements.addObserver(w);
        this.odsElements.addTableToContent(t);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddExtraDirNoSlash() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);

        PowerMock.resetAll();
        this.logger.log(Level.FINER, "Writing extra elements to zip file");
        this.logger
                .log(EasyMock.eq(Level.FINEST), EasyMock.eq("Writing ods element: {0} to zip file"),
                        EasyMock.isA(ManifestEntryElement.class));

        PowerMock.replayAll();
        this.odsElements.addExtraDir("dir");
        this.odsElements.writeExtras(this.util, writer);
        writer.finish();

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddExtraDirSlash() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);

        PowerMock.resetAll();
        this.logger.log(Level.FINER, "Writing extra elements to zip file");
        this.logger
                .log(EasyMock.eq(Level.FINEST), EasyMock.eq("Writing ods element: {0} to zip file"),
                        EasyMock.isA(ManifestEntryElement.class));

        PowerMock.replayAll();
        this.odsElements.addExtraDir("dir/");
        this.odsElements.writeExtras(this.util, writer);
        writer.finish();

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddExtraFile() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);
        final byte[] bytes = {'c', 'o', 'n', 't', 'e', 'n', 't'};

        PowerMock.resetAll();
        this.logger.log(Level.FINER, "Writing extra elements to zip file");
        this.logger
                .log(EasyMock.eq(Level.FINEST), EasyMock.eq("Writing ods element: {0} to zip file"),
                        EasyMock.isA(ExtraElement.class));

        PowerMock.replayAll();
        this.odsElements.addExtraFile("path", "mt", bytes);
        this.odsElements.writeExtras(this.util, writer);
        writer.finish();

        PowerMock.verifyAll();
        final String pathContent = handler.getEntryAsString("OdsEntry[path=path]");
        Assert.assertEquals("content", pathContent);
        final String entryAsString =
                handler.getEntryAsString("UnregisteredOdsEntry[path=META-INF/manifest.xml]");
        Assert.assertEquals("", entryAsString);

    }

    @Test
    public final void testSetModes() {
        final Container.Mode mode = Container.Mode.UPDATE;

        PowerMock.resetAll();
        this.stylesContainer.setDataStylesMode(Container.Mode.UPDATE);
        this.stylesContainer.setMasterPageStyleMode(Container.Mode.UPDATE);
        this.stylesContainer.setPageLayoutStyleMode(Container.Mode.UPDATE);
        this.stylesContainer.setPageStyleMode(Container.Mode.UPDATE);
        this.stylesContainer.setObjectStyleMode(Container.Mode.UPDATE);

        PowerMock.replayAll();
        this.odsElements.setDataStylesMode(mode);
        this.odsElements.setMasterPageStyleMode(mode);
        this.odsElements.setPageLayoutStyleMode(mode);
        this.odsElements.setPageStyleMode(mode);
        this.odsElements.setObjectStyleMode(mode);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddDataStyle() {
        final DataStyle ds = new BooleanStyleBuilder("foo", Locale.US).build();

        PowerMock.resetAll();
        EasyMock.expect(this.stylesContainer.addDataStyle(ds)).andReturn(true);

        PowerMock.replayAll();
        this.odsElements.addDataStyle(ds);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddNewDataStyleFromCellStyle() {
        PowerMock.resetAll();
        EasyMock.expect(this.stylesContainer.addNewDataStyleFromCellStyle(
                TableCellStyle.DEFAULT_CELL_STYLE)).andReturn(true);

        PowerMock.replayAll();
        this.odsElements.addNewDataStyleFromCellStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddExtraObject() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);

        PowerMock.resetAll();
        this.logger.log(Level.FINER, "Writing extra elements to zip file");
        this.logger.log(EasyMock.eq(Level.FINEST),
                EasyMock.eq("Writing ods element: {0} to zip file"),
                EasyMock.isA(ManifestEntryElement.class));

        PowerMock.replayAll();
        this.odsElements.addExtraObject("path.xml", "text/xml", "1.0");
        this.odsElements.writeExtras(this.util, writer);

        PowerMock.verifyAll();
        Assert.assertEquals(1, handler.getRegisteredNames().size());
        Assert.assertEquals(Optional.of("OdsEntry[path=path.xml/]"),
                handler.getRegisteredNames().stream().findAny());
    }

    @Test
    public final void testSetActiveTable() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        this.settingsElement.setActiveTable(t);

        PowerMock.replayAll();
        this.odsElements.setActiveTable(t);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddEvents() {
        final ScriptEventListener sel = PowerMock.createMock(ScriptEventListener.class);

        PowerMock.resetAll();
        this.contentElement.addEvents(sel);

        PowerMock.replayAll();
        this.odsElements.addEvents(sel);

        PowerMock.verifyAll();
    }

    @Test
    public final void testAddPilotTable() {
        final PilotTable pt = PowerMock.createMock(PilotTable.class);

        PowerMock.resetAll();
        this.contentElement.addPilotTable(pt);

        PowerMock.replayAll();
        this.odsElements.addPilotTable(pt);

        PowerMock.verifyAll();
    }
}