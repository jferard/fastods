package com.github.jferard.fastods.util;

import static org.junit.Assert.*;

import java.util.List;

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

		fl.addAll(Lists.<String> newArrayList(be, "non blank3", be,
				be, be, be));
		assertEquals(104, fl.size());
		assertEquals(be, fl.get(104));

		fl.addAll(100, Lists.<String> newArrayList(be, "non blank3", be,
				be, be, be));
		assertEquals(110, fl.size());
		assertEquals("non blank", fl.get(106));
		assertEquals("non blank3", fl.get(109));
		
		fl.add(100, "non blank4");
		assertEquals(111, fl.size());
		assertEquals("non blank", fl.get(107));
		assertEquals("non blank3", fl.get(110));
	}
	
	@Test
	public final void testAddBlank() {
		final String be = "blank";
		List<String> fl = FullList.<String> builder().blankElement(be)
				.capacity(10).build();
		fl.set(100, "non blank");
		
		assertFalse(fl.add(be));
		assertEquals(101, fl.size());
		
		assertFalse(fl.addAll(Lists.newArrayList(be, be, be)));
		assertEquals(101, fl.size());
		
		assertFalse(fl.addAll(1000, Lists.newArrayList(be, be, be)));
		assertEquals(101, fl.size());
	}

	@Test
	public final void testContains() {
		final String be = "blank";
		List<String> fl = FullList.<String>builder().blankElement(be).capacity(10).build();
		assertTrue(fl.contains(be));
	
		assertTrue(fl.containsAll(Lists.<String>newArrayList(be, be, be)));
		
		fl.set(100, "non blank2");
		assertEquals(101, fl.size());
		assertTrue(fl.contains("non blank2"));
		
		assertFalse(fl.contains("non blank3"));
		assertFalse(fl.containsAll(Lists.<String>newArrayList(be, "non blank3")));
	}
	
	@Test
	public final void testIndexOfBlankElement() {
		final String be = "blank";
		List<String> fl = FullList.<String>builder().blankElement(be).capacity(10).build();

		assertEquals(0, fl.indexOf(be));
		assertEquals(0, fl.lastIndexOf(be));
		
		fl.set(100, "non blank2");
		assertEquals(0, fl.indexOf(be));
		assertEquals(99, fl.lastIndexOf(be));
	}

	@Test
	public final void testIndexOf() {
		final String be = "blank";
		List<String> fl = FullList.<String>builder().blankElement(be).capacity(10).build();

		assertEquals(-1, fl.indexOf("foo"));
	}

	@Test
	public final void testRemove() {
		final String be = "blank";
		List<String> fl = FullList.<String>builder().blankElement(be).capacity(10).build();
		fl.set(100, "non blank2");

		assertEquals(be, fl.remove(10));
		assertEquals(be, fl.remove(1000));
	}
	
	@Test
	public final void testClear() {
		final String be = "blank";
		List<String> fl = FullList.<String>builder().blankElement(be).capacity(10).build();
		fl.set(100, "non blank2");
		fl.clear();
		assertTrue(fl.isEmpty());
		assertEquals(be, fl.get(100));	}
}
