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
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MultiContainerTest {
    private MultiContainer<String, Dest, Integer> container;

    @Before
    public void setUp() throws Exception {
        this.container = new MultiContainer<String, Dest, Integer>(Dest.class);
    }

    @Test
    public final void testCreateThenUpdate() {
        Assert.assertTrue(this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1));
        Assert.assertEquals(Integer.valueOf(1),
                this.container.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().next());
        this.container.setMode(Container.Mode.UPDATE);
        Assert.assertTrue(this.container.add("a", Dest.STYLES_AUTOMATIC_STYLES, 2));
        Assert.assertEquals(Integer.valueOf(2),
                this.container.getValues(Dest.STYLES_AUTOMATIC_STYLES).iterator().next());
        Assert.assertFalse(
                this.container.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().hasNext());
        Assert.assertFalse(
                this.container.getValues(Dest.STYLES_COMMON_STYLES).iterator().hasNext());
    }

    @Test
    public final void testCreateTwice() {
        Assert.assertTrue(this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1));
        Assert.assertEquals(Integer.valueOf(1),
                this.container.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().next());
        Assert.assertFalse(this.container.add("a", Dest.STYLES_AUTOMATIC_STYLES, 2));
        Assert.assertEquals(Integer.valueOf(1),
                this.container.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().next());
        Assert.assertFalse(
                this.container.getValues(Dest.STYLES_AUTOMATIC_STYLES).iterator().hasNext());
        Assert.assertFalse(
                this.container.getValues(Dest.STYLES_COMMON_STYLES).iterator().hasNext());
    }

    @Test
    public final void testEmpty() {
        for (final Dest s : Dest.values()) {
            Assert.assertFalse(this.container.getValues(s).iterator().hasNext());
            Assert.assertEquals(0, this.container.getValueByKey(s).size());
        }
    }

    @Test
    public final void testUpdateWithoutCreation() {
        this.container.setMode(Container.Mode.UPDATE);
        Assert.assertFalse(this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1));
        for (final Dest s : Dest.values())
            Assert.assertFalse(this.container.getValues(s).iterator().hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public final void testCreateAfterFreeze() {
        this.container.freeze();
        this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1);
    }

    @Test
    public final void testCreateAfterDebug() {
        final Logger logger = Logger.getLogger("debug");
        final Handler handler = TestHelper.getMockHandler(logger);

        PowerMock.resetAll();
        handler.publish(EasyMock.isA(LogRecord.class));
        handler.close();
        EasyMock.expectLastCall().anyTimes();

        PowerMock.replayAll();
        this.container.debug();
        this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1);

        PowerMock.verifyAll();
    }

    @Test
    public final void testGet() {
        this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1);
        Assert.assertEquals(Integer.valueOf(1),
                this.container.get("a", Dest.CONTENT_AUTOMATIC_STYLES));
        Assert.assertEquals(null, this.container.get("a", Dest.STYLES_AUTOMATIC_STYLES));
        Assert.assertEquals(null, this.container.get("b", Dest.CONTENT_AUTOMATIC_STYLES));

        final Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("a", 1);
        Assert.assertEquals(m, this.container.getValueByKey(Dest.CONTENT_AUTOMATIC_STYLES));
    }

    public enum Dest {
        CONTENT_AUTOMATIC_STYLES, STYLES_AUTOMATIC_STYLES, STYLES_COMMON_STYLES,
    }
}
