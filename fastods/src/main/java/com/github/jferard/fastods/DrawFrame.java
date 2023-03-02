/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.SVGRectangle;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 10.4.2 draw:frame
 */
public class DrawFrame implements Shape {
    /**
     * Create a new builder
     *
     * @param name      the name of the frame
     * @param content   the content of the frame
     * @param rectangle the frame coordinates
     * @return the builder
     */
    public static DrawFrameBuilder builder(final String name, final FrameContent content,
                                           final SVGRectangle rectangle) {
        return new DrawFrameBuilder(name, content, rectangle);
    }

    private final FrameContent content;
    private final SVGRectangle rectangle;
    private final int zIndex;
    private final GraphicStyle drawStyle;
    private final TextStyle textStyle;
    private final String name;

    /**
     * @param name      the name of the frame
     * @param content   the content of the frame
     * @param rectangle the frame coordinates
     * @param zIndex    the z index
     * @param drawStyle a style of family graphic
     * @param textStyle the text style
     */
    DrawFrame(final String name, final FrameContent content, final SVGRectangle rectangle,
              final int zIndex, final GraphicStyle drawStyle, final TextStyle textStyle) {
        this.name = name;
        this.content = content;
        this.rectangle = rectangle;
        this.zIndex = zIndex;
        this.drawStyle = drawStyle;
        this.textStyle = textStyle;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<draw:frame");
        this.appendAttributes(util, appendable);
        appendable.append(">");
        this.content.appendXMLContent(util, appendable);
        appendable.append("</draw:frame>");
    }

    private void appendAttributes(final XMLUtil util, final Appendable appendable)
            throws IOException {
        util.appendAttribute(appendable, "draw:name", this.name);
        util.appendAttribute(appendable, "draw:z-index", this.zIndex);
        if (this.drawStyle != null) {
            util.appendAttribute(appendable, "draw:style-name", this.drawStyle.getName());
        }
        if (this.textStyle != null) {
            util.appendAttribute(appendable, "draw:text-style-name", this.textStyle.getName());
        }
        this.rectangle.appendXMLContent(util, appendable);
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        if (this.drawStyle != null) {
            stylesContainer.addContentStyle(this.drawStyle);
        }
        if (this.textStyle != null) {
            stylesContainer.addContentStyle(this.textStyle);
        }
    }
}
