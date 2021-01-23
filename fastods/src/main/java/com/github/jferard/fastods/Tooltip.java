/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.util.SVGRectangle;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * @author Julien Férard
 */
public class Tooltip implements XMLConvertible, ElementWithEmbeddedStyles {
    /**
     * @param text the escaped test
     * @return the new tooltip parameter
     */
    public static TooltipBuilder builder(final XMLUtil xmlUtil, final String text) {
        String escapedXMLContent = xmlUtil.escapeXMLContent(text);
        if (escapedXMLContent.contains("\n")) {
            escapedXMLContent = escapedXMLContent.replaceAll("\r?\n", "</text:p><text:p>");
        }

        return new TooltipBuilder(escapedXMLContent);
    }

    private final boolean visible;
    private final GraphicStyle graphicStyle;
    private final String text;
    private final SVGRectangle rectangle;

    /**
     * Create a new tooltip parameter
     *
     * @param rectangle the tooltip coordinates
     * @param visible   true if the tooltip is visible
     */
    Tooltip(final String text, final SVGRectangle rectangle, final boolean visible,
            final GraphicStyle graphicStyle) {
        this.text = text;
        this.rectangle = rectangle;
        this.visible = visible;
        this.graphicStyle = graphicStyle;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<office:annotation");
        if (this.visible) {
            util.appendAttribute(appendable, "office:display", this.visible);
        }
        if (this.rectangle != null) {
            this.rectangle.appendXMLContent(util, appendable);
        }
        if (this.graphicStyle != null) {
            util.appendAttribute(appendable, "draw:style-name", this.graphicStyle.getName());
        }
        appendable.append("><text:p>").append(this.text).append("</text:p></office:annotation>");
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        if (this.graphicStyle != null) {
            this.graphicStyle.addEmbeddedStyles(stylesContainer);
            stylesContainer.addContentStyle(this.graphicStyle);
        }
    }
}
