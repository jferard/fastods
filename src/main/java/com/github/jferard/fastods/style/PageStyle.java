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

import com.github.jferard.fastods.FooterHeader;
import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file PageStyle.java is part of FastODS.
 *
 *         WHERE ?
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page
 * 
 * @see 16.5 <style:page-layout>
 * @see 16.9 <style:master-page>
 */
public class PageStyle {
	public static enum PaperFormat {
		A3(PageStyle.A3_H, PageStyle.A3_W), A4(PageStyle.A3_W,
				PageStyle.A4_W), A5(PageStyle.A4_W, PageStyle.A5_W), LEGAL(
						PageStyle.LEGAL_H,
						PageStyle.LETTER_W), LETTER(PageStyle.LETTER_H,
								PageStyle.LETTER_W), USER("", "");

		private final String height;
		private final String width;

		private PaperFormat(final String height, final String width) {
			this.height = height;
			this.width = width;

		}

		String getHeight() {
			return this.height;
		}

		String getWidth() {
			return this.width;
		}
	}

	public static enum PrintOrientation {
		HORIZONTAL("landscape"), VERTICAL("portrait");

		private final String attrValue;

		private PrintOrientation(final String attrValue) {
			this.attrValue = attrValue;
		}

		String getAttrValue() {
			return this.attrValue;
		}
	}

	public static enum WritingMode {
		LR("lr"), LRTB("lr-tb"), PAGE("page"), RL("rl"), RLTB("rl-tb"), TB(
				"tb"), TBLR("tb_lr"), TBRL("tb-rl");

		private final String attrValue;

		private WritingMode(final String attrValue) {
			this.attrValue = attrValue;
		}

		String getAttrValue() {
			return this.attrValue;
		}

	}

	public static final PaperFormat DEFAULT_FORMAT = PaperFormat.A4;

	public static final String DEFAULT_MASTER_PAGE = "DefaultMasterPage";
	public static final WritingMode DEFAULT_WRITING_MODE = WritingMode.LRTB;
	public static final PrintOrientation DEFAULT_PRINTORIENTATION = PrintOrientation.VERTICAL;

	private static final String A3_H = "42.0cm";

	private static final String A3_W = "29.7cm";

	private static final String A4_W = "21.0cm";

	private static final String A5_W = "14.8cm";

	private static final String LEGAL_H = "35.57cm";

	private static final String LETTER_H = "27.94cm";
	private static final String LETTER_W = "21.59cm";

	public static final PageStyle DEFAULT_PAGE_STYLE = PageStyle.builder("Mpm1")
			.build();
	
	public static PageStyleBuilder builder(final String name) {
		return new PageStyleBuilder(name);
	}

	private static void appendFooterHeaderStyle(final XMLUtil util,
			final Appendable appendable, final FooterHeader footerHeader,
			final String tag) throws IOException {
		if (footerHeader == null)
			appendable.append("<").append(tag).append("/>");
		else
			footerHeader.appendXMLToAutomaticStyle(util, appendable);
	}

	private final FooterHeader footer;
	private final FooterHeader header;

	private final PaperFormat paperFormat;
	private final PrintOrientation printOrientation;
	private final String sBackgroundColor;
	private final String sMarginBottom;

	private final String sMarginLeft;
	private final String sMarginRight;
	private final String sMarginTop;
	private final String name;

	private final String sNumFormat;
	private final String sPageHeight;

	private final String sPageWidth;

	private final WritingMode writingMode;

	/**
	 * Create a new page style. Version 0.5.0 Added parameter OdsFile o
	 *
	 * @param name
	 *            A unique name for this style
	 * @param header2
	 * @param footer
	 */
	public PageStyle(final String name, final String sMarginTop,
			final String sMarginBottom, final String sMarginLeft,
			final String sMarginRight, final String sPageWidth,
			final String sPageHeight, final String sNumFormat,
			final String sBackgroundColor, final FooterHeader footer,
			final FooterHeader header, final PrintOrientation printOrientation,
			final PaperFormat paperFormat, final WritingMode writingMode) {
		this.name = name;
		this.sMarginTop = sMarginTop;
		this.sMarginBottom = sMarginBottom;
		this.sMarginLeft = sMarginLeft;
		this.sMarginRight = sMarginRight;
		this.sPageWidth = sPageWidth;
		this.sPageHeight = sPageHeight;
		this.sNumFormat = sNumFormat;
		this.sBackgroundColor = sBackgroundColor;
		this.footer = footer;
		this.header = header;
		this.printOrientation = printOrientation;
		this.paperFormat = paperFormat;
		this.writingMode = writingMode;
	}

	public void addToFile(final OdsFile odsFile) {
		odsFile.addPageStyle(this);
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 */
	public void appendXMLToAutomaticStyle(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:page-layout");
		util.appendAttribute(appendable, "style:name", this.name);
		appendable.append("><style:page-layout-properties");
		util.appendAttribute(appendable, "fo:page-width", this.sPageWidth);
		util.appendAttribute(appendable, "fo:page-height", this.sPageHeight);
		util.appendAttribute(appendable, "style:num-format", this.sNumFormat);
		util.appendEAttribute(appendable, "style:writing-mode",
				this.writingMode.getAttrValue());
		util.appendEAttribute(appendable, "style:print-orientation",
				this.printOrientation.getAttrValue());
		this.appendBackgroundColor(util, appendable);
		util.appendAttribute(appendable, "fo:margin-top", this.sMarginTop);
		util.appendAttribute(appendable, "fo:margin-bottom",
				this.sMarginBottom);
		util.appendAttribute(appendable, "fo:margin-left", this.sMarginLeft);
		util.appendAttribute(appendable, "fo:margin-right", this.sMarginRight);
		appendable.append("/>"); // End of page-layout-properties

		PageStyle.appendFooterHeaderStyle(util, appendable, this.getHeader(),
				"style:header-style");
		PageStyle.appendFooterHeaderStyle(util, appendable, this.getFooter(),
				"style:footer-style");
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

	/**
	 * Return the master-style informations for this PageStyle.
	 *
	 * @return The master style representation for styles.xml
	 * @throws IOException
	 */
	public void appendXMLToMasterStyle(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:master-page");
		util.appendEAttribute(appendable, "style:name",
				PageStyle.DEFAULT_MASTER_PAGE);
		util.appendAttribute(appendable, "style:page-layout-name", this.name);
		appendable.append("><style:header>");
		this.getHeader().appendXMLToMasterStyle(util, appendable);
		appendable.append("</style:header>");
		appendable.append("<style:header-left");
		util.appendEAttribute(appendable, "style:display", false);
		appendable.append("/>");
		appendable.append("<style:footer>");
		this.getFooter().appendXMLToMasterStyle(util, appendable);
		appendable.append("</style:footer>");
		appendable.append("<style:footer-left");
		util.appendEAttribute(appendable, "style:display", false);
		appendable.append("/>");
		appendable.append("</style:master-page>");
	}

	public String getBackgroundColor() {
		return this.sBackgroundColor;
	}

	public FooterHeader getFooter() {
		return this.footer;
	}

	public FooterHeader getHeader() {
		return this.header;
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
		return this.name;
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
	public PaperFormat getPaperFormat() {
		return this.paperFormat;
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
	public WritingMode getWritingMode() {
		return this.writingMode;
	}

	private void appendBackgroundColor(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.getBackgroundColor().length() > 0) {
			util.appendAttribute(appendable, "fo:background-color",
					this.sBackgroundColor);
		}
	}
}
