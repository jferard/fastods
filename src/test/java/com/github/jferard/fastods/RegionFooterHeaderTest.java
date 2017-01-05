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

import com.github.jferard.fastods.testutil.DomTester;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.FooterHeader.Region;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.Container.Mode;

public class RegionFooterHeaderTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testNullOrEmptyRegions() throws IOException {
		final FooterHeader header = FooterHeader
				.regionBuilder(FooterHeader.Type.HEADER).region(Region.LEFT)
				.content("l").region(Region.CENTER).text(Text.builder().build())
				.build();

		final StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		DomTester.assertEquals(
				"<style:region-left>" + "<text:p>l</text:p>"
						+ "</style:region-left>" + "<style:region-center>"
						+ "<text:p>" + "</text:p>" + "</style:region-center>",
				sb.toString());
	}

	@Test
	public final void testRegionsToMasterStyle() throws IOException {
		final TextStyle ts1 = TextProperties.builder().fontStyleItalic()
				.buildStyle("style1");
		final TextStyle ts2 = TextProperties.builder().fontStyleNormal()
				.fontWeightNormal().buildStyle("style2");
		final TextStyle ts3 = TextProperties.builder().fontWeightBold()
				.buildStyle("style3");
		final FooterHeader header = FooterHeader
				.regionBuilder(FooterHeader.Type.HEADER).region(Region.LEFT)
				.styledContent("left-text", ts1).region(Region.CENTER)
				.styledContent("center-text", ts2).region(Region.RIGHT)
				.styledContent("right-text", ts3).build();
		final StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		DomTester.assertEquals("<style:region-left>" + "<text:p>"
				+ "<text:span text:style-name=\"style1\">left-text</text:span>"
				+ "</text:p>" + "</style:region-left>" + "<style:region-center>"
				+ "<text:p>"
				+ "<text:span text:style-name=\"style2\">center-text</text:span>"
				+ "</text:p>" + "</style:region-center>"
				+ "<style:region-right>" + "<text:p>"
				+ "<text:span text:style-name=\"style3\">right-text</text:span>"
				+ "</text:p>" + "</style:region-right>", sb.toString());
	}

	@Test
	public final void testRegionToAutomaticStyle() throws IOException {
		final TextStyle ts = TextProperties.builder().fontWeightBold()
				.buildStyle("style");
		final FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.CENTER)
				.styledContent(Text.TEXT_PAGE_NUMBER, ts).build();
		final StringBuilder sb = new StringBuilder();
		footer.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		DomTester.assertEquals("<style:footer-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
				+ "</style:footer-style>", sb.toString());
	}

	@Test
	public final void testRegionToMasterStyle() throws IOException {
		final TextStyle ts = TextProperties.builder().fontWeightBold()
				.buildStyle("style");
		final FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.CENTER)
				.styledContent(Text.TEXT_PAGE_NUMBER, ts).build();
		final StringBuilder sb = new StringBuilder();
		footer.appendXMLToMasterStyle(this.util, sb);
		DomTester.assertEquals("<style:region-center>" + "<text:p>"
				+ "<text:span text:style-name=\"style\">"
				+ "<text:page-number>1</text:page-number>" + "</text:span>"
				+ "</text:p>" + "</style:region-center>", sb.toString());
	}

	@Test
	public final void testEmbedded() throws IOException {
		StylesContainer sc = PowerMock.createMock(StylesContainer.class);
		final TextStyle ts1 = TextProperties.builder().fontStyleItalic()
				.buildStyle("style1");
		final TextStyle ts2 = TextProperties.builder().fontStyleNormal()
				.fontWeightNormal().buildStyle("style2");
		final TextStyle ts3 = TextProperties.builder().fontWeightBold()
				.buildStyle("style3");
		final FooterHeader header = FooterHeader
				.regionBuilder(FooterHeader.Type.HEADER).region(Region.LEFT)
				.styledContent("left-text", ts1).region(Region.CENTER)
				.styledContent("center-text", ts2).region(Region.RIGHT)
				.styledContent("right-text", ts3).build();

		// play
		sc.addStyleToStylesAutomaticStyles(ts1);
		sc.addStyleToStylesAutomaticStyles(ts2);
		sc.addStyleToStylesAutomaticStyles(ts3);
		PowerMock.replayAll();
		header.addEmbeddedStylesToStylesElement(sc);
		PowerMock.verifyAll();
	}

	@Test
	public final void testEmbeddedNull() throws IOException {
		StylesContainer sc = PowerMock.createMock(StylesContainer.class);
		final FooterHeader header = FooterHeader
				.regionBuilder(FooterHeader.Type.HEADER).build();

		// play
		PowerMock.replayAll();
		header.addEmbeddedStylesToStylesElement(sc);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testEmbeddedMode() throws IOException {
		StylesContainer sc = PowerMock.createMock(StylesContainer.class);
		final TextStyle ts1 = TextProperties.builder().fontStyleItalic()
				.buildStyle("style1");
		final TextStyle ts2 = TextProperties.builder().fontStyleNormal()
				.fontWeightNormal().buildStyle("style2");
		final TextStyle ts3 = TextProperties.builder().fontWeightBold()
				.buildStyle("style3");
		final FooterHeader header = FooterHeader
				.regionBuilder(FooterHeader.Type.HEADER).region(Region.LEFT)
				.styledContent("left-text", ts1).region(Region.CENTER)
				.styledContent("center-text", ts2).region(Region.RIGHT)
				.styledContent("right-text", ts3).build();

		// play
		EasyMock.expect(sc.addStyleToStylesAutomaticStyles(ts1, Mode.CREATE_OR_UPDATE)).andReturn(true);
		EasyMock.expect(sc.addStyleToStylesAutomaticStyles(ts2, Mode.CREATE_OR_UPDATE)).andReturn(true);
		EasyMock.expect(sc.addStyleToStylesAutomaticStyles(ts3, Mode.CREATE_OR_UPDATE)).andReturn(true);
		PowerMock.replayAll();
		header.addEmbeddedStylesToStylesElement(sc, Mode.CREATE_OR_UPDATE);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testEmbeddedNullMode() throws IOException {
		StylesContainer sc = PowerMock.createMock(StylesContainer.class);
		final FooterHeader header = FooterHeader
				.regionBuilder(FooterHeader.Type.HEADER).build();

		// play
		PowerMock.replayAll();
		header.addEmbeddedStylesToStylesElement(sc, Mode.CREATE_OR_UPDATE);
		PowerMock.verifyAll();
	}
}
