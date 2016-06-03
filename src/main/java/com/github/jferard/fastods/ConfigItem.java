/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods;

import java.io.IOException;

import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file ConfigItem.java is part of FastODS.
 *
 *         WHERE ?
 *         settings.xml/office:document-settings/office:settings/settingsEntry:
 *         settingsEntry-item-set/config:config-item
 */
public class ConfigItem {
	private final String sItemName;
	private final String sType;
	private final String sValue;

	public ConfigItem(final String sName, final String sType,
			final String sValue) {
		this.sItemName = sName;
		this.sType = sType;
		this.sValue = sValue;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @return The XML string for this object.
	 * @throws IOException
	 */
	public void appendXMLToObject(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<config:config-item");
		util.appendAttribute(appendable, "config:name", this.sItemName);
		util.appendAttribute(appendable, "config:type", this.sType);
		appendable.append(">");
		appendable.append(util.escapeXMLContent(this.sValue));
		appendable.append("</config:config-item>");
	}

	/**
	 * Get the name of this ConfigItem.
	 *
	 * @return The name of this ConfigItem
	 */
	public String getName() {
		return this.sItemName;
	}

	public String getType() {
		return this.sType;
	}

	public String getValue() {
		return this.sValue;
	}
}
