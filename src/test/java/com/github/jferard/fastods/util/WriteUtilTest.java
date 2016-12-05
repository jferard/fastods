package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WriteUtilTest {
	private WriteUtil util;
	
	@Before
	public void setUp() throws Exception {
		this.util = new WriteUtil();
	}

	@Test
	public final void test() {
		Assert.assertEquals("1", this.util.toString(1));
		Assert.assertEquals("1", this.util.toString(1));
		Assert.assertEquals("1001", this.util.toString(1001));
		Assert.assertEquals("1001", this.util.toString(1001));
		Assert.assertEquals("-500", this.util.toString(-500));
		Assert.assertEquals("-500", this.util.toString(-500));
		Assert.assertEquals("-1001", this.util.toString(-1001));
		Assert.assertEquals("-1001", this.util.toString(-1001));
	}

}
