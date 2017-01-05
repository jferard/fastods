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

/**
 * 3.10.5 config:config-item-map-entry
 */
public class ConfigItemMapEntry {
	private final String name;
	private ConfigBlock block;

	public ConfigItemMapEntry(String name, final ConfigBlock block) {
		this.name = name;
		this.block = block;
	}

	public String getName() {
		return this.name;
	}

	public ConfigBlock getBlock() {
		return block;
	}

	public void appendXML(XMLUtil util, Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-entry");
		util.appendAttribute(appendable, "config:name", this.name);
		appendable.append(">");
		this.block.appendXML(util, appendable);
		appendable.append("</config:config-item-map-entry>");
	}
}
