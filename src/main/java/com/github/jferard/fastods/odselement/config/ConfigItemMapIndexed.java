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
 * 3.10.4 config:config-item-map-indexed
 */
public class ConfigItemMapIndexed implements ConfigBlock {
	private final String name;

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public boolean contains(Object o) {
		return list.contains(o);
	}

	public Iterator<ConfigItemMapEntry> iterator() {
		return list.iterator();
	}

	public boolean add(ConfigItemMapEntry configItemMapEntry) {
		return list.add(configItemMapEntry);
	}

	public void remove(int index) {
		list.remove(index);
	}

	public ConfigItemMapEntry get(int index) {
		return list.get(index);
	}

	public ConfigItemMapEntry set(int index, ConfigItemMapEntry element) {
		return list.set(index, element);
	}

	public void add(int index, ConfigItemMapEntry element) {
		list.add(index, element);
	}

	private final List<ConfigItemMapEntry> list;

	public ConfigItemMapIndexed(String name) {
		this.name = name;
		this.list = new ArrayList<ConfigItemMapEntry>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void appendXML(XMLUtil util, Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-indexed");
		util.appendAttribute(appendable, "config:name", this.name);
		appendable.append(">");
		for (ConfigItemMapEntry entry : this.list)
			entry.appendXML(util, appendable);
		appendable.append("</config:config-item-map-indexed>");
	}
}
