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
package com.github.jferard.fastods.style;

import java.io.IOException;

import com.github.jferard.fastods.entry.OdsEntries;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * WHERE ? content.xml/office:document-content/office:automatic-styles/style:
 * style content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-column
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public class TableRowStyle implements StyleTag {
	public static final TableRowStyle DEFAULT_TABLE_ROW_STYLE = TableRowStyle
			.builder("ro1").build();

	public static TableRowStyleBuilder builder(final String name) {
		return new TableRowStyleBuilder(name);
	}

	private final String name;
	private final String rowHeight;

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
	 * @param odsFile
	 *            The OdsFile to add this style to
	 */
	TableRowStyle(final String styleName, final String rowHeight) {
		this.name = styleName;
		this.rowHeight = rowHeight;
	}

	public void addToEntries(final OdsEntries odsEntries) {
		odsEntries.addStyleTag(this);
	}

	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<style:style");
		util.appendAttribute(appendable, "style:name", this.name);
		util.appendAttribute(appendable, "style:family", "table-row");
		appendable.append("><style:table-row-properties");
		if (this.rowHeight != null)
			util.appendAttribute(appendable, "style:row-height",
					this.rowHeight);
		util.appendAttribute(appendable, "fo:break-before", "auto");
		util.appendAttribute(appendable, "style:use-optimal-row-height",
				"true");
		appendable.append("/></style:style>");
	}

	@Override
	public String getFamily() {
		return "table-row";
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Set the row height to a table row.<br>
	 * height is a length value expressed as a number followed by a unit of
	 * measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @return height The table row height to be used, e.g. '1.0cm'
	 */
	public String getRowHeight() {
		return this.rowHeight;
	}
}
