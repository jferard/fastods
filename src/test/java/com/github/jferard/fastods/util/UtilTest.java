package com.github.jferard.fastods.util;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.util.PositionUtil.Position;

public class UtilTest {
	Util util;
	private PositionUtil positionUtil;
	private WriteUtil writeUtil;

	@Before
	public void setUp() {
		this.positionUtil = PowerMock.createMock(PositionUtil.class);
		this.writeUtil = PowerMock.createMock(WriteUtil.class);
		this.util = new Util(this.positionUtil, this.writeUtil);
	}

	@Test
	public final void testPosition() {
		EasyMock.expect(this.positionUtil.getPosition("@"))
				.andReturn(new Position(10, 9));
		PowerMock.replayAll();
		Assert.assertEquals(new Position(10, 9), this.util.getPosition("@"));
		PowerMock.verifyAll();
	}

	@Test
	public final void testInt() {
		EasyMock.expect(this.writeUtil.toString(-1001)).andReturn("-1001@");
		EasyMock.expect(this.writeUtil.toString(1001)).andReturn("1001@");
		PowerMock.replayAll();
		Assert.assertEquals("-1001@", this.util.toString(-1001));
		Assert.assertEquals("1001@", this.util.toString(1001));
		PowerMock.verifyAll();
	}

	@Test
	public final void testEquals() {
		PowerMock.replayAll();
		Assert.assertTrue(this.util.equal(null, null));
		final Object s = "object";
		Assert.assertFalse(this.util.equal(null, s));
		Assert.assertFalse(this.util.equal(s, null));
		Assert.assertTrue(this.util.equal(s, s));
		PowerMock.verifyAll();
	}

}
