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

import java.util.ListIterator;

/**
 * /**
 * 
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file TableFamilyStyle.java is part of FastODS. SimpleODS 0.5.1
 *         Changed all 'throw Exception' to 'throw SimpleOdsException' SimpleODS
 *         0.5.2 Replaced all text properties with a TextStyle object
 *
 * WHERE ?
 * content.xml/office:document-content/office:automatic-styles/style:style
 * content.xml/office:document-content/office:body/office:spreadsheet/table:table/table:table-column
 */
public class TableRowStyle implements NamedObject {
	public static TableRowStyleBuilder builder() {
		return new TableRowStyleBuilder();
	}
	
	private String sRowHeight;
	private String sName;

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
	TableRowStyle(String sStyleName, String sRowHeight) {
		this.sName = sStyleName;
		this.sRowHeight = sRowHeight;
	}

	/**
	 * Set the row height to a table row.<br>
	 * sHeight is a length value expressed as a number followed by a unit of
	 * measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 * 
	 * @return sHeight
	 *            The table row height to be used, e.g. '1.0cm'
	 */
	public String getRowHeight() {
		return this.sRowHeight;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	public String toXML(Util util) {
		StringBuilder sbTemp = new StringBuilder();
		sbTemp.append("<style:style");
		util.appendAttribute(sbTemp, "style:name", this.sName);
		util.appendAttribute(sbTemp, "style:family", "table-row");
		sbTemp.append("><style:table-row-properties");
		util.appendAttribute(sbTemp, "row-height", this.sRowHeight);
		util.appendAttribute(sbTemp, "fo:break-before", "auto");
		util.appendAttribute(sbTemp, "style:use-optimal-row-height", "true");
		sbTemp.append("/></style:style>");

		return sbTemp.toString();
	}

	public String getName() {
		return this.sName;
	}

	public void addToFile(final OdsFile odsFile) {
		odsFile.getContent().addTableStyle(this);
	}
}
