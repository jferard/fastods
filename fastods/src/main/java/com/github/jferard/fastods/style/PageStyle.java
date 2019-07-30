/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.StyleWithEmbeddedStyles;
import com.github.jferard.fastods.attribute.PagePrintOrientation;
import com.github.jferard.fastods.attribute.PageWritingMode;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.Hidable;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * OpenDocument 16.5 style:page-layout
 * OpenDocument 16.9 style:master-page
 * <p>
 * The PageStyle class represents a combination of a master page style and a page layout style.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class PageStyle implements AddableToOdsElements, StyleWithEmbeddedStyles, Hidable {
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
    public static final PagePrintOrientation DEFAULT_PRINT_ORIENTATION;
    /**
     * The default writing mode (left to right and top to bottom)
     */
    public static final PageWritingMode DEFAULT_WRITING_MODE;
    /**
     * The default master page style
     */
    public static final PageStyle DEFAULT_MASTER_PAGE_STYLE;
    /**
     * The default style
     */
    public static final PageStyle DEFAULT_PAGE_STYLE;

    /**
     * Create a new builder
     *
     * @param name the name of the style to build
     * @return the builder
     */
    public static PageStyleBuilder builder(final String name) {
        return new PageStyleBuilder(name);
    }

    static {
        DEFAULT_FORMAT = PaperFormat.A4;
        DEFAULT_WRITING_MODE = PageWritingMode.LRTB;
        DEFAULT_PRINT_ORIENTATION = PagePrintOrientation.VERTICAL;
        DEFAULT_PAGE_STYLE = PageStyle.builder("Mpm1").build();
        DEFAULT_MASTER_PAGE_STYLE = PageStyle.builder(PageStyle.DEFAULT_MASTER_PAGE_NAME).build();
    }

    private final boolean hidden;
    private final MasterPageStyle masterPageStyle;
    private final PageLayoutStyle pageLayoutStyle;
    /**
     * Create a new page style.
     *
     * @param hidden          if the page style is hidden (automatic)
     * @param masterPageStyle the master page style
     * @param pageLayoutStyle the page layout style
     */
    PageStyle(final boolean hidden, final MasterPageStyle masterPageStyle,
              final PageLayoutStyle pageLayoutStyle) {
        this.hidden = hidden;
        this.masterPageStyle = masterPageStyle;
        this.pageLayoutStyle = pageLayoutStyle;
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
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
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        this.masterPageStyle.addEmbeddedStyles(stylesContainer);
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
    public void appendXMLToAutomaticStyle(final XMLUtil util, final Appendable appendable)
            throws IOException {
        this.pageLayoutStyle.appendXMLToAutomaticStyle(util, appendable);
    }

    /**
     * @param util       a util to write XML
     * @param appendable where to write
     * @throws IOException If an I/O error occurs
     */
    public void appendXMLToMasterStyle(final XMLUtil util, final Appendable appendable)
            throws IOException {
        this.masterPageStyle.appendXMLToMasterStyle(util, appendable);
    }


}
