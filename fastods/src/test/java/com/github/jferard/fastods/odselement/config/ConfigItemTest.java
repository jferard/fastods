/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods.odselement.config;

import com.github.jferard.fastods.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ConfigItemTest {
    @Test
    public final void test() {
        final ConfigItem loadReadonly = new ConfigItem("LoadReadonly", "boolean", "false");
        Assert.assertEquals("LoadReadonly", loadReadonly.getName());
        Assert.assertEquals("boolean", loadReadonly.getType());
        Assert.assertEquals("false", loadReadonly.getValue());
    }

    @Test
    public final void testElement() {
        final ConfigItem loadReadonly = ConfigItem.create(ConfigElement.LOAD_READONLY, "false");
        Assert.assertEquals("LoadReadonly", loadReadonly.getName());
        Assert.assertEquals("boolean", loadReadonly.getType());
        Assert.assertEquals("false", loadReadonly.getValue());
    }

    @Test
    public final void testXML() throws IOException {
        final ConfigItem loadReadonly = new ConfigItem("LoadReadonly", "boolean", "false");
        TestHelper.assertXMLEquals("<config:config-item config:name=\"LoadReadonly\" " +
                "config:type=\"boolean\">false</config:config-item>", loadReadonly);
    }

    @Test
    public final void testXMLEscape() throws IOException {
        final ConfigItem escape = new ConfigItem("LoadReadonly", "&", "<");
        TestHelper.assertXMLEquals(
                "<config:config-item config:name=\"LoadReadonly\" config:type=\"&amp;\">&lt;" +
                        "</config:config-item>", escape);
    }
}
