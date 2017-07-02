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
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntrySet;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.*;
import com.github.jferard.fastods.util.PositionUtil.Position;

import java.io.IOException;
import java.util.ArrayList;

/**
 * OpenDocument 9.1.2 table:table
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class TableBuilder {

	private static void checkCol(final int col) throws FastOdsException {
		if (col < 0) {
			throw new FastOdsException(new StringBuilder(
					"Negative column number exception, column value:[")
					.append(col).append("]").toString());
		}
	}

	private static void checkRow(final int row) throws FastOdsException {
		if (row < 0) {
			throw new FastOdsException(new StringBuilder(
					"Negative row number exception, row value:[").append(row)
					.append("]").toString());
		}
	}

	public static TableBuilder create(final PositionUtil positionUtil, final WriteUtil writeUtil, final XMLUtil xmlUtil, final StylesContainer stylesContainer, final DataStyles format, final String name, final int rowCapacity, final int columnCapacity) {
		final ConfigItemMapEntrySet configEntry = ConfigItemMapEntrySet.createSet(name);
		configEntry.add(new ConfigItem("CursorPositionX", "int", "0"));
		configEntry.add(new ConfigItem("CursorPositionY", "int", "0"));
		configEntry.add(new ConfigItem("HorizontalSplitMode", "short", "0"));
		configEntry.add(new ConfigItem("VerticalSplitMode", "short", "0"));
		configEntry.add(new ConfigItem("HorizontalSplitPosition", "int", "0"));
		configEntry.add(new ConfigItem("VerticalSplitPosition", "int", "0"));
		configEntry.add(new ConfigItem("ActiveSplitRange", "short", "2"));
		configEntry.add(new ConfigItem("PositionLeft", "int", "0"));
		configEntry.add(new ConfigItem("PositionRight", "int", "0"));
		configEntry.add(new ConfigItem("PositionTop", "int", "0"));
		configEntry.add(new ConfigItem("PositionBottom", "int", "0"));
		configEntry.add(new ConfigItem("ZoomType", "short", "0"));
		configEntry.add(new ConfigItem("ZoomValue", "int", "100"));
		configEntry.add(new ConfigItem("PageViewZoomValue", "int", "60"));

		return new TableBuilder(positionUtil, writeUtil, xmlUtil, stylesContainer, format, name, rowCapacity, columnCapacity, configEntry, 102);
	}


	private OdsFileWriter observer;
	private final int bufferSize;
	private final int columnCapacity;
	private final FullList<TableColumnStyle> columnStyles;
	private final ConfigItemMapEntrySet configEntry;
	private final DataStyles format;
	private final PositionUtil positionUtil;
	private final StylesContainer stylesContainer;
	private final FullList<TableRow> tableRows;
	private final WriteUtil writeUtil;
	private final XMLUtil xmlUtil;

	private int curRowIndex;
	private int lastFlushedRowIndex;
	private int lastRowIndex;
	private String name;
	private TableStyle style;

	TableBuilder(final PositionUtil positionUtil, final WriteUtil writeUtil,
				 final XMLUtil xmlUtil, final StylesContainer stylesContainer,
				 final DataStyles format, final String name, final int rowCapacity,
				 final int columnCapacity, final ConfigItemMapEntrySet configEntry, final int bufferSize) {
		this.xmlUtil = xmlUtil;
		this.writeUtil = writeUtil;
		this.positionUtil = positionUtil;
		this.stylesContainer = stylesContainer;
		this.format = format;
		this.name = name;
		this.columnCapacity = columnCapacity;
		this.configEntry = configEntry;
		this.style = TableStyle.DEFAULT_TABLE_STYLE;

		this.columnStyles = FullList.<TableColumnStyle>builder()
				.blankElement(TableColumnStyle.DEFAULT_TABLE_COLUMN_STYLE)
				.capacity(this.columnCapacity).build();
		this.tableRows = FullList.newListWithCapacity(rowCapacity);
		this.curRowIndex = -1;
		this.lastFlushedRowIndex = 0;
		this.lastRowIndex = -1;
		this.bufferSize = bufferSize;
	}

	/**
	 * Add an observer to this table
	 * @param observer the observer
	 */
	public void addObserver(final OdsFileWriter observer) {
		this.observer = observer;
	}

	public void flushBeginTable(final TableAppender appender) throws IOException {
		this.observer.update(new BeginTableFlusher(appender));
	}

	public void flushEndTable(final TableAppender appender) throws IOException {
		this.observer.update(new EndTableFlusher(appender, this.tableRows.subList(this.lastFlushedRowIndex, this
				.tableRows.usedSize())));
	}

	public FullList<TableColumnStyle> getColumnStyles() {
		return this.columnStyles;
	}

	public ConfigItemMapEntry getConfigEntry() {
		return this.configEntry;
	}

	public int getLastRowNumber() {
		return this.tableRows.usedSize() - 1;
	}

	public TableRow getRow(final Table table, final TableAppender appender, final int rowIndex) throws FastOdsException, IOException {
		TableBuilder.checkRow(rowIndex);
		return this.getRowSecure(table, appender, rowIndex, true);
	}

	public TableRow getRow(final Table table, final TableAppender appender, final String pos) throws FastOdsException, IOException {
		final int row = this.positionUtil.getPosition(pos).getRow();
		return this.getRow(table, appender,row);
	}

	private TableRow getRowSecure(final Table table, final TableAppender appender,
								  final int rowIndex, final boolean updateRowIndex) throws IOException {
		TableRow tr = this.tableRows.get(rowIndex);
		if (tr == null) {
			tr = new TableRow(this.writeUtil, this.xmlUtil,
					this.stylesContainer, this.format, table, rowIndex,
					this.columnCapacity);
			this.tableRows.set(rowIndex, tr);
			if (rowIndex > this.lastRowIndex)
				this.lastRowIndex = rowIndex;

			this.notifyIfHasObserver(appender, rowIndex);
		}
		if (updateRowIndex)
			this.curRowIndex = rowIndex;
		return tr;
	}

	private void notifyIfHasObserver(final TableAppender appender, final int rowIndex) throws IOException {
		if (this.observer != null) {
            if (rowIndex == 0) {
                this.observer.update(new BeginTableFlusher(appender));
            } else if (rowIndex % this.bufferSize == 0) {
                this.observer.update(this.createPreprocessedRowsFlusher(rowIndex)); // (0..1023), (1024..2047)
                this.lastFlushedRowIndex = rowIndex;
            }
        }
	}

	private PreprocessedRowsFlusher createPreprocessedRowsFlusher(final int toRowIndex) throws IOException {
		return PreprocessedRowsFlusher.create(this.xmlUtil,
            new ArrayList<TableRow>(
                    this.tableRows.subList(
                            this.lastFlushedRowIndex, toRowIndex
                    )
            ));
	}

	/**
	 * Get the current TableFamilyStyle
	 *
	 * @return The current TableStlye
	 */
	public String getStyleName() {
		return this.style.getName();
	}

	public TableRow nextRow(final Table table, final TableAppender appender) throws IOException {
		return this.getRowSecure(table, appender, this.curRowIndex + 1, true);
	}

	public void setCellMerge(final Table table, final TableAppender appender, final int rowIndex, final int colIndex, final int rowMerge, final int columnMerge) throws FastOdsException, IOException {
		final TableRow row = this.getRowSecure(table, appender, rowIndex, true);
		if (row.isCovered(colIndex)) // already spanned
			return;

		row.setColumnsSpanned(colIndex, columnMerge);
		this.spanColumnsFromRowsBelow(table, appender, rowIndex, colIndex, rowMerge, columnMerge);
	}

	private void spanColumnsFromRowsBelow(final Table table, final TableAppender appender, final int rowIndex, final int colIndex, final int rowMerge, final int columnMerge) throws IOException {
		for (int r = rowIndex + 1; r < rowIndex + rowMerge; r++) {
			final TableRow row = this.getRowSecure(table, appender, r, false);
			row.setColumnsSpanned(colIndex, columnMerge);
		}
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
	public void setCellMerge(final Table table, final TableAppender appender, final String pos, final int rowMerge,
							 final int columnMerge) throws FastOdsException, IOException {
		final Position position = this.positionUtil.getPosition(pos);
		this.setCellMerge(table, appender, position.getRow(), position.getColumn(), rowMerge, columnMerge);
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
		TableBuilder.checkCol(col);
		this.stylesContainer.addStyleToContentAutomaticStyles(ts);
		this.columnStyles.set(col, ts);
	}

	public void setConfigItem(final String name, final String type,
							  final String value) {
		this.configEntry.add(new ConfigItem("PageViewZoomValue", "int", "60"));
	}

	/**
	 * Set the name of this table.
	 *
	 * @param name The name of this table.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public void setSettings(final String viewId, final String item, final String value) {
		this.configEntry.set(item, value);
	}

	/**
	 * Set a new TableFamilyStyle
	 *
	 * @param style The new TableStyle to be used
	 */
	public void setStyle(final TableStyle style) {
		this.stylesContainer.addPageStyle(style.getPageStyle());
		this.stylesContainer.addStyleToContentAutomaticStyles(style);
		this.style = style;
	}

    public void setRowsSpanned(final Table table, final TableAppender appender, final int rowIndex, final int colIndex, final int n) throws IOException {
		if (n <= 1)
			return;

		final TableCell firstCell = this.getRowSecure(table, appender, rowIndex, false).getOrCreateCell(colIndex);
		if (firstCell.isCovered())
			return;

		firstCell.markRowsSpanned(n);
		this.coverCellsBelow(table, appender, rowIndex, colIndex, n);
	}

	private void coverCellsBelow(final Table table, final TableAppender appender, final int rowIndex, final int colIndex, final int n) throws IOException {
		for (int r = rowIndex + 1; r < rowIndex + n; r++) {
			final TableRow row = this
					.getRowSecure(table, appender, r, false);
			final TableCell cell = row.getOrCreateCell(colIndex);
			cell.setCovered();
		}
	}

	public String getName() {
		return this.name;
	}

	public int getTableRowsUsedSize() {
		return this.tableRows.usedSize();
	}

	public TableRow getTableRow(final int r) {
		return this.tableRows.get(r);
	}

}
