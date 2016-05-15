package com.github.jferard.fastods;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConfigItemTest {
	
	private Util util;

	@Before
	public void setUp() {
		this.util = Util.getInstance();
	}

	@Test
	public final void test() {
		ConfigItem loadReadonly = new ConfigItem("LoadReadonly", "boolean",
				"false");
		Assert.assertEquals(
				"<config:config-item config:name=\"LoadReadonly\" config:type=\"boolean\">false</config:config-item>",
				loadReadonly.toXML(this.util));
	}

	@Test
	public final void testEscape() {
		ConfigItem escape = new ConfigItem("LoadReadonly", "&", "<");
		Assert.assertEquals(
				"<config:config-item config:name=\"LoadReadonly\" config:type=\"&amp;\">&lt;</config:config-item>",
				escape.toXML(this.util));
	}
}
