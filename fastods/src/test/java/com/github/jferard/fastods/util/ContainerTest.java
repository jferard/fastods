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
package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.util.Container.Mode;

public class ContainerTest {
	private Container<String, Integer> c;

	@Before
	public void setUp() throws Exception {
		this.c = new Container<String, Integer>();
	}

	@Test
	public final void testCreateThenUpdate() {
		Assert.assertTrue(this.c.add("a", 1, Mode.CREATE_OR_UPDATE));
		Assert.assertEquals(Integer.valueOf(1),
				this.c.getValues().iterator().next());
		Assert.assertTrue(this.c.add("a", 2, Mode.CREATE_OR_UPDATE));
		Assert.assertEquals(Integer.valueOf(2),
				this.c.getValues().iterator().next());
	}

	@Test
	public final void testCreateTwice() {
		Assert.assertTrue(this.c.add("a", 1, Mode.CREATE));
		Assert.assertEquals(Integer.valueOf(1),
				this.c.getValues().iterator().next());
		Assert.assertFalse(this.c.add("a", 2, Mode.CREATE));
		Assert.assertEquals(Integer.valueOf(1),
				this.c.getValues().iterator().next());
	}

	@Test
	public final void testEmpty() {
		Assert.assertFalse(this.c.getValues().iterator().hasNext());
	}

	@Test
	public final void testUpdateWithoutCreation() {
		Assert.assertFalse(this.c.add("a", 1, Mode.UPDATE));
		Assert.assertFalse(this.c.getValues().iterator().hasNext());
	}
}
