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
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file TextStyle.java is part of FastODS. SimpleOds Version 0.5.0
 *         Added support for Font underline style
 */

class TextStyleBuilder {
	private String sName;
	private String sFontColor;
	private String sFontName;
	private String sFontWeight;
	private String sFontWeightAsian;
	private String sFontWeightComplex;
	private String sFontSize;
	private String sFontSizeAsian;
	private String sFontSizeComplex;
	private String sFontUnderlineColor;

	private int nFontUnderlineStyle;

	/**
	 * Create a new text style without a name.<br>
	 * This is used by class TableFamilyStyle. Version 0.5.2 Added
	 * 
	 * @param odsFile
	 *            The file to add this style to
	 */
	public TextStyleBuilder() {
		this.sName = "";
		this.sFontColor = "";
		this.sFontName = "";
		this.sFontWeight = ""; // 0.5.2 Renamed from FontStyle to
								// FontWeight
		this.sFontWeightAsian = ""; // 0.5.2 Renamed from FontStyle to
									// FontWeight
		this.sFontWeightComplex = ""; // 0.5.2 Renamed from FontStyle to
										// FontWeight
		this.sFontSize = ""; // text property
		this.sFontSizeAsian = "";
		this.sFontSizeComplex = "";
		this.sFontUnderlineColor = "";

		this.nFontUnderlineStyle = TextStyle.STYLE_UNDERLINE_NONE;
	}

	/**
	 * Set the font color to sColor.
	 * 
	 * @param sColor
	 *            The color to be used in format #rrggbb e.g. #ff0000 for a red
	 *            cell background
	 * @return this for fluent style
	 */
	public TextStyleBuilder fontColor(final String sColor) {
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
	public TextStyleBuilder fontName(final String fontName) {
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
	public TextStyleBuilder fontSize(final int fontSize) {
		String sSize = new StringBuilder(fontSize).append("pt").toString();
		this.sFontSize = sSize;
		this.sFontSizeAsian = sSize;
		this.sFontSizeComplex = sSize;
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
	public TextStyleBuilder fontSize(final String fontSize) {
		this.sFontSize = fontSize;
		this.sFontSizeAsian = fontSize;
		this.sFontSizeComplex = fontSize;
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
	public TextStyleBuilder fontUnderlineColor(final String sColor) {
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
	public TextStyleBuilder fontUnderlineStyle(final int nStyle) {
		this.nFontUnderlineStyle = nStyle;
		return this;
	}

	/**
	 * Set the font weight to bold.
	 * 
	 * @return true
	 */
	public TextStyleBuilder fontWeightBold() {
		this.sFontWeight = "bold";
		this.sFontWeightAsian = "bold";
		this.sFontWeightComplex = "bold";
		return this;
	}

	/**
	 * Set the font weight to italic.
	 * 
	 * @return true
	 */
	public TextStyleBuilder fontWeightItalic() {
		this.sFontWeight = "italic";
		this.sFontWeightAsian = "italic";
		this.sFontWeightComplex = "italic";

		return this;
	}

	/**
	 * Set the font weight to normal.
	 * 
	 * @return true -
	 */
	public TextStyleBuilder fontWeightNormal() {
		this.sFontWeight = "normal";
		this.sFontWeightAsian = "normal";
		this.sFontWeightComplex = "normal";

		return this;
	}

	/**
	 * Set the name of this style to sName.
	 * 
	 * @param name
	 *            - The name of this style
	 * @return
	 */
	public final TextStyleBuilder name(final String name) {
		this.sName = name;
		return this;
	}

	public TextStyle build() {
		return new TextStyle(this.sName, this.sFontColor, this.sFontName,
				this.sFontWeight, this.sFontWeightAsian,
				this.sFontWeightComplex, this.sFontSize, this.sFontSizeAsian,
				this.sFontSizeComplex, this.sFontUnderlineColor,
				this.nFontUnderlineStyle);
	}
}
