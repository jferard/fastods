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
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.Iterator;

/**
 * Created by jferard on 17/05/17.
 */
public class ConfigItemMapEntrySingletonTest {
    private ConfigItemMapEntrySingleton singleton;
    private ConfigItem item;
    private XMLUtil util;

    @Before
    public void setUp() throws Exception {
        this.item = new ConfigItem("n", "t", "v");
        this.singleton = ConfigItemMapEntrySingleton.createSingleton("singleton", this.item);
        this.util = XMLUtil.create();
    }

    @Test
    public void createSingleton() throws Exception {
        final ConfigItemMapEntrySingleton sing = ConfigItemMapEntrySingleton.createSingleton(this.item);
        Assert.assertEquals(1, sing.size());
        Assert.assertFalse(sing.isEmpty());
        Assert.assertNull(sing.getName());
    }

    @Test
    public void createNamedSingleton() throws Exception {
        Assert.assertEquals(1, this.singleton.size());
        Assert.assertFalse(this.singleton.isEmpty());
        Assert.assertEquals("singleton", this.singleton.getName());
    }

    @Test
    public void appendXML() throws Exception {
        final StringBuilder sb = new StringBuilder();


        this.singleton.appendXML(this.util, sb);
        Assert.assertEquals("<config:config-item-map-entry config:name=\"singleton\">" +
                "<config:config-item config:name=\"n\" config:type=\"t\">v</config:config-item>" +
                "</config:config-item-map-entry>", sb.toString());

    }

    @Test(expected = UnsupportedOperationException.class)
    public void addItem() throws Exception {
        this.singleton.add("name", "type", "value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add() throws Exception {
        this.singleton.add(this.item);
    }

    @Test
    public void contains() throws Exception {
        Assert.assertFalse(this.singleton.contains("name"));
        Assert.assertTrue(this.singleton.contains("n"));
    }

    @Test
    public void iterator() {
        final Iterator<ConfigBlock> it = this.singleton.iterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(this.item, it.next());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getByName() {
        this.singleton.getByName("x");
    }

    @Test
    public void set() throws Exception {
        Assert.assertEquals("v", this.singleton.set("value"));
        Assert.assertEquals(null, this.singleton.set("name", "value"));
    }
}