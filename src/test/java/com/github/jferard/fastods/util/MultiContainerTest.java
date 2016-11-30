package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.util.Container.Mode;

public class MultiContainerTest {
	private MultiContainer<String, Integer, Dest> c;

	public enum Dest {
		CONTENT_AUTOMATIC_STYLES,
		STYLES_AUTOMATIC_STYLES,
		STYLES_COMMON_STYLES,
	}
	
	@Before
	public void setUp() throws Exception {
		this.c = new MultiContainer<String, Integer, Dest>(Dest.class);
	}

	@Test
	public final void testEmpty() {
		for (Dest s : Dest.values())
			Assert.assertFalse(this.c.getValues(s).iterator().hasNext());
	}

	@Test
	public final void testCreateTwice() {
		Assert.assertTrue(this.c.add("a", 1, Dest.CONTENT_AUTOMATIC_STYLES, Mode.CREATE));
		Assert.assertEquals(Integer.valueOf(1), this.c.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().next());
		Assert.assertFalse(this.c.add("a", 2, Dest.STYLES_AUTOMATIC_STYLES, Mode.CREATE));
		Assert.assertEquals(Integer.valueOf(1), this.c.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().next());
		Assert.assertFalse(this.c.getValues(Dest.STYLES_AUTOMATIC_STYLES).iterator().hasNext());
		Assert.assertFalse(this.c.getValues(Dest.STYLES_COMMON_STYLES).iterator().hasNext());
	}
	
	@Test
	public final void testUpdateWithoutCreation() {
		Assert.assertFalse(this.c.add("a", 1, Dest.CONTENT_AUTOMATIC_STYLES, Mode.UPDATE));
		for (Dest s : Dest.values())
			Assert.assertFalse(this.c.getValues(s).iterator().hasNext());
	}
	
	@Test
	public final void testCreateThenUpdate() {
		Assert.assertTrue(this.c.add("a", 1, Dest.CONTENT_AUTOMATIC_STYLES, Mode.CREATE_OR_UPDATE));
		Assert.assertEquals(Integer.valueOf(1), this.c.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().next());
		Assert.assertTrue(this.c.add("a", 2, Dest.STYLES_AUTOMATIC_STYLES, Mode.CREATE_OR_UPDATE));
		Assert.assertEquals(Integer.valueOf(2), this.c.getValues(Dest.STYLES_AUTOMATIC_STYLES).iterator().next());
		Assert.assertFalse(this.c.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().hasNext());
		Assert.assertFalse(this.c.getValues(Dest.STYLES_COMMON_STYLES).iterator().hasNext());
	}
}
