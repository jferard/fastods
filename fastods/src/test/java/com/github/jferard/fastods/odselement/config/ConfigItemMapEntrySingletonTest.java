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
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

/**
 * Created by jferard on 17/05/17.
 */
public class ConfigItemMapEntrySingletonTest {
    private ConfigItemMapEntrySingleton singleton;
    private ConfigItem item;
    private String itemXML;

    @Before
    public void setUp() throws Exception {
        this.item = new ConfigItem("n", "t", "v");
        this.singleton = ConfigItemMapEntrySingleton.createSingleton("singleton", this.item);
        this.itemXML = TestHelper.toXML(this.item);
    }

    @Test
    public void testCreateSingleton() {
        final ConfigItemMapEntrySingleton sing =
                ConfigItemMapEntrySingleton.createSingleton(this.item);
        Assert.assertEquals(1, sing.size());
        Assert.assertFalse(sing.isEmpty());
        Assert.assertNull(sing.getName());
    }

    @Test
    public void testCreateNamedSingleton() {
        Assert.assertEquals(1, this.singleton.size());
        Assert.assertFalse(this.singleton.isEmpty());
        Assert.assertEquals("singleton", this.singleton.getName());
    }

    @Test
    public void testAppendXML() throws Exception {
        TestHelper.assertXMLEquals(
                "<config:config-item-map-entry config:name=\"singleton\">" + this.itemXML +
                        "</config:config-item-map-entry>", this.singleton);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addItem() {
        this.singleton.add("name", "type", "value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add() {
        this.singleton.add(this.item);
    }

    @Test
    public void testContains() {
        Assert.assertFalse(this.singleton.contains("name"));
        Assert.assertTrue(this.singleton.contains("n"));
    }

    @Test
    public void testIterator() {
        final Iterator<ConfigBlock> it = this.singleton.iterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(this.item, it.next());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testGetByName() {
        Assert.assertNull(this.singleton.getByName("x"));
        Assert.assertEquals(this.item, this.singleton.getByName("n"));
    }

    @Test
    public void testSet() {
        Assert.assertEquals("v", this.singleton.set("value"));
        Assert.assertNull(this.singleton.set("name", "value"));
    }

    @Test
    public void testSet2() {
        Assert.assertEquals("v", this.singleton.set("value"));
        Assert.assertEquals("value", this.singleton.set("n", "value2"));
    }

    @Test
    public void testSet3() {
        final ConfigItemMapEntrySingleton sing =
                ConfigItemMapEntrySingleton.createSingleton(this.singleton);
        Assert.assertNull(sing.set("value"));
    }
}