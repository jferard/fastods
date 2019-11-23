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
import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.odselement.config.ConfigBlock;
import com.github.jferard.fastods.odselement.config.ConfigElement;
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntrySet;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntrySingleton;
import com.github.jferard.fastods.odselement.config.ConfigItemSet;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class SettingsTest {
    private Settings defaultSettings;
    private List<ConfigBlock> blocks;

    @Before
    public void setUp() {
        this.defaultSettings = Settings.create();
        this.blocks = this.defaultSettings.getRootBlocks();
        PowerMock.resetAll();
    }

    @After
    public void tearDown() {
        PowerMock.verifyAll();
    }

    @Test
    public void testSize() {
        PowerMock.replayAll();
        Assert.assertEquals(2, this.blocks.size());
    }

    @Test
    public void testViewSettings() {
        PowerMock.replayAll();
        final ConfigBlock block = this.blocks.get(0);
        Assert.assertEquals("ooo:view-settings", block.getName());
    }

    @Test
    public void testViewSettingsContent() throws IOException {
        PowerMock.replayAll();
        final ConfigBlock block = this.blocks.get(0);
        TestHelper.assertXMLUnsortedEquals(
                "<config:config-item-set config:name=\"ooo:view-settings\">" +
                        "<config:config-item " + "config:name=\"VisibleAreaTop\" " +
                        "config:type=\"int\">0</config:config-item>" + "<config:config-item " +
                        "config:name=\"VisibleAreaLeft\" " +
                        "config:type=\"int\">0</config:config-item>" + "<config:config-item " +
                        "config:name=\"VisibleAreaWidth\" " +
                        "config:type=\"int\">680</config:config-item>" + "<config:config-item " +
                        "config:name=\"VisibleAreaHeight\" " +
                        "config:type=\"int\">400</config:config-item>" +
                        "<config:config-item-map-indexed " + "config:name=\"Views\">" +
                        "<config:config-item-map-entry>" + "<config:config-item " +
                        "config:name=\"ViewId\" config:type=\"string\">View1</config:config-item>" +
                        "<config:config-item-map-named config:name=\"Tables\" />" +
                        "<config:config-item " + "config:name=\"ActiveTable\" " +
                        "config:type=\"string\">Sheet1</config:config-item>" +
                        "<config:config-item" + " config:name=\"HorizontalScrollbarWidth\" " +
                        "config:type=\"int\">270</config:config-item>" + "<config:config-item " +
                        "config:name=\"PageViewZoomValue\" " +
                        "config:type=\"int\">60</config:config-item>" +
                        "<config:config-item config:name=\"ZoomType\" " +
                        "config:type=\"short\">0</config:config-item>" + "<config:config-item " +
                        "config:name=\"ZoomValue\" " +
                        "config:type=\"short\">100</config:config-item>" + "<config:config-item " +
                        "config:name=\"ShowPageBreakPreview\" " +
                        "config:type=\"boolean\">false</config:config-item>" +
                        "<config:config-item " + "config:name=\"ShowZeroValues\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item config:name=\"ShowNotes\" " + "" +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item " +
                        "config:name=\"ShowGrid\" config:type=\"boolean\">true</config:config" +
                        "-item>" + "<config:config-item " + "config:name=\"GridColor\" " +
                        "config:type=\"long\">12632256</config:config-item>" +
                        "<config:config-item " + "config:name=\"ShowPageBreaks\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item " + "config:name=\"HasColumnRowHeaders\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item " + "config:name=\"IsOutlineSymbolsSet\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item " + "config:name=\"HasSheetTabs\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item " + "config:name=\"IsSnapToRaster\" " +
                        "config:type=\"boolean\">false</config:config-item>" +
                        "<config:config-item " + "config:name=\"RasterIsVisible\" " +
                        "config:type=\"boolean\">false</config:config-item>" +
                        "<config:config-item " + "config:name=\"RasterResolutionX\" " +
                        "config:type=\"long\">1000</config:config-item>" + "<config:config-item " +
                        "config:name=\"RasterResolutionY\" " +
                        "config:type=\"long\">1000</config:config-item>" + "<config:config-item " +
                        "config:name=\"RasterSubdivisionX\" " +
                        "config:type=\"long\">1</config:config-item>" + "<config:config-item " +
                        "config:name=\"RasterSubdivisionY\" " +
                        "config:type=\"long\">1</config:config-item>" + "<config:config-item " +
                        "config:name=\"IsRasterAxisSynchronized\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "</config:config-item-map-entry>" + "</config:config-item-map-indexed>" +
                        "</config:config-item-set>", block);
    }

    @Test
    public void testConfigurationSettingsContent() throws IOException {
        PowerMock.replayAll();
        final ConfigBlock block = this.blocks.get(1);
        TestHelper.assertXMLUnsortedEquals(
                "<config:config-item-set config:name=\"ooo:configuration-settings\">" +
                        "<config:config-item " + "config:name=\"ShowZeroValues\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item config:name=\"ShowNotes\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item " +
                        "config:name=\"ShowGrid\" config:type=\"boolean\">true</config:config" +
                        "-item>" + "<config:config-item config:name=\"GridColor\" " +
                        "config:type=\"long\">12632256</config:config-item>" +
                        "<config:config-item " + "config:name=\"ShowPageBreaks\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item config:name=\"LinkUpdateMode\" " +
                        "config:type=\"short\">3</config:config-item>" + "<config:config-item " +
                        "config:name=\"HasColumnRowHeaders\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item config:name=\"HasSheetTabs\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item " + "config:name=\"IsOutlineSymbolsSet\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item config:name=\"IsSnapToRaster\" " +
                        "config:type=\"boolean\">false</config:config-item>" +
                        "<config:config-item " + "config:name=\"RasterIsVisible\" " +
                        "config:type=\"boolean\">false</config:config-item>" +
                        "<config:config-item config:name=\"RasterResolutionX\" " +
                        "config:type=\"long\">1000</config:config-item>" + "<config:config-item " +
                        "config:name=\"RasterResolutionY\" " +
                        "config:type=\"long\">1000</config:config-item>" +
                        "<config:config-item config:name=\"RasterSubdivisionX\" " +
                        "config:type=\"long\">1</config:config-item>" + "<config:config-item " +
                        "config:name=\"RasterSubdivisionY\" " +
                        "config:type=\"long\">1</config:config-item>" +
                        "<config:config-item config:name=\"IsRasterAxisSynchronized\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item " + "config:name=\"AutoCalculate\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item config:name=\"PrinterName\" config:type=\"string\"/>" +
                        "<config:config-item config:name=\"PrinterSetup\" " +
                        "config:type=\"base64Binary\"/>" +
                        "<config:config-item config:name=\"ApplyUserData\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item " + "config:name=\"CharacterCompressionType\" " +
                        "config:type=\"short\">0</config:config-item>" +
                        "<config:config-item config:name=\"IsKernAsianPunctuation\" " +
                        "config:type=\"boolean\">false</config:config-item>" +
                        "<config:config-item " + "config:name=\"SaveVersionOnClose\" " +
                        "config:type=\"boolean\">false</config:config-item>" +
                        "<config:config-item config:name=\"UpdateFromTemplate\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item " + "config:name=\"AllowPrintJobCancel\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item config:name=\"LoadReadonly\" " +
                        "config:type=\"boolean\">false</config:config-item>" +
                        "</config:config-item-set>", block);
    }


    @Test
    public void testAddTable() throws IOException {
        final Table table = PowerMock.createMock(Table.class);

        final ConfigItem item = new ConfigItem("n", "t", "v");
        final ConfigItemMapEntrySingleton singleton =
                ConfigItemMapEntrySingleton.createSingleton("singleton", item);

        // play
        EasyMock.expect(table.getConfigEntry()).andReturn(singleton);

        PowerMock.replayAll();
        final Settings s = this.createVoidSettings();
        s.addTable(table);
        final ConfigBlock block = s.getRootBlocks().get(0);
        TestHelper.assertXMLUnsortedEquals(
                "<config:config-item-set config:name=\"ooo:view-settings\">" +
                        "<config:config-item-map-indexed " + "config:name=\"Views\">" +
                        "<config:config-item-map-entry>" + "<config:config-item " +
                        "config:name=\"ViewId\" config:type=\"string\">View1</config:config-item>" +
                        "<config:config-item-map-named config:name=\"Tables\">" +
                        "<config:config-item-map-entry " + "config:name=\"singleton\">" +
                        "<config:config-item config:name=\"n\" " +
                        "config:type=\"t\">v</config:config-item>" +
                        "</config:config-item-map-entry>" + "</config:config-item-map-named>" +
                        "</config:config-item-map-entry>" + "</config:config-item-map-indexed>" +
                        "</config:config-item-set>", block);
    }

    @Test
    public void testSetMissingViewSettings() throws IOException {
        PowerMock.replayAll();

        final Settings s = this.createVoidSettings();
        s.setViewSetting("vId", "i", "v");
        TestHelper.assertXMLUnsortedEquals(
                "<config:config-item-set config:name=\"ooo:view-settings\">" +
                        "<config:config-item-map-indexed " + "config:name=\"Views\">" +
                        "<config:config-item-map-entry>" + "<config:config-item " +
                        "config:name=\"ViewId\" config:type=\"string\">View1</config:config-item>" +
                        "<config:config-item-map-named config:name=\"Tables\" />" +
                        "</config:config-item-map-entry>" + "</config:config-item-map-indexed>" +
                        "</config:config-item-set>", s.getRootBlocks().get(0));
    }

    @Test
    public void testSetViewMissingSettings() throws IOException {
        PowerMock.replayAll();

        final Settings s = this.createVoidSettings();
        s.setViewSetting("View1", "i", "v");
        TestHelper.assertXMLUnsortedEquals(
                "<config:config-item-set config:name=\"ooo:view-settings\">" +
                        "<config:config-item-map-indexed " + "config:name=\"Views\">" +
                        "<config:config-item-map-entry>" + "<config:config-item " +
                        "config:name=\"ViewId\" config:type=\"string\">View1</config:config-item>" +
                        "<config:config-item-map-named config:name=\"Tables\" />" +
                        "</config:config-item-map-entry>" + "</config:config-item-map-indexed>" +
                        "</config:config-item-set>", s.getRootBlocks().get(0));

    }

    @Test
    public void testSetViewSettings() throws IOException {
        PowerMock.replayAll();
        final Settings s = this.createVoidSettings();
        s.setViewSetting("View1", "ViewId", "View2");
        TestHelper.assertXMLUnsortedEquals(
                "<config:config-item-set config:name=\"ooo:view-settings\">" +
                        "<config:config-item-map-indexed " + "config:name=\"Views\">" +
                        "<config:config-item-map-entry>" + "<config:config-item " +
                        "config:name=\"ViewId\" config:type=\"string\">View2</config:config-item>" +
                        "<config:config-item-map-named config:name=\"Tables\" />" +
                        "</config:config-item-map-entry>" + "</config:config-item-map-indexed>" +
                        "</config:config-item-set>", s.getRootBlocks().get(0));
    }

    private Settings createVoidSettings() {
        final ConfigItemSet viewSettings = new ConfigItemSet("ooo:view-settings");
        final ConfigItemMapEntrySet firstView = ConfigItemMapEntrySet.createSet();
        firstView.add(ConfigItem.create(ConfigElement.VIEW_ID, "View1"));
        final ConfigItemSet configurationSettings = new ConfigItemSet("ooo:configuration-settings");
        return Settings.create(viewSettings, firstView, configurationSettings);
    }
}