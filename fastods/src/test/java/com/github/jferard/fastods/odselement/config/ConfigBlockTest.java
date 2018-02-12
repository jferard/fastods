/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods.odselement.config;

import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 */
public class ConfigBlockTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = XMLUtil.create();
	}

	@Test
	public void test() throws IOException {
		final Appendable sb = new StringBuilder();
		final ConfigItemSet cb = new ConfigItemSet("root");
		cb.add(new ConfigItem("item1", "int", "1"));
		final ConfigItemSet set1 = new ConfigItemSet("set1");
		set1.add(new ConfigItem("item2", "string", "str"));
		cb.add(set1);
		final ConfigItemMapIndexed map1 = new ConfigItemMapIndexed("map1");
		map1.add(ConfigItemMapEntrySingleton.createSingleton("entry1", new ConfigItem("item3", "short", "0")));
		cb.add(map1);
		final ConfigItemMapNamed map2 = new ConfigItemMapNamed("map2");
		map2.put(ConfigItemMapEntrySingleton.createSingleton("entry2", new ConfigItem("item4", "long", "123456789")));
		cb.add(map2);

		cb.appendXMLContent(util, sb);

		DomTester.assertUnsortedEquals("<config:config-item-set config:name=\"root\">" +
				"<config:config-item config:name=\"item1\" config:type=\"int\">1</config:config-item>" +
				"<config:config-item-set config:name=\"set1\">" +
				"<config:config-item config:name=\"item2\" config:type=\"string\">str</config:config-item>" +
				"</config:config-item-set>" +
				"<config:config-item-map-indexed config:name=\"map1\">" +
				"<config:config-item-map-entry config:name=\"entry1\">" +
				"<config:config-item config:name=\"item3\" config:type=\"short\">0</config:config-item>" +
				"</config:config-item-map-entry>" +
				"</config:config-item-map-indexed>" +
				"<config:config-item-map-named config:name=\"map2\">" +
				"<config:config-item-map-entry config:name=\"entry2\">" +
				"<config:config-item config:name=\"item4\" config:type=\"long\">123456789</config:config-item>" +
				"</config:config-item-map-entry>" +
				"</config:config-item-map-named>" +
				"</config:config-item-set>", sb.toString());
	}
}