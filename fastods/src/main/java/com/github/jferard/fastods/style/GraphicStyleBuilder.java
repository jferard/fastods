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

import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.util.StyleBuilder;

/**
 * 16.37 Graphic Styles
 */
public class GraphicStyleBuilder
        implements StyleBuilder<GraphicStyle>, ShowableBuilder<GraphicStyleBuilder> {
    private final String name;
    private boolean hidden;
    private DrawFillBitmap fillImage;
    private DrawFillGradient gradient;
    private Color color;
    private DrawFillHatch hatch;
    private DrawFill drawFill;

    /**
     * @param name the name of the style
     */
    GraphicStyleBuilder(final String name) {
        this.name = name;
        this.hidden = true;
    }

    /**
     * @param drawFill the fill style
     * @return this for fluent style
     */
    public GraphicStyleBuilder drawFill(final DrawFill drawFill) {
        this.drawFill = drawFill;
        return this;
    }

    @Override
    public GraphicStyleBuilder visible() {
        this.hidden = false;
        return this;
    }

    @Override
    public GraphicStyle build() {
        return new GraphicStyle(this.name, this.hidden, this.drawFill);
    }
}
