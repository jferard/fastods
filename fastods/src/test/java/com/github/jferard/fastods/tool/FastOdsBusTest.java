/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.tool;

import org.junit.Assert;
import org.junit.Test;

public class FastOdsBusTest {
    @Test
    public void testBus() {
        final FastOdsBus<Integer> b = new FastOdsBus<Integer>();
        b.put(10);
        b.put(20);
        Assert.assertFalse(b.isClosed());
        Assert.assertEquals(Integer.valueOf(10), b.get());
        Assert.assertEquals(Integer.valueOf(20), b.get());
        b.close();
        Assert.assertTrue(b.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void testClosed() {
        final FastOdsBus<Integer> b = new FastOdsBus<Integer>();
        b.close();
        b.put(10);
    }
}