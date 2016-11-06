package com.github.jferard.fastods.style;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.xml.sax.SAXException;

import com.github.jferard.fastods.DomTester;
import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class TableStyleTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testAddEmptyToFile() {
		TableStyle ts = TableStyle.builder("test").build();
		OdsFile f = PowerMock.createMock(OdsFile.class);

		f.addStyleTag(ts);
		PowerMock.replayAll();

		ts.addToFile(f);

		PowerMock.verifyAll();
	}

	@Test
	public final void testEmpty() throws IOException, SAXException {
		TableStyle ts = TableStyle.builder("test").build();
		final StringBuilder sb = new StringBuilder();
		ts.appendXMLToContentEntry(this.util, sb);

		Assert.assertTrue(DomTester.equals(
				"<style:style style:name=\"test\" style:family=\"table\" style:master-page-name=\"DefaultMasterPage\">"
						+ "<style:table-properties table:display=\"true\" style:writing-mode=\"lr-tb\"/>"
						+ "</style:style>",
				sb.toString()));
	}

	@Test
	public final void testPageStyle() throws IOException, SAXException {
		PageStyle ps = PageStyle.builder("p").build();
		TableStyle ts = TableStyle.builder("test").pageStyle(ps).build();
		final StringBuilder sb = new StringBuilder();
		ts.appendXMLToContentEntry(this.util, sb);

		Assert.assertTrue(DomTester.equals(
				"<style:style style:name=\"test\" style:family=\"table\" style:master-page-name=\"p\">"
						+ "<style:table-properties table:display=\"true\" style:writing-mode=\"lr-tb\"/>"
						+ "</style:style>",
				sb.toString()));
		Assert.assertEquals("test", ts.getName());
	}
}
