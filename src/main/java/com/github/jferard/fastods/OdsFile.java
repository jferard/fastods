/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.datastyle.LocaleDataStyles;
import com.github.jferard.fastods.entry.OdsEntries;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;

/**
 * WHERE ? root !
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class OdsFile {
	private static final int DEFAULT_COLUMN_CAPACITY = 32;
	private static final int DEFAULT_ROW_CAPACITY = 1024;

	public static OdsFile create(final Locale locale, final String name) {
		final PositionUtil positionUtil = new PositionUtil();
		final WriteUtil writeUtil = new WriteUtil();
		final XMLUtil xmlUtil = XMLUtil.create();
		final DataStyleBuilderFactory builderFactory = new DataStyleBuilderFactory(
				xmlUtil, locale);
		final LocaleDataStyles format = new LocaleDataStyles(builderFactory,
				xmlUtil);
		final OdsEntries entries = OdsEntries.create(positionUtil, xmlUtil,
				format);
		return new OdsFile(Logger.getLogger(OdsFile.class.getName()), name,
				entries, writeUtil, xmlUtil);
	}

	public static OdsFile create(final String name) {
		return OdsFile.create(Locale.getDefault(), name);
	}

	private final OdsEntries entries;

	private String filename;

	private final Logger logger;
	private final XMLUtil xmlUtil;

	/**
	 * Create a new ODS file.
	 * 
	 * @param logger
	 * @param name
	 *            - The filename for this file, if this file exists it is
	 *            overwritten
	 * @param writeUtil
	 * @param xmlUtil
	 * @param entries2
	 */
	public OdsFile(final Logger logger, final String name,
			final OdsEntries entries, final WriteUtil writeUtil,
			final XMLUtil xmlUtil) {
		this.logger = logger;
		this.newFile(name);
		this.entries = entries;
		this.xmlUtil = xmlUtil;
		// Add four default stylesEntry to contentEntry
		TableStyle.DEFAULT_TABLE_STYLE.addToEntries(this.entries);
		TableRowStyle.DEFAULT_TABLE_ROW_STYLE.addToEntries(this.entries);
		TableColumnStyle.getDefaultColumnStyle(xmlUtil)
				.addToEntries(this.entries);
		TableCellStyle.getDefaultCellStyle().addToEntries(this.entries);
		PageStyle.DEFAULT_PAGE_STYLE.addToEntries(this.entries);
	}

	public void addDataStyle(final DataStyle dataStyle) {
		this.entries.addDataStyle(dataStyle);
	}

	public void addPageStyle(final PageStyle pageStyle) {
		this.entries.addPageStyle(pageStyle);
	}

	/**
	 * Add a new table to the file, the new table is set to the active table.
	 * <br>
	 * Use setActiveTable to override the current active table, this has no
	 * influence to<br>
	 * the program, the active table is the first table that is shown in
	 * OpenOffice.
	 *
	 * @param name
	 *            - The name of the table to add
	 * @return the table
	 * @throws FastOdsException
	 */
	public Table addTable(final String name) throws FastOdsException {
		return this.addTable(name, OdsFile.DEFAULT_ROW_CAPACITY,
				OdsFile.DEFAULT_COLUMN_CAPACITY);
	}

	public Table addTable(final String name, final int rowCapacity,
			final int columnCapacity) {
		final Table table = this.entries.addTableToContent(name, rowCapacity,
				columnCapacity);
		this.entries.setActiveTable(table);
		return table;
	}

	public void addTextStyle(final TextStyle fhTextStyle) {
		this.entries.addTextStyle(fhTextStyle);
	}

	/**
	 * The filename of the spreadsheet file.
	 *
	 * @return The filename of the spreadsheet file
	 */
	public String getName() {
		return this.filename;
	}

	public Table getTable(final int n) throws FastOdsException {
		final List<Table> tableQueue = this.entries.getTables();
		if (n < 0 || n >= tableQueue.size()) {
			throw new FastOdsException("Wrong table number [" + n + "]");
		}

		final Table t = tableQueue.get(n);
		return t;
	}

	/**
	 * @param name
	 *            the name of the table
	 * @return the table, or null if not exists
	 * @throws FastOdsException
	 */
	public Table getTable(final String name) throws FastOdsException {
		final Table table = this.entries.getTable(name);
		if (table == null) {
			throw new FastOdsException("Wrong table name [" + name + "]");
		}
		return table;
	}

	/**
	 * Returns the name of the table.
	 *
	 * @param n
	 *            The number of the table
	 * @return The name of the table
	 */
	public String getTableName(final int n) throws FastOdsException {
		final Table t = this.getTable(n);
		return t.getName();
	}

	/**
	 * Search a table by name and return its number.
	 *
	 * @param name
	 *            The name of the table
	 * @return The number of the table or -1 if name was not found
	 */
	public int getTableNumber(final String name) {
		final ListIterator<Table> iterator = this.entries.getTables()
				.listIterator();
		while (iterator.hasNext()) {
			final int n = iterator.nextIndex();
			final Table tab = iterator.next();
			if (tab.getName().equalsIgnoreCase(name)) {
				return n;
			}
		}

		return -1;
	}

	// ----------------------------------------------------------------------
	// All methods for setCell with OldHeavyTableCell.Type.STRING
	// ----------------------------------------------------------------------

	/**
	 * @return the list of tables
	 */
	public List<Table> getTables() {
		return this.entries.getTables();
	}

	/**
	 * Create a new,empty file, use addTable to add tables.
	 *
	 * @param name
	 *            - The filename of the new spreadsheet file, if this file
	 *            exists it is overwritten
	 * @return False, if filename is a directory
	 */
	public final boolean newFile(final String name) {
		final File f = new File(name);
		// Check if name is a directory and abort if YES
		if (f.isDirectory()) {
			return false;
		}
		this.filename = name;
		return true;
	}

	/**
	 * Save the new file.
	 *
	 * @return true if the file was saved and false if an exception happened
	 */
	public boolean save() {
		try {
			return this.save(new FileOutputStream(this.filename));
		} catch (final FileNotFoundException e) {
			this.logger.log(Level.SEVERE, "Can't open " + this.filename, e);
			return false;
		}
	}

	/**
	 * Save the new file.
	 *
	 * @param out
	 *            The OutputStream that should be used.
	 * @return true - the file was saved<br>
	 *         false - an exception happened
	 */
	public boolean save(final OutputStream out) {
		final ZipUTF8WriterBuilder builder = ZipUTF8Writer.builder();
		return this.save(builder.build(out));
	}

	public boolean save(final ZipUTF8Writer writer) {
		this.entries.setTables();

		try {
			this.entries.writeEntries(this.xmlUtil, writer);
			this.entries.createEmptyEntries(writer);
		} catch (final IOException e) {
			this.logger.log(Level.SEVERE, "Can't write data", e);
			return false;
		} finally {
			try {
				writer.close();
			} catch (final IOException e) {
				this.logger.log(Level.SEVERE, "Can't close file", e);
				return false;
			}
		}
		this.logger.log(Level.FINE, "file saved");

		return true;
	}

	/**
	 * @param builder
	 *            a builder for the ZipOutputStream and the Writer (buffers,
	 *            level, ...)
	 * @return true if the file was saved and false if an exception happened
	 */
	public boolean save(final ZipUTF8WriterBuilder builder) {
		try {
			return this
					.save(builder.build(new FileOutputStream(this.filename)));
		} catch (final FileNotFoundException e) {
			this.logger.log(Level.SEVERE, "Can't open " + this.filename, e);
			return false;
		}
	}

	/**
	 * Set the active table, this is the table that is shown if you open the
	 * file.
	 *
	 * @param tableIndex
	 *            The table number, this table should already exist, otherwise
	 *            the first table is shown
	 * @return true - The active table was set, false - tab has an illegal value
	 */
	public boolean setActiveTable(final int tableIndex) {
		if (tableIndex < 0 || tableIndex >= this.entries.getTableCount())
			return false;

		final Table table = this.entries.getTable(tableIndex);
		this.entries.setActiveTable(table);
		return true;
	}

	/**
	 * Gets the number of the last table.
	 *
	 * @return The number of the last table
	 */
	public int tableCount() {
		return this.entries.getTableCount();
	}
}
