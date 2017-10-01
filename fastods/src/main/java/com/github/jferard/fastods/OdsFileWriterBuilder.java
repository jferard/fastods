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

import com.github.jferard.fastods.util.FileOpenResult;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;
import com.github.jferard.fastods.util.ZipUTF8WriterImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class OdsFileWriterBuilder {
	private final Logger logger;
	private final OdsDocument document;
	private OutputStream out;
	private ZipUTF8WriterBuilder builder;
	private String filename;

	/**
	 * Create a new ODS file.
	 *
	 * @param logger   the logger
	 * @param document the document to write
	 */
	OdsFileWriterBuilder(final Logger logger, final OdsDocument document) {
		this.logger = logger;
		this.document = document;
		this.builder = ZipUTF8WriterImpl.builder();
	}

	/**
	 * @param filename the name of the destination file
	 * @return this for fluent style
	 * @throws FileNotFoundException if the file can't be found
	 */
	@Deprecated
	public OdsFileWriterBuilder filename(final String filename) throws FileNotFoundException {
		this.filename = filename;
		return this;
	}

	/**
	 * @param out where to write
	 * @return this for fluent style
	 */
	public OdsFileWriterBuilder outputStream(final OutputStream out) {
		this.out = out;
		return this;
	}

	/**
	 * @param lockResult the result of a file lock
	 * @return this for fluent style
	 * @throws FileNotFoundException the file exists but is a directory
	 *                               rather than a regular file, does not exist but cannot
	 *                               be created, or cannot be opened for any other reason
	 */
	public OdsFileWriterBuilder openResult(final FileOpenResult lockResult) throws FileNotFoundException {
		this.out = lockResult.getStream();
		return this;
	}

	/**
	 * @return the writer for the ods file
	 * @throws FileNotFoundException if there is no stream to write
	 */
	public OdsFileWriter build() throws FileNotFoundException {
		if (this.out == null)
			this.out = new FileOutputStream(this.filename);

		ZipUTF8Writer writer = this.builder.build(this.out);
		return new OdsFileDirectWriter(this.logger, this.document, XMLUtil.create(), writer);
	}

	/**
	 * @param builder a builder for the ZipOutputStream and the Writer (buffers,
	 *                level, ...)
	 * @return this for fluent style
	 */
	public OdsFileWriterBuilder zipBuilder(final ZipUTF8WriterBuilder builder) {
		this.builder = builder;
		return this;
	}
}
