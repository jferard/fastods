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
package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class SimpleFooterHeaderTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testAlmostEmptyToMasterStyle() throws IOException {
		final FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER).build();
		final StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		Assert.assertEquals("", sb.toString());
	}

	@Test
	public final void testFourMarginsAndMinHeightToAutomaticStyle()
			throws IOException {
		final TextStyle ts = TextProperties.builder().fontStyleItalic()
				.buildStyle("style");
		final FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER).marginTop("10pt")
				.marginRight("11pt").marginBottom("12pt").marginLeft("13pt")
				.minHeight("120pt").styledContent("text", ts).build();
		final StringBuilder sb = new StringBuilder();
		header.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		DomTester.assertEquals("<style:header-style>"
				+ "<style:header-footer-properties fo:min-height=\"120pt\" fo:margin=\"0cm\" fo:margin-top=\"10pt\" fo:margin-right=\"11pt\" fo:margin-bottom=\"12pt\" fo:margin-left=\"13pt\"/>"
				+ "</style:header-style>", sb.toString());
	}

	@Test
	public final void testMarginsAndMinHeightToAutomaticStyle()
			throws IOException {
		final TextStyle ts = TextProperties.builder().fontStyleItalic()
				.buildStyle("style");
		final FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER).allMargins("10pt")
				.minHeight("120pt").styledContent("text", ts).build();
		final StringBuilder sb = new StringBuilder();
		header.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		DomTester.assertEquals("<style:header-style>"
				+ "<style:header-footer-properties fo:min-height=\"120pt\" fo:margin=\"10pt\"/>"
				+ "</style:header-style>", sb.toString());
	}

	@Test
	public final void testPageToMasterStyle() throws IOException {
		final TextStyle ts1 = TextProperties.builder().buildStyle("test1");
		final TextStyle ts2 = TextProperties.builder().buildStyle("test2");
		final FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER)
				.text(Text.builder()
						.parStyledContent(Text.TEXT_PAGE_NUMBER, ts1)
						.parStyledContent(Text.TEXT_PAGE_COUNT, ts2).build())
				.build();
		final StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		DomTester.assertEquals(
				"<text:p>" + "<text:span text:style-name=\"test1\">"
						+ "<text:page-number>1</text:page-number>"
						+ "</text:span>" + "</text:p>" + "<text:p>"
						+ "<text:span text:style-name=\"test2\">"
						+ "<text:page-count>99</text:page-count>"
						+ "</text:span>" + "</text:p>",
				sb.toString());
	}

	@Test
	public final void testPageToMasterStyle2() throws IOException {
		final TextStyle ts1 = TextProperties.builder().buildStyle("test1");
		final TextStyle ts2 = TextProperties.builder().buildStyle("test2");
		final FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER)
				.text(Text.builder().par()
						.styledSpan(Text.TEXT_PAGE_NUMBER, ts1)
						.styledSpan(Text.TEXT_PAGE_COUNT, ts2).build())
				.build();
		final StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		DomTester.assertEquals("<text:p>"
				+ "<text:span text:style-name=\"test1\">"
				+ "<text:page-number>1</text:page-number>" + "</text:span>"
				+ "<text:span text:style-name=\"test2\">"
				+ "<text:page-count>99</text:page-count>" + "</text:span>"
				+ "</text:p>", sb.toString());
	}

	@Test
	public final void testSimpleFooterToAutomaticStyle() throws IOException {
		final TextStyle ts = TextProperties.builder().fontStyleItalic()
				.buildStyle("style");
		final FooterHeader header = FooterHeader.simpleHeader("text", ts);
		final StringBuilder sb = new StringBuilder();
		header.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		DomTester.assertEquals("<style:header-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
				+ "</style:header-style>", sb.toString());
	}

	@Test
	public final void testSimpleFooterToMasterStyle() throws IOException {
		final TextStyle ts = TextProperties.builder().fontStyleItalic()
				.buildStyle("style");
		final FooterHeader header = FooterHeader.simpleHeader("text", ts);
		final StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		DomTester
				.assertEquals(
						"<text:p>" + "<text:span text:style-name=\"style\">"
								+ "text" + "</text:span>" + "</text:p>",
						sb.toString());
	}

	@Test
	public final void testSimpleStyledTextToAutomaticStyle()
			throws IOException {
		final TextStyle ts = TextProperties.builder().buildStyle("test");
		final String text = "text";
		final FooterHeader footer = FooterHeader
				.simpleBuilder(FooterHeader.Type.FOOTER).styledContent(text, ts)
				.build();
		final StringBuilder sb = new StringBuilder();
		footer.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		DomTester.assertEquals("<style:footer-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
				+ "</style:footer-style>", sb.toString());
	}

	@Test
	public final void testSimpleStyledTextToMasterStyle() throws IOException {
		final TextStyle ts = TextProperties.builder().buildStyle("test");
		final FooterHeader footer = FooterHeader
				.simpleBuilder(FooterHeader.Type.FOOTER)
				.styledContent("text", ts).build();
		final StringBuilder sb = new StringBuilder();
		footer.appendXMLToMasterStyle(this.util, sb);
		DomTester.assertEquals("<text:p>"
				+ "<text:span text:style-name=\"test\">text</text:span>"
				+ "</text:p>", sb.toString());
	}
	
	@Test
	public final void testAddEmbbeded() throws IOException {
		StylesContainer sc = PowerMock.createMock(StylesContainer.class);
		
		PowerMock.replayAll();
		
		final FooterHeader footer = FooterHeader
				.simpleBuilder(FooterHeader.Type.FOOTER)
				.content("text").build();

		footer.addEmbeddedStylesToStylesElement(sc);
		footer.addEmbeddedStylesToStylesElement(sc, Mode.CREATE);
		PowerMock.verifyAll();
	}
}
