package com.github.jferard.fastods;

import static org.junit.Assert.*;

import java.util.Locale;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.datastyle.BooleanStyle;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.LocaleDataStyles;
import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.XMLUtil;

public class OdsEntriesTest {
	private OdsEntries oe;
	private MimetypeEntry mimetypeEntry;
	private ManifestEntry manifestEntry;
	private SettingsEntry settingsEntry;
	private MetaEntry metaEntry;
	private ContentEntry contentEntry;
	private StylesEntry stylesEntry;
	private XMLUtil util;
	private Locale locale;
	private DataStyleBuilderFactory factory;

	@Before
	public void setUp() {
		this.mimetypeEntry = PowerMock.createMock(MimetypeEntry.class);
		this.manifestEntry = PowerMock.createMock(ManifestEntry.class);
		this.settingsEntry = PowerMock.createMock(SettingsEntry.class);
		this.metaEntry = PowerMock.createMock(MetaEntry.class);
		this.contentEntry = PowerMock.createMock(ContentEntry.class);
		this.stylesEntry = PowerMock.createMock(StylesEntry.class);

		this.oe = new OdsEntries(this.mimetypeEntry, this.manifestEntry, this.settingsEntry,
				this.metaEntry, this.contentEntry, this.stylesEntry);
		this.util = new XMLUtil(new FastOdsXMLEscaper());
		this.locale = Locale.US;
		this.factory = new DataStyleBuilderFactory(this.util, this.locale);
	}

	@Test
	public final void testAddDataStyle() {
		DataStyle dataStyle = this.factory.booleanStyleBuilder("test").build();
		
		this.stylesEntry.addDataStyle(dataStyle);
		PowerMock.replayAll();
		this.oe.addDataStyle(dataStyle);
		PowerMock.verifyAll();
	}

	@Test
	public final void testAddTextStyle() {
		FHTextStyle ts = FHTextStyle.builder("test").build();
		
		this.stylesEntry.addTextStyle(ts);
		PowerMock.replayAll();
		this.oe.addTextStyle(ts);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testGetTable() {
		Table t = PowerMock.createMock(Table.class);
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
