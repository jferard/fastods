package com.github.jferard.fastods.datastyle;

import java.io.IOException;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class PercentageStyleTest {
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
	public final void testDecimalPlaces() throws IOException {
		PercentageStyle ps = this.factory.percentageStyleBuilder("test")
				.decimalPlaces(5).build();
		StringBuilder sb = new StringBuilder();
		ps.appendXMLToStylesEntry(this.util, sb);
		Assert.assertEquals(
				"<number:percentage-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"5\" number:min-integer-digits=\"1\"/>"
						+ "<number:text>%</number:text>"
						+ "</number:percentage-style>",
				sb.toString());
	}

	@Test
	public final void testGroupThousands() throws IOException {
		PercentageStyle ps = this.factory.percentageStyleBuilder("test")
				.groupThousands(true).build();
		StringBuilder sb = new StringBuilder();
		ps.appendXMLToStylesEntry(this.util, sb);
		Assert.assertEquals(
				"<number:percentage-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\" number:grouping=\"true\"/>"
						+ "<number:text>%</number:text>"
						+ "</number:percentage-style>",
				sb.toString());
	}

	@Test
	public final void testMinIntegeDigits() throws IOException {
		PercentageStyle ps = this.factory.percentageStyleBuilder("test")
				.minIntegerDigits(8).build();
		StringBuilder sb = new StringBuilder();
		ps.appendXMLToStylesEntry(this.util, sb);
		Assert.assertEquals(
				"<number:percentage-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"8\"/>"
						+ "<number:text>%</number:text>"
						+ "</number:percentage-style>",
				sb.toString());
	}

	@Test
	public final void testNegativeValueColor() throws IOException {
		PercentageStyle ps = this.factory.percentageStyleBuilder("test")
				.negativeValueColor(Color.GREEN).build();
		StringBuilder sb = new StringBuilder();
		ps.appendXMLToStylesEntry(this.util, sb);
		Assert.assertEquals(
				"<number:percentage-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
						+ "<number:text>%</number:text>"
						+ "</number:percentage-style>"
						+ "<number:percentage-style style:name=\"test-neg\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<style:text-properties fo:color=\"#008000\"/>"
						+ "<number:text>-</number:text>"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
						+ "<number:text>%</number:text>"
						+ "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"test\"/>"
						+ "</number:percentage-style>",
				sb.toString());
	}

	@Test
	public final void testNegativeValueRed() throws IOException {
		PercentageStyle ps = this.factory.percentageStyleBuilder("test")
				.negativeValueRed().build();
		StringBuilder sb = new StringBuilder();
		ps.appendXMLToStylesEntry(this.util, sb);
		Assert.assertEquals(
				"<number:percentage-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
						+ "<number:text>%</number:text>"
						+ "</number:percentage-style>"
						+ "<number:percentage-style style:name=\"test-neg\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\">"
						+ "<style:text-properties fo:color=\"#FF0000\"/>"
						+ "<number:text>-</number:text>"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
						+ "<number:text>%</number:text>"
						+ "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"test\"/>"
						+ "</number:percentage-style>",
				sb.toString());
	}
}
