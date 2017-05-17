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
public class ConfigItemMapEntrySetTest {
    private ConfigBlock block;
    private ConfigItemMapEntrySet set;
    private ConfigItem item;
    private XMLUtil util;

    @Before
    public void setUp() throws Exception {
        this.item = new ConfigItem("n", "t", "v");
        this.block = ConfigItemMapEntrySingleton.createSingleton(this.item);
        this.set = ConfigItemMapEntrySet.createSet("seq");
        this.util = XMLUtil.create();
    }

    @Test
    public void createSet() throws Exception {
        final ConfigItemMapEntrySet s = ConfigItemMapEntrySet.createSet();
        Assert.assertEquals(0, s.size());
        Assert.assertTrue(s.isEmpty());
        Assert.assertNull(s.getName());
    }

    @Test
    public void createNamedSequence() throws Exception {
        Assert.assertEquals(0, this.set.size());
        Assert.assertTrue(this.set.isEmpty());
        Assert.assertEquals("seq", this.set.getName());
    }

    @Test
    public void add() throws Exception {
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
    public void remove() throws Exception {
        this.set.add(this.block);
        this.set.remove(0);
    }

    @Test
    public void remove2() throws Exception {
        this.set.remove(new Object());
    }

    @Test
    public void appendXML() throws Exception {
        final StringBuilder sb = new StringBuilder();
        this.set.add("name", "type", "value");
        this.set.appendXML(this.util, sb);
        Assert.assertEquals("<config:config-item-map-entry config:name=\"seq\">" +
                "<config:config-item config:name=\"name\" config:type=\"type\">value</config:config-item>" +
                "</config:config-item-map-entry>", sb.toString());
    }

    @Test
    public void addItem() throws Exception {
        this.set.add("name", "type", "value");
        Assert.assertTrue(this.set.iterator().next() instanceof ConfigItem);
    }

    @Test
    public void set() throws Exception {
        this.set.add(this.item);
        Assert.assertEquals("v", this.set.set("n", "value"));
        Assert.assertEquals(null, this.set.set("name", "value"));
    }
}