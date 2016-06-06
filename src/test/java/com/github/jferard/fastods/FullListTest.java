package com.github.jferard.fastods;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;


public class FullListTest {

	@Test
	public final void testSize() {
		List<String> l = FullList.newList();
		Assert.assertEquals(0, l.size());
		Assert.assertTrue(l.isEmpty());
	}
	
	@Test
	public final void testGet() {
		List<String> l = FullList.newList();
		Assert.assertNull(l.get(10));
		Assert.assertTrue(l.isEmpty());
	}
	
	@Test
	public final void testSetNullAfter() {
		List<String> l = FullList.newList();
		l.set(10, null);
		Assert.assertTrue(l.isEmpty());
	}
	
	@Test
	public final void testSetOne() {
		List<String> l = FullList.newList();
		l.set(1, "a");
		Assert.assertEquals(Arrays.asList(null, "a"), l);
	}

	@Test
	public final void testSetNullBefore() {
		List<String> l = FullList.newList();
		l.add("a");
		l.add("b");
		Assert.assertEquals("a", l.set(0, null));
		Assert.assertEquals(Arrays.asList(null, "b"), l);		
		Assert.assertEquals("b", l.set(1, null));
		Assert.assertTrue(l.isEmpty());
	}

	@Test
	public final void testSetNonNull() {
		List<String> l = FullList.newList();
		l.set(10, "a");
		List<String> l2 = Lists.newArrayList(l);
		Assert.assertEquals(Arrays.asList(null, null, null, null, null, null, null, null, null, null, "a"), l2);		
	}
}
