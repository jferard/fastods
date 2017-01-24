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

import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class AnonymousOdsFileWriter {
	private final OdsDocument document;
	private final Logger logger;

	/**
	 * Create a new ODS file.
	 *
	 * @param logger   the logger
	 * @param document the document to write
	 */
	AnonymousOdsFileWriter(final Logger logger, final OdsDocument document) {
		this.logger = logger;
		this.document = document;
	}

	/**
	 * Save the new file.
	 *
	 * @param out The OutputStream that should be used.
	 * @throws IOException The file can't be saved.
	 */
	public void save(final OutputStream out) throws IOException {
		final ZipUTF8WriterBuilder builder = ZipUTF8Writer.builder();
		this.save(builder.build(out));
	}

	public void save(final ZipUTF8Writer writer) throws IOException {
		this.document.save(writer);
	}

	/**
	 * Save the new file.
	 *
	 * @param filename the name of the destination file
	 * @throws IOException If an I/O error occurs
	 */
	public void saveAs(final String filename) throws IOException {
		this.saveAs(new File(filename));
	}

	/**
	 * Save the new file.
	 *
	 * @param file the destination file
	 * @throws IOException If an I/O error occurs
	 */
	public void saveAs(final File file) throws IOException {
		try {
			final FileOutputStream out = new FileOutputStream(file);
			this.save(out);
		} catch (final FileNotFoundException e) {
			this.logger.log(Level.SEVERE, "Can't open " + file, e);
			throw new IOException(e);
		} catch (final NullPointerException e) {
			this.logger.log(Level.SEVERE, "No file", e);
			throw new IOException(e);
		}
	}

	/**
	 * @param filename the name of the destination file
	 * @param builder  a builder for the ZipOutputStream and the Writer (buffers,
	 *                 level, ...)
	 * @throws IOException if the file was not saved
	 */
	public void saveAs(final String filename, final ZipUTF8WriterBuilder builder) throws IOException {
		try {
			final FileOutputStream out = new FileOutputStream(filename);
			this.save(builder.build(out));
		} catch (final FileNotFoundException e) {
			this.logger.log(Level.SEVERE, "Can't open " + filename, e);
			throw new IOException(e);
		}
	}

}