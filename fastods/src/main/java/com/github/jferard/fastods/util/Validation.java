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

import com.github.jferard.fastods.XMLConvertible;
import com.github.jferard.fastods.attribute.DisplayList;
import com.github.jferard.fastods.ref.CellRef;

import java.io.IOException;

public class Validation implements XMLConvertible, NamedObject {
    public static ValidationBuilder builder(final String name) {
        return new ValidationBuilder(name);
    }

    private final String name;
    private final String condition;
    private final boolean allowEmptyCells;
    private final CellRef baseCellAddress;
    private final DisplayList displayList;
    private final ErrorMessage errorMessage;

    public Validation(final String name, final String condition, final boolean allowEmptyCells,
                      final CellRef baseCellAddress,
                      final DisplayList displayList, final ErrorMessage errorMessage) {
        this.name = name;
        this.condition = condition;
        this.allowEmptyCells = allowEmptyCells;
        this.baseCellAddress = baseCellAddress;
        this.displayList = displayList;
        this.errorMessage = errorMessage;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:content-validation");
        util.appendEAttribute(appendable, "table:name", this.name);
        util.appendEAttribute(appendable, "table:condition", this.condition); // "of:cell-content-is-in-list(&quot;A&quot;;&quot;B&quot;;&quot;C&quot;)">
        if (!this.allowEmptyCells) {
            util.appendEAttribute(appendable, "table:allow-empty-cell", "false");
        }
        if (this.baseCellAddress != null) {
            util.appendEAttribute(appendable, "table:base-cell-address", this.baseCellAddress.toString());
        }
        if (this.baseCellAddress != null) {
            util.appendAttribute(appendable, "table:base-cell-address", this.baseCellAddress.toString());
        }
        if (this.displayList != DisplayList.UNSORTED) {
            util.appendAttribute(appendable, "table:base-cell-address", this.displayList);
        }
        if (this.errorMessage != null) {
            appendable.append(">");
            this.errorMessage.appendXMLContent(util, appendable);
            appendable.append("</table:content-validation>");
        } else {
            appendable.append("/>");
        }
    }

    @Override
    public String getName() {
        return this.name;
    }
}
