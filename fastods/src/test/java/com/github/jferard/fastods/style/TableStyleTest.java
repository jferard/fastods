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
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class TableStyleTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = XMLUtil.create();
	}

	@Test
	public final void testAddEmptyToFile() {
		final TableStyle ts = TableStyle.builder("test").build();
		final OdsElements odsElements = PowerMock.createMock(OdsElements.class);
		final ObjectStyle objectStyle = ts;

		odsElements.addObjectStyle(objectStyle);
		PowerMock.replayAll();

		ts.addToElements(odsElements);

		PowerMock.verifyAll();
	}

	@Test
	public final void testEmpty() throws IOException {
		final TableStyle ts = TableStyle.builder("test").build();
		final StringBuilder sb = new StringBuilder();
		ts.appendXML(this.util, sb);

		DomTester.assertEquals(
				"<style:style style:name=\"test\" style:family=\"table\" style:master-page-name=\"DefaultMasterPage\">"
						+ "<style:table-properties table:display=\"true\" style:writing-mode=\"lr-tb\"/>"
						+ "</style:style>",
				sb.toString());
	}

	@Test
	public final void testPageStyle() throws IOException {
		final PageStyle ps = PageStyle.builder("p").build();
		final TableStyle ts = TableStyle.builder("test").pageStyle(ps)
				.build();
		final StringBuilder sb = new StringBuilder();
		ts.appendXML(this.util, sb);

		DomTester.assertEquals(
				"<style:style style:name=\"test\" style:family=\"table\" style:master-page-name=\"p\">"
						+ "<style:table-properties table:display=\"true\" style:writing-mode=\"lr-tb\"/>"
						+ "</style:style>",
				sb.toString());
		Assert.assertEquals("test", ts.getName());
	}
}
