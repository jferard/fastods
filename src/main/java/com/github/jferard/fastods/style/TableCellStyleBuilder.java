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

import java.util.EnumMap;
import java.util.Map;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.MarginAttribute.Position;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableCellStyleBuilder {
	private String backgroundColor;
	private final Map<BorderAttribute.Position, BorderAttribute> borderByPosition;
	private DataStyle dataStyle;
	private final EnumMap<Position, MarginAttribute> marginByPosition;
	private final String name;
	// true
	private String parentCellStyle;
	private TableCellStyle.Align textAlign; // 'center','end','start','justify'
	private final FHTextStyleBuilder tsBuilder;
	private final XMLUtil util;
	private TableCellStyle.VerticalAlign verticalAlign; // 'middle', 'bottom',
	// 'top'
	private boolean wrap; // No line wrap when false, line wrap when

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
	public TableCellStyleBuilder(final XMLUtil util, final String name) {
		this.util = util;
		if (name == null)
			throw new IllegalArgumentException();

		this.name = util.escapeXMLAttribute(name);
		this.parentCellStyle = "Default";
		this.tsBuilder = FHTextStyle.builder("fh" + name);
		this.borderByPosition = new EnumMap<BorderAttribute.Position, BorderAttribute>(
				BorderAttribute.Position.class);
		this.marginByPosition = new EnumMap<MarginAttribute.Position, MarginAttribute>(
				MarginAttribute.Position.class);
	}

	/**
	 * Add a border style to this cell.
	 *
	 * @param bs
	 *            - The border style to be used
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder addBorder(final BorderAttribute bs) {
		this.borderByPosition.put(bs.getPosition(), bs);
		return this;
	}

	/**
	 * Add a border style to this cell.
	 *
	 * @param size
	 *            The size of the line e.g. '0.1cm'
	 * @param borderColor
	 *            - The color to be used in format #rrggbb e.g. '#ff0000' for a
	 *            red border
	 * @param style
	 *            - The style of the border line, either
	 *            BorderAttribute.BORDER_SOLID or BorderAttribute.BORDER_DOUBLE
	 * @param position
	 *            - The position of the line in this cell, e.g.
	 *            BorderAttribute.POSITION_TOP
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder addBorder(final String size,
			final String borderColor, final BorderAttribute.Style style,
			final BorderAttribute.Position position) {
		final BorderAttribute bs = new BorderAttribute(size, borderColor, style,
				position);
		this.addBorder(bs);
		return this;
	}

	/**
	 * Add a border style to this cell.
	 *
	 * @param size
	 *            The size of the margin '0cm'
	 * @param position
	 *            - The position of the line in this cell, e.g.
	 *            BorderAttribute.POSITION_TOP
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder addMargin(final String size,
			final MarginAttribute.Position position) {
		this.marginByPosition.put(position,
				new MarginAttribute(size, position));
		return this;
	}

	/**
	 * Set the cell background color to color.<br>
	 * The TableFamilyStyle must be of a format of
	 * TableFamilyStyle.STYLE_TABLECELL
	 *
	 * @param color
	 *            - The color to be used in format #rrggbb e.g. #ff0000 for a
	 *            red cell background
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder backgroundColor(final String color) {
		this.backgroundColor = color;
		return this;
	}

	public TableCellStyle build() {
		if (this.name == null)
			throw new IllegalStateException();

		return new TableCellStyle(this.util, this.name, this.dataStyle,
				this.backgroundColor, this.tsBuilder.build(), this.textAlign,
				this.verticalAlign, this.wrap, this.parentCellStyle,
				this.borderByPosition, this.marginByPosition);

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
	public TableCellStyleBuilder dataStyle(final DataStyle ds) {
		this.dataStyle = ds;
		return this;
	}

	// /**
	// * Set the data style for this TableFamilyStyle to cs.<br>
	// * If the StyleType of this TableFamilyStyle is not STYLE_TABLECELL, an
	// * exception is thrown
	// *
	// * @param cs
	// * The currency style to be used
	// * @return this for fluent style
	// */
	// public TableCellStyleBuilder dataStyle(final CurrencyStyle cs) {
	// this.dataStyle = cs;
	// return this;
	// }

	/**
	 * Set the font color to color.<br>
	 * The TableFamilyStyle must be of a format of
	 * TableFamilyStyle.STYLE_TABLECELL
	 *
	 * @param color
	 *            The color to be used in format #rrggbb e.g. #ff0000 for a red
	 *            cell background
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder fontColor(final String color) {
		this.tsBuilder.fontColor(color);
		return this;
	}

	// /**
	// * Set the data style for this TableFamilyStyle to ns.<br>
	// * If the StyleType of this TableFamilyStyle is not STYLE_TABLECELL, an
	// * exception is thrown
	// *
	// * @param ns
	// * The number style to be used
	// * @return this for fluent style
	// */
	// public TableCellStyleBuilder dataStyle(final NumberStyle ns) {
	// this.dataStyle = ns;
	// return this;
	// }

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
		this.wrap = fSetWrap;
		return this;
	}

	/**
	 * Sets the parent cell style
	 *
	 * @param parentCellStyle
	 * @return this for fluent style.
	 */
	public TableCellStyleBuilder parentCellStyle(
			final TableCellStyle tableCellStyle) {
		if (tableCellStyle == null)
			this.parentCellStyle = null;
		else
			this.parentCellStyle = tableCellStyle.getName();
		return this;
	}

	/**
	 * Set the alignment of text.
	 *
	 * @param align
	 *            - The text alignment flag,
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder textAlign(final TableCellStyle.Align align) {
		this.textAlign = align;
		return this;
	}

	/**
	 * Set the vertical alignment of text.
	 *
	 * @param align
	 *            - The vertical alignment flag,<br>
	 *            either: VERTICALALIGN_TOP,VERTICALALIGN_MIDDLE or
	 *            VERTICALALIGN_BOTTOM
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder verticalAlign(
			final TableCellStyle.VerticalAlign align) {
		this.verticalAlign = align;
		return this;
	}
}
