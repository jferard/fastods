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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class FullListTest {

	@Test
	public final void testAddBlank() {
		final String be = "blank";
		final List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank");

		Assert.assertFalse(fl.add(be));
		Assert.assertEquals(101, fl.size());

		fl.add(1000, be);
		Assert.assertEquals(101, fl.size());

		Assert.assertFalse(fl.addAll(Lists.newArrayList(be, be, be)));
		Assert.assertEquals(101, fl.size());

		Assert.assertFalse(fl.addAll(1000, Lists.newArrayList(be, be, be)));
		Assert.assertEquals(101, fl.size());
	}

	@Test
	public final void testClear() {
		final String be = "blank";
		final List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank2");
		fl.clear();
		Assert.assertTrue(fl.isEmpty());
		Assert.assertEquals(be, fl.get(100));
	}

	@Test
	public final void testClone() {
		final String be = "blank";
		final List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank");
		final List<String> l = new ArrayList<String>(101);
		for (int i = 0; i < 100; i++)
			l.add(be);
		l.add("non blank");
		Assert.assertTrue(l.equals(fl));
		Assert.assertTrue(fl.equals(l));
		Assert.assertEquals(fl.hashCode(), l.hashCode());
	}

	@Test
	public final void testContains() {
		final String be = "blank";
		final List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		Assert.assertTrue(fl.contains(be));

		Assert.assertTrue(
				fl.containsAll(Lists.<String> newArrayList(be, be, be)));

		fl.set(100, "non blank2");
		Assert.assertEquals(101, fl.size());
		Assert.assertTrue(fl.contains("non blank2"));

		Assert.assertFalse(fl.contains("non blank3"));
		Assert.assertFalse(
				fl.containsAll(Lists.<String> newArrayList(be, "non blank3")));
	}

	@Test
	public final void testIndexOf() {
		final String be = "blank";
		final List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();

		Assert.assertEquals(-1, fl.indexOf("foo"));
		Assert.assertEquals(-1, fl.lastIndexOf("foo"));
	}

	@Test
	public final void testIndexOfBlankElement() {
		final String be = "blank";
		final List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();

		Assert.assertEquals(0, fl.indexOf(be));
		Assert.assertEquals(0, fl.lastIndexOf(be));

		fl.set(100, "non blank2");
		Assert.assertEquals(0, fl.indexOf(be));
		Assert.assertEquals(99, fl.lastIndexOf(be));
	}

	@Test
	public final void testIterator() {
		final String be = "blank";
		final List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank2");

		ListIterator<String> li = fl.listIterator();
		Assert.assertTrue(li.hasNext());
		li = fl.listIterator(100);
		Assert.assertTrue(li.hasNext());
		Assert.assertEquals("non blank2", li.next());
	}

	@Test
	public final void testRemove() {
		final String be = "blank";
		final List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank2");

		Assert.assertEquals(be, fl.remove(10));
		Assert.assertEquals(100, fl.size());
		Assert.assertEquals(be, fl.remove(1000));
		Assert.assertEquals(100, fl.size());

		Assert.assertTrue(fl.remove(be));
		Assert.assertEquals(99, fl.size());
		Assert.assertTrue(fl.remove("non blank2"));
		Assert.assertTrue(fl.isEmpty());
	}

	@Test
	public final void testRemoveAll() {
		final String be = "blank";
		final List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(50, "non blank");
		fl.set(100, "non blank2");

		Assert.assertTrue(fl.removeAll(Arrays.asList("non blank2")));
		Assert.assertEquals(51, fl.size());
	}

	@Test
	public final void testRetain() {
		final String be = "blank";
		final List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank2");
		fl.retainAll(Lists.<String> newArrayList("foo"));
		Assert.assertTrue(fl.isEmpty());

		fl.set(100, "non blank2");
		fl.retainAll(Lists.<String> newArrayList(be));
		Assert.assertTrue(fl.isEmpty());

		fl.set(100, "non blank2");
		fl.retainAll(Lists.<String> newArrayList("non blank2"));
		Assert.assertEquals(1, fl.size());
	}

	@Test
	public final void testSetAndAdd() {
		final String be = "blank";
		final List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();

		Assert.assertEquals(0, fl.size());
		Assert.assertEquals(be, fl.get(100));

		fl.set(100, "non blank");
		Assert.assertFalse(fl.isEmpty());
		Assert.assertEquals(101, fl.size());
		Assert.assertEquals("non blank", fl.get(100));

		fl.add("non blank2");
		Assert.assertEquals(102, fl.size());
		Assert.assertEquals("non blank2", fl.get(101));

		fl.add(be);
		Assert.assertEquals(102, fl.size());
		Assert.assertEquals(be, fl.get(102));

		fl.set(1000, be);
		Assert.assertEquals(102, fl.size());
		Assert.assertEquals(be, fl.get(1000));

		fl.addAll(
				Lists.<String> newArrayList(be, "non blank3", be, be, be, be));
		Assert.assertEquals(104, fl.size());
		Assert.assertEquals(be, fl.get(104));

		fl.addAll(100,
				Lists.<String> newArrayList(be, "non blank3", be, be, be, be));
		Assert.assertEquals(110, fl.size());
		Assert.assertEquals("non blank", fl.get(106));
		Assert.assertEquals("non blank3", fl.get(109));

		fl.add(100, "non blank4");
		Assert.assertEquals(111, fl.size());
		Assert.assertEquals("non blank", fl.get(107));
		Assert.assertEquals("non blank3", fl.get(110));
	}

	@Test
	public void testToString() {
		final List<String> fl = FullList.<String> builder().build();
		fl.add("a");
		fl.add(4, "e");

		Assert.assertEquals("[a, null, null, null, e]", fl.toString());
	}

	@Test
	public void testToArray() {
		final List<String> fl = FullList.<String> builder().build();
		fl.add("a");
		fl.add(4, "e");

		Assert.assertArrayEquals(
				new String[] { "a", null, null, null, "e" },
				fl.toArray(new String[] {}));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testSubList() {
		final List<String> fl = FullList.<String> builder().build();
		fl.add("a");
		fl.add(4, "e");
		fl.subList(1, 3);
	}
}
