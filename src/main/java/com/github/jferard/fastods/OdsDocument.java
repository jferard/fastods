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
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.PageLayoutStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class OdsDocument {
	private static final int DEFAULT_COLUMN_CAPACITY = 32;
	private static final int DEFAULT_ROW_CAPACITY = 1024;

	private final OdsElements odsElements;
	private final Logger logger;
	private final XMLUtil xmlUtil;

	/**
	 * Create a new ODS file.
	 *
	 * @param logger      the logger
	 * @param odsElements the ods elements (file entries in zip archive)
	 * @param xmlUtil     a util for XML writing
	 */
	OdsDocument(final Logger logger,
				final OdsElements odsElements, final XMLUtil xmlUtil) {
		this.logger = logger;
		this.odsElements = odsElements;
		this.xmlUtil = xmlUtil;
		// Add four default stylesEntry to contentEntry
		TableStyle.DEFAULT_TABLE_STYLE.addToElements(this.odsElements);
		TableRowStyle.DEFAULT_TABLE_ROW_STYLE.addToElements(this.odsElements);
		TableColumnStyle.getDefaultColumnStyle(xmlUtil)
				.addToElements(this.odsElements);
		TableCellStyle.getDefaultCellStyle().addToElements(this.odsElements);
		PageStyle.DEFAULT_PAGE_STYLE.addToElements(this.odsElements);
	}

	/**
	 * Add a data style to this document. Use only if you want to flush data before the end of the document
	 * construction.
	 *
	 * @param dataStyle the data style to add to this document
	 */
	public void addDataStyle(final DataStyle dataStyle) {
		this.odsElements.addDataStyle(dataStyle);
	}

	/**
	 * Add a master page style to this document. Use only if you want to flush data before the end of the document
	 * construction.
	 *
	 * @param masterPageStyle the master page style to add to this document
	 */
	public void addMasterPageStyle(final MasterPageStyle masterPageStyle) {
		this.odsElements.addMasterPageStyle(masterPageStyle);
	}

	/**
	 * Add a page layout style to this document. Use only if you want to flush data before the end of the document
	 * construction.
	 *
	 * @param pageLayoutStyle the page layout to add to this document
	 */
	public void addPageLayoutStyle(final PageLayoutStyle pageLayoutStyle) {
		this.odsElements.addPageLayoutStyle(pageLayoutStyle);
	}

	/**
	 * Add a style tag to this document. Use only if you want to flush data before the end of the document
	 * construction.
	 *
	 * @param styleTag the style tag to add to this document
	 */
	public void addStyleTag(final StyleTag styleTag) {
		this.odsElements.addStyleTag(styleTag);
	}

	/**
	 * Add a new table to the file, the new table is set to the active table.<br>
	 * Use setActiveTable to override the current active table, this has no
	 * influence to the program, the active table is the first table that is shown in
	 * OpenOffice.
	 *
	 * @param name - The name of the table to add
	 * @return the table
	 */
	public Table addTable(final String name) {
		return this.addTable(name, OdsDocument.DEFAULT_ROW_CAPACITY,
				OdsDocument.DEFAULT_COLUMN_CAPACITY);
	}

	public Table addTable(final String name, final int rowCapacity,
						  final int columnCapacity) {
		final Table table = this.odsElements.addTableToContent(name, rowCapacity,
				columnCapacity);
		this.odsElements.setActiveTable(table);
		return table;
	}

	/*
	public void addTextStyle(final TextStyle fhTextStyle) {
		this.odsElements.addTextStyle(fhTextStyle);
	}
	*/

	public Table getTable(final int n) throws FastOdsException {
		final List<Table> tableQueue = this.odsElements.getTables();
		if (n < 0 || n >= tableQueue.size()) {
			throw new FastOdsException("Wrong table number [" + n + "]");
		}

		final Table t = tableQueue.get(n);
		return t;
	}

	/**
	 * @param name the name of the table
	 * @return the table
	 * @throws FastOdsException if the table does not exist.
	 */
	public Table getTable(final String name) throws FastOdsException {
		final Table table = this.odsElements.getTable(name);
		if (table == null) {
			throw new FastOdsException("Wrong table name [" + name + "]");
		}
		return table;
	}

	/**
	 * Returns the name of the table.
	 *
	 * @param n The number of the table
	 * @return The name of the table
	 * @throws FastOdsException if n is negative
	 */
	public String getTableName(final int n) throws FastOdsException {
		final Table t = this.getTable(n);
		return t.getName();
	}

	/**
	 * Search a table by name and return its number.
	 *
	 * @param name The name of the table
	 * @return The number of the table or -1 if name was not found
	 */
	public int getTableNumber(final String name) {
		final ListIterator<Table> iterator = this.odsElements.getTables()
				.listIterator();
		while (iterator.hasNext()) {
			final int n = iterator.nextIndex();
			final Table tab = iterator.next();
			if (tab.getName().equals(name)) {
				return n;
			}
		}

		return -1;
	}

	/**
	 * @return the list of tables
	 */
	public List<Table> getTables() {
		return this.odsElements.getTables();
	}

	/**
	 * Save the new file.
	 *
	 * @param filename the name of the destination file
	 * @throws IOException If an I/O error occurs
	 */
	public void saveAs(final String filename) throws IOException {
		try {
			final FileOutputStream out = new FileOutputStream(filename);
			this.save(out);
		} catch (final FileNotFoundException e) {
			this.logger.log(Level.SEVERE, "Can't open " + filename, e);
			throw new IOException(e);
		} catch (final NullPointerException e) {
			this.logger.log(Level.SEVERE, "No file", e);
			throw new IOException(e);
		}
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
		this.odsElements.setTables();

		try {
			this.odsElements.writeElements(this.xmlUtil, writer);
			this.odsElements.createEmptyElements(writer);
		} finally {
			writer.close();
		}
		this.logger.log(Level.FINE, "file saved");
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

	/**
	 * Set the active table, this is the table that is shown if you open the
	 * file.
	 *
	 * @param tableIndex The table number, this table should already exist, otherwise
	 *                   the first table is shown
	 * @return true - The active table was set, false - tab has an illegal value
	 */
	public boolean setActiveTable(final int tableIndex) {
		if (tableIndex < 0 || tableIndex >= this.odsElements.getTableCount())
			return false;

		final Table table = this.odsElements.getTable(tableIndex);
		this.odsElements.setActiveTable(table);
		return true;
	}

	/**
	 * Gets the number of the last table.
	 *
	 * @return The number of the last table
	 */
	public int tableCount() {
		return this.odsElements.getTableCount();
	}

	public Logger getLogger() {
		return this.logger;
	}

	public void setViewSetting(final String viewId, final String item, final String value) {
		this.odsElements.setViewSettings(viewId, item, value);
	}

	public void addPageStyle(final PageStyle ps) {
		this.odsElements.addPageStyle(ps);
	}
}
