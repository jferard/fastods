/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods.style;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.DomTester;
import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class TextStyleTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		final XMLEscaper escaper = new FastOdsXMLEscaper();
		this.util = new XMLUtil(escaper);
	}

	@Test
	public final void testAddToFile() {
		final TextStyle style = TextStyle.builder("style").build();

		final OdsFile f = PowerMock.createMock(OdsFile.class);
		f.addTextStyle(style);
		PowerMock.replayAll();
		style.addToFile(f);
		PowerMock.verifyAll();
	}

	@Test
	public final void testBadSize() throws IOException {
		final Appendable sb = new StringBuilder();
		final TextStyle style = TextStyle.builder("style").fontSize("@")
				.build();
		style.appendXMLToContentEntry(this.util, sb);
		DomTester.assertEquals(
				"<style:text-properties fo:font-size=\"@\" style:font-size-asian=\"@\" style:font-size-complex=\"@\"/>",
				sb.toString());
	}

	@Test
	public final void testColorNameSize() throws IOException {
		final Appendable sb = new StringBuilder();
		final TextStyle style = TextStyle.builder("style")
				.fontColor(Color.ALICEBLUE).fontName("Verdana").fontSize(10)
				.build();
		style.appendXMLToContentEntry(this.util, sb);
		DomTester.assertEquals(
				"<style:text-properties fo:color=\"#F0F8FF\" style:font-name=\"Verdana\" fo:font-size=\"10pt\" style:font-size-asian=\"10pt\" style:font-size-complex=\"10pt\"/>",
				sb.toString());
	}

	@Test
	public final void testDefault() {
		final TextStyle style = TextStyle.builder("style").build();
		Assert.assertEquals(null, style.getFontColor());
		Assert.assertEquals(null, style.getFontSize());
		Assert.assertEquals(null, style.getFontUnderlineColor());
		Assert.assertEquals(null, style.getFontUnderlineStyle());
		Assert.assertEquals("style", style.getName());
		Assert.assertFalse(style.isNotEmpty());
	}

	@Test
	public final void testItalicBold() throws IOException {
		final Appendable sb = new StringBuilder();
		final TextStyle style = TextStyle.builder("style").fontStyleItalic()
				.fontWeightBold().build();
		style.appendXMLToContentEntry(this.util, sb);
		DomTester.assertEquals(
				"<style:text-properties fo:font-weight=\"bold\" style:font-weight-asian=\"bold\" style:font-weight-complex=\"bold\" fo:font-style=\"italic\" style:font-style-asian=\"italic\" style:font-style-complex=\"italic\"/>",
				sb.toString());
	}

	@Test
	public final void testUnderline() throws IOException {
		final Appendable sb = new StringBuilder();
		final TextStyle style = TextStyle.builder("style")
				.fontUnderlineStyle(TextStyle.Underline.DASH)
				.fontUnderlineColor(Color.RED).build();
		style.appendXMLToContentEntry(this.util, sb);
		DomTester.assertEquals(
				"<style:text-properties style:text-underline-style=\"dash\" style:text-underline-width=\"auto\" style:text-underline-color=\"#FF0000\"/>",
				sb.toString());
	}
}
