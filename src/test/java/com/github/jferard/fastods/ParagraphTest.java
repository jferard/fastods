/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * ****************************************************************************/
package com.github.jferard.fastods;

import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.testutil.DomTester;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ParagraphTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testNoSpan() throws IOException {
		final Paragraph par = Paragraph.builder().build();
		Assert.assertEquals(0, par.getSpans().size());
		final StringBuilder sb = new StringBuilder();
		par.appendXMLContent(this.util, sb);
		DomTester.assertEquals("<text:p/>", sb.toString());
	}

	@Test
	public final void testSpans() throws IOException {
		final ParagraphBuilder parBuilder = Paragraph.builder();
		parBuilder.span("content");
		parBuilder.span("text");
		final Paragraph par = parBuilder.build();
		Assert.assertEquals("content", par.getSpans().get(0).getText());
		Assert.assertNull(par.getSpans().get(0).getTextStyle());
		Assert.assertEquals("text", par.getSpans().get(1).getText());
		final StringBuilder sb = new StringBuilder();
		par.appendXMLContent(this.util, sb);
		DomTester.assertEquals("<text:p>contenttext</text:p>", sb.toString());
	}

	@Test
	public final void testWithStyle() throws IOException {
		final TextStyle ts = TextProperties.builder().fontStyleNormal()
				.fontWeightNormal().buildStyle("style");

		final Paragraph par = Paragraph.builder().style(ts).span("text")
				.build();
		Assert.assertEquals(1, par.getSpans().size());
		final StringBuilder sb = new StringBuilder();
		par.appendXMLContent(this.util, sb);
		DomTester.assertEquals("<text:p text:style-name=\"style\">text</text:p>",
				sb.toString());
	}

}
