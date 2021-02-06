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

package com.github.jferard.fastods;

import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AbstractTableCellTest {
    @Test
    public void testEverythingButHasValue() {
        final WritableTableCell cell = new AbstractTableCell() {
            @Override
            public void appendXMLToTableRow(final XMLUtil util, final Appendable appendable) {
                throw new UnsupportedOperationException();
            }
        };
        for (final Method method : AbstractTableCell.class.getDeclaredMethods()) {
            if (method.getName().equals("hasValue") || method.getName().equals("$jacocoInit")) {
                continue;
            }
            final Object[] parameters = this.createDefaultParameters(method);
            Assert.assertThrows(UnsupportedOperationException.class, new ThrowingRunnable() {
                @Override
                public void run() throws Throwable {
                    try {
                        method.invoke(cell, parameters);
                    } catch (final InvocationTargetException e) {
                        throw e.getTargetException();
                    }

                }
            });
        }
    }

    @Test
    public void testHasValue() {
        final WritableTableCell cell = new AbstractTableCell() {
            @Override
            public void appendXMLToTableRow(final XMLUtil util, final Appendable appendable) {
                throw new UnsupportedOperationException();
            }
        };
        Assert.assertTrue(cell.hasValue());
    }

    private Object[] createDefaultParameters(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = getDefaultValue(parameterTypes[i]);
        }
        return parameters;
    }

    private static <T> T getDefaultValue(final Class<T> clazz) {
        return (T) Array.get(Array.newInstance(clazz, 1), 0);
    }

}