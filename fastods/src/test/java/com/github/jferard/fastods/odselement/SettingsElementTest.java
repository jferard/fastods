/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.odselement.config.ConfigElement;
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntrySingleton;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.testlib.ZipUTF8WriterMockHandler;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SettingsElementTest {
    @Test
    public void testAll() throws IOException {
        final Table table1 = PowerMock.createMock(Table.class);
        final ConfigItemMapEntry t1ce = PowerMock.createMock(ConfigItemMapEntry.class);
        final Table table2 = PowerMock.createMock(Table.class);
        final ConfigItemMapEntry t2ce = PowerMock.createMock(ConfigItemMapEntry.class);
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);

        final SettingsElement element = SettingsElement.create();
        XMLUtil util = XMLUtil.create();

        PowerMock.resetAll();
        EasyMock.expect(table1.getName()).andReturn("t1");
        EasyMock.expect(table1.getConfigEntry()).andReturn(t1ce);
        EasyMock.expect(t1ce.getName()).andReturn("t1ce");

        EasyMock.expect(table2.getConfigEntry()).andReturn(t2ce);
        EasyMock.expect(t2ce.getName()).andReturn("t2ce");

        t1ce.appendXMLContent(util, writer);
        t2ce.appendXMLContent(util, writer);


        PowerMock.replayAll();

        element.setActiveTable(table1);
        element.setViewSetting("view1", "item", "value");
        element.setTables(Arrays.asList(table1, table2));
        element.addTableConfig(ConfigItemMapEntrySingleton.createSingleton(
                ConfigItem.create(ConfigElement.AUTO_CALCULATE, "true")));

        element.write(util, writer);
        PowerMock.verifyAll();

        Assert.assertEquals(Collections.singletonList("OdsEntry[path=settings.xml]"),
                new ArrayList<>(handler.getEntryNames()));
        DomTester.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<office:document-settings xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                        "xmlns:config=\"urn:oasis:names:tc:opendocument:xmlns:config:1.0\" " +
                        "xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" " +
                        "xmlns:ooo=\"http://openoffice.org/2004/office\" office:version=\"1.2\">" +
                        "<office:settings><config:config-item-set config:name=\"ooo:view-settings\">" +
                        "<config:config-item config:name=\"VisibleAreaTop\" config:type=\"int\">0" +
                        "</config:config-item>" +
                        "<config:config-item config:name=\"VisibleAreaLeft\" config:type=\"int\">0" +
                        "</config:config-item><config:config-item-map-indexed config:name=\"Views\">" +
                        "<config:config-item-map-entry><config:config-item " +
                        "config:name=\"RasterSubdivisionX\" config:type=\"long\">1" +
                        "</config:config-item><config:config-item config:name=\"ShowGrid\" " +
                        "config:type=\"boolean\">true</config:config-item><config:config-item " +
                        "config:name=\"IsSnapToRaster\" config:type=\"boolean\">false" +
                        "</config:config-item><config:config-item config:name=\"RasterSubdivisionY\" " +
                        "config:type=\"long\">1</config:config-item>" +
                        "<config:config-item config:name=\"ZoomType\" config:type=\"short\">0" +
                        "</config:config-item><config:config-item config:name=\"IsOutlineSymbolsSet\" " +
                        "config:type=\"boolean\">true</config:config-item><config:config-item " +
                        "config:name=\"HorizontalScrollbarWidth\" config:type=\"int\">270" +
                        "</config:config-item><config:config-item config:name=\"ShowZeroValues\" " +
                        "config:type=\"boolean\">true</config:config-item>" +
                        "<config:config-item config:name=\"ShowNotes\" config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item config:name=\"GridColor\" " +
                        "config:type=\"long\">12632256</config:config-item><config:config-item " +
                        "config:name=\"IsRasterAxisSynchronized\" config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item config:name=\"RasterResolutionX\" " +
                        "config:type=\"long\">1000</config:config-item><config:config-item " +
                        "config:name=\"ShowPageBreakPreview\" config:type=\"boolean\">false" +
                        "</config:config-item><config:config-item config:name=\"ZoomValue\" " +
                        "config:type=\"short\">100</config:config-item><config:config-item " +
                        "config:name=\"RasterResolutionY\" config:type=\"long\">1000" +
                        "</config:config-item><config:config-item config:name=\"ActiveTable\" " +
                        "config:type=\"string\">Sheet1</config:config-item><config:config-item config:name=\"HasColumnRowHeaders\" config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item config:name=\"ViewId\" config:type=\"string\">View1" +
                        "</config:config-item><config:config-item-map-named config:name=\"Tables\"><config:config-item-map-entry><config:config-item config:name=\"AutoCalculate\" config:type=\"boolean\">true" +
                        "</config:config-item>" +
                        "</config:config-item-map-entry>" +
                        "</config:config-item-map-named><config:config-item " +
                        "config:name=\"PageViewZoomValue\" config:type=\"int\">60" +
                        "</config:config-item><config:config-item config:name=\"RasterIsVisible\" " +
                        "config:type=\"boolean\">false" +
                        "</config:config-item><config:config-item config:name=\"HasSheetTabs\" " +
                        "config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item config:name=\"ShowPageBreaks\" " +
                        "config:type=\"boolean\">true" +
                        "</config:config-item></config:config-item-map-entry>" +
                        "</config:config-item-map-indexed><config:config-item " +
                        "config:name=\"VisibleAreaWidth\" config:type=\"int\">680" +
                        "</config:config-item><config:config-item config:name=\"VisibleAreaHeight\" " +
                        "config:type=\"int\">400" +
                        "</config:config-item></config:config-item-set><config:config-item-set " +
                        "config:name=\"ooo:configuration-settings\"><config:config-item " +
                        "config:name=\"RasterSubdivisionX\" config:type=\"long\">1" +
                        "</config:config-item><config:config-item config:name=\"IsSnapToRaster\" " +
                        "config:type=\"boolean\">false</config:config-item><config:config-item " +
                        "config:name=\"RasterSubdivisionY\" config:type=\"long\">1" +
                        "</config:config-item><config:config-item " +
                        "config:name=\"AllowPrintJobCancel\" config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item " +
                        "config:name=\"IsOutlineSymbolsSet\" config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item " +
                        "config:name=\"UpdateFromTemplate\" config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item " +
                        "config:name=\"CharacterCompressionType\" config:type=\"short\">0" +
                        "</config:config-item><config:config-item config:name=\"ShowZeroValues\" " +
                        "config:type=\"boolean\">true</config:config-item><config:config-item " +
                        "config:name=\"AutoCalculate\" config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item config:name=\"RasterResolutionX\" " +
                        "config:type=\"long\">1000</config:config-item><config:config-item " +
                        "config:name=\"LoadReadonly\" config:type=\"boolean\">false" +
                        "</config:config-item><config:config-item config:name=\"RasterResolutionY\" " +
                        "config:type=\"long\">1000</config:config-item><config:config-item " +
                        "config:name=\"ApplyUserData\" config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item config:name=\"RasterIsVisible\" " +
                        "config:type=\"boolean\">false</config:config-item><config:config-item " +
                        "config:name=\"HasSheetTabs\" config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item config:name=\"ShowPageBreaks\" " +
                        "config:type=\"boolean\">true</config:config-item><config:config-item " +
                        "config:name=\"ShowGrid\" config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item config:name=\"ShowNotes\" " +
                        "config:type=\"boolean\">true</config:config-item><config:config-item " +
                        "config:name=\"GridColor\" config:type=\"long\">12632256" +
                        "</config:config-item><config:config-item config:name=\"LinkUpdateMode\" " +
                        "config:type=\"short\">3</config:config-item><config:config-item " +
                        "config:name=\"PrinterSetup\" config:type=\"base64Binary\">" +
                        "</config:config-item><config:config-item " +
                        "config:name=\"IsRasterAxisSynchronized\" config:type=\"boolean\">true" +
                        "</config:config-item><config:config-item " +
                        "config:name=\"SaveVersionOnClose\" config:type=\"boolean\">false" +
                        "</config:config-item><config:config-item " +
                        "config:name=\"IsKernAsianPunctuation\" config:type=\"boolean\">false" +
                        "</config:config-item><config:config-item config:name=\"PrinterName\" " +
                        "config:type=\"string\"></config:config-item><config:config-item " +
                        "config:name=\"HasColumnRowHeaders\" config:type=\"boolean\">true" +
                        "</config:config-item>" +
                        "</config:config-item-set></office:settings></office:document-settings>",
                handler.getEntryAsString("OdsEntry[path=settings.xml]"));
    }
}