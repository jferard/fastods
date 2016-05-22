package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class CurrencyStyleTest {

	@Test(expected = IllegalArgumentException.class)
	public final void testWithNoName() {
		CurrencyStyle cs = CurrencyStyle.builder().build();
	}

	@Test
	public final void test() throws IOException {
		CurrencyStyle cs = CurrencyStyle.builder().name("test").build();
		StringBuilder sb = new StringBuilder();
		cs.appendXML(Util.getInstance(), sb);
		Assert.assertEquals(
				"<number:currency-style style:name=\"testnn\" style:volatile=\"true\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\" />"
						+ "<number:text> "
						+ "</number:text><number:currency-symbol>\"€\""
						+ "</number:currency-symbol>"
						+ "</number:currency-style>"
						+ "<number:currency-style style:name=\"test\">"
						+ "<style:text-properties fo:color=\"#FF0000\"/>"
						+ "<number:text>-" + "</number:text>"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\" />"
						+ "<number:text> "
						+ "</number:text><number:currency-symbol>\"€\""
						+ "</number:currency-symbol>"
						+ "<style:map style:condition=\"value()&gt;=0\" style:apply-style-name=\"testnn\"/>"
						+ "</number:currency-style>",
				sb.toString());
	}
}
