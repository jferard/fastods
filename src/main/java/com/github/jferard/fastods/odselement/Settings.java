/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * 3.10 office:settings
 * <p>
 * A typical {@code settings.xml} file has two {@code config-item-set}s:
 * <ul>
 * <li>{@code ooo:view-settings}
 * <ul>
 * <li>{@code config-item}s for the view settings</li>
 * <li>{@code config-item-map-indexed} with a {@code config-item-map-entry} per view
 * <ul>
 * <li>{@code config-item}s of the wiew</li>
 * <li>a {@code config-item-map-named} with a {@code config-item-map-entry} per table
 * <ul>
 * <li>{@code config-item}s of the table in the wiew</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * </ul>
 * <li>{@code ooo:configuration-settings}
 * <ul>
 * <li>{@code config-item}s for the configuration settings</li>
 * </ul>
 * </li>
 * </ul>
 * *
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
@SuppressWarnings("PMD.CommentRequired")
public class Settings {
	private final List<ConfigBlock> rootBlocks;
	private final ConfigItemMapNamed tablesMap;
	private final ConfigItemSet configurationSettings;
	private final ConfigItemSet viewSettings;
	private final ConfigItemMapEntry firstView;
	private final ConfigItemMapIndexed views;

	Settings() {
		this.rootBlocks = new ArrayList<ConfigBlock>();
		this.viewSettings = new ConfigItemSet("ooo:view-settings");
		this.views = new ConfigItemMapIndexed("Views");
		this.firstView = new ConfigItemSetMapEntry("");
		this.tablesMap = new ConfigItemMapNamed("Tables");
		this.configurationSettings = new ConfigItemSet("ooo:configuration-settings");

		this.rootBlocks.add(this.viewSettings);
		this.viewSettings.add(views);
		this.views.add(firstView);
		this.firstView.add(tablesMap);
		this.rootBlocks.add(this.configurationSettings);

		viewSettings.add(new ConfigItem("VisibleAreaTop", "int", "0"));
		viewSettings.add(new ConfigItem("VisibleAreaLeft", "int", "0"));
		viewSettings.add(new ConfigItem("VisibleAreaWidth", "int", "680"));
		viewSettings.add(new ConfigItem("VisibleAreaHeight", "int", "400"));

		firstView.add(new ConfigItem("ViewId", "string", "View1"));
		firstView.add(new ConfigItem("ActiveTable", "string", "Tab1"));
		firstView.add(new ConfigItem("HorizontalScrollbarWidth", "int", "270"));
		firstView.add(new ConfigItem("ZoomType", "short", "0"));
		firstView.add(new ConfigItem("ZoomValue", "int", "100"));
		firstView.add(new ConfigItem("PageViewZoomValue", "int", "60"));
		firstView.add(new ConfigItem("ShowPageBreakPreview", "boolean", "false"));
		firstView.add(new ConfigItem("ShowZeroValues", "boolean", "true"));
		firstView.add(new ConfigItem("ShowNotes", "boolean", "true"));
		firstView.add(new ConfigItem("ShowGrid", "boolean", "true"));
		firstView.add(new ConfigItem("GridColor", "long", "12632256"));
		firstView.add(new ConfigItem("ShowPageBreaks", "boolean", "true"));
		firstView.add(new ConfigItem("HasColumnRowHeaders", "boolean", "true"));
		firstView.add(new ConfigItem("HasSheetTabs", "boolean", "true"));
		firstView.add(new ConfigItem("IsOutlineSymbolsSet", "boolean", "true"));
		firstView.add(new ConfigItem("IsSnapToRaster", "boolean", "false"));
		firstView.add(new ConfigItem("RasterIsVisible", "boolean", "false"));
		firstView.add(new ConfigItem("RasterResolutionX", "int", "1000"));
		firstView.add(new ConfigItem("RasterResolutionY", "int", "1000"));
		firstView.add(new ConfigItem("RasterSubdivisionX", "int", "1"));
		firstView.add(new ConfigItem("RasterSubdivisionY", "int", "1"));
		firstView.add(new ConfigItem("IsRasterAxisSynchronized", "boolean", "true"));

		configurationSettings.add(new ConfigItem("ShowZeroValues", "boolean", "true"));
		configurationSettings.add(new ConfigItem("ShowNotes", "boolean", "true"));
		configurationSettings.add(new ConfigItem("ShowGrid", "boolean", "true"));
		configurationSettings.add(new ConfigItem("GridColor", "long", "12632256"));
		configurationSettings.add(new ConfigItem("ShowPageBreaks", "boolean", "true"));
		configurationSettings.add(new ConfigItem("LinkUpdateMode", "short", "3"));
		configurationSettings.add(new ConfigItem("HasColumnRowHeaders", "boolean", "true"));
		configurationSettings.add(new ConfigItem("HasSheetTabs", "boolean", "true"));
		configurationSettings.add(new ConfigItem("IsOutlineSymbolsSet", "boolean", "true"));
		configurationSettings.add(new ConfigItem("IsSnapToRaster", "boolean", "false"));
		configurationSettings.add(new ConfigItem("RasterIsVisible", "boolean", "false"));
		configurationSettings.add(new ConfigItem("RasterResolutionX", "int", "1000"));
		configurationSettings.add(new ConfigItem("RasterResolutionY", "int", "1000"));
		configurationSettings.add(new ConfigItem("RasterSubdivisionX", "int", "1"));
		configurationSettings.add(new ConfigItem("RasterSubdivisionY", "int", "1"));
		configurationSettings.add(new ConfigItem("IsRasterAxisSynchronized", "boolean", "true"));
		configurationSettings.add(new ConfigItem("AutoCalculate", "boolean", "true"));
		configurationSettings.add(new ConfigItem("PrinterName", "string", ""));
		configurationSettings.add(new ConfigItem("PrinterSetup", "base64Binary", ""));
		configurationSettings.add(new ConfigItem("ApplyUserData", "boolean", "true"));
		configurationSettings.add(new ConfigItem("CharacterCompressionType", "short", "0"));
		configurationSettings.add(new ConfigItem("IsKernAsianPunctuation", "boolean", "false"));
		configurationSettings.add(new ConfigItem("SaveVersionOnClose", "boolean", "false"));
		configurationSettings.add(new ConfigItem("UpdateFromTemplate", "boolean", "true"));
		configurationSettings.add(new ConfigItem("AllowPrintJobCancel", "boolean", "true"));
		configurationSettings.add(new ConfigItem("LoadReadonly", "boolean", "false"));
	}

	public List<ConfigBlock> getRootBlocks() {
		return rootBlocks;
	}

	/**
	 * Set the active table , this is the table that is shown if you open the
	 * file.
	 *
	 * @param table The table to show
	 */
	public void setActiveTable(final Table table) {
		this.firstView.add(new ConfigItem("ActiveTable", "string",
				table.getName()));
	}
}