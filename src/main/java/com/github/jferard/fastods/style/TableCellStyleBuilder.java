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

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.util.Length;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableCellStyleBuilder {
	private String backgroundColor;
	private final BordersBuilder bordersBuilder;
	private DataStyle dataStyle;
	private final MarginsBuilder marginsBuilder;
	private final String name;
	// true
	private String parentCellStyleName;
	private TableCellStyle.Align textAlign; // 'center','end','start','justify'
	private final TextPropertiesBuilder tpBuilder;
	private TableCellStyle.VerticalAlign verticalAlign; // 'middle', 'bottom',
	// 'top'
	private boolean wrap; // No line wrap when false, line wrap when

	/**
	 * @param name
	 *            A unique name for this style
	 */
	TableCellStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalArgumentException();

		this.name = name;
		this.parentCellStyleName = "Default";
		this.tpBuilder = TextProperties.builder();
		this.bordersBuilder = new BordersBuilder();
		this.marginsBuilder = new MarginsBuilder();
	}

	/**
	 * Add a margin to this cell.
	 *
	 * @param size
	 *            The size of the margin
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder allMargins(final Length size) {
		this.marginsBuilder.all(size);
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

	/**
	 * Add a border style to this cell.
	 *
	 * @param size
	 *            The size of the line as a length
	 * @param borderColor
	 *            - The color to be used in format #rrggbb e.g. '#ff0000' for a
	 *            red border
	 * @param style
	 *            - The style of the border line, either
	 *            BorderAttribute.BORDER_SOLID or BorderAttribute.BORDER_DOUBLE
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder borderAll(final Length size,
			final String borderColor, final BorderAttribute.Style style) {
		final BorderAttribute bs = new BorderAttribute(size, borderColor,
				style);
		this.bordersBuilder.all(bs);
		return this;
	}

	public TableCellStyleBuilder borderBottom(final Length size,
			final String borderColor, final BorderAttribute.Style style) {
		final BorderAttribute bs = new BorderAttribute(size, borderColor,
				style);
		this.bordersBuilder.bottom(bs);
		return this;
	}

	public TableCellStyleBuilder borderLeft(final Length size,
			final String borderColor, final BorderAttribute.Style style) {
		final BorderAttribute bs = new BorderAttribute(size, borderColor,
				style);
		this.bordersBuilder.left(bs);
		return this;
	}

	public TableCellStyleBuilder borderRight(final Length size,
			final String borderColor, final BorderAttribute.Style style) {
		final BorderAttribute bs = new BorderAttribute(size, borderColor,
				style);
		this.bordersBuilder.right(bs);
		return this;
	}

	public TableCellStyleBuilder borderTop(final Length size,
			final String borderColor, final BorderAttribute.Style style) {
		final BorderAttribute bs = new BorderAttribute(size, borderColor,
				style);
		this.bordersBuilder.top(bs);
		return this;
	}

	public TableCellStyle build() {
		return new TableCellStyle(this.name, this.dataStyle,
				this.backgroundColor, this.tpBuilder.build(), this.textAlign,
				this.verticalAlign, this.wrap, this.parentCellStyleName,
				this.bordersBuilder.build(), this.marginsBuilder.build());
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
		this.tpBuilder.fontColor(color);
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
		this.tpBuilder.fontStyleItalic();
		return this;
	}

	public TableCellStyleBuilder fontStyleNormal() {
		this.tpBuilder.fontStyleNormal();
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
		this.tpBuilder.fontWeightBold();
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
	 * Set the font weight to normal.<br>
	 * The TableFamilyStyle must be of a format of
	 * TableFamilyStyle.STYLE_TABLECELL
	 *
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder fontWeightNormal() {
		this.tpBuilder.fontWeightNormal();
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
	 * Add a border style to this cell.
	 *
	 * @param size
	 *            The size of the margin as length
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder marginBottom(final Length size) {
		this.marginsBuilder.bottom(size);
		return this;
	}

	/**
	 * Add a border style to this cell.
	 *
	 * @param size
	 *            The size of the margin as length
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder marginLeft(final Length size) {
		this.marginsBuilder.left(size);
		return this;
	}

	/**
	 * Add a border style to this cell.
	 *
	 * @param size
	 *            The size of the margin as length
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder marginRight(final Length size) {
		this.marginsBuilder.right(size);
		return this;
	}

	/**
	 * Add a border style to this cell.
	 *
	 * @param size
	 *            The size of the margin as length
	 * @return this for fluent style
	 */
	public TableCellStyleBuilder marginTop(final Length size) {
		this.marginsBuilder.top(size);
		return this;
	}

	/**
	 * Sets the parent cell style
	 *
	 * @param tableCellStyle the parent cell style
	 * @return this for fluent style.
	 */
	public TableCellStyleBuilder parentCellStyle(
			final TableCellStyle tableCellStyle) {
		if (tableCellStyle == null)
			this.parentCellStyleName = null;
		else
			this.parentCellStyleName = tableCellStyle.getRealName();
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
