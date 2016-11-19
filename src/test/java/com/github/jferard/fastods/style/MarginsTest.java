package com.github.jferard.fastods.style;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class MarginsTest {
	private MarginsBuilder builder;
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
		this.builder = new MarginsBuilder();
	}

	@Test
	public final void testAll() throws IOException {
		final Margins margins = this.builder.all("10pt").build();
		final StringBuilder sb = new StringBuilder();
		margins.appendXMLToTableCellStyle(this.util, sb);
		Assert.assertEquals(" fo:margin=\"10pt\"", sb.toString());
	}

	@Test
	public final void testTRBL() throws IOException {
		final Margins margins = this.builder.all("10pt").top("10pt")
				.right("11pt").bottom("12pt").left("13pt").build();
		final StringBuilder sb = new StringBuilder();
		margins.appendXMLToTableCellStyle(this.util, sb);
		Assert.assertEquals(
				" fo:margin=\"10pt\" fo:margin-right=\"11pt\" fo:margin-bottom=\"12pt\" fo:margin-left=\"13pt\"",
				sb.toString());
	}

	@Test
	public final void testTRBL2() throws IOException {
		final Margins margins = this.builder.top("10pt").right("11pt")
				.bottom("12pt").left("13pt").build();
		final StringBuilder sb = new StringBuilder();
		margins.appendXMLToTableCellStyle(this.util, sb);
		Assert.assertEquals(
				" fo:margin-top=\"10pt\" fo:margin-right=\"11pt\" fo:margin-bottom=\"12pt\" fo:margin-left=\"13pt\"",
				sb.toString());
	}
}
