package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.FooterHeader.Region;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class FooterHeaderTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testToAutomaticStyle() throws IOException {
		FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.CENTER)
				.pageNumber(
						FHTextStyle.builder("style").fontWeightBold().build())
				.build();
		StringBuilder sb = new StringBuilder();
		footer.appendXMLToAutomaticStyle(this.util, sb);
		Assert.assertEquals("<style:footer-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin-left=\"0cm\" fo:margin-right=\"0cm\" fo:margin-top=\"0cm\"/>"
				+ "</style:footer-style>", sb.toString());
	}

	@Test
	public final void testToMasterStyle() throws IOException {
		FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.CENTER)
				.pageNumber(
						FHTextStyle.builder("style").fontWeightBold().build())
				.build();
		StringBuilder sb = new StringBuilder();
		footer.appendXMLToMasterStyle(this.util, sb);
		Assert.assertEquals(
				"<style:region-center>" + "<text:p text:style-name=\"style\">"
						+ "<text:page-number>1</text:page-number>" + "</text:p>"
						+ "</style:region-center>",
				sb.toString());
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
	public final void testSimpleFooterToAutomaticStyle() throws IOException {
		FooterHeader header = FooterHeader.simpleHeader(
				FHTextStyle.builder("style").fontStyleItalic().build(), "text");
		StringBuilder sb = new StringBuilder();
		header.appendXMLToAutomaticStyle(this.util, sb);
		Assert.assertEquals("<style:header-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin-left=\"0cm\" fo:margin-right=\"0cm\" fo:margin-top=\"0cm\"/>"
				+ "</style:header-style>", sb.toString());
	}

	@Test
	public final void testSimpleFooterToMasterStyle() throws IOException {
		FooterHeader header = FooterHeader.simpleHeader(
				FHTextStyle.builder("style").fontStyleItalic().build(), "text");
		StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(this.util, sb);
		Assert.assertEquals(
				"<text:p text:style-name=\"style\">" + "text" + "</text:p>",
				sb.toString());
	}

}
