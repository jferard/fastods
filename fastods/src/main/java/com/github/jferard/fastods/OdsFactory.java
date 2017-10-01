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

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesFactory;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.FileExists;
import com.github.jferard.fastods.util.FileOpen;
import com.github.jferard.fastods.util.FileOpenResult;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;
import com.github.jferard.fastods.util.ZipUTF8WriterImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import static com.github.jferard.fastods.OdsFactory.FileState.FILE_EXISTS;
import static com.github.jferard.fastods.OdsFactory.FileState.IS_DIRECTORY;
import static com.github.jferard.fastods.OdsFactory.FileState.OK;

/**
 * @author Julien Férard
 */
public class OdsFactory {
	public static OdsFactory create() {
		return OdsFactory.create(Logger.getLogger(OdsDocument.class.getName()), Locale.getDefault());
	}

	public static OdsFactory create(final Logger logger, final Locale locale) {
		final PositionUtil positionUtil = new PositionUtil(new EqualityUtil());
		final WriteUtil writeUtil = WriteUtil.create();
		final XMLUtil xmlUtil = XMLUtil.create();
		final DataStyles format = DataStylesFactory.create(xmlUtil, locale);
		return new OdsFactory(logger, positionUtil, writeUtil, xmlUtil, format);
	}

	private final DataStyles format;
	private final Logger logger;
	private final PositionUtil positionUtil;
	private final WriteUtil writeUtil;
	private final XMLUtil xmlUtil;


	public OdsFactory(final Logger logger, final PositionUtil positionUtil, final WriteUtil writeUtil, final XMLUtil xmlUtil, final DataStyles format) {
		this.logger = logger;
		this.positionUtil = positionUtil;
		this.writeUtil = writeUtil;
		this.xmlUtil = xmlUtil;
		this.format = format;
	}

	/**
	 * @param filename the name of the file.
	 * @return the state of the file with name filename: IS_DIRECTORY|FILE_EXISTS|OK
	 * @deprecated
	 */
	@Deprecated
	public FileState checkFile(final String filename) {
		final File f = new File(filename);
		if (f.isDirectory())
			return IS_DIRECTORY;

		if (f.exists())
			return FILE_EXISTS;

		return OK;
	}

	/**
	 * Create a new, empty document. Use addTable to add tables.
	 *
	 * @return a new document
	 */
	private OdsDocument createDocument() {
		final OdsElements odsElements = OdsElements.create(this.positionUtil, this.xmlUtil,
				this.writeUtil, this.format);
		return new OdsDocument(this.logger,
				odsElements, this.xmlUtil);
	}

	public AnonymousOdsFileWriter createWriter() {
		final OdsDocument document = this.createDocument();
		return new AnonymousOdsFileWriter(this.logger, document);
	}

	/**
	 * Create a new ODS file writer from a document. Be careful: this method opens immediatly a stream.
	 *
	 * @param filename the name of the destination file
	 * @return the ods writer
	 * @throws FileNotFoundException if the file can't be found
	 */
	public OdsFileWriter createWriter(final String filename) throws
			IOException {
		final OdsDocument document = this.createDocument();
		final OdsFileWriter writer = OdsFileDirectWriter.builder(this.logger, document).openResult(this.openFile
				(filename))
				.build();
		document.addObserver(writer);
		document.prepareFlush();
		return writer;
	}

	public OdsFileWriter createWriter(final File file) throws IOException {
		final OdsDocument document = this.createDocument();
		final OdsFileWriter writer =
				OdsFileDirectWriter.builder(this.logger, document).openResult(this.openFile(file)).build();
		document.addObserver(writer);
		document.prepareFlush();
		return writer;
	}

	public OdsFileWriterAdapter createWriterAdapter(final File file) throws IOException {
		final OdsDocument document = this.createDocument();
		final ZipUTF8WriterBuilder zipUTF8Writer = ZipUTF8WriterImpl.builder().noWriterBuffer();
		final OdsFileWriterAdapter writerAdapter = OdsFileWriterAdapter.create(
				OdsFileDirectWriter.builder(this.logger, document).openResult(this.openFile(file)).zipBuilder(
						zipUTF8Writer)
						.build());
		document.addObserver(writerAdapter);
		document.prepareFlush();
		return writerAdapter;
	}

	/**
	 * @param file the file.
	 * @return the result of the operation
	 * @throws FileNotFoundException if the file does not exist
	 */
	public FileOpenResult openFile(final File file) throws FileNotFoundException {
		if (file.isDirectory())
			return FileOpenResult.FILE_IS_DIR;

		if (file.exists())
			return new FileExists(file);

		return new FileOpen(new FileOutputStream(file));
	}

	/**
	 * @param filename the name of the file.
	 * @return the result of the operation
	 * @throws FileNotFoundException if the file does not exist
	 */
	public FileOpenResult openFile(final String filename) throws FileNotFoundException {
		final File f = new File(filename);
		return this.openFile(f);
	}

	public static enum FileState {
		IS_DIRECTORY,
		FILE_EXISTS,
		OK
	}
}
