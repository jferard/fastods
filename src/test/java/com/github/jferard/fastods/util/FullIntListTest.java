/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Test;

public class FullIntListTest {
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	

	@Test
	public final void testAddZero() {
		final FullIntList fl = new FullIntList(10);
		Assert.assertEquals(ZERO, fl.set(100, ONE));
		Assert.assertEquals(101, fl.size());

		fl.set(1000, ZERO);
		Assert.assertEquals(101, fl.size());
	}

	@Test
	public final void testSetAndAdd() {
		final FullIntList fl = new FullIntList(10);

		Assert.assertEquals(0, fl.size());
		Assert.assertEquals(ZERO, fl.get(100));

		fl.set(100, ONE);
		Assert.assertFalse(fl.isEmpty());
		Assert.assertEquals(101, fl.size());
		Assert.assertEquals(ONE, fl.get(100));

		fl.set(1000, ZERO);
		Assert.assertEquals(101, fl.size());
		Assert.assertEquals(ZERO, fl.get(1000));
	}

	@Test
	public final void testGet() {
		final FullIntList l = new FullIntList();
		Assert.assertEquals(ZERO, l.get(10));
		Assert.assertTrue(l.isEmpty());
	}

	@Test
	public final void testSetNullAfter() {
		final FullIntList l = new FullIntList();
		l.set(10, ZERO);
		Assert.assertTrue(l.isEmpty());
	}

	@Test
	public final void testSetNullBefore() {
		final FullIntList l = new FullIntList();
		Assert.assertEquals(ZERO, l.set(0, ONE));
		Assert.assertEquals(1, l.size());
		Assert.assertEquals(ONE, l.set(0, ZERO));
		Assert.assertEquals(0, l.size());
		Assert.assertTrue(l.isEmpty());
	}

	@Test
	public final void testSize() {
		final FullIntList l = new FullIntList();
		Assert.assertEquals(0, l.size());
		Assert.assertTrue(l.isEmpty());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void testInit() {
		new FullIntList(-1);
	}
	
	@Test
	public final void testSetNullBefore2() {
		final FullIntList l = new FullIntList();
		Assert.assertEquals(ZERO, l.set(10, ONE));
		Assert.assertEquals(11, l.size());
		Assert.assertEquals(ONE, l.set(10, TWO));
		Assert.assertEquals(11, l.size());
		Assert.assertEquals(ZERO, l.set(9, ZERO));
		Assert.assertEquals(11, l.size());
		Assert.assertEquals(TWO, l.set(10, ZERO));
		Assert.assertEquals(0, l.size());
		Assert.assertTrue(l.isEmpty());
	}
}
