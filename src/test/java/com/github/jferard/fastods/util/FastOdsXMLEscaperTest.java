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
	public final void testAttrAmp() {
		String s = this.escaper.escapeXMLAttribute("&0");
		Assert.assertEquals("&amp;0", s);
		s = this.escaper.escapeXMLAttribute("& 0");
		Assert.assertEquals("&amp; 0", s);
	}

	@Test
	public final void testAttrApos() {
		final String s = this.escaper.escapeXMLAttribute("' 0");
		Assert.assertEquals("&apos; 0", s);
	}

	@Test
	public final void testAttrBuffer() {
		final XMLEscaper escaper2 = new FastOdsXMLEscaper(124);
		final StringBuilder sb = new StringBuilder(8 * (2 >> 5));
		final StringBuilder sb2 = new StringBuilder(8 * (2 >> 5));
		sb.append("'ae< >");
		sb2.append("&apos;ae&lt; &gt;");
		for (int i = 0; i < 5; i++) {
			sb.append(sb.toString()).append(sb.toString());
			sb2.append(sb2.toString()).append(sb2.toString());
		}

		Assert.assertEquals(sb2.toString(),
				escaper2.escapeXMLAttribute(sb.toString()));
	}

	@Test
	public final void testAttrExpression() {
		final String s = this.escaper.escapeXMLAttribute("w<& ' d\"gfgh >");
		Assert.assertEquals("w&lt;&amp; &apos; d&quot;gfgh &gt;", s);
	}

	@Test
	public final void testAttrLt() {
		final String s = this.escaper.escapeXMLAttribute("< 0");
		Assert.assertEquals("&lt; 0", s);
	}

	@Test
	public final void testAttrNullString() {
		final String s = this.escaper.escapeXMLAttribute(null);
		Assert.assertEquals(null, s);
	}

	@Test
	public final void testAttrOther() {
		final String s = this.escaper.escapeXMLAttribute("\t\n\r\b");
		Assert.assertEquals("&#x9;&#xA;&#xD;\\uFFFD", s);
	}

	@Test
	public final void testBasicChars() {
		final String s = this.escaper.escapeXMLAttribute("abcde");
		Assert.assertEquals("abcde", s);
	}

	@Test
	public final void testContentAmp() {
		final String s = this.escaper.escapeXMLContent("& 0");
		Assert.assertEquals("&amp; 0", s);
	}

	@Test
	public final void testContentBuffer() {
		final XMLEscaper escaper2 = new FastOdsXMLEscaper(124);
		final StringBuilder sb = new StringBuilder(8 * (2 >> 5));
		final StringBuilder sb2 = new StringBuilder(8 * (2 >> 5));
		sb.append("'ae< >");
		sb2.append("'ae&lt; &gt;");
		for (int i = 0; i < 5; i++) {
			sb.append(sb.toString()).append(sb.toString());
			sb2.append(sb2.toString()).append(sb2.toString());
		}

		Assert.assertEquals(sb2.toString(),
				escaper2.escapeXMLContent(sb.toString()));
	}
	
	@Test
	public final void testContentBuffer2() {
		final XMLEscaper escaper2 = new FastOdsXMLEscaper(5);
		final String s = "<ae";
		final String s2 = "&lt;ae";
		Assert.assertEquals(s2,
				escaper2.escapeXMLContent(s));
	}
	
	@Test
	public final void testAttrBuffer2() {
		final XMLEscaper escaper2 = new FastOdsXMLEscaper(5);
		final String s = "<ae";
		final String s2 = "&lt;ae";
		Assert.assertEquals(s2,
				escaper2.escapeXMLAttribute(s));
	}

	@Test
	public final void testContentExpression() {
		final String s = this.escaper.escapeXMLContent("w<& ' d\"gfgh >");
		Assert.assertEquals("w&lt;&amp; ' d\"gfgh &gt;", s);
	}

	@Test
	public final void testContentLt() {
		final String s = this.escaper.escapeXMLContent("<");
		Assert.assertEquals("&lt;", s);
	}

	@Test
	public final void testContentNullString() {
		final String s = this.escaper.escapeXMLContent(null);
		Assert.assertEquals(null, s);
	}

	@Test
	public final void testContentOther() {
		final String s = this.escaper.escapeXMLContent("\t\n\r\b");
		Assert.assertEquals("\t\n\r\\uFFFD", s);
	}

	@Test
	public final void testDifferentCaches() {
		String s = this.escaper.escapeXMLAttribute("\t\n\r\b");
		Assert.assertEquals("&#x9;&#xA;&#xD;\\uFFFD", s);
		s = this.escaper.escapeXMLAttribute("\t\n\r\b");
		Assert.assertEquals("&#x9;&#xA;&#xD;\\uFFFD", s);
		s = this.escaper.escapeXMLContent("\t\n\r\b");
		Assert.assertEquals("\t\n\r\\uFFFD", s);
		s = this.escaper.escapeXMLContent("\t\n\r\b");
		Assert.assertEquals("\t\n\r\\uFFFD", s);
	}

	@Test
	public final void testEmptyString() {
		final String s = this.escaper.escapeXMLAttribute("");
		Assert.assertEquals("", s);
	}

	@Test
	public final void testFinalChars() {
		final String s = this.escaper.escapeXMLAttribute("'abcde");
		Assert.assertEquals("&apos;abcde", s);
	}
}
