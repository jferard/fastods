/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.TagParameters;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * OpenDocument 16.27.28
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TextProperties implements TagParameters {
	/** 20.380 : none,solid,dotted,dash,long-dash,dot-dash,dot-dot-dash,wave */
	public enum Underline {
		/**
		 * "text has a dashed line underlining it"
		 */
		DASH("dash"),
		/**
		 * " text has a line whose repeating pattern is a dot followed by a dash underlining it"
		 */
		DOTDASH("dot-dash"),
		/**
		 * "text has a line whose repeating pattern is two dots followed by a dash underlining it"
		 */
		DOTDOTDASH("dot-dot-dash"),
		/**
		 * "text has a dotted line underlining it"
		 */
		DOTTED("dotted"),
		/**
		 * "text has a dashed line whose dashes are longer than the ones from the dashed line for value dash underlining it"
		 */
		LONGDASH("long-dash"),
		/**
		 * "text has no underlining"
		 */
		NONE("none"),
		/**
		 * "text has a solid line underlining it"
		 */
		SOLID("solid"),
		/**
		 * "text has a wavy line underlining it"
		 */
		WAVE("wave");

		private final String attrValue;

		/**
		 * Create a new Underline attribute
		 * @param attrValue the attribute value
		 */
		Underline(final String attrValue) {
			this.attrValue = attrValue;
		}
	}

	/**
	 * @return a new builder
	 */
	public static TextPropertiesBuilder builder() {
		return new TextPropertiesBuilder();
	}

	private final Color fontColor;
	private final String fontName;
	private final String fontSize;
	private final String fontStyle;

	private final Color fontUnderlineColor;

	private final Underline fontUnderlineStyle;
	private final String fontWeight;

    /**
     * Create a new text style with the name name.
	 * @param fontColor the font color
     * @param fontName the font name
	 * @param fontWeight the font weight
	 * @param fontStyle the fon style
	 * @param fontSize the font size
	 * @param fontUnderlineColor the font underline color
	 * @param fontUnderlineStyle the font underline style
	 */
    TextProperties(final Color fontColor, final String fontName,
				   final String fontWeight, final String fontStyle,
				   final String fontSize, final Color fontUnderlineColor,
				   final Underline fontUnderlineStyle) {
		this.fontColor = fontColor;
		this.fontName = fontName;
		this.fontWeight = fontWeight;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
		this.fontUnderlineColor = fontUnderlineColor;
		this.fontUnderlineStyle = fontUnderlineStyle;
	}

	@Override
	public void appendXMLContent(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:text-properties");
		// Check if the font weight should be added
		if (this.fontWeight != null) {
			util.appendEAttribute(appendable, "fo:font-weight", this.fontWeight);
			util.appendEAttribute(appendable, "style:font-weight-asian",
					this.fontWeight);
			util.appendEAttribute(appendable, "style:font-weight-complex",
					this.fontWeight);
		}

		if (this.fontStyle != null) {
			util.appendEAttribute(appendable, "fo:font-style", this.fontStyle);
			util.appendEAttribute(appendable, "style:font-style-asian",
					this.fontStyle);
			util.appendEAttribute(appendable, "style:font-style-complex",
					this.fontStyle);
		}

		// Check if a font color should be added
		if (this.fontColor != SimpleColor.NONE) {
			util.appendAttribute(appendable, "fo:color", this.fontColor.hexValue());
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
			util.appendAttribute(appendable, "style:text-underline-style",
					this.fontUnderlineStyle.attrValue);
			util.appendAttribute(appendable, "style:text-underline-width",
					"auto");

			// ---------------------------------------------------------------------------------
			// If any underline color was set, add the color, otherwise use
			// the
			// font color
			// ---------------------------------------------------------------------------------
			if (this.fontUnderlineColor != SimpleColor.NONE) {
				util.appendAttribute(appendable, "style:text-underline-color",
						this.fontUnderlineColor.hexValue());
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
	 * @return The currently set font color.
	 */
	public Color getFontColor() {
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
	 * @return The color
	 */
	public Color getFontUnderlineColor() {
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

	/**
	 * @return true if at least one property was set
	 */
	public boolean isNotEmpty() {
		return this.fontUnderlineStyle != null || this.fontColor != SimpleColor.NONE
				|| this.fontSize != null || this.fontStyle != null
				|| this.fontUnderlineColor != SimpleColor.NONE || this.fontWeight != null;
	}
}
