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

import com.github.jferard.fastods.style.BorderAttribute;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file BenchIT.java is part of FastODS.
 */
public class BorderAttributeTest {
	
	private XMLUtil util;

	@Test
	public final void basicTest() throws IOException {
		BorderAttribute ba = BorderAttribute.builder().borderSize("1cm")
				.borderColor(Color.ALICEBLUE)
				.borderStyle(BorderAttribute.Style.SOLID)
				.position(BorderAttribute.Position.ALL).build();
		Assert.assertEquals("1cm solid #F0F8FF",
				ba.toXMLAttributeValue());
	}

	@Test
	public final void nullTest() throws IOException {
		BorderAttribute ba = BorderAttribute.builder()
				.build();
		Assert.assertEquals("", ba.toXMLAttributeValue());
	}

	@Test
	public final void nullSizeTest() throws IOException {
		BorderAttribute ba = BorderAttribute.builder().borderColor(Color.AQUAMARINE).build();
		Assert.assertEquals("solid #7FFFD4",
				ba.toXMLAttributeValue());
	}

	@Test
	public final void nullColorTest() throws IOException {
		BorderAttribute ba = BorderAttribute.builder().borderSize("1cm").build();
		Assert.assertEquals("1cm", ba.toXMLAttributeValue());
	}

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

}
