package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class PageStyleTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}
	
	@Test(expected = IllegalStateException.class)
	public final void testEmpty() {
		PageStyle pageStyle = PageStyle.builder(null).build();
	}

	@Test
	public final void testAlmostEmptyToAutomaticStyle() throws IOException {
		PageStyle pageStyle = PageStyle.builder("test").build();
		StringBuilder sb = new StringBuilder();
		pageStyle.appendXMLToAutomaticStyle(this.util, sb);
		Assert.assertEquals(
				"<style:page-layout style:name=\"test\">"
						+ "<style:page-layout-properties fo:page-width=\"29.7cm\" fo:page-height=\"21.0cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin-top=\"1.5cm\" fo:margin-bottom=\"1.5cm\" fo:margin-left=\"1.5cm\" fo:margin-right=\"1.5cm\"/>"
						+ "<style:header-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin-left=\"0cm\" fo:margin-right=\"0cm\" fo:margin-top=\"0cm\"/>"
						+ "</style:header-style>" + "<style:footer-style>"
						+ "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin-left=\"0cm\" fo:margin-right=\"0cm\" fo:margin-top=\"0cm\"/>"
						+ "</style:footer-style>" + "</style:page-layout>",
				sb.toString());
	}

	@Test
	public final void testAlmostEmptyToMasterStyle() throws IOException {
		PageStyle pageStyle = PageStyle.builder("test").build();
		StringBuilder sb = new StringBuilder();
		pageStyle.appendXMLToMasterStyle(this.util, sb);
		Assert.assertEquals(
				"<style:master-page style:name=\"DefaultMasterPage\" style:page-layout-name=\"test\">"
						+ "<style:header>" + "<text:p text:style-name=\"none\">"
						+ "</text:p>" + "</style:header>"
						+ "<style:header-left style:display=\"false\"/>"
						+ "<style:footer>" + "<text:p text:style-name=\"none\">"
						+ "</text:p>" + "</style:footer>"
						+ "<style:footer-left style:display=\"false\"/>"
						+ "</style:master-page>",
				sb.toString());
	}

}
