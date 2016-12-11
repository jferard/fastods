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
package com.github.jferard.fastods.style;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.DomTester;
import com.github.jferard.fastods.FooterHeader;
import com.github.jferard.fastods.style.MasterPageStyle.PaperFormat;
import com.github.jferard.fastods.style.MasterPageStyle.WritingMode;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class PageStyleTest {
	private static final String MASTER = "<style:master-page style:name=\"test\" style:page-layout-name=\"test\">"
			+ "<style:header>" + "<text:p>"
			+ "<text:span text:style-name=\"none\">" + "</text:span>"
			+ "</text:p>" + "</style:header>"
			+ "<style:header-left style:display=\"false\"/>" + "<style:footer>"
			+ "<text:p>" + "<text:span text:style-name=\"none\">"
			+ "</text:span>" + "</text:p>" + "</style:footer>"
			+ "<style:footer-left style:display=\"false\"/>"
			+ "</style:master-page>";
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testAlmostEmptyToAutomaticStyle() throws IOException {
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.build();
		final StringBuilder sb = new StringBuilder();
		masterPageStyle.appendXMLToAutomaticStyle(this.util, sb);
		DomTester.assertEquals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sb.toString());
	}

	@Test
	public final void testAlmostEmptyToMasterStyle() throws IOException {
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.build();
		final StringBuilder sb = new StringBuilder();
		masterPageStyle.appendXMLToMasterStyle(this.util, sb);
		DomTester.assertEquals(PageStyleTest.MASTER, sb.toString());
	}

	@Test
	public final void testBackground() throws IOException {
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.backgroundColor(Color.BLANCHEDALMOND).build();
		final StringBuilder sba = new StringBuilder();
		final StringBuilder sbm = new StringBuilder();

		masterPageStyle.appendXMLToAutomaticStyle(this.util, sba);
		masterPageStyle.appendXMLToMasterStyle(this.util, sbm);
		DomTester.assertEquals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:background-color=\"#FFEBCD\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/></style:header-style><style:footer-style><style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString());
		DomTester.assertEquals(PageStyleTest.MASTER, sbm.toString());
	}

	@Test(expected = IllegalStateException.class)
	public final void testEmpty() {
		MasterPageStyle.builder(null).build();
	}

	@Test
	public final void testFooterHeader() throws IOException {
		final FooterHeader header = PowerMock.createMock(FooterHeader.class);
		final FooterHeader footer = PowerMock.createMock(FooterHeader.class);
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.header(header).footer(footer).build();
		final StringBuilder sb = new StringBuilder();

		header.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		footer.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		/*		header.appendTextStylesXMLToStylesEntry(this.util, sb);
				footer.appendTextStylesXMLToStylesEntry(this.util, sb); */
		PowerMock.replayAll();
		masterPageStyle.appendXMLToAutomaticStyle(this.util, sb);
		DomTester.assertEquals("<style:page-layout style:name=\"test\">"
				+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
				+ "</style:page-layout>", sb.toString());
		PowerMock.verifyAll();
	}

	@Test
	public final void testHeightAndWidth() throws IOException {
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.pageHeight("20cm").pageWidth("10cm").build();
		final StringBuilder sb = new StringBuilder();
		masterPageStyle.appendXMLToAutomaticStyle(this.util, sb);
		DomTester.assertEquals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"10cm\" fo:page-height=\"20cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sb.toString());
	}

	@Test
	public final void testMargins() throws IOException {
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.allMargins("10pt").build();
		final StringBuilder sb = new StringBuilder();
		masterPageStyle.appendXMLToAutomaticStyle(this.util, sb);
		DomTester.assertEquals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"10pt\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sb.toString());

		Assert.assertEquals(new MarginsBuilder().all("10pt").build(),
				masterPageStyle.getMargins());
	}

	@Test
	public final void testMargins2() throws IOException {
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.marginTop("1pt").marginRight("2pt").marginBottom("3pt")
				.marginLeft("4pt").printOrientationVertical()
				.printOrientationHorizontal().writingMode(WritingMode.PAGE)
				.build();
		final StringBuilder sba = new StringBuilder();
		final StringBuilder sbm = new StringBuilder();

		masterPageStyle.appendXMLToAutomaticStyle(this.util, sba);
		masterPageStyle.appendXMLToMasterStyle(this.util, sbm);
		DomTester.assertEquals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" style:print-orientation=\"landscape\" fo:margin=\"1.5cm\" fo:margin-top=\"1pt\" fo:margin-right=\"2pt\" fo:margin-bottom=\"3pt\" fo:margin-left=\"4pt\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString());
		DomTester.assertEquals(PageStyleTest.MASTER, sbm.toString());
		Assert.assertEquals(
				new MarginsBuilder().top("1pt").right("2pt").bottom("3pt")
						.left("4pt").all("1.5cm").build(),
				masterPageStyle.getMargins());
	}

	@Test
	public final void testNullFooterHeader() throws IOException {
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.header(null).footer(null).build();
		final StringBuilder sb = new StringBuilder();

		PowerMock.replayAll();
		masterPageStyle.appendXMLToAutomaticStyle(this.util, sb);
		DomTester.assertEquals("<style:page-layout style:name=\"test\">"
				+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
				+ "<style:header-style/>" + "<style:footer-style/>"
				+ "</style:page-layout>", sb.toString());
		PowerMock.verifyAll();
	}

	@Test
	public final void testPaperFormat() throws IOException {
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.paperFormat(PaperFormat.A3).build();
		final StringBuilder sba = new StringBuilder();
		final StringBuilder sbm = new StringBuilder();

		masterPageStyle.appendXMLToAutomaticStyle(this.util, sba);
		masterPageStyle.appendXMLToMasterStyle(this.util, sbm);
		DomTester.assertEquals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"29.7cm\" fo:page-height=\"42.0cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString());
		DomTester.assertEquals(PageStyleTest.MASTER, sbm.toString());
		Assert.assertEquals("29.7cm", masterPageStyle.getPageWidth());
		Assert.assertEquals("42.0cm", masterPageStyle.getPageHeight());
		Assert.assertEquals(PaperFormat.A3, masterPageStyle.getPaperFormat());
	}

	@Test
	public final void testPrintOrientationH() throws IOException {
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.printOrientationHorizontal().writingMode(WritingMode.PAGE)
				.build();
		final StringBuilder sba = new StringBuilder();
		final StringBuilder sbm = new StringBuilder();

		masterPageStyle.appendXMLToAutomaticStyle(this.util, sba);
		masterPageStyle.appendXMLToMasterStyle(this.util, sbm);
		DomTester.assertEquals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" style:print-orientation=\"landscape\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString());
		DomTester.assertEquals(PageStyleTest.MASTER, sbm.toString());
	}

	@Test
	public final void testPrintOrientationV() throws IOException {
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.printOrientationVertical().writingMode(WritingMode.PAGE)
				.build();
		final StringBuilder sba = new StringBuilder();
		final StringBuilder sbm = new StringBuilder();

		masterPageStyle.appendXMLToAutomaticStyle(this.util, sba);
		masterPageStyle.appendXMLToMasterStyle(this.util, sbm);
		DomTester.assertEquals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString());
		DomTester.assertEquals(PageStyleTest.MASTER, sbm.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testWritingException() {
		MasterPageStyle.builder("test").writingMode(null);
	}

	@Test
	public final void testWritingMode() throws IOException {
		final MasterPageStyle masterPageStyle = MasterPageStyle.builder("test")
				.writingMode(WritingMode.PAGE).build();
		final StringBuilder sba = new StringBuilder();
		final StringBuilder sbm = new StringBuilder();

		masterPageStyle.appendXMLToAutomaticStyle(this.util, sba);
		masterPageStyle.appendXMLToMasterStyle(this.util, sbm);
		DomTester.assertEquals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString());
		DomTester.assertEquals(PageStyleTest.MASTER, sbm.toString());
		Assert.assertEquals(WritingMode.PAGE, masterPageStyle.getWritingMode());
	}
}
