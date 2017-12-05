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

import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.StylesEmbedder;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.Hidable;
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * OpenDocument 16.5 style:page-layout
 * OpenDocument 16.9 style:master-page
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class PageStyle implements AddableToOdsElements, StylesEmbedder, Hidable {
	/**
	 * The classic default master page name
	 */
	public static final String DEFAULT_MASTER_PAGE_NAME = "DefaultMasterPage";
	/**
	 * The default format (A4)
	 */
	public static final PaperFormat DEFAULT_FORMAT;
	/**
	 * The default print orientation (VERTICAL)
	 */
	public static final PrintOrientation DEFAULT_PRINT_ORIENTATION;
	/**
	 * The default writing mode (left to right and top to bottom)
	 */
	public static final WritingMode DEFAULT_WRITING_MODE;
	/**
	 * The default master page style
	 */
	public static final PageStyle DEFAULT_MASTER_PAGE_STYLE;
	/**
	 * The default style
	 */
	public static final PageStyle DEFAULT_PAGE_STYLE;

	@Override
	public boolean isHidden() {
		return this.hidden;
	}

	/**
	 * 20.325style:print-orientation
	 * The print orientation of the page (either landscape or portrait)
	 */
	public enum PrintOrientation {
		/**
		 * "a page is printed in landscape orientation"
		 */
		HORIZONTAL("landscape"),
        /**
		 * "a page is printed in portrait orientation"
		 */
		VERTICAL("portrait");

		private final String attrValue;

		/**
		 * @param attrValue landscape|portrait
		 */
		PrintOrientation(final String attrValue) {
			this.attrValue = attrValue;
		}

        /**
         * @return landscape|portrait
         */
        String getAttrValue() {
			return this.attrValue;
		}
	}

    /**
     * 20.394 style:writing-mode
     * see See §7.27.7 of [XSL] (https://www.w3.org/TR/2001/REC-xsl-20011015/slice7.html#writing-mode-related)
     */
    public enum WritingMode {
        /**
         * "Shorthand for lr-tb"
         */
        LR("lr"),
        /**
         * left to right then top to bottom
         */
        LRTB("lr-tb"),
        /**
         * page means inherit
         */
        PAGE("page"),
        /**
         * "Shorthand for rl-tb"
         */
        RL("rl"),
        /**
         * right to left then top to bottom
         */
        RLTB("rl-tb"),
        /**
         * "Shorthand for tb-rl"
         */
        TB("tb"),
        /**
         * top to bottom then left to right
         */
        TBLR("tb-lr"),
        /**
         * top to bottom then right to left
         */
        TBRL("tb-rl");

		private final String attrValue;

        /**
         * @param attrValue the value See §7.27.7 of [XSL]
         */
        WritingMode(final String attrValue) {
			this.attrValue = attrValue;
		}

        /**
         * @return the value See §7.27.7 of [XSL]
         */
        String getAttrValue() {
			return this.attrValue;
		}

	}


	static {
		DEFAULT_FORMAT = PaperFormat.A4;
		DEFAULT_WRITING_MODE = WritingMode.LRTB;
		DEFAULT_PRINT_ORIENTATION = PrintOrientation.VERTICAL;
		DEFAULT_PAGE_STYLE = PageStyle.builder("Mpm1").build();
		DEFAULT_MASTER_PAGE_STYLE = PageStyle
				.builder(PageStyle.DEFAULT_MASTER_PAGE_NAME).build();
	}

	private final boolean hidden;
	private final MasterPageStyle masterPageStyle;
	private final PageLayoutStyle pageLayoutStyle;

	/**
	 * Create a new page style.
	 *
     * @param hidden if the page style is hidden (automatic)
	 * @param masterPageStyle the master page style
	 * @param pageLayoutStyle the page layout style
	 */
	PageStyle(final boolean hidden, final MasterPageStyle masterPageStyle, final PageLayoutStyle pageLayoutStyle) {
		this.hidden = hidden;
		this.masterPageStyle = masterPageStyle;
		this.pageLayoutStyle = pageLayoutStyle;
	}

    /**
     * Create a new builder
     * @param name the name of the style to build
     * @return the builder
     */
    public static PageStyleBuilder builder(final String name) {
		return new PageStyleBuilder(name);
	}

    /**
     * @return the contained master style
     */
    public MasterPageStyle getMasterPageStyle() {
		return this.masterPageStyle;
	}

    /**
     * @return the contained page layout
     */
    public PageLayoutStyle getPageLayoutStyle() {
		return this.pageLayoutStyle;
	}

    /**
     * @return the name of the contained master style
     */
    public String getMasterName() {
		return this.masterPageStyle.getName();
	}

	@Override
    public void addEmbeddedStylesToStylesElement(
			final StylesContainer stylesContainer) {
		this.masterPageStyle.addEmbeddedStylesToStylesContainer(stylesContainer);
	}

    @Override
    public void addEmbeddedStylesToStylesElement(
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
}
