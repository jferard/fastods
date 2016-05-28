/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * /**
 *
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file SettingsEntry.java is part of FastODS.
 *
 *         WHERE ? settings.xml/office:document-settings
 */
@SuppressWarnings("PMD.CommentRequired")
public class SettingsEntry implements OdsEntry {
	private final ConfigItem allowPrintJobCancel;
	private final ConfigItem applyUserData;
	private final ConfigItem autoCalculate;
	private final ConfigItem characterCompressionType;

	private final ConfigItem gridColor;
	private final ConfigItem hasColumnRowHeaders;
	private final ConfigItem hasSheetTabs;
	private final ConfigItem isKernAsianPunctuation;
	private final ConfigItem isOutlineSymbolsSet;
	private final ConfigItem isRasterAxisSynchronized;
	private final ConfigItem isSnapToRaster;
	private final ConfigItem linkUpdateMode;
	private final ConfigItem loadReadonly;
	private final ConfigItem printerName;
	private final ConfigItem printerSetup;
	private final ConfigItem rasterIsVisible;
	private final ConfigItem rasterResolutionX;
	private final ConfigItem rasterResolutionY;
	private final ConfigItem rasterSubdivisionX;
	private final ConfigItem rasterSubdivisionY;
	private final ConfigItem saveVersionOnClose;
	private final ConfigItem showGrid;
	private final ConfigItem showNotes;
	private final ConfigItem showPageBreaks;
	// ConfigurationSettings
	private final ConfigItem showZeroValues;

	// private final List<String> tableConfigs;
	private List<Table> tables;
	private final ConfigItem updateFromTemplate;
	// ViewIdSettings
	private ConfigItem viewIdActiveTable;
	private final ConfigItem viewIdGridColor;
	private final ConfigItem viewIdHasColumnRowHeaders;
	private final ConfigItem viewIdHasSheetTabs;
	private final ConfigItem viewIdHorizontalScrollbarWidth;
	private final ConfigItem viewIdIsOutlineSymbolsSet;
	private final ConfigItem viewIdIsRasterAxisSynchronized;
	private final ConfigItem viewIdIsSnapToRaster;
	private final ConfigItem viewIdPageViewZoomValue;
	private final ConfigItem viewIdRasterIsVisible;
	private final ConfigItem viewIdRasterResolutionX;
	private final ConfigItem viewIdRasterResolutionY;
	private final ConfigItem viewIdRasterSubdivisionX;
	private final ConfigItem viewIdRasterSubdivisionY;
	private final ConfigItem viewIdShowGrid;
	private final ConfigItem viewIdShowNotes;
	private final ConfigItem viewIdShowPageBreakPreview;
	private final ConfigItem viewIdShowPageBreaks;
	private final ConfigItem viewIdShowZeroValues;
	private final ConfigItem viewIdZoomType;
	private final ConfigItem viewIdZoomValue;
	private final ConfigItem visibleAreaHeight;
	private final ConfigItem visibleAreaLeft;
	// ViewSettings
	private final ConfigItem visibleAreaTop;

	private final ConfigItem visibleAreaWidth;

	public SettingsEntry() {
		this.tables = Collections.emptyList();
		this.visibleAreaTop = new ConfigItem("VisibleAreaTop", "int", "0");
		this.visibleAreaLeft = new ConfigItem("VisibleAreaLeft", "int", "0");
		this.visibleAreaWidth = new ConfigItem("VisibleAreaWidth", "int",
				"680");
		this.visibleAreaHeight = new ConfigItem("VisibleAreaHeight", "int",
				"400");
		this.viewIdActiveTable = new ConfigItem("ActiveTable", "string",
				"Tab1");
		this.viewIdHorizontalScrollbarWidth = new ConfigItem(
				"ViewIdHorizontalScrollbarWidth", "int", "270");
		this.viewIdZoomType = new ConfigItem("ViewIdZoomType", "short", "0");
		this.viewIdZoomValue = new ConfigItem("ViewIdZoomValue", "int", "100");
		this.viewIdPageViewZoomValue = new ConfigItem("ViewIdPageViewZoomValue",
				"int", "60");
		this.viewIdShowPageBreakPreview = new ConfigItem(
				"ViewIdShowPageBreakPreview", "boolean", "false");
		this.viewIdShowZeroValues = new ConfigItem("ViewIdShowZeroValues",
				"boolean", "true");
		this.viewIdShowNotes = new ConfigItem("ViewIdShowNotes", "boolean",
				"true");
		this.viewIdShowGrid = new ConfigItem("ViewIdShowGrid", "boolean",
				"true");
		this.viewIdGridColor = new ConfigItem("ViewIdGridColor", "long",
				"12632256");
		this.viewIdShowPageBreaks = new ConfigItem("ViewIdShowPageBreaks",
				"boolean", "true");
		this.viewIdHasColumnRowHeaders = new ConfigItem(
				"ViewIdHasColumnRowHeaders", "boolean", "true");
		this.viewIdHasSheetTabs = new ConfigItem("ViewIdHasSheetTabs",
				"boolean", "true");
		this.viewIdIsOutlineSymbolsSet = new ConfigItem(
				"ViewIdIsOutlineSymbolsSet", "boolean", "true");
		this.viewIdIsSnapToRaster = new ConfigItem("ViewIdIsSnapToRaster",
				"boolean", "false");
		this.viewIdRasterIsVisible = new ConfigItem("ViewIdRasterIsVisible",
				"boolean", "false");
		this.viewIdRasterResolutionX = new ConfigItem("ViewIdRasterResolutionX",
				"int", "1000");
		this.viewIdRasterResolutionY = new ConfigItem("ViewIdRasterResolutionY",
				"int", "1000");
		this.viewIdRasterSubdivisionX = new ConfigItem(
				"ViewIdRasterSubdivisionX", "int", "1");
		this.viewIdRasterSubdivisionY = new ConfigItem(
				"ViewIdRasterSubdivisionY", "int", "1");
		this.viewIdIsRasterAxisSynchronized = new ConfigItem(
				"ViewIdIsRasterAxisSynchronized", "boolean", "true");
		this.showZeroValues = new ConfigItem("ShowZeroValues", "boolean",
				"true");
		this.showNotes = new ConfigItem("ShowNotes", "boolean", "true");
		this.showGrid = new ConfigItem("ShowGrid", "boolean", "true");
		this.gridColor = new ConfigItem("GridColor", "long", "12632256");
		this.showPageBreaks = new ConfigItem("ShowPageBreaks", "boolean",
				"true");
		this.linkUpdateMode = new ConfigItem("LinkUpdateMode", "short", "3");
		this.hasColumnRowHeaders = new ConfigItem("HasColumnRowHeaders",
				"boolean", "true");
		this.hasSheetTabs = new ConfigItem("HasSheetTabs", "boolean", "true");
		this.isOutlineSymbolsSet = new ConfigItem("IsOutlineSymbolsSet",
				"boolean", "true");
		this.isSnapToRaster = new ConfigItem("IsSnapToRaster", "boolean",
				"false");
		this.rasterIsVisible = new ConfigItem("RasterIsVisible", "boolean",
				"false");
		this.rasterResolutionX = new ConfigItem("RasterResolutionX", "int",
				"1000");
		this.rasterResolutionY = new ConfigItem("RasterResolutionY", "int",
				"1000");
		this.rasterSubdivisionX = new ConfigItem("RasterSubdivisionX", "int",
				"1");
		this.rasterSubdivisionY = new ConfigItem("RasterSubdivisionY", "int",
				"1");
		this.isRasterAxisSynchronized = new ConfigItem(
				"IsRasterAxisSynchronized", "boolean", "true");
		this.autoCalculate = new ConfigItem("AutoCalculate", "boolean", "true");
		this.printerName = new ConfigItem("PrinterName", "string", "");
		this.printerSetup = new ConfigItem("PrinterSetup", "base64Binary", "");
		this.applyUserData = new ConfigItem("ApplyUserData", "boolean", "true");
		this.characterCompressionType = new ConfigItem(
				"CharacterCompressionType", "short", "0");
		this.isKernAsianPunctuation = new ConfigItem("IsKernAsianPunctuation",
				"boolean", "false");
		this.saveVersionOnClose = new ConfigItem("SaveVersionOnClose",
				"boolean", "false");
		this.updateFromTemplate = new ConfigItem("UpdateFromTemplate",
				"boolean", "true");
		this.allowPrintJobCancel = new ConfigItem("AllowPrintJobCancel",
				"boolean", "true");
		this.loadReadonly = new ConfigItem("LoadReadonly", "boolean", "false");
		// this.tableConfigs = new LinkedList<String>();
	}

	/**
	 * Set the active table , this is the table that is shown if you open the
	 * file.
	 *
	 * @param sName
	 *            The table name, this table should already exist, otherwise the
	 *            first table is shown
	 */
	public void setActiveTable(final Table table) {
		this.viewIdActiveTable = new ConfigItem("ActiveTable", "string", table.getName());
	}

	public void setTables(final List<Table> tables) {
		this.tables = tables;
	}

	@Override
	public void write(final Util util, final ZipOutputStream zipOut, final Writer writer)
			throws IOException {
		zipOut.putNextEntry(new ZipEntry("settings.xml"));
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		writer.write(
				"<office:document-settings xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:settingsEntry=\"urn:oasis:names:tc:opendocument:xmlns:settingsEntry:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" office:version=\"1.1\">");
		writer.write("<office:settings>");
		writer.write(
				"<settingsEntry:settingsEntry-item-set settingsEntry:name=\"ooo:view-settings\">");
		this.visibleAreaTop.appendXMLToObject(util, writer);
		this.visibleAreaLeft.appendXMLToObject(util, writer);
		this.visibleAreaWidth.appendXMLToObject(util, writer);
		this.visibleAreaHeight.appendXMLToObject(util, writer);
		writer.write(
				"<settingsEntry:settingsEntry-item-map-indexed settingsEntry:name=\"Views\">");
		writer.write("<settingsEntry:settingsEntry-item-map-entry>");
		writer.write(
				"<settingsEntry:settingsEntry-item settingsEntry:name=\"ViewId\" settingsEntry:type=\"string\">View1</settingsEntry:settingsEntry-item>");
		writer.write(
				"<settingsEntry:settingsEntry-item-map-named settingsEntry:name=\"Tables\">");

		for (final Table t : this.tables)
			t.appendXMLToSettingsEntry(util, writer);

		writer.write("</settingsEntry:settingsEntry-item-map-named>");
		this.viewIdActiveTable.appendXMLToObject(util, writer);
		this.viewIdHorizontalScrollbarWidth.appendXMLToObject(util, writer);
		this.viewIdPageViewZoomValue.appendXMLToObject(util, writer);
		this.viewIdZoomType.appendXMLToObject(util, writer);
		this.viewIdZoomValue.appendXMLToObject(util, writer);
		this.viewIdShowPageBreakPreview.appendXMLToObject(util, writer);
		this.viewIdShowZeroValues.appendXMLToObject(util, writer);
		this.viewIdShowNotes.appendXMLToObject(util, writer);
		this.viewIdShowGrid.appendXMLToObject(util, writer);
		this.viewIdGridColor.appendXMLToObject(util, writer);
		this.viewIdShowPageBreaks.appendXMLToObject(util, writer);
		this.viewIdHasColumnRowHeaders.appendXMLToObject(util, writer);
		this.viewIdIsOutlineSymbolsSet.appendXMLToObject(util, writer);
		this.viewIdHasSheetTabs.appendXMLToObject(util, writer);
		this.viewIdIsSnapToRaster.appendXMLToObject(util, writer);
		this.viewIdRasterIsVisible.appendXMLToObject(util, writer);
		this.viewIdRasterResolutionX.appendXMLToObject(util, writer);
		this.viewIdRasterResolutionY.appendXMLToObject(util, writer);
		this.viewIdRasterSubdivisionX.appendXMLToObject(util, writer);

		this.viewIdRasterSubdivisionY.appendXMLToObject(util, writer);
		this.viewIdIsRasterAxisSynchronized.appendXMLToObject(util, writer);
		writer.write("</settingsEntry:settingsEntry-item-map-entry>");
		writer.write("</settingsEntry:settingsEntry-item-map-indexed>");
		writer.write("</settingsEntry:settingsEntry-item-set>");
		writer.write(
				"<settingsEntry:settingsEntry-item-set settingsEntry:name=\"ooo:configuration-settings\">");
		this.showZeroValues.appendXMLToObject(util, writer);
		this.showNotes.appendXMLToObject(util, writer);
		this.showGrid.appendXMLToObject(util, writer);
		this.gridColor.appendXMLToObject(util, writer);
		this.showPageBreaks.appendXMLToObject(util, writer);
		this.linkUpdateMode.appendXMLToObject(util, writer);
		this.hasColumnRowHeaders.appendXMLToObject(util, writer);
		this.hasSheetTabs.appendXMLToObject(util, writer);
		this.isOutlineSymbolsSet.appendXMLToObject(util, writer);
		this.isSnapToRaster.appendXMLToObject(util, writer);
		this.rasterIsVisible.appendXMLToObject(util, writer);
		this.rasterResolutionX.appendXMLToObject(util, writer);
		this.rasterResolutionY.appendXMLToObject(util, writer);
		this.rasterSubdivisionX.appendXMLToObject(util, writer);
		this.rasterSubdivisionY.appendXMLToObject(util, writer);
		this.isRasterAxisSynchronized.appendXMLToObject(util, writer);
		this.autoCalculate.appendXMLToObject(util, writer);
		this.printerName.appendXMLToObject(util, writer);
		this.printerSetup.appendXMLToObject(util, writer);
		this.applyUserData.appendXMLToObject(util, writer);
		this.characterCompressionType.appendXMLToObject(util, writer);
		this.isKernAsianPunctuation.appendXMLToObject(util, writer);
		this.saveVersionOnClose.appendXMLToObject(util, writer);
		this.updateFromTemplate.appendXMLToObject(util, writer);
		this.allowPrintJobCancel.appendXMLToObject(util, writer);
		this.loadReadonly.appendXMLToObject(util, writer);
		writer.write("</settingsEntry:settingsEntry-item-set>");
		writer.write("</office:settings>");
		writer.write("</office:document-settings>");
		writer.flush();
		zipOut.closeEntry();
	}
}
