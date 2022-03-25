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
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

/**
 * Created by jferard on 17/05/17.
 */
public class ConfigItemMapEntrySequenceTest {
    private ConfigBlock block;
    private ConfigItemMapEntrySequence sequence;
    private ConfigItem item;
    private XMLUtil util;
    private String itemXML;
    private String blockXML;

    @Before
    public void setUp() throws Exception {
        this.item = new ConfigItem("n", "t", "v");
        this.block = ConfigItemMapEntrySingleton.createSingleton(this.item);
        this.sequence = ConfigItemMapEntrySequence.createSequence("seq");
        this.util = XMLUtil.create();

        this.itemXML = TestHelper.toXML(this.item);
        this.blockXML = TestHelper.toXML(this.block);
    }

    @Test
    public void testCreateSequence() {
        final ConfigItemMapEntry seq = ConfigItemMapEntrySequence.createSequence();
        Assert.assertEquals(0, seq.size());
        Assert.assertTrue(seq.isEmpty());
        Assert.assertNull(seq.getName());
    }

    @Test
    public void testCreateNamedSequence() {
        Assert.assertEquals(0, this.sequence.size());
        Assert.assertTrue(this.sequence.isEmpty());
        Assert.assertEquals("seq", this.sequence.getName());
    }

    @Test
    public void testAdd() {
        this.sequence.add(this.block);
        Assert.assertEquals(1, this.sequence.size());
        Assert.assertFalse(this.sequence.isEmpty());
        Assert.assertEquals("seq", this.sequence.getName());
        final Iterator<ConfigBlock> it = this.sequence.iterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(this.block, it.next());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testRemove() {
        this.sequence.add(this.block);
        this.sequence.remove(0);
    }

    @Test
    public void remove2() {
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> this.sequence.remove(10));
    }

    @Test
    public void testAppendXML() throws Exception {
        this.sequence.add(this.item);
        this.sequence.add(this.block);
        TestHelper.assertXMLEquals(
                "<config:config-item-map-entry config:name=\"seq\">" + this.itemXML +
                        this.blockXML + "</config:config-item-map-entry>", this.sequence);
    }

    @Test
    public void testAppendXML2() throws Exception {
        final ConfigItemMapEntrySequence seq = ConfigItemMapEntrySequence.createSequence();
        seq.add(this.item);
        seq.add(this.block);
        TestHelper.assertXMLEquals("<config:config-item-map-entry>" + this.itemXML + this.blockXML +
                "</config:config-item-map-entry>", seq);
    }

    @Test
    public void testSet() {
        this.sequence.add(this.block);
        this.sequence.add(this.item);
        Assert.assertNull(this.sequence.set(0, "value"));
        Assert.assertEquals("v", this.sequence.set(1, "value"));
        Assert.assertEquals("value", this.item.getValue());
    }

    @Test
    public void testAddItem() {
        this.sequence.add("name", "type", "value");
        Assert.assertTrue(this.sequence.iterator().next() instanceof ConfigItem);
    }
}