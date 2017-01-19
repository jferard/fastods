/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * WHERE ? content.xml/office:document-content/office:automatic-styles/style:
 * style content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-column
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public class TableColumnStyle implements StyleTag {
	private static TableColumnStyle defaultColumnStyle;

	public static TableColumnStyleBuilder builder(final String name) {
		return new TableColumnStyleBuilder(name);
	}

	public static TableColumnStyle getDefaultColumnStyle(
			final XMLUtil xmlUtil) {
		if (TableColumnStyle.defaultColumnStyle == null)
			TableColumnStyle.defaultColumnStyle = TableColumnStyle
					.builder("co1").build();

		return TableColumnStyle.defaultColumnStyle;
	}

	private final String columnWidth;
	private final TableCellStyle defaultCellStyle;
	private final String name;

	/**
	 * Create a new column style
	 *
	 * @param name
	 *            A unique name for this style
	 * @param columnWidth
	 *            the width of the column
	 * @param defaultCellStyle
	 *            the default style for cells
	 */
	TableColumnStyle(final String name, final String columnWidth,
			final TableCellStyle defaultCellStyle) {
		this.name = name;
		this.columnWidth = columnWidth;
		this.defaultCellStyle = defaultCellStyle;
	}

	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addStyleTag(this);
	}

	/**
	 * OpenDocument 17.16 style:table-column-properties
	 */
	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable)
			throws IOException {
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
	public String getFamily() {
		return "table-column";
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
	
	private String key;
	@Override
	public String getKey() {
		if (this.key == null)
			this.key = this.getFamily()+"@"+this.getName();
		return this.key;
	}
}
