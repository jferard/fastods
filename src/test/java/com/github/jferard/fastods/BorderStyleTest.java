package com.github.jferard.fastods;

import org.junit.Assert;
import org.junit.Test;

public class BorderStyleTest {

	@Test
	public final void basicTest() {
		BorderStyle bs = BorderStyle.builder().borderSize("1cm")
				.borderColor(Util.COLOR_AliceBlue)
				.borderStyle(BorderStyle.BORDER_SOLID)
				.position(BorderStyle.POSITION_ALL).build();
		Assert.assertEquals("fo:border=\"1cm solid #F0F8FF\"",
				bs.toString().trim());
	}

	@Test
	public final void nullTest() {
		BorderStyle bs = BorderStyle.builder().borderStyle(-1).position(-1)
				.build();
		// was fo:border="[null solid null]" but should be empty
		Assert.assertEquals("", bs.toString().trim());
	}

	@Test
	public final void nullSizeTest() {
		BorderStyle bs = BorderStyle.builder().borderColor(Util.COLOR_AliceBlue)
				.borderStyle(-1).position(-1).build();
		// was fo:border="null solid #F0F8FF" but should be fo:border="solid
		// #F0F8FF"
		Assert.assertEquals("fo:border=\"solid #F0F8FF\"",
				bs.toString().trim());
	}

	@Test
	public final void nullColorTest() {
		BorderStyle bs = BorderStyle.builder().borderSize("1cm")
				.borderStyle(-1).position(-1).build();
		// was fo:border="1cm solid null" but should be fo:border="1cm"
		Assert.assertEquals("fo:border=\"1cm \"", bs.toString().trim());
	}

}
