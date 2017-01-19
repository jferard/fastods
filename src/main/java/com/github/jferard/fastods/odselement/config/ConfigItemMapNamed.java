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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 3.10.4 config:config-item-map-indexed
 */
public class ConfigItemMapNamed implements ConfigBlock {
	private final String name;
	private final Map<String, ConfigItemMapEntry> map;

	public ConfigItemMapNamed(final String name) {
		this.name = name;
		this.map = new HashMap<String, ConfigItemMapEntry>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean contains(final String name) {
		return map.containsKey(name);
	}

	public ConfigItemMapEntry getByName(final String name) {
		return map.get(name);
	}

	public ConfigItemMapEntry put(final ConfigItemMapEntry value) {
		return map.put(value.getName(), value);
	}

	public ConfigItemMapEntry removeByName(final String name) {
		return map.remove(name);
	}

	public Iterator<ConfigItemMapEntry> iterator() {
		return map.values().iterator();
	}

	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-named");
		util.appendAttribute(appendable, "config:name", this.name);
		appendable.append(">");
		for (final ConfigItemMapEntry entry : this.map.values())
			entry.appendXML(util, appendable);
		appendable.append("</config:config-item-map-named>");
	}

	public void clear() {
		this.map.clear();
	}
}
