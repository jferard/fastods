package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BorderStyleTest {
	
	private Util util;

	@Test
	public final void basicTest() throws IOException {
		BorderStyle bs = BorderStyle.builder().borderSize("1cm")
				.borderColor(Util.COLOR_AliceBlue)
				.borderStyle(BorderStyle.BORDER_SOLID)
				.position(BorderStyle.POSITION_ALL).build();
		StringBuilder sb = new StringBuilder();
		bs.appendXML(this.util, sb);
		Assert.assertEquals("fo:border=\"1cm solid #F0F8FF\"",
				sb.toString().trim());
	}

	@Test
	public final void nullTest() throws IOException {
		BorderStyle bs = BorderStyle.builder().borderStyle(-1).position(-1)
				.build();
		StringBuilder sb = new StringBuilder();
		bs.appendXML(this.util, sb);
		// was fo:border="[null solid null]" but should be empty
		Assert.assertEquals("", sb.toString().trim());
	}

	@Test
	public final void nullSizeTest() throws IOException {
		BorderStyle bs = BorderStyle.builder().borderColor(Util.COLOR_AliceBlue)
				.borderStyle(-1).position(-1).build();
		StringBuilder sb = new StringBuilder();
		bs.appendXML(this.util, sb);
		// was fo:border="null solid #F0F8FF" but should be fo:border="solid
		// #F0F8FF"
		Assert.assertEquals("fo:border=\"solid #F0F8FF\"",
				sb.toString().trim());
	}

	@Test
	public final void nullColorTest() throws IOException {
		BorderStyle bs = BorderStyle.builder().borderSize("1cm")
				.borderStyle(-1).position(-1).build();
		StringBuilder sb = new StringBuilder();
		bs.appendXML(this.util, sb);
		// was fo:border="1cm solid null" but should be fo:border="1cm"
		Assert.assertEquals("fo:border=\"1cm \"", sb.toString().trim());
	}

	@Before
	public void setUp() {
		this.util = Util.getInstance();
	}

}
