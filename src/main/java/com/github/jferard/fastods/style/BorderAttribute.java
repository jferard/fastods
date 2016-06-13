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

import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file BorderAttribute.java is part of FastODS.
 *
 *         WHERE ? ../style:style#
 */
public class BorderAttribute {
	public static enum Position {
		ALL("fo:border"), BOTTOM("fo:border-bottom"), LEFT(
				"fo:border-left"), RIGHT(
						"fo:border-right"), TOP("fo:border-top");

		private final String attrName;

		private Position(final String attrName) {
			this.attrName = attrName;
		}

		String getAttrName() {
			return this.attrName;
		}
	}

	public static enum Style {
		DOUBLE("double"), SOLID("solid");

		private final String attrValue;

		private Style(final String attrValue) {
			this.attrValue = attrValue;
		}

		String getAttrValue() {
			return this.attrValue;
		}
	}

	/**
	 * The border color default is #000000 (black).
	 */
	public static final String DEFAULT_BORDER_COLOR = "#000000";

	/**
	 * The border size default is 0.1cm
	 */
	public static final String DEFAULT_BORDER_SIZE = "0.1cm";

	public static final Position DEFAULT_POSITION = Position.ALL;

	public static final Style DEFAULT_STYLE = Style.SOLID;

	/** a builder */
	public static BorderAttributeBuilder builder() {
		return new BorderAttributeBuilder();
	}

	/**
	 * The border position. Either BorderAttribute.POSITION_ALL,
	 * BorderAttribute.POSITION_BOTTOM, BorderAttribute.POSITION_TOP,
	 * BorderAttribute.POSITION_LEFT or BorderAttribute.POSITION_RIGHT.
	 */
	private final Position position;

	/**
	 * The border color
	 */
	private final String borderColor;

	/**
	 * The border size.
	 */
	private final String borderSize;

	/**
	 * The border style. Either BorderAttribute.BORDER_SOLID or
	 * BorderAttribute.BORDER_DOUBLE.<br>
	 * Default is BorderAttribute.BORDER_SOLID.
	 */
	private final Style style;

	/**
	 * size is a length value expressed as a number followed by a unit of
	 * measurement e.g. 0.1cm or 4px.<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @param size
	 *            The size of the border
	 * @param color
	 *            The color of the border in format '#rrggbb'
	 * @param style
	 *            The style of the border, BorderAttribute.BORDER_SOLID or
	 *            BorderAttribute.BORDER_DOUBLE
	 * @param pos
	 *            The position to put the border on the cell,
	 *            BorderAttribute.POSITION_TOP,BorderAttribute.POSITION_BOTTOM,
	 *            BorderAttribute.POSITION_LEFT,BorderAttribute.POSITION_RIGHT
	 *            or BorderAttribute.POSITION_ALL
	 */
	BorderAttribute(final String size, final String color, final Style style,
			final Position position) {
		this.borderSize = size;
		this.borderColor = color;
		this.style = style;
		this.position = position;
	}

	public void appendXMLToTableCellStyle(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.borderSize == null && this.borderColor == null)
			return;

		final StringBuilder sb = new StringBuilder();
		if (this.borderSize != null)
			sb.append(this.borderSize).append(XMLUtil.SPACE_CHAR);

		if (this.borderColor != null)
			sb.append(this.style.attrValue).append(XMLUtil.SPACE_CHAR)
					.append(this.borderColor);

		util.appendEAttribute(appendable, this.position.attrName,
				sb.toString());
	}

	/**
	 * Get the currently set border color.
	 *
	 * @return The color in format #rrggbb
	 */
	public String getBorderColor() {
		return this.borderColor;
	}

	/**
	 * Gets the current value of border size.
	 *
	 * @return The size as string, e.g. '0.1cm'
	 */
	public String getBorderSize() {
		return this.borderSize;
	}

	/**
	 * Returns the border positions as numerical value.
	 *
	 * @return The position as one of
	 *         POSITION_TOP,POSITION_BOTTOM,POSITION_LEFT,POSITION_RIGHT or
	 *         POSITION_ALL.
	 */
	public Position getPosition() {
		return this.position;
	}

	/**
	 * Gets the current border NamedObject.
	 *
	 * @return BorderAttribute.BORDER_SOLID or BorderAttribute.BORDER_DOUBLE
	 */
	public Style getStyle() {
		return this.style;
	}
}
