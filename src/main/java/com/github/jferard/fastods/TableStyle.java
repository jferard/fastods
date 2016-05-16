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
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file TableStyle.java is part of SimpleODS.<br>
 *         0.5.1 Changed all 'throw Exception' to 'throw SimpleOdsException'<br>
 *         0.5.2 Replaced all text properties with a TextStyle object<br>
 */
public class TableStyle implements Style {

	public final static int STYLE_TABLE = 1;
	public final static int STYLE_TABLECOLUMN = 2;
	public final static int STYLE_TABLEROW = 3;
	public final static int STYLE_TABLECELL = 4;

	public final static int VERTICALALIGN_TOP = 1;
	public final static int VERTICALALIGN_MIDDLE = 2;
	public final static int VERTICALALIGN_BOTTOM = 3;

	public final static int ALIGN_LEFT = 1;
	public final static int ALIGN_CENTER = 2;
	public final static int ALIGN_RIGHT = 3;
	public final static int ALIGN_JUSTIFY = 4;

	private int nFamily;
	private String sName;
	private String sParentStyleName = "Default";
	private String sDataStyle = "";
	private String sBackgroundColor = "";
	// private String sColor="";
	// private String sFontWeight="";
	// private String sFontWeightAsian="";
	// private String sFontWeightComplex="";
	// private String sFontSize = ""; // text property
	// private String sFontSizeAsian = "";
	// private String sFontSizeComplex = "";
	// private String sFontUnderlineStyle = "";
	// private String sFontUnderlineColor = "";
	private TextStyle ts;
	private int nTextAlign = 0; // 'center','end','start','justify'
	private int nVerticalAlign = 0; // 'middle', 'bottom', 'top'
	private boolean bWrap = false; // No line wrap when false, line wrap when
									// true
	private String sDefaultCellStyle = "Default";
	private String sRowHeight;
	private String sColumnWidth;
	private ObjectQueue<BorderStyle> qBorders = ObjectQueue.newQueue();

	private Content content;

	/**
	 * The OdsFile where this object belong to.
	 */
	private OdsFile o;

	/**
	 * Create a new table style and add it to content.<br>
	 * Version 0.5.0 Added parameter OdsFile o
	 * 
	 * @param nFamily
	 *            The type of this style, either
	 *            STYLE_TABLECOLUMN,STYLE_TABLEROW,STYLE_TABLE or
	 *            STYLE_TABLECELL
	 * @param sStyleName
	 *            A unique name for this style
	 * @param odsFile
	 *            The OdsFile to add this style to
	 */
	public TableStyle(int nFamily, String sStyleName, OdsFile odsFile) {
		init(nFamily, sStyleName);
		this.o = odsFile;
		this.ts = new TextStyle(this.o);
		this.content = this.o.getContent();
		this.content.addTableStyle(this);

	}

	/**
	 * Create a new table style.<br>
	 * Removed with version 0.5.0
	 * 
	 * @param nFamily
	 *            The type of this style, either
	 *            STYLE_TABLECOLUMN,STYLE_TABLEROW,STYLE_TABLE or
	 *            STYLE_TABLECELL
	 * @param sName
	 *            A unique name for this style
	 */
	// public TableStyle(int nFamily, String sName) {
	// init(nFamily,sName);
	// }

	private void init(final int nFamily, String sName) {
		switch (nFamily) {
		case STYLE_TABLECOLUMN:
			this.sName = sName;
			this.sColumnWidth = "2.5cm"; // 0.5.0 changed from 2,500cm to 2.5cm
			this.sDefaultCellStyle = "Default";
			break;
		case STYLE_TABLEROW:
			this.sName = sName;
			this.sRowHeight = "0.45cm";
			break;
		case STYLE_TABLE:
			this.sName = sName;
			break;
		case STYLE_TABLECELL:
			this.sName = sName;
			this.sBackgroundColor = "#FFFFFF";
			break;
		default:

		}
		this.nFamily = nFamily;

	}

	protected void addStylesObject(Styles s) {
		this.o.setStyles(s);
	}

	/**
	 * Get the name of this table style.
	 * 
	 * @return The table style name
	 */
	public String getName() {
		return this.sName;
	}

	/**
	 * Get the style type of this TableStyle, this is either:<br>
	 * TableStyle.STYLE_TABLE, TableStyle.STYLE_TABLECOLUMN
	 * ,TableStyle.STYLE_TABLEROW or TableStyle.STYLE_TABLECELL.
	 * 
	 * @return One of the table styles
	 */
	public int getStyleType() {
		return this.nFamily;
	}

	public String getDefaultCellStyle() {
		return this.sDefaultCellStyle;
	}

	/**
	 * Set the column width of a table column.<br>
	 * sWidth is a length value expressed as a number followed by a unit of
	 * measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 * 
	 * @param sWidth
	 *            - The width of a column, e.g. '10cm'
	 * @return true - The width was set, false - this object is no table column,
	 *         you can not set the default cell to it
	 */
	public boolean setColumnWidth(final String sWidth) {
		if (this.nFamily == STYLE_TABLECOLUMN) {
			this.sColumnWidth = sWidth;
			return true;
		}
		return false;
	}

	/**
	 * Set the default cell style of a table column.
	 * 
	 * @param ts
	 *            The table cell style to be used as default
	 * @return true - The style was set as default,<br>
	 *         false - this object is no table column, you can not set the
	 *         default cell to it
	 */
	public boolean setDefaultCellStyle(final TableStyle ts) {
		if (this.nFamily != STYLE_TABLECOLUMN) {
			return false;
		}

		this.sDefaultCellStyle = ts.getName();
		this.content.addTableStyle(ts);

		return true;

	}

	/**
	 * Set the row height to a table row.<br>
	 * sHeight is a length value expressed as a number followed by a unit of
	 * measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 * 
	 * @param sHeight
	 *            The table row height to be used, e.g. '1.0cm'
	 * @return true - The height was set,<br>
	 *         false - his object is no table row, you can not set the height to
	 *         it
	 */
	public boolean setRowHeight(final String sHeight) {
		if (this.nFamily != STYLE_TABLEROW) {
			return false;
		}
		this.sRowHeight = sHeight;

		return true;
	}

	/**
	 * Set the cell background color to sColor.<br>
	 * The TableStyle must be of a format of TableStyle.STYLE_TABLECELL
	 * 
	 * @param sColor
	 *            - The color to be used in format #rrggbb e.g. #ff0000 for a
	 *            red cell background
	 * @return true - the value was set,<br>
	 *         false - This object is no table cell, you can not set the
	 *         background color to it
	 */
	public boolean setBackgroundColor(final String sColor) {
		if (this.nFamily != STYLE_TABLECELL) {
			return false;
		}
		this.sBackgroundColor = sColor;

		return true;
	}

	/**
	 * Set the font color to sColor.<br>
	 * The TableStyle must be of a format of TableStyle.STYLE_TABLECELL
	 * 
	 * @param sColor
	 *            The color to be used in format #rrggbb e.g. #ff0000 for a red
	 *            cell background
	 * @return true - the value was set,<br>
	 *         false - This object is no table cell, you can not set the font
	 *         color to it
	 */
	public boolean setFontColor(final String sColor) {
		if (this.nFamily != STYLE_TABLECELL) {
			return false;
		}
		this.ts.setFontColor(sColor);

		return true;
	}

	/**
	 * Set the font weight to italic.<br>
	 * The TableStyle must be of a format of TableStyle.STYLE_TABLECELL
	 * 
	 * @return true - the value was set,<br>
	 *         false - This object is no table cell, you can not set it to bold
	 */
	public boolean setFontWeightItalic() {
		if (this.nFamily != STYLE_TABLECELL) {
			return false;
		}
		this.ts.setFontWeightItalic();
		// this.sFontWeight = "italic";
		// this.sFontWeightAsian = "italic";
		// this.sFontWeightComplex = "italic";

		return true;
	}

	/**
	 * Set the font weight to bold.<br>
	 * The TableStyle must be of a format of TableStyle.STYLE_TABLECELL
	 * 
	 * @return true - the value was set,<br>
	 *         false - This object is no table cell, you can not set it to bold
	 */
	public boolean setFontWeightBold() {
		if (this.nFamily != STYLE_TABLECELL) {
			return false;
		}
		this.ts.setFontWeightBold();
		// this.sFontWeight = "bold";
		// this.sFontWeightAsian = "bold";
		// this.sFontWeightComplex = "bold";

		return true;
	}

	/**
	 * Set the font weight to normal.<br>
	 * The TableStyle must be of a format of TableStyle.STYLE_TABLECELL
	 * 
	 * @return true - the value was set,<br>
	 *         false - This object is no table cell, you can not set it to
	 *         normal
	 */
	public boolean setFontWeightNormal() {
		if (this.nFamily != STYLE_TABLECELL) {
			return false;
		}
		this.ts.setFontWeightNormal();
		// this.sFontWeight = "normal";
		// this.sFontWeightAsian = "normal";
		// this.sFontWeightComplex = "normal";

		return true;
	}

	/**
	 * Get the font size as string, e.g. '10.5pt' or '8pt'
	 * 
	 * @return The font size as string, e.g. '10.5pt' or '8pt'
	 */
	public String getFontSize() {
		return this.ts.getFontSize();
	}

	/**
	 * Set the font size to the given value<br>
	 * fontSize is a length value expressed as a number followed by pt, e.g.
	 * 12pt
	 * 
	 * @param fontSize
	 *            - The font size as string, e.g. '10.5pt' or '8pt'
	 */
	public void setFontSize(final String fontSize) {
		this.ts.setFontSize(fontSize);
		// sFontSize = fontSize;
		// sFontSizeAsian = fontSize;
		// sFontSizeComplex = fontSize;
	}

	/**
	 * Set the font size in points to the given value.
	 * 
	 * @param fontSize
	 *            - The font size as int , e.g. 10 or 8
	 */
	public void setFontSize(final int fontSize) {
		this.ts.setFontSize(fontSize);
		// sFontSize = Integer.toString(fontSize)+"pt";
		// sFontSizeAsian = Integer.toString(fontSize)+"pt";
		// sFontSizeComplex = Integer.toString(fontSize)+"pt";
	}

	/**
	 * Set font wrap.
	 * 
	 * @param fSetWrap
	 *            <br>
	 *            true - Font will be wrapped,<br>
	 *            false - no font wrapping
	 * @return true - successful,<br>
	 *         false - style must be either STYLE_TABLECELL or STYLE_TABLECOLUMN
	 */
	public boolean setFontWrap(final boolean fSetWrap) {
		if (this.nFamily == STYLE_TABLECELL
				|| this.nFamily == STYLE_TABLECOLUMN) {
			this.bWrap = fSetWrap;
			return true;
		}

		return false;
	}

	/**
	 * Set fond wrap to true.
	 * 
	 * @return true - successful, false - style must be either STYLE_TABLECELL
	 *         or STYLE_TABLECOLUMN
	 * @deprecated - Use setFontWrap(true)
	 */
	@Deprecated
	public boolean setFontWrap() {
		if (this.nFamily == STYLE_TABLECELL
				|| this.nFamily == STYLE_TABLECOLUMN) {
			this.bWrap = true;
			return true;
		}

		return false;
	}

	/**
	 * Reset the fond wrap.
	 * 
	 * @return true - successful, false - style must be either STYLE_TABLECELL
	 *         or STYLE_TABLECOLUMN
	 * @deprecated - Use setFontWrap(false)
	 */
	@Deprecated
	public boolean resetFontWrap() {
		if (this.nFamily == STYLE_TABLECELL
				|| this.nFamily == STYLE_TABLECOLUMN) {
			this.bWrap = false;
			return true;
		}

		return false;
	}

	/**
	 * Set the alignment of text.
	 * 
	 * @param nAlign
	 *            - The text alignment flag,
	 * @return true - successful,<br>
	 *         false - style must be either STYLE_TABLECELL or STYLE_TABLECOLUMN
	 */
	public boolean setTextAlign(final int nAlign) {
		if (this.nFamily == STYLE_TABLECELL
				|| this.nFamily == STYLE_TABLECOLUMN) {
			this.nTextAlign = nAlign;
			return true;
		}

		return false;
	}

	/**
	 * Reset any text alignment.
	 * 
	 * @return true - successful,<br>
	 *         false - style must be either STYLE_TABLECELL or STYLE_TABLECOLUMN
	 */
	public boolean resetTextAlign() {
		if (this.nFamily == STYLE_TABLECELL
				|| this.nFamily == STYLE_TABLECOLUMN) {
			this.nTextAlign = 0;
			return true;
		}

		return false;
	}

	/**
	 * Set the vertical alignment of text.
	 * 
	 * @param nAlign
	 *            - The vertical alignment flag,<br>
	 *            either: VERTICALALIGN_TOP,VERTICALALIGN_MIDDLE or
	 *            VERTICALALIGN_BOTTOM
	 * @return true - successful,<br>
	 *         false - style must be either STYLE_TABLECELL or STYLE_TABLECOLUMN
	 */
	public boolean setVerticalAlign(final int nAlign) {
		if (this.nFamily == STYLE_TABLECELL
				|| this.nFamily == STYLE_TABLECOLUMN) {
			this.nVerticalAlign = nAlign;
			return true;
		}

		return false;
	}

	/**
	 * Reset any vertical text alignment.
	 * 
	 * @return true - successful,<br>
	 *         false - style must be either STYLE_TABLECELL or STYLE_TABLECOLUMN
	 */
	public boolean resetVerticalAlign() {
		if (this.nFamily == STYLE_TABLECELL
				|| this.nFamily == STYLE_TABLECOLUMN) {
			this.nVerticalAlign = 0;
			return true;
		}

		return false;
	}

	/**
	 * Add a border style to this cell.
	 * 
	 * @param sSize
	 *            The size of the line e.g. '0.1cm'
	 * @param sBorderColor
	 *            - The color to be used in format #rrggbb e.g. '#ff0000' for a
	 *            red border
	 * @param nStyle
	 *            - The style of the border line, either
	 *            BorderStyle.BORDER_SOLID or BorderStyle.BORDER_DOUBLE
	 * @param nPosition
	 *            - The position of the line in this cell, e.g.
	 *            BorderStyle.POSITION_TOP
	 */
	public void addBorderStyle(final String sSize, final String sBorderColor,
			final int nStyle, final int nPosition) {
		if (this.nFamily == STYLE_TABLECELL) {
			BorderStyle bs = new BorderStyle(sSize, sBorderColor, nStyle,
					nPosition);
			this.addBorderStyle(bs);
		}
	}

	/**
	 * Add a border style to this cell.
	 * 
	 * @param bs
	 *            - The border style to be used
	 */
	public void addBorderStyle(final BorderStyle bs) {

		if (this.nFamily == STYLE_TABLECELL) {
			// -----------------------------------------
			// Make sure each position is unique
			// -----------------------------------------
			int n;
			for (n = 0; n < this.qBorders.size(); n++) {
				BorderStyle b = this.qBorders.get(n);
				if (b.getPosition() == bs.getPosition()) {
					this.qBorders.setAt(n, bs);
					return;
				}
			}
			// ----------------------------------------------
			// Did not find it in qBorders, make a new entry
			// ----------------------------------------------
			this.qBorders.add(bs);

		}
	}

	/**
	 * Set the data style for this TableStyle to ns.<br>
	 * If the StyleType of this TableStyle is not STYLE_TABLECELL, an exception
	 * is thrown
	 * 
	 * @param ns
	 *            The number style to be used
	 * @throws SimpleOdsException
	 *             Thrown if the style type of ns is not
	 *             TableStyle.STYLE_TABLECELL
	 */
	public void setDataStyle(final NumberStyle ns) throws SimpleOdsException {
		if (this.getStyleType() != TableStyle.STYLE_TABLECELL) {
			throw new SimpleOdsException(
					"Can not add the NumberStyle to this TableStyle, the TableStyle must be TableStyle.STYLE_TABLECELL");
		}
		this.sDataStyle = ns.getName();

	}

	/**
	 * Set the data style for this TableStyle to cs.<br>
	 * If the StyleType of this TableStyle is not STYLE_TABLECELL, an exception
	 * is thrown
	 * 
	 * @param cs
	 *            The currency style to be used
	 * @throws SimpleOdsException
	 *             Thrown if the style type of cs is not
	 *             TableStyle.STYLE_TABLECELL
	 */
	public void setDataStyle(final CurrencyStyle cs) throws SimpleOdsException {
		if (this.getStyleType() != TableStyle.STYLE_TABLECELL) {
			throw new SimpleOdsException(
					"Can not add the CurrencyStyle to this TableStyle, the TableStyle must be TableStyle.STYLE_TABLECELL");
		}
		this.sDataStyle = cs.getName();
	}

	/**
	 * Set the data style for this TableStyle to ds.<br>
	 * If the StyleType of this TableStyle is not STYLE_TABLECELL, an exception
	 * is thrown
	 * 
	 * @param ds
	 *            The date style to be used
	 * @throws SimpleOdsException
	 *             Thrown if the style type of ds is not
	 *             TableStyle.STYLE_TABLECELL
	 */
	public void setDataStyle(final DateStyle ds) throws SimpleOdsException {
		if (this.getStyleType() != TableStyle.STYLE_TABLECELL) {
			throw new SimpleOdsException(
					"Can not add the DateStyle to this TableStyle, the TableStyle must be TableStyle.STYLE_TABLECELL");
		}
		this.sDataStyle = ds.getName();
	}

	/**
	 * @return The currently set style for the underline.
	 */
	public int getFontUnderline() {
		return this.ts.getFontUnderlineStyle();
		// return sFontUnderlineStyle;
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
	 * @param nUnderlineStyle
	 *            The underline stlye
	 */
	public void setFontUnderline(final int nUnderlineStyle) {
		this.ts.setFontUnderlineStyle(nUnderlineStyle);
		// if
		// ("none,solid,dotted,dash,long-dash,dot-dash,dot-dot-dash,wave".indexOf(sFontUnderline.toLowerCase())
		// < 0) {
		// return;
		// }
		// this.sFontUnderlineStyle = sFontUnderline;
	}

	/**
	 * Get the current TextStyle object.
	 * 
	 * @return The current textStyle object
	 */
	public TextStyle getTextStyle() {
		return this.ts;
	}

	/**
	 * Set a new TextStyle object. This will overwrite all previous changed to
	 * the text styles<br>
	 * with the new textStyle.
	 * 
	 * @param textStyle
	 *            The new text style to be used
	 */
	public void setTextStyle(final TextStyle textStyle) {
		this.ts = textStyle;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	protected String toXML(Util util) {
		StringBuilder sbTemp = new StringBuilder();
		sbTemp.append("<style:style style:name=\"" + this.getName() + "\" ");
		sbTemp.append("style:family=");

		switch (this.nFamily) {
		case STYLE_TABLECOLUMN:
			sbTemp.append(
					"\"table-column\"><style:table-column-properties fo:break-before=\"auto\" style:column-width=\""
							+ this.sColumnWidth + "\" ");
			sbTemp.append("table:default-cell-style-name=\""
					+ this.sDefaultCellStyle + "\"/>");
			break;
		case STYLE_TABLEROW:
			sbTemp.append(
					"\"table-row\"><style:table-row-properties style:row-height=\""
							+ this.sRowHeight
							+ "\" fo:break-before=\"auto\" style:use-optimal-row-height=\"true\"/>");
			break;
		case STYLE_TABLE:
			sbTemp.append(
					"\"table\" style:master-page-name=\"DefaultMasterPage\"><style:table-properties table:display=\"true\" style:writing-mode=\"lr-tb\"/>");
			break;
		case STYLE_TABLECELL:
			sbTemp.append(
					"\"table-cell\" style:parent-style-name=\"Default\" ");
			if (this.sDataStyle.length() > 0) {
				sbTemp.append(
						"style:data-style-name=\"" + this.sDataStyle + "\">");
			} else {
				sbTemp.append(">");
			}
			sbTemp.append("<style:table-cell-properties fo:background-color=\""
					+ this.sBackgroundColor + "\" ");

			switch (this.nVerticalAlign) {
			case VERTICALALIGN_TOP:
				sbTemp.append("style:vertical-align=\"top\" ");
				break;
			case VERTICALALIGN_MIDDLE:
				sbTemp.append("style:vertical-align=\"middle\" ");
				break;
			case VERTICALALIGN_BOTTOM:
				sbTemp.append("style:vertical-align=\"bottom\" ");
				break;
			default:
				sbTemp.append("style:vertical-align=\"top\" ");
				break;
			}

			// -----------------------------------------------
			// Add all border styles
			// -----------------------------------------------
			for (int n = 0; n < this.qBorders.size(); n++) {
				BorderStyle bs = this.qBorders.get(n);
				sbTemp.append(bs.toString());
			}

			if (this.bWrap) {
				sbTemp.append(" fo:wrap-option=\"wrap\" ");
			}
			sbTemp.append("/>");
			// ----------------------------------------------------
			// First check if any text properties should be added
			// ----------------------------------------------------
			if (this.ts.getFontWeight().length() > 0
					|| this.ts.getFontSize().length() > 0
					|| this.ts.getFontColor().length() > 0) {
				sbTemp.append(this.ts.toXML(util));
			}

			/*
			 * <style:style style:family="text"><style:text-properties fo:font-weight="bold" style:font-weight-asian="bold" style:font-weight-complex="bold" fo:font-size="13pt" style:font-size-asian="13pt" style:font-size-complex="13pt" style:text-underline-style="dot-dot-dash" style:text-underline-width="auto" style:text-underline-color="#8A2BE2"/></style:style>
			if (this.sFontWeight.length() > 0 || this.sColor.length() > 0 || this.sFontSize.length() > 0) {
				sbTemp.append("<style:text-properties ");
				//-----------------------------------------------
				// Check if the font weight should be added
				//-----------------------------------------------
				if (this.sFontWeight.length() > 0) {
					sbTemp.append("fo:font-weight=\"" + this.sFontWeight
							+ "\" style:font-weight-asian=\""
							+ this.sFontWeightAsian
							+ "\" style:font-weight-complex=\""
							+ this.sFontWeightComplex + "\" ");
			
				}
				//-----------------------------------------------
				// Check if a font color should be added
				//-----------------------------------------------
				if (this.sColor.length() > 0) {
					sbTemp.append("fo:color=\"" + this.sColor + "\" ");
				}
				//-----------------------------------------------
				// Check is a font size should be added
				//-----------------------------------------------
				if (this.sFontSize.length() > 0) {
					sbTemp.append("fo:font-size=\"" + this.sFontSize + "\" style:font-size-asian=\"" + this.sFontSizeAsian
							+ "\" style:font-size-complex=\"" + this.sFontSizeComplex + "\" "
			
					);
				}
				//-----------------------------------------------
				// Check if a underline style should be added
				//-----------------------------------------------
				if (this.sFontUnderlineStyle.length() > 0) {
					sbTemp.append("style:text-underline-style=\"" + this.sFontUnderlineStyle
							+ "\" style:text-underline-width=\"auto\" style:text-underline-color=\"font-color\" ");
				}	
								
				sbTemp.append("/>");
			}*/
			switch (this.nTextAlign) {
			case ALIGN_LEFT:
				sbTemp.append(
						"<style:paragraph-properties fo:text-align=\"start\" fo:margin-left=\"0cm\"/>");
				break;
			case ALIGN_CENTER:
				sbTemp.append(
						"<style:paragraph-properties fo:text-align=\"center\" fo:margin-left=\"0cm\"/>");
				break;
			case ALIGN_RIGHT:
				sbTemp.append(
						"<style:paragraph-properties fo:text-align=\"end\" fo:margin-left=\"0cm\"/>");
				break;
			case ALIGN_JUSTIFY:
				sbTemp.append(
						"<style:paragraph-properties fo:text-align=\"justify\" fo:margin-left=\"0cm\"/>");
				break;
			default:
				sbTemp.append(
						"<style:paragraph-properties fo:text-align=\"start\" fo:margin-left=\"0cm\"/>");
				break;
			}

			break;
		default:
			sbTemp.append("\">");
		}
		sbTemp.append("</style:style>");

		return sbTemp.toString();
	}

}
