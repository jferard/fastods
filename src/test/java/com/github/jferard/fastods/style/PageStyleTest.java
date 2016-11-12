package com.github.jferard.fastods.style;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.xml.sax.SAXException;

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.DomTester;
import com.github.jferard.fastods.FooterHeader;
import com.github.jferard.fastods.style.PageStyle.PaperFormat;
import com.github.jferard.fastods.style.PageStyle.WritingMode;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class PageStyleTest {
	private static final String MASTER = "<style:master-page style:name=\"DefaultMasterPage\" style:page-layout-name=\"test\">"
			+ "<style:header>" + "<text:p>"
			+ "<text:span text:style-name=\"none\">" + "</text:span>"
			+ "</text:p>" + "</style:header>"
			+ "<style:header-left style:display=\"false\"/>" + "<style:footer>"
			+ "<text:p>" + "<text:span text:style-name=\"none\">" + "</text:span>"
			+ "</text:p>" + "</style:footer>"
			+ "<style:footer-left style:display=\"false\"/>"
			+ "</style:master-page>";
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test(expected = IllegalStateException.class)
	public final void testEmpty() {
		PageStyle.builder(null).build();
	}

	@Test
	public final void testAlmostEmptyToAutomaticStyle()
			throws IOException, SAXException {
		PageStyle pageStyle = PageStyle.builder("test").build();
		StringBuilder sb = new StringBuilder();
		pageStyle.appendXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sb.toString()));
	}

	@Test
	public final void testAlmostEmptyToMasterStyle()
			throws IOException, SAXException {
		PageStyle pageStyle = PageStyle.builder("test").build();
		StringBuilder sb = new StringBuilder();
		pageStyle.appendXMLToMasterStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals(MASTER, sb.toString()));
	}

	@Test
	public final void testMargins() throws IOException, SAXException {
		PageStyle pageStyle = PageStyle.builder("test").allMargins("10pt")
				.build();
		StringBuilder sb = new StringBuilder();
		pageStyle.appendXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"10pt\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sb.toString()));

		Assert.assertEquals((new MarginsBuilder()).all("10pt").build(),
				pageStyle.getMargins());
	}

	@Test
	public final void testHeightAndWidth() throws IOException, SAXException {
		PageStyle pageStyle = PageStyle.builder("test").pageHeight("20cm")
				.pageWidth("10cm").build();
		StringBuilder sb = new StringBuilder();
		pageStyle.appendXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"10cm\" fo:page-height=\"20cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sb.toString()));
	}

	@Test
	public final void testFooterHeader() throws IOException, SAXException {
		FooterHeader header = PowerMock.createMock(FooterHeader.class);
		FooterHeader footer = PowerMock.createMock(FooterHeader.class);
		PageStyle pageStyle = PageStyle.builder("test").header(header)
				.footer(footer).build();
		StringBuilder sb = new StringBuilder();

		header.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		footer.appendStyleFooterHeaderXMLToAutomaticStyle(this.util, sb);
		header.appendTextStylesXMLToAutomaticStyle(this.util, sb);
		footer.appendTextStylesXMLToAutomaticStyle(this.util, sb);
		PowerMock.replayAll();
		pageStyle.appendXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(
				DomTester.equals("<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "</style:page-layout>", sb.toString()));
		PowerMock.verifyAll();
	}

	@Test
	public final void testNullFooterHeader() throws IOException, SAXException {
		PageStyle pageStyle = PageStyle.builder("test").header(null)
				.footer(null).build();
		StringBuilder sb = new StringBuilder();

		PowerMock.replayAll();
		pageStyle.appendXMLToAutomaticStyle(this.util, sb);
		Assert.assertTrue(
				DomTester.equals("<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style/>" + "<style:footer-style/>"
						+ "</style:page-layout>", sb.toString()));
		PowerMock.verifyAll();
	}

	@Test
	public final void testBackground() throws IOException, SAXException {
		PageStyle pageStyle = PageStyle.builder("test")
				.backgroundColor(Color.BLANCHEDALMOND).build();
		StringBuilder sba = new StringBuilder();
		StringBuilder sbm = new StringBuilder();

		pageStyle.appendXMLToAutomaticStyle(this.util, sba);
		pageStyle.appendXMLToMasterStyle(this.util, sbm);
		Assert.assertTrue(DomTester.equals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:background-color=\"#FFEBCD\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/></style:header-style><style:footer-style><style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString()));
		Assert.assertTrue(DomTester.equals(MASTER, sbm.toString()));
	}

	@Test
	public final void testMargins2() throws IOException, SAXException {
		PageStyle pageStyle = PageStyle.builder("test").marginTop("1pt")
				.marginRight("2pt").marginBottom("3pt").marginLeft("4pt")
				.printOrientationVertical().printOrientationHorizontal()
				.writingMode(WritingMode.PAGE).build();
		StringBuilder sba = new StringBuilder();
		StringBuilder sbm = new StringBuilder();

		pageStyle.appendXMLToAutomaticStyle(this.util, sba);
		pageStyle.appendXMLToMasterStyle(this.util, sbm);
		Assert.assertTrue(DomTester.equals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" style:print-orientation=\"landscape\" fo:margin=\"1.5cm\" fo:margin-top=\"1pt\" fo:margin-right=\"2pt\" fo:margin-bottom=\"3pt\" fo:margin-left=\"4pt\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString()));
		Assert.assertTrue(DomTester.equals(MASTER, sbm.toString()));
		Assert.assertEquals(
				(new MarginsBuilder()).top("1pt").right("2pt").bottom("3pt")
						.left("4pt").all("1.5cm").build(),
				pageStyle.getMargins());
	}

	@Test
	public final void testPrintOrientationV() throws IOException, SAXException {
		PageStyle pageStyle = PageStyle.builder("test")
				.printOrientationVertical().writingMode(WritingMode.PAGE)
				.build();
		StringBuilder sba = new StringBuilder();
		StringBuilder sbm = new StringBuilder();

		pageStyle.appendXMLToAutomaticStyle(this.util, sba);
		pageStyle.appendXMLToMasterStyle(this.util, sbm);
		Assert.assertTrue(DomTester.equals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString()));
		Assert.assertTrue(DomTester.equals(MASTER, sbm.toString()));
	}

	@Test
	public final void testPrintOrientationH() throws IOException, SAXException {
		PageStyle pageStyle = PageStyle.builder("test")
				.printOrientationHorizontal().writingMode(WritingMode.PAGE)
				.build();
		StringBuilder sba = new StringBuilder();
		StringBuilder sbm = new StringBuilder();

		pageStyle.appendXMLToAutomaticStyle(this.util, sba);
		pageStyle.appendXMLToMasterStyle(this.util, sbm);
		Assert.assertTrue(DomTester.equals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" style:print-orientation=\"landscape\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString()));
		Assert.assertTrue(DomTester.equals(MASTER, sbm.toString()));
	}

	@Test
	public final void testWritingMode() throws IOException, SAXException {
		PageStyle pageStyle = PageStyle.builder("test")
				.writingMode(WritingMode.PAGE).build();
		StringBuilder sba = new StringBuilder();
		StringBuilder sbm = new StringBuilder();

		pageStyle.appendXMLToAutomaticStyle(this.util, sba);
		pageStyle.appendXMLToMasterStyle(this.util, sbm);
		Assert.assertTrue(DomTester.equals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"21.0cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"page\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString()));
		Assert.assertTrue(DomTester.equals(MASTER, sbm.toString()));
		Assert.assertEquals(WritingMode.PAGE, pageStyle.getWritingMode());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testWritingException() throws IOException, SAXException {
		PageStyle.builder("test").writingMode(null);
	}

	@Test
	public final void testPaperFormat() throws IOException, SAXException {
		PageStyle pageStyle = PageStyle.builder("test")
				.paperFormat(PaperFormat.A3).build();
		StringBuilder sba = new StringBuilder();
		StringBuilder sbm = new StringBuilder();

		pageStyle.appendXMLToAutomaticStyle(this.util, sba);
		pageStyle.appendXMLToMasterStyle(this.util, sbm);
		Assert.assertTrue(DomTester.equals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"29.7cm\" fo:page-height=\"42.0cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sba.toString()));
		Assert.assertTrue(DomTester.equals(MASTER, sbm.toString()));
		Assert.assertEquals("29.7cm", pageStyle.getPageWidth());
		Assert.assertEquals("42.0cm", pageStyle.getPageHeight());
		Assert.assertEquals(PaperFormat.A3, pageStyle.getPaperFormat());
	}
}
