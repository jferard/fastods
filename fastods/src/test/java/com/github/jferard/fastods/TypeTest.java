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

import com.github.jferard.fastods.PageSection.Type;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jferard on 09/05/17.
 */
public class TypeTest {
    @Test
    public void test() {
        Assert.assertArrayEquals(new Type[]{Type.FOOTER, Type.HEADER}, Type.values());
        Assert.assertEquals(Type.FOOTER, Type.valueOf("FOOTER"));
        Assert.assertEquals(Type.HEADER, Type.valueOf("HEADER"));
    }

    @Test
    public void testValueOfX() {
        Assert.assertThrows(IllegalArgumentException.class, () -> Type.valueOf("x"));
    }
}