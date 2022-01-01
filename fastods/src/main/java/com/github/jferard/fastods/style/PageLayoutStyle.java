/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.PageSection;
import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.attribute.PageCentering;
import com.github.jferard.fastods.attribute.PagePrintOrientation;
import com.github.jferard.fastods.attribute.PageWritingMode;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.Hidable;
import com.github.jferard.fastods.util.StringUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.List;

/**
 * OpenDocument 16.5 style:page-layout
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class PageLayoutStyle implements AddableToOdsElements, Hidable {
    private final Color backgroundColor;
    private final Footer footer;
    private final Header header;
    private final Margins margins;

    private final String name;
    private final String numFormat;
    private final Length pageHeight;

    private final Length pageWidth;

    private final PagePrintOrientation printOrientation;
    private final PageWritingMode writingMode;
    private final int scaleTo;
    private final int scaleToPages;
    private final int scaleToX;
    private final int scaleToY;
    private final PageCentering centering;
    private final List<PrintComponent> printComponents;

    /**
     * Create a new page style. Version 0.5.0 Added parameter NamedOdsDocument o
     *
     * @param name             A unique name for this style
     * @param margins          the margins of the page
     * @param pageWidth        the width of the page
     * @param pageHeight       the height of the page
     * @param numFormat        the format of the sequence of page numbers (20.314 style:num-format)
     * @param backgroundColor  the color of the background, as an hexadecimal number
     * @param header           the header for this style
     * @param footer           the footer for this style
     * @param printOrientation the print orientation
     * @param printComponents  the components to print
     * @param writingMode      the writing mode
     * @param scaleTo          a percentage for print scale
     * @param scaleToPages     a number of pages for print scale or 0
     * @param scaleToX         a number of horizontal pages for print scale or 0
     * @param scaleToY         a number of vertical pages for print scale or 0
     * @param centering        center the table in the page if true
     */
    PageLayoutStyle(final String name, final Margins margins, final Length pageWidth,
                    final Length pageHeight, final String numFormat, final Color backgroundColor,
                    final Header header, final Footer footer,
                    final PagePrintOrientation printOrientation,
                    final List<PrintComponent> printComponents, final PageWritingMode writingMode,
                    final int scaleTo, final int scaleToPages, final int scaleToX,
                    final int scaleToY, final PageCentering centering) {
        this.name = name;
        this.margins = margins;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        this.numFormat = numFormat;
        this.backgroundColor = backgroundColor;
        this.footer = footer;
        this.header = header;
        this.printOrientation = printOrientation;
        this.printComponents = printComponents;
        this.writingMode = writingMode;
        this.scaleTo = scaleTo;
        this.scaleToPages = scaleToPages;
        this.scaleToX = scaleToX;
        this.scaleToY = scaleToY;
        this.centering = centering;
    }

    @Override
    public void addToElements(final OdsElements odsElements) {
        odsElements.addPageLayoutStyle(this);
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
        appendable.append("<style:page-layout");
        util.appendEAttribute(appendable, "style:name", this.name);
        appendable.append("><style:page-layout-properties");
        util.appendAttribute(appendable, "fo:page-width", this.pageWidth);
        util.appendAttribute(appendable, "fo:page-height", this.pageHeight);
        if (this.scaleTo != 100) {
            util.appendAttribute(appendable, "style:scale-to", this.scaleTo + "%");
        }
        if (this.scaleToPages != 0) {
            util.appendAttribute(appendable, "style:scale-to-pages", this.scaleToPages);
        }
        if (this.scaleToX != 0) {
            util.appendAttribute(appendable, "style:scale-to-X", this.scaleToX);
            util.appendAttribute(appendable, "loext:scale-to-X", this.scaleToX);
        }
        if (this.scaleToY != 0) {
            util.appendAttribute(appendable, "style:scale-to-Y", this.scaleToY);
            util.appendAttribute(appendable, "loext:scale-to-Y", this.scaleToY);
        }
        if (this.centering != PageCentering.NONE) {
            util.appendAttribute(appendable, "style:table-centering", this.centering);
        }

        util.appendEAttribute(appendable, "style:num-format", this.numFormat);
        util.appendAttribute(appendable, "style:writing-mode", this.writingMode);
        util.appendAttribute(appendable, "style:print-orientation", this.printOrientation);
        util.appendAttribute(appendable, "style:print", StringUtil.join(" ", this.printComponents));
        this.appendBackgroundColor(util, appendable);
        this.margins.appendXMLContent(util, appendable);
        appendable.append("/>"); // End of page-layout-properties

        PageSection.appendPageSectionStyleXMLToAutomaticStyle(this.header, util, appendable);
        PageSection.appendPageSectionStyleXMLToAutomaticStyle(this.footer, util, appendable);
        appendable.append("</style:page-layout>");
    }

    /**
     * Get the name of this page style.
     *
     * @return The page style name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the writing mode
     *
     * @return The current writing mode.
     */
    public PageWritingMode getWritingMode() {
        return this.writingMode;
    }

    private void appendBackgroundColor(final XMLUtil util, final Appendable appendable)
            throws IOException {
        if (this.backgroundColor != SimpleColor.NONE) {
            util.appendAttribute(appendable, "fo:background-color", this.backgroundColor);
        }
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
