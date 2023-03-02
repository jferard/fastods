/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.TestHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class UserDefinedTest {
    @Test
    public void fromBoolean() throws IOException {
        TestHelper.assertXMLEquals(
                "<meta:user-defined meta:name=\"b\" meta:type=\"boolean\">true</meta:user-defined>",
                UserDefined.fromBoolean("b", true));
    }

    @Test
    public void fromDate() throws IOException {
        TestHelper.assertXMLEquals(
                "<meta:user-defined meta:name=\"d\" " +
                        "meta:type=\"date\">1970-01-01</meta:user-defined>",
                UserDefined.fromDate("d", new Date(0)));
    }

    @Test
    public void fromFloat() throws IOException {
        TestHelper.assertXMLEquals(
                "<meta:user-defined meta:name=\"f\" meta:type=\"float\">123.45</meta:user-defined>",
                UserDefined.fromFloat("f", 123.45));
    }

    @Test
    public void fromTime() throws IOException {
        TestHelper.assertXMLEquals(
                "<meta:user-defined meta:name=\"t\" " +
                        "meta:type=\"time\">00:20:34</meta:user-defined>",
                UserDefined.fromTime("t", new Date(1234567)));
    }

    @Test
    public void fromString() throws IOException {
        TestHelper.assertXMLEquals(
                "<meta:user-defined meta:name=\"s\" meta:type=\"string\">a " +
                        "string</meta:user-defined>",
                UserDefined.fromString("s", "a string"));
    }

    @Test
    public void appendXMLContent() {
    }
}