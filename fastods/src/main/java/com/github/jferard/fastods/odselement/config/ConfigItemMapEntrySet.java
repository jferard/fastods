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

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A hashtable of ConfigBlocks.
 * 3.10.5 config:config-item-map-entry
 * @author Julien Férard
 */
public class ConfigItemMapEntrySet implements ConfigItemMapEntry {
	private final Map<String, ConfigBlock> blockByName;
	private final String name;

	/**
	 * @return a new ConfigItemMapEntrySet
	 */
	public static ConfigItemMapEntrySet createSet() {
		return new ConfigItemMapEntrySet(new HashSet<ConfigBlock>());
	}

	/**
	 * @param name the name of the hashtable
	 * @return a new ConfigItemMapEntrySet
	 */
	public static ConfigItemMapEntrySet createSet(final String name) {
		return new ConfigItemMapEntrySet(name, new HashSet<ConfigBlock>());
	}


	/**
	 * @param blocks the set of blocks
	 */
	ConfigItemMapEntrySet(final Set<ConfigBlock> blocks) {
		this(null, blocks);
	}

	/**
	 * @param name the name of the config item set
	 * @param blocks the set of blocks
	 */
	ConfigItemMapEntrySet(final String name, final Set<ConfigBlock> blocks) {
		this.name = name;
		this.blockByName = new HashMap<String, ConfigBlock>();
		for (final ConfigBlock block : blocks)
			this.blockByName.put(block.getName(), block);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int size() {
		return this.blockByName.size();
	}

	@Override
	public boolean isEmpty() {
		return this.blockByName.isEmpty();
	}

	/**
	 * @param name the name to look for
	 * @return true if a block with that name exists in the set
	 */
	public boolean contains(final String name) {
		return this.blockByName.containsKey(name);
	}

	@Override
	public boolean add(final ConfigBlock block) {
		final String name = block.getName();
		if (this.blockByName.containsKey(name))
			return false;

		this.blockByName.put(name, block);
		return true;
	}

	@Override
	public ConfigBlock put(final ConfigBlock block) {
		final String name = block.getName();
		return this.blockByName.put(name, block);
	}

	/**
	 * @param o the block to remove
	 */
	public void remove(final Object o) {
		this.blockByName.remove(o);
	}

	@Override
	public void appendXMLContent(final XMLUtil util, final Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-entry");
		if (this.name != null)
			util.appendEAttribute(appendable, "config:name", this.name);
		appendable.append(">");
		for (final ConfigBlock block : this.blockByName.values())
			block.appendXMLContent(util, appendable);
		appendable.append("</config:config-item-map-entry>");
	}

	@Override
	public Iterator<ConfigBlock> iterator() {
		return this.blockByName.values().iterator();
	}

	/**
	 * @param name the name of the config block
	 * @return the config block matching the name, or null
	 */
	public ConfigBlock getByName(final String name) {
		return this.blockByName.get(name);
	}

	/**
	 * Set a value for an item with a given name
	 * @param name the name of the item
	 * @param value the new value
	 * @return the previous value, or null if the block of that name doesn't exist or is not a ConfigItem.
	 */
	public String set(final String name, final String value) {
		final ConfigBlock block = this.blockByName.get(name);
		if (block instanceof ConfigItem) {
			final ConfigItem item = (ConfigItem) block;
			final String previousValue = item.getValue();
			item.setValue(value);
			return previousValue;
		}
		return null;
	}

	@Override
	public boolean add(final String name, final String type, final String value) {
		final ConfigBlock block = this.getByName(name);
		if (block instanceof ConfigItem)
			return false; // can't add an item twice, but ok for a different block with the same name

		this.blockByName.put(name, new ConfigItem(name, type, value));
		return true;
	}
}