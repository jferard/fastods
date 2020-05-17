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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.odselement.OdsElements;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.powermock.api.easymock.PowerMock;

public class DataStyleTestHelper {
    /**
     * very basic test
     */
    public static void testAddToElements(final DataStyle dataStyle) {
        final OdsElements elements = PowerMock.createMock(OdsElements.class);

        PowerMock.resetAll();
        EasyMock.expect(elements.addDataStyle(dataStyle)).andReturn(true);

        PowerMock.replayAll();
        dataStyle.addToElements(elements);

        PowerMock.verifyAll();
    }

    public static <S extends DataStyle, T extends DataStyleBuilder<S, T>> void testGetters(
            final DataStyleBuilder<S, T> builder) {
        final S style = builder.build();
        Assert.assertEquals("test", style.getName());
        Assert.assertTrue(style.isHidden());
        Assert.assertFalse(builder.visible().build().isHidden());

    }
}
