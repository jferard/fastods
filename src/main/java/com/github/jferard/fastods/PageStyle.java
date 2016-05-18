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

	private String sName;
	private String sMarginTop = "1.5cm";
	private String sMarginBottom = "1.5cm";
	private String sMarginLeft = "1.5cm";
	private String sMarginRight = "1.5cm";

	private String sPageWidth = "29.7cm";
	private String sPageHeight = "21.0cm";
	private String sNumFormat = "1";
	private String sBackgroundColor = "";

	private String sTextStyleFooter = "";
	private String sTextStyleHeader = "";
	private String sTextHeader = "";
	private String sTextFooter = "";

	private int nPrintOrientation = PageStyle.STYLE_PRINTORIENTATION_VERTICAL;
	private int nPaperFormat = PageStyle.STYLE_PAPERFORMAT_A4;
	private int nWritingMode = PageStyle.STYLE_WRITINGMODE_LRTB;

	/**
	 * The OdsFile where this object belong to
	 */
	private OdsFile o;

	/**
	 * Create a new page style. Version 0.5.0 Added parameter OdsFile o
	 * 
	 * @param sName
	 *            A unique name for this style
	 */
	public PageStyle(final String sName, OdsFile odsFile) {
		this.sName = sName;
		this.o = odsFile;
		this.o.getStyles().addPageStyle(this);
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

	/**
	 * Set the margin at the top,bottom,left and right. margin is a length value
	 * expressed as a number followed by a unit of measurement e.g. 1.5cm or
	 * 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 * 
	 * @param margin
	 */
	public void setAllMargins(final String margin) {
		setMarginTop(margin);
		setMarginBottom(margin);
		setMarginLeft(margin);
		setMarginRight(margin);
	}

	/**
	 * Set the background color to sColor, a six-digit hex value. Example:
	 * #aa32f0.<br>
	 * The background color may also be set to 'transparent' if a background
	 * image is used (currently unsupported).
	 * 
	 * @param sColor
	 */
	public void setBackgroundColor(final String sColor) {
		this.sBackgroundColor = sColor;
	}

	/**
	 * Set the margin at the bottom. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 * 
	 * @param margin
	 */
	public void setMarginBottom(final String margin) {
		this.sMarginBottom = margin;
	}

	/**
	 * Set the margin at the left. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 * 
	 * @param margin
	 */
	public void setMarginLeft(final String margin) {
		this.sMarginLeft = margin;
	}

	/**
	 * Set the margin at the right. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 * 
	 * @param margin
	 */
	public void setMarginRight(final String margin) {
		this.sMarginRight = margin;
	}

	/**
	 * Set the margin at the top. margin is a length value expressed as a number
	 * followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 * 
	 * @param margin
	 */
	public void setMarginTop(final String margin) {
		this.sMarginTop = margin;
	}

	/**
	 * Set the page height. pageHeight is a length value expressed as a number
	 * followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 * When using this method, the paper format is set to
	 * PageStyle.STYLE_PAPERFORMAT_USER
	 * 
	 * @param pageHeight
	 */
	public void setPageHeight(final String pageHeight) {
		this.nPaperFormat = PageStyle.STYLE_PAPERFORMAT_USER;
		this.sPageHeight = pageHeight;
	}

	/**
	 * Set the page width. pageWidth is a length value expressed as a number
	 * followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 * When using this method, the paper format is set to
	 * PageStyle.STYLE_PAPERFORMAT_USER
	 * 
	 * @param pageWidth
	 */
	public void setPageWidth(final String pageWidth) {
		this.nPaperFormat = PageStyle.STYLE_PAPERFORMAT_USER;
		this.sPageWidth = pageWidth;
	}

	/**
	 * Set the paper format to one of<br>
	 * PageStyle.STYLE_PAPERFORMAT_A3<br>
	 * PageStyle.STYLE_PAPERFORMAT_A4<br>
	 * PageStyle.STYLE_PAPERFORMAT_A5<br>
	 * PageStyle.STYLE_PAPERFORMAT_LETTER<br>
	 * PageStyle.STYLE_PAPERFORMAT_LEGAL<br>
	 * PageStyle.STYLE_PAPERFORMAT_USER , automatically used if you use
	 * setPageHeight() or setPageWidth().
	 * 
	 * @param nPaperFormat
	 */
	public void setPaperFormat(final int nPaperFormat) {

		this.nPaperFormat = nPaperFormat;
		switch (nPaperFormat) {
		case STYLE_PAPERFORMAT_A3:
			this.sPageWidth = "42.0cm";
			this.sPageHeight = "29.7cm";
			break;
		case STYLE_PAPERFORMAT_A4:
			this.sPageWidth = "29.7cm";
			this.sPageHeight = "21.0cm";
			break;
		case STYLE_PAPERFORMAT_A5:
			this.sPageWidth = "21.0cm";
			this.sPageHeight = "14.8cm";
			break;
		case STYLE_PAPERFORMAT_LETTER:
			this.sPageWidth = "27.94cm";
			this.sPageHeight = "21.59cm";
			break;
		case STYLE_PAPERFORMAT_LEGAL:
			this.sPageWidth = "35.57cm";
			this.sPageHeight = "21.59cm";
			break;
		default:
			this.sPageWidth = "29.7cm";
			this.sPageHeight = "21.0cm";
			this.nPaperFormat = PageStyle.STYLE_PAPERFORMAT_A4;
		}
	}

	public void setPrintOrientationHorizontal() {
		this.nPrintOrientation = STYLE_PRINTORIENTATION_HORIZONTAL;
	}

	public void setPrintOrientationVertical() {
		this.nPrintOrientation = STYLE_PRINTORIENTATION_VERTICAL;
	}

	/**
	 * Set the writing mode to one of<br>
	 * STYLE_WRITINGMODE_LRTB lr-tb (left to right; top to bottom)<br>
	 * STYLE_WRITINGMODE_RLTB<br>
	 * STYLE_WRITINGMODE_TBRL<br>
	 * STYLE_WRITINGMODE_TBLR<br>
	 * STYLE_WRITINGMODE_LR<br>
	 * STYLE_WRITINGMODE_RL<br>
	 * STYLE_WRITINGMODE_TB<br>
	 * STYLE_WRITINGMODE_PAGE<br>
	 * 
	 * @param writingMode
	 */
	public void setWritingMode(final int writingMode) {
		this.nWritingMode = writingMode;
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
		StringBuilder sbTemp = new StringBuilder();
		if (this.nPrintOrientation == PageStyle.STYLE_PRINTORIENTATION_VERTICAL) {
			sbTemp.append("style:print-orientation=\"portrait\" ");
		} else {
			sbTemp.append("style:print-orientation=\"landscape\" ");
		}
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

	protected void addFooterStyle(final StringBuilder sbTemp) {
		final FooterHeader header = this.o.getStyles().getFooter();
		this.addFooterHeaderStyle(sbTemp, header, "style:footer-style");
	}

	protected void addHeaderStyle(final StringBuilder sbTemp) {
		final FooterHeader header = this.o.getStyles().getHeader();
		this.addFooterHeaderStyle(sbTemp, header, "style:header-style");
	}

	/**
	 * Return the master-style informations for this PageStyle.
	 * 
	 * @return The master style representation for styles.xml
	 */
	protected String toMasterStyleXML() {
		StringBuilder sbTemp = new StringBuilder();

		sbTemp.append("<style:master-page style:name=\"DefaultMasterPage\" ");
		sbTemp.append("style:page-layout-name=\"").append(this.getName())
				.append("\">");

		sbTemp.append("<style:header>");
		if (this.o.getStyles().getHeader() == null) {
			sbTemp.append("<text:p text:style-name=\"")
					.append(this.sTextStyleHeader).append("\">")
					.append(this.sTextHeader).append("</text:p>");
		} else {
			sbTemp.append(this.o.getStyles().getHeader().toMasterStyleXML());
		}
		sbTemp.append("</style:header>");

		sbTemp.append("<style:footer>");
		if (this.o.getStyles().getFooter() == null) {
			sbTemp.append("<text:p text:style-name=\"")
					.append(this.sTextStyleFooter).append("\">")
					.append(this.sTextFooter).append("</text:p>");
		} else {
			sbTemp.append(this.o.getStyles().getFooter().toMasterStyleXML());
		}
		sbTemp.append("</style:footer>");

		sbTemp.append("</style:master-page>");

		return sbTemp.toString();
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	protected String toXML() {
		StringBuilder sbTemp = new StringBuilder();

		sbTemp.append("<style:page-layout style:name=\"").append(this.getName())
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

		addHeaderStyle(sbTemp);
		addFooterStyle(sbTemp);
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

}
