package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class EqualityUtilTest {
	EqualityUtil equalityUtil;

	@Before
	public void setUp() {
		PowerMock.createMock(PositionUtil.class);
		PowerMock.createMock(WriteUtil.class);
		this.equalityUtil = new EqualityUtil();
	}

	@Test
	public final void testEquals() {
		PowerMock.replayAll();
		Assert.assertTrue(this.equalityUtil.equal(null, null));
		final Object s = "object";
		Assert.assertFalse(this.equalityUtil.equal(null, s));
		Assert.assertFalse(this.equalityUtil.equal(s, null));
		Assert.assertTrue(this.equalityUtil.equal(s, s));
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testDifferent() {
		PowerMock.replayAll();
		Assert.assertFalse(this.equalityUtil.different(null, null));
		final Object s = "object";
		Assert.assertTrue(this.equalityUtil.different(null, s));
		Assert.assertTrue(this.equalityUtil.different(s, null));
		Assert.assertFalse(this.equalityUtil.different(s, s));
		PowerMock.verifyAll();
	}

	@Test
	public final void testHashObjects() {
		PowerMock.replayAll();
		Object[] integers = {null, Integer.valueOf(1), null, Integer.valueOf(2)};
		Assert.assertEquals(924484, this.equalityUtil.hashObjects(integers));
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testHashInts() {
		PowerMock.replayAll();
		Assert.assertEquals(994, this.equalityUtil.hashInts(1, 2));
		PowerMock.verifyAll();
	}
}
