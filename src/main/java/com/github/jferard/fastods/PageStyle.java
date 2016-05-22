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
 * TODO : clean code
 * 
 * @author Martin Schulz<br>
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file PageStyle.java is part of SimpleODS.
 *
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page
 */
public class PageStyle implements NamedObject {

	public final static int STYLE_PAPERFORMAT_A3 = 0;
	public final static int STYLE_PAPERFORMAT_A4 = 1;
	public final static int STYLE_PAPERFORMAT_A5 = 2;
	public final static int STYLE_PAPERFORMAT_LETTER = 3;
	public final static int STYLE_PAPERFORMAT_LEGAL = 4;
	public final static int STYLE_PAPERFORMAT_USER = 255; // Change nPaperFormat
															// to 255 if
															// setPageWidth() or
															// setPageHeigth()
															// is used

	public final static int STYLE_PRINTORIENTATION_VERTICAL = 0;
	public final static int STYLE_PRINTORIENTATION_HORIZONTAL = 1;

	public final static int STYLE_WRITINGMODE_LRTB = 0; // lr-tb (left to right;
														// top to bottom)
	public final static int STYLE_WRITINGMODE_RLTB = 1;
	public final static int STYLE_WRITINGMODE_TBRL = 2;
	public final static int STYLE_WRITINGMODE_TBLR = 3;
	public final static int STYLE_WRITINGMODE_LR = 4;
	public final static int STYLE_WRITINGMODE_RL = 5;
	public final static int STYLE_WRITINGMODE_TB = 6;
	public final static int STYLE_WRITINGMODE_PAGE = 7;

	private final String sName;
	private final String sMarginTop;
	private final String sMarginBottom;
	private final String sMarginLeft;
	private final String sMarginRight;

	private final String sPageWidth;
	private final String sPageHeight;
	private final String sNumFormat;
	private final String sBackgroundColor;

	private final String sTextStyleFooter;
	private final String sTextStyleHeader;
	private final String sTextHeader;
	private final String sTextFooter;

	private final int nPrintOrientation;
	private final int nPaperFormat;
	private final int nWritingMode;
	private FooterHeader header;
	private FooterHeader footer;

	/**
	 * Create a new page style. Version 0.5.0 Added parameter OdsFile o
	 * 
	 * @param sName
	 *            A unique name for this style
	 * @param header2
	 * @param footer
	 */
	public PageStyle(String sName, String sMarginTop, String sMarginBottom,
			String sMarginLeft, String sMarginRight, String sPageWidth,
			String sPageHeight, String sNumFormat, String sBackgroundColor,
			FooterHeader footer, String sTextStyleFooter, FooterHeader header,
			String sTextStyleHeader, String sTextHeader, String sTextFooter,
			int nPrintOrientation, int nPaperFormat, int nWritingMode) {
		this.sName = sName;
		this.sMarginTop = sMarginTop;
		this.sMarginBottom = sMarginBottom;
		this.sMarginLeft = sMarginLeft;
		this.sMarginRight = sMarginRight;
		this.sPageWidth = sPageWidth;
		this.sPageHeight = sPageHeight;
		this.sNumFormat = sNumFormat;
		this.sBackgroundColor = sBackgroundColor;
		this.footer = footer;
		this.sTextStyleFooter = sTextStyleFooter;
		this.header = header;
		this.sTextStyleHeader = sTextStyleHeader;
		this.sTextHeader = sTextHeader;
		this.sTextFooter = sTextFooter;
		this.nPrintOrientation = nPrintOrientation;
		this.nPaperFormat = nPaperFormat;
		this.nWritingMode = nWritingMode;
	}

	public void addToFile(OdsFile odsFile) {
		odsFile.getStyles().addPageStyle(this);
	}

	public String getBackgroundColor() {
		return this.sBackgroundColor;
	}

	public String getMarginBottom() {
		return this.sMarginBottom;
	}

	public String getMarginLeft() {
		return this.sMarginLeft;
	}

	public String getMarginRight() {
		return this.sMarginRight;
	}

	public String getMarginTop() {
		return this.sMarginTop;
	}

	/**
	 * Get the name of this page style.
	 * 
	 * @return The page style name
	 */
	public String getName() {
		return this.sName;
	}

	public String getPageHeight() {
		return this.sPageHeight;
	}

	public String getPageWidth() {
		return this.sPageWidth;
	}

	/**
	 * Get the paper format as one of PageStyle.STYLE_PAPERFORMAT_*.
	 */
	public int getPaperFormat() {
		return this.nPaperFormat;
	}

	/**
	 * Get the writing mode<br>
	 * . STYLE_WRITINGMODE_LRTB lr-tb (left to right; top to bottom)<br>
	 * STYLE_WRITINGMODE_RLTB<br>
	 * STYLE_WRITINGMODE_TBRL<br>
	 * STYLE_WRITINGMODE_TBLR<br>
	 * STYLE_WRITINGMODE_LR<br>
	 * STYLE_WRITINGMODE_RL<br>
	 * STYLE_WRITINGMODE_TB<br>
	 * STYLE_WRITINGMODE_PAGE<br>
	 * 
	 * @return The current writing mode.
	 */
	public int getWritingMode() {
		return this.nWritingMode;
	}

	private void appendBackgroundColor(Util util, Appendable appendable) throws IOException {
		if (this.getBackgroundColor().length() > 0) {
		util.appendAttribute(appendable, "fo:background-color", this.sBackgroundColor);
		}
	}

	private void addFooterHeaderStyle(final Appendable appendable,
			final FooterHeader header, final String tag) throws IOException {
		if (header == null) {
			appendable.append("<").append(tag).append(" />");
		} else {
			FooterHeader h = header;
			appendable.append("<").append(tag).append(">");
			appendable.append("<style:header-footer-properties ");
			appendable.append("fo:min-height=\"").append(h.getMinHeight())
					.append("\" ");
			appendable.append("fo:margin-left=\"").append(h.getMarginLeft())
					.append("\" ");
			appendable.append("fo:margin-right=\"").append(h.getMarginRight())
					.append("\" ");
			appendable.append("fo:margin-top=\"").append(h.getMarginTop())
					.append("\"/>");
			appendable.append("</").append(tag).append(">");
		}
	}

	private String addPrintOrientation() {
		StringBuilder sbTemp = new StringBuilder(" style:print-orientation=\"");
		if (this.nPrintOrientation == PageStyle.STYLE_PRINTORIENTATION_VERTICAL) {
			sbTemp.append("portrait");
		} else {
			sbTemp.append("landscape");
		}
		sbTemp.append("\" ");
		return sbTemp.toString();
	}

	private String addWritingMode() {
		StringBuilder sbTemp = new StringBuilder();

		sbTemp.append(" style:writing-mode=\"");

		switch (this.getWritingMode()) {
		case STYLE_WRITINGMODE_LRTB: // lr-tb (left to right; top to bottom)
			sbTemp.append("lr-tb");
			break;
		case STYLE_WRITINGMODE_RLTB:
			sbTemp.append("rl-tb");
			break;
		case STYLE_WRITINGMODE_TBRL:
			sbTemp.append("tb-rl");
			break;
		case STYLE_WRITINGMODE_TBLR:
			sbTemp.append("tb_lr");
			break;
		case STYLE_WRITINGMODE_LR:
			sbTemp.append("lr");
			break;
		case STYLE_WRITINGMODE_RL:
			sbTemp.append("rl");
			break;
		case STYLE_WRITINGMODE_TB:
			sbTemp.append("tb");
			break;
		case STYLE_WRITINGMODE_PAGE:
			sbTemp.append("page");
			break;
		default:
			sbTemp.append("lr-tb");
		}

		sbTemp.append("\"");
		return sbTemp.toString();
	}

	/**
	 * Return the master-style informations for this PageStyle.
	 * 
	 * @return The master style representation for styles.xml
	 */
	protected String toMasterStyleXML(Util util) {
		StringBuilder sbTemp = new StringBuilder();
		sbTemp.append("<style:master-page style:name=\"DefaultMasterPage\" ");
		sbTemp.append("style:page-layout-name=\"").append(this.getName())
				.append("\">");

		sbTemp.append("<style:header>");
		if (this.header == null) {
			sbTemp.append("<text:p text:style-name=\"")
					.append(this.sTextStyleHeader).append("\">")
					.append(this.sTextHeader).append("</text:p>");
		} else {
			sbTemp.append(this.header.toMasterStyleXML(util));
		}
		sbTemp.append("</style:header>");

		sbTemp.append("<style:footer>");
		if (this.footer == null) {
			sbTemp.append("<text:p text:style-name=\"")
					.append(this.sTextStyleFooter).append("\">")
					.append(this.sTextFooter).append("</text:p>");
		} else {
			sbTemp.append(this.footer.toMasterStyleXML(util));
		}
		sbTemp.append("</style:footer>");

		sbTemp.append("</style:master-page>");

		return sbTemp.toString();
	}

	public static PageStyleBuilder builder() {
		return new PageStyleBuilder();
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 */
	@Override
	public void appendXML(Util util, Appendable appendable) throws IOException {
		appendable.append("<style:page-layout");
		util.appendAttribute(appendable, "style:name", this.sName);
		appendable.append(">");
		appendable.append("<style:page-layout-properties");
		util.appendAttribute(appendable, "fo:page-width", this.sPageWidth);
		util.appendAttribute(appendable, "fo:page-height", this.sPageHeight);
		util.appendAttribute(appendable, "style:num-format", this.sNumFormat);
		appendable.append(addWritingMode());
		appendable.append(addPrintOrientation());
		this.appendBackgroundColor(util, appendable);
		util.appendAttribute(appendable, "fo:margin-top", this.sMarginTop);
		util.appendAttribute(appendable, "fo:margin-bottom", this.sMarginBottom);
		util.appendAttribute(appendable, "fo:margin-left", this.sMarginLeft);
		util.appendAttribute(appendable, "fo:margin-right", this.sMarginRight);
		appendable.append("/>"); // End of page-layout-properties

		appendable.append("<style:header-style />");

		this.addFooterHeaderStyle(appendable, this.header, "style:header-style");
		this.addFooterHeaderStyle(appendable, this.header, "style:footer-style");
		/*
		if( styles.getFooter()==null ) {
			sbTemp.append("<style:footer-style />");
		} else {
			FooterHeader f = styles.getFooter();
			sbTemp.append("<style:footer-style>");
			sbTemp.append("<style:header-footer-properties ");
			sbTemp.append("fo:min-height=\""+f.getMinHeight()+"\" ");
			sbTemp.append("fo:margin-left=\""+f.getMarginLeft()+"\" ");
			sbTemp.append("fo:margin-right=\""+f.getMarginRight()+"\" ");
			sbTemp.append("fo:margin-top=\""+f.getsMarginTop()+"\"/>");
			sbTemp.append("</style:footer-style>");		
		}*/
		appendable.append("</style:page-layout>");
	}
}
