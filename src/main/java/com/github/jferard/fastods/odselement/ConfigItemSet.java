/* *****************************************************************************
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
 * ****************************************************************************/
package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 3.10.2 config:config-item-set
 */
public class ConfigItemSet implements ConfigBlock {
	private final Set<ConfigBlock> set;
	private final String name;

	ConfigItemSet(String name) {
		this.name = name;
		this.set = new HashSet<ConfigBlock>();
	}

	/**
	 * @return The name of this ConfigSet
	 */
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void appendXML(XMLUtil util, Appendable appendable) throws IOException {
		appendable.append("<config:config-item-set");
		util.appendAttribute(appendable, "config:name", this.name);
		appendable.append(">");
		for (ConfigBlock block : this.set)
			block.appendXML(util, appendable);
		appendable.append("</config:config-item-set>");
	}

	public int size() {
		return set.size();
	}

	public boolean isEmpty() {
		return set.isEmpty();
	}

	public Iterator<ConfigBlock> iterator() {
		return set.iterator();
	}

	public boolean add(ConfigBlock configBlock) {
		return set.add(configBlock);
	}

	public boolean remove(Object o) {
		return set.remove(o);
	}
}
