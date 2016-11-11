package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class FHTextTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}
	
	@Test
	public final void testSimpleFHText() throws IOException {
		FHText fhtext = new FHText("text");
		Assert.assertEquals("text",  fhtext.getText());
		Assert.assertNull(fhtext.getTextStyle());
		StringBuilder sbo = new StringBuilder();
		fhtext.appendXMLOptionalSpanToParagraph(this.util, sbo);
		Assert.assertEquals("text", sbo.toString());
		StringBuilder sbt = new StringBuilder();
		fhtext.appendXMLTextPToParagraph(this.util, sbt);
		Assert.assertEquals("<text:p>text</text:p>", sbt.toString());
	}

	@Test
	public final void testFHTextWithStyle() throws IOException {
		FHTextStyle ts = FHTextStyle.builder("test").build();
		FHText fhtext = new FHText("text", ts);
		Assert.assertEquals("text",  fhtext.getText());
		Assert.assertEquals(ts, fhtext.getTextStyle());
		StringBuilder sbo = new StringBuilder();
		fhtext.appendXMLOptionalSpanToParagraph(this.util, sbo);
		Assert.assertEquals("<text:span text:style-name=\"test\">text</text:span>", sbo.toString());
		StringBuilder sbt = new StringBuilder();
		fhtext.appendXMLTextPToParagraph(this.util, sbt);
		Assert.assertEquals("<text:p text:style-name=\"test\">text</text:p>", sbt.toString());
	}
}
