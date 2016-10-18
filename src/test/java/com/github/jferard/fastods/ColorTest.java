package com.github.jferard.fastods;

import org.junit.Assert;
import org.junit.Test;

public class ColorTest {

	@Test
	public final void test() {
		Assert.assertEquals("#ffffff", Color.createHexColor(255, 255, 255));
		Assert.assertEquals("#ffffff", Color.createHexColor(2550, 2550, 2550));
		Assert.assertEquals("#000000", Color.createHexColor(0, 0, 0));
		Assert.assertEquals("#000000", Color.createHexColor(-10, -10, -10));
		Assert.assertEquals("#174b81", Color.createHexColor(23, 75, 129));
	}

}
