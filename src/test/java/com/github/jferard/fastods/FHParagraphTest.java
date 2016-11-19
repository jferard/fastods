package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class FHParagraphTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void test() throws IOException {
		final FHParagraph par = new FHParagraph();
		par.add("content");
		final FHText fHText = new FHText("text");
		par.add(fHText);
		Assert.assertEquals("content", par.getTexts().get(0).getText());
		Assert.assertNull(par.getTexts().get(0).getTextStyle());
		Assert.assertEquals(fHText, par.getTexts().get(1));
		final StringBuilder sb = new StringBuilder();
		par.appendXMLToRegionBody(this.util, sb);
		Assert.assertEquals("<text:p>contenttext</text:p>", sb.toString());
	}

}
