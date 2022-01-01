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

package com.github.jferard.fastods;

import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.SVGRectangle;

/**
 * A builder for DrawFrame class
 */
public class DrawFrameBuilder {
    private final String name;
    private final FrameContent content;
    private final SVGRectangle rectangle;
    private TextStyle textStyle;
    private GraphicStyle drawStyle;
    private int zIndex;

    /**
     * @param name      the name of the frame
     * @param content   the content of the frame
     * @param rectangle the frame coordinates
     */
    DrawFrameBuilder(final String name, final FrameContent content, final SVGRectangle rectangle) {
        this.name = name;
        this.content = content;
        this.rectangle = rectangle;
        this.zIndex = 0;
        this.drawStyle = null;
        this.textStyle = null;
    }

    /**
     * @param zIndex the new z index
     * @return this for fluent style
     */
    public DrawFrameBuilder zIndex(final int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    /**
     * @param drawStyle the style of the frame
     * @return this for fluent style
     */
    public DrawFrameBuilder style(final GraphicStyle drawStyle) {
        this.drawStyle = drawStyle;
        return this;
    }

    /**
     * @param textStyle the style of the text
     * @return this for fluent style
     */
    public DrawFrameBuilder textStyle(final TextStyle textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    /**
     * @return the frame
     */
    public DrawFrame build() {
        return new DrawFrame(this.name, this.content, this.rectangle, this.zIndex, this.drawStyle,
                this.textStyle);
    }
}
