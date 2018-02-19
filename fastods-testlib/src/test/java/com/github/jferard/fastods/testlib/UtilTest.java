/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.testlib;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.junit.Assert.*;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(Util.class)
public class UtilTest {
//    @Test
//    public void testNewDirString() throws NoSuchMethodException {
//        PowerMock.mockStatic(Util.class, Util.class.getMethod("mkdir", File.class));
//
//        EasyMock.expect(Util.mkdir(EasyMock.isA(File.class))).andReturn(true);
//        PowerMock.replayAll();
//        Assert.assertTrue(Util.mkdir("f"));
//        PowerMock.verifyAll();
//    }

    @Test
    public void testNewDirFile() {
        final File f = PowerMock.createMock(File.class);
        EasyMock.expect(f.exists()).andReturn(false);
        EasyMock.expect(f.mkdir()).andReturn(true);
        PowerMock.replayAll();
        Assert.assertTrue(Util.mkdir(f));
        PowerMock.verifyAll();
    }

    @Test
    public void testExistingDir() {
        final File f = PowerMock.createMock(File.class);
        EasyMock.expect(f.exists()).andReturn(true);
        EasyMock.expect(f.isDirectory()).andReturn(true);
        PowerMock.replayAll();
        Assert.assertFalse(Util.mkdir(f));
        PowerMock.verifyAll();
    }

    @Test(expected=IllegalStateException.class)
    public void testExistingFile() {
        final File f = PowerMock.createMock(File.class);
        EasyMock.expect(f.exists()).andReturn(true);
        EasyMock.expect(f.isDirectory()).andReturn(false);
        PowerMock.replayAll();
        Util.mkdir(f);
        PowerMock.verifyAll();
    }
}