package com.github.jferard.fastods.style;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.xml.sax.SAXException;

import com.github.jferard.fastods.DomTester;
import com.github.jferard.fastods.entry.OdsEntries;
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
		final TableStyle ts = TableStyle.builder("test").build();
		final OdsEntries entries = PowerMock.createMock(OdsEntries.class);
		final StyleTag styleTag = ts;

		entries.addStyleTag(styleTag);
		PowerMock.replayAll();

		ts.addToEntries(entries);

		PowerMock.verifyAll();
	}

	@Test
	public final void testEmpty() throws IOException, SAXException {
		final TableStyle ts = TableStyle.builder("test").build();
		final StringBuilder sb = new StringBuilder();
		ts.appendXMLToContentEntry(this.util, sb);

		Assert.assertTrue(DomTester
				.equals("<style:style style:name=\"test\" style:family=\"table\" style:master-page-name=\"DefaultMasterPage\">"
						+ "<style:table-properties table:display=\"true\" style:writing-mode=\"lr-tb\"/>"
						+ "</style:style>", sb.toString()));
	}

	@Test
	public final void testPageStyle() throws IOException, SAXException {
		final PageStyle ps = PageStyle.builder("p").build();
		final TableStyle ts = TableStyle.builder("test").pageStyle(ps).build();
		final StringBuilder sb = new StringBuilder();
		ts.appendXMLToContentEntry(this.util, sb);

		Assert.assertTrue(DomTester
				.equals("<style:style style:name=\"test\" style:family=\"table\" style:master-page-name=\"p\">"
						+ "<style:table-properties table:display=\"true\" style:writing-mode=\"lr-tb\"/>"
						+ "</style:style>", sb.toString()));
		Assert.assertEquals("test", ts.getName());
	}
}
