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

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TestHelper;
import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class AutoFilterTest {
    @Test
    public void test() throws IOException {
        final Table table = PowerMock.createMock(Table.class);
        final Filter filter = PowerMock.createMock(Filter.class);

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("t").times(2);
        filter.appendXMLContent(EasyMock.isA(XMLUtil.class), EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        final AutoFilter af =
                AutoFilter.builder("my_range", table, 0, 1, 2, 3).filter(filter).hideButtons()
                        .build();
        TestHelper.assertXMLEquals("<table:database-range table:name=\"my_range\" " +
                "table:display-filter-buttons=\"false\" " +
                "table:target-range-address=\"t.B1:t.D3\"><table:filter></table:filter>" +
                "</table:database-range>", af);

        PowerMock.verifyAll();
    }

}