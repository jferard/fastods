package com.github.jferard.fastods;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.entry.OdsEntries;
import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.google.common.collect.Sets;

public class OdsFileTest {

	private DataStyleBuilderFactory dataStyleBuilderFactory;
	private ByteArrayOutputStream os;

	private ZipOutputStream zos;
	private XMLUtil xmlUtil;
	private WriteUtil writeUtil;
	private OdsEntries entries;
	private ZipUTF8Writer writer;

	@Before
	public final void setUp() {
		this.dataStyleBuilderFactory = new DataStyleBuilderFactory(
				XMLUtil.create(), Locale.US);
		this.os = new ByteArrayOutputStream();
		this.writer = PowerMock.createMock(ZipUTF8Writer.class);
		this.xmlUtil = XMLUtil.create();
		this.entries = PowerMock.createMock(OdsEntries.class);
	}

	@Test
	public final void testSaveToOS() throws IOException {
		OdsFile f = OdsFile.create(Locale.US, "file");
		PowerMock.replayAll();

		f.save(this.os);
		InputStream is = new ByteArrayInputStream(this.os.toByteArray());
		ZipInputStream zis = new ZipInputStream(is);

		ZipEntry entry = zis.getNextEntry();
		Set<String> names = new HashSet<String>();
		while (entry != null) {
			names.add(entry.getName());
			entry = zis.getNextEntry();
		}
		Assert.assertEquals(Sets.newHashSet("settings.xml",
				"Configurations2/images/Bitmaps/", "Configurations2/toolbar/",
				"META-INF/manifest.xml", "Thumbnails/",
				"Configurations2/floater/", "Configurations2/menubar/",
				"mimetype", "meta.xml",
				"Configurations2/accelerator/current.xml",
				"Configurations2/popupmenu/", "styles.xml", "content.xml",
				"Configurations2/progressbar/", "Configurations2/statusbar/"),
				names);
		PowerMock.verifyAll();
	}

	@Test
	public final void testSaveTo() throws IOException {
		this.initEntries();
		this.entries.setTables();
		this.entries.writeEntries(this.xmlUtil, this.writer);
		this.entries.createEmptyEntries(this.writer);
		this.writer.close();
		
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		Assert.assertEquals("file", f.getName());
		f.save(this.writer);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testSaveWriterException() throws IOException {
		this.initEntries();
		this.entries.setTables();
		this.entries.writeEntries(this.xmlUtil, this.writer);
		EasyMock.expectLastCall().andThrow(new IOException());
		this.writer.close();
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		Assert.assertEquals("file", f.getName());
		Assert.assertFalse(f.save(this.writer));
		PowerMock.verifyAll();
	}

	@Test
	public final void testSaveToCloseException() throws IOException {
		this.initEntries();
		this.entries.setTables();
		this.entries.writeEntries(this.xmlUtil, this.writer);
		this.writer.close();
		EasyMock.expectLastCall().andThrow(new IOException());
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		Assert.assertEquals("file", f.getName());
		Assert.assertFalse(f.save(this.writer));
		PowerMock.verifyAll();
	}
	
	protected void initEntries() {
		TableStyle.DEFAULT_TABLE_STYLE.addToEntries(this.entries);
		TableRowStyle.DEFAULT_TABLE_ROW_STYLE.addToEntries(this.entries);
		TableColumnStyle.getDefaultColumnStyle(this.xmlUtil)
				.addToEntries(this.entries);
		TableCellStyle.getDefaultCellStyle().addToEntries(this.entries);
		PageStyle.DEFAULT_PAGE_STYLE.addToEntries(this.entries);
	}
	
	@Test
	public final void testAddBooleanStyle() {
		DataStyle ds = this.dataStyleBuilderFactory.booleanStyleBuilder("b")
				.build();
		
		this.initEntries();
		this.entries.addDataStyle(ds);
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		f.addDataStyle(ds);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testAddPageStyle() {
		PageStyle ps = PageStyle.builder("p").build();
		
		this.initEntries();
		this.entries.addPageStyle(ps);
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		f.addPageStyle(ps);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testAddTextStyle() {
		FHTextStyle ts = FHTextStyle.builder("t").build();
		
		this.initEntries();
		this.entries.addTextStyle(ts);
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		f.addTextStyle(ts);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testAddTable() throws FastOdsException {
		Table t = PowerMock.createMock(Table.class);
		
		this.initEntries();
		
		expect(this.entries.addTableToContent("t1", 100, 100)).andReturn(t);
		this.entries.setActiveTable(t);
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		Assert.assertEquals(t,  f.addTable("t1", 100, 100));
		PowerMock.verifyAll();
	}

	@Test
	public final void testAddTableDefault() throws FastOdsException {
		Table t = PowerMock.createMock(Table.class);
		
		this.initEntries();
		
		expect(this.entries.addTableToContent(eq("t1"), anyInt(), anyInt())).andReturn(t);
		this.entries.setActiveTable(t);
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		Assert.assertEquals(t,  f.addTable("t1"));
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testGetTable() throws FastOdsException {
		Table t = PowerMock.createMock(Table.class);
		this.initEntries();
		
		expect(this.entries.getTables()).andReturn(Arrays.asList(t, t, t, t)).anyTimes();
		expect(t.getName()).andReturn("t2").anyTimes();
		expect(this.entries.getTable("t2")).andReturn(t).anyTimes();
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		Assert.assertEquals(t, f.getTable(3));
		Assert.assertEquals("t2", f.getTableName(3));
		f.getTable("t2");
		f.getTableNumber("t2");
		PowerMock.verifyAll();
	}

	@Test(expected=FastOdsException.class)
	public final void testGetTableByIndexExceptionIOOB() throws FastOdsException {
		Table t = PowerMock.createMock(Table.class);
		this.initEntries();
		
		expect(this.entries.getTables()).andReturn(Arrays.<Table>asList());
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		f.getTable(3);
		PowerMock.verifyAll();
	}

	@Test(expected=FastOdsException.class)
	public final void testGetTableByIndexExceptionNegative() throws FastOdsException {
		Table t = PowerMock.createMock(Table.class);
		this.initEntries();
		
		expect(this.entries.getTables()).andReturn(Arrays.<Table>asList());
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		f.getTable(-3);
		PowerMock.verifyAll();
	}
	
	@Test(expected=FastOdsException.class)
	public final void testGetTableByNameException() throws FastOdsException {
		Table t = PowerMock.createMock(Table.class);
		this.initEntries();
		
		expect(this.entries.getTable("t1")).andReturn(null);
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		f.getTable("t1");
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testGetTableNumberByNameException() throws FastOdsException {
		Table t = PowerMock.createMock(Table.class);
		this.initEntries();
		
		expect(this.entries.getTables()).andReturn(Arrays.<Table>asList());
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		Assert.assertEquals(-1, f.getTableNumber("t1"));
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testTableCount() {
		this.initEntries();
		expect(this.entries.getTableCount()).andReturn(11);
		PowerMock.replayAll();
		OdsFile f = new OdsFile("file", this.entries, this.writeUtil, this.xmlUtil,
				1000);
		Assert.assertEquals(11, f.tableCount());
		PowerMock.verifyAll();
	}
}

