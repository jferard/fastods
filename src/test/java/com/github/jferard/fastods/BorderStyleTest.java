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

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file BenchTest.java is part of FastODS.
 */
public class BorderStyleTest {
	
	private Util util;

	@Test
	public final void basicTest() throws IOException {
		BorderAttribute bs = BorderAttribute.builder().borderSize("1cm")
				.borderColor(Color.ALICEBLUE)
				.borderStyle(BorderAttribute.Style.SOLID)
				.position(BorderAttribute.Position.ALL).build();
		StringBuilder sb = new StringBuilder();
		bs.appendXML(this.util, sb, null);
		Assert.assertEquals("fo:border=\"1cm solid #F0F8FF\"",
				sb.toString().trim());
	}

	@Test
	public final void nullTest() throws IOException {
		BorderAttribute bs = BorderAttribute.builder()
				.build();
		StringBuilder sb = new StringBuilder();
		bs.appendXML(this.util, sb, null);
		// was fo:border="[null solid null]" but should be empty
		Assert.assertEquals("", sb.toString().trim());
	}

	@Test
	public final void nullSizeTest() throws IOException {
		BorderAttribute bs = BorderAttribute.builder().borderColor(Color.AQUAMARINE).build();
		StringBuilder sb = new StringBuilder();
		bs.appendXML(this.util, sb, null);
		// was fo:border="null solid #F0F8FF" but should be fo:border="solid
		// #F0F8FF"
		Assert.assertEquals("fo:border=\"solid #7FFFD4\"",
				sb.toString().trim());
	}

	@Test
	public final void nullColorTest() throws IOException {
		BorderAttribute bs = BorderAttribute.builder().borderSize("1cm").build();
		StringBuilder sb = new StringBuilder();
		bs.appendXML(this.util, sb, null);
		// was fo:border="1cm solid null" but should be fo:border="1cm"
		Assert.assertEquals("fo:border=\"1cm \"", sb.toString().trim());
	}

	@Before
	public void setUp() {
		this.util = Util.getInstance();
	}

}
