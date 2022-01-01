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
import com.github.jferard.fastods.attribute.DisplayList;
import com.github.jferard.fastods.ref.CellRef;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class ValidationTest {
    @Test
    public void testWithoutCondition() throws IOException {
        final ValidationBuilder builder = Validation.builder("test");
        builder.baseCellAddress(CellRef.create(5, 7, CellRef.RELATIVE));
        builder.dontAllowEmptyCells();
        builder.errorMessage(ErrorMessage.create());
        builder.displayList(DisplayList.UNSORTED);
        final Validation validation = builder.build();

        TestHelper.assertXMLEquals("<table:content-validation table:name=\"test\" " +
                "table:allow-empty-cell=\"false\" table:base-cell-address=\"H6\">" +
                "<table:error-message/>" +
                "</table:content-validation>", validation);
    }

    @Test
    public void testWithCondition() throws IOException {
        final Validation validation = Validation.builder("test")
                .dontAllowEmptyCells()
                .listCondition(Arrays.asList("A", "B", "C"))
                .displayList(DisplayList.SORT_ASCENDING)
                .build();

        TestHelper.assertXMLEquals("<table:content-validation table:name=\"test\" " +
                        "table:condition=\"of:cell-content-is-in-list(&quot;A&quot;;&quot;B&quot;;&quot;C&quot;)\" " +
                        "table:allow-empty-cell=\"false\" table:display-list=\"sort-ascending\"/>",
                validation);
    }
}