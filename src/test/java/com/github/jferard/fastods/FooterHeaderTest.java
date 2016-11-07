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
	public final void testToAutomaticStyle() throws IOException, SAXException {
		FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.CENTER)
				.pageNumber(
						FHTextStyle.builder("style").fontWeightBold().build())
				.build();
		StringBuilder sb = new StringBuilder();
		footer.appendXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals("<style:footer-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
				+ "</style:footer-style>", sb.toString()));
	}

	@Test
	public final void testToMasterStyle() throws IOException, SAXException {
		FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.CENTER)
				.pageNumber(
						FHTextStyle.builder("style").fontWeightBold().build())
				.build();
		StringBuilder sb = new StringBuilder();
		footer.appendXMLToMasterStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<style:region-center>" + "<text:p text:style-name=\"style\">"
						+ "<text:page-number>1</text:page-number>" + "</text:p>"
						+ "</style:region-center>",
				sb.toString()));
	}

	@Test
	public final void testAlmostEmptyToMasterStyle() throws IOException {
		FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER).build();
		StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		Assert.assertEquals("", sb.toString());
	}

	@Test
	public final void testSimpleFooterToAutomaticStyle()
			throws IOException, SAXException {
		FooterHeader header = FooterHeader.simpleHeader(
				FHTextStyle.builder("style").fontStyleItalic().build(), "text");
		StringBuilder sb = new StringBuilder();
		header.appendXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals("<style:header-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
				+ "</style:header-style>", sb.toString()));
	}

	@Test
	public final void testSimpleFooterToMasterStyle()
			throws IOException, SAXException {
		FooterHeader header = FooterHeader.simpleHeader(
				FHTextStyle.builder("style").fontStyleItalic().build(), "text");
		StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<text:p text:style-name=\"style\">" + "text" + "</text:p>",
				sb.toString()));
	}

	@Test
	public final void testMarginsAndMinHeight()
			throws IOException, SAXException {
		FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER).allMargins("10pt")
				.minHeight("120pt")
				.styledText(
						FHTextStyle.builder("style").fontStyleItalic().build(),
						"text")
				.build();
		StringBuilder sb = new StringBuilder();
		header.appendXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals("<style:header-style>"
				+ "<style:header-footer-properties fo:min-height=\"120pt\" fo:margin=\"10pt\"/>"
				+ "</style:header-style>", sb.toString()));
	}

	@Test
	public final void testRegions() throws IOException, SAXException {
		FooterHeader header = FooterHeader
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
		StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<style:region-left>"
						+ "<text:p text:style-name=\"style1\">left-text</text:p>"
						+ "</style:region-left>" + "<style:region-center>"
						+ "<text:p text:style-name=\"style2\">center-text</text:p>"
						+ "</style:region-center>" + "<style:region-right>"
						+ "<text:p text:style-name=\"style3\">right-text</text:p>"
						+ "</style:region-right>",
				sb.toString()));
	}
}
