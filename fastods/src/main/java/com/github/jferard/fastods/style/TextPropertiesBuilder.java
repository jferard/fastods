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

import com.github.jferard.fastods.style.TextProperties.Underline;

/**
 * @author Julien Férard
 */
public class TextPropertiesBuilder {
	private String fontColor;
	private String fontName;
	private String fontSize;
	private String fontStyle;
	private String fontUnderlineColor;
	private Underline fontUnderlineStyle;
	private String fontWeight;

	/**
	 * @return the TextProperties
	 */
	public TextProperties build() {
		return new TextProperties(this.fontColor, this.fontName,
				this.fontWeight, this.fontStyle, this.fontSize,
				this.fontUnderlineColor, this.fontUnderlineStyle);
	}

	/**
	 * @param name the name of the style
	 * @return the TextStyle of that name
	 */
	public TextStyle buildStyle(final String name) {
		final TextProperties tp = new TextProperties(this.fontColor,
				this.fontName, this.fontWeight, this.fontStyle, this.fontSize,
				this.fontUnderlineColor, this.fontUnderlineStyle);
		return new TextStyle(name, tp);
	}

	/**
	 * Set the font color to color.
	 *
	 * @param color The color to be used in format #rrggbb e.g. #ff0000 for a red
	 *              cell background
	 * @return this for fluent style
	 */
	public TextPropertiesBuilder fontColor(final String color) {
		this.fontColor = color;
		return this;
	}

	/**
	 * Set the font name to be used for this style.
	 *
	 * @param fontName The font name for this TextStyle
	 * @return this for fluent style
	 */
	public TextPropertiesBuilder fontName(final String fontName) {
		this.fontName = fontName;
		return this;
	}

	/**
	 * Set the font size in points to the given value.
	 *
	 * @param fontSize - The font size as int , e.g. 10 or 8
	 * @return this for fluent style
	 */
	public TextPropertiesBuilder fontSize(final int fontSize) {
		this.fontSize = new StringBuilder(8).append(fontSize).append("pt")
				.toString();
		return this;
	}

	/**
	 * Set the font size to the given value<br>
	 * fontSize is a length value expressed as a number followed by pt, e.g.
	 * 12pt
	 *
	 * @param fontSize - The font size as string, e.g. '10.5pt' or '8pt'
	 * @return this for fluent style
	 */
	public TextPropertiesBuilder fontSize(final String fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 * Set the font weight to italic.
	 *
	 * @return true
	 */
	public TextPropertiesBuilder fontStyleItalic() {
		this.fontStyle = "italic";
		return this;
	}

	/**
	 * Set the font weight to italic.
	 *
	 * @return true
	 */
	public TextPropertiesBuilder fontStyleNormal() {
		this.fontStyle = "normal";
		return this;
	}

	/**
	 * Set the font underline color to color. Use an empty string to reset it to
	 * 'auto'.
	 *
	 * @param color The color to be used in format #rrggbb e.g. #ff0000 for a red
	 *              cell background.
	 * @return this for fluent style
	 */
	public TextPropertiesBuilder fontUnderlineColor(final String color) {
		this.fontUnderlineColor = color;
		return this;
	}

	/**
	 * Set the style that should be used for the underline.
	 *
	 * @param style One of the TextStyle.STYLE_UNDERLINE
	 * @return this for fluent style
	 */
	public TextPropertiesBuilder fontUnderlineStyle(final Underline style) {
		this.fontUnderlineStyle = style;
		return this;
	}

	/**
	 * Set the font weight to bold.
	 *
	 * @return this for fluent style
	 */
	public TextPropertiesBuilder fontWeightBold() {
		this.fontWeight = "bold";
		return this;
	}

	/**
	 * Set the font weight to normal.
	 *
	 * @return this for fluent style
	 */
	public TextPropertiesBuilder fontWeightNormal() {
		this.fontWeight = "normal";
		return this;
	}
}
