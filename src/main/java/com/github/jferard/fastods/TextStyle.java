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

import java.io.IOException;

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
public class TextStyle implements NamedObject, XMLAppendable {
	// 20.380 : none,solid,dotted,dash,long-dash,dot-dash,dot-dot-dash,wave
	public static enum Underline {
		NONE("none"),
		SOLID("solid"),
		DOTTED("dotted"),
		DASH("dash"),
		LONGDASH("long-dash"),
		DOTDASH("dot-dash"),
		DOTDOTDASH("dot-dot-dash"),
		WAVE("wave");
		
		private final String attrValue;

		private Underline(String attrValue) {
			this.attrValue = attrValue;
		}
	}

	public static TextStyleBuilder builder() {
		return new TextStyleBuilder();
	}

	private final String sName;
	private final String sFontColor;
	private final String sFontName;
	private final String sFontWeight;

	private final String sFontWeightAsian;

	private final String sFontWeightComplex;

	private final String sFontSize;
	private final String sFontSizeAsian;
	private final String sFontSizeComplex;
	private final String sFontUnderlineColor;

	private final Underline nFontUnderlineStyle;

	/**
	 * Create a new text style with the name sName.<br>
	 * Version 0.5.0 Added parameter OdsFile odsFile
	 * 
	 * @param sStyleName
	 *            The name of the text style.
	 * @param odsFile
	 *            The file to add this style to
	 */
	TextStyle(String sName, String sFontColor, String sFontName,
			String sFontWeight, String sFontWeightAsian,
			String sFontWeightComplex, String sFontSize, String sFontSizeAsian,
			String sFontSizeComplex, String sFontUnderlineColor,
			Underline nFontUnderlineStyle) {
		this.sName = sName;
		this.sFontColor = sFontColor;
		this.sFontName = sFontName;
		this.sFontWeight = sFontWeight;
		this.sFontWeightAsian = sFontWeightAsian;
		this.sFontWeightComplex = sFontWeightComplex;
		this.sFontSize = sFontSize;
		this.sFontSizeAsian = sFontSizeAsian;
		this.sFontSizeComplex = sFontSizeComplex;
		this.sFontUnderlineColor = sFontUnderlineColor;
		this.nFontUnderlineStyle = nFontUnderlineStyle;
	}

	public void addToFile(final OdsFile odsFile) {
		odsFile.getStyles().addTextStyle(this);
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
	public String getName() {
		return this.sName;
	}

	@Override
	public void appendXML(Util util, Appendable sbTemp) throws IOException {
		// -------------------------------------------------------------
		// The name maybe empty if this style is part of TableFamilyStyle.
		// Do not add the style:style
		// -------------------------------------------------------------
		if (this.sName.length() > 0) {
			sbTemp.append("<style:style ");
			util.appendAttribute(sbTemp, "style:name", this.getName());
			util.appendEAttribute(sbTemp, "style:family", "text");
			sbTemp.append(">");
		}

		// First check if any text properties should be added

		sbTemp.append("<style:text-properties ");
		// Check if the font weight should be added
		if (this.sFontWeight.length() > 0) {
			util.appendAttribute(sbTemp, "fo:font-weight", this.sFontWeight);
			util.appendAttribute(sbTemp, "style:font-weight-asian",
					this.sFontWeightAsian);
			util.appendAttribute(sbTemp, "style:font-weight-complex",
					this.sFontWeightComplex);
		}
		// Check if a font color should be added
		if (this.sFontColor.length() > 0) {
			util.appendAttribute(sbTemp, "fo:color", this.sFontColor);
		}
		// Check if a font name should be added
		if (this.sFontName.length() > 0) {
			util.appendAttribute(sbTemp, "style:font-name", this.sFontName);
		}
		// Check if a font size should be added
		if (this.sFontSize.length() > 0) {
			util.appendAttribute(sbTemp, "fo:font-size", this.sFontSize);
			util.appendAttribute(sbTemp, "style:font-size-asian",
					this.sFontSizeAsian);
			util.appendAttribute(sbTemp, "style:font-size-complex",
					this.sFontSizeComplex);
		}

		if (this.nFontUnderlineStyle != null) {
			util.appendEAttribute(sbTemp, "style:text-underline-style",
					this.nFontUnderlineStyle.attrValue);
			util.appendEAttribute(sbTemp, "style:text-underline-width", "auto");

			// ---------------------------------------------------------------------------------
			// If any underline color was set, add the color, otherwise use the
			// font color
			// ---------------------------------------------------------------------------------
			if (this.getFontUnderlineColor().length() > 0) {
				util.appendAttribute(sbTemp, "style:text-underline-color",
						this.sFontUnderlineColor);
			} else {
				util.appendAttribute(sbTemp, "style:text-underline-color",
						"font-color");
			}
		}

		sbTemp.append("/>");

		// -------------------------------------------------------------
		// The name maybe empty if this style is part of TableFamilyStyle.
		// Do not add the style:style
		// -------------------------------------------------------------
		if (this.getName().length() > 0) {
			sbTemp.append("</style:style>");
		}
	}

}
