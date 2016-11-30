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
	public final void testEmpty() {
		Assert.assertFalse(this.c.getValues().iterator().hasNext());
	}

	@Test
	public final void testCreateTwice() {
		Assert.assertTrue(this.c.add("a", 1, Mode.CREATE));
		Assert.assertEquals(Integer.valueOf(1), this.c.getValues().iterator().next());
		Assert.assertFalse(this.c.add("a", 2, Mode.CREATE));
		Assert.assertEquals(Integer.valueOf(1), this.c.getValues().iterator().next());
	}
	
	@Test
	public final void testUpdateWithoutCreation() {
		Assert.assertFalse(this.c.add("a", 1, Mode.UPDATE));
		Assert.assertFalse(this.c.getValues().iterator().hasNext());
	}
	
	@Test
	public final void testCreateThenUpdate() {
		Assert.assertTrue(this.c.add("a", 1, Mode.CREATE_OR_UPDATE));
		Assert.assertEquals(Integer.valueOf(1), this.c.getValues().iterator().next());
		Assert.assertTrue(this.c.add("a", 2, Mode.CREATE_OR_UPDATE));
		Assert.assertEquals(Integer.valueOf(2), this.c.getValues().iterator().next());
	}
}
