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
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class OdsDocument {
	static final int DEFAULT_COLUMN_CAPACITY = 32;
	static final int DEFAULT_ROW_CAPACITY = 1024;
	private final Logger logger;
	private final OdsElements odsElements;
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

	public void addChildCellStyle(final TableCell.Type type) {
		this.odsElements.addChildCellStyle(TableCellStyle.getDefaultCellStyle(), type);
	}

	public void addChildCellStyle(final TableCellStyle style, final TableCell.Type type) {
		this.odsElements.addChildCellStyle(style, type);
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

	public void addObserver(final OdsFileWriter writer) {
		this.odsElements.addObserver(writer);
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

	public void addPageStyle(final PageStyle ps) {
		this.odsElements.addPageStyle(ps);
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

	public void addStyleToContentAutomaticStyles(final TextStyle textStyle) {
		this.odsElements.addStyleToContentAutomaticStyles(textStyle);
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
	public Table addTable(final String name) throws IOException {
		return this.addTable(name, OdsDocument.DEFAULT_ROW_CAPACITY,
				OdsDocument.DEFAULT_COLUMN_CAPACITY);
	}

	public Table addTable(final String name, final int rowCapacity,
						  final int columnCapacity) throws IOException {
		final Table table = this.odsElements.addTableToContent(name, rowCapacity,
				columnCapacity);
		this.odsElements.setActiveTable(table);
		return table;
	}

	public void debugStyles() {
		this.odsElements.debugStyles();
	}

	public void freezeStyles() {
		this.odsElements.freezeStyles();
	}

	public Logger getLogger() {
		return this.logger;
	}

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
	 * @param name the name of the table
	 * @return the table
	 * @throws IOException if the table does not exist.
	 */
	public Table getOrAddTable(final String name) throws IOException {
		Table table = this.odsElements.getTable(name);
		if (table == null) {
			table = this.addTable(name);
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

	public void prepareFlush() throws IOException {
		this.odsElements.prepare();
	}

	public void save() throws IOException {
		this.odsElements.save();
	}

	/**
	 * Saves a file
	 *
	 * @param writer where to write
	 * @throws IOException
	 */
	public void save(final ZipUTF8Writer writer) throws IOException {
		try {
			this.odsElements.createEmptyElements(writer);
			this.odsElements.writeImmutableElements(this.xmlUtil, writer);
			this.odsElements.writeMeta(this.xmlUtil, writer);
			this.odsElements.writeStyles(this.xmlUtil, writer);
			this.odsElements.writeContent(this.xmlUtil, writer);
			this.odsElements.writeSettings(this.xmlUtil, writer);
		} finally {
			writer.close();
		}
		this.logger.log(Level.FINE, "file saved");
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

	public void setViewSetting(final String viewId, final String item, final String value) {
		this.odsElements.setViewSettings(viewId, item, value);
	}

	/**
	 * Gets the number of the last table.
	 *
	 * @return The number of the last table
	 */
	public int tableCount() {
		return this.odsElements.getTableCount();
	}

	public void addAutofilter(final Table table, final int r1, final int c1, final int r2, final int c2) {
		this.odsElements.addAutofilter(table, r1, c1, r2, c2);
	}
}
