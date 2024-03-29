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
import org.apache.commons.compress.utils.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by jferard on 17/05/17.
 */
public class ConfigItemMapEntrySetTest {
    private ConfigBlock block;
    private ConfigBlock block2;
    private ConfigItemMapEntrySet set;
    private ConfigItem item;
    private String itemXML;

    @Before
    public void setUp() throws Exception {
        this.item = new ConfigItem("n", "t", "v");
        this.block = ConfigItemMapEntrySingleton.createSingleton("b", this.item);
        this.block2 = ConfigItemMapEntrySingleton.createSingleton("b", this.block);
        this.set = ConfigItemMapEntrySet.createSet("seq");
        this.itemXML = TestHelper.toXML(this.item);
    }

    @Test
    public void testCreateEmptySet() {
        final ConfigItemMapEntrySet s = ConfigItemMapEntrySet.createSet();
        Assert.assertEquals(0, s.size());
        Assert.assertTrue(s.isEmpty());
        Assert.assertNull(s.getName());
    }

    @Test
    public void testCreateSingletonSet() {
        final ConfigItemMapEntrySet s =
                new ConfigItemMapEntrySet("n", TestHelper.<ConfigBlock>newSet(this.item));
        Assert.assertEquals(1, s.size());
        Assert.assertFalse(s.isEmpty());
    }

    @Test
    public void testCreateNamedSequence() {
        Assert.assertEquals(0, this.set.size());
        Assert.assertTrue(this.set.isEmpty());
        Assert.assertEquals("seq", this.set.getName());
    }

    @Test
    public void testAdd() {
        this.set.add(this.block);
        Assert.assertEquals(1, this.set.size());
        Assert.assertFalse(this.set.isEmpty());
        Assert.assertEquals("seq", this.set.getName());
        final Iterator<ConfigBlock> it = this.set.iterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(this.block, it.next());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testRemove() {
        this.set.add(this.block);
        this.set.remove(0);
    }

    @Test
    public void testContains() {
        this.set.add(this.block);
        Assert.assertTrue(this.set.contains("b"));
        Assert.assertFalse(this.set.contains("n"));
    }

    @Test
    public void testRemove2() {
        this.set.remove(new Object());
    }

    @Test
    public void testAppendXML() throws Exception {
        this.set.add(this.item);
        TestHelper.assertXMLEquals(
                "<config:config-item-map-entry config:name=\"seq\">" + this.itemXML +
                        "</config:config-item-map-entry>", this.set);
    }

    @Test
    public void testAddItem() {
        this.set.add("name", "type", "value");
        Assert.assertTrue(this.set.iterator().next() instanceof ConfigItem);
    }

    @Test
    public void testAddItemTwice() {
        Assert.assertTrue(this.set.add("name", "type", "value"));
        Assert.assertFalse(this.set.add("name", "type", "value"));
    }

    @Test
    public void testSet() {
        this.set.add(this.item);
        Assert.assertEquals("v", this.set.set("n", "value"));
        Assert.assertNull(this.set.set("name", "value"));
    }

    @Test
    public void testAddBlock() {
        Assert.assertTrue(this.set.add(this.block));
        Assert.assertEquals(Arrays.asList(this.block), Lists.newArrayList(this.set.iterator()));
        Assert.assertFalse(this.set.add(this.block));
        Assert.assertEquals(Arrays.asList(this.block), Lists.newArrayList(this.set.iterator()));
    }

    @Test
    public void testPutBlock() {
        Assert.assertEquals(null, this.set.put(this.block));
        Assert.assertEquals(Arrays.asList(this.block), Lists.newArrayList(this.set.iterator()));
        Assert.assertEquals(this.block, this.set.put(this.block2));
        Assert.assertEquals(Arrays.asList(this.block2), Lists.newArrayList(this.set.iterator()));
        Assert.assertEquals(this.block2, this.set.put(this.block));
        Assert.assertEquals(Arrays.asList(this.block), Lists.newArrayList(this.set.iterator()));
    }
}