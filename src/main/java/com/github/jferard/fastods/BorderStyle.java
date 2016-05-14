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
 * @author Martin Schulz<br>
 *
 * Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net><br>
 *
 * This file BorderStyle.java is part of SimpleODS.
 *
 */
public class BorderStyle {

	/**
	 * Flag to indicate that the top border should be shown, used by setPosition().
	 */
	public static final int POSITION_TOP = 1;
	/**
	 * Flag to indicate that the bottom border should be shown, used by setPosition().
	 */
	public static final int POSITION_BOTTOM = 2;
	/**
	 * Flag to indicate that the left border should be shown, used by setPosition().
	 */
	public static final int POSITION_LEFT = 3;
	/**
	 * Flag to indicate that the right border should be shown, used by setPosition().
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
	 * The border size.<br>
	 * Default is 0.1cm
	 */
	private String sBorderSize = "0.1cm";
	/**
	 * The border color.<br>
	 * Default is #000000 (black).
	 */
	private String sBorderColor = "#000000";
	/**
	 * The border style. Either BorderStyle.BORDER_SOLID or BorderStyle.BORDER_DOUBLE.<br>
	 * Default is BorderStyle.BORDER_SOLID.
	 */
	private int nBorderStyle = BORDER_SOLID;
	private int nPosition = POSITION_BOTTOM;
	
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
	public BorderStyle(final String sSize, final String sColor, final int nStyle, final int nPos) {
		this.setBorderSize(sSize);
		this.setBorderColor(sColor);
		this.setBorderStyle(nStyle);
		this.setPosition(nPos);

	}

	/**
	 * Gets the current value of border size.
	 * 
	 * @return The size as string, e.g. '0.1cm'
	 */
	public String getBorderSize() {
		return sBorderSize;
	}

	/**
	 * Adds the border size, e.g. '0.1cm'. borderSize is a length value<br>
	 * expressed as a number followed by a unit of measurement e.g. 0.1cm or 4px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), 
	 * pc (picas; 6 picas equals one inch), and pt (points; 72points equal one inch).<br>
	 * 
	 * @param borderSize
	 *            The border size as a unit of measurement
	 */
	public final void setBorderSize(final String borderSize) {
		sBorderSize = borderSize;
	}

	/**
	 * Set the border size in points to the given value.
	 * 
	 * @param borderSize
	 *            - The border size as int , e.g. 2 or 3
	 */
	public void setBorderSize(final int borderSize) {
		sBorderSize = Integer.toString(borderSize) + "pt";
	}

	/**
	 * Get the currently set border color.
	 * 
	 * @return The color in format #rrggbb
	 */
	public String getBorderColor() {
		return sBorderColor;
	}

	/**
	 * Set the border color to sColor.
	 * 
	 * @param borderColor
	 *            The color to be used in format #rrggbb e.g. #ff0000 for a red
	 *            border.
	 */
	public final void setBorderColor(final String borderColor) {
		sBorderColor = borderColor;
	}

	/**
	 * Gets the current border Style.
	 * 
	 * @return BorderStyle.BORDER_SOLID or BorderStyle.BORDER_DOUBLE
	 */
	public int getBorderStyle() {
		return nBorderStyle;
	}

	/**
	 * Sets the border style.
	 * 
	 * @param borderStyle
	 *            BorderStyle.BORDER_SOLID, BorderStyle.BORDER_DOUBLE
	 */
	public final void setBorderStyle(final int borderStyle) {
		nBorderStyle = borderStyle;
	}
	/**
	 * Sets the border size to solid.
	 */
	public void setBorderStyleSolid() {
		nBorderStyle = BORDER_SOLID;
	}
	/**
	 * Sets the border size to double.
	 */
	public void setBorderStyleDouble() {
		nBorderStyle = BORDER_DOUBLE;
	}
	/**
	 * Resets any previously set BorderStyle.
	 */
	public void unsetBorderStyle() {
		nBorderStyle = 0;
	}

	/**
	 * Returns the border positions as numerical value.
	 * 
	 * @return The position as one of
	 *         POSITION_TOP,POSITION_BOTTOM,POSITION_LEFT,POSITION_RIGHT or
	 *         POSITION_ALL.
	 */
	public int getPosition() {
		return nPosition;
	}

	/**
	 * Sets the position of the border, e.g. BorderStyle.POSITION_BOTTOM to set
	 * the border at the cell bottom.
	 * 
	 * @param position
	 *            The position as one of
	 *            POSITION_TOP,POSITION_BOTTOM,POSITION_LEFT,POSITION_RIGHT or
	 *            POSITION_ALL.
	 */
	public final void setPosition(final int position) {
		if (nPosition < 0 || nPosition > POSITION_ALL) {
			nPosition = POSITION_ALL;
		} else {
			nPosition = position;
		}
	}
	
	public String toString() {
		StringBuffer sbReturn = new StringBuffer();
		
		switch(this.getPosition()) {
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
			sbReturn.append("fo:border=\"");
			break;
		default:
			sbReturn.append("fo:border=\"");
		}
		
		sbReturn.append(this.sBorderSize + " ");
		
		switch(this.getBorderStyle()) {
		case BORDER_DOUBLE:
			sbReturn.append("double ");
			break;
		case BORDER_SOLID:
			sbReturn.append("solid ");
			break;
		default:	
			sbReturn.append("solid ");
		
		}
		sbReturn.append(this.getBorderColor() + "\" ");
		
		return sbReturn.toString();
	}	
	
}
