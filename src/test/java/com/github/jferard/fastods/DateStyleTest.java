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

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file BenchTest.java is part of FastODS.
 */
public class DateStyleTest {

	@Test(expected = IllegalArgumentException.class)
	public final void testWithNoName() {
		DateStyle ds = DateStyle.builder(null).build();
	}

	@Test
	public final void test() throws IOException {
		DateStyle ds = DateStyle.builder("test").build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(Util.getInstance(), sb);
		Assert.assertEquals(
				"<number:date-style style:name=\"test\" number:automatic-order=\"false\">"
						+ "<number:day number:style=\"long\"/>"
						+ "<number:text>.</number:text>"
						+ "<number:month number:style=\"long\"/>"
						+ "<number:text>.</number:text><number:year/>"
						+ "</number:date-style>",
				sb.toString());
	}
}
