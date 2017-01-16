/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * ****************************************************************************/
package com.github.jferard.fastods;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.style.*;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;
import com.google.common.collect.Sets;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class OdsDocumentTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private DataStyleBuilderFactory dataStyleBuilderFactory;
	private OdsElements odsElements;

	private Logger logger;
	private ByteArrayOutputStream os;
	private ZipUTF8Writer writer;
	private WriteUtil writeUtil;
	private XMLUtil xmlUtil;

	@Before
	public final void setUp() {
		this.logger = PowerMock.createNiceMock(Logger.class);
		this.dataStyleBuilderFactory = new DataStyleBuilderFactory(
				XMLUtil.create(), Locale.US);
		this.os = new ByteArrayOutputStream();
		this.writer = PowerMock.createMock(ZipUTF8Writer.class);
		this.xmlUtil = XMLUtil.create();
		this.odsElements = PowerMock.createMock(OdsElements.class);
	}

	@Test
	public final void testAddBooleanStyle() {
		final DataStyle ds = this.dataStyleBuilderFactory
				.booleanStyleBuilder("b").build();

		this.initOdsElements();
		this.odsElements.addDataStyle(ds);
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		f.addDataStyle(ds);
		PowerMock.verifyAll();
	}

	@Test
	public final void testAddPageStyle() {
		final PageStyle ps = PageStyle.builder("p").build();

		this.initOdsElements();
		this.odsElements.addPageStyle(ps);
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		f.addPageStyle(ps);
		PowerMock.verifyAll();
	}

	@Test
	public final void testAddTable() {
		final Table t = PowerMock.createMock(Table.class);

		this.initOdsElements();

		EasyMock.expect(this.odsElements.addTableToContent("t1", 100, 100))
				.andReturn(t);
		this.odsElements.setActiveTable(t);
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		Assert.assertEquals(t, f.addTable("t1", 100, 100));
		PowerMock.verifyAll();
	}

	@Test
	public final void testAddTableDefault() throws FastOdsException {
		final Table t = PowerMock.createMock(Table.class);

		this.initOdsElements();

		EasyMock.expect(this.odsElements.addTableToContent(EasyMock.eq("t1"),
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(t);
		this.odsElements.setActiveTable(t);
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		Assert.assertEquals(t, f.addTable("t1"));
		PowerMock.verifyAll();
	}

	/*
	@Test
	public final void testAddTextStyle() {
		final TextStyle ts = TextStyle.builder("t").build();
	
		this.initOdsElements();
		this.odsElements.addTextStyle(ts);
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, "file", this.odsElements,
				this.writeUtil, this.xmlUtil);
		f.addTextStyle(ts);
		PowerMock.verifyAll();
	}*/

	@Test
	public final void testGetTable() throws FastOdsException {
		final Table t = PowerMock.createMock(Table.class);
		this.initOdsElements();

		EasyMock.expect(this.odsElements.getTables())
				.andReturn(Arrays.asList(t, t, t, t)).anyTimes();
		EasyMock.expect(t.getName()).andReturn("t2").anyTimes();
		EasyMock.expect(this.odsElements.getTable("t2")).andReturn(t).anyTimes();
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		Assert.assertEquals(t, f.getTable(3));
		Assert.assertEquals("t2", f.getTableName(3));
		f.getTable("t2");
		f.getTableNumber("t2");
		PowerMock.verifyAll();
	}

	@Test(expected = FastOdsException.class)
	public final void testGetTableByIndexExceptionIOOB()
			throws FastOdsException {
		this.initOdsElements();

		EasyMock.expect(this.odsElements.getTables())
				.andReturn(Arrays.<Table> asList());
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		f.getTable(3);
		PowerMock.verifyAll();
	}

	@Test(expected = FastOdsException.class)
	public final void testGetTableByIndexExceptionNegative()
			throws FastOdsException {
		this.initOdsElements();

		EasyMock.expect(this.odsElements.getTables())
				.andReturn(Arrays.<Table> asList());
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		f.getTable(-3);
		PowerMock.verifyAll();
	}

	@Test(expected = FastOdsException.class)
	public final void testGetTableByNameException() throws FastOdsException {
		this.initOdsElements();

		EasyMock.expect(this.odsElements.getTable("t1")).andReturn(null);
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		f.getTable("t1");
		PowerMock.verifyAll();
	}

	@Test
	public final void testGetTableNumberByNameException() {
		this.initOdsElements();

		EasyMock.expect(this.odsElements.getTables())
				.andReturn(Arrays.<Table> asList());
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		Assert.assertEquals(-1, f.getTableNumber("t1"));
		PowerMock.verifyAll();
	}

	@Test
	public final void testSaveTo() throws IOException {
		this.initOdsElements();
		this.odsElements.setTables();
		this.odsElements.writeElements(this.xmlUtil, this.writer);
		this.odsElements.createEmptyElements(this.writer);
		this.writer.close();

		PowerMock.replayAll();
		final OdsDocument d = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		d.save(this.writer);
		PowerMock.verifyAll();
	}

	@Test
	public final void testSaveToCloseException() throws IOException {
		this.initOdsElements();
		this.odsElements.setTables();
		this.odsElements.writeElements(this.xmlUtil, this.writer);
		this.writer.close();
		EasyMock.expectLastCall().andThrow(new IOException("@"));
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		this.thrown.expect(IOException.class);
		this.thrown.expectMessage("@");
		f.save(this.writer);
		PowerMock.verifyAll();
	}

	@Test
	public final void testSaveToOS() throws IOException {
		final OdsDocument d = new OdsFactory(Locale.US).createDocument();
		PowerMock.replayAll();

		d.save(this.os);
		final InputStream is = new ByteArrayInputStream(this.os.toByteArray());
		final ZipInputStream zis = new ZipInputStream(is);

		ZipEntry entry = zis.getNextEntry();
		final Set<String> names = new HashSet<String>();
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
	public final void testSaveWriterException() throws IOException {
		this.initOdsElements();
		this.odsElements.setTables();
		this.odsElements.writeElements(this.xmlUtil, this.writer);
		EasyMock.expectLastCall().andThrow(new IOException("@"));
		this.writer.close();
		PowerMock.replayAll();
		final OdsDocument d = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		this.thrown.expect(IOException.class);
		this.thrown.expectMessage("@");
		d.save(this.writer);
		PowerMock.verifyAll();
	}

	@Test
	public final void testTableCount() {
		this.initOdsElements();
		EasyMock.expect(this.odsElements.getTableCount()).andReturn(11);
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		Assert.assertEquals(11, f.tableCount());
		PowerMock.verifyAll();
	}

	@Test
	public final void testGetTableNumber() {
		Table t = PowerMock.createMock(Table.class);

		// PLAY
		this.initOdsElements();
		EasyMock.expect(this.odsElements.getTables()).andReturn(Arrays.asList(t))
				.anyTimes();
		EasyMock.expect(t.getName()).andReturn("@t").anyTimes();
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		Assert.assertEquals(-1, f.getTableNumber("@s"));
		Assert.assertEquals(0, f.getTableNumber("@t"));
		Assert.assertEquals(-1, f.getTableNumber("@T"));
		Assert.assertEquals(Arrays.asList(t), f.getTables());
		PowerMock.verifyAll();
	}

	@Test
	public final void testSetActiveTable() {
		Table t = PowerMock.createMock(Table.class);

		// PLAY
		this.initOdsElements();
		EasyMock.expect(this.odsElements.getTableCount()).andReturn(1).anyTimes();
		EasyMock.expect(this.odsElements.getTable(0)).andReturn(t).anyTimes();
		this.odsElements.setActiveTable(t);
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		Assert.assertFalse(f.setActiveTable(-1));
		Assert.assertFalse(f.setActiveTable(1));
		Assert.assertTrue(f.setActiveTable(0));
		PowerMock.verifyAll();
	}

	@Test(expected = IOException.class)
	public final void testFileIsDir() throws IOException {
		// PLAY
		this.initOdsElements();
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		f.saveAs(".");
		PowerMock.verifyAll();
	}

	@Test
	public final void testSaveWriter() throws IOException {
		ZipUTF8WriterBuilder zb = PowerMock
				.createMock(ZipUTF8WriterBuilder.class);
		ZipUTF8Writer z = PowerMock.createMock(ZipUTF8Writer.class);
		File temp = File.createTempFile("tempfile", ".tmp");
		
		// PLAY
		this.initOdsElements();
		this.odsElements.setTables();
		EasyMock.expect(zb.build(EasyMock.isA(FileOutputStream.class)))
				.andReturn(z);
		this.odsElements.writeElements(this.xmlUtil, z);
		this.odsElements.createEmptyElements(z);
		z.close();
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		f.saveAs("file", zb);
		PowerMock.verifyAll();
	}

	@Test // (expected = IOException.class)
	public final void testSaveWriterWithException() throws IOException {
		OutputStream o = PowerMock.createMock(OutputStream.class);
		
		// PLAY
		this.initOdsElements();
		this.odsElements.setTables();
		this.odsElements.writeElements(EasyMock.eq(this.xmlUtil), EasyMock.isA(ZipUTF8Writer.class));
		this.odsElements.createEmptyElements(EasyMock.isA(ZipUTF8Writer.class));
		o.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		EasyMock.expectLastCall().anyTimes();
		o.flush();
		EasyMock.expectLastCall().anyTimes();
		o.close();
		PowerMock.replayAll();
		final OdsDocument f = new OdsDocument(this.logger, this.odsElements,
				this.xmlUtil);
		f.save(o);
		PowerMock.verifyAll();
	}

	protected void initOdsElements() {
		TableStyle.DEFAULT_TABLE_STYLE.addToElements(this.odsElements);
		TableRowStyle.DEFAULT_TABLE_ROW_STYLE.addToElements(this.odsElements);
		TableColumnStyle.getDefaultColumnStyle(this.xmlUtil)
				.addToElements(this.odsElements);
		TableCellStyle.getDefaultCellStyle().addToElements(this.odsElements);
		PageStyle.DEFAULT_PAGE_STYLE.addToElements(this.odsElements);
	}
}
