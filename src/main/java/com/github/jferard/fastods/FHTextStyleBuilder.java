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

import com.github.jferard.fastods.FHTextStyle.Underline;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 *
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file TextStyleBuilder.java is part of FastODS. SimpleOds Version
 *         0.5.0 Added support for Font underline style
 */

class FHTextStyleBuilder {
	private Underline nFontUnderlineStyle;
	private String sFontColor;
	private String sFontName;
	private String sFontSize;
	private String sFontSizeAsian;
	private String sFontSizeComplex;
	private String sFontUnderlineColor;
	private String sFontWeight;
	private String sFontWeightAsian;
	private String sFontWeightComplex;

	private final String sName;
	private String sFontStyle;

	/**
	 * Create a new text style without a name.<br>
	 * This is used by class TableFamilyStyle. Version 0.5.2 Added
	 * @param sName2 
	 *
	 * @param odsFile
	 *            The file to add this style to
	 */
	public FHTextStyleBuilder(String sName) {
		if (sName == null)
			throw new IllegalArgumentException();

		this.sName = sName;
	}

	public FHTextStyle build() {
		return new FHTextStyle(this.sName, this.sFontColor, this.sFontName,
				this.sFontWeight, this.sFontStyle, this.sFontSize, this.sFontUnderlineColor,
				this.nFontUnderlineStyle);
	}

	/**
	 * Set the font color to sColor.
	 *
	 * @param sColor
	 *            The color to be used in format #rrggbb e.g. #ff0000 for a red
	 *            cell background
	 * @return this for fluent style
	 */
	public FHTextStyleBuilder fontColor(final String sColor) {
		this.sFontColor = sColor;
		return this;
	}

	/**
	 * Set the font name to be used for this style.
	 *
	 * @param fontName
	 *            The font name for this TextStyle
	 * @return
	 */
	public FHTextStyleBuilder fontName(final String fontName) {
		this.sFontName = fontName;
		return this;
	}

	/**
	 * Set the font size in points to the given value.
	 *
	 * @param fontSize
	 *            - The font size as int , e.g. 10 or 8
	 * @return
	 */
	public FHTextStyleBuilder fontSize(final int fontSize) {
		final String sSize = new StringBuilder(fontSize).append("pt")
				.toString();
		this.sFontSize = sSize;
		return this;
	}

	/**
	 * Set the font size to the given value<br>
	 * fontSize is a length value expressed as a number followed by pt, e.g.
	 * 12pt
	 *
	 * @param fontSize
	 *            - The font size as string, e.g. '10.5pt' or '8pt'
	 * @return
	 */
	public FHTextStyleBuilder fontSize(final String fontSize) {
		this.sFontSize = fontSize;
		return this;
	}

	/**
	 * Set the font underline color to sColor. Use an empty string to reset it
	 * to 'auto'.
	 *
	 * @param sColor
	 *            The color to be used in format #rrggbb e.g. #ff0000 for a red
	 *            cell background.
	 * @return
	 */
	public FHTextStyleBuilder fontUnderlineColor(final String sColor) {
		this.sFontUnderlineColor = sColor;
		return this;
	}

	/**
	 * Set the style that should be used for the underline. Valid is:<br>
	 * TextStyle.STYLE_UNDERLINE_NONE<br>
	 * TextStyle.STYLE_UNDERLINE_SOLID<br>
	 * TextStyle.STYLE_UNDERLINE_DOTTED<br>
	 * TextStyle.STYLE_UNDERLINE_DASH<br>
	 * TextStyle.STYLE_UNDERLINE_LONGDASH<br>
	 * TextStyle.STYLE_UNDERLINE_DOTDASH<br>
	 * TextStyle.STYLE_UNDERLINE_DOTDOTDASH<br>
	 * TextStyle.STYLE_UNDERLINE_WAVE<br>
	 * Other values are ignored.
	 *
	 * @param nStyle
	 *            One of the TextStyle.STYLE_UNDERLINE
	 * @return
	 */
	public FHTextStyleBuilder fontUnderlineStyle(final Underline nStyle) {
		this.nFontUnderlineStyle = nStyle;
		return this;
	}

	/**
	 * Set the font weight to bold.
	 *
	 * @return true
	 */
	public FHTextStyleBuilder fontWeightBold() {
		this.sFontWeight = "bold";
		return this;
	}

	/**
	 * Set the font weight to italic.
	 *
	 * @return true
	 */
	public FHTextStyleBuilder fontStyleItalic() {
		this.sFontStyle = "italic";
		return this;
	}
	
	/**
	 * Set the font weight to italic.
	 *
	 * @return true
	 */
	public FHTextStyleBuilder fontStyleNormal() {
		this.sFontStyle = "normal";
		return this;
	}
	

	/**
	 * Set the font weight to normal.
	 *
	 * @return true -
	 */
	public FHTextStyleBuilder fontWeightNormal() {
		this.sFontWeight = "normal";
		return this;
	}
}
