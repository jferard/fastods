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

import com.github.jferard.fastods.datastyle.BooleanStyleBuilder;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class ChildCellStyleKeyTest {
    @Test
    public void testEquals() {
        final Locale locale = Locale.US;
        final TableCellStyle tcs = TableCellStyle.builder("tcs").build();
        final DataStyle ds = new BooleanStyleBuilder("bs", locale).build();
        final StylesContainerImpl.ChildCellStyleKey key1 =
                new StylesContainerImpl.ChildCellStyleKey(tcs, ds);
        Assert.assertEquals(key1, key1);
        Assert.assertNotEquals(key1, new Object());
        Assert.assertNotEquals(new Object(), key1);

        final StylesContainerImpl.ChildCellStyleKey key2 =
                new StylesContainerImpl.ChildCellStyleKey(tcs, ds);
        Assert.assertEquals(key1, key2);
    }

    @Test
    public void testHashCode() {
        final Locale locale = Locale.US;
        final TableCellStyle tcs = TableCellStyle.builder("tcs").build();
        final DataStyle ds = new BooleanStyleBuilder("bs", locale).build();
        final StylesContainerImpl.ChildCellStyleKey key1 =
                new StylesContainerImpl.ChildCellStyleKey(tcs, ds);
        Assert.assertEquals(1813590458, key1.hashCode());
        final StylesContainerImpl.ChildCellStyleKey key2 =
                new StylesContainerImpl.ChildCellStyleKey(tcs, ds);
        Assert.assertEquals(1813590458, key2.hashCode());
    }
}