package com.github.jferard.fastods;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class DateStyleTest {

	@Test(expected = IllegalArgumentException.class)
	public final void testWithNoName() {
		DateStyle ds = DateStyle.builder().build();
	}

	@Test
	public final void test() throws IOException {
		DateStyle ds = DateStyle.builder().name("test").build();
		StringBuilder sb = new StringBuilder();
		ds.appendXML(Util.getInstance(), sb);
		Assert.assertEquals(
				"<number:date-style style:name=\"test\" number:automatic-order=\"false\">"
						+ "<number:day number:style=\"long\"/>"
						+ "<number:text>.</number:text>"
						+ "<number:month number:style=\"long\"/>"
						+ "<number:text>.</number:text><number:year/>"
						+ "</number:date-style>",
				sb.toString());
	}
}
