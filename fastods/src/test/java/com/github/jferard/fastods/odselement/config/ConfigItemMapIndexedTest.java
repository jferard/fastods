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
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

/**
 * Created by jferard on 19/05/17.
 */
public class ConfigItemMapIndexedTest {
    private ConfigItem item;
    private ConfigItemMapEntrySingleton block;
    private ConfigItemMapEntrySet set;
    private XMLUtil util;
    private String setXML;

    @Before
    public void setUp() throws Exception {
        this.item = new ConfigItem("n", "t", "v");
        this.set = ConfigItemMapEntrySet.createSet("seq");
        this.set.add(this.item);
        this.util = XMLUtil.create();
        this.setXML = TestHelper.toXML(this.set);
    }

    @Test
    public void testCreateMapIndexed() {
        final ConfigItemMapIndexed m = new ConfigItemMapIndexed("mapindexed");
        Assert.assertEquals(0, m.size());
        Assert.assertTrue(m.isEmpty());
        Assert.assertEquals("mapindexed", m.getName());
    }

    @Test
    public void testCreateMapIndexed2() {
        final ConfigItemMapIndexed m = new ConfigItemMapIndexed("mapindexed");
        m.add(this.set);
        Assert.assertEquals(1, m.size());
        Assert.assertFalse(m.isEmpty());
    }

    @Test
    public void testSet() {
        final ConfigItemMapIndexed m = new ConfigItemMapIndexed("mapindexed");
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> m.set(1, this.set));
    }

    @Test
    public void testRemove() {
        final ConfigItemMapIndexed m = new ConfigItemMapIndexed("mapindexed");
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> m.remove(1));

    }

    @Test
    public void testSetOk() {
        final ConfigItemMapIndexed m = new ConfigItemMapIndexed("mapindexed");
        m.add(this.set);
        m.set(0, this.set);
        Assert.assertEquals(1, m.size());
        Assert.assertFalse(m.isEmpty());
    }

    @Test
    public void testRemoveOk() {
        final ConfigItemMapIndexed m = new ConfigItemMapIndexed("mapindexed");
        m.add(this.set);
        m.remove(0);
        Assert.assertEquals(0, m.size());
        Assert.assertTrue(m.isEmpty());
    }

    @Test
    public void testContains() {
        final ConfigItemMapIndexed m = new ConfigItemMapIndexed("mapindexed");
        m.add(this.set);
        Assert.assertTrue(m.contains(this.set));
    }

    @Test
    public void testGet() {
        final ConfigItemMapIndexed m = new ConfigItemMapIndexed("mapindexed");
        m.add(0, this.set);
        Assert.assertEquals(this.set, m.get(0));
    }

    @Test
    public void testIterator() {
        final ConfigItemMapIndexed m = new ConfigItemMapIndexed("mapindexed");
        m.add(this.set);
        final Iterator<ConfigItemMapEntry> i = m.iterator();
        Assert.assertTrue(i.hasNext());
        Assert.assertEquals(this.set, i.next());
        Assert.assertFalse(i.hasNext());
    }

    @Test
    public void testAppendXMLRepresentation() throws Exception {
        final ConfigItemMapIndexed m = new ConfigItemMapIndexed("mapindexed");
        m.add(this.set);
        TestHelper.assertXMLEquals(
                "<config:config-item-map-indexed config:name=\"mapindexed\">" + this.setXML +
                        "</config:config-item-map-indexed>", m);
    }

}