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
 * TODO : clean code
 * 
 * @author Martin Schulz<br>
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file PageStyle.java is part of SimpleODS.
 *
 * styles.xml/office:document-styles/office:master-styles/style:master-page 
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
	 */
	public PageStyle(String sName, String sMarginTop, String sMarginBottom,
			String sMarginLeft, String sMarginRight, String sPageWidth,
			String sPageHeight, String sNumFormat, String sBackgroundColor,
			String sTextStyleFooter, String sTextStyleHeader,
			String sTextHeader, String sTextFooter, int nPrintOrientation,
			int nPaperFormat, int nWritingMode) {
		this.sName = sName;
		this.sMarginTop = sMarginTop;
		this.sMarginBottom = sMarginBottom;
		this.sMarginLeft = sMarginLeft;
		this.sMarginRight = sMarginRight;
		this.sPageWidth = sPageWidth;
		this.sPageHeight = sPageHeight;
		this.sNumFormat = sNumFormat;
		this.sBackgroundColor = sBackgroundColor;
		this.sTextStyleFooter = sTextStyleFooter;
		this.sTextStyleHeader = sTextStyleHeader;
		this.sTextHeader = sTextHeader;
		this.sTextFooter = sTextFooter;
		this.nPrintOrientation = nPrintOrientation;
		this.nPaperFormat = nPaperFormat;
		this.nWritingMode = nWritingMode;
	}

	public void addToFile(OdsFile odsFile) {
		this.header = odsFile.getStyles().getHeader();
		this.footer = odsFile.getStyles().getFooter();
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

	private String addBackgroundColor() {
		if (this.getBackgroundColor().length() == 0) {
			return "";
		}

		return new StringBuilder("fo:background-color=\"")
				.append(this.getBackgroundColor()).append("\" ").toString();
	}

	private void addFooterHeaderStyle(final StringBuilder sbTemp,
			final FooterHeader header, final String tag) {
		if (header == null) {
			sbTemp.append("<").append(tag).append(" />");
		} else {
			FooterHeader h = header;
			sbTemp.append("<").append(tag).append(">");
			sbTemp.append("<style:header-footer-properties ");
			sbTemp.append("fo:min-height=\"").append(h.getMinHeight())
					.append("\" ");
			sbTemp.append("fo:margin-left=\"").append(h.getMarginLeft())
					.append("\" ");
			sbTemp.append("fo:margin-right=\"").append(h.getMarginRight())
					.append("\" ");
			sbTemp.append("fo:margin-top=\"").append(h.getMarginTop())
					.append("\"/>");
			sbTemp.append("</").append(tag).append(">");
		}
	}

	private String addPrintOrientation() {
		StringBuilder sbTemp = new StringBuilder("style:print-orientation=\"");
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

		sbTemp.append("style:writing-mode=\"");

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

		sbTemp.append("\" ");
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

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @param odsFile
	 * 
	 * @return The XML string for this object.
	 */
	public String toXML(Util util) {
		StringBuilder sbTemp = new StringBuilder();

		sbTemp.append("<style:page-layout style:name=\"").append(this.sName)
				.append("\">");
		sbTemp.append("<style:page-layout-properties ");
		sbTemp.append("fo:page-width=\"").append(this.getPageWidth())
				.append("\" ");
		sbTemp.append("fo:page-height=\"").append(this.getPageHeight())
				.append("\" ");
		sbTemp.append("style:num-format=\"").append(this.sNumFormat)
				.append("\" ");
		sbTemp.append(addWritingMode());
		sbTemp.append(addPrintOrientation());
		sbTemp.append(addBackgroundColor());
		sbTemp.append("fo:margin-top=\"").append(this.getMarginTop())
				.append("\" ");
		sbTemp.append("fo:margin-bottom=\"").append(this.getMarginBottom())
				.append("\" ");
		sbTemp.append("fo:margin-left=\"").append(this.getMarginLeft())
				.append("\" ");
		sbTemp.append("fo:margin-right=\"").append(this.getMarginRight())
				.append("\" ");
		sbTemp.append("/>"); // End of page-layout-properties

		sbTemp.append("<style:header-style />");

		this.addFooterHeaderStyle(sbTemp, this.header, "style:header-style");
		this.addFooterHeaderStyle(sbTemp, this.header, "style:footer-style");
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
		sbTemp.append("</style:page-layout>");

		return sbTemp.toString();
	}

	public static PageStyleBuilder builder() {
		return new PageStyleBuilder();
	}
}
