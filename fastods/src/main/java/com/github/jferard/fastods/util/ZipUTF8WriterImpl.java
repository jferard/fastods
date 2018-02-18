/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

import java.io.IOException;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A writer for a zip file/ It's a writer and a zipper
 * @author Julien Férard
 */
public class ZipUTF8WriterImpl
		implements ZipUTF8Writer {
	private final Writer writer;
	private final ZipOutputStream zipStream;

	/**
	 * Create a new writer. Do not use directly. Use a builder if you want to avoid mistakes
	 * @param zipStream the zip stream
	 * @param writer the utf-8 writer
	 */
	ZipUTF8WriterImpl(final ZipOutputStream zipStream, final Writer writer) {
		this.zipStream = zipStream;
		this.writer = writer;
	}

    /**
     * @return a new builder
     */
    public static ZipUTF8WriterBuilder builder() {
		return new ZipUTF8WriterBuilder();
	}

	@Override
	public Appendable append(final char c) throws IOException {
		return this.writer.append(c);
	}

	@Override
	public Appendable append(final CharSequence arg0) throws IOException {
		return this.writer.append(arg0);
	}

	@Override
	public Appendable append(final CharSequence csq, final int start, final int end)
			throws IOException {
		return this.writer.append(csq, start, end);
	}

	@Override
	public void close() throws IOException {
		this.zipStream.close();
	}

	@Override
	public void closeEntry() throws IOException {
		this.writer.flush();
		this.zipStream.closeEntry();
	}

	@Override
	public void finish() throws IOException {
		this.writer.flush();
		this.zipStream.finish();
	}

	@Override
	public void flush() throws IOException {
		this.writer.flush();
	}

	@Override
	public void putNextEntry(final ZipEntry entry) throws IOException {
		this.zipStream.putNextEntry(entry);
	}

	@Override
	public void setComment(final String comment) {
		this.zipStream.setComment(comment);
	}

	@Override
	public void write(final String str) throws IOException {
		this.writer.write(str);
	}
}
