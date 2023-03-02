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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.TestHelper;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Arrays;

public class PilotTableTest {
    private PilotTableField field1;
    private XMLUtil util;

    @Before
    public void setUp() {
        this.field1 = PowerMock.createMock(PilotTableField.class);
        this.util = XMLUtil.create();
    }

    @Test
    public void test() throws IOException {
        final PilotTable pt =
                PilotTable.builder("n", "scr", "tr", Arrays.asList("b1", "b2")).field(this.field1)
                        .build();

        PowerMock.resetAll();
        this.field1.appendXMLContent(EasyMock.isA(XMLUtil.class), EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        TestHelper.assertXMLEquals("<table:data-pilot-table table:name=\"n\" " +
                "table:application-data=\"\" table:target-range-address=\"tr\" table:buttons=\"b1" +
                " b2\" table:show-filter-button=\"true\" " +
                "table:drill-down-on-double-click=\"false\">" +
                "<table:source-cell-range table:cell-range-address=\"scr\"/>" +
                "</table:data-pilot-table>", pt);

        PowerMock.verifyAll();
    }
}