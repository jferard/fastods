package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BorderStyleTest {
	
	private Util util;

	@Test
	public final void basicTest() throws IOException {
		BorderAttribute bs = BorderAttribute.builder().borderSize("1cm")
				.borderColor(Util.COLOR_ALICEBLUE)
				.borderStyle(BorderAttribute.Style.SOLID)
				.position(BorderAttribute.Position.ALL).build();
		StringBuilder sb = new StringBuilder();
		bs.appendXML(this.util, sb);
		Assert.assertEquals("fo:border=\"1cm solid #F0F8FF\"",
				sb.toString().trim());
	}

	@Test
	public final void nullTest() throws IOException {
		BorderAttribute bs = BorderAttribute.builder()
				.build();
		StringBuilder sb = new StringBuilder();
		bs.appendXML(this.util, sb);
		// was fo:border="[null solid null]" but should be empty
		Assert.assertEquals("", sb.toString().trim());
	}

	@Test
	public final void nullSizeTest() throws IOException {
		BorderAttribute bs = BorderAttribute.builder().borderColor(Util.COLOR_ALICEBLUE).build();
		StringBuilder sb = new StringBuilder();
		bs.appendXML(this.util, sb);
		// was fo:border="null solid #F0F8FF" but should be fo:border="solid
		// #F0F8FF"
		Assert.assertEquals("fo:border=\"solid #F0F8FF\"",
				sb.toString().trim());
	}

	@Test
	public final void nullColorTest() throws IOException {
		BorderAttribute bs = BorderAttribute.builder().borderSize("1cm").build();
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
