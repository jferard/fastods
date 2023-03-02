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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.OdsFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.logging.Logger;

/**
 *
 */
public class FileOpenResultTest {
    private OdsFactory odsFactory;

    @Before
    public void setUp() {
        final Logger logger = PowerMock.createMock(Logger.class);
        this.odsFactory = OdsFactory.create(logger, Locale.US);
    }

    @Test
    public void testIsDir() throws Exception {
        final FileOpenResult fileOpenResult = FileOpen.openFile(".");

        Assert.assertSame(FileOpenResult.FILE_IS_DIR, fileOpenResult);
        Assert.assertThrows(IllegalStateException.class, () -> fileOpenResult.getStream());
    }

    @Test
    public void testOpen() throws Exception {
        final File aTest = new File("atest");

        try {
            final FileOpenResult fileOpenResult = FileOpen.openFile(aTest.getAbsolutePath());
            Assert.assertTrue(fileOpenResult instanceof FileOpen);
            Assert.assertEquals(0, aTest.length());
            // the file has been touched
            final OutputStream s = fileOpenResult.getStream();
            s.write(new byte[]{'a', 'b', 'c'});
            s.close();
            Assert.assertEquals(3, aTest.length());
        } finally {
            Assert.assertTrue(aTest.delete());
        }
    }

    @Test
    public void testExists() throws Exception {
        final File aTest = new File("atest");

        // create a file
        OutputStream s = new FileOutputStream(aTest);
        s.write(new byte[]{'a', 'b', 'c'});
        s.close();
        Assert.assertEquals(3, aTest.length());
        try {
            final FileOpenResult fileOpenResult = FileOpen.openFile(aTest.getAbsolutePath());
            Assert.assertTrue(fileOpenResult instanceof FileExists);
            // the file has not been touched
            Assert.assertEquals(3, aTest.length());
            // the file has been touched
            s = fileOpenResult.getStream();
            s.close();
            Assert.assertEquals(0, aTest.length());
        } finally {
            Assert.assertTrue(aTest.delete());
        }
    }
}