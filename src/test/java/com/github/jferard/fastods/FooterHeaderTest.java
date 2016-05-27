package com.github.jferard.fastods;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.github.jferard.fastods.FooterHeader.Region;

public class FooterHeaderTest {
	@Test
	public final void testToAutomaticStyle() throws IOException {
		FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.CENTER)
				.pageNumber(TextStyle.builder().fontWeightBold().build())
				.build();
		StringBuilder sb = new StringBuilder();
		footer.appendXMLToAutomaticStyle(Util.getInstance(), sb);
		Assert.assertEquals("<style:footer-style>"
				+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin-left=\"0cm\" fo:margin-right=\"0cm\" fo:margin-top=\"0cm\"/>"
				+ "</style:footer-style>", sb.toString());
	}

	@Test
	public final void testToMasterStyle() throws IOException {
		FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.CENTER)
				.pageNumber(TextStyle.builder().fontWeightBold().build())
				.build();
		StringBuilder sb = new StringBuilder();
		footer.appendXMLToMasterStyle(Util.getInstance(), sb);
		Assert.assertEquals("<style:region-center>" + "<text:p>"
				+ "<text:span text:style-name=\"\">"
				+ "<text:page-number>1</text:page-number>" + "</text:span>"
				+ "</text:p>" + "</style:region-center>", sb.toString());
	}

	@Test
	public final void testAlmostEmptyToMasterStyle() throws IOException {
		FooterHeader header = FooterHeader
				.simpleBuilder(FooterHeader.Type.HEADER).build();
		StringBuilder sb = new StringBuilder();
		header.appendXMLToMasterStyle(Util.getInstance(), sb);
		Assert.assertEquals("", sb.toString());
	}

}
