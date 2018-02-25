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

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.util.PositionUtil.Position;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class PositionUtilTest {
	PositionUtil util;

	@Before
	public void setUp() {
		this.util = new PositionUtil(new EqualityUtil());
		PowerMock.resetAll();
	}

	@Test
	public final void testA5() {
		final Position position = this.util.getPosition("A5");
		Assert.assertEquals(0, position.getColumn());
		Assert.assertEquals(4, position.getRow());
	}

	@Test
	public final void testAB6666() {
		final Position position = this.util.getPosition("AB6666");
		Assert.assertEquals(27, position.getColumn());
		Assert.assertEquals(6665, position.getRow());
	}

	@Test
	public final void testB6() {
		final Position position = this.util.getPosition("B6");
		Assert.assertEquals(1, position.getColumn());
		Assert.assertEquals(5, position.getRow());
	}

	@Test
	public final void testdBd6() {
		final Position position = this.util.getPosition("$B$6");
		Assert.assertEquals(1, position.getColumn());
		Assert.assertEquals(5, position.getRow());
	}

	@Test
	public final void testdBd6d() {
		final Position position = this.util.getPosition("$B$6$");
		Assert.assertNull(position);
	}

	@Test
	public final void testddBd6d() {
		final Position position = this.util.getPosition("$$B$6$");
		Assert.assertNull(position);
	}

	@Test
	public final void testMinusAB6666() {
		final Position position = this.util.getPosition("-AB6666");
		Assert.assertNull(position);
	}

	@Test
	public final void testWeirdPos() {
		Assert.assertNull(this.util.getPosition("_6"));
		Assert.assertNull(this.util.getPosition("@6"));
		Assert.assertNull(this.util.getPosition("A@6"));
		Assert.assertNull(this.util.getPosition("A_6"));

		Assert.assertNull(this.util.getPosition("AA$."));
		Assert.assertNull(this.util.getPosition("AA$A"));

		Assert.assertNull(this.util.getPosition("AA$9."));
		Assert.assertNull(this.util.getPosition("AA$9A"));
	}
	
	@Test
	public final void testEquals() {
		final Position position1 = this.util.getPosition("A5");
		Assert.assertEquals(position1, position1);
		Assert.assertNotEquals(position1, null);
		final Position position2 = this.util.getPosition(4, 0);
		Assert.assertEquals(position1, position2);
		final Position position3 = this.util.getPosition(5, 0);
		Assert.assertNotEquals(position1, position3);
		final Position position4 = this.util.getPosition(4, 1);
		Assert.assertNotEquals(position1, position4);
		Assert.assertEquals(1085, position1.hashCode());
	}

	@Test
	public final void testAddress() {
		final Position position1 = this.util.getPosition(0,0);
		Assert.assertEquals("A1", position1.toCellAddress());
		final Position position2 = this.util.getPosition(1,2);
		Assert.assertEquals("C2", position2.toCellAddress());
		final Position position3 = this.util.getPosition(0,25);
		Assert.assertEquals("Z1", position3.toCellAddress());
		final Position position4 = this.util.getPosition(0,26);
		Assert.assertEquals("AA1", position4.toCellAddress());
		final Position position5 = this.util.getPosition(0,52);
		Assert.assertEquals("BA1", position5.toCellAddress());
		final Position position6 = this.util.getPosition(0,1023);
		Assert.assertEquals("AMJ1", position6.toCellAddress());
	}

	@Test
	public final void testRangeAddress() {
		Assert.assertEquals("A1:K11", this.util.toRangeAddress(0,0, 10, 10));
	}

	@Test
	public final void testCellAndRangeAddressInTable() {
		// CREATE
		final Table t = PowerMock.createMock(Table.class);

		// PLAY
		EasyMock.expect(t.getName()).andReturn("n").times(3);

		// TEST
		PowerMock.replayAll();
		Assert.assertEquals("n.AMJ1", this.util.toCellAddress(t,0,1023));
		Assert.assertEquals("n.A1:n.K11", this.util.toRangeAddress(t, 0,0, 10, 10));
		PowerMock.verifyAll();
	}
}
