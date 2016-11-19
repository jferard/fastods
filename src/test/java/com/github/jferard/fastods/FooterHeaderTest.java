package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.github.jferard.fastods.FooterHeader.Region;
import com.github.jferard.fastods.style.FHTextStyle;
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
			throws IOException, SAXException {
		final FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER).marginTop("10pt")
				.marginRight("11pt").marginBottom("12pt").marginLeft("13pt")
				.minHeight("120pt")
				.styledText(
						FHTextStyle.builder("style").fontStyleItalic().build(),
						"text")
				.build();
		final StringBuilder sb = new StringBuilder();
		header.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals("<style:header-style>"
				+ "<style:header-footer-properties fo:min-height=\"120pt\" fo:margin=\"0cm\" fo:margin-top=\"10pt\" fo:margin-right=\"11pt\" fo:margin-bottom=\"12pt\" fo:margin-left=\"13pt\"/>"
				+ "</style:header-style>", sb.toString()));
	}

	@Test
	public final void testMarginsAndMinHeightToAutomaticStyle()
			throws IOException, SAXException {
		final FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER).allMargins("10pt")
				.minHeight("120pt")
				.styledText(
						FHTextStyle.builder("style").fontStyleItalic().build(),
						"text")
				.build();
		final StringBuilder sb = new StringBuilder();
		header.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals("<style:header-style>"
				+ "<style:header-footer-properties fo:min-height=\"120pt\" fo:margin=\"10pt\"/>"
				+ "</style:header-style>", sb.toString()));
	}

	@Test
	public final void testPageToMasterStyle() throws IOException, SAXException {
		final FHTextStyle ts1 = FHTextStyle.builder("test1").build();
		final FHTextStyle ts2 = FHTextStyle.builder("test2").build();
		final FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER).pageNumber(ts1)
				.pageCount(ts2).build();
		final StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		Assert.assertTrue(DomTester
				.equals("<text:p>" + "<text:span text:style-name=\"test1\">"
						+ "<text:page-number>1</text:page-number>"
						+ "</text:span>" + "</text:p>" + "<text:p>"
						+ "<text:span text:style-name=\"test2\">"
						+ "<text:page-count>99</text:page-count>"
						+ "</text:span>" + "</text:p>", sb.toString()));
	}

	@Test
	public final void testPageToMasterStyle2() throws IOException {
		final FHTextStyle ts1 = FHTextStyle.builder("test1").build();
		final FHTextStyle ts2 = FHTextStyle.builder("test2").build();
		final FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER).pageNumber(ts1, 1)
				.pageCount(ts2, 3).build();
		final StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		Assert.assertEquals(// True(DomTester.equals(
				"<text:p>" + "<text:span text:style-name=\"test1\">"
						+ "<text:page-number>1</text:page-number>"
						+ "</text:span>" + "</text:p>" + "<text:p>"
						+ "<text:span text:style-name=\"test2\">"
						+ "<text:page-count>99</text:page-count>"
						+ "</text:span>" + "</text:p>",
				sb.toString()); // );
	}

	@Test
	public final void testRegionsToMasterStyle() throws IOException {
		final FooterHeader header = FooterHeader
				.regionBuilder(FooterHeader.Type.HEADER).region(Region.LEFT)
				.styledText(
						FHTextStyle.builder("style1").fontStyleItalic().build(),
						"left-text")
				.region(Region.CENTER)
				.styledText(FHTextStyle.builder("style2").fontStyleNormal()
						.fontWeightNormal().build(), "center-text")
				.region(Region.RIGHT)
				.styledText(
						FHTextStyle.builder("style3").fontWeightBold().build(),
						"right-text")
				.build();
		final StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<style:region-left>" + "<text:p>"
						+ "<text:span text:style-name=\"style1\">left-text</text:span>"
						+ "</text:p>" + "</style:region-left>"
						+ "<style:region-center>" + "<text:p>"
						+ "<text:span text:style-name=\"style2\">center-text</text:span>"
						+ "</text:p>" + "</style:region-center>"
						+ "<style:region-right>" + "<text:p>"
						+ "<text:span text:style-name=\"style3\">right-text</text:span>"
						+ "</text:p>" + "</style:region-right>",
				sb.toString()));
	}

	@Test
	public final void testRegionToAutomaticStyle()
			throws IOException, SAXException {
		final FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.CENTER)
				.pageNumber(
						FHTextStyle.builder("style").fontWeightBold().build())
				.build();
		final StringBuilder sb = new StringBuilder();
		footer.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals("<style:footer-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
				+ "</style:footer-style>", sb.toString()));
	}

	@Test
	public final void testRegionToMasterStyle()
			throws IOException, SAXException {
		final FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.CENTER)
				.pageNumber(
						FHTextStyle.builder("style").fontWeightBold().build())
				.build();
		final StringBuilder sb = new StringBuilder();
		footer.appendXMLToMasterStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals("<style:region-center>" + "<text:p>"
				+ "<text:span text:style-name=\"style\">"
				+ "<text:page-number>1</text:page-number>" + "</text:span>"
				+ "</text:p>" + "</style:region-center>", sb.toString()));
	}

	@Test
	public final void testSimpleFooterToAutomaticStyle()
			throws IOException, SAXException {
		final FooterHeader header = FooterHeader.simpleHeader(
				FHTextStyle.builder("style").fontStyleItalic().build(), "text");
		final StringBuilder sb = new StringBuilder();
		header.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals("<style:header-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
				+ "</style:header-style>", sb.toString()));
	}

	@Test
	public final void testSimpleFooterToMasterStyle()
			throws IOException, SAXException {
		final FooterHeader header = FooterHeader.simpleHeader(
				FHTextStyle.builder("style").fontStyleItalic().build(), "text");
		final StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		Assert.assertTrue(
				DomTester.equals(
						"<text:p>" + "<text:span text:style-name=\"style\">"
								+ "text" + "</text:span>" + "</text:p>",
						sb.toString()));
	}

	@Test
	public final void testSimpleStyledTextToAutomaticStyle()
			throws IOException, SAXException {
		final FHTextStyle ts = FHTextStyle.builder("test").build();
		final String text = "text";
		final int paragraphIndex = 2;
		final FooterHeader footer = FooterHeader
				.simpleBuilder(FooterHeader.Type.FOOTER)
				.styledText(ts, text, paragraphIndex).build();
		final StringBuilder sb = new StringBuilder();
		footer.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals("<style:footer-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
				+ "</style:footer-style>", sb.toString()));
	}

	@Test
	public final void testSimpleStyledTextToMasterStyle()
			throws IOException, SAXException {
		final FHTextStyle ts = FHTextStyle.builder("test").build();
		final String text = "text";
		final int paragraphIndex = 2;
		final FooterHeader footer = FooterHeader
				.simpleBuilder(FooterHeader.Type.FOOTER)
				.styledText(ts, text, paragraphIndex).build();
		final StringBuilder sb = new StringBuilder();
		footer.appendXMLToMasterStyle(this.util, sb);
		Assert.assertEquals(// True(DomTester.equals(
				"<text:p>"
						+ "<text:span text:style-name=\"test\">text</text:span>"
						+ "</text:p>",
				sb.toString()); // );
	}
}
