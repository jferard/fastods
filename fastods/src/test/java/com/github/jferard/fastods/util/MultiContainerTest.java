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
import com.github.jferard.fastods.util.Container.Mode;
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
	public enum Dest {
		CONTENT_AUTOMATIC_STYLES, STYLES_AUTOMATIC_STYLES, STYLES_COMMON_STYLES,
	}

	private MultiContainer<String, Dest, Integer> c;

	@Before
	public void setUp() throws Exception {
		this.c = new MultiContainer<String, Dest, Integer>(Dest.class);
	}

	@Test
	public final void testCreateThenUpdate() {
		Assert.assertTrue(this.c.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1, Mode.CREATE_OR_UPDATE));
		Assert.assertEquals(Integer.valueOf(1), this.c
				.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().next());
		Assert.assertTrue(this.c.add("a", Dest.STYLES_AUTOMATIC_STYLES, 2, Mode.CREATE_OR_UPDATE));
		Assert.assertEquals(Integer.valueOf(2), this.c
				.getValues(Dest.STYLES_AUTOMATIC_STYLES).iterator().next());
		Assert.assertFalse(this.c.getValues(Dest.CONTENT_AUTOMATIC_STYLES)
				.iterator().hasNext());
		Assert.assertFalse(this.c.getValues(Dest.STYLES_COMMON_STYLES)
				.iterator().hasNext());
	}

	@Test
	public final void testCreateTwice() {
		Assert.assertTrue(
				this.c.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1, Mode.CREATE));
		Assert.assertEquals(Integer.valueOf(1), this.c
				.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().next());
		Assert.assertFalse(
				this.c.add("a", Dest.STYLES_AUTOMATIC_STYLES, 2, Mode.CREATE));
		Assert.assertEquals(Integer.valueOf(1), this.c
				.getValues(Dest.CONTENT_AUTOMATIC_STYLES).iterator().next());
		Assert.assertFalse(this.c.getValues(Dest.STYLES_AUTOMATIC_STYLES)
				.iterator().hasNext());
		Assert.assertFalse(this.c.getValues(Dest.STYLES_COMMON_STYLES)
				.iterator().hasNext());
	}

	@Test
	public final void testEmpty() {
		for (final Dest s : Dest.values()) {
			Assert.assertFalse(this.c.getValues(s).iterator().hasNext());
			Assert.assertEquals(0, this.c.getValueByKey(s).size());
		}
	}

	@Test
	public final void testUpdateWithoutCreation() {
		Assert.assertFalse(
				this.c.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1, Mode.UPDATE));
		for (final Dest s : Dest.values())
			Assert.assertFalse(this.c.getValues(s).iterator().hasNext());
	}

	@Test(expected = IllegalStateException.class)
	public final void testCreateAfterFreeze() {
	    this.c.freeze();
				this.c.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1, Mode.CREATE);
	}

    @Test
    public final void testCreateAfterDebug() {
        final Logger logger = Logger.getLogger("debug");
        final Handler handler = TestHelper.getMockHandler(logger);

        PowerMock.resetAll();

        // PLAY
        handler.publish(EasyMock.isA(LogRecord.class));
        handler.close();
        EasyMock.expectLastCall().anyTimes();

        PowerMock.replayAll();

        this.c.debug();
        this.c.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1, Mode.CREATE);
    }

    @Test
    public final void testGet() {
        this.c.add("a", Dest.CONTENT_AUTOMATIC_STYLES, 1, Mode.CREATE);
        Assert.assertEquals(Integer.valueOf(1), this.c.get("a", Dest.CONTENT_AUTOMATIC_STYLES));
        Assert.assertEquals(null, this.c.get("a", Dest.STYLES_AUTOMATIC_STYLES));
        Assert.assertEquals(null, this.c.get("b", Dest.CONTENT_AUTOMATIC_STYLES));

        final Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("a", 1);
        Assert.assertEquals(m, this.c.getValueByKey(Dest.CONTENT_AUTOMATIC_STYLES));
    }
}
