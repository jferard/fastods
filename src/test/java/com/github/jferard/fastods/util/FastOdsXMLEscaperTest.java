package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class FastOdsXMLEscaperTest {
	XMLEscaper escaper;
	
	@Before
	public void setUp() {
		this.escaper = new FastOdsXMLEscaper(); 
	}
	
	@Test
	public final void testNullString() {
		String s = this.escaper.escapeXMLAttribute(null);
		Assert.assertEquals(null, s);
	}
	
	
	@Test
	public final void testEmptyString() {
		String s = this.escaper.escapeXMLAttribute("");
		Assert.assertEquals("", s);
	}

	@Test
	public final void testBasicChars() {
		String s = this.escaper.escapeXMLAttribute("abcde");
		Assert.assertEquals("abcde", s);
	}
	
	@Test
	public final void testAmp() {
		String s = this.escaper.escapeXMLAttribute("&");
		Assert.assertEquals("&amp;", s);
	}
	
	@Test
	public final void testLt() {
		String s = this.escaper.escapeXMLAttribute("<");
		Assert.assertEquals("&lt;", s);
	}
	
	@Test
	public final void testExpression() {
		String s = this.escaper.escapeXMLAttribute("w<& ' d\"gfgh >");
		Assert.assertEquals("w&lt;&amp;&apos; d&quot;gfgh &gt;", s);
	}
	
	@Test
	public final void testFinalChars() {
		String s = this.escaper.escapeXMLAttribute("'abcde");
		Assert.assertEquals("&apos;abcde", s);
	}
	

}
