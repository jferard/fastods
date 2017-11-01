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

import java.io.IOException;

import com.github.jferard.fastods.util.XMLUtil;

/**
 * OpenDocument 16.27.28
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TextProperties {
	// 20.380 : none,solid,dotted,dash,long-dash,dot-dash,dot-dot-dash,wave
	public enum Underline {
		DASH("dash"), DOTDASH("dot-dash"), DOTDOTDASH("dot-dot-dash"), DOTTED(
				"dotted"), LONGDASH("long-dash"), NONE(
						"none"), SOLID("solid"), WAVE("wave");

		private final String attrValue;

		Underline(final String attrValue) {
			this.attrValue = attrValue;
		}
	}

	public static TextPropertiesBuilder builder() {
		return new TextPropertiesBuilder();
	}

	private final /*@Nullable*/ String fontColor;
	private final /*@Nullable*/ String fontName;
	private final /*@Nullable*/ String fontSize;
	private final /*@Nullable*/ String fontStyle;

	private final /*@Nullable*/ String fontUnderlineColor;

	private final /*@Nullable*/ Underline fontUnderlineStyle;
	private final /*@Nullable*/ String fontWeight;

	/**
	 * Create a new text style with the name name.
	 */
	TextProperties(final String fontColor, final String fontName,
			final String fontWeight, final String fontStyle,
			final String fontSize, final String fontUnderlineColor,
			final Underline fontUnderlineStyle) {
		this.fontColor = fontColor;
		this.fontName = fontName;
		this.fontWeight = fontWeight;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
		this.fontUnderlineColor = fontUnderlineColor;
		this.fontUnderlineStyle = fontUnderlineStyle;
	}

	public void appendXMLContent(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:text-properties");
		// Check if the font weight should be added
		if (this.fontWeight != null) {
			util.appendAttribute(appendable, "fo:font-weight", this.fontWeight);
			util.appendAttribute(appendable, "style:font-weight-asian",
					this.fontWeight);
			util.appendAttribute(appendable, "style:font-weight-complex",
					this.fontWeight);
		}

		if (this.fontStyle != null) {
			util.appendAttribute(appendable, "fo:font-style", this.fontStyle);
			util.appendAttribute(appendable, "style:font-style-asian",
					this.fontStyle);
			util.appendAttribute(appendable, "style:font-style-complex",
					this.fontStyle);
		}

		// Check if a font color should be added
		if (this.fontColor != null) {
			util.appendAttribute(appendable, "fo:color", this.fontColor);
		}
		// Check if a font name should be added
		if (this.fontName != null) {
			util.appendAttribute(appendable, "style:font-name", this.fontName);
		}
		// Check if a font size should be added
		if (this.fontSize != null) {
			util.appendAttribute(appendable, "fo:font-size", this.fontSize);
			util.appendAttribute(appendable, "style:font-size-asian",
					this.fontSize);
			util.appendAttribute(appendable, "style:font-size-complex",
					this.fontSize);
		}

		if (this.fontUnderlineStyle != null) {
			util.appendEAttribute(appendable, "style:text-underline-style",
					this.fontUnderlineStyle.attrValue);
			util.appendEAttribute(appendable, "style:text-underline-width",
					"auto");

			// ---------------------------------------------------------------------------------
			// If any underline color was set, add the color, otherwise use
			// the
			// font color
			// ---------------------------------------------------------------------------------
			if (this.fontUnderlineColor != null) {
				util.appendAttribute(appendable, "style:text-underline-color",
						this.fontUnderlineColor);
			} else {
				util.appendAttribute(appendable, "style:text-underline-color",
						"font-color");
			}
		}
		appendable.append("/>");
	}

	/**
	 * Get the current font color.
	 *
	 * @return The currently set font color as a String in format #rrggbb
	 */
	public String getFontColor() {
		return this.fontColor;
	}

	/**
	 * Get the font size as string, e.g. '10.5pt' or '8pt'
	 *
	 * @return The font size as string, e.g. '10.5pt' or '8pt'
	 */
	public String getFontSize() {
		return this.fontSize;
	}

	/**
	 * Get the currently set underline color.
	 *
	 * @return The color in format #rrggbb
	 */
	public String getFontUnderlineColor() {
		return this.fontUnderlineColor;
	}

	/**
	 * @return The currently set style for the underline.
	 */
	public Underline getFontUnderlineStyle() {
		return this.fontUnderlineStyle;
	}

	/**
	 * Get the current font weight.
	 *
	 * @return The current font weight, normal, bold or italic.
	 */
	public String getFontWeight() {
		return this.fontWeight;
	}

	public boolean isNotEmpty() {
		return this.fontUnderlineStyle != null || this.fontColor != null
				|| this.fontSize != null || this.fontStyle != null
				|| this.fontUnderlineColor != null || this.fontWeight != null;
	}
}
