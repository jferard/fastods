package com.github.jferard.fastods.datastyle;

import java.io.IOException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.DomTester;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class FractionStyleTest {
	private DataStyleBuilderFactory factory;
	private Locale locale;
	private XMLUtil util;

	@Before
	public final void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
		this.locale = Locale.US;
		this.factory = new DataStyleBuilderFactory(this.util, this.locale);
	}

	@Test
	public final void test1()
			throws IOException {
		final FractionStyle s = this.factory.fractionStyleBuilder("name")
				.country("FR").language("en").volatileStyle(true)
				.fractionValues(1, 3).groupThousands(true).minIntegerDigits(8)
				.negativeValueRed().build();
		final StringBuilder sb = new StringBuilder();
		s.appendXML(this.util, sb);
		final String str = "<number:number-style style:name=\"name\" number:language=\"en\" number:country=\"FR\" style:volatile=\"true\">"
				+ "<number:fraction number:min-numerator-digits=\"1\" number:min-denominator-digits=\"3\" number:min-integer-digits=\"8\" number:grouping=\"true\"/>"
				+ "</number:number-style>"
				+ "<number:number-style style:name=\"name-neg\" number:language=\"en\" number:country=\"FR\" style:volatile=\"true\">"
				+ "<style:text-properties fo:color=\"#FF0000\"/>"
				+ "<number:text>-</number:text>"
				+ "<number:fraction number:min-numerator-digits=\"1\" number:min-denominator-digits=\"3\" number:min-integer-digits=\"8\" number:grouping=\"true\"/>"
				+ "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"name\"/>"
				+ "</number:number-style>";
		DomTester.assertEquals(str, sb.toString());
	}

	@Test
	public final void test2()
			throws IOException {
		final FractionStyle s = this.factory.fractionStyleBuilder("name")
				.country("FR").language("en").locale(Locale.GERMANY)
				.volatileStyle(true).fractionValues(1, 3).groupThousands(true)
				.minIntegerDigits(8).negativeValueRed().build();
		final StringBuilder sb = new StringBuilder();
		s.appendXML(this.util, sb);
		final String str = "<number:number-style style:name=\"name\" number:language=\"de\" number:country=\"DE\" style:volatile=\"true\">"
				+ "<number:fraction number:min-numerator-digits=\"1\" number:min-denominator-digits=\"3\" number:min-integer-digits=\"8\" number:grouping=\"true\"/>"
				+ "</number:number-style>"
				+ "<number:number-style style:name=\"name-neg\" number:language=\"de\" number:country=\"DE\" style:volatile=\"true\">"
				+ "<style:text-properties fo:color=\"#FF0000\"/>"
				+ "<number:text>-</number:text>"
				+ "<number:fraction number:min-numerator-digits=\"1\" number:min-denominator-digits=\"3\" number:min-integer-digits=\"8\" number:grouping=\"true\"/>"
				+ "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"name\"/>"
				+ "</number:number-style>";
		DomTester.assertEquals(str, sb.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testNull() {
		this.factory.fractionStyleBuilder(null);
	}

}
