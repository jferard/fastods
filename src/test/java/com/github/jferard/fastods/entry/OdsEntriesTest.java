package com.github.jferard.fastods.entry;

import java.util.Locale;
import java.util.logging.Logger;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class OdsEntriesTest {
	private ContentEntry contentEntry;
	private DataStyleBuilderFactory factory;
	private Locale locale;
	private ManifestEntry manifestEntry;
	private MetaEntry metaEntry;
	private MimetypeEntry mimetypeEntry;
	private OdsEntries oe;
	private SettingsEntry settingsEntry;
	private StylesContainer stylesContainer;
	private StylesEntry stylesEntry;
	private XMLUtil util;

	@Before
	public void setUp() {
		final Logger logger = PowerMock.createNiceMock(Logger.class);
		this.mimetypeEntry = PowerMock.createMock(MimetypeEntry.class);
		this.manifestEntry = PowerMock.createMock(ManifestEntry.class);
		this.settingsEntry = PowerMock.createMock(SettingsEntry.class);
		this.metaEntry = PowerMock.createMock(MetaEntry.class);
		this.contentEntry = PowerMock.createMock(ContentEntry.class);
		this.stylesEntry = PowerMock.createMock(StylesEntry.class);
		this.stylesContainer = new StylesContainer();

		this.oe = new OdsEntries(logger, this.mimetypeEntry, this.manifestEntry,
				this.settingsEntry, this.metaEntry, this.contentEntry,
				this.stylesEntry, this.stylesContainer);
		this.util = new XMLUtil(new FastOdsXMLEscaper());
		this.locale = Locale.US;
		this.factory = new DataStyleBuilderFactory(this.util, this.locale);
	}

	/*
	@Test
	public final void testAddTextStyle() {
		final TextStyle ts = TextStyle.builder("test").build();
	
		this.stylesEntry.addTextStyle(ts);
		PowerMock.replayAll();
		this.oe.addTextStyle(ts);
		PowerMock.verifyAll();
	}*/

	@Test
	public final void testGetTable() {
		final Table t = PowerMock.createMock(Table.class);
		EasyMock.expect(this.contentEntry.getTable(9)).andReturn(t);
		EasyMock.expect(this.contentEntry.getTable("nine")).andReturn(t);
		EasyMock.expect(this.contentEntry.getTableCount()).andReturn(8);
		PowerMock.replayAll();
		Assert.assertEquals(t, this.oe.getTable(9));
		Assert.assertEquals(t, this.oe.getTable("nine"));
		Assert.assertEquals(8, this.oe.getTableCount());
		PowerMock.verifyAll();
	}

}
