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
package com.github.jferard.fastods.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class MultiContainerTest {
    private MultiContainer<String, Dest, Integer> container;
    private Logger logger;

    @Before
    public void setUp() {
        this.logger = Logger.getLogger("");
        this.container = new MultiContainer<String, Dest, Integer>(this.logger, Dest.class);
        this.container.debug();
        PowerMock.resetAll();
        PowerMock.replayAll();
    }

    @After
    public void tearDown() {
        PowerMock.resetAll();
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
        Iterator<Integer> iterator =
                this.container.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), iterator.next());
        Assert.assertFalse(iterator.hasNext());

        // shift "a" from content to styles
        Assert.assertTrue(this.container.add("a", Dest.STYLES_AUTOMATIC_STYLES, 2));

        Assert.assertFalse(
                this.container.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().hasNext());
        iterator = this.container.getValues(Dest.STYLES_AUTOMATIC_STYLES).iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(2), iterator.next());
        Assert.assertFalse(iterator.hasNext());
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
        for (final Dest s : Dest.values()) {
            Assert.assertFalse(this.container.getValues(s).iterator().hasNext());
        }
    }

    @Test
    public final void testCreateAfterFreeze() {
        this.container.freeze();
        Assert.assertThrows(IllegalStateException.class,
                () -> this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1));
    }

    @Test
    public final void testCreateAfterDebug() {
        PowerMock.resetAll();
        this.logger.severe("MultiContainer put(a, 1) in CONTENT_AUTOMATIC_STYLES");

        PowerMock.replayAll();
        this.container.debug();
        final boolean ret = this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1);

        PowerMock.verifyAll();
        Assert.assertTrue(ret);
    }

    @Test
    public final void testAddTwiceCreate() {
        PowerMock.resetAll();
        this.logger.severe("MultiContainer put(a, 1) in CONTENT_AUTOMATIC_STYLES");

        PowerMock.replayAll();
        this.container.debug();
        this.container.setMode(Container.Mode.CREATE);
        final boolean ret1 = this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1);
        final boolean ret2 = this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1);

        PowerMock.verifyAll();
        Assert.assertTrue(ret1);
        Assert.assertFalse(ret2);
    }

    @Test
    public final void testAddTwiceUpdateClosed() {
        PowerMock.resetAll();
        this.logger.severe("MultiContainer put(a, 1) in CONTENT_AUTOMATIC_STYLES");

        PowerMock.replayAll();
        this.container.debug();
        final boolean ret1 = this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1);
        this.container.freeze();
        Assert.assertThrows(IllegalStateException.class,
                () -> this.container.add("a", Dest.STYLES_AUTOMATIC_STYLES, 1));

        PowerMock.verifyAll();
        Assert.assertTrue(ret1);
    }

    @Test
    public final void testGet() {
        this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1);
        Assert.assertEquals(Integer.valueOf(1),
                this.container.get("a", Dest.CONTENT_AUTOMATIC_STYLES));
        Assert.assertNull(this.container.get("a", Dest.STYLES_AUTOMATIC_STYLES));
        Assert.assertNull(this.container.get("b", Dest.CONTENT_AUTOMATIC_STYLES));

        final Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("a", 1);
        Assert.assertEquals(m, this.container.getValueByKey(Dest.CONTENT_AUTOMATIC_STYLES));
    }

    @Test
    public final void testToString() {
        this.container.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1);
        Assert.assertTrue(Arrays.asList(
                "{CONTENT_AUTOMATIC_STYLES={a=1}, STYLES_COMMON_STYLES={}, STYLES_AUTOMATIC_STYLES={}}",
                "{CONTENT_AUTOMATIC_STYLES={a=1}, STYLES_AUTOMATIC_STYLES={}, STYLES_COMMON_STYLES={}}",
                "{STYLES_COMMON_STYLES={}, CONTENT_AUTOMATIC_STYLES={a=1}, STYLES_AUTOMATIC_STYLES={}}",
                "{STYLES_AUTOMATIC_STYLES={}, CONTENT_AUTOMATIC_STYLES={a=1}, STYLES_COMMON_STYLES={}}",
                "{STYLES_COMMON_STYLES={}, STYLES_AUTOMATIC_STYLES={}, CONTENT_AUTOMATIC_STYLES={a=1}}",
                "{STYLES_AUTOMATIC_STYLES={}, STYLES_COMMON_STYLES={}, CONTENT_AUTOMATIC_STYLES={a=1}}"
                ).contains(this.container.toString()));
    }

    public enum Dest {
        CONTENT_AUTOMATIC_STYLES, STYLES_AUTOMATIC_STYLES, STYLES_COMMON_STYLES,
    }
}
