/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WriteUtilTest {
	private WriteUtil util;
	
	@Before
	public void setUp() {
		this.util = WriteUtil.create();
	}

	@Test
	public final void test() {
		Assert.assertEquals("1", this.util.toString(1));
		Assert.assertEquals("1", this.util.toString(1));
		Assert.assertEquals("1001", this.util.toString(1001));
		Assert.assertEquals("1001", this.util.toString(1001));
		Assert.assertEquals("-500", this.util.toString(-500));
		Assert.assertEquals("-500", this.util.toString(-500));
		Assert.assertEquals("-1001", this.util.toString(-1001));
		Assert.assertEquals("-1001", this.util.toString(-1001));
	}



}
