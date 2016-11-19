package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class UtilTest {
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

}
