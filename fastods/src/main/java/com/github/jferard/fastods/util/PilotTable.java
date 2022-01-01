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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * 9.6Data Pilot Tables
 *
 * @author J. Férard
 */
public class PilotTable implements XMLConvertible {
    public static PilotTableBuilder builder(final String name, final String sourceCellRange,
                                            final String targetRange, final List<String> buttons) {
        return new PilotTableBuilder(name, sourceCellRange, targetRange, buttons);
    }

    private final List<PilotTableField> fields;
    private final String sourceCellRange;
    private final String name;
    private final String targetRange;
    private final List<String> buttons;
    private final boolean showFilterButton;
    private final boolean drillDownOnDoubleClick;

    public PilotTable(final String name, final String sourceCellRange, final String targetRange,
                      final List<String> buttons, final List<PilotTableField> fields,
                      final boolean showFilterButton, final boolean drillDownOnDoubleClick) {
        this.fields = fields;
        this.sourceCellRange = sourceCellRange;
        this.name = name;
        this.targetRange = targetRange;
        this.buttons = buttons;
        this.showFilterButton = showFilterButton;
        this.drillDownOnDoubleClick = drillDownOnDoubleClick;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:data-pilot-table");
        util.appendAttribute(appendable, "table:name", this.name);
        util.appendAttribute(appendable, "table:application-data", "");
        util.appendAttribute(appendable, "table:target-range-address", this.targetRange);
        if (this.buttons.size() > 0) {
            util.appendAttribute(appendable, "table:buttons", this.buildButtonsAttr());
        }
        util.appendAttribute(appendable, "table:show-filter-button", this.showFilterButton);
        util.appendAttribute(appendable, "table:drill-down-on-double-click",
                this.drillDownOnDoubleClick);
        appendable.append(">");

        appendable.append("<table:source-cell-range");
        util.appendAttribute(appendable, "table:cell-range-address", this.sourceCellRange);
        appendable.append("/>");
        for (final PilotTableField field : this.fields) {
            field.appendXMLContent(util, appendable);
        }
        appendable.append("</table:data-pilot-table>");
    }

    private StringBuilder buildButtonsAttr() {
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> it = this.buttons.iterator();
        sb.append(it.next());
        while (it.hasNext()) {
            sb.append(' ').append(it.next());
        }
        return sb;
    }
}
