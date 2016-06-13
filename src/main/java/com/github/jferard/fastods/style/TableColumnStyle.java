/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
package com.github.jferard.fastods.style;

import java.io.IOException;

import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.XMLUtil;

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
	private static TableColumnStyle defaultColumnStyle;

	public static TableColumnStyleBuilder builder(final XMLUtil xmlUtil,
			final String name) {
		return new TableColumnStyleBuilder(xmlUtil, name);
	}

	public static TableColumnStyle getDefaultColumnStyle(
			final XMLUtil xmlUtil) {
		if (TableColumnStyle.defaultColumnStyle == null)
			TableColumnStyle.defaultColumnStyle = TableColumnStyle
					.builder(xmlUtil, "co1").build();

		return TableColumnStyle.defaultColumnStyle;
	}

	private final TableCellStyle defaultCellStyle;
	private final String name;
	private final String columnWidth;

	/**
	 * Create a new table style and add it to contentEntry.<br>
	 * Version 0.5.0 Added parameter OdsFile o
	 *
	 * @param family
	 *            The type of this style, either
	 *            STYLE_TABLECOLUMN,STYLE_TABLEROW,STYLE_TABLE or
	 *            STYLE_TABLECELL
	 * @param styleName
	 *            A unique name for this style
	 * @param defaultCellStyle
	 * @param odsFile
	 *            The OdsFile to add this style to
	 */
	TableColumnStyle(final String styleName, final String columnWidth,
			final TableCellStyle defaultCellStyle) {
		this.name = styleName;
		this.columnWidth = columnWidth;
		this.defaultCellStyle = defaultCellStyle;
	}

	public void addToFile(final OdsFile odsFile) {
		odsFile.addStyleTag(this);
	}

	/**
	 * 17.16 <style:table-column-properties>
	 */
	@Override
	public void appendXMLToContentEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:style");
		util.appendAttribute(appendable, "style:name", this.name);
		util.appendAttribute(appendable, "style:family", "table-column");
		appendable.append("><style:table-column-properties");
		util.appendAttribute(appendable, "fo:break-before", "auto");
		util.appendAttribute(appendable, "style:column-width",
				this.columnWidth);
		appendable.append("/></style:style>");
	}

	public void appendXMLToTable(final XMLUtil util,
			final Appendable appendable, final int count) throws IOException {
		appendable.append("<table:table-column");
		util.appendAttribute(appendable, "table:style-name", this.name);
		if (count > 1)
			util.appendEAttribute(appendable, "table:number-columns-repeated",
					count);
		if (this.defaultCellStyle != null)
			util.appendAttribute(appendable, "table:default-cell-style-name",
					this.defaultCellStyle.getName());
		appendable.append("/>");
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;

		if (obj instanceof TableColumnStyle) {
			final TableColumnStyle other = (TableColumnStyle) obj;
			return this.name.equals(other.name);
		} else
			return false;
	}

	public String getColumnWidth() {
		return this.columnWidth;
	}

	public String getDefaultCellStyleName() {
		return this.defaultCellStyle.getName();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (this.name == null ? 0 : this.name.hashCode());
		return result;
	}

}
