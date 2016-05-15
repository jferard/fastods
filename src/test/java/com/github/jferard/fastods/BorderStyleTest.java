package com.github.jferard.fastods;

import org.junit.Assert;
import org.junit.Test;

public class BorderStyleTest {

	@Test
	public final void basicTest() {
		BorderStyle bs = new BorderStyle("1cm", Util.COLOR_AliceBlue, BorderStyle.BORDER_SOLID, BorderStyle.POSITION_ALL);
		Assert.assertEquals("fo:border=\"1cm solid #F0F8FF\"", bs.toString().trim());
	}
	
	@Test
	public final void nullTest() {
		BorderStyle bs = new BorderStyle(null, null, -1, -1);
		// was fo:border="[null solid null]" but should be empty
		Assert.assertEquals("", bs.toString().trim());
	}
	
	@Test
	public final void nullSizeTest() {
		BorderStyle bs = new BorderStyle(null, Util.COLOR_AliceBlue, -1, -1);
		// was fo:border="null solid #F0F8FF" but should be fo:border="solid #F0F8FF"
		Assert.assertEquals("fo:border=\"solid #F0F8FF\"", bs.toString().trim());
	}
	
	@Test
	public final void nullColorTest() {
		BorderStyle bs = new BorderStyle("1cm", null, -1, -1);
		// was fo:border="1cm solid null" but should be fo:border="1cm"
		Assert.assertEquals("fo:border=\"1cm \"", bs.toString().trim());
	}
	
	

}
