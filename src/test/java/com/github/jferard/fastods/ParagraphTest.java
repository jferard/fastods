package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class ParagraphTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testNoSpan() throws IOException {
		final Paragraph par = Paragraph.builder().build();
		Assert.assertEquals(0, par.getSpans().size());
		final StringBuilder sb = new StringBuilder();
		par.appendXMLContent(this.util, sb);
		DomTester.assertEquals("<text:p/>", sb.toString());
	}

	@Test
	public final void testSpans() throws IOException {
		final ParagraphBuilder parBuilder = Paragraph.builder();
		parBuilder.span("content");
		parBuilder.span("text");
		final Paragraph par = parBuilder.build();
		Assert.assertEquals("content", par.getSpans().get(0).getText());
		Assert.assertNull(par.getSpans().get(0).getTextStyle());
		Assert.assertEquals("text", par.getSpans().get(1).getText());
		final StringBuilder sb = new StringBuilder();
		par.appendXMLContent(this.util, sb);
		Assert.assertEquals("<text:p>contenttext</text:p>", sb.toString());
	}

	@Test
	public final void testWithStyle() throws IOException {
		final TextStyle ts = TextProperties.builder().fontStyleNormal()
				.fontWeightNormal().buildStyle("style");

		final Paragraph par = Paragraph.builder().style(ts).span("text")
				.build();
		Assert.assertEquals(1, par.getSpans().size());
		final StringBuilder sb = new StringBuilder();
		par.appendXMLContent(this.util, sb);
		Assert.assertEquals("<text:p text:style-name=\"style\">text</text:p>",
				sb.toString());
	}

}
