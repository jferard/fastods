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
 * /**
 *
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file TableFamilyStyle.java is part of FastODS. SimpleODS 0.5.1
 *         Changed all 'throw Exception' to 'throw FastOdsException' SimpleODS
 *         0.5.2 Replaced all text properties with a TextStyle object
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:automatic-styles/style:
 *         style
 *         content.xml/office:document-content/office:body/office:spreadsheet/
 *         table:table/table:table-column
 */
public class TableColumnStyle implements StyleTag {
	public static final TableColumnStyle DEFAULT_TABLE_COLUMN_STYLE = TableColumnStyle
			.builder("co1").build();

	public static TableColumnStyleBuilder builder(final String sName) {
		return new TableColumnStyleBuilder(sName);
	}

	private final String sColumnWidth;
	private final String sName;
	private final TableCellStyle defaultCellStyle;

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
	 * @param defaultCellStyle 
	 * @param odsFile
	 *            The OdsFile to add this style to
	 */
	TableColumnStyle(final String sStyleName, final String sColumnWidth, TableCellStyle defaultCellStyle) {
		this.sName = sStyleName;
		this.sColumnWidth = sColumnWidth;
		this.defaultCellStyle = defaultCellStyle;
	}

	public void addToFile(final OdsFile odsFile) {
		odsFile.getContent().addStyleTag(this);
	}

	/**
	 * 17.16 <style:table-column-properties>
	 */
	@Override
	public void appendXMLToContentEntry(final Util util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:style");
		util.appendAttribute(appendable, "style:name", this.sName);
		util.appendAttribute(appendable, "style:family", "table-column");
		appendable.append("><style:table-column-properties");
		util.appendAttribute(appendable, "fo:break-before", "auto");
		util.appendAttribute(appendable, "style:column-width",
				this.sColumnWidth);
		appendable.append("/></style:style>");
	}

	public void appendXMLToTable(final Util util, final Appendable appendable, final int nCount)
			throws IOException {
		appendable.append("<table:table-column");
		util.appendAttribute(appendable, "table:style-name",
				this.sName);
		if (nCount > 1)
			util.appendAttribute(appendable, "table:number-columns-repeated",
					nCount);
		if (this.defaultCellStyle != null)
			util.appendAttribute(appendable, "table:default-cell-style-name",
					this.defaultCellStyle.getName());
		appendable.append("/>");
	}

	public String getColumnWidth() {
		return this.sColumnWidth;
	}

	public String getDefaultCellStyleName() {
		return this.defaultCellStyle.getName();
	}

	@Override
	public String getName() {
		return this.sName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.sName == null) ? 0 : this.sName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj instanceof TableColumnStyle) {
			TableColumnStyle other = (TableColumnStyle) obj;
			return this.sName.equals(other.sName);
		} else
			return false;
	}

}
