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
import java.util.EnumMap;
import java.util.Map;

import com.github.jferard.fastods.Color;
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
					.builder(util, "Default")
					.textAlign(TableCellStyle.Align.LEFT)
					.verticalAlign(TableCellStyle.VerticalAlign.TOP)
					.fontWrap(false).backgroundColor(Color.WHITE)
					.addMargin("0cm", MarginAttribute.Position.LEFT)
					.parentCellStyle(null).build();

		return TableCellStyle.defaultCellStyle;
	}

	private final String backgroundColor;
	private final Map<BorderAttribute.Position, BorderAttribute> borderByPosition;
	private DataStyle dataStyle;
	private final Map<MarginAttribute.Position, MarginAttribute> marginByPosition;
	private final String name;
	// true
	private final String parentCellStyleName;
	private final Align textAlign; // 'center','end','start','justify'
	private final FHTextStyle textStyle;

	private final VerticalAlign verticalAlign; // 'middle', 'bottom', 'top'

	private final boolean wrap; // No line wrap when false, line wrap when

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
			final String parentCellStyleName,
			final Map<BorderAttribute.Position, BorderAttribute> borderByPosition,
			final EnumMap<MarginAttribute.Position, MarginAttribute> marginByPosition) {
		this.marginByPosition = marginByPosition;
		this.name = util.escapeXMLAttribute(name);
		this.dataStyle = dataStyle;
		this.backgroundColor = sBackgroundColor;
		this.textStyle = ts;
		this.textAlign = nTextAlign;
		this.verticalAlign = nVerticalAlign;
		this.wrap = bWrap;
		this.parentCellStyleName = parentCellStyleName;
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
		if (this.parentCellStyleName != null)
			util.appendEAttribute(appendable, "style:parent-style-name",
					this.parentCellStyleName);
		if (this.dataStyle != null)
			util.appendAttribute(appendable, "style:data-style-name",
					this.dataStyle.getName());

		appendable.append("><style:table-cell-properties");
		if (this.backgroundColor != null)
			util.appendAttribute(appendable, "fo:background-color",
					this.backgroundColor);

		if (this.verticalAlign != null)
			util.appendEAttribute(appendable, "style:vertical-align",
					this.verticalAlign.attrValue);

		// -----------------------------------------------
		// Add all border styles
		// -----------------------------------------------
		for (final BorderAttribute bs : this.borderByPosition.values()) {
			bs.appendXMLToTableCellStyle(util, appendable);
		}

		if (this.wrap)
			util.appendEAttribute(appendable, "fo:wrap-option", "wrap");

		appendable.append("/>");
		// ----------------------------------------------------
		// First check if any text properties should be added
		// ----------------------------------------------------
		if (this.textStyle != null && this.textStyle.isNotEmpty()) {
			this.textStyle.appendXMLToContentEntry(util, appendable);
		}

		appendable.append("<style:paragraph-properties");
		if (this.textAlign != null)
			util.appendEAttribute(appendable, "fo:text-align",
					this.textAlign.attrValue);

		for (final MarginAttribute ms : this.marginByPosition.values()) {
			ms.appendXMLToTableCellStyle(util, appendable);
		}

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
