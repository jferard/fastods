/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUTF8WriterTest {

	private ZipUTF8Writer zipUTF8Writer;
	private StringWriter writer;
	private ByteArrayOutputStream out;

	@Before
	public void setUp() throws Exception {
		this.writer = new StringWriter();
		this.out = new ByteArrayOutputStream();
		this.zipUTF8Writer = new ZipUTF8WriterImpl(new ZipOutputStream(this.out), this.writer);
		PowerMock.resetAll();
	}

	@Test
	public final void test() throws IOException {
		this.zipUTF8Writer.setComment("comment");
		this.zipUTF8Writer.putNextEntry(new ZipEntry("a"));
		this.zipUTF8Writer.append("text", 0, 2);
		Assert.assertEquals(this.writer.toString(), "te");
		Assert.assertEquals(31, this.out.toByteArray().length);
		this.zipUTF8Writer.closeEntry();
		this.zipUTF8Writer.finish();
	}

}
