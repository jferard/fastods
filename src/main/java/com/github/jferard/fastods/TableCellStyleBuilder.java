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
 */
class TableCellStyleBuilder {
	private String sName;
	private String sDataStyle;
	private String sBackgroundColor;
	private TextStyleBuilder tsBuilder;
	private int nTextAlign; // 'center','end','start','justify'
	private int nVerticalAlign; // 'middle', 'bottom', 'top'
	private boolean bWrap; // No line wrap when false, line wrap when
							// true
	private String sDefaultCellStyle;
	private ObjectQueue<BorderStyle> qBorders;

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
	public TableCellStyleBuilder() {
		this.nTextAlign = 0;
		this.nVerticalAlign = 0;
		this.bWrap = false;
		this.sBackgroundColor = "#FFFFFF";

		this.sDataStyle = "";
		this.tsBuilder = new TextStyleBuilder();
		this.sDefaultCellStyle = "Default";
		this.qBorders = ObjectQueue.newQueue();
	}

	/**
	 * Add a border style to this cell.
	 * 
	 * @param bs
	 *            - The border style to be used
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder addBorderStyle(final BorderStyle bs) {
		// -----------------------------------------
		// Make sure each position is unique
		// -----------------------------------------
		ListIterator<BorderStyle> iterator = this.qBorders.listIterator();
		while (iterator.hasNext()) {
			BorderStyle b = iterator.next();
			if (b.getPosition() == bs.getPosition()) {
				iterator.set(bs);
				return this;
			}
		}
		// ----------------------------------------------
		// Did not find it in qBorders, make a new entry
		// ----------------------------------------------
		this.qBorders.add(bs);
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
	 *            BorderStyle.BORDER_SOLID or BorderStyle.BORDER_DOUBLE
	 * @param nPosition
	 *            - The position of the line in this cell, e.g.
	 *            BorderStyle.POSITION_TOP
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder addBorderStyle(final String sSize,
			final String sBorderColor, final int nStyle, final int nPosition) {
		BorderStyle bs = new BorderStyle(sSize, sBorderColor, nStyle,
				nPosition);
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

	/**
	 * Set the data style for this TableFamilyStyle to cs.<br>
	 * If the StyleType of this TableFamilyStyle is not STYLE_TABLECELL, an
	 * exception is thrown
	 * 
	 * @param cs
	 *            The currency style to be used
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder dataStyle(final CurrencyStyle cs)
			throws SimpleOdsException {
		this.sDataStyle = cs.getName();
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
	public TableCellStyleBuilder dataStyle(final DateStyle ds)
			throws SimpleOdsException {
		this.sDataStyle = ds.getName();
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
	public TableCellStyleBuilder dataStyle(final NumberStyle ns)
			throws SimpleOdsException {
		this.sDataStyle = ns.getName();
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
	public TableCellStyleBuilder fontWeightItalic() {
		this.tsBuilder.fontWeightItalic();
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
	public TableCellStyleBuilder textAlign(final int nAlign) {
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
	public TableCellStyleBuilder verticalAlign(final int nAlign) {
		this.nVerticalAlign = nAlign;
		return this;
	}

	public TableCellStyleBuilder name(String sName) {
		this.sName = sName;
		return this;
	}

	public TableCellStyle build() {
		if (sName == null)
			throw new IllegalStateException();
		
		return new TableCellStyle(this.sName, this.sDataStyle,
				this.sBackgroundColor, this.tsBuilder.build(), this.nTextAlign,
				this.nVerticalAlign, this.bWrap, this.sDefaultCellStyle,
				this.qBorders

		);

	}
}
