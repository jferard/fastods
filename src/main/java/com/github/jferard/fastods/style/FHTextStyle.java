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

import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.NamedObject;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file TextStyle.java is part of FastODS. SimpleOds Version 0.5.0
 *         Added support for Font underline style
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:automatic-styles/style:
 *         style/style:style
 */
public class FHTextStyle implements NamedObject {
	// 20.380 : none,solid,dotted,dash,long-dash,dot-dash,dot-dot-dash,wave
	public static enum Underline {
		DASH("dash"), DOTDASH("dot-dash"), DOTDOTDASH("dot-dot-dash"), DOTTED(
				"dotted"), LONGDASH("long-dash"), NONE(
						"none"), SOLID("solid"), WAVE("wave");

		private final String attrValue;

		private Underline(final String attrValue) {
			this.attrValue = attrValue;
		}
	}

	public static final FHTextStyle DEFAULT_TEXT_STYLE = FHTextStyle
			.builder("Default").build();

	public static FHTextStyleBuilder builder(final String name) {
		return new FHTextStyleBuilder(name);
	}

	private final String name;
	private final /*@Nullable*/ Underline nFontUnderlineStyle;
	private final /*@Nullable*/ String sFontColor;
	private final /*@Nullable*/ String sFontName;

	private final /*@Nullable*/ String sFontSize;

	private final /*@Nullable*/ String sFontStyle;
	private final /*@Nullable*/ String sFontUnderlineColor;

	private final /*@Nullable*/ String sFontWeight;

	/**
	 * Create a new text style with the name name.<br>
	 * Version 0.5.0 Added parameter OdsFile odsFile
	 *
	 * @param sStyleName
	 *            The name of the text style.
	 * @param odsFile
	 *            The file to add this style to
	 */
	FHTextStyle(final String name, final String sFontColor,
			final String sFontName, final String sFontWeight,
			final String sFontStyle, final String sFontSize,
			final String sFontUnderlineColor,
			final Underline nFontUnderlineStyle) {
		this.name = name;
		this.sFontColor = sFontColor;
		this.sFontName = sFontName;
		this.sFontWeight = sFontWeight;
		this.sFontStyle = sFontStyle;
		this.sFontSize = sFontSize;
		this.sFontUnderlineColor = sFontUnderlineColor;
		this.nFontUnderlineStyle = nFontUnderlineStyle;
	}

	public void appendXMLToContentEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:text-properties");
		// Check if the font weight should be added
		if (this.sFontWeight != null) {
			util.appendAttribute(appendable, "fo:font-weight",
					this.sFontWeight);
			util.appendAttribute(appendable, "style:font-weight-asian",
					this.sFontWeight);
			util.appendAttribute(appendable, "style:font-weight-complex",
					this.sFontWeight);
		}

		if (this.sFontStyle != null) {
			util.appendAttribute(appendable, "fo:font-style", this.sFontStyle);
			util.appendAttribute(appendable, "style:font-style-asian",
					this.sFontStyle);
			util.appendAttribute(appendable, "style:font-style-complex",
					this.sFontStyle);
		}

		// Check if a font color should be added
		if (this.sFontColor != null) {
			util.appendAttribute(appendable, "fo:color", this.sFontColor);
		}
		// Check if a font name should be added
		if (this.sFontName != null) {
			util.appendAttribute(appendable, "style:font-name", this.sFontName);
		}
		// Check if a font size should be added
		if (this.sFontSize != null) {
			util.appendAttribute(appendable, "fo:font-size", this.sFontSize);
			util.appendAttribute(appendable, "style:font-size-asian",
					this.sFontSize);
			util.appendAttribute(appendable, "style:font-size-complex",
					this.sFontSize);
		}

		if (this.nFontUnderlineStyle != null) {
			util.appendEAttribute(appendable, "style:text-underline-style",
					this.nFontUnderlineStyle.attrValue);
			util.appendEAttribute(appendable, "style:text-underline-width",
					"auto");

			// ---------------------------------------------------------------------------------
			// If any underline color was set, add the color, otherwise use
			// the
			// font color
			// ---------------------------------------------------------------------------------
			if (this.sFontUnderlineColor != null) {
				util.appendAttribute(appendable, "style:text-underline-color",
						this.sFontUnderlineColor);
			} else {
				util.appendAttribute(appendable, "style:text-underline-color",
						"font-color");
			}
		}
		appendable.append("/>");
	}

	public void appendXMLToStylesEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		// -------------------------------------------------------------
		// The name maybe empty if this style is part of TableFamilyStyle.
		// Do not add the style:style
		// -------------------------------------------------------------
		appendable.append("<style:style ");
		util.appendAttribute(appendable, "style:name", this.name);
		util.appendEAttribute(appendable, "style:family", "text");
		appendable.append(">");

		// First check if any text properties should be added

		this.appendXMLToContentEntry(util, appendable);

		appendable.append("</style:style>");
	}

	/**
	 * Get the current font color.
	 *
	 * @return The currently set font color as a String in format #rrggbb
	 */
	public String getFontColor() {
		return this.sFontColor;
	}

	/**
	 * Get the font size as string, e.g. '10.5pt' or '8pt'
	 *
	 * @return The font size as string, e.g. '10.5pt' or '8pt'
	 */
	public String getFontSize() {
		return this.sFontSize;
	}

	/**
	 * Get the currently set underline color.
	 *
	 * @return The color in format #rrggbb
	 */
	public String getFontUnderlineColor() {
		return this.sFontUnderlineColor;
	}

	/**
	 * @return The currently set style for the underline.
	 */
	public Underline getFontUnderlineStyle() {
		return this.nFontUnderlineStyle;
	}

	/**
	 * Get the current font weight.
	 *
	 * @return The current font weight, normal, bold or italic.
	 */
	public String getFontWeight() {
		return this.sFontWeight;
	}

	/**
	 * Get the name of this text style.
	 *
	 * @return The text style name
	 */
	@Override
	public String getName() {
		return this.name;
	}

	public boolean isNotEmpty() {
		return this.name != null && this.name.length() > 0
				&& (this.nFontUnderlineStyle != null || this.sFontColor != null
						|| this.sFontSize != null || this.sFontStyle != null
						|| this.sFontUnderlineColor != null
						|| this.sFontWeight != null);
	}

	void addToFile(final OdsFile odsFile) {
		odsFile.addTextStyle(this);
	}
}
