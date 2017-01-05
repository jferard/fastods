/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods.testutil;

import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

/**
 */
public class DomTesterTest {
	@Before
	public void setUp() throws ParserConfigurationException {
	}

	@Test
	public void testEmpty() {
		DomTester.assertEquals("", "");
	}

	@Test
	public void testSelfClosing() {
		DomTester.assertEquals("<a/>", "<a></a>");
	}

	@Test
	public void testAttributes() {
		DomTester.assertEquals("<a b=\"1\" c=\"2\"/>", "<a b=\"1\" c=\"2\"/>");
		DomTester.assertEquals("<a b=\"1\" c=\"2\"/>", "<a c=\"2\" b=\"1\"/>");
		DomTester.assertNotEquals("<a b=\"2\" c=\"1\"/>", "<a c=\"2\" b=\"1\"/>");
	}

	@Test
	public void testSortedChildren() {
		DomTester.assertEquals("<a><b/><c/></a>", "<a><b></b><c></c></a>");
		DomTester.assertNotEquals("<a><b/><c/></a>", "<a><c/><b/></a>");
	}

	@Test
	public void testUnsortedChildren() {
		DomTester.assertUnsortedEquals("<a><b/><c/></a>", "<a><b></b><c></c></a>");
		DomTester.assertUnsortedEquals("<a><b><x/><y/></b><c/></a>", "<a><c/><b><y/><x/></b></a>");
		DomTester.assertUnsortedEquals("<a><b/><c/></a>", "<a><c/><b/></a>");
	}
}