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

package com.github.jferard.fastods.odselement.config;

/**
 * A ConfigItemMapEntry represents an entry in the settings. It can be a sequence, a set or a
 * singleton.
 * <p>
 * See 3.10.5 config:config-item-map-entry
 *
 * @author Julien Férard
 */
public interface ConfigItemMapEntry extends ConfigItemCollection<ConfigBlock> {
    /**
     * Add a block to this entry.
     *
     * @param block the block to add to this entry. May throw
     * @return true if the block was added
     * @throws UnsupportedOperationException if the entry is a singleton
     */
    boolean add(ConfigBlock block);

    /**
     * Put a block in this entry. Replace existing block with the same name.
     *
     * @param block the block to add to this entry. May throw
     * @return the previous block or null.
     * @throws UnsupportedOperationException if the entry is a singleton or a list
     */
    ConfigBlock put(ConfigBlock block);

    /**
     * Add an ConfigItem to this entry. Shortcut for {@code add(new ConfigItem(name, type, value))}.
     *
     * @param name  the name of the item
     * @param type  the type of the item
     * @param value the value of the item
     * @return true if the block was added
     * @throws UnsupportedOperationException if the entry is a singleton
     */
    boolean add(String name, String type, String value);
}