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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 16.27.25 <number:text-style>
 *
 * @author Julien Férard
 */
public class TextDataStyle implements DataStyle {
    private final CoreDataStyle dataStyle;
    private final String text;

    /**
     * A text-style
     *
     * @param dataStyle the embedded data style
     * @param text      the text ("<number:text-content>" for instance)
     */
    TextDataStyle(final CoreDataStyle dataStyle, final String text) {
        this.dataStyle = dataStyle;
        this.text = text;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<number:text-style");
        util.appendEAttribute(appendable, "style:name", this.getName());
        this.dataStyle.appendLVAttributes(util, appendable);
        appendable.append(">").append(this.text).append("</number:text-style>");
    }

    @Override
    public String getName() {
        return this.dataStyle.getName();
    }

    @Override
    public boolean isHidden() {
        return this.dataStyle.isHidden();
    }

    @Override
    public void addToElements(final OdsElements odsElements) {
        odsElements.addDataStyle(this);
    }
}
