package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.FooterHeader.Region;
import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class RegionFooterHeaderTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
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
}
