/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.attribute.FilterOperator;
import com.github.jferard.fastods.attribute.FilterType;

import java.io.IOException;

/**
 * 9.5.5<table:filter-condition>
 *
 * @author J. Férard
 */
public class FilterCompare implements Filter {
    private final int colIndex;
    private final FilterOperator operator;
    private final String value;
    private final FilterType type;

    /**
     * @param colIndex the index
     * @param operator the operator
     * @param value    the value
     * @param type     the type
     */
    FilterCompare(final int colIndex, final FilterOperator operator, final String value,
                  final FilterType type) {
        this.colIndex = colIndex;
        this.operator = operator;
        this.value = value;
        this.type = type;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:filter-condition");
        util.appendAttribute(appendable, "table:operator", this.operator);
        util.appendAttribute(appendable, "table:value", this.value);
        util.appendAttribute(appendable, "table:data-type", this.type);
        util.appendAttribute(appendable, "table:field-number", this.colIndex);
        appendable.append("/>");
    }

}
