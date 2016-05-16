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

/**
 * @author Julien Férard
 *
 *         Copyright (C) 2016 J. Férard 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file BorderStyleBuilder.java is part of FastODS.
 *
 */
class BorderStyleBuilder {
	/**
	 * The border size.
	 */
	private String sBorderSize;

	/**
	 * The border color
	 */
	private String sBorderColor;

	/**
	 * The border style. Either BorderStyle.BORDER_SOLID or
	 * BorderStyle.BORDER_DOUBLE.<br>
	 * Default is BorderStyle.BORDER_SOLID.
	 */
	private int nBorderStyle;

	/**
	 * The border position. Either BorderStyle.POSITION_ALL,
	 * BorderStyle.POSITION_BOTTOM, BorderStyle.POSITION_TOP,
	 * BorderStyle.POSITION_LEFT or BorderStyle.POSITION_RIGHT.
	 */
	private int nPosition;

	public BorderStyleBuilder() {
		this.nPosition = BorderStyle.DEFAULT_BORDER_POSITION;
		this.nBorderStyle = BorderStyle.DEFAULT_BORDER_STYLE;
	}

	/**
	 * Sets the current value of border size.
	 * 
	 * @param sBorderSize
	 *            The size as string, e.g. '0.1cm'
	 * @return this for fluent style
	 */
	public BorderStyleBuilder borderSize(String sBorderSize) {
		this.sBorderSize = sBorderSize;
		return this;
	}

	/**
	 * Sets the current value of border size in pt.
	 * 
	 * @param nSize
	 *            The size as int, in pt
	 * @return this for fluent style
	 */
	public BorderStyleBuilder borderSize(int nSize) {
		this.sBorderSize = new StringBuilder(nSize).append("pt").toString();
		return this;
	}

	/**
	 * Set the currently set border color.
	 * 
	 * @param sBorderColor
	 *            The color in format #rrggbb
	 * @return this for fluent style
	 */
	public BorderStyleBuilder borderColor(String sBorderColor) {
		this.sBorderColor = sBorderColor;
		return this;
	}

	/**
	 * Sets the current border NamedObject.
	 * 
	 * @param nBorderStyle
	 *            BorderStyle.BORDER_SOLID or BorderStyle.BORDER_DOUBLE
	 * @return this for fluent style
	 */
	public BorderStyleBuilder borderStyle(int nBorderStyle) {
		if (nBorderStyle < 0 || nBorderStyle > BorderStyle.BORDER_DOUBLE)
			this.nBorderStyle = BorderStyle.BORDER_SOLID;
		else
			this.nBorderStyle = nBorderStyle;
		return this;
	}

	/**
	 * Sets the border positions as numerical value.
	 * 
	 * @param nPosition
	 *            The position as one of
	 *            POSITION_TOP,POSITION_BOTTOM,POSITION_LEFT,POSITION_RIGHT or
	 *            POSITION_ALL.
	 */
	public BorderStyleBuilder position(int nPosition) {
		if (nPosition < 0 || nPosition > BorderStyle.POSITION_ALL)
			this.nPosition = BorderStyle.POSITION_ALL;
		else
			this.nPosition = nPosition;
		return this;
	}

	/**
	 * Builds a border style
	 * 
	 * @return ths BorderStyle
	 */
	public BorderStyle build() {
		return new BorderStyle(this.sBorderSize, this.sBorderColor,
				this.nBorderStyle, this.nPosition);
	}
}
