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
import java.util.Arrays;
import java.util.Iterator;

/**
 * 3.10.5 config:config-item-map-entry
 * A Single element.
 */
public class ConfigItemMapEntrySingleton implements ConfigItemMapEntry {
	private final ConfigBlock block;
	private final String name;

	public static ConfigItemMapEntry createSingleton(final String name, final ConfigItem configItem) {
		return new ConfigItemMapEntrySingleton(name, configItem);
	}

	public static ConfigItemMapEntry createSingleton(final ConfigItem configItem) {
		return new ConfigItemMapEntrySingleton(configItem);
	}

	ConfigItemMapEntrySingleton(final ConfigBlock block) {
		this(null, block);
	}

	ConfigItemMapEntrySingleton(final String name, final ConfigBlock block) {
		this.name = name;
		this.block = block;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean add(final ConfigBlock block) {
		throw new UnsupportedOperationException();
	}

	public boolean contains(final String name) {
		return block.getName().equals(name);
	}

	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-entry");
		if (this.name != null)
			util.appendAttribute(appendable, "config:name", this.name);
		appendable.append(">");
		this.block.appendXML(util, appendable);
		appendable.append("</config:config-item-map-entry>");
	}

	@Override
	public Iterator<ConfigBlock> iterator() {
		return Arrays.asList(this.block).iterator();
	}

	@Override
	public boolean add(final String name, final String type, final String value) {
		throw new UnsupportedOperationException();
	}

	public ConfigBlock getByName(final String name) {
		if (block.getName().equals(name))
			return block;
		else
			return null;
	}

	public String set(final String name, final String value) {
		final ConfigBlock block = this.getByName(name);
		if (block instanceof ConfigItem) {
			final ConfigItem item = (ConfigItem) block;
			final String previousValue = item.getValue();
			item.setValue(value);
			return previousValue;
		}
		return null;
	}

	public String set(final String value) {
		if (block instanceof ConfigItem) {
			final ConfigItem item = (ConfigItem) block;
			final String previousValue = item.getValue();
			item.setValue(value);
			return previousValue;
		}
		return null;
	}
}