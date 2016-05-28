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

import java.util.EnumMap;
import java.util.Map;

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
 */
class TableCellStyleBuilder {
	private final Map<BorderAttribute.Position, BorderAttribute> borderByPosition;
	// 'top'
	private boolean bWrap; // No line wrap when false, line wrap when
	private TableCellStyle.Align nTextAlign; // 'center','end','start','justify'
	private TableCellStyle.VerticalAlign nVerticalAlign; // 'middle', 'bottom',
	private String sBackgroundColor;
	private DataStyle dataStyle;
	// true
	private final String sDefaultCellStyle;
	private final String sName;
	private final TextStyleBuilder tsBuilder;
	
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
	public TableCellStyleBuilder(final String sName) {
		if (sName == null)
			throw new IllegalArgumentException();
		
		this.sName = sName;
		this.nTextAlign = TableCellStyle.Align.LEFT;
		this.nVerticalAlign = TableCellStyle.VerticalAlign.TOP;
		this.bWrap = false;
		this.sBackgroundColor = "#FFFFFF";

		this.tsBuilder = new TextStyleBuilder(sName);
		this.sDefaultCellStyle = "Default";
		this.borderByPosition = new EnumMap<BorderAttribute.Position, BorderAttribute>(
				BorderAttribute.Position.class);
	}

	/**
	 * Add a border style to this cell.
	 *
	 * @param bs
	 *            - The border style to be used
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder addBorderStyle(final BorderAttribute bs) {
		this.borderByPosition.put(bs.getPosition(), bs);
		return this;
	}

	/**
	 * Add a border style to this cell.
	 *
	 * @param sSize
	 *            The size of the line e.g. '0.1cm'
	 * @param sBorderColor
	 *            - The color to be used in format #rrggbb e.g. '#ff0000' for a
	 *            red border
	 * @param nStyle
	 *            - The style of the border line, either
	 *            BorderAttribute.BORDER_SOLID or BorderAttribute.BORDER_DOUBLE
	 * @param nPosition
	 *            - The position of the line in this cell, e.g.
	 *            BorderAttribute.POSITION_TOP
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder addBorderStyle(final String sSize,
			final String sBorderColor, final BorderAttribute.Style nStyle,
			final BorderAttribute.Position nPosition) {
		final BorderAttribute bs = new BorderAttribute(sSize, sBorderColor,
				nStyle, nPosition);
		this.addBorderStyle(bs);
		return this;
	}

	/**
	 * Set the cell background color to sColor.<br>
	 * The TableFamilyStyle must be of a format of
	 * TableFamilyStyle.STYLE_TABLECELL
	 *
	 * @param sColor
	 *            - The color to be used in format #rrggbb e.g. #ff0000 for a
	 *            red cell background
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder backgroundColor(final String sColor) {
		this.sBackgroundColor = sColor;
		return this;
	}

	public TableCellStyle build() {
		if (this.sName == null)
			throw new IllegalStateException();

		return new TableCellStyle(this.sName, this.dataStyle,
				this.sBackgroundColor, this.tsBuilder.build(), this.nTextAlign,
				this.nVerticalAlign, this.bWrap, this.sDefaultCellStyle,
				this.borderByPosition);

	}

	/**
	 * Set the data style for this TableFamilyStyle to cs.<br>
	 * If the StyleType of this TableFamilyStyle is not STYLE_TABLECELL, an
	 * exception is thrown
	 *
	 * @param cs
	 *            The currency style to be used
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder dataStyle(final CurrencyStyle cs) {
		this.dataStyle = cs;
		return this;
	}

	/**
	 * Set the data style for this TableFamilyStyle to ds.<br>
	 * If the StyleType of this TableFamilyStyle is not STYLE_TABLECELL, an
	 * exception is thrown
	 *
	 * @param ds
	 *            The date style to be used
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder dataStyle(final DateStyle ds) {
		this.dataStyle = ds;
		return this;
	}

	/**
	 * Set the data style for this TableFamilyStyle to ns.<br>
	 * If the StyleType of this TableFamilyStyle is not STYLE_TABLECELL, an
	 * exception is thrown
	 *
	 * @param ns
	 *            The number style to be used
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder dataStyle(final NumberStyle ns) {
		this.dataStyle = ns;
		return this;
	}

	/**
	 * Set the font color to sColor.<br>
	 * The TableFamilyStyle must be of a format of
	 * TableFamilyStyle.STYLE_TABLECELL
	 *
	 * @param sColor
	 *            The color to be used in format #rrggbb e.g. #ff0000 for a red
	 *            cell background
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder fontColor(final String sColor) {
		this.tsBuilder.fontColor(sColor);
		return this;
	}

	/**
	 * Set the font weight to bold.<br>
	 * The TableFamilyStyle must be of a format of
	 * TableFamilyStyle.STYLE_TABLECELL
	 *
	 * @return true - the value was set,<br>
	 *         false - This object is no table cell, you can not set it to bold
	 */
	public TableCellStyleBuilder fontWeightBold() {
		this.tsBuilder.fontWeightBold();
		return this;
	}

	/**
	 * Set the font weight to italic.<br>
	 * The TableFamilyStyle must be of a format of
	 * TableFamilyStyle.STYLE_TABLECELL
	 *
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder fontStyleItalic() {
		this.tsBuilder.fontStyleItalic();
		return this;
	}

	public TableCellStyleBuilder fontStyleNormal() {
		this.tsBuilder.fontStyleNormal();
		return this;
	}
	
	/**
	 * Set the font weight to normal.<br>
	 * The TableFamilyStyle must be of a format of
	 * TableFamilyStyle.STYLE_TABLECELL
	 *
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder fontWeightNormal() {
		this.tsBuilder.fontWeightNormal();
		return this;
	}

	/**
	 * /** Set font wrap.
	 *
	 * @param fSetWrap
	 *            <br>
	 *            true - Font will be wrapped,<br>
	 *            false - no font wrapping
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder fontWrap(final boolean fSetWrap) {
		this.bWrap = fSetWrap;
		return this;
	}

	/**
	 * Set the alignment of text.
	 *
	 * @param nAlign
	 *            - The text alignment flag,
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder textAlign(final TableCellStyle.Align nAlign) {
		this.nTextAlign = nAlign;
		return this;
	}

	/**
	 * Set the vertical alignment of text.
	 *
	 * @param nAlign
	 *            - The vertical alignment flag,<br>
	 *            either: VERTICALALIGN_TOP,VERTICALALIGN_MIDDLE or
	 *            VERTICALALIGN_BOTTOM
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder verticalAlign(
			final TableCellStyle.VerticalAlign nAlign) {
		this.nVerticalAlign = nAlign;
		return this;
	}
}
