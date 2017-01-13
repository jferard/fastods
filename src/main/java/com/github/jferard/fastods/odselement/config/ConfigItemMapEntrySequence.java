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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 3.10.5 config:config-item-map-entry
 */
public class ConfigItemMapEntrySequence implements ConfigItemMapEntry {
	private final List<ConfigBlock> blocks;
	private String name;

	public static ConfigItemMapEntry createSequence() {
		return new ConfigItemMapEntrySequence(new ArrayList<ConfigBlock>());
	}

	public static ConfigItemMapEntry createSequence(String name) {
		return new ConfigItemMapEntrySequence(name, new ArrayList<ConfigBlock>());
	}


	ConfigItemMapEntrySequence(List<ConfigBlock> blocks) {
		this(null, blocks);
	}

	ConfigItemMapEntrySequence(String name, List<ConfigBlock> blocks) {
		this.name = name;
		this.blocks = blocks;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int size() {
		return blocks.size();
	}

	@Override
	public boolean isEmpty() {
		return blocks.isEmpty();
	}

	@Override
	public boolean add(ConfigBlock block) {
		return blocks.add(block);
	}

	public void remove(int i) {
		blocks.remove(i);
	}

	@Override
	public void appendXML(XMLUtil util, Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-entry");
		if (this.name != null)
			util.appendAttribute(appendable, "config:name", this.name);
		appendable.append(">");
		for (ConfigBlock block : this.blocks)
			block.appendXML(util, appendable);
		appendable.append("</config:config-item-map-entry>");
	}

	@Override
	public Iterator<ConfigBlock> iterator() {
		return this.blocks.iterator();
	}

	public String set(int i, String value) {
		ConfigBlock block = this.blocks.get(i);
		if (block instanceof ConfigItem) {
			ConfigItem item = (ConfigItem) block;
			String previousValue = item.getValue();
			item.setValue(value);
			return previousValue;
		}
		return null;
	}

	@Override
	public boolean add(String name, String type, String value) {
		ConfigItem item = new ConfigItem(name, type, value);
		return this.blocks.add(item);
	}
}