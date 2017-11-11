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
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.NamedObject;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * OpenDocument 9.1.2 table:table
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class Table implements NamedObject {
	private final TableBuilder builder;
	private final TableAppender appender;

	private String name;

	public static Table create(final PositionUtil positionUtil, final WriteUtil writeUtil, final XMLUtil xmlUtil,
							   final StylesContainer stylesContainer, final DataStyles format,
							   final String name, final int rowCapacity, final int columnCapacity) {
		final TableBuilder builder = TableBuilder.create(positionUtil, writeUtil,
				xmlUtil, stylesContainer, format, name, rowCapacity, columnCapacity);
		return new Table(name, builder);
	}


	public Table(final String name, final TableBuilder builder) {
		this.name = name;
		this.builder = builder;
		this.appender = new TableAppender(builder);
	}


	public void addData(final DataWrapper data) throws IOException {
		data.addToTable(this);
	}

	/**
	 * Add an observer to this table
	 * @param observer the observer
	 */
	public void addObserver(final OdsFileWriter observer) {
		this.builder.addObserver(observer);
	}

	public void appendXMLToContentEntry(final XMLUtil util,
										final Appendable appendable) throws IOException {
		this.appender.appendXMLToContentEntry(util, appendable);
	}

	public void flush() throws IOException {
		this.builder.flushBeginTable(this.appender);
		this.builder.flushEndTable(this.appender);
	}

	/**
	 * Open the table, flush all rows from start, but do not freeze the table
	 *
	 * @param util       a XMLUtil instance for writing XML
	 * @param appendable where to write
	 * @throws IOException if an I/O error occurs during the flush
	 */
	public void flushAllAvailableRows(final XMLUtil util, final Appendable appendable) throws IOException {
		this.appender.flushAllAvailableRows(util, appendable);
	}

	/**
	 * Flush all rows from a given position, and do freeze the table
	 *
	 * @param util       a XMLUtil instance for writing XML
	 * @param appendable where to write
	 * @param rowIndex   the first index to use.
	 * @throws IOException if an I/O error occurs during the flush
	 */
	public void flushRemainingRowsFrom(final XMLUtil util, final Appendable appendable, final int rowIndex)
			throws IOException {
		this.appender.flushRemainingRowsFrom(util, appendable, rowIndex);
	}

	/**
	 * Flush all rows from a given position, but do not freeze the table
	 *
	 * @param util       a XMLUtil instance for writing XML
	 * @param appendable where to write
	 * @param rowIndex the index of the row
	 * @throws IOException if an I/O error occurs during the flush
	 */
	public void flushSomeAvailableRowsFrom(final XMLUtil util, final Appendable appendable, final int rowIndex)
			throws IOException {
		this.appender.flushSomeAvailableRowsFrom(util, appendable, rowIndex);
	}

	public ConfigItemMapEntry getConfigEntry() {
		return this.builder.getConfigEntry();
	}

	public int getLastRowNumber() {
		return this.builder.getLastRowNumber();
	}

	/**
	 * Get the name of this table.
	 *
	 * @return The name of this table.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	public TableRow getRow(final int rowIndex) throws FastOdsException, IOException {
		return this.builder.getRow(this, this.appender, rowIndex);
	}

	public TableRow getRow(final String pos) throws FastOdsException, IOException {
		return this.builder.getRow(this, this.appender, pos);
	}

	/**
	 * Get the current TableFamilyStyle
	 *
	 * @return The current TableStlye
	 */
	public String getStyleName() {
		return this.builder.getStyleName();
	}

	public TableRow nextRow() throws IOException {
		return this.builder.nextRow(this, this.appender);
	}

	public void setCellMerge(final int rowIndex, final int colIndex, final int rowMerge, final int columnMerge) throws IOException {
		this.builder.setCellMerge(this, this.appender, rowIndex, colIndex, rowMerge, columnMerge);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param pos         The cell position e.g. 'A1'
	 * @param rowMerge    the number of rows to merge
	 * @param columnMerge the number of cells to merge
	 * @throws FastOdsException if the row index or the col index is negative
	 * @throws IOException if the cells can't be merged
	 */
	@Deprecated
	public void setCellMerge(final String pos, final int rowMerge,
							 final int columnMerge) throws FastOdsException, IOException {
		this.builder.setCellMerge(this, this.appender, pos, rowMerge, columnMerge);
	}

	/**
	 * Set the style of a column.
	 *
	 * @param col The column number
	 * @param ts  The style to be used, make sure the style is of type
	 *            TableFamilyStyle.STYLEFAMILY_TABLECOLUMN
	 * @throws FastOdsException Thrown if col has an invalid value.
	 */
	public void setColumnStyle(final int col, final TableColumnStyle ts)
			throws FastOdsException {
		if (this.appender.isPreambleWritten())
			throw new IllegalStateException();

		this.builder.setColumnStyle(col, ts);
	}

	public void setConfigItem(final String name, final String type,
							  final String value) {
		this.builder.setConfigItem(name, type, value);
	}

	/**
	 * Set the name of this table.
	 *
	 * @param name The name of this table.
	 */
	public void setName(final String name) {
		if (this.appender.isPreambleWritten())
			throw new IllegalStateException();

		this.name = name;
	}

	public void setSettings(final String viewId, final String item, final String value) {
		this.builder.setSettings(viewId, item, value);
	}

	/**
	 * Set a new TableFamilyStyle
	 *
	 * @param style The new TableStyle to be used
	 */
	public void setStyle(final TableStyle style) {
		this.builder.setStyle(style);
	}

    public void setRowsSpanned(final int rowIndex, final int colIndex, final int n) throws IOException {
		this.builder.setRowsSpanned(this, this.appender, rowIndex, colIndex, n);
	}
}