/*
*	SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
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
package org.simpleods;

/**
 * @author Martin Schulz<br>
 * 
 * Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net><br>
 * 
 * This file ConfigItem.java is part of SimpleODS.
 *
 */
public class ConfigItem {
	private String sItemName;
	private String sType;
	private String sValue;

	public ConfigItem(final String sName, final String sType, final String sValue) {

		this.sItemName = sName;
		this.sType = sType;
		this.sValue = sValue;

	}
	
	/**
	 * Get the name of this ConfigItem.
	 * @return The name of this ConfigItem
	 */
	public String getName() {
		return sItemName;
	}

	public void setName(final String name) {
		sItemName = name;
	}

	public String getType() {
		return sType;
	}

	public void setType(final String type) {
		sType = type;
	}

	public String getValue() {
		return sValue;
	}

	public void setValue(final String value) {
		sValue = value;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	public String toXML() {
		return ("<config:config-item config:name=\"" + sItemName + "\" config:type=\"" + sType + "\">" + sValue + "</config:config-item>");
	}

}
