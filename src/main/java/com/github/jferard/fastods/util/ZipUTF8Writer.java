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

package com.github.jferard.fastods.util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUTF8Writer
		implements Closeable, Flushable, Appendable {
	public static final Charset UTF_8 = Charset.forName("UTF-8");
	private final Writer writer;
	private final ZipOutputStream zipStream;

	ZipUTF8Writer(final ZipOutputStream zipStream, final Writer writer) {
		this.zipStream = zipStream;
		this.writer = writer;
	}

	public static ZipUTF8WriterBuilder builder() {
		return new ZipUTF8WriterBuilder();
	}

	@Override
	public Writer append(final char c) throws IOException {
		return this.writer.append(c);
	}

	@Override
	public Writer append(final CharSequence arg0) throws IOException {
		return this.writer.append(arg0);
	}

	@Override
	public Writer append(final CharSequence csq, final int start, final int end)
			throws IOException {
		return this.writer.append(csq, start, end);
	}

	@Override
	public void close() throws IOException {
		this.zipStream.close();
	}

	public void closeEntry() throws IOException {
		this.writer.flush();
		this.zipStream.closeEntry();
	}

	public void finish() throws IOException {
		this.writer.flush();
		this.zipStream.finish();
	}

	@Override
	public void flush() throws IOException {
		this.writer.flush();
	}

	public void putNextEntry(final ZipEntry arg0) throws IOException {
		this.zipStream.putNextEntry(arg0);
	}

	public void setComment(final String arg0) {
		this.zipStream.setComment(arg0);
	}

	public void write(final String str) throws IOException {
		this.writer.write(str);
	}
}
