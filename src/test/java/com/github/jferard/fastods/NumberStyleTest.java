package com.github.jferard.fastods;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class NumberStyleTest {

	@Test
	public final void test() {
		NumberStyle ds = NumberStyle.builder().name("test").build();
		Assert.assertEquals(
				"<number:number-style style:name=\"test\" >"
						+ "<number:number number:decimal-places=\"2\" number:min-integer-digits=\"1\" />"
						+ "</number:number-style>",
				ds.toXML(Util.getInstance()));
	}

}
