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
package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.NamedOdsFileWriter;
import com.github.jferard.fastods.PrepareContentFlusher;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.odselement.config.ManifestEntry;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.ZipUTF8WriterMockHandler;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.Container;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Locale;
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
        this.stylesContainer = new StylesContainerImpl(this.logger);

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

    @Test(expected = IllegalStateException.class)
    public void testFreezeStyles() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.odsElements.freezeStyles();
        this.odsElements.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testDebugStyles() {
        PowerMock.resetAll();
        this.logger.severe(EasyMock.anyString());

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
        EasyMock.expect(t.getName()).andReturn("table");
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
    public void testAddStyleToContentAutomaticStyles() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.odsElements.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
        final StringBuilder sb = new StringBuilder();
        this.stylesContainer.writeContentAutomaticStyles(XMLUtil.create(), sb);
        Assert.assertEquals("", sb.toString());
    }

    @Test
    public void testAddChildCellStyle() {
        PowerMock.resetAll();
        EasyMock.expect(this.contentElement
                .addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.STRING))
                .andReturn(null);

        PowerMock.replayAll();
        this.odsElements.addCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.STRING);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddPageStyle() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.odsElements.addPageStyle(PageStyle.DEFAULT_PAGE_STYLE);

        PowerMock.verifyAll();
        final StringBuilder sb1 = new StringBuilder();
        this.stylesContainer.writeMasterPageStyles(XMLUtil.create(), sb1);
        Assert.assertEquals("<style:master-page style:name=\"Mpm1\" " +
                "style:page-layout-name=\"Mpm1\"><style:header><text:p><text:span " +
                "text:style-name=\"none\"></text:span></text:p></style:header><style:header-left " +
                "style:display=\"false\"/><style:footer><text:p><text:span " +
                "text:style-name=\"none\"></text:span></text:p></style:footer><style:footer-left " +
                "style:display=\"false\"/></style:master-page>", sb1.toString());
        final StringBuilder sb2 = new StringBuilder();
        this.stylesContainer.writePageLayoutStyles(XMLUtil.create(), sb2);
        Assert.assertEquals("<style:page-layout style:name=\"Mpm1\"><style:page-layout-properties" +
                " fo:page-width=\"21cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" " +
                "style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1" +
                ".5cm\"/><style:header-style><style:header-footer-properties " +
                "fo:min-height=\"0cm\" fo:margin=\"0cm\"/></style:header-style><style:footer" +
                "-style><style:header-footer-properties fo:min-height=\"0cm\" " +
                "fo:margin=\"0cm\"/></style:footer-style></style:page-layout>", sb2.toString());
    }

    @Test
    public void testAddTableToContent() throws IOException {
        final NamedOdsFileWriter w = PowerMock.createMock(NamedOdsFileWriter.class);
        final Table t = PowerMock.createMock(Table.class);
        final ConfigItemMapEntry e = PowerMock.createMock(ConfigItemMapEntry.class);

        PowerMock.resetAll();
        EasyMock.expect(this.contentElement.getLastTable()).andReturn(null);
        EasyMock.expect(this.contentElement.addTable(t)).andReturn(true);
        EasyMock.expect(t.getConfigEntry()).andReturn(e);
        this.settingsElement.addTableConfig(e);
        t.addObserver(w);
        w.update(EasyMock.isA(PrepareContentFlusher.class));

        PowerMock.replayAll();
        this.odsElements.addObserver(w);
        this.odsElements.addTableToContent(t);

        PowerMock.verifyAll();
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
    public final void testAddExtraDir() throws IOException {
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
        final String pathContent = handler.getEntryAsString("ManifestEntry[path=path]");
        Assert.assertEquals("content", pathContent);
        final String entryAsString = handler.getEntryAsString("ManifestEntry[path=META-INF/manifest.xml]");
        Assert.assertEquals("", entryAsString);

    }

    @Test
    public final void testSetModes() {
        final Container.Mode mode = Container.Mode.UPDATE;

        PowerMock.resetAll();


        PowerMock.replayAll();
        this.odsElements.setDataStylesMode(mode);
        this.odsElements.setMasterPageStyleMode(mode);
        this.odsElements.setPageLayoutStyleMode(mode);
        this.odsElements.setPageStyleMode(mode);
        this.odsElements.setObjectStyleMode(mode);

        PowerMock.verifyAll();
    }

}
