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
package com.github.jferard.fastods.util;

import com.github.jferard.fastods.TestHelper;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ContainerTest {
    private Container<String, Integer> container;

    @Before
    public void setUp() throws Exception {
        this.container = new Container<String, Integer>();
    }

    @Test
    public final void testCreateThenUpdate() {
        Assert.assertTrue(this.container.add("a", 1));
        Assert.assertEquals(Integer.valueOf(1), this.container.getValues().iterator().next());

        this.container.setMode(Container.Mode.UPDATE);
        Assert.assertTrue(this.container.add("a", 2));
        Assert.assertEquals(Integer.valueOf(2), this.container.getValues().iterator().next());
    }

    @Test
    public final void testCreateTwice() {
        Assert.assertTrue(this.container.add("a", 1));
        Assert.assertEquals(Integer.valueOf(1), this.container.getValues().iterator().next());
        Assert.assertFalse(this.container.add("a", 2));
        Assert.assertEquals(Integer.valueOf(1), this.container.getValues().iterator().next());
    }

    @Test
    public final void testEmpty() {
        Assert.assertFalse(this.container.getValues().iterator().hasNext());
    }

    @Test
    public final void testUpdateWithoutCreation() {
        this.container.setMode(Container.Mode.UPDATE);
        Assert.assertFalse(this.container.add("a", 1));
        Assert.assertFalse(this.container.getValues().iterator().hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public final void testCreateAfterFreeze() {
        this.container.freeze();
        this.container.add("a", 1);
    }

    @Test
    public final void testGet() {
        this.container.add("a", 1);
        Assert.assertEquals(Integer.valueOf(1), this.container.get("a"));
        Assert.assertEquals(null, this.container.get("b"));

        final Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("a", 1);
        Assert.assertEquals(m, this.container.getValueByKey());
    }

    @Test
    public final void testCreateAfterDebug() {
        final Logger logger = Logger.getLogger("debug");
        final Handler handler = TestHelper.getMockHandler(logger);

        PowerMock.resetAll();
        handler.publish(EasyMock.isA(LogRecord.class));
        handler.close();
        EasyMock.expectLastCall();

        PowerMock.replayAll();
        this.container.debug();
        final boolean ret = this.container.add("a", 1);
        LogManager.getLogManager().reset();

        PowerMock.verifyAll();
        Assert.assertTrue(ret);
    }
}
