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

import com.github.jferard.fastods.BorderAttribute.Position;
import com.github.jferard.fastods.BorderAttribute.Style;

/**
 * @author Julien Férard
 *
 *         Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin Schulz
 *         <mtschulz at users.sourceforge.net>
 *
 *         This file BorderAttributeBuilder.java is part of FastODS.
 *
 */
class BorderAttributeBuilder {
	/**
	 * The border size.
	 */
	private String sBorderSize;

	/**
	 * The border color
	 */
	private String sBorderColor;

	/**
	 * The border style. Either BorderAttribute.BORDER_SOLID or
	 * BorderAttribute.BORDER_DOUBLE.<br>
	 * Default is BorderAttribute.BORDER_SOLID.
	 */
	private Style style;

	/**
	 * The border position. Either BorderAttribute.POSITION_ALL,
	 * BorderAttribute.POSITION_BOTTOM, BorderAttribute.POSITION_TOP,
	 * BorderAttribute.POSITION_LEFT or BorderAttribute.POSITION_RIGHT.
	 */
	private Position position;

	public BorderAttributeBuilder() {
		this.position = BorderAttribute.DEFAULT_POSITION;
		this.style = BorderAttribute.DEFAULT_STYLE;
	}

	/**
	 * Set the currently set border color.
	 * 
	 * @param sBorderColor
	 *            The color in format #rrggbb
	 * @return this for fluent style
	 */
	public BorderAttributeBuilder borderColor(String sBorderColor) {
		this.sBorderColor = sBorderColor;
		return this;
	}

	/**
	 * Sets the current value of border size in pt.
	 * 
	 * @param nSize
	 *            The size as int, in pt
	 * @return this for fluent style
	 */
	public BorderAttributeBuilder borderSize(int nSize) {
		this.sBorderSize = new StringBuilder(nSize).append("pt").toString();
		return this;
	}

	/**
	 * Sets the current value of border size.
	 * 
	 * @param sBorderSize
	 *            The size as string, e.g. '0.1cm'
	 * @return this for fluent style
	 */
	public BorderAttributeBuilder borderSize(String sBorderSize) {
		this.sBorderSize = sBorderSize;
		return this;
	}

	/**
	 * Sets the current border NamedObject.
	 * 
	 * @param style
	 *            BorderAttribute.BORDER_SOLID or BorderAttribute.BORDER_DOUBLE
	 * @return this for fluent style
	 */
	public BorderAttributeBuilder borderStyle(Style style) {
		this.style = style;
		return this;
	}

	/**
	 * Builds a border style
	 * 
	 * @return ths BorderAttribute
	 */
	public BorderAttribute build() {
		return new BorderAttribute(this.sBorderSize, this.sBorderColor,
				this.style, this.position);
	}

	/**
	 * Sets the border positions as numerical value.
	 * 
	 * @param position
	 *            The position as one of
	 *            POSITION_TOP,POSITION_BOTTOM,POSITION_LEFT,POSITION_RIGHT or
	 *            POSITION_ALL.
	 */
	public BorderAttributeBuilder position(Position position) {
		this.position = position;
		return this;
	}
}
