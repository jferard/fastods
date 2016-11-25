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
package com.github.jferard.fastods.style;

import java.io.IOException;

import com.github.jferard.fastods.entry.OdsEntries;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * content.xml/office:document-content/office:automatic-styles
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public class TableStyle implements StyleTag {
	public static final TableStyle DEFAULT_TABLE_STYLE = TableStyle
			.builder("ta1").build();

	public static TableStyleBuilder builder(final String name) {
		return new TableStyleBuilder(name);
	}

	private final String name;
	private final PageStyle pageStyle;

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
	TableStyle(final String styleName, final PageStyle pageStyle) {
		this.name = styleName;
		this.pageStyle = pageStyle;
	}

	public void addToEntries(final OdsEntries odsEntries) {
		odsEntries.addStyleTag(this);
	}

	@Override
	public void appendXMLToStylesEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:style");
		util.appendAttribute(appendable, "style:name", this.name);
		util.appendAttribute(appendable, "style:family", "table");
		if (this.pageStyle != null)
			util.appendEAttribute(appendable, "style:master-page-name",
					this.pageStyle.getName());
		appendable.append("><style:table-properties");
		util.appendAttribute(appendable, "table:display", "true");
		util.appendAttribute(appendable, "style:writing-mode", "lr-tb");
		appendable.append("/></style:style>");
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getFamily() {
		return "table";
	}
}
