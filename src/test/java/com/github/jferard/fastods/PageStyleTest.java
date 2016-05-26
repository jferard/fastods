package com.github.jferard.fastods;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class PageStyleTest {

	@Test(expected = IllegalStateException.class)
	public final void testEmpty() throws IOException {
		PageStyle pageStyle = PageStyle.builder().build();
	}
	
	@Test
	public final void testAlmostEmpty() throws IOException {
		PageStyle pageStyle = PageStyle.builder().name("test").build();
		StringBuilder sb = new StringBuilder();
		pageStyle.appendXMLToAutomaticStyle(Util.getInstance(), sb);
		Assert.assertEquals("<style:page-layout style:name=\"test\">"+
				"<style:page-layout-properties fo:page-width=\"29.7cm\" fo:page-height=\"21.0cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin-top=\"1.5cm\" fo:margin-bottom=\"1.5cm\" fo:margin-left=\"1.5cm\" fo:margin-right=\"1.5cm\"/>"+
				"<style:header-style />"+
				"<style:footer-style />"+
				"</style:page-layout>", sb.toString());
	}

}
