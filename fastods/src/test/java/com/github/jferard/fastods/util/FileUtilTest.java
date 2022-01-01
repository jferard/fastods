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

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FileUtilTest {
    @Test
    public void testCopy() throws IOException {
        this.testCopyAux("FastODS", 2, 4);
    }

    @Test
    public void testCopy2() throws IOException {
        this.testCopyAux("FastODS", 8, 2);
    }

    @Test
    public void testCopy3() throws IOException {
        this.testCopyAux("FastODSFastODSFastODSFastODSFastODSFastODSFastODS", 4, 4);
    }

    private void testCopyAux(final String text, final int bufferSize, final int startSize)
            throws IOException {
        final byte[] expectedBytes = text.getBytes(CharsetUtil.UTF_8);
        final FileUtil fu = new FileUtil(bufferSize, startSize);
        final byte[] actualBytes = fu.readStream(new ByteArrayInputStream(expectedBytes));
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }
}