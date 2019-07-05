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

import com.github.jferard.fastods.FinalizeFlusher;
import com.github.jferard.fastods.MetaAndStylesElementsFlusher;
import com.github.jferard.fastods.NamedOdsFileWriter;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.datastyle.BooleanStyleBuilder;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.style.ObjectStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

public class OdsElementsTest {
    private static final int TABLE_INDEX = 9;
    private ContentElement contentElement;
    private Locale locale;
    private ManifestElement manifestElement;
    private MetaElement metaElement;
    private MimetypeElement mimetypeElement;
    private OdsElements oe;
    private SettingsElement settingsElement;
    private StylesContainer stylesContainer;
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
        this.stylesContainer = new StylesContainer(this.logger);

        this.oe = new OdsElements(this.logger, this.stylesContainer, this.mimetypeElement,
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
        Assert.assertEquals(t, this.oe.getTable(TABLE_INDEX));
        Assert.assertEquals(t, this.oe.getTable("nine"));
        Assert.assertEquals(TABLE_INDEX + 1, this.oe.getTableCount());

        PowerMock.verifyAll();
    }

    @Test
    public final void testFlush() throws IOException {
        final DataStyle ds = new BooleanStyleBuilder("ds", this.locale).build();
        final ObjectStyle os = TableCellStyle.builder("os").build();
        final NamedOdsFileWriter o = PowerMock.createMock(NamedOdsFileWriter.class);
        final ZipUTF8Writer w = PowerMock.createMock(ZipUTF8Writer.class);
        final Table t = PowerMock.createMock(Table.class);


        PowerMock.resetAll();
        this.contentElement.flushRows(this.util, w, this.settingsElement);
        this.contentElement.flushTables(this.util, w, this.settingsElement);
        EasyMock.expectLastCall().times(2);
        this.contentElement.writePostamble(this.util, w);
        EasyMock.expect(this.contentElement.getLastTable()).andReturn(t);
        t.flush();
        o.update(EasyMock.isA(FinalizeFlusher.class));

        PowerMock.replayAll();
        this.oe.addObserver(o);
        this.oe.addDataStyle(ds);
        this.oe.addStyleStyle(os);
        this.oe.flushRows(this.util, w);
        this.oe.flushTables(this.util, w);
        this.oe.finalizeContent(this.util, w);
        this.oe.save();

        PowerMock.verifyAll();
    }

    @Test(expected = IllegalStateException.class)
    public void testFreezeStyles() {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.oe.freezeStyles();
        this.oe.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testDebugStyles() {
        PowerMock.resetAll();
        this.logger.severe(EasyMock.anyString());

        PowerMock.replayAll();
        this.oe.debugStyles();
        this.oe.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testViewSettings() {
        PowerMock.resetAll();
        this.settingsElement.setViewSetting("view1", "ShowZeroValues", "false");

        PowerMock.replayAll();
        this.oe.setViewSetting("view1", "ShowZeroValues", "false");

        PowerMock.verifyAll();
    }

    @Test
    public void testAutofilter() {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        t.addAutoFilter(0, 0, 1, 1);

        PowerMock.replayAll();
        this.oe.addAutoFilter(t, 0, 0, 1, 1);

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
        this.oe.freezeCells(t, 1, 1);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddStyleToContentAutomaticStyles() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.oe.addStyleToContentAutomaticStyles(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
        final StringBuilder sb = new StringBuilder();
        this.stylesContainer.writeContentAutomaticStyles(XMLUtil.create(), sb);
        Assert.assertEquals("", sb.toString());
    }

    @Test
    public void testAddChildCellStyle() {
        PowerMock.resetAll();
        EasyMock.expect(this.contentElement
                .addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, TableCell.Type.STRING))
                .andReturn(null);

        PowerMock.replayAll();
        this.oe.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, TableCell.Type.STRING);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddPageStyle() throws IOException {
        PowerMock.resetAll();

        PowerMock.replayAll();
        this.oe.addPageStyle(PageStyle.DEFAULT_PAGE_STYLE);

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
        EasyMock.expect(this.contentElement.addTable("table1", 10, 5)).andReturn(t);
        EasyMock.expect(t.getConfigEntry()).andReturn(e);
        this.settingsElement.addTableConfig(e);
        t.addObserver(w);
        w.update(EasyMock.isA(MetaAndStylesElementsFlusher.class));

        PowerMock.replayAll();
        this.oe.addObserver(w);
        this.oe.addTableToContent("table1", 10, 5);

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
        EasyMock.expect(this.contentElement.addTable("table1", 10, 5)).andReturn(t);
        EasyMock.expect(t.getConfigEntry()).andReturn(e);
        this.settingsElement.addTableConfig(e);
        lt.flush();
        t.addObserver(w);

        PowerMock.replayAll();
        this.oe.addObserver(w);
        this.oe.addTableToContent("table1", 10, 5);

        PowerMock.verifyAll();
    }
}
