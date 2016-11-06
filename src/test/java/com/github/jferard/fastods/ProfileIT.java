/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file BenchIT.java is part of FastODS.
 *         
 * Usage : launch jvisualvm.
 * mvn -Dmaven.surefire.debug="-agentpath:\"C:/Program Files/Java/visualvm_138/profiler/lib/deployed/jdk16/windows-amd64/profilerinterface.dll\"=\"C:\Program Files\Java\visualvm_138\profiler\lib\",5140" -Dtest=ProfileIT#testFast test
 */
public class ProfileIT {
	private static final int COL_COUNT = 40;
	private static final int ROW_COUNT = 80000;
	private Random random;
	private long t1;

	@Rule public TestName name = new TestName();
	
	@Before
	public final void setUp() {
		this.random = new Random();
		System.out.println(this.name.getMethodName()+" : filling a " + ROW_COUNT + " rows, "
				+ COL_COUNT + " columns spreadsheet");
		this.t1 = System.currentTimeMillis();
	}
	
	@After
	public final void tearDown() {
		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - this.t1) + " ms");
	}

	@Test
	public final void testFast() {
		// Open the file.
		OdsFile file = OdsFile.create("f20columns.ods");
		final Table table = file.addTable("test", ProfileIT.ROW_COUNT, ProfileIT.COL_COUNT);

		for (int y = 0; y < ROW_COUNT; y++) {
			final HeavyTableRow row = table.nextRow();
			TableCellWalker walker = row.getWalker();
			for (int x = 0; x < COL_COUNT; x++) {
				walker.lastCell();
				walker.setFloatValue(this.random.nextInt(1000));
			}
		}

		file.save();
	}
}
