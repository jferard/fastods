package com.github.jferard.fastods;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class NumberStyleTest {

	@Test
	public final void test() throws IOException {
		NumberStyle ns = NumberStyle.builder().name("test").build();
		StringBuilder sb = new StringBuilder();
		ns.appendXML(Util.getInstance(), sb);
		Assert.assertEquals(
				"<number:number-style style:name=\"test\">"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\"/>"
						+ "</number:number-style>",
				sb.toString());
	}

}
