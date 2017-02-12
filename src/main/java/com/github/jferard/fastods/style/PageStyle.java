/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.FooterHeader;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * OpenDocument 16.5 style:page-layout
 * OpenDocument 16.9 style:master-page
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class PageStyle implements AddableToOdsElements {
	public static final PaperFormat DEFAULT_FORMAT = PaperFormat.A4;
	public static final String DEFAULT_MASTER_PAGE = "DefaultMasterPage";
	public static final PageStyle DEFAULT_MASTER_PAGE_STYLE;
	public static final PageStyle DEFAULT_PAGE_STYLE;
	public static final PrintOrientation DEFAULT_PRINTORIENTATION = PrintOrientation.VERTICAL;
	public static final WritingMode DEFAULT_WRITING_MODE = WritingMode.LRTB;
	private static final String A3_H = "42.0cm";
	private static final String A3_W = "29.7cm";
	private static final String A4_W = "21.0cm";
	private static final String A5_W = "14.8cm";
	private static final String LEGAL_H = "35.57cm";
	private static final String LETTER_H = "27.94cm";
	private static final String LETTER_W = "21.59cm";

	static {
		DEFAULT_PAGE_STYLE = PageStyle.builder("Mpm1").build();
		DEFAULT_MASTER_PAGE_STYLE = PageStyle
				.builder(PageStyle.DEFAULT_MASTER_PAGE).build();
	}

	private final MasterPageStyle masterPageStyle;
	private final PageLayoutStyle pageLayoutStyle;

	/**
	 * Create a new page style.
	 *
	 * @param masterPageStyle the master page style
	 * @param pageLayoutStyle the page layout style
	 */
	PageStyle(final MasterPageStyle masterPageStyle, final PageLayoutStyle pageLayoutStyle) {
		this.masterPageStyle = masterPageStyle;
		this.pageLayoutStyle = pageLayoutStyle;
	}

	public static PageStyleBuilder builder(final String name) {
		return new PageStyleBuilder(name);
	}

	public MasterPageStyle getMasterPageStyle() {
		return this.masterPageStyle;
	}

	public PageLayoutStyle getPageLayoutStyle() {
		return this.pageLayoutStyle;
	}

	public String getMasterName() {
		return this.masterPageStyle.getName();
	}

	public void addEmbeddedStylesToStylesEntry(
			final StylesContainer stylesContainer) {
		this.masterPageStyle.addEmbeddedStylesToStylesContainer(stylesContainer);
	}

	public void addEmbeddedStylesToStylesEntry(
			final StylesContainer stylesContainer, final Mode mode) {
		this.masterPageStyle.addEmbeddedStylesToStylesContainer(stylesContainer, mode);
	}

	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addMasterPageStyle(this.masterPageStyle);
		odsElements.addPageLayoutStyle(this.pageLayoutStyle);
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @param util       a util to write XML
	 * @param appendable where to write
	 * @throws IOException If an I/O error occurs
	 */
	public void appendXMLToAutomaticStyle(final XMLUtil util,
										  final Appendable appendable) throws IOException {
		this.pageLayoutStyle.appendXMLToAutomaticStyle(util, appendable);
	}

	/**
	 * @param util       a util to write XML
	 * @param appendable where to write
	 * @throws IOException If an I/O error occurs
	 */
	public void appendXMLToMasterStyle(final XMLUtil util,
									   final Appendable appendable) throws IOException {
		this.masterPageStyle.appendXMLToMasterStyle(util, appendable);
	}

	public String getBackgroundColor() {
		return this.pageLayoutStyle.getBackgroundColor();
	}

	public FooterHeader getFooter() {
		return this.masterPageStyle.getFooter();
	}

	public FooterHeader getHeader() {
		return this.masterPageStyle.getHeader();
	}

	public Margins getMargins() {
		return this.pageLayoutStyle.getMargins();
	}

	public String getPageHeight() {
		return this.pageLayoutStyle.getPageHeight();
	}

	public String getPageWidth() {
		return this.pageLayoutStyle.getPageWidth();
	}

	/**
	 * Get the paper format as one of PageStyle.STYLE_PAPERFORMAT_*.
	 * @return the format of the paper
	 */
	public PaperFormat getPaperFormat() {
		return this.pageLayoutStyle.getPaperFormat();
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
		return this.pageLayoutStyle.getWritingMode();
	}

	public static enum PaperFormat {
		A3(PageStyle.A3_H, PageStyle.A3_W), A4(PageStyle.A3_W,
				PageStyle.A4_W), A5(PageStyle.A4_W,
				PageStyle.A5_W), LEGAL(PageStyle.LEGAL_H,
				PageStyle.LETTER_W), LETTER(
				PageStyle.LETTER_H,
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
}
