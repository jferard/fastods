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

import com.github.jferard.fastods.PageStyle.PaperFormat;
import com.github.jferard.fastods.PageStyle.PrintOrientation;
import com.github.jferard.fastods.PageStyle.WritingMode;

/**
 * @author Julien Férard
 *
 *         Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin Schulz
 *         <mtschulz at users.sourceforge.net>
 *
 *         This file PageStyleBuilder.java is part of FastODS.
 *
 */
class PageStyleBuilder {
	private FooterHeader footer;
	private FooterHeader header;
	private PaperFormat paperFormat;
	private PrintOrientation printOrientation;
	private String sBackgroundColor;

	private String sMarginBottom;
	private String sMarginLeft;
	private String sMarginRight;
	private String sMarginTop;

	private final String sName;
	private final String sNumFormat;
	private String sPageHeight;
	private String sPageWidth;

	private WritingMode writingMode;

	/**
	 * Create a new page style.
	 *
	 */
	public PageStyleBuilder(final String sName) {
		if (sName == null)
			throw new IllegalStateException();

		this.sName = sName;
		this.sMarginTop = "1.5cm";
		this.sMarginBottom = "1.5cm";
		this.sMarginLeft = "1.5cm";
		this.sMarginRight = "1.5cm";

		this.paperFormat(PageStyle.DEFAULT_FORMAT);
		this.sNumFormat = "1";
		this.sBackgroundColor = "";

		this.printOrientation = PageStyle.DEFAULT_PRINTORIENTATION;
		this.writingMode = PageStyle.DEFAULT_WRITINGMODE;

		final FHTextStyle noneStyle = FHTextStyle.builder("none").build();
		this.header = FooterHeader
				.simpleHeader(noneStyle, "");
		this.footer = FooterHeader
				.simpleFooter(noneStyle, "");
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

	public PageStyle build() {
		// TODO : create MarginAttribute and use a
		// EnumMap<MarginAtribute.Position, MarginAttribute>
		return new PageStyle(this.sName, this.sMarginTop, this.sMarginBottom,
				this.sMarginLeft, this.sMarginRight, this.sPageWidth,
				this.sPageHeight, this.sNumFormat, this.sBackgroundColor,
				this.footer, this.header, this.printOrientation,
				this.paperFormat, this.writingMode);
	}

	public PageStyleBuilder footer(final FooterHeader footer) {
		this.footer = footer;
		return this;
	}

	public PageStyleBuilder header(final FooterHeader header) {
		this.header = header;
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
		this.paperFormat = PageStyle.PaperFormat.USER;
		this.sPageHeight = pageHeight;
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
	 * @param paperFormat
	 * @return this for fluent style
	 */
	public final PageStyleBuilder paperFormat(final PaperFormat paperFormat) {
		this.paperFormat = paperFormat;
		this.sPageWidth = paperFormat.getHeight();
		this.sPageHeight = paperFormat.getWidth();
		return this;
	}

	public PageStyleBuilder printOrientationHorizontal() {
		this.printOrientation = PageStyle.PrintOrientation.HORIZONTAL;
		return this;
	}

	public PageStyleBuilder printOrientationVertical() {
		this.printOrientation = PageStyle.PrintOrientation.VERTICAL;
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
		this.paperFormat = PageStyle.PaperFormat.USER;
		this.sPageWidth = pageWidth;
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
	public PageStyleBuilder writingMode(final WritingMode writingMode) {
		this.writingMode = writingMode;
		return this;
	}
}
