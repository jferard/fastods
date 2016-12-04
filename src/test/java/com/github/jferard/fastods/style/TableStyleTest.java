package com.github.jferard.fastods.style;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

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
	public final void testEmpty() throws IOException {
		final TableStyle ts = TableStyle.builder("test").build();
		final StringBuilder sb = new StringBuilder();
		ts.appendXML(this.util, sb);

		DomTester.assertEquals("<style:style style:name=\"test\" style:family=\"table\" style:master-page-name=\"DefaultMasterPage\">"
						+ "<style:table-properties table:display=\"true\" style:writing-mode=\"lr-tb\"/>"
						+ "</style:style>", sb.toString());
	}

	@Test
	public final void testPageStyle() throws IOException {
		final MasterPageStyle ps = MasterPageStyle.builder("p").build();
		final TableStyle ts = TableStyle.builder("test").masterPageStyle(ps).build();
		final StringBuilder sb = new StringBuilder();
		ts.appendXML(this.util, sb);

		DomTester.assertEquals("<style:style style:name=\"test\" style:family=\"table\" style:master-page-name=\"p\">"
						+ "<style:table-properties table:display=\"true\" style:writing-mode=\"lr-tb\"/>"
						+ "</style:style>", sb.toString());
		Assert.assertEquals("test", ts.getName());
	}
}
