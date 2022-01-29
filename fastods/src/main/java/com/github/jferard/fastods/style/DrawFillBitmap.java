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

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

public class DrawFillBitmap implements DrawFill, ObjectStyle {
    private final String name;
    private final String href;
    private String key;

    public DrawFillBitmap(final String name, final String href) {
        this.name = name;
        this.href = href;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<draw:fill-image");
        util.appendAttribute(appendable, "draw:name", this.name);
        util.appendAttribute(appendable, "xlink:href", this.href);
        util.appendAttribute(appendable, "xlink:type", "simple");
        util.appendAttribute(appendable, "xlink:show", "embed");
        util.appendAttribute(appendable, "xlink:actuate", "onLoad");
        appendable.append("/>");
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ObjectStyleFamily getFamily() {
        return ObjectStyleFamily.DRAW_FILL_BITMAP;
    }

    @Override
    public String getKey() {
        if (this.key == null) {
            this.key = this.getFamily() + "@" + this.getName();
        }
        return this.key;
    }

    @Override
    public void addToElements(final OdsElements odsElements) {
        // TODO: why nothing here?
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void appendAttributes(final XMLUtil util, final Appendable appendable)
            throws IOException {
        util.appendAttribute(appendable, "draw:fill", "bitmap");
        util.appendAttribute(appendable, "draw:fill-image-name", this.name);
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        stylesContainer.addStylesStyle(this);
    }
}
