/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.odselement.config.*;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.List;

/**

 */
public class SettingsTest {

	private Settings defaultSettings;
	private List<ConfigBlock> blocks;
	private XMLUtil util;

	@Before
	public void setUp() {
		this.defaultSettings = Settings.create();
		this.blocks = this.defaultSettings.getRootBlocks();
		this.util = XMLUtil.create();

	}

	@Test
	public void testSize() {
		Assert.assertEquals(2, this.blocks.size());
	}

	@Test
	public void testViewSettings() throws IOException {
		final ConfigBlock block = this.blocks.get(0);
		Assert.assertEquals("ooo:view-settings", block.getName());
	}

	@Test
	public void testViewSettingsContent() throws IOException {
		final ConfigBlock block = this.blocks.get(0);
		final Appendable sb = new StringBuilder();
		block.appendXML(this.util, sb);
		DomTester.assertUnsortedEquals("<config:config-item-set config:name=\"ooo:view-settings\">" +
						"<config:config-item config:name=\"VisibleAreaTop\" config:type=\"int\">0</config:config-item>" +
						"<config:config-item config:name=\"VisibleAreaLeft\" config:type=\"int\">0</config:config-item>" +
						"<config:config-item config:name=\"VisibleAreaWidth\" config:type=\"int\">680</config:config-item>" +
						"<config:config-item config:name=\"VisibleAreaHeight\" config:type=\"int\">400</config:config-item>" +
						"<config:config-item-map-indexed config:name=\"Views\">" +
						"<config:config-item-map-entry>" +
						"<config:config-item config:name=\"ViewId\" config:type=\"string\">View1</config:config-item>" +
						"<config:config-item-map-named config:name=\"Tables\" />" +
						"<config:config-item config:name=\"ActiveTable\" config:type=\"string\">Tab1</config:config-item>" +
						"<config:config-item config:name=\"HorizontalScrollbarWidth\" config:type=\"int\">270</config:config-item>" +
						"<config:config-item config:name=\"PageViewZoomValue\" config:type=\"int\">60</config:config-item>" +
						"<config:config-item config:name=\"ZoomType\" config:type=\"short\">0</config:config-item>" +
						"<config:config-item config:name=\"ZoomValue\" config:type=\"int\">100</config:config-item>" +
						"<config:config-item config:name=\"ShowPageBreakPreview\" config:type=\"boolean\">false</config:config-item>" +
						"<config:config-item config:name=\"ShowZeroValues\" config:type=\"boolean\">true</config:config-item>" +
						"<config:config-item config:name=\"ShowNotes\" config:type=\"boolean\">true</config:config-item>" +
						"<config:config-item config:name=\"ShowGrid\" config:type=\"boolean\">true</config:config-item>" +
						"<config:config-item config:name=\"GridColor\" config:type=\"long\">12632256</config:config-item>" +
						"<config:config-item config:name=\"ShowPageBreaks\" config:type=\"boolean\">true</config:config-item>" +
						"<config:config-item config:name=\"HasColumnRowHeaders\" config:type=\"boolean\">true</config:config-item>" +
						"<config:config-item config:name=\"IsOutlineSymbolsSet\" config:type=\"boolean\">true</config:config-item>" +
						"<config:config-item config:name=\"HasSheetTabs\" config:type=\"boolean\">true</config:config-item>" +
						"<config:config-item config:name=\"IsSnapToRaster\" config:type=\"boolean\">false</config:config-item>" +
						"<config:config-item config:name=\"RasterIsVisible\" config:type=\"boolean\">false</config:config-item>" +
						"<config:config-item config:name=\"RasterResolutionX\" config:type=\"int\">1000</config:config-item>" +
						"<config:config-item config:name=\"RasterResolutionY\" config:type=\"int\">1000</config:config-item>" +
						"<config:config-item config:name=\"RasterSubdivisionX\" config:type=\"int\">1</config:config-item>" +
						"<config:config-item config:name=\"RasterSubdivisionY\" config:type=\"int\">1</config:config-item>" +
						"<config:config-item config:name=\"IsRasterAxisSynchronized\" config:type=\"boolean\">true</config:config-item>" +
						"</config:config-item-map-entry>" +
						"</config:config-item-map-indexed>" +
						"</config:config-item-set>"
				, sb.toString());
	}

	@Test
	public void testConfigurationSettingsContent() throws IOException {
		final ConfigBlock block = this.blocks.get(1);
		final Appendable sb = new StringBuilder();
		block.appendXML(this.util, sb);
		DomTester.assertUnsortedEquals(		"<config:config-item-set config:name=\"ooo:configuration-settings\">"+
						"<config:config-item config:name=\"ShowZeroValues\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"ShowNotes\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"ShowGrid\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"GridColor\" config:type=\"long\">12632256</config:config-item>"+
						"<config:config-item config:name=\"ShowPageBreaks\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"LinkUpdateMode\" config:type=\"short\">3</config:config-item>"+
						"<config:config-item config:name=\"HasColumnRowHeaders\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"HasSheetTabs\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"IsOutlineSymbolsSet\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"IsSnapToRaster\" config:type=\"boolean\">false</config:config-item>"+
						"<config:config-item config:name=\"RasterIsVisible\" config:type=\"boolean\">false</config:config-item>"+
						"<config:config-item config:name=\"RasterResolutionX\" config:type=\"int\">1000</config:config-item>"+
						"<config:config-item config:name=\"RasterResolutionY\" config:type=\"int\">1000</config:config-item>"+
						"<config:config-item config:name=\"RasterSubdivisionX\" config:type=\"int\">1</config:config-item>"+
						"<config:config-item config:name=\"RasterSubdivisionY\" config:type=\"int\">1</config:config-item>"+
						"<config:config-item config:name=\"IsRasterAxisSynchronized\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"AutoCalculate\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"PrinterName\" config:type=\"string\"/>"+
						"<config:config-item config:name=\"PrinterSetup\" config:type=\"base64Binary\"/>"+
						"<config:config-item config:name=\"ApplyUserData\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"CharacterCompressionType\" config:type=\"short\">0</config:config-item>"+
						"<config:config-item config:name=\"IsKernAsianPunctuation\" config:type=\"boolean\">false</config:config-item>"+
						"<config:config-item config:name=\"SaveVersionOnClose\" config:type=\"boolean\">false</config:config-item>"+
						"<config:config-item config:name=\"UpdateFromTemplate\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"AllowPrintJobCancel\" config:type=\"boolean\">true</config:config-item>"+
						"<config:config-item config:name=\"LoadReadonly\" config:type=\"boolean\">false</config:config-item>"+
						"</config:config-item-set>"
				, sb.toString());
	}


	@Test
	public void testAddTable() throws IOException {
		final Appendable sb = new StringBuilder();
		final Table table = PowerMock.createMock(Table.class);

		final ConfigItem item = new ConfigItem("n", "t", "v");
		final ConfigItemMapEntrySingleton singleton = ConfigItemMapEntrySingleton.createSingleton("singleton", item);

		EasyMock.expect(table.getConfigEntry()).andReturn(singleton);
		PowerMock.replayAll();

		final Settings s = this.createVoidSettings();
		s.addTable(table);
		s.getRootBlocks().get(0).appendXML(this.util, sb);

		DomTester.assertUnsortedEquals("<config:config-item-set config:name=\"ooo:view-settings\">" +
						"<config:config-item-map-indexed config:name=\"Views\">" +
						"<config:config-item-map-entry>" +
						"<config:config-item config:name=\"ViewId\" config:type=\"string\">View1</config:config-item>" +
						"<config:config-item-map-named config:name=\"Tables\">" +
						"<config:config-item-map-entry config:name=\"singleton\">" +
						"<config:config-item config:name=\"n\" config:type=\"t\">v</config:config-item>" +
						"</config:config-item-map-entry>" +
						"</config:config-item-map-named>" +
						"</config:config-item-map-entry>" +
						"</config:config-item-map-indexed>" +
						"</config:config-item-set>"
				, sb.toString());

		PowerMock.verifyAll();
	}

	@Test
	public void testSetMissingViewSettings() throws IOException {
		final Appendable sb = new StringBuilder();

		final Settings s = this.createVoidSettings();
		s.setViewSettings("vId", "i", "v");
		s.getRootBlocks().get(0).appendXML(this.util, sb);

		DomTester.assertUnsortedEquals("<config:config-item-set config:name=\"ooo:view-settings\">" +
						"<config:config-item-map-indexed config:name=\"Views\">" +
						"<config:config-item-map-entry>" +
						"<config:config-item config:name=\"ViewId\" config:type=\"string\">View1</config:config-item>" +
						"<config:config-item-map-named config:name=\"Tables\" />" +
						"</config:config-item-map-entry>" +
						"</config:config-item-map-indexed>" +
						"</config:config-item-set>"
				, sb.toString());

		PowerMock.verifyAll();
	}

	@Test
	public void testSetViewMissingSettings() throws IOException {
		final Appendable sb = new StringBuilder();

		final Settings s = this.createVoidSettings();
		s.setViewSettings("View1", "i", "v");
		s.getRootBlocks().get(0).appendXML(this.util, sb);

		DomTester.assertUnsortedEquals("<config:config-item-set config:name=\"ooo:view-settings\">" +
						"<config:config-item-map-indexed config:name=\"Views\">" +
						"<config:config-item-map-entry>" +
						"<config:config-item config:name=\"ViewId\" config:type=\"string\">View1</config:config-item>" +
						"<config:config-item-map-named config:name=\"Tables\" />" +
						"</config:config-item-map-entry>" +
						"</config:config-item-map-indexed>" +
						"</config:config-item-set>"
				, sb.toString());

		PowerMock.verifyAll();
	}

	@Test
	public void testSetViewSettings() throws IOException {
		final Appendable sb = new StringBuilder();

		final Settings s = this.createVoidSettings();
		s.setViewSettings("View1", "ViewId", "View2");
		s.getRootBlocks().get(0).appendXML(this.util, sb);

		DomTester.assertUnsortedEquals("<config:config-item-set config:name=\"ooo:view-settings\">" +
						"<config:config-item-map-indexed config:name=\"Views\">" +
						"<config:config-item-map-entry>" +
						"<config:config-item config:name=\"ViewId\" config:type=\"string\">View2</config:config-item>" +
						"<config:config-item-map-named config:name=\"Tables\" />" +
						"</config:config-item-map-entry>" +
						"</config:config-item-map-indexed>" +
						"</config:config-item-set>"
				, sb.toString());

		PowerMock.verifyAll();
	}

	private Settings createVoidSettings() {
		final ConfigItemSet viewSettings = new ConfigItemSet("ooo:view-settings");
		final ConfigItemMapEntrySet firstView = ConfigItemMapEntrySet.createSet();
		firstView.add(new ConfigItem("ViewId", "string", "View1"));
		final ConfigItemSet configurationSettings = new ConfigItemSet("ooo:configuration-settings");
		return Settings.create(viewSettings, firstView, configurationSettings);
	}
}