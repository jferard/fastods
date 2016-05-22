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
 * /**
 * 
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file TableFamilyStyle.java is part of FastODS. SimpleODS 0.5.1
 *         Changed all 'throw Exception' to 'throw SimpleOdsException' SimpleODS
 *         0.5.2 Replaced all text properties with a TextStyle object
 * 
 *         WHERE ?
 *         content.xml/office:document-content/office:automatic-styles/style:
 *         style
 */
public class TableCellStyle implements NamedObject, XMLAppendable {
	public final static int VERTICALALIGN_TOP = 1;
	public final static int VERTICALALIGN_MIDDLE = 2;
	public final static int VERTICALALIGN_BOTTOM = 3;

	public final static int ALIGN_LEFT = 1;
	public final static int ALIGN_CENTER = 2;
	public final static int ALIGN_RIGHT = 3;
	public final static int ALIGN_JUSTIFY = 4;

	private final String sName;
	private final String sDataStyle;
	private final String sBackgroundColor;
	private final TextStyle ts;
	private final int nTextAlign; // 'center','end','start','justify'
	private final int nVerticalAlign; // 'middle', 'bottom', 'top'
	private final boolean bWrap; // No line wrap when false, line wrap when
	// true
	private final String sDefaultCellStyle;
	private final ObjectQueue<BorderStyle> qBorders;

	/**
	 * Create a new table style and add it to contentEntry.<br>
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
	TableCellStyle(String sName, String sDataStyle, String sBackgroundColor,
			TextStyle ts, int nTextAlign, int nVerticalAlign, boolean bWrap,
			String sDefaultCellStyle, ObjectQueue<BorderStyle> qBorders) {
		this.sName = sName;
		this.sDataStyle = sDataStyle;
		this.sBackgroundColor = sBackgroundColor;
		this.ts = ts;
		this.nTextAlign = nTextAlign;
		this.nVerticalAlign = nVerticalAlign;
		this.bWrap = bWrap;
		this.sDefaultCellStyle = sDefaultCellStyle;
		this.qBorders = qBorders;
	}

	public void addToFile(final OdsFile odsFile) {
		odsFile.getContent().addTableStyle(this);
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 * @throws IOException
	 */
	public String toXML(Util util) {
		try {
			StringBuilder sbTemp = new StringBuilder();
			this.appendXML(util, sbTemp);
			return sbTemp.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	// enum + toString()
	private static String getVAlignAttribute(int nVerticalAlign) {
		String vAlignAttribute;
		switch (nVerticalAlign) {
		case TableCellStyle.VERTICALALIGN_TOP:
			vAlignAttribute = "top";
			break;
		case TableCellStyle.VERTICALALIGN_MIDDLE:
			vAlignAttribute = "middle";
			break;
		case TableCellStyle.VERTICALALIGN_BOTTOM:
			vAlignAttribute = "bottom";
			break;
		default:
			vAlignAttribute = "top";
			break;
		}
		return vAlignAttribute;
	}

	// enum + toSring();
	private static String getTextAlignAttribute(int nTextAlign) {
		String textAlignAttribute;
		switch (nTextAlign) {
		case TableCellStyle.ALIGN_LEFT:
			textAlignAttribute = "start";
			break;
		case TableCellStyle.ALIGN_CENTER:
			textAlignAttribute = "center";
			break;
		case TableCellStyle.ALIGN_RIGHT:
			textAlignAttribute = "end";
			break;
		case TableCellStyle.ALIGN_JUSTIFY:
			textAlignAttribute = "justify";
			break;
		default:
			textAlignAttribute = "start";
			break;
		}
		return textAlignAttribute;
	}

	public String getName() {
		return this.sName;
	}

	public static TableCellStyleBuilder builder() {
		return new TableCellStyleBuilder();
	}

	@Override
	public void appendXML(Util util, Appendable appendable) throws IOException {
		appendable.append("<style:style");
		util.appendAttribute(appendable, "style:name", this.sName);
		util.appendAttribute(appendable, "style:family", "table-cell");
		util.appendAttribute(appendable, "style:parent-style-name", "Default");
		if (this.sDataStyle.length() > 0)
			util.appendAttribute(appendable, "style:data-style-name",
					this.sDataStyle);

		appendable.append("><style:table-cell-properties");
		util.appendAttribute(appendable, "fo:background-color",
				this.sBackgroundColor);

		String vAlignAttribute = getVAlignAttribute(this.nVerticalAlign);
		util.appendAttribute(appendable, "style:vertical-align",
				vAlignAttribute);

		// -----------------------------------------------
		// Add all border styles
		// -----------------------------------------------
		for (BorderStyle bs : this.qBorders) {
			appendable.append(bs.toString());
		}

		if (this.bWrap)
			util.appendAttribute(appendable, "fo:wrap-option", "wrap");

		appendable.append("/>");
		// ----------------------------------------------------
		// First check if any text properties should be added
		// ----------------------------------------------------
		if (this.ts.getFontWeight().length() > 0
				|| this.ts.getFontSize().length() > 0
				|| this.ts.getFontColor().length() > 0) {
			this.ts.appendXML(util, appendable);
		}

		appendable.append("<style:paragraph-properties");

		String textAlignAttribute = TableCellStyle
				.getTextAlignAttribute(this.nTextAlign);

		util.appendAttribute(appendable, "fo:text-align", textAlignAttribute);
		util.appendAttribute(appendable, "fo:margin-left", "0cm");
		appendable.append("/></style:style>");
	}
}
