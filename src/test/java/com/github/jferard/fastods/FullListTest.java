package com.github.jferard.fastods;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;


public class FullListTest {

	@Test(expected=UnsupportedOperationException.class)
	public final void testSize() {
		List<String> l = FullList.newList();
		l.size();
	}
	
	@Test
	public final void testGet() {
		List<String> l = FullList.newList();
		Assert.assertNull(l.get(10));
		Assert.assertFalse(l.iterator().hasNext());
	}
	
	@Test
	public final void testSetNullAfter() {
		List<String> l = FullList.newList();
		l.set(10, null);
		Assert.assertFalse(l.iterator().hasNext());
	}

	@Test
	public final void testSetNullBefore() {
		List<String> l = FullList.newList();
		l.add("a");
		l.add("b");
		Assert.assertEquals("a", l.set(0, null));
		Assert.assertEquals(Arrays.asList(null, "b"), l);		
		Assert.assertEquals("b", l.set(1, null));
		Assert.assertFalse(l.iterator().hasNext());
	}

	@Test
	public final void testSetNonNull() {
		List<String> l = FullList.newList();
		l.set(10, "a");
		List<String> l2 = Lists.newArrayList(l);
		Assert.assertEquals(Arrays.asList(null, null, null, null, null, null, null, null, null, "a"), l2);		
	}
}
