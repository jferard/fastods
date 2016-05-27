package com.github.jferard.fastods;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class PageStyleTest {
	@Test(expected = IllegalStateException.class)
	public final void testEmpty() throws IOException {
		PageStyle pageStyle = PageStyle.builder(null).build();
	}

	@Test
	public final void testAlmostEmptyToAutomaticStyle() throws IOException {
		PageStyle pageStyle = PageStyle.builder("test").build();
		StringBuilder sb = new StringBuilder();
		pageStyle.appendXMLToAutomaticStyle(Util.getInstance(), sb);
		Assert.assertEquals("<style:page-layout style:name=\"test\">"
				+ "<style:page-layout-properties fo:page-width=\"29.7cm\" fo:page-height=\"21.0cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin-top=\"1.5cm\" fo:margin-bottom=\"1.5cm\" fo:margin-left=\"1.5cm\" fo:margin-right=\"1.5cm\"/>"
				+ "<style:header-style/>" + "<style:footer-style/>"
				+ "</style:page-layout>", sb.toString());
	}

	@Test
	public final void testAlmostEmptyToMasterStyle() throws IOException {
		PageStyle pageStyle = PageStyle.builder("test").build();
		StringBuilder sb = new StringBuilder();
		pageStyle.appendXMLToMasterStyle(Util.getInstance(), sb);
		Assert.assertEquals(
				"<style:master-page style:name=\"DefaultMasterPage\" style:page-layout-name=\"test\">"
						+ "<style:header>" + "<text:p text:style-name=\"\">"
						+ "</text:p>" + "</style:header>" + "<style:header-left style:display=\"false\"/>" + "<style:footer>"
						+ "<text:p text:style-name=\"\">" + "</text:p>"
						+ "</style:footer>" + "<style:footer-left style:display=\"false\"/>" + "</style:master-page>",
				sb.toString());
	}

}
