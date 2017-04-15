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
package com.github.jferard.fastods;

import java.util.Date;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

/**
 * @author Julien
 *
 */
public class DateValueTest {
	@Test
	public final void test() {
		PowerMock.verifyAll();
		final HeavyTableRow htr = PowerMock.createMock(HeavyTableRow.class);
		final Capture<Date> captured = Capture.newInstance();
		
		// PLAY
		htr.setDateValue(EasyMock.eq(1), EasyMock.capture(captured));
		PowerMock.replayAll();
		
		final Date date = new Date(10);
		final CellValue dv= new DateValue(date);
		date.setTime(0);
		dv.setToRow(htr, 1);
		Assert.assertEquals(10, captured.getValue().getTime());

		PowerMock.verifyAll();
	}

}
