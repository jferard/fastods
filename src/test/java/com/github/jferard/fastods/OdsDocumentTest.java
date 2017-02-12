/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
 */
package com.github.jferard.fastods;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;

public class OdsDocumentTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	private ZipUTF8WriterBuilder builder;

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
		this.builder = ZipUTF8Writer.builder();
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
	public final void testAddTable() throws IOException {
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
	public final void testAddTableDefault() throws FastOdsException, IOException {
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
		this.odsElements.createEmptyElements(this.writer);
		this.odsElements.writeImmutableElements(this.xmlUtil, this.writer);
		this.odsElements.writeMeta(this.xmlUtil, this.writer);
		this.odsElements.writeStyles(this.xmlUtil, this.writer);
		this.odsElements.writeContent(this.xmlUtil, this.writer);
		this.odsElements.writeSettings(this.xmlUtil, this.writer);
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
		this.odsElements.createEmptyElements(this.writer);
		this.odsElements.writeImmutableElements(this.xmlUtil, this.writer);
		this.odsElements.writeMeta(this.xmlUtil, this.writer);
		this.odsElements.writeStyles(this.xmlUtil, this.writer);
		this.odsElements.writeContent(this.xmlUtil, this.writer);
		this.odsElements.writeSettings(this.xmlUtil, this.writer);
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
	public final void testSaveWriterException() throws IOException {
		this.initOdsElements();
		this.odsElements.createEmptyElements(this.writer);
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
		final Table t = PowerMock.createMock(Table.class);

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
		final Table t = PowerMock.createMock(Table.class);

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

	protected void initOdsElements() {
		TableStyle.DEFAULT_TABLE_STYLE.addToElements(this.odsElements);
		TableRowStyle.DEFAULT_TABLE_ROW_STYLE.addToElements(this.odsElements);
		TableColumnStyle.getDefaultColumnStyle(this.xmlUtil)
				.addToElements(this.odsElements);
		TableCellStyle.getDefaultCellStyle().addToElements(this.odsElements);
		PageStyle.DEFAULT_PAGE_STYLE.addToElements(this.odsElements);
	}
}
