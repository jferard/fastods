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

package com.github.jferard.fastods.ref;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class ColTest {
    @Test
    public final void testParseString() {
        Assert.assertEquals(0, Col.parseString("A"));
        Assert.assertEquals(25, Col.parseString("Z"));
        Assert.assertEquals(26, Col.parseString("AA"));
        Assert.assertEquals(27, Col.parseString("AB"));
        Assert.assertEquals(52, Col.parseString("BA"));
        Assert.assertEquals(389, Col.parseString("NZ"));
        Assert.assertEquals(1023, Col.parseString("AMJ"));
        Assert.assertEquals(16383, Col.parseString("XFD"));
    }

    @Test
    public final void testParseStringUsingReflection() throws IllegalAccessException {
        final Field[] declaredFields = Col.class.getDeclaredFields();
        for (final Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                try {
                    final int colPos = (int) field.get(null);
                    final String colLetters = field.getName();
                    Assert.assertEquals(colPos, Col.parseString(colLetters));
                } catch (final IllegalAccessException e) {
                    System.out.println("Ignore" + field);
                }
            }
        }
    }
}