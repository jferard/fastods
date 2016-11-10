package com.github.jferard.fastods.datastyle;

import java.io.IOException;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class TimeStyleTest {
	private XMLUtil util;
	private Locale locale;
	private DataStyleBuilderFactory factory;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
		this.locale = Locale.US;
		this.factory = new DataStyleBuilderFactory(this.util, this.locale);
	}

	@Test
	public final void testFormat() throws IOException {
		TimeStyle ps = this.factory.timeStyleBuilder("test")
				.timeFormat(TimeStyle.Format.HHMMSS).build();
		StringBuilder sb = new StringBuilder();
		ps.appendXMLToStylesEntry(this.util, sb);
		Assert.assertEquals(
				"<number:time-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:format-source=\"fixed\">"
						+ "<number:hours/>" + "<number:text>:</number:text>"
						+ "<number:minutes/>" + "<number:text>:</number:text>"
						+ "<number:seconds/>" + "</number:time-style>",
				sb.toString());
	}

	@Test
	public final void testNullFormat() throws IOException {
		TimeStyle ps = this.factory.timeStyleBuilder("test").timeFormat(null)
				.build();
		StringBuilder sb = new StringBuilder();
		ps.appendXMLToStylesEntry(this.util, sb);
		Assert.assertEquals(
				"<number:time-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:format-source=\"language\"/>",
				sb.toString());
	}
}
