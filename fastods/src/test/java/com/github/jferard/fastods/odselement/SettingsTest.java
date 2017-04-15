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

import com.github.jferard.fastods.odselement.config.ConfigBlock;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**

 */
public class SettingsTest {

	private Settings s;
	private List<ConfigBlock> blocks;
	private XMLUtil util;

	@Before
	public void setUp() {
		s = new Settings();
		blocks = s.getRootBlocks();
		util = XMLUtil.create();
	}

	@Test
	public void testSize() {
		Assert.assertEquals(2, blocks.size());
	}

	@Test
	public void testViewSettings() throws IOException {
		final ConfigBlock block = blocks.get(0);
		Assert.assertEquals("ooo:view-settings", block.getName());
	}

	@Test
	public void testViewSettingsContent() throws IOException {
		final ConfigBlock block = blocks.get(0);
		final Appendable sb = new StringBuilder();
		block.appendXML(util, sb);
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
		final ConfigBlock block = blocks.get(1);
		final Appendable sb = new StringBuilder();
		block.appendXML(util, sb);
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
}