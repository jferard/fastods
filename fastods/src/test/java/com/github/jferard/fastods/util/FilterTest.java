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

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.attribute.FilterOperator;
import com.github.jferard.fastods.attribute.FilterType;
import org.junit.Test;

import java.io.IOException;

public class FilterTest {
    @Test
    public void test() throws IOException {
        final Filter filter = new FilterOr(new FilterEnumerate(1, "1", "2"),
                new FilterAnd(new FilterCompare(2, FilterOperator.BEGINS, "7", FilterType.TEXT),
                        new FilterCompare(2, FilterOperator.N_BEGINS, "70", FilterType.TEXT)));

        TestHelper.assertXMLEquals(
                "<table:filter-or>" + "<table:filter-condition table:field-number=\"1\">" +
                        "<table:filter-set-item table:value=\"1\"/>" +
                        "<table:filter-set-item table:value=\"2\"/>" + "</table:filter-condition>" +
                        "<table:filter-and>" +
                        "<table:filter-condition table:operator=\"begins\" table:value=\"7\" " +
                        "table:data-type=\"text\" table:field-number=\"2\"/>" +
                        "<table:filter-condition table:operator=\"!begins\" table:value=\"70\" " +
                        "table:data-type=\"text\" table:field-number=\"2\"/>" +
                        "</table:filter-and>" + "</table:filter-or>", filter);
    }

}