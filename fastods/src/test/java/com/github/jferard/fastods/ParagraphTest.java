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

import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.io.File;
import java.net.URL;

public class ParagraphTest {
	private XMLUtil util;
	private ParagraphBuilder parBuilder;

	@Before
	public void setUp() {
		this.util = XMLUtil.create();
		this.parBuilder = Paragraph.builder();
	}

	@Test
	public final void testNoSpan() throws IOException {
		final Paragraph par = this.parBuilder.build();
		Assert.assertEquals(0, par.getParagraphElements().size());
		DomTester.assertEquals("<text:p/>", this.getXML());
	}

	@Test
	public final void testSpans() throws IOException {
		this.parBuilder.span("content");
		this.parBuilder.span("text");
		final Paragraph par = this.parBuilder.build();
		Assert.assertEquals("content", par.getParagraphElements().get(0).getText());
		Assert.assertNull(par.getParagraphElements().get(0).getTextStyle());
		Assert.assertEquals("text", par.getParagraphElements().get(1).getText());
		DomTester.assertEquals("<text:p>contenttext</text:p>", this.getXML());
	}

	@Test
	public final void testWithStyle() throws IOException {
		final TextStyle ts = TextProperties.builder().fontStyleNormal()
				.fontWeightNormal().buildStyle("style");

		final Paragraph par = this.parBuilder.style(ts).span("text")
				.build();
		Assert.assertEquals(1, par.getParagraphElements().size());
		DomTester.assertEquals("<text:p text:style-name=\"style\">text</text:p>",
				this.getXML());
	}

	@Test
	public final void testLinks() throws IOException {
		final Table table = PowerMock.createMock(Table.class);

		EasyMock.expect(table.getName()).andReturn("tableName");

		PowerMock.replayAll();
		final Paragraph par = this.parBuilder
				.link("text1", "ref")
				.link("text1", new File("f"))
				.link("text1", new URL("http:a/b"))
				.link("text1", table)
				.build();
		Assert.assertEquals(4, par.getParagraphElements().size());
		DomTester.assertEquals("<text:p>" +
						"<text:a xlink:href=\"#ref\" xlink:type=\"simple\">text1</text:a>" +
						"<text:a xlink:href=\"file:/home/jferard/prog/java/fastods/fastods/f\" xlink:type=\"simple\">text1</text:a>" +
						"<text:a xlink:href=\"http:a/b\" xlink:type=\"simple\">text1</text:a>" +
						"<text:a xlink:href=\"#tableName\" xlink:type=\"simple\">text1</text:a>" +
						"</text:p>",
				this.getXML());

		PowerMock.verifyAll();
	}

	@Test
	public final void testStyledLinks() throws IOException {
		final TextStyle ts = TextProperties.builder().fontStyleNormal()
				.fontWeightNormal().buildStyle("style");
		final Table table = PowerMock.createMock(Table.class);

		EasyMock.expect(table.getName()).andReturn("tableName");

		PowerMock.replayAll();
		final Paragraph par = this.parBuilder
				.styledLink("text1", "ref", ts)
				.styledLink("text1", new File("f"), ts)
				.styledLink("text1", new URL("http:a/b"), ts)
				.styledLink("text1", table, ts)
				.build();
		Assert.assertEquals(4, par.getParagraphElements().size());
		DomTester.assertEquals("<text:p>" +
						"<text:a text:style-name=\"style\" xlink:href=\"#ref\" xlink:type=\"simple\">text1</text:a>" +
						"<text:a text:style-name=\"style\" xlink:href=\"file:/home/jferard/prog/java/fastods/fastods/f\" xlink:type=\"simple\">text1</text:a>" +
						"<text:a text:style-name=\"style\" xlink:href=\"http:a/b\" xlink:type=\"simple\">text1</text:a>" +
						"<text:a text:style-name=\"style\" xlink:href=\"#tableName\" xlink:type=\"simple\">text1</text:a>" +
						"</text:p>",
				this.getXML());

		PowerMock.verifyAll();
	}

	private String getXML() throws IOException {
		final StringBuilder sb = new StringBuilder();
		this.parBuilder.build().appendXMLContent(this.util, sb);
		return sb.toString();
	}
}