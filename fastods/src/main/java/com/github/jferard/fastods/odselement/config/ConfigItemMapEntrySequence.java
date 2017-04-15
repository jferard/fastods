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

package com.github.jferard.fastods.odselement.config;

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A sequence of ConfigBlocks.
 * See 3.10.5 config:config-item-map-entry
 * @author Julien Férard
 */
public class ConfigItemMapEntrySequence implements ConfigItemMapEntry {
	private final List<ConfigBlock> blocks;
	private final String name;

	/**
	 * @return a new sequence
	 */
	public static ConfigItemMapEntry createSequence() {
		return new ConfigItemMapEntrySequence(new ArrayList<ConfigBlock>());
	}

	/**
	 * @param name the name of the sequence
	 * @return a new named sequence
	 */
	public static ConfigItemMapEntry createSequence(final String name) {
		return new ConfigItemMapEntrySequence(name, new ArrayList<ConfigBlock>());
	}


	/**
	 * @param blocks the block sequence, as a list
	 */
	ConfigItemMapEntrySequence(final List<ConfigBlock> blocks) {
		this(null, blocks);
	}

	/**
	 * @param name the name of the sequence
	 * @param blocks the block sequence, as a list
	 */
	ConfigItemMapEntrySequence(final String name, final List<ConfigBlock> blocks) {
		this.name = name;
		this.blocks = blocks;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int size() {
		return this.blocks.size();
	}

	@Override
	public boolean isEmpty() {
		return this.blocks.isEmpty();
	}

	@Override
	public boolean add(final ConfigBlock block) {
		return this.blocks.add(block);
	}

	/**
	 * Remove the block at a given index.
	 * @param i the index.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
	 */
	public void remove(final int i) {
		this.blocks.remove(i);
	}

	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-entry");
		if (this.name != null)
			util.appendAttribute(appendable, "config:name", this.name);
		appendable.append(">");
		for (final ConfigBlock block : this.blocks)
			block.appendXML(util, appendable);
		appendable.append("</config:config-item-map-entry>");
	}

	@Override
	public Iterator<ConfigBlock> iterator() {
		return this.blocks.iterator();
	}

	/**
	 * Set a value for an item at a given index
	 * @param i the index
	 * @param value the new value
	 * @return the previous value
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
	 */
	public String set(final int i, final String value) {
		final ConfigBlock block = this.blocks.get(i);
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
		final ConfigItem item = new ConfigItem(name, type, value);
		return this.blocks.add(item);
	}
}