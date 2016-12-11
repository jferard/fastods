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
package com.github.jferard.fastods;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.jferard.fastods.util.FullList;
import com.google.common.collect.Lists;

public class FullListTest {

	@Test
	public final void testGet() {
		final List<String> l = FullList.newList();
		Assert.assertNull(l.get(10));
		Assert.assertTrue(l.isEmpty());
	}

	@Test
	public final void testSetNonNull() {
		final List<String> l = FullList.newList();
		l.set(10, "a");
		final List<String> l2 = Lists.newArrayList(l);
		Assert.assertEquals(Arrays.asList(null, null, null, null, null, null,
				null, null, null, null, "a"), l2);
	}

	@Test
	public final void testSetNullAfter() {
		final List<String> l = FullList.newList();
		l.set(10, null);
		Assert.assertTrue(l.isEmpty());
	}

	@Test
	public final void testSetNullBefore() {
		final List<String> l = FullList.newList();
		l.add("a");
		l.add("b");
		Assert.assertEquals("a", l.set(0, null));
		Assert.assertEquals(Arrays.asList(null, "b"), l);
		Assert.assertEquals("b", l.set(1, null));
		Assert.assertTrue(l.isEmpty());
	}

	@Test
	public final void testSetOne() {
		final List<String> l = FullList.newList();
		l.set(1, "a");
		Assert.assertEquals(Arrays.asList(null, "a"), l);
	}

	@Test
	public final void testSize() {
		final List<String> l = FullList.newList();
		Assert.assertEquals(0, l.size());
		Assert.assertTrue(l.isEmpty());
	}
}
