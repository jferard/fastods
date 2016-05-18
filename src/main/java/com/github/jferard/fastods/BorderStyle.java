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
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file BorderStyle.java is part of FastODS.
 */
public class BorderStyle {
	/**
	 * Flag to indicate that the top border should be shown, used by
	 * setPosition().
	 */
	public static final int POSITION_TOP = 1;
	/**
	 * Flag to indicate that the bottom border should be shown, used by
	 * setPosition().
	 */
	public static final int POSITION_BOTTOM = 2;
	/**
	 * Flag to indicate that the left border should be shown, used by
	 * setPosition().
	 */
	public static final int POSITION_LEFT = 3;
	/**
	 * Flag to indicate that the right border should be shown, used by
	 * setPosition().
	 */
	public static final int POSITION_RIGHT = 4;
	/**
	 * Flag to indicate that all borders should be shown, used by setPosition().
	 */
	public static final int POSITION_ALL = 5;
	/**
	 * Flag to indicate a border style 'Solid', used by setBorderStyle().
	 */
	public static final int BORDER_SOLID = 1;
	/**
	 * Flag to indicate a border style 'Double', used by setBorderStyle().
	 */
	public static final int BORDER_DOUBLE = 2;

	/**
	 * The border size default is 0.1cm
	 */
	public static final String DEFAULT_BORDER_SIZE = "0.1cm";

	/**
	 * The border color default is #000000 (black).
	 */
	public static final String DEFAULT_BORDER_COLOR = "#000000";

	/**
	 * The border style default is BorderStyle.BORDER_SOLID.
	 */
	public static final int DEFAULT_BORDER_STYLE = BORDER_SOLID;

	/*
	 * The border position default is BorderStyle.BORDER_ALL.
	 */
	public static final int DEFAULT_BORDER_POSITION = POSITION_ALL;

	/** a builder */
	public static BorderStyleBuilder builder() {
		return new BorderStyleBuilder();
	}

	/**
	 * The border size.
	 */
	private final String sBorderSize;

	/**
	 * The border color
	 */
	private final String sBorderColor;

	/**
	 * The border style. Either BorderStyle.BORDER_SOLID or
	 * BorderStyle.BORDER_DOUBLE.<br>
	 * Default is BorderStyle.BORDER_SOLID.
	 */
	private final int nBorderStyle;

	/**
	 * The border position. Either BorderStyle.POSITION_ALL,
	 * BorderStyle.POSITION_BOTTOM, BorderStyle.POSITION_TOP,
	 * BorderStyle.POSITION_LEFT or BorderStyle.POSITION_RIGHT.
	 */
	private final int nPosition;

	/** String representation */
	private final String string;

	/**
	 * sSize is a length value expressed as a number followed by a unit of
	 * measurement e.g. 0.1cm or 4px.<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 * 
	 * @param sSize
	 *            The size of the border
	 * @param sColor
	 *            The color of the border in format '#rrggbb'
	 * @param nStyle
	 *            The style of the border, BorderStyle.BORDER_SOLID or
	 *            BorderStyle.BORDER_DOUBLE
	 * @param nPos
	 *            The position to put the border on the cell,
	 *            BorderStyle.POSITION_TOP,BorderStyle.POSITION_BOTTOM,
	 *            BorderStyle.POSITION_LEFT,BorderStyle.POSITION_RIGHT or
	 *            BorderStyle.POSITION_ALL
	 */
	BorderStyle(final String sSize, final String sColor, final int nStyle,
			final int nPos) {
		assert 0 <= nStyle && nStyle <= BorderStyle.BORDER_DOUBLE;
		assert 0 <= nPos && nPos <= POSITION_ALL;

		this.sBorderSize = sSize;
		this.sBorderColor = sColor;
		this.nBorderStyle = nStyle;
		this.nPosition = nPos;
		this.string = this.computeString();
	}

	/**
	 * Get the currently set border color.
	 * 
	 * @return The color in format #rrggbb
	 */
	public String getBorderColor() {
		return this.sBorderColor;
	}

	/**
	 * Gets the current value of border size.
	 * 
	 * @return The size as string, e.g. '0.1cm'
	 */
	public String getBorderSize() {
		return this.sBorderSize;
	}

	/**
	 * Gets the current border NamedObject.
	 * 
	 * @return BorderStyle.BORDER_SOLID or BorderStyle.BORDER_DOUBLE
	 */
	public int getBorderStyle() {
		return this.nBorderStyle;
	}

	/**
	 * Returns the border positions as numerical value.
	 * 
	 * @return The position as one of
	 *         POSITION_TOP,POSITION_BOTTOM,POSITION_LEFT,POSITION_RIGHT or
	 *         POSITION_ALL.
	 */
	public int getPosition() {
		return this.nPosition;
	}

	@Override
	public String toString() {
		return this.string;
	}

	/**
	 * Since BorderStyle in now final, we can precomute the String
	 * representation
	 */
	private String computeString() {
		if (this.sBorderSize == null && this.sBorderColor == null)
			return "";

		StringBuilder sbReturn = new StringBuilder();
		switch (this.getPosition()) {
		case POSITION_TOP:
			sbReturn.append("fo:border-top=\"");
			break;
		case POSITION_BOTTOM:
			sbReturn.append("fo:border-bottom=\"");
			break;
		case POSITION_LEFT:
			sbReturn.append("fo:border-left=\"");
			break;
		case POSITION_RIGHT:
			sbReturn.append("fo:border-right=\"");
			break;
		case POSITION_ALL:
		default:
			sbReturn.append("fo:border=\"");
			break;
		}

		if (this.sBorderSize != null)
			sbReturn.append(this.sBorderSize).append(Util.SPACE_CHAR);

		if (this.sBorderColor != null) {
			switch (this.getBorderStyle()) {
			case BORDER_DOUBLE:
				sbReturn.append("double ");
				break;
			case BORDER_SOLID:
			default:
				sbReturn.append("solid ");
				break;
			}
			sbReturn.append(this.sBorderColor);
		}
		sbReturn.append("\" ");

		return sbReturn.toString();
	}
}
