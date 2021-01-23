/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.odselement.config.ConfigBlock;
import com.github.jferard.fastods.odselement.config.ConfigElement;
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntrySet;
import com.github.jferard.fastods.odselement.config.ConfigItemMapIndexed;
import com.github.jferard.fastods.odselement.config.ConfigItemMapNamed;
import com.github.jferard.fastods.odselement.config.ConfigItemSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 3.10 office:settings
 * <p>
 * A typical {@code settings.xml} file has two {@code config-item-set}s:
 * <ul>
 * <li>{@code ooo:view-settings}
 * <li>{@code config-item}s for the view settings</li>
 * <li>{@code config-item-map-indexed} with a {@code config-item-map-entry} per view
 * <li>{@code config-item}s of the view</li>
 * <li>a {@code config-item-map-named} with a {@code config-item-map-entry} per table
 * <li>{@code config-item}s of the table in the view</li>
 * <li>{@code ooo:configuration-settings}
 * <li>{@code config-item}s for the configuration settings</li>
 * </ul>
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class Settings {
    /**
     * @return a new settings representation
     */
    public static Settings create() {
        final ConfigItemSet viewSettings = new ConfigItemSet("ooo:view-settings");

        // undocumented unokywds.hxx
        viewSettings.add(ConfigItem.create(ConfigElement.VISIBLE_AREA_TOP, "0"));
        viewSettings.add(ConfigItem.create(ConfigElement.VISIBLE_AREA_LEFT, "0"));
        viewSettings.add(ConfigItem.create(ConfigElement.VISIBLE_AREA_WIDTH, "680"));
        viewSettings.add(ConfigItem.create(ConfigElement.VISIBLE_AREA_HEIGHT, "400"));

        final ConfigItemMapEntrySet firstView = ConfigItemMapEntrySet.createSet();
        // com.sun.star.sheet.SpreadsheetViewSettings
        firstView.add(ConfigItem.create(ConfigElement.ZOOM_TYPE, "0"));
        firstView.add(ConfigItem.create(ConfigElement.ZOOM_VALUE, "100"));
        firstView.add(ConfigItem.create(ConfigElement.SHOW_ZERO_VALUES, "true"));
        firstView.add(ConfigItem.create(ConfigElement.SHOW_NOTES, "true"));
        firstView.add(ConfigItem.create(ConfigElement.SHOW_GRID, "true"));
        firstView.add(ConfigItem.create(ConfigElement.GRID_COLOR, "12632256"));
        firstView.add(ConfigItem.create(ConfigElement.SHOW_PAGE_BREAKS, "true"));
        firstView.add(ConfigItem.create(ConfigElement.HAS_COLUMN_ROW_HEADERS, "true"));
        firstView.add(ConfigItem.create(ConfigElement.HAS_SHEET_TABS, "true"));
        firstView.add(ConfigItem.create(ConfigElement.IS_OUTLINE_SYMBOLS_SET, "true"));

        // com.sun.star.sheet.DocumentSettings
        firstView.add(ConfigItem.create(ConfigElement.IS_SNAP_TO_RASTER, "false"));
        firstView.add(ConfigItem.create(ConfigElement.RASTER_IS_VISIBLE, "false"));
        firstView.add(ConfigItem.create(ConfigElement.RASTER_RESOLUTION_X, "1000"));
        firstView.add(ConfigItem.create(ConfigElement.RASTER_RESOLUTION_Y, "1000"));
        firstView.add(ConfigItem.create(ConfigElement.RASTER_SUBDIVISION_X, "1"));
        firstView.add(ConfigItem.create(ConfigElement.RASTER_SUBDIVISION_Y, "1"));
        firstView.add(ConfigItem.create(ConfigElement.IS_RASTER_AXIS_SYNCHRONIZED, "true"));


        // undocumented ViewSettingsSequenceDefines.hxx
        firstView.add(ConfigItem.create(ConfigElement.VIEW_ID, "View1"));
        firstView.add(ConfigItem.create(ConfigElement.ACTIVE_TABLE, "Sheet1"));
        firstView.add(ConfigItem.create(ConfigElement.HORIZONTAL_SCROLLBAR_WIDTH, "270"));
        firstView.add(ConfigItem.create(ConfigElement.PAGE_VIEW_ZOOM_VALUE, "60"));
        firstView.add(ConfigItem.create(ConfigElement.SHOW_PAGE_BREAK_PREVIEW, "false"));

        final ConfigItemSet configurationSettings = new ConfigItemSet("ooo:configuration-settings");

        // com.sun.star.sheet.SpreadsheetViewSettings
        configurationSettings.add(ConfigItem.create(ConfigElement.SHOW_ZERO_VALUES, "true"));
        configurationSettings.add(ConfigItem.create(ConfigElement.SHOW_NOTES, "true"));
        configurationSettings.add(ConfigItem.create(ConfigElement.SHOW_GRID, "true"));
        configurationSettings.add(ConfigItem.create(ConfigElement.GRID_COLOR, "12632256"));
        configurationSettings.add(ConfigItem.create(ConfigElement.SHOW_PAGE_BREAKS, "true"));
        configurationSettings.add(ConfigItem.create(ConfigElement.HAS_COLUMN_ROW_HEADERS, "true"));
        configurationSettings.add(ConfigItem.create(ConfigElement.HAS_SHEET_TABS, "true"));
        configurationSettings.add(ConfigItem.create(ConfigElement.IS_OUTLINE_SYMBOLS_SET, "true"));

        // com.sun.star.document.Settings
        configurationSettings.add(ConfigItem.create(ConfigElement.LINK_UPDATE_MODE, "3"));
        configurationSettings.add(ConfigItem.create(ConfigElement.PRINTER_NAME, ""));
        configurationSettings.add(ConfigItem.create(ConfigElement.PRINTER_SETUP, ""));
        configurationSettings.add(ConfigItem.create(ConfigElement.APPLY_USER_DATA, "true"));
        configurationSettings.add(ConfigItem.create(ConfigElement.CHARACTER_COMPRESSION_TYPE, "0"));
        configurationSettings
                .add(ConfigItem.create(ConfigElement.IS_KERN_ASIAN_PUNCTUATION, "false"));
        configurationSettings.add(ConfigItem.create(ConfigElement.SAVE_VERSION_ON_CLOSE, "false"));
        configurationSettings.add(ConfigItem.create(ConfigElement.UPDATE_FROM_TEMPLATE, "true"));
        configurationSettings.add(ConfigItem.create(ConfigElement.AUTO_CALCULATE, "true"));

        // com.sun.star.sheet.DocumentSettings
        configurationSettings.add(ConfigItem.create(ConfigElement.IS_SNAP_TO_RASTER, "false"));
        configurationSettings.add(ConfigItem.create(ConfigElement.RASTER_IS_VISIBLE, "false"));
        configurationSettings.add(ConfigItem.create(ConfigElement.RASTER_RESOLUTION_X, "1000"));
        configurationSettings.add(ConfigItem.create(ConfigElement.RASTER_RESOLUTION_Y, "1000"));
        configurationSettings.add(ConfigItem.create(ConfigElement.RASTER_SUBDIVISION_X, "1"));
        configurationSettings.add(ConfigItem.create(ConfigElement.RASTER_SUBDIVISION_Y, "1"));
        configurationSettings
                .add(ConfigItem.create(ConfigElement.IS_RASTER_AXIS_SYNCHRONIZED, "true"));

        // undocumented unonames.hxx
        configurationSettings.add(ConfigItem.create(ConfigElement.ALLOW_PRINT_JOB_CANCEL, "true"));
        configurationSettings.add(ConfigItem.create(ConfigElement.LOAD_READONLY, "false"));

        return Settings.create(viewSettings, firstView, configurationSettings);
    }

    /**
     * Create a new settings representation
     *
     * @param viewSettings          inner view settings
     * @param firstView             the view to display
     * @param configurationSettings the configuration
     * @return the settings representation
     */
    static Settings create(final ConfigItemSet viewSettings, final ConfigItemMapEntrySet firstView,
                           final ConfigItemSet configurationSettings) {
        final List<ConfigBlock> rootBlocks = new ArrayList<ConfigBlock>();
        final ConfigItemMapIndexed views = new ConfigItemMapIndexed("Views");
        final Map<String, ConfigItemMapEntrySet> viewById =
                new HashMap<String, ConfigItemMapEntrySet>();
        final ConfigItemMapNamed tablesMap = new ConfigItemMapNamed("Tables");

        return new Settings(rootBlocks, viewSettings, views, viewById, firstView, tablesMap,
                configurationSettings);
    }

    private final ConfigItemMapEntrySet firstView;
    private final List<ConfigBlock> rootBlocks;
    private final ConfigItemMapNamed tablesMap;
    private final Map<String, ConfigItemMapEntrySet> viewById;

    /**
     * Create a new settings representation
     *
     * @param rootBlocks            the root blocks
     * @param viewSettings          inner view settings
     * @param views                 the views
     * @param viewById              a map id -> view
     * @param firstView             the view to display
     * @param tablesMap             the tables map
     * @param configurationSettings the configuration
     */
    Settings(final List<ConfigBlock> rootBlocks, final ConfigItemSet viewSettings,
             final ConfigItemMapIndexed views, final Map<String, ConfigItemMapEntrySet> viewById,
             final ConfigItemMapEntrySet firstView, final ConfigItemMapNamed tablesMap,
             final ConfigItemSet configurationSettings) {
        this.rootBlocks = rootBlocks;
        this.viewById = viewById;
        this.firstView = firstView;
        this.tablesMap = tablesMap;

        // build tree
        views.add(this.firstView);
        viewSettings.add(views);
        this.rootBlocks.add(viewSettings);
        this.viewById.put(((ConfigItem) firstView.getByName("ViewId")).getValue(), firstView);
        this.firstView.add(this.tablesMap);
        this.rootBlocks.add(configurationSettings);
    }

    /**
     * Add a table
     *
     * @param table the table to add
     */
    public void addTable(final Table table) {
        final ConfigItemMapEntry configEntry = table.getConfigEntry();
        this.addTableConfig(configEntry);
    }

    /**
     * A a config for a table
     *
     * @param configEntry the table config
     */
    public void addTableConfig(final ConfigItemMapEntry configEntry) {
        this.tablesMap.put(configEntry);
    }

    /**
     * Get the root blocks, ie item, item-set, item-map that are at the top of the settings.
     *
     * @return the root blocks
     */
    public List<ConfigBlock> getRootBlocks() {
        return this.rootBlocks;
    }

    /**
     * Set the active table , this is the table that is shown if you open the
     * file.
     *
     * @param table The table to show
     */
    public void setActiveTable(final Table table) {
        this.firstView.add(ConfigItem.create(ConfigElement.ACTIVE_TABLE, table.getName()));
    }

    /**
     * Set the tables (replace all existing tables)
     *
     * @param tables the tables
     */
    public void setTables(final List<Table> tables) {
        this.tablesMap.clear();
        for (final Table table : tables) {
            this.addTableConfig(table.getConfigEntry());
        }
    }

    /**
     * Set a view setting
     *
     * @param viewId the view to which add the setting
     * @param item   the item name
     * @param value  the item value
     */
    public void setViewSetting(final String viewId, final String item, final String value) {
        final ConfigItemMapEntrySet view = this.viewById.get(viewId);
        if (view == null) {
            return;
        }

        view.set(item, value);
    }
}