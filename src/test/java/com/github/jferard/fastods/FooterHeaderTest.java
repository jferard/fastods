package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.FooterHeader.Region;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class FooterHeaderTest {
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
		final TextStyle ts = TextStyle.builder("style").fontStyleItalic()
				.build();
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
		final TextStyle ts = TextStyle.builder("style").fontStyleItalic()
				.build();
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
		final TextStyle ts1 = TextStyle.builder("test1").build();
		final TextStyle ts2 = TextStyle.builder("test2").build();
		final FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER)
				.text(Text.builder().par()
						.styledSpan(Text.TEXT_PAGE_NUMBER, ts1).par()
						.styledSpan(Text.TEXT_PAGE_COUNT, ts2).build())
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
		final TextStyle ts1 = TextStyle.builder("test1").build();
		final TextStyle ts2 = TextStyle.builder("test2").build();
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
	public final void testRegionsToMasterStyle() throws IOException {
		final TextStyle ts1 = TextStyle.builder("style1").fontStyleItalic()
				.build();
		final TextStyle ts2 = TextStyle.builder("style2").fontStyleNormal()
				.fontWeightNormal().build();
		final TextStyle ts3 = TextStyle.builder("style3").fontWeightBold()
				.build();
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
		final TextStyle ts = TextStyle.builder("style").fontWeightBold()
				.build();
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
		final TextStyle ts = TextStyle.builder("style").fontWeightBold()
				.build();
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
	public final void testSimpleFooterToAutomaticStyle() throws IOException {
		final TextStyle ts = TextStyle.builder("style").fontStyleItalic()
				.build();
		final FooterHeader header = FooterHeader.simpleHeader("text", ts);
		final StringBuilder sb = new StringBuilder();
		header.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		DomTester.assertEquals("<style:header-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
				+ "</style:header-style>", sb.toString());
	}

	@Test
	public final void testSimpleFooterToMasterStyle() throws IOException {
		final TextStyle ts = TextStyle.builder("style").fontStyleItalic()
				.build();
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
		final TextStyle ts = TextStyle.builder("test").build();
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
		final TextStyle ts = TextStyle.builder("test").build();
		final FooterHeader footer = FooterHeader
				.simpleBuilder(FooterHeader.Type.FOOTER)
				.styledContent("text", ts).build();
		final StringBuilder sb = new StringBuilder();
		footer.appendXMLToMasterStyle(this.util, sb);
		DomTester.assertEquals("<text:p>"
				+ "<text:span text:style-name=\"test\">text</text:span>"
				+ "</text:p>", sb.toString());
	}
}
