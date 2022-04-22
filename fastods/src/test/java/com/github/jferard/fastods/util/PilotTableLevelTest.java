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

import com.github.jferard.fastods.TestHelper;
import org.junit.Test;

import java.io.IOException;

public class PilotTableLevelTest {
    @Test
    public void testDontShowEmpty() throws IOException {
        final PilotTableLevel level = new PilotTableLevel(false);

        TestHelper.assertXMLEquals("<table:data-pilot-level table:show-empty=\"false\">" +
                "<table:data-pilot-display-info table:enabled=\"false\" " +
                "table:display-member-mode=\"from-top\" table:member-count=\"0\" " +
                "table:data-field=\"\"/>" +
                "<table:data-pilot-sort-info table:order=\"ascending\" " +
                "table:sort-mode=\"name\"/>" +
                "<table:data-pilot-layout-info table:add-empty-lines=\"false\" " +
                "table:layout-mode=\"tabular-layout\"/>" + "</table:data-pilot-level>", level);
    }

    @Test
    public void testShowEmpty() throws IOException {
        final PilotTableLevel level = new PilotTableLevel(true);

        TestHelper.assertXMLEquals("<table:data-pilot-level table:show-empty=\"true\">" +
                "<table:data-pilot-display-info table:enabled=\"false\" " +
                "table:display-member-mode=\"from-top\" table:member-count=\"0\" " +
                "table:data-field=\"\"/>" +
                "<table:data-pilot-sort-info table:order=\"ascending\" " +
                "table:sort-mode=\"name\"/>" +
                "<table:data-pilot-layout-info table:add-empty-lines=\"false\" " +
                "table:layout-mode=\"tabular-layout\"/>" + "</table:data-pilot-level>", level);
    }
}