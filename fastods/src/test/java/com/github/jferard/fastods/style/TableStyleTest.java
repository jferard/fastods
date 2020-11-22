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
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.odselement.OdsElements;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class TableStyleTest {
    @Test
    public final void testAddEmptyToFile() {
        final TableStyle ts = TableStyle.builder("test").build();
        final OdsElements odsElements = PowerMock.createMock(OdsElements.class);

        PowerMock.resetAll();
        EasyMock.expect(odsElements.addContentStyle(ts)).andReturn(true);
        EasyMock.expect(odsElements
                .addMasterPageStyle(PageStyle.DEFAULT_MASTER_PAGE_STYLE.getMasterPageStyle()))
                .andReturn(true);
        EasyMock.expect(odsElements
                .addPageLayoutStyle(PageStyle.DEFAULT_MASTER_PAGE_STYLE.getPageLayoutStyle()))
                .andReturn(true);

        PowerMock.replayAll();
        ts.addToElements(odsElements);

        PowerMock.verifyAll();
    }

    @Test
    public final void testEmpty() throws IOException {
        final TableStyle ts = TableStyle.builder("test").build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table\" " +
                "style:master-page-name=\"Default\">" + "<style:table-properties " +
                "table:display=\"true\" style:writing-mode=\"lr-tb\"/>" + "</style:style>", ts);
    }

    @Test
    public final void testPageStyle() throws IOException {
        final PageStyle ps = PageStyle.builder("p").build();
        final TableStyle ts = TableStyle.builder("test").pageStyle(ps).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table\" " +
                "style:master-page-name=\"p\">" +
                "<style:table-properties table:display=\"true\" " +
                "style:writing-mode=\"lr-tb\"/>" + "</style:style>", ts);
        Assert.assertEquals("test", ts.getName());
    }

    @Test
    public final void testGetters() {
        StyleTestHelper.testGettersHidden(TableStyle.builder("test"));
    }
}
