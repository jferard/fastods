package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class SpanTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testFHTextWithStyle() throws IOException {
		final TextStyle ts = TextStyle.builder("test").build();
		final Span fhtext = new Span("text", ts);
		Assert.assertEquals("text", fhtext.getText());
		Assert.assertEquals(ts, fhtext.getTextStyle());
		final StringBuilder sbo = new StringBuilder();
		fhtext.appendXMLOptionalSpanToParagraph(this.util, sbo);
		Assert.assertEquals(
				"<text:span text:style-name=\"test\">text</text:span>",
				sbo.toString());
		final StringBuilder sbt = new StringBuilder();
		fhtext.appendXMLTextPToParagraph(this.util, sbt);
		Assert.assertEquals("<text:p text:style-name=\"test\">text</text:p>",
				sbt.toString());
	}

	@Test
	public final void testSimpleFHText() throws IOException {
		final Span fhtext = new Span("text");
		Assert.assertEquals("text", fhtext.getText());
		Assert.assertNull(fhtext.getTextStyle());
		final StringBuilder sbo = new StringBuilder();
		fhtext.appendXMLOptionalSpanToParagraph(this.util, sbo);
		Assert.assertEquals("text", sbo.toString());
		final StringBuilder sbt = new StringBuilder();
		fhtext.appendXMLTextPToParagraph(this.util, sbt);
		Assert.assertEquals("<text:p>text</text:p>", sbt.toString());
	}
}
