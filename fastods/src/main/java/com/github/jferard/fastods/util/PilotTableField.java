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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.XMLConvertible;
import com.github.jferard.fastods.attribute.FieldOrientation;
import com.github.jferard.fastods.attribute.PilotFunction;

import java.io.IOException;

/**
 * 9.6.7 table:data-pilot-field
 *
 * @author J. Férard
 */
public class PilotTableField implements XMLConvertible {

    private final String sourceFieldName;
    private final FieldOrientation orientation;
    private final int usedHierarchy;
    private final boolean isDataLayout;
    private final PilotFunction function;
    private final PilotTableLevel level;

    public PilotTableField(final String sourceFieldName, final FieldOrientation orientation,
                           final int usedHierarchy, final boolean isDataLayout,
                           final PilotFunction function, final PilotTableLevel level) {
        this.sourceFieldName = sourceFieldName;
        this.orientation = orientation;
        this.usedHierarchy = usedHierarchy;
        this.isDataLayout = isDataLayout;
        this.function = function;
        this.level = level;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:data-pilot-field");
        util.appendAttribute(appendable, "table:source-field-name", this.sourceFieldName);
        util.appendAttribute(appendable, "table:orientation", this.orientation);
        if (this.usedHierarchy != -1) {
            util.appendAttribute(appendable, "table:used-hierarchy", this.usedHierarchy);
        }
        if (this.isDataLayout) {
            util.appendAttribute(appendable, "table:is-data-layout-field", true);
        }
        util.appendAttribute(appendable, "table:function", this.function);
        if (this.level == null) {
            appendable.append("/>");
        } else {
            appendable.append(">");
            this.level.appendXMLContent(util, appendable);
            appendable.append("</table:data-pilot-field>");
        }
    }
}
