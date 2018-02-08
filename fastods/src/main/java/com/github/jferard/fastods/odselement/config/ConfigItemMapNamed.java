/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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
 * A map with elements mapped by names
 * 3.10.4 config:config-item-map-named
 * @author Julien Férard
 */
public class ConfigItemMapNamed implements ConfigItemCollection<ConfigItemMapEntry> {
	private final String name;
	private final Map<String, ConfigItemMapEntry> map;

	/**
	 * @param name the name of this item
	 */
	public ConfigItemMapNamed(final String name) {
		this.name = name;
		this.map = new HashMap<String, ConfigItemMapEntry>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int size() {
		return this.map.size();
	}

	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	/**
	 * @param name the name to look for
	 * @return true iff an element of this item is mapped to the name
	 */
	public boolean contains(final String name) {
		return this.map.containsKey(name);
	}

	/**
	 * @param name the name to look for
	 * @return the element mapped to this name, null if none
	 */
	public ConfigItemMapEntry getByName(final String name) {
		return this.map.get(name);
	}

	/**
	 * @param value the value to add. It's name will be the key
	 * @return the previous value with this name
	 */
	public ConfigItemMapEntry put(final ConfigItemMapEntry value) {
		return this.map.put(value.getName(), value);
	}

	/**
	 * @param name the name to look for
	 * @return the previous value, or null if none.
	 */
	public ConfigItemMapEntry removeByName(final String name) {
		return this.map.remove(name);
	}

	@Override
	public Iterator<ConfigItemMapEntry> iterator() {
		return this.map.values().iterator();
	}

	@Override
	public void appendXMLRepresentation(final XMLUtil util, final Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-named");
		util.appendEAttribute(appendable, "config:name", this.name);
		appendable.append(">");
		for (final ConfigItemMapEntry entry : this.map.values())
			entry.appendXMLRepresentation(util, appendable);
		appendable.append("</config:config-item-map-named>");
	}

	/**
	 * Clears this item.
	 */
	public void clear() {
		this.map.clear();
	}
}
