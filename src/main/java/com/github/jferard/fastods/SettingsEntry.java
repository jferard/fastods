package com.github.jferard.fastods;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SettingsEntry implements OdsEntry {
	// ViewSettings
	private ConfigItem VisibleAreaTop = new ConfigItem("VisibleAreaTop", "int",
			"0");
	private ConfigItem VisibleAreaLeft = new ConfigItem("VisibleAreaLeft",
			"int", "0");
	private ConfigItem VisibleAreaWidth = new ConfigItem("VisibleAreaWidth",
			"int", "680");
	private ConfigItem VisibleAreaHeight = new ConfigItem("VisibleAreaHeight",
			"int", "400");

	// ViewIdSettings
	private ConfigItem ViewIdActiveTable = new ConfigItem("ActiveTable",
			"string", "Tab1");
	private ConfigItem ViewIdHorizontalScrollbarWidth = new ConfigItem(
			"ViewIdHorizontalScrollbarWidth", "int", "270");
	private ConfigItem ViewIdZoomType = new ConfigItem("ViewIdZoomType",
			"short", "0");
	private ConfigItem ViewIdZoomValue = new ConfigItem("ViewIdZoomValue",
			"int", "100");
	private ConfigItem ViewIdPageViewZoomValue = new ConfigItem(
			"ViewIdPageViewZoomValue", "int", "60");
	private ConfigItem ViewIdShowPageBreakPreview = new ConfigItem(
			"ViewIdShowPageBreakPreview", "boolean", "false");
	private ConfigItem ViewIdShowZeroValues = new ConfigItem(
			"ViewIdShowZeroValues", "boolean", "true");
	private ConfigItem ViewIdShowNotes = new ConfigItem("ViewIdShowNotes",
			"boolean", "true");
	private ConfigItem ViewIdShowGrid = new ConfigItem("ViewIdShowGrid",
			"boolean", "true");
	private ConfigItem ViewIdGridColor = new ConfigItem("ViewIdGridColor",
			"long", "12632256");
	private ConfigItem ViewIdShowPageBreaks = new ConfigItem(
			"ViewIdShowPageBreaks", "boolean", "true");
	private ConfigItem ViewIdHasColumnRowHeaders = new ConfigItem(
			"ViewIdHasColumnRowHeaders", "boolean", "true");
	private ConfigItem ViewIdHasSheetTabs = new ConfigItem("ViewIdHasSheetTabs",
			"boolean", "true");
	private ConfigItem ViewIdIsOutlineSymbolsSet = new ConfigItem(
			"ViewIdIsOutlineSymbolsSet", "boolean", "true");
	private ConfigItem ViewIdIsSnapToRaster = new ConfigItem(
			"ViewIdIsSnapToRaster", "boolean", "false");
	private ConfigItem ViewIdRasterIsVisible = new ConfigItem(
			"ViewIdRasterIsVisible", "boolean", "false");
	private ConfigItem ViewIdRasterResolutionX = new ConfigItem(
			"ViewIdRasterResolutionX", "int", "1000");
	private ConfigItem ViewIdRasterResolutionY = new ConfigItem(
			"ViewIdRasterResolutionY", "int", "1000");
	private ConfigItem ViewIdRasterSubdivisionX = new ConfigItem(
			"ViewIdRasterSubdivisionX", "int", "1");
	private ConfigItem ViewIdRasterSubdivisionY = new ConfigItem(
			"ViewIdRasterSubdivisionY", "int", "1");
	private ConfigItem ViewIdIsRasterAxisSynchronized = new ConfigItem(
			"ViewIdIsRasterAxisSynchronized", "boolean", "true");

	// ConfigurationSettings
	private ConfigItem ShowZeroValues = new ConfigItem("ShowZeroValues",
			"boolean", "true");
	private ConfigItem ShowNotes = new ConfigItem("ShowNotes", "boolean",
			"true");
	private ConfigItem ShowGrid = new ConfigItem("ShowGrid", "boolean", "true");
	private ConfigItem GridColor = new ConfigItem("GridColor", "long",
			"12632256");
	private ConfigItem ShowPageBreaks = new ConfigItem("ShowPageBreaks",
			"boolean", "true");
	private ConfigItem LinkUpdateMode = new ConfigItem("LinkUpdateMode",
			"short", "3");
	private ConfigItem HasColumnRowHeaders = new ConfigItem(
			"HasColumnRowHeaders", "boolean", "true");
	private ConfigItem HasSheetTabs = new ConfigItem("HasSheetTabs", "boolean",
			"true");
	private ConfigItem IsOutlineSymbolsSet = new ConfigItem(
			"IsOutlineSymbolsSet", "boolean", "true");
	private ConfigItem IsSnapToRaster = new ConfigItem("IsSnapToRaster",
			"boolean", "false");
	private ConfigItem RasterIsVisible = new ConfigItem("RasterIsVisible",
			"boolean", "false");
	private ConfigItem RasterResolutionX = new ConfigItem("RasterResolutionX",
			"int", "1000");
	private ConfigItem RasterResolutionY = new ConfigItem("RasterResolutionY",
			"int", "1000");
	private ConfigItem RasterSubdivisionX = new ConfigItem("RasterSubdivisionX",
			"int", "1");
	private ConfigItem RasterSubdivisionY = new ConfigItem("RasterSubdivisionY",
			"int", "1");
	private ConfigItem IsRasterAxisSynchronized = new ConfigItem(
			"IsRasterAxisSynchronized", "boolean", "true");
	private ConfigItem AutoCalculate = new ConfigItem("AutoCalculate",
			"boolean", "true");
	private ConfigItem PrinterName = new ConfigItem("PrinterName", "string",
			"");
	private ConfigItem PrinterSetup = new ConfigItem("PrinterSetup",
			"base64Binary", "");
	private ConfigItem ApplyUserData = new ConfigItem("ApplyUserData",
			"boolean", "true");
	private ConfigItem CharacterCompressionType = new ConfigItem(
			"CharacterCompressionType", "short", "0");
	private ConfigItem IsKernAsianPunctuation = new ConfigItem(
			"IsKernAsianPunctuation", "boolean", "false");
	private ConfigItem SaveVersionOnClose = new ConfigItem("SaveVersionOnClose",
			"boolean", "false");
	private ConfigItem UpdateFromTemplate = new ConfigItem("UpdateFromTemplate",
			"boolean", "true");
	private ConfigItem AllowPrintJobCancel = new ConfigItem(
			"AllowPrintJobCancel", "boolean", "true");
	private ConfigItem LoadReadonly = new ConfigItem("LoadReadonly", "boolean",
			"false");

	private List<String> tableConfigs = new LinkedList<String>();

	/**
	 * Set the active table , this is the table that is shown if you open the
	 * file.
	 * 
	 * @param sName
	 *            The table name, this table should already exist, otherwise the
	 *            first table is shown
	 */
	public void setActiveTable(final String sName) {
		this.ViewIdActiveTable = new ConfigItem("ActiveTable", "string", sName);
	}

	public void addTableConfig(String config) {
		this.tableConfigs.add(config);
	}

	public void write(Util util, ZipOutputStream zipOut) throws IOException {
		zipOut.putNextEntry(new ZipEntry("settings.xml"));
		Writer writer = util.wrapStream(zipOut);

		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		writer.write(
				"<office:document-settings xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:settingsEntry=\"urn:oasis:names:tc:opendocument:xmlns:settingsEntry:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" office:version=\"1.1\">");
		writer.write("<office:settings>");
		writer.write(
				"<settingsEntry:settingsEntry-item-set settingsEntry:name=\"ooo:view-settings\">");
		writer.write(this.VisibleAreaTop.toXML(util));
		writer.write(this.VisibleAreaLeft.toXML(util));
		writer.write(this.VisibleAreaWidth.toXML(util));
		writer.write(this.VisibleAreaHeight.toXML(util));
		writer.write(
				"<settingsEntry:settingsEntry-item-map-indexed settingsEntry:name=\"Views\">");
		writer.write("<settingsEntry:settingsEntry-item-map-entry>");
		writer.write(
				"<settingsEntry:settingsEntry-item settingsEntry:name=\"ViewId\" settingsEntry:type=\"string\">View1</settingsEntry:settingsEntry-item>");
		writer.write(
				"<settingsEntry:settingsEntry-item-map-named settingsEntry:name=\"Tables\">");

		for (String config : this.tableConfigs)
			writer.write(config);

		writer.write("</settingsEntry:settingsEntry-item-map-named>");
		writer.write(this.ViewIdActiveTable.toXML(util));
		writer.write(this.ViewIdHorizontalScrollbarWidth.toXML(util));
		writer.write(this.ViewIdPageViewZoomValue.toXML(util));
		writer.write(this.ViewIdZoomType.toXML(util));
		writer.write(this.ViewIdZoomValue.toXML(util));
		writer.write(this.ViewIdShowPageBreakPreview.toXML(util));
		writer.write(this.ViewIdShowZeroValues.toXML(util));
		writer.write(this.ViewIdShowNotes.toXML(util));
		writer.write(this.ViewIdShowGrid.toXML(util));
		writer.write(this.ViewIdGridColor.toXML(util));
		writer.write(this.ViewIdShowPageBreaks.toXML(util));
		writer.write(this.ViewIdHasColumnRowHeaders.toXML(util));
		writer.write(this.ViewIdIsOutlineSymbolsSet.toXML(util));
		writer.write(this.ViewIdHasSheetTabs.toXML(util));
		writer.write(this.ViewIdIsSnapToRaster.toXML(util));
		writer.write(this.ViewIdRasterIsVisible.toXML(util));
		writer.write(this.ViewIdRasterResolutionX.toXML(util));
		writer.write(this.ViewIdRasterResolutionY.toXML(util));
		writer.write(this.ViewIdRasterSubdivisionX.toXML(util));

		writer.write(this.ViewIdRasterSubdivisionY.toXML(util));
		writer.write(this.ViewIdIsRasterAxisSynchronized.toXML(util));
		writer.write("</settingsEntry:settingsEntry-item-map-entry>");
		writer.write("</settingsEntry:settingsEntry-item-map-indexed>");
		writer.write("</settingsEntry:settingsEntry-item-set>");
		writer.write(
				"<settingsEntry:settingsEntry-item-set settingsEntry:name=\"ooo:configuration-settings\">");
		writer.write(this.ShowZeroValues.toXML(util));
		writer.write(this.ShowNotes.toXML(util));
		writer.write(this.ShowGrid.toXML(util));
		writer.write(this.GridColor.toXML(util));
		writer.write(this.ShowPageBreaks.toXML(util));
		writer.write(this.LinkUpdateMode.toXML(util));
		writer.write(this.HasColumnRowHeaders.toXML(util));
		writer.write(this.HasSheetTabs.toXML(util));
		writer.write(this.IsOutlineSymbolsSet.toXML(util));
		writer.write(this.IsSnapToRaster.toXML(util));
		writer.write(this.RasterIsVisible.toXML(util));
		writer.write(this.RasterResolutionX.toXML(util));
		writer.write(this.RasterResolutionY.toXML(util));
		writer.write(this.RasterSubdivisionX.toXML(util));
		writer.write(this.RasterSubdivisionY.toXML(util));
		writer.write(this.IsRasterAxisSynchronized.toXML(util));
		writer.write(this.AutoCalculate.toXML(util));
		writer.write(this.PrinterName.toXML(util));
		writer.write(this.PrinterSetup.toXML(util));
		writer.write(this.ApplyUserData.toXML(util));
		writer.write(this.CharacterCompressionType.toXML(util));
		writer.write(this.IsKernAsianPunctuation.toXML(util));
		writer.write(this.SaveVersionOnClose.toXML(util));
		writer.write(this.UpdateFromTemplate.toXML(util));
		writer.write(this.AllowPrintJobCancel.toXML(util));
		writer.write(this.LoadReadonly.toXML(util));
		writer.write("</settingsEntry:settingsEntry-item-set>");
		writer.write("</office:settings>");
		writer.write("</office:document-settings>");

		writer.flush();
		zipOut.closeEntry();
	}
}
