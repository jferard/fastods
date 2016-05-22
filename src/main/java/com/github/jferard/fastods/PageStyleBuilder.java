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
public class PageStyleBuilder {

	private static final String LEGAL_WIDTH = "21.59cm";
	private static final String A5_HEIGHT = "21.0cm";
	private static final String A4_HEIGHT = "29.7cm";

	private String sName;
	private String sMarginTop;
	private String sMarginBottom;
	private String sMarginLeft;
	private String sMarginRight;

	private String sPageWidth;
	private String sPageHeight;
	private String sNumFormat;
	private String sBackgroundColor;

	private String sTextStyleFooter;
	private String sTextStyleHeader;
	private String sTextHeader;
	private String sTextFooter;

	private int nPrintOrientation;
	private int nPaperFormat;
	private int nWritingMode;
	private FooterHeader header;
	private FooterHeader footer;

	/**
	 * Create a new page style.
	 * 
	 */
	public PageStyleBuilder() {
		this.sMarginTop = "1.5cm";
		this.sMarginBottom = "1.5cm";
		this.sMarginLeft = "1.5cm";
		this.sMarginRight = "1.5cm";

		this.sPageWidth = A4_HEIGHT;
		this.sPageHeight = A5_HEIGHT;
		this.sNumFormat = "1";
		this.sBackgroundColor = "";

		this.sTextStyleFooter = "";
		this.sTextStyleHeader = "";
		this.sTextHeader = "";
		this.sTextFooter = "";

		this.nPrintOrientation = PageStyle.STYLE_PRINTORIENTATION_VERTICAL;
		this.nPaperFormat = PageStyle.STYLE_PAPERFORMAT_A4;
		this.nWritingMode = PageStyle.STYLE_WRITINGMODE_LRTB;
	}
	
	public PageStyleBuilder header(FooterHeader header) {
		this.header = header;
		return this;
	}
	
	public PageStyleBuilder footer(FooterHeader footer) {
		this.footer = footer;
		return this;
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
	 * @return this for fluent style
	 */
	public PageStyleBuilder allMargins(final String margin) {
		this.marginTop(margin);
		this.marginBottom(margin);
		this.marginLeft(margin);
		this.marginRight(margin);
		return this;
	}

	/**
	 * Set the background color to sColor, a six-digit hex value. Example:
	 * #aa32f0.<br>
	 * The background color may also be set to 'transparent' if a background
	 * image is used (currently unsupported).
	 * 
	 * @param sColor
	 * @return this for fluent style
	 */
	public PageStyleBuilder backgroundColor(final String sColor) {
		this.sBackgroundColor = sColor;
		return this;
	}

	/**
	 * Set the margin at the bottom. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 * 
	 * @param margin
	 * @return this for fluent style
	 */
	public PageStyleBuilder marginBottom(final String margin) {
		this.sMarginBottom = margin;
		return this;
	}

	/**
	 * Set the margin at the left. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 * 
	 * @param margin
	 * @return this for fluent style
	 */
	public PageStyleBuilder marginLeft(final String margin) {
		this.sMarginLeft = margin;
		return this;
	}

	/**
	 * Set the margin at the right. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 * 
	 * @param margin
	 * @return this for fluent style
	 */
	public PageStyleBuilder marginRight(final String margin) {
		this.sMarginRight = margin;
		return this;
	}

	/**
	 * Set the margin at the top. margin is a length value expressed as a number
	 * followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 * 
	 * @param margin
	 * @return this for fluent style
	 */
	public PageStyleBuilder marginTop(final String margin) {
		this.sMarginTop = margin;
		return this;
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
	 * @return this for fluent style
	 */
	public PageStyleBuilder pageHeight(final String pageHeight) {
		this.nPaperFormat = PageStyle.STYLE_PAPERFORMAT_USER;
		this.sPageHeight = pageHeight;
		return this;
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
	 * @return this for fluent style
	 */
	public PageStyleBuilder setPageWidth(final String pageWidth) {
		this.nPaperFormat = PageStyle.STYLE_PAPERFORMAT_USER;
		this.sPageWidth = pageWidth;
		return this;
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
	 * @return this for fluent style
	 */
	public PageStyleBuilder paperFormat(final int nPaperFormat) {
		this.nPaperFormat = nPaperFormat;
		switch (nPaperFormat) {
		case PageStyle.STYLE_PAPERFORMAT_A3:
			this.sPageWidth = "42.0cm";
			this.sPageHeight = A4_HEIGHT;
			break;
		case PageStyle.STYLE_PAPERFORMAT_A4:
			this.sPageWidth = A4_HEIGHT;
			this.sPageHeight = A5_HEIGHT;
			break;
		case PageStyle.STYLE_PAPERFORMAT_A5:
			this.sPageWidth = A5_HEIGHT;
			this.sPageHeight = "14.8cm";
			break;
		case PageStyle.STYLE_PAPERFORMAT_LETTER:
			this.sPageWidth = "27.94cm";
			this.sPageHeight = LEGAL_WIDTH;
			break;
		case PageStyle.STYLE_PAPERFORMAT_LEGAL:
			this.sPageWidth = "35.57cm";
			this.sPageHeight = LEGAL_WIDTH;
			break;
		default:
			this.sPageWidth = A4_HEIGHT;
			this.sPageHeight = A5_HEIGHT;
			this.nPaperFormat = PageStyle.STYLE_PAPERFORMAT_A4;
		}
		return this;
	}

	public PageStyleBuilder printOrientationHorizontal() {
		this.nPrintOrientation = PageStyle.STYLE_PRINTORIENTATION_HORIZONTAL;
		return this;
	}

	public PageStyleBuilder printOrientationVertical() {
		this.nPrintOrientation = PageStyle.STYLE_PRINTORIENTATION_VERTICAL;
		return this;
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
	 * @return
	 */
	public PageStyleBuilder writingMode(final int writingMode) {
		this.nWritingMode = writingMode;
		return this;
	}

	public PageStyle build() {
		return new PageStyle(this.sName, this.sMarginTop, this.sMarginBottom,
				this.sMarginLeft, this.sMarginRight, this.sPageWidth,
				this.sPageHeight, this.sNumFormat, this.sBackgroundColor, this.footer,
				this.sTextStyleFooter, this.header, this.sTextStyleHeader, this.sTextHeader,
				this.sTextFooter, this.nPrintOrientation, this.nPaperFormat,
				this.nWritingMode);
	}

	public PageStyleBuilder name(String sName) {
		this.sName = sName;
		return this;
	}
}
