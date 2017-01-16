/*
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
 * 3.10.5 config:config-item-map-entry
 */
public class ConfigItemMapEntrySet implements ConfigItemMapEntry {
	private final Map<String, ConfigBlock> blockByName;
	private final String name;

	public static ConfigItemMapEntrySet createSet() {
		return new ConfigItemMapEntrySet(new HashSet<ConfigBlock>());
	}

	public static ConfigItemMapEntrySet createSet(final String name) {
		return new ConfigItemMapEntrySet(name, new HashSet<ConfigBlock>());
	}


	ConfigItemMapEntrySet(final Set<ConfigBlock> blocks) {
		this(null, blocks);
	}

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
		return blockByName.size();
	}

	@Override
	public boolean isEmpty() {
		return blockByName.isEmpty();
	}

	public boolean contains(final String name) {
		return blockByName.containsKey(name);
	}

	@Override
	public boolean add(final ConfigBlock block) {
		final String name = block.getName();
		if (blockByName.containsKey(name))
			return false;

		blockByName.put(name, block);
		return true;
	}

	public void remove(final Object o) {
		blockByName.remove(o);
	}

	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-entry");
		if (this.name != null)
			util.appendAttribute(appendable, "config:name", this.name);
		appendable.append(">");
		for (final ConfigBlock block : this.blockByName.values())
			block.appendXML(util, appendable);
		appendable.append("</config:config-item-map-entry>");
	}

	@Override
	public Iterator<ConfigBlock> iterator() {
		return this.blockByName.values().iterator();
	}

	public ConfigBlock getByName(final String name) {
		return this.blockByName.get(name);
	}

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
			return false;

		this.blockByName.put(name, new ConfigItem(name, type, value));
		return true;
	}
}