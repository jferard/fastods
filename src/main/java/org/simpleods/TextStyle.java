/*
*	SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
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
package org.simpleods;

/**
 * @author Martin Schulz<br>
 * 
 * Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net><br>
 * 
 * This file TextStyle.java is part of SimpleODS.<br>
 * Version 0.5.0 Added support for Font underline style
 */

public class TextStyle {
	private Util u = Util.getInstance();
	
	// none,solid,dotted,dash,long-dash,dot-dash,dot-dot-dash,wave
	public final static int STYLE_UNDERLINE_NONE = 0;
	public final static int STYLE_UNDERLINE_SOLID = 1;
	public final static int STYLE_UNDERLINE_DOTTED = 2;
	public final static int STYLE_UNDERLINE_DASH = 3;
	public final static int STYLE_UNDERLINE_LONGDASH = 4;
	public final static int STYLE_UNDERLINE_DOTDASH = 5;
	public final static int STYLE_UNDERLINE_DOTDOTDASH = 6;
	public final static int STYLE_UNDERLINE_WAVE = 7;
	
	private String sName="";
	private String sFontColor="";
	private String sFontName="";
	private String sFontWeight="";			// 0.5.2 Renamed from FontStyle to FontWeight
	private String sFontWeightAsian="";		// 0.5.2 Renamed from FontStyle to FontWeight
	private String sFontWeightComplex="";	// 0.5.2 Renamed from FontStyle to FontWeight
	private String sFontSize = "";	// text property
	private String sFontSizeAsian = "";
	private String sFontSizeComplex = "";
	private String sFontUnderlineColor = "";
	
	private int nFontUnderlineStyle = TextStyle.STYLE_UNDERLINE_NONE;
	
	/**
	 * The OdsFile where this object belong to.
	 */
	private OdsFile o;
	
	/**
	 * Create a new text style with the name sName.<br>
	 * Version 0.5.0 Added parameter OdsFile odsFile
	 * 
	 * @param sStyleName
	 *            The name of the text style.
	 * @param odsFile
	 *            The file to add this style to
	 */
	public TextStyle(final String sStyleName, final OdsFile odsFile) {
		this.setName(sStyleName);
		this.o = odsFile;
		o.getStyles().addTextStyle(this);
	}
	
	/**
	 * Create a new text style without a name.<br>
	 * This is used by class TableStyle.
	 * Version 0.5.2 Added 
	 * 
	 * @param odsFile
	 *            The file to add this style to
	 */
	public TextStyle(final OdsFile odsFile) {
		this.o = odsFile;
	}
	
	/**
	 * Get the name of this text style.
	 * @return The text style name
	 */
	public String getName() {
		return sName;
	}

	/**
	 * Set the name of this style to sName.
	 * @param name	- The name of this style
	 */
	public final void setName(final String name) {
		sName = name;
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
	 * Set the font color to sColor.
	 * 
	 * @param sColor
	 *            The color to be used in format #rrggbb e.g. #ff0000 for a red
	 *            cell background
	 */
	public void setFontColor(final String sColor) {
		this.sFontColor = sColor;
	}

	/**
	 * Set the font name to be used for this style.
	 * 
	 * @param fontName
	 *            The font name for this TextStyle
	 */
	public void setFontName(final String fontName) {
		this.sFontName = fontName;
	}

	/**
	 * Set the font weight to italic.
	 * 
	 * @return true
	 * @deprecated 0.5.2 Use setFontWeightItalic() instead.
	 */
	public boolean setFontStyleItalic() {
		this.sFontWeight = "italic";
		this.sFontWeightAsian = "italic";
		this.sFontWeightComplex = "italic";

		return true;
	}

	/**
	 * Set the font weight to italic.
	 * 
	 * @return true
	 */
	public boolean setFontWeightItalic() {
		this.sFontWeight = "italic";
		this.sFontWeightAsian = "italic";
		this.sFontWeightComplex = "italic";

		return true;
	}

	/**
	 * Set the font weight to bold.
	 * 
	 * @return true
	 * @deprecated 0.5.2 Use setFontWeightBold() instead.
	 */
	public boolean setFontStyleBold() {
		this.sFontWeight = "bold";
		this.sFontWeightAsian = "bold";
		this.sFontWeightComplex = "bold";

		return true;
	}

	/**
	 * Set the font weight to bold.
	 * 
	 * @return true
	 */
	public boolean setFontWeightBold() {
		this.sFontWeight = "bold";
		this.sFontWeightAsian = "bold";
		this.sFontWeightComplex = "bold";

		return true;
	}

	/**
	 * Set the font weight to normal.
	 * 
	 * @return true -
	 * @deprecated 0.5.2 Use setFontWeightNormal() instead.
	 */
	public boolean setFontStyleNormal() {
		this.sFontWeight = "normal";
		this.sFontWeightAsian = "normal";
		this.sFontWeightComplex = "normal";

		return true;
	}
	
	/**
	 * Get the current font weight.
	 * @return The current font weight, normal, bold or italic.
	 */
	public String getFontWeight() {
		return this.sFontWeight;
	}

	/**
	 * Set the font weight to normal.
	 * 
	 * @return true -
	 */
	public boolean setFontWeightNormal() {
		this.sFontWeight = "normal";
		this.sFontWeightAsian = "normal";
		this.sFontWeightComplex = "normal";

		return true;
	}
	
	
	/**
	 * Get the font size as string, e.g. '10.5pt' or '8pt' 
	 * @return	The font size as string, e.g. '10.5pt' or '8pt' 
	 */
	public String getFontSize() {
		return sFontSize;
	}

	/**
	 * Set the font size to the given value<br>
	 * fontSize is a length value expressed as a number followed by pt, e.g. 12pt
	 * @param fontSize	- The font size as string, e.g. '10.5pt' or '8pt'
	 */
	public void setFontSize(final String fontSize) {
		sFontSize = fontSize;
		sFontSizeAsian = fontSize;
		sFontSizeComplex = fontSize;
	}
	
	/**
	 * Set the font size in points to the given value.
	 * 
	 * @param fontSize
	 *            - The font size as int , e.g. 10 or 8
	 */
	public void setFontSize(final int fontSize) {
		String sSize = Integer.toString(fontSize) + "pt";
		sFontSize = sSize;
		sFontSizeAsian = sSize;
		sFontSizeComplex = sSize;
	}
	
	
	/**
	 * @return The currently set style for the underline.
	 */
	public int getFontUnderlineStyle() {
		return nFontUnderlineStyle;
	}

	/**
	 * Set the style that should be used for the underline. Valid is:<br>
	 * 	TextStyle.STYLE_UNDERLINE_NONE<br>
	 *  TextStyle.STYLE_UNDERLINE_SOLID<br>
	 *  TextStyle.STYLE_UNDERLINE_DOTTED<br>
	 *  TextStyle.STYLE_UNDERLINE_DASH<br>
	 *  TextStyle.STYLE_UNDERLINE_LONGDASH<br>
	 *  TextStyle.STYLE_UNDERLINE_DOTDASH<br>
	 *  TextStyle.STYLE_UNDERLINE_DOTDOTDASH<br>
	 *  TextStyle.STYLE_UNDERLINE_WAVE<br>
	 * Other values are ignored.
	 * 
	 * @param nStyle One of the TextStyle.STYLE_UNDERLINE
	 */
	public void setFontUnderlineStyle(final int nStyle) {
		this.nFontUnderlineStyle = nStyle;
		
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
	 * Set the font underline color to sColor. Use an empty string to reset it
	 * to 'auto'.
	 * 
	 * @param sColor
	 *            The color to be used in format #rrggbb e.g. #ff0000 for a red
	 *            cell background.
	 */
	public void setFontUnderlineColor(final String sColor) {
		this.sFontUnderlineColor = sColor;
	}
	

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	protected String toXML() {
		StringBuffer sbTemp = new StringBuffer();
		
		// -------------------------------------------------------------
		// The name maybe empty if this style is part of TableStyle.
		// Do not add the style:style
		// -------------------------------------------------------------
		if (this.getName().length() > 0) {
			sbTemp.append("<style:style ");
			u.appendElement(sbTemp, "style:name", this.getName());
			u.appendElement(sbTemp, "style:family", "text");
			sbTemp.append(">");
		}

		// First check if any text properties should be added

		sbTemp.append("<style:text-properties ");
		// Check if the font weight should be added
		if (this.sFontWeight.length() > 0) {
			sbTemp.append("fo:font-weight=\"" + this.sFontWeight
					+ "\" style:font-weight-asian=\"" + this.sFontWeightAsian
					+ "\" style:font-weight-complex=\""
					+ this.sFontWeightComplex + "\" ");

		}
		// Check if a font color should be added
		if (this.sFontColor.length() > 0) {
			u.appendElement(sbTemp, "fo:color", this.sFontColor);
			//sbTemp.append("fo:color=\"" + this.sFontColor + "\" ");
		}
		// Check if a font name should be added
		if (this.sFontName.length() > 0) {
			u.appendElement(sbTemp, "style:font-name", this.sFontName);
			//sbTemp.append("style:font-name=\"" + this.sFontName + "\" ");
		}
		// Check if a font size should be added
		if (this.sFontSize.length() > 0) {
			sbTemp.append("fo:font-size=\"" + this.sFontSize
					+ "\" style:font-size-asian=\"" + this.sFontSizeAsian
					+ "\" style:font-size-complex=\"" + this.sFontSizeComplex
					+ "\" "

			);
		}
		
		if (this.nFontUnderlineStyle > 0) {
			sbTemp.append("style:text-underline-style=\"");
			switch(this.getFontUnderlineStyle()) {
			case STYLE_UNDERLINE_NONE:
				sbTemp.append("none");
				break;
			case STYLE_UNDERLINE_SOLID:
				sbTemp.append("solid");
				break;
			case STYLE_UNDERLINE_DOTTED:
				sbTemp.append("dotted");
				break;
			case STYLE_UNDERLINE_DASH:
				sbTemp.append("dash");
				break;
			case STYLE_UNDERLINE_LONGDASH:
				sbTemp.append("long-dash");
				break;
			case STYLE_UNDERLINE_DOTDASH:
				sbTemp.append("dot-dash");
				break;
			case STYLE_UNDERLINE_DOTDOTDASH:
				sbTemp.append("dot-dot-dash");
				break;
			case STYLE_UNDERLINE_WAVE:
				sbTemp.append("wave");
				break;
			default:	
				sbTemp.append("none");
			}
			sbTemp.append("\" style:text-underline-width=\"auto\"");
			
			//---------------------------------------------------------------------------------
			// If any underline color was set, add the color, otherwise use the font color
			//---------------------------------------------------------------------------------
			if (this.getFontUnderlineColor().length() > 0) {
				u.appendElement(sbTemp, "style:text-underline-color", this.getFontUnderlineColor());
			} else {
				sbTemp.append(" style:text-underline-color=\"font-color\" ");
			}
		}

		sbTemp.append("/>");	
		
		// -------------------------------------------------------------
		// The name maybe empty if this style is part of TableStyle.
		// Do not add the style:style
		// -------------------------------------------------------------
		if (this.getName().length() > 0) {
			sbTemp.append("</style:style>");
		}

		return sbTemp.toString();
	}	
	

}
