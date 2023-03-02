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

package com.github.jferard.fastods.style;

import org.junit.Assert;
import org.junit.Test;

public class FontFaceTest {
    @Test
    public void testEquals() {
        final FontFace face = new FontFace(LOFonts.CALADEA);
        Assert.assertEquals(face, face);
        Assert.assertNotEquals(face, new Object());
        Assert.assertNotEquals(new Object(), face);
    }

    @Test
    public void testEquals2() {
        final FontFace face1 = new FontFace(LOFonts.CALADEA);
        final FontFace face2 = new FontFace(LOFonts.CALADEA);
        final FontFace face3 = new FontFace(LOFonts.OPENSYMBOL);
        Assert.assertEquals(face1, face2);
        Assert.assertNotEquals(face1, face3);
        Assert.assertNotEquals(face2, face3);
    }
}