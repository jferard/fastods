package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.util.PositionUtil.Position;

public class PositionUtilTest {
	PositionUtil util;

	@Before
	public void setUp() {
		this.util = new PositionUtil();
	}

	@Test
	public final void testA5() {
		final Position position = this.util.getPosition("A5");
		Assert.assertEquals(0, position.getColumn());
		Assert.assertEquals(4, position.getRow());
	}

	@Test
	public final void testAB6666() {
		final Position position = this.util.getPosition("AB6666");
		Assert.assertEquals(27, position.getColumn());
		Assert.assertEquals(6665, position.getRow());
	}

	@Test
	public final void testB6() {
		final Position position = this.util.getPosition("B6");
		Assert.assertEquals(1, position.getColumn());
		Assert.assertEquals(5, position.getRow());
	}

	@Test
	public final void testdBd6() {
		final Position position = this.util.getPosition("$B$6");
		Assert.assertEquals(1, position.getColumn());
		Assert.assertEquals(5, position.getRow());
	}

	@Test
	public final void testdBd6d() {
		final Position position = this.util.getPosition("$B$6$");
		Assert.assertNull(position);
	}

	@Test
	public final void testddBd6d() {
		final Position position = this.util.getPosition("$$B$6$");
		Assert.assertNull(position);
	}

	@Test
	public final void testMinusAB6666() {
		final Position position = this.util.getPosition("-AB6666");
		Assert.assertNull(position);
	}

	@Test
	public final void testWeirdPos() {
		Assert.assertNull(this.util.getPosition("_6"));
		Assert.assertNull(this.util.getPosition("@6"));
		Assert.assertNull(this.util.getPosition("A@6"));
		Assert.assertNull(this.util.getPosition("A_6"));

		Assert.assertNull(this.util.getPosition("AA$."));
		Assert.assertNull(this.util.getPosition("AA$A"));

		Assert.assertNull(this.util.getPosition("AA$9."));
		Assert.assertNull(this.util.getPosition("AA$9A"));
	}
}
