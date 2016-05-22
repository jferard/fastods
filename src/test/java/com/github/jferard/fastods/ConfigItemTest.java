package com.github.jferard.fastods;

import java.io.IOException;

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
	public final void test() throws IOException {
		ConfigItem loadReadonly = new ConfigItem("LoadReadonly", "boolean",
				"false");
		StringBuilder sb = new StringBuilder();
		loadReadonly.appendXML(this.util, sb);
		Assert.assertEquals(
				"<config:config-item config:name=\"LoadReadonly\" config:type=\"boolean\">false</config:config-item>",
				sb.toString());
	}

	@Test
	public final void testEscape() throws IOException {
		ConfigItem escape = new ConfigItem("LoadReadonly", "&", "<");
		StringBuilder sb = new StringBuilder();
		escape.appendXML(this.util, sb);
		Assert.assertEquals(
				"<config:config-item config:name=\"LoadReadonly\" config:type=\"&amp;\">&lt;</config:config-item>",
				sb.toString());
	}
}
