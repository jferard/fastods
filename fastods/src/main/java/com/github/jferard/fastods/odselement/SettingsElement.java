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

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.odselement.config.ConfigBlock;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;

import static com.github.jferard.fastods.odselement.MetaElement.OFFICE_VERSION;

/**
 * 3.1.3.5 office:document-settings and 3.10 office:settings
 * <p>
 * A typical {@code settings.xml} file has two {@code config-item-set}s:
 * <ul>
 *     <li>{@code ooo:view-settings}
 *     		<ul>
 *     		 	   <li>{@code config-item}s for the view settings</li>
 *     		 	   <li>{@code config-item-map-indexed} with a {@code config-item-map-entry} per
 *     		 	   view
 *     					<ul>
 *     		 	   			<li>{@code config-item}s of the view</li>
 *     		 	   			<li>a {@code config-item-map-named} with a {@code config-item-map
 *     		 	   			-entry} per table
 * 		    					<ul>
 *     				 	   			<li>{@code config-item}s of the table in the wiew</li>
 * 		    					</ul>
 *     		 	   			</li>
 *     					</ul>
 *     		 	   </li>
 *     		</ul>
 *     	<li>{@code ooo:configuration-settings}
 *     		<ul>
 *     		    <li>{@code config-item}s for the configuration settings</li>
 *     		</ul>
 *     	</li>
 * </ul>
 * *
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
@SuppressWarnings("PMD.CommentRequired")
public class SettingsElement implements OdsElement {
    public static final Map<String, String> SETTINGS_NAMESPACE_BY_PREFIX = new HashMap<String, String>();

    static {
        SETTINGS_NAMESPACE_BY_PREFIX.putAll(OdsElements.BASE_NAMESPACE_BY_PREFIX);
        SETTINGS_NAMESPACE_BY_PREFIX.put("xmlns:config",
                "urn:oasis:names:tc:opendocument:xmlns:config:1.0");
    }

    /**
     * @return a settings.xml element
     */
    static SettingsElement create() {
        return new SettingsElement(Settings.create());
    }

    private final Settings settings;
    private List<Table> tables;

    /**
     * Create a settings.xml element
     *
     * @param settings the settings
     */
    SettingsElement(final Settings settings) {
        this.settings = settings;
    }

    /**
     * Add a table config.
     *
     * @param configEntry the config
     */
    public void addTableConfig(final ConfigItemMapEntry configEntry) {
        this.settings.addTableConfig(configEntry);
    }

    /**
     * Set the tables
     *
     * @param tables the tables
     */
    public void setTables(final List<Table> tables) {
        this.settings.setTables(tables);
    }

    @Override
    public void write(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
        writer.putNextEntry(new ZipEntry("settings.xml"));
        writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        writer.append("<office:document-settings");
        for (final Map.Entry<String, String> entry: SETTINGS_NAMESPACE_BY_PREFIX.entrySet()) {
            util.appendAttribute(writer, entry.getKey(), entry.getValue());
        }
        util.appendAttribute(writer, "office:version", OFFICE_VERSION);
        writer.append("><office:settings>");
        for (final ConfigBlock block : this.settings.getRootBlocks()) {
            block.appendXMLContent(util, writer);
        }
        writer.append("</office:settings>");
        writer.append("</office:document-settings>");
        writer.flush();
        writer.closeEntry();
    }

    /**
     * Set the active table
     *
     * @param table the table
     */
    public void setActiveTable(final Table table) {
        this.settings.setActiveTable(table);
    }

    /**
     * Set a view setting
     *
     * @param viewId the view id
     * @param item   the item name
     * @param value  the item value
     */
    public void setViewSetting(final String viewId, final String item, final String value) {
        this.settings.setViewSetting(viewId, item, value);
    }
}