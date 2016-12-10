
package com.github.jferard.fastods.style;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.DomTester;
import com.github.jferard.fastods.style.BorderAttribute.Style;
import com.github.jferard.fastods.util.XMLUtil;

public class BordersTest {

	private XMLUtil util;
	private BorderAttribute a1;
	private BorderAttribute a2;

	@Before
	public void setUp() throws Exception {
		this.util = XMLUtil.create();
		this.a1 = new BorderAttribute("10pt", Color.BLACK, Style.DOUBLE);
		this.a2 = new BorderAttribute("11pt", Color.WHITE, Style.SOLID);
	}

	@Test
	public final void testEqual() throws IOException {
		Borders b = new BordersBuilder().all(this.a1).top(this.a1)
				.right(this.a1).bottom(this.a1).left(this.a1).build();

		StringBuilder sb = new StringBuilder();
		b.appendXMLToTableCellStyle(this.util, sb);
		Assert.assertEquals(" fo:border=\"10pt double #000000\"",
				sb.toString());
	}

	@Test
	public final void testDifferent() throws IOException {
		Borders b = new BordersBuilder().all(this.a1).top(this.a2)
				.right(this.a2).bottom(this.a2).left(this.a2).build();

		StringBuilder sb = new StringBuilder();
		b.appendXMLToTableCellStyle(this.util, sb);
		Assert.assertEquals(
				" fo:border=\"10pt double #000000\" fo:border-top=\"11pt solid #FFFFFF\" fo:border-right=\"11pt solid #FFFFFF\" fo:border-bottom=\"11pt solid #FFFFFF\" fo:border-left=\"11pt solid #FFFFFF\"",
				sb.toString());
	}

	@Test
	public final void testGet() {
		Borders b = new BordersBuilder().all(this.a1).top(this.a2)
				.right(this.a2).bottom(this.a2).left(this.a2).build();

		Assert.assertEquals(b.getAll(), this.a1);
		Assert.assertEquals(b.getTop(), this.a2);
		Assert.assertEquals(b.getRight(), this.a2);
		Assert.assertEquals(b.getBottom(), this.a2);
		Assert.assertEquals(b.getLeft(), this.a2);
	}

	@Test
	public final void testEquals() {
		Borders b1 = new BordersBuilder().all(this.a1).build();
		Borders b2 = new BordersBuilder().all(this.a2).build();
		Borders b3 = new BordersBuilder().all(this.a1).build();

		Assert.assertFalse(b1.equals(null));
		Assert.assertFalse(b1.equals("a"));
		Assert.assertFalse(b1.equals(b2));
		Assert.assertTrue(b1.equals(b1));
		Assert.assertTrue(b1.equals(b3));
	}

	@Test
	public final void testHashCode() {
		Borders b1 = new BordersBuilder().all(this.a1).build();
		Borders b2 = new BordersBuilder().all(this.a1).build();

		Assert.assertEquals(b1.hashCode(), b2.hashCode());
	}
}
