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

/**
 * 
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file TableFamilyStyle.java is part of FastODS. SimpleODS 0.5.1
 *         Changed all 'throw Exception' to 'throw SimpleOdsException' SimpleODS
 *         0.5.2 Replaced all text properties with a TextStyle object
 * 
 *         content.xml/office:document-content/office:automatic-styles
 */
public class TableStyle implements NamedObject, XMLAppendable {
	public static TableStyleBuilder builder() {
		return new TableStyleBuilder();
	}

	private final String sName;

	/**
	 * Create a new table style and add it to contentEntry.<br>
	 * Version 0.5.0 Added parameter OdsFile o
	 * 
	 * @param nFamily
	 *            The type of this style, either
	 *            STYLE_TABLECOLUMN,STYLE_TABLEROW,STYLE_TABLE or
	 *            STYLE_TABLECELL
	 * @param sStyleName
	 *            A unique name for this style
	 * @param odsFile
	 *            The OdsFile to add this style to
	 */
	TableStyle(String sStyleName) {
		this.sName = sStyleName;
	}

	public void addToFile(final OdsFile odsFile) {
		odsFile.getContent().addTableStyle(this);
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	public String toXML(Util util) {
		try {
			StringBuilder sbTemp = new StringBuilder();
			this.appendXML(util, sbTemp);
			return sbTemp.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getName() {
		return this.sName;
	}

	public void appendXML(Util util, Appendable appendable) throws IOException {
		appendable.append("<style:style");
		util.appendAttribute(appendable, "style:name", this.sName);
		util.appendAttribute(appendable, "style:family", "table");
		util.appendAttribute(appendable, "style:master-page-name",
				"DefaultMasterPage");
		appendable.append("><style:table-properties");
		util.appendAttribute(appendable, "table:display", "true");
		util.appendAttribute(appendable, "style:writing-mode", "lr-tb");
		appendable.append("/></style:style>");
	}
}
