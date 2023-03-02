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

package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A test helper
 */
public class TestHelper {
    private static final XMLUtil util = XMLUtil.create();

    public static String toXML(final XMLConvertible o) throws IOException {
        final StringBuilder sb = new StringBuilder();
        o.appendXMLContent(util, sb);
        return sb.toString();
    }

    /**
     * Beware: sensitive to spaces (spaces are children)
     *
     * @param xml the xml string
     * @param o an object convertible to XML
     * @throws IOException if an I/O error occurs
     */
    public static void assertXMLEquals(final String xml, final XMLConvertible o)
            throws IOException {
        DomTester.assertEquals(xml, TestHelper.toXML(o));
    }

    public static void assertXMLUnsortedEquals(final String xml, final XMLConvertible o)
            throws IOException {
        DomTester.assertUnsortedEquals(xml, TestHelper.toXML(o));
    }

    public static Handler getMockHandler(final Logger logger) {
        final Handler handler = PowerMock.createMock(Handler.class);
        for (final Handler h : logger.getHandlers()) {
            logger.removeHandler(h);
        }
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
        handler.setLevel(Level.ALL);

        final Handler[] handlers = logger.getHandlers();
        assert handlers.length == 1 && handlers[0] == handler : handlers;
        return handler;
    }

    /**
     * When creating a doc with mock odsElements
     *
     * @param mockOdsElements the mocked odsElements
     */
    public static void initMockDocument(final OdsElements mockOdsElements) {
        EasyMock.expect(mockOdsElements.addContentStyle(TableStyle.DEFAULT_TABLE_STYLE))
                .andReturn(true);
        EasyMock.expect(mockOdsElements.addContentStyle(TableRowStyle.DEFAULT_TABLE_ROW_STYLE))
                .andReturn(true);
        EasyMock.expect(
                mockOdsElements.addContentStyle(TableColumnStyle.DEFAULT_TABLE_COLUMN_STYLE))
                .andReturn(true);
        EasyMock.expect(mockOdsElements.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true); //.times(2);
        EasyMock.expect(mockOdsElements
                .addMasterPageStyle(PageStyle.DEFAULT_MASTER_PAGE_STYLE.getMasterPageStyle()))
                .andReturn(true).anyTimes();
        EasyMock.expect(mockOdsElements
                .addPageLayoutStyle(PageStyle.DEFAULT_MASTER_PAGE_STYLE.getPageLayoutStyle()))
                .andReturn(true).anyTimes();
    }

    /**
     * Create a new set
     * @param elements the elements
     * @param <T> the type of the elements
     * @return the set
     */
    public static <T> Set<T> newSet(final T... elements) {
        return new HashSet<T>(Arrays.asList(elements));
    }
}
