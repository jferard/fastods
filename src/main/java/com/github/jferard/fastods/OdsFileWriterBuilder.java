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

import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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
	 * @param logger
	 * @param document
	 */
	OdsFileWriterBuilder(final Logger logger, OdsDocument document) {
		this.logger = logger;
		this.document = document;
		builder = ZipUTF8Writer.builder();
	}

	/**
	 */
	public OdsFileWriterBuilder filename(final String filename) throws FileNotFoundException {
		this.filename = filename;
		return this;
	}

	/**
	 */
	public OdsFileWriterBuilder outputStream(final OutputStream out) throws FileNotFoundException {
		this.out = out;
		return this;
	}

	public OdsFileWriter build() throws FileNotFoundException {
		if (this.out == null)
			this.out = new FileOutputStream(this.filename);

		return new OdsFileWriter(this.logger, this.document, this.builder.build(this.out));
	}

	/**
	 * @param builder
	 *            a builder for the ZipOutputStream and the Writer (buffers,
	 *            level, ...)
	 */
	public void zipBuilder(final ZipUTF8WriterBuilder builder) {
		this.builder = builder;
	}
}
