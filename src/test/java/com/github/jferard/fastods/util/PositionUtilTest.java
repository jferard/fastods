package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.util.PositionUtil.Position;

public class PositionUtilTest {
	PositionUtil util;

	@Before
	public void setUp() {
		this.util = new PositionUtil(new EqualityUtil());
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
	
	@Test
	public final void testEquals() {
		Position position1 = this.util.getPosition("A5");
		Assert.assertEquals(position1, position1);
		Assert.assertNotEquals(position1, null);
		final Position position2 = this.util.getPosition(4, 0);
		Assert.assertEquals(position1, position2);
		final Position position3 = this.util.getPosition(5, 0);
		Assert.assertNotEquals(position1, position3);
		final Position position4 = this.util.getPosition(4, 1);
		Assert.assertNotEquals(position1, position4);
		Assert.assertEquals(1085, position1.hashCode());
	}
}
