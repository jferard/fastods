/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntrySet;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.FastFullList;
import com.github.jferard.fastods.util.Position;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * OpenDocument 9.1.2 table:table
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class TableBuilder {
    /**
     * The size of the buffer
     */
    public static final int BUFFER_SIZE = 8 * 1024;

    /**
     * Check if a col index is valid, otherwise throws an exception
     *
     * @param col the index
     * @throws IllegalArgumentException if the index is invalid
     */
    private static void checkCol(final int col) {
        if (col < 0) {
            throw new IllegalArgumentException(
                    "Negative column number exception, column value:[" + col + "]");
        }
    }

    /**
     * Check if a row index is valid, otherwise throws an exception
     *
     * @param row the index
     * @throws IllegalArgumentException if the index is invalid
     */
    private static void checkRow(final int row) {
        if (row < 0) {
            throw new IllegalArgumentException(
                    "Negative row number exception, row value:[" + row + "]");
        }
    }

    /**
     * Create a new table builder
     *
     * @param positionUtil    an util
     * @param writeUtil       an util
     * @param xmlUtil         an util
     * @param stylesContainer the container
     * @param format          the available data styles
     * @param name            the name of the table
     * @param rowCapacity     the row capacity of the table
     * @param columnCapacity  the column capacity of the table
     * @return the builder
     */
    public static TableBuilder create(final PositionUtil positionUtil, final WriteUtil writeUtil,
                                      final XMLUtil xmlUtil, final StylesContainer stylesContainer,
                                      final DataStyles format, final boolean libreOfficeMode,
                                      final String name, final int rowCapacity,
                                      final int columnCapacity) {
        final ConfigItemMapEntrySet configEntry = ConfigItemMapEntrySet.createSet(name);
        configEntry.add(new ConfigItem("CursorPositionX", "int", "0"));
        configEntry.add(new ConfigItem("CursorPositionY", "int", "0"));
        configEntry
                .add(new ConfigItem("HorizontalSplitMode", "short", OdsElements.SC_SPLIT_NORMAL));
        configEntry.add(new ConfigItem("VerticalSplitMode", "short", OdsElements.SC_SPLIT_NORMAL));
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

        return new TableBuilder(positionUtil, writeUtil, xmlUtil, stylesContainer, format,
                libreOfficeMode, name, rowCapacity, columnCapacity, configEntry, BUFFER_SIZE);
    }

    private final int bufferSize;
    private final int columnCapacity;
    private final FastFullList<TableColumnStyle> columnStyles;
    private final ConfigItemMapEntrySet configEntry;
    private final DataStyles format;
    private final PositionUtil positionUtil;
    private final StylesContainer stylesContainer;
    private final FastFullList<TableRow> tableRows;
    private final WriteUtil writeUtil;
    private final XMLUtil xmlUtil;
    private final boolean libreOfficeMode;
    private NamedOdsFileWriter observer;
    private int curRowIndex;
    private int lastFlushedRowIndex;
    private int lastRowIndex;
    private String name;
    private TableStyle style;

    /**
     * Create a new table builder
     *
     * @param positionUtil    an util
     * @param writeUtil       an util
     * @param xmlUtil         an util
     * @param stylesContainer the container
     * @param format          the available data styles
     * @param libreOfficeMode
     * @param name            the name of the table
     * @param rowCapacity     the row capacity of the table
     * @param columnCapacity  the column capacity of the table
     * @param configEntry     the config
     * @param bufferSize      the buffer size
     */
    TableBuilder(final PositionUtil positionUtil, final WriteUtil writeUtil, final XMLUtil xmlUtil,
                 final StylesContainer stylesContainer, final DataStyles format,
                 final boolean libreOfficeMode, final String name, final int rowCapacity,
                 final int columnCapacity, final ConfigItemMapEntrySet configEntry,
                 final int bufferSize) {
        this.xmlUtil = xmlUtil;
        this.writeUtil = writeUtil;
        this.positionUtil = positionUtil;
        this.stylesContainer = stylesContainer;
        this.format = format;
        this.libreOfficeMode = libreOfficeMode;
        this.name = name;
        this.columnCapacity = columnCapacity;
        this.configEntry = configEntry;
        this.style = TableStyle.DEFAULT_TABLE_STYLE;

        this.columnStyles = FastFullList.<TableColumnStyle>builder()
                .blankElement(TableColumnStyle.DEFAULT_TABLE_COLUMN_STYLE)
                .capacity(this.columnCapacity).build();
        this.tableRows = FastFullList.newListWithCapacity(rowCapacity);
        this.curRowIndex = -1;
        this.lastFlushedRowIndex = 0;
        this.lastRowIndex = -1;
        this.bufferSize = bufferSize;
    }

    /**
     * Add an observer to this table
     *
     * @param observer the observer
     */
    public void addObserver(final NamedOdsFileWriter observer) {
        this.observer = observer;
    }

    /**
     * Flush the begin of the table
     *
     * @param appender the destination
     * @throws IOException if an error occurs
     */
    public void flushBeginTable(final TableAppender appender) throws IOException {
        this.observer.update(new BeginTableFlusher(appender));
    }

    /**
     * Flush the end of the table
     *
     * @param appender the destination
     * @throws IOException if an error occurs
     */
    public void flushEndTable(final TableAppender appender) throws IOException {
        this.observer.update(new EndTableFlusher(appender,
                this.tableRows.subList(this.lastFlushedRowIndex, this.tableRows.usedSize())));
    }

    /**
     * @return the list of the column styles
     */
    public FastFullList<TableColumnStyle> getColumnStyles() {
        return this.columnStyles;
    }

    /**
     * @return the config entry
     */
    public ConfigItemMapEntry getConfigEntry() {
        return this.configEntry;
    }

    /**
     * @return the index of the last used row
     */
    public int getLastRowNumber() {
        return this.tableRows.usedSize() - 1;
    }

    /**
     * get a row from a table
     *
     * @param table    the table
     * @param appender the appender
     * @param rowIndex the row index
     * @return the table row
     * @throws IllegalArgumentException if the index is invalid
     * @throws IOException              if an I/O error occurs
     */
    public TableRow getRow(final Table table, final TableAppender appender, final int rowIndex)
            throws IOException {
        TableBuilder.checkRow(rowIndex);
        return this.getRowSecure(table, appender, rowIndex, true);
    }

    /**
     * get a row from a table
     *
     * @param table    the table
     * @param appender the appender
     * @param address  a cell position, e.g. A5
     * @return the table row
     * @throws IllegalArgumentException if the index is invalid
     * @throws IOException              if an I/O error occurs
     */
    public TableRow getRow(final Table table, final TableAppender appender, final String address)
            throws FastOdsException, IOException, ParseException {
        final int row = this.positionUtil.newPosition(address).getRow();
        return this.getRow(table, appender, row);
    }

    private TableRow getRowSecure(final Table table, final TableAppender appender,
                                  final int rowIndex, final boolean updateRowIndex)
            throws IOException {
        TableRow tr = this.tableRows.get(rowIndex);
        if (tr == null) {
            tr = new TableRow(this.writeUtil, this.xmlUtil, this.stylesContainer, this.format,
                    this.libreOfficeMode, table, rowIndex, this.columnCapacity);
            this.tableRows.set(rowIndex, tr);
            if (rowIndex > this.lastRowIndex) {
                this.lastRowIndex = rowIndex;
            }

            this.notifyIfHasObserver(appender, rowIndex);
        }
        if (updateRowIndex && this.curRowIndex < rowIndex) {
            this.curRowIndex = rowIndex;
        }
        return tr;
    }

    private void notifyIfHasObserver(final TableAppender appender, final int rowIndex)
            throws IOException {
        if (this.observer != null) {
            if (rowIndex == 0) {
                this.observer.update(new BeginTableFlusher(appender));
            } else if (rowIndex % this.bufferSize == 0) {
                this.observer.update(this
                        .createPreprocessedRowsFlusher(rowIndex)); // (0..1023), (1024..2047)
                this.lastFlushedRowIndex = rowIndex;
            }
        }
    }

    private OdsFlusher createPreprocessedRowsFlusher(final int toRowIndex) throws IOException {
        return PreprocessedRowsFlusher.create(this.xmlUtil, new ArrayList<TableRow>(
                this.tableRows.subList(this.lastFlushedRowIndex, toRowIndex)));
    }

    /**
     * Get the current TableFamilyStyle
     *
     * @return The current TableStlye
     */
    public String getStyleName() {
        return this.style.getName();
    }

    /**
     * Get the next row
     *
     * @param table    the table
     * @param appender the appender
     * @return the row
     * @throws IOException if an I/O error occurs
     */
    public TableRow nextRow(final Table table, final TableAppender appender) throws IOException {
        return this.getRowSecure(table, appender, this.curRowIndex + 1, true);
    }

    /**
     * Merge cells
     *
     * @param table    the table
     * @param appender the appender
     * @param rowIndex the start row
     * @param colIndex the start column
     * @param rowCount number of rows
     * @param colCount number of cols
     * @throws IOException if an I/O error occurs
     */
    public void setCellMerge(final Table table, final TableAppender appender, final int rowIndex,
                             final int colIndex, final int rowCount, final int colCount)
            throws IOException {
        final TableRow row = this.getRowSecure(table, appender, rowIndex, true);
        final TableCell firstCell = row.getOrCreateCell(colIndex);
        if (firstCell.isCovered()) {// already spanned
            throw new IllegalArgumentException("Can't merge cells from a covered cell");
        }

        firstCell.markColumnsSpanned(colCount);
        firstCell.markRowsSpanned(rowCount);
        row.coverRightCells(colIndex, colCount);
        for (int r = rowIndex + 1; r < rowIndex + rowCount; r++) {
            final TableRow otherRow = this.getRowSecure(table, appender, r, false);
            otherRow.coverRightCells(colIndex - 1, colCount + 1);
        }
    }

    /**
     * Set the merging of multiple cells to one cell.
     *
     * @param table       the table
     * @param appender    the appender
     * @param address     The cell position e.g. 'A1'
     * @param rowMerge    the number of rows to merge
     * @param columnMerge the number of cells to merge
     * @throws FastOdsException if the row index or the col index is negative
     * @throws IOException      if the cells can't be merged
     */
    @Deprecated
    public void setCellMerge(final Table table, final TableAppender appender, final String address,
                             final int rowMerge, final int columnMerge)
            throws IOException, ParseException {
        final Position position = this.positionUtil.newPosition(address);
        this.setCellMerge(table, appender, position.getRow(), position.getColumn(), rowMerge,
                columnMerge);
    }

    /**
     * Set the style of a column.
     *
     * @param col The column number
     * @param ts  The style to be used, make sure the style is of type
     *            TableFamilyStyle.STYLEFAMILY_TABLECOLUMN
     * @throws FastOdsException Thrown if col has an invalid value.
     */
    public void setColumnStyle(final int col, final TableColumnStyle ts) throws FastOdsException {
        TableBuilder.checkCol(col);
        this.stylesContainer.addContentFontFaceContainerStyle(ts);
        ts.addToContentStyles(this.stylesContainer);
        this.columnStyles.set(col, ts);
    }

    /**
     * Set a config item
     *
     * @param name  the item name
     * @param type  the item type
     * @param value the item value
     */
    public void setConfigItem(final String name, final String type, final String value) {
        this.configEntry.add(new ConfigItem(name, type, value));
    }

    /**
     * Set one of the settings
     *
     * @param item  the item name
     * @param value the item value
     */
    public void updateConfigItem(final String item, final String value) {
        this.configEntry.set(item, value);
    }

    /**
     * Set a new TableFamilyStyle
     *
     * @param style The new TableStyle to be used
     */
    public void setStyle(final TableStyle style) {
        this.stylesContainer.addPageStyle(style.getPageStyle());
        this.stylesContainer.addContentStyle(style);
        this.style = style;
    }

    /**
     * Set a span over rows
     *
     * @param table    the table
     * @param appender the appender
     * @param rowIndex the row index
     * @param colIndex the col index
     * @param n        the number of rows
     * @throws IOException              if an error occurs
     * @throws IllegalArgumentException if n < 0
     */
    public void setRowsSpanned(final Table table, final TableAppender appender, final int rowIndex,
                               final int colIndex, final int n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("Can't span over a negative number of rows");
        } else if (n <= 1) {
            return;
        }

        final TableCell firstCell = this.getRowSecure(table, appender, rowIndex, false)
                .getOrCreateCell(colIndex);
        if (firstCell.isCovered()) {
            throw new IllegalArgumentException("Can't span from a covered cell");
        }

        firstCell.markRowsSpanned(n);
        this.coverCellsBelow(table, appender, rowIndex, colIndex, n);
    }

    private void coverCellsBelow(final Table table, final TableAppender appender,
                                 final int rowIndex, final int colIndex, final int n)
            throws IOException {
        for (int r = rowIndex + 1; r < rowIndex + n; r++) {
            final TableRow row = this.getRowSecure(table, appender, r, false);
            final TableCell cell = row.getOrCreateCell(colIndex);
            cell.setCovered();
        }
    }

    /**
     * @return the name of the table to build
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of this table.
     *
     * @param name The name of this table.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the number if rows
     */
    public int getTableRowsUsedSize() {
        return this.tableRows.usedSize();
    }

    /**
     * Return a table row from its index
     *
     * @param r the index
     * @return the row
     */
    public TableRow getTableRow(final int r) {
        return this.tableRows.get(r);
    }

    /**
     * Find the default cell style for a column
     *
     * @param columnIndex the column index
     * @return the style, *never null*
     */
    public TableCellStyle findDefaultCellStyle(final int columnIndex) {
        TableCellStyle style = this.columnStyles.get(columnIndex).getDefaultCellStyle();
        if (style == null) {
            style = TableCellStyle.DEFAULT_CELL_STYLE;
        }
        return style;
    }
}
