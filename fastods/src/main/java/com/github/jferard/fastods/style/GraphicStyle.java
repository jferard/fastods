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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.ElementWithEmbeddedStyles;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 16.37 Graphic Styles
 */
public class GraphicStyle implements ObjectStyle, ElementWithEmbeddedStyles {
    public static GraphicStyleBuilder builder(final String name) {
        return new GraphicStyleBuilder(name);
    }

    private final String name;
    private final boolean hidden;
    private final DrawFill drawFill;
    private String key;

    public GraphicStyle(final String name, final boolean hidden, final DrawFill drawFill) {
        this.name = name;
        this.hidden = hidden;
        this.drawFill = drawFill;
    }

    @Override
    public ObjectStyleFamily getFamily() {
        return ObjectStyleFamily.GRAPHIC;
    }

    @Override
    public String getKey() {
        if (this.key == null) {
            this.key = this.getFamily() + "@" + this.getName();
        }
        return this.key;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<style:style");
        util.appendAttribute(appendable, "style:name", this.name);
        util.appendAttribute(appendable, "style:family", "graphic");
        appendable.append("><style:graphic-properties");
        if (this.drawFill != null) {
            this.drawFill.appendAttributes(util, appendable);
        } else {
            util.appendAttribute(appendable, "draw:fill", "none");
            util.appendAttribute(appendable, "draw:stroke", "none");
            util.appendAttribute(appendable, "draw:textarea-horizontal-align", "center");
            util.appendAttribute(appendable, "draw:textarea-vertical-align", "middle");
        }
        appendable.append("/></style:style>");

    }

    @Override
    public void addToElements(final OdsElements odsElements) {
        odsElements.addContentStyle(this);
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        if (this.drawFill != null) {
            this.drawFill.addEmbeddedStyles(stylesContainer);
        }
    }
}
