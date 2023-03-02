/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

/**
 * Created by jferard on 19/05/17.
 */
public class ConfigItemSetTest {
    private ConfigItem item;
    private String itemXML;

    @Before
    public void setUp() throws Exception {
        this.item = new ConfigItem("n", "t", "v");
        this.itemXML = TestHelper.toXML(this.item);
    }

    @Test
    public void testCreateSet() {
        final ConfigItemSet m = new ConfigItemSet("set");
        Assert.assertEquals(0, m.size());
        Assert.assertTrue(m.isEmpty());
        Assert.assertEquals("set", m.getName());
    }

    @Test
    public void testCreateSet2() {
        final ConfigItemSet m = new ConfigItemSet("set");
        m.add(this.item);
        Assert.assertEquals(1, m.size());
        Assert.assertFalse(m.isEmpty());
    }

    @Test
    public void testGetByName() {
        final ConfigItemSet m = new ConfigItemSet("set");
        m.add(this.item);
        Assert.assertEquals(this.item, m.getByName("n"));
        Assert.assertNull(m.getByName("s"));
    }

    @Test
    public void testContains() {
        final ConfigItemSet m = new ConfigItemSet("set");
        m.add(this.item);
        Assert.assertFalse(m.contains("s"));
        Assert.assertTrue(m.contains("n"));
    }

    @Test
    public void testIterator() {
        final ConfigItemSet m = new ConfigItemSet("set");
        m.add(this.item);
        final Iterator<ConfigBlock> i = m.iterator();
        Assert.assertTrue(i.hasNext());
        Assert.assertEquals(this.item, i.next());
        Assert.assertFalse(i.hasNext());
    }

    @Test
    public void testAppendXML() throws Exception {
        final ConfigItemSet m = new ConfigItemSet("set");
        m.add(this.item);
        TestHelper.assertXMLEquals("<config:config-item-set config:name=\"set\">" + this.itemXML +
                "</config:config-item-set>", m);
    }

    @Test
    public void testRemoveByName() {
        final ConfigItemSet m = new ConfigItemSet("set");
        m.add(this.item);
        Assert.assertEquals(1, m.size());
        m.removeByName("s");
        Assert.assertEquals(1, m.size());
        m.removeByName("n");
        Assert.assertEquals(0, m.size());
        Assert.assertTrue(m.isEmpty());
    }
}