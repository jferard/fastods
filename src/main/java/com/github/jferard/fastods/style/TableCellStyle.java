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

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.entry.ContentEntry;
import com.github.jferard.fastods.entry.OdsEntries;
import com.github.jferard.fastods.entry.StylesEntry;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * WHERE ? content.xml/office:document-content/office:automatic-styles/style:
 * style
 *
 * @author Julien Férard
 * @author Martin Schulz
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

	public static TableCellStyleBuilder builder(final String name) {
		return new TableCellStyleBuilder(name);
	}

	public static TableCellStyle getDefaultCellStyle() {
		if (TableCellStyle.defaultCellStyle == null)
			TableCellStyle.defaultCellStyle = TableCellStyle.builder("Default")
					.textAlign(TableCellStyle.Align.LEFT)
					.verticalAlign(TableCellStyle.VerticalAlign.TOP)
					.fontWrap(false).backgroundColor(Color.WHITE)
					.allMargins("0cm").parentCellStyle(null).build();

		return TableCellStyle.defaultCellStyle;
	}

	private final String backgroundColor;
	private final Borders borders;
	private DataStyle dataStyle;
	private final Margins margins;
	private final String name;
	// true
	private final String parentCellStyleName;
	private final Align textAlign; // 'center','end','start','justify'

	private final TextStyle textStyle;

	private final VerticalAlign verticalAlign; // 'middle', 'bottom', 'top'
	private final boolean wrap; // No line wrap when false, line wrap when

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
	TableCellStyle(final String name, final DataStyle dataStyle,
			final String backgroundColor, final TextStyle ts,
			final Align textAlign, final VerticalAlign verticalAlign,
			final boolean wrap, final String parentCellStyleName,
			final Borders borders, final Margins margins) {
		this.borders = borders;
		this.margins = margins;
		this.name = name;
		this.dataStyle = dataStyle;
		this.backgroundColor = backgroundColor;
		this.textStyle = ts;
		this.textAlign = textAlign;
		this.verticalAlign = verticalAlign;
		this.wrap = wrap;
		this.parentCellStyleName = parentCellStyleName;
	}

	public void addToStyles(final StylesEntry stylesEntry) {
		if (this.dataStyle != null)
			this.dataStyle.addToStyles(stylesEntry);
		stylesEntry.addStyleTag(this);
	}

	public void addToEntries(final OdsEntries odsEntries) {
		if (this.dataStyle != null)
			this.dataStyle.addToEntries(odsEntries);
		odsEntries.addStyleTag(this);
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @return The XML string for this object.
	 * @throws IOException
	 */
	@Override
	public void appendXMLToStylesEntry(final XMLUtil util,
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

		this.borders.appendXMLToTableCellStyle(util, appendable);

		if (this.wrap)
			util.appendEAttribute(appendable, "fo:wrap-option", "wrap");

		appendable.append("/>");

		if (this.textStyle != null && this.textStyle.isNotEmpty()) {
			this.textStyle.appendAnonymousXMLToContentEntry(util, appendable);
		}

		appendable.append("<style:paragraph-properties");
		if (this.textAlign != null)
			util.appendEAttribute(appendable, "fo:text-align",
					this.textAlign.attrValue);

		this.margins.appendXMLToTableCellStyle(util, appendable);
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

	@Override
	public String getFamily() {
		return "table-cell";
	}
}
