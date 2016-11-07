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
import java.io.Writer;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.zip.Deflater;
import java.util.zip.ZipOutputStream;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.datastyle.LocaleDataStyles;
import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.Util.Position;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * WHERE ? root !
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
/**
 * @author Julien
 *
 */
public class OdsFile {
	/**
	 * 512 k of buffer before sending data to OutputStreamWriter.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 512 * 1024;
	private static final int DEFAULT_COLUMN_CAPACITY = 32;
	private static final int DEFAULT_ROW_CAPACITY = 1024;

	public static OdsFile create(final Locale locale, final String name) {
		final XMLUtil xmlUtil = XMLUtil.create();
		final Util util = new Util();
		final DataStyleBuilderFactory builderFactory = new DataStyleBuilderFactory(
				xmlUtil, locale);
		final LocaleDataStyles format = new LocaleDataStyles(builderFactory,
				xmlUtil);
		OdsEntries entries = OdsEntries.create(util, xmlUtil, format);
		return new OdsFile(name, entries, util, xmlUtil,
				OdsFile.DEFAULT_BUFFER_SIZE);
	}

	public static OdsFile create(final String name) {
		return OdsFile.create(Locale.getDefault(), name);
	}

	private final int bufferSize;
	private String filename;

	private final Util util;

	private final XMLUtil xmlUtil;
	private OdsEntries entries;

	/**
	 * Create a new ODS file.
	 *
	 * @param name
	 *            - The filename for this file, if this file exists it is
	 *            overwritten
	 * @param entries2
	 * @param util
	 * @param xmlUtil
	 */
	public OdsFile(final String name, OdsEntries entries, final Util util,
			final XMLUtil xmlUtil, final int bufferSize) {
		this.newFile(name);
		this.entries = entries;
		this.util = util;
		this.xmlUtil = xmlUtil;
		this.bufferSize = bufferSize;

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

	public void addTextStyle(final FHTextStyle fhTextStyle) {
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
	 * @return true - the file was saved<br>
	 *         false - an exception happened
	 */
	public boolean save() {

		try {
			return this.save(new FileOutputStream(this.filename));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Save the new file.
	 *
	 * @param output
	 *            The OutputStream that should be used.
	 * @return true - the file was saved<br>
	 *         false - an exception happened
	 */
	public boolean save(final OutputStream output) {
		this.entries.setTables();
		final ZipOutputStream zipOut = new ZipOutputStream(output);
		zipOut.setLevel(Deflater.BEST_SPEED);
		return this.save(zipOut);
	}

	protected boolean save(final ZipOutputStream zipOut) {
		final Writer writer = this.util.wrapStream(zipOut, this.bufferSize);

		try {
			this.entries.writeEntries(this.xmlUtil, zipOut, writer);
			this.entries.createEmptyEntries(zipOut);
		} catch (final IOException e) {
			// should catch exceptions into a logger
			return false;
		} finally {
			try {
				zipOut.close();
			} catch (final IOException e) {
				// should catch exceptions into a logger
				return false;
			}
		}

		return true;
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
	 * Sets the cell value in all tables to the date from the Calendar object.
	 *
	 * @param rowIndex
	 *            The row, 0 is the first row
	 * @param col
	 *            The column, 0 is the first column
	 * @param cal
	 *            The calendar object with the date
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	@Deprecated
	public void setCellInAllTables(final int rowIndex, final int colIndex,
			final Calendar cal, final TableCellStyle ts)
			throws FastOdsException {

		for (final Table table : this.entries.getTables()) {
			final HeavyTableRow row = table.getRow(rowIndex);
			final TableCellWalker walker = row.getWalker();
			walker.to(colIndex);
			walker.setDateValue(cal);
			walker.setStyle(ts);
		}

	}

	/**
	 * Sets the cell value in all tables to the date from the Calendar object.
	 *
	 * @param pos
	 *            The cell position e.g. 'A1'
	 * @param cal
	 *            The calendar object with the date
	 * @param ts
	 *            The table style for this cells, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	@Deprecated
	public void setCellInAllTables(final String pos, final Calendar cal,
			final TableCellStyle ts) throws FastOdsException {
		final Position position = this.util.getPosition(pos);
		final int row = position.getRow();
		final int col = position.getColumn();
		this.setCellInAllTables(row, col, cal, ts);
	}

	/**
	 * Sets the cell value in all tables to the given values.
	 *
	 * @param pos
	 *            The cell position e.g. 'A1'
	 * @param valuetype
	 *            The value type of value,
	 *            OldHeavyTableCell.Type.STRING,OldHeavyTableCell.Type.FLOAT or
	 *            OldHeavyTableCell.Type.PERCENTAGE.
	 * @param value
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	@Deprecated
	public void setCellInAllTables(final String pos,
			final TableCell.Type valuetype, final String value,
			final TableCellStyle ts) throws FastOdsException {
		final Position position = this.util.getPosition(pos);
		final int row = position.getRow();
		final int col = position.getColumn();
		// this.setCellInAllTables(row, col, valuetype, value,
		// ts);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param rowIndex
	 *            The row, 0 is the first row
	 * @param colIndex
	 *            The column, 0 is the first column
	 * @param rowMerge
	 * @param columnMerge
	 * @throws FastOdsException
	 */
	@Deprecated
	public void setCellMergeInAllTables(final int rowIndex, final int colIndex,
			final int rowMerge, final int columnMerge) throws FastOdsException {
		for (final Table table : this.entries.getTables()) {
			final HeavyTableRow row = table.getRow(rowIndex);
			final TableCellWalker walker = row.getWalker();
			walker.to(colIndex);
			walker.setRowsSpanned(rowMerge);
			walker.setColumnsSpanned(columnMerge);
		}
	}

	/**
	 * Set the merging of multiple cells to one cell in all existing tables.
	 *
	 * @param pos
	 *            The cell position e.g. 'A1'
	 * @param rowMerge
	 * @param columnMerge
	 * @throws FastOdsException
	 */
	@Deprecated
	public void setCellMergeInAllTables(final String pos, final int rowMerge,
			final int columnMerge) throws FastOdsException {
		final Position position = this.util.getPosition(pos);
		final int row = position.getRow();
		final int col = position.getColumn();
		this.setCellMergeInAllTables(row, col, rowMerge, columnMerge);
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
