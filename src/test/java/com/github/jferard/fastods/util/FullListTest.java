package com.github.jferard.fastods.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

import com.google.common.collect.Lists;

public class FullListTest {

	@Test
	public final void testSetAndAdd() {
		final String be = "blank";
		List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();

		assertEquals(0, fl.size());
		assertEquals(be, fl.get(100));

		fl.set(100, "non blank");
		assertFalse(fl.isEmpty());
		assertEquals(101, fl.size());
		assertEquals("non blank", fl.get(100));

		fl.add("non blank2");
		assertEquals(102, fl.size());
		assertEquals("non blank2", fl.get(101));

		fl.add(be);
		assertEquals(102, fl.size());
		assertEquals(be, fl.get(102));

		fl.set(1000, be);
		assertEquals(102, fl.size());
		assertEquals(be, fl.get(1000));

		fl.addAll(
				Lists.<String> newArrayList(be, "non blank3", be, be, be, be));
		assertEquals(104, fl.size());
		assertEquals(be, fl.get(104));

		fl.addAll(100,
				Lists.<String> newArrayList(be, "non blank3", be, be, be, be));
		assertEquals(110, fl.size());
		assertEquals("non blank", fl.get(106));
		assertEquals("non blank3", fl.get(109));

		fl.add(100, "non blank4");
		assertEquals(111, fl.size());
		assertEquals("non blank", fl.get(107));
		assertEquals("non blank3", fl.get(110));
	}

	@Test
	public final void testClone() {
		final String be = "blank";
		List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank");
		List<String> l = new ArrayList<String>(101);
		for (int i = 0; i < 100; i++)
			l.add(be);
		l.add("non blank");
		assertTrue(l.equals(fl));
		assertTrue(fl.equals(l));
		assertEquals(fl.hashCode(), l.hashCode());
	}

	@Test
	public final void testAddBlank() {
		final String be = "blank";
		List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank");

		assertFalse(fl.add(be));
		assertEquals(101, fl.size());

		fl.add(1000, be);
		assertEquals(101, fl.size());

		assertFalse(fl.addAll(Lists.newArrayList(be, be, be)));
		assertEquals(101, fl.size());

		assertFalse(fl.addAll(1000, Lists.newArrayList(be, be, be)));
		assertEquals(101, fl.size());
	}

	@Test
	public final void testContains() {
		final String be = "blank";
		List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		assertTrue(fl.contains(be));

		assertTrue(fl.containsAll(Lists.<String> newArrayList(be, be, be)));

		fl.set(100, "non blank2");
		assertEquals(101, fl.size());
		assertTrue(fl.contains("non blank2"));

		assertFalse(fl.contains("non blank3"));
		assertFalse(
				fl.containsAll(Lists.<String> newArrayList(be, "non blank3")));
	}

	@Test
	public final void testIndexOfBlankElement() {
		final String be = "blank";
		List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();

		assertEquals(0, fl.indexOf(be));
		assertEquals(0, fl.lastIndexOf(be));

		fl.set(100, "non blank2");
		assertEquals(0, fl.indexOf(be));
		assertEquals(99, fl.lastIndexOf(be));
	}

	@Test
	public final void testIndexOf() {
		final String be = "blank";
		List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();

		assertEquals(-1, fl.indexOf("foo"));
		assertEquals(-1, fl.lastIndexOf("foo"));
	}

	@Test
	public final void testRemove() {
		final String be = "blank";
		List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank2");

		assertEquals(be, fl.remove(10));
		assertEquals(100, fl.size());
		assertEquals(be, fl.remove(1000));
		assertEquals(100, fl.size());

		assertTrue(fl.remove(be));
		assertEquals(99, fl.size());
		assertTrue(fl.remove("non blank2"));
		assertTrue(fl.isEmpty());
	}

	@Test
	public final void testRetain() {
		final String be = "blank";
		List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank2");
		fl.retainAll(Lists.<String>newArrayList("foo"));
		assertTrue(fl.isEmpty());
		
		fl.set(100, "non blank2");
		fl.retainAll(Lists.<String>newArrayList(be));
		assertTrue(fl.isEmpty());
		
		fl.set(100, "non blank2");
		fl.retainAll(Lists.<String>newArrayList("non blank2"));
		assertEquals(1, fl.size());
	}

	@Test
	public final void testClear() {
		final String be = "blank";
		List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank2");
		fl.clear();
		assertTrue(fl.isEmpty());
		assertEquals(be, fl.get(100));
	}

	@Test
	public final void testIterator() {
		final String be = "blank";
		List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank2");

		ListIterator<String> li = fl.listIterator();
		assertTrue(li.hasNext());
		li = fl.listIterator(100);
		assertTrue(li.hasNext());
		assertEquals("non blank2", li.next());
	}

}
