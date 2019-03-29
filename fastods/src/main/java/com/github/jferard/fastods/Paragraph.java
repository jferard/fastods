/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.List;

/**
 * 5.1.3 text:p
 *
 * @author Julien Férard
 */
public class Paragraph implements TagParameters, ParagraphElement {
    /**
     * @return a new builder
     */
    public static ParagraphBuilder builder() {
        return new ParagraphBuilder();
    }

    private final List<ParagraphElement> paragraphElements;

    private final TextStyle style;

    /**
     * Create a new paragraph
     *
     * @param paragraphElements the elements
     * @param style             the style
     */
    Paragraph(final List<ParagraphElement> paragraphElements, final TextStyle style) {
        this.paragraphElements = paragraphElements;
        this.style = style;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        if (this.paragraphElements.isEmpty()) {
            appendable.append("<text:p/>");
        } else {
            appendable.append("<text:p");
            if (this.style != null)
                util.appendEAttribute(appendable, "text:style-name", this.style.getName());
            appendable.append('>');
            for (final ParagraphElement paragraphElement : this.paragraphElements)
                paragraphElement.appendXMLContent(util, appendable);
            appendable.append("</text:p>");
        }
    }

    @Override
    public void addEmbeddedStylesFromFooterHeader(final StylesContainer stylesContainer) {
        if (this.style != null) stylesContainer.addStylesFontFaceContainerStyle(this.style);
        for (final ParagraphElement element : this.paragraphElements) {
            element.addEmbeddedStylesFromFooterHeader(stylesContainer);
        }
    }

    @Override
    public void addEmbeddedStylesFromCell(final StylesContainer stylesContainer) {
        if (this.style != null) stylesContainer.addContentFontFaceContainerStyle(this.style);
        for (final ParagraphElement element : this.paragraphElements) {
            element.addEmbeddedStylesFromCell(stylesContainer);
        }
    }
}
