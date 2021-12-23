/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

/**
 * 9.6.8 table:data-pilot-level
 *
 * @author J. Férard
 */
public class PilotTableLevel implements XMLConvertible {
    private final boolean showEmpty;

    public PilotTableLevel(final boolean showEmpty) {
        this.showEmpty = showEmpty;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:data-pilot-level"); // calcext:repeat-item-labels=\"false\"");
        util.appendAttribute(appendable, "table:show-empty", this.showEmpty);
        if (this.showEmpty) {
            appendable.append("/>");
        } else {
            appendable.append(">");
            appendable.append("<table:data-pilot-display-info table:enabled=\"false\" " +
                    "table:display-member-mode=\"from-top\" table:member-count=\"0\" " +
                    "table:data-field=\"\"/>");
            appendable.append("<table:data-pilot-sort-info " +
                    "table:order=\"ascending\" table:sort-mode=\"name\"/>");
            appendable
                    .append("<table:data-pilot" + "-layout-info table:add-empty-lines=\"false\" " +
                            "table:layout-mode=\"tabular-layout\"/>");
            appendable.append("</table:data-pilot-level>");
        }
        //         util.appendAttribute(appendable, "table:is-data-layout-field", true);
    }
}
