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
import java.util.Map;

import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.util.XMLUtil;

/**
 *
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file TableFamilyStyle.java is part of FastODS.
 *
 *         SimpleODS 0.5.1 Changed all 'throw Exception' to 'throw
 *         FastOdsException' SimpleODS 0.5.2 Replaced all text properties with a
 *         TextStyle object
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:automatic-styles/style:
 *         style
 */
public class TableCellStyle implements StyleTag {
	public static enum Align {
		CENTER("center"), JUSTIFY("justify"), LEFT("start"), RIGHT("end");

		private final String attrValue;

		private Align(final String attrValue) {
			this.attrValue = attrValue;
		}
	}

	public static enum VerticalAlign {
		BOTTOM("bottom"), MIDDLE("middle"), TOP("top");

		private final String attrValue;

		private VerticalAlign(final String attrValue) {
			this.attrValue = attrValue;
		}
	}

	private static TableCellStyle defaultCellStyle;

	public static TableCellStyleBuilder builder(final XMLUtil util,
			final String name) {
		return new TableCellStyleBuilder(util, name);
	}

	public static TableCellStyle getDefaultCellStyle(final XMLUtil util) {
		if (TableCellStyle.defaultCellStyle == null)
			TableCellStyle.defaultCellStyle = TableCellStyle
					.builder(util, "Default").build();

		return TableCellStyle.defaultCellStyle;
	}

	private final Map<BorderAttribute.Position, BorderAttribute> borderByPosition;
	private final boolean bWrap; // No line wrap when false, line wrap when
	private DataStyle dataStyle;
	private final String name;
	private final Align nTextAlign; // 'center','end','start','justify'
	private final VerticalAlign nVerticalAlign; // 'middle', 'bottom', 'top'
	private final String sBackgroundColor;

	// true
	private final String sDefaultCellStyle;

	private final FHTextStyle textStyle;

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
	TableCellStyle(final XMLUtil util, final String name,
			final DataStyle dataStyle, final String sBackgroundColor,
			final FHTextStyle ts, final Align nTextAlign,
			final VerticalAlign nVerticalAlign, final boolean bWrap,
			final String sDefaultCellStyle,
			final Map<BorderAttribute.Position, BorderAttribute> borderByPosition) {
		this.name = util.escapeXMLAttribute(name);
		this.dataStyle = dataStyle;
		this.sBackgroundColor = sBackgroundColor;
		this.textStyle = ts;
		this.nTextAlign = nTextAlign;
		this.nVerticalAlign = nVerticalAlign;
		this.bWrap = bWrap;
		this.sDefaultCellStyle = sDefaultCellStyle;
		this.borderByPosition = borderByPosition;
	}

	public void addToFile(final OdsFile odsFile) {
		if (this.dataStyle != null)
			this.dataStyle.addToFile(odsFile);
		odsFile.addStyleTag(this);
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @return The XML string for this object.
	 * @throws IOException
	 */
	@Override
	public void appendXMLToContentEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:style");
		util.appendEAttribute(appendable, "style:name", this.name);
		util.appendEAttribute(appendable, "style:family", "table-cell");
		util.appendEAttribute(appendable, "style:parent-style-name", "Default");
		if (this.dataStyle != null)
			util.appendAttribute(appendable, "style:data-style-name",
					this.dataStyle.getName());

		appendable.append("><style:table-cell-properties");
		util.appendAttribute(appendable, "fo:background-color",
				this.sBackgroundColor);

		util.appendEAttribute(appendable, "style:vertical-align",
				this.nVerticalAlign.attrValue);

		// -----------------------------------------------
		// Add all border styles
		// -----------------------------------------------
		for (final BorderAttribute bs : this.borderByPosition.values()) {
			bs.appendXMLToTableCellStyle(util, appendable);
		}

		if (this.bWrap)
			util.appendEAttribute(appendable, "fo:wrap-option", "wrap");

		appendable.append("/>");
		// ----------------------------------------------------
		// First check if any text properties should be added
		// ----------------------------------------------------
		if (this.textStyle != null && this.textStyle.isNotEmpty()) {
			this.textStyle.appendXMLToContentEntry(util, appendable);
		}

		appendable.append("<style:paragraph-properties");
		util.appendEAttribute(appendable, "fo:text-align",
				this.nTextAlign.attrValue);
		util.appendEAttribute(appendable, "fo:margin-left", "0cm");
		appendable.append("/></style:style>");
	}

	public DataStyle getDataStyle() {
		return this.dataStyle;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setDataStyle(final DataStyle dataStyle) {
		this.dataStyle = dataStyle;
	}
}
