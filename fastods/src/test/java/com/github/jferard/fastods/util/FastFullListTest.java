/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
import org.junit.Test;

public class FastFullListTest {
	@Test
	public final void testSetAndAdd() {
		final String be = "blank";
		final FastFullList<String> fl = FastFullList.<String> builder().blankElement(be)
				.capacity(10).build();

		Assert.assertEquals(0, fl.usedSize());
		Assert.assertEquals(be, fl.get(100));

		fl.set(100, "non blank");
		Assert.assertEquals(101, fl.usedSize());
		Assert.assertEquals("non blank", fl.get(100));

		fl.set(1000, be);
		Assert.assertEquals(101, fl.usedSize());
		Assert.assertEquals(be, fl.get(1000));
	}

	@Test
	public final void testSet() {
		final FastFullList<String> fl = FastFullList.<String> builder()
				.capacity(10).build();

		Assert.assertEquals(0, fl.usedSize());
		Assert.assertEquals(null, fl.get(100));

		for (int i = 0; i<100; i++) {
            fl.set(i, "non blank");
            Assert.assertEquals(i + 1, fl.usedSize());
        }
        fl.set(100, null);
		Assert.assertEquals(100, fl.usedSize());
	}

	@Test
	public final void testGet() {
		final FastFullList<String> fl = FastFullList.<String>builder().build();
		Assert.assertNull(fl.get(10));
		Assert.assertEquals(0, fl.usedSize());
	}
}
