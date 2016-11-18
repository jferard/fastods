/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods.entry;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.entry.ConfigItem;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file BenchIT.java is part of FastODS.
 */
public class ConfigItemTest {
	
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void test() {
		ConfigItem loadReadonly = new ConfigItem("LoadReadonly", "boolean",
				"false");
		Assert.assertEquals("LoadReadonly", loadReadonly.getName());
		Assert.assertEquals("boolean", loadReadonly.getType());
		Assert.assertEquals("false", loadReadonly.getValue());
	}
	
	@Test
	public final void testXML() throws IOException {
		ConfigItem loadReadonly = new ConfigItem("LoadReadonly", "boolean",
				"false");
		StringBuilder sb = new StringBuilder();
		loadReadonly.appendXMLToObject(this.util, sb);
		Assert.assertEquals(
				"<config:config-item config:name=\"LoadReadonly\" config:type=\"boolean\">false</config:config-item>",
				sb.toString());
	}

	@Test
	public final void testXMLEscape() throws IOException {
		ConfigItem escape = new ConfigItem("LoadReadonly", "&", "<");
		StringBuilder sb = new StringBuilder();
		escape.appendXMLToObject(this.util, sb);
		Assert.assertEquals(
				"<config:config-item config:name=\"LoadReadonly\" config:type=\"&amp;\">&lt;</config:config-item>",
				sb.toString());
	}
}
