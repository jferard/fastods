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

        final StringBuilder sb = new StringBuilder();
        this.item.appendXML(this.util, sb);
        this.itemXML = sb.toString();

        final StringBuilder sb2 = new StringBuilder();
        this.block.appendXML(this.util, sb2);
        this.blockXML = sb2.toString();
    }

    @Test
    public void createSequence() throws Exception {
        final ConfigItemMapEntry seq = ConfigItemMapEntrySequence.createSequence();
        Assert.assertEquals(0, seq.size());
        Assert.assertTrue(seq.isEmpty());
        Assert.assertNull(seq.getName());
    }

    @Test
    public void createNamedSequence() throws Exception {
        Assert.assertEquals(0, this.sequence.size());
        Assert.assertTrue(this.sequence.isEmpty());
        Assert.assertEquals("seq", this.sequence.getName());
    }

    @Test
    public void add() throws Exception {
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
    public void remove() throws Exception {
        this.sequence.add(this.block);
        this.sequence.remove(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void remove2() throws Exception {
        this.sequence.remove(10);
    }

    @Test
    public void appendXML() throws Exception {
        final StringBuilder sb = new StringBuilder();
        this.sequence.add(this.item);
        this.sequence.add(this.block);
        this.sequence.appendXML(this.util, sb);
        Assert.assertEquals("<config:config-item-map-entry config:name=\"seq\">" +
                this.itemXML +
                this.blockXML +
                "</config:config-item-map-entry>", sb.toString());
    }

    @Test
    public void appendXML2() throws Exception {
        final StringBuilder sb = new StringBuilder();
        final ConfigItemMapEntrySequence seq = ConfigItemMapEntrySequence.createSequence();
        seq.add(this.item);
        seq.add(this.block);
        seq.appendXML(this.util, sb);
        Assert.assertEquals("<config:config-item-map-entry>" +
                this.itemXML +
                this.blockXML +
                "</config:config-item-map-entry>", sb.toString());
    }

    @Test
    public void set() throws Exception {
        this.sequence.add(this.block);
        this.sequence.add(this.item);
        Assert.assertEquals(null, this.sequence.set(0, "value"));
        Assert.assertEquals("v", this.sequence.set(1, "value"));
        Assert.assertEquals("value", this.item.getValue());
    }

    @Test
    public void addItem() throws Exception {
        this.sequence.add("name", "type", "value");
        Assert.assertTrue(this.sequence.iterator().next() instanceof ConfigItem);
    }
}