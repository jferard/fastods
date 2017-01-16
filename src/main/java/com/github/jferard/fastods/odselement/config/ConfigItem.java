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

/**
 * 3.10.3 config:config-item
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public class ConfigItem implements ConfigBlock {
	private final String name;
	private final String type;
	private String value;

	public ConfigItem(final String name, final String type,
			final String value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	/**
	 * Write the XML format for this object. This is used while writing the ODS file.
	 *
	 * @throws IOException
	 */
	@Override
	public void appendXML(final XMLUtil util,
						  final Appendable appendable) throws IOException {
		appendable.append("<config:config-item");
		util.appendAttribute(appendable, "config:name", this.name);
		util.appendAttribute(appendable, "config:type", this.type);
		appendable.append(">");
		appendable.append(util.escapeXMLContent(this.value));
		appendable.append("</config:config-item>");
	}

	/**
	 * Get the name of this ConfigItem.
	 *
	 * @return The name of this ConfigItem
	 */
	@Override
	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}
}