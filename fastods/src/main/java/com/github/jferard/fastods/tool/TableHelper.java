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

package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.CellValue;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.ref.CellRef;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.style.TableCellStyle;

import java.io.IOException;
import java.text.ParseException;

/**
 * A table helper
 *
 * @author Julien Férard
 */
public class TableHelper {
    /**
     * @return a new TableHelper
     */
    public static TableHelper create() {
        return new TableHelper(PositionUtil.create());
    }

    private final PositionUtil positionUtil;

    /**
     * Create the table helper.
     *
     * @param positionUtil an util
     */
    public TableHelper(final PositionUtil positionUtil) {
        this.positionUtil = positionUtil;
    }

    /**
     * Set the merging of multiple cells to one cell.
     *
     * @param table       the table where the cells to merge are
     * @param rowIndex    The row, 0 is the first row
     * @param colIndex    The column, 0 is the first column
     * @param rowMerge    the number of rows to merge
     * @param columnMerge the number of columns to merge
     * @throws IOException if the cells can't be merged
     * @deprecated use table.setCellMerge.
     */
    @Deprecated
    public void setCellMerge(final Table table, final int rowIndex, final int colIndex,
                             final int rowMerge, final int columnMerge) throws IOException {
        table.setCellMerge(rowIndex, colIndex, rowMerge, columnMerge);
    }

    /**
     * Set the merging of multiple cells to one cell in all existing tables.
     *
     * @param table       the table where the cells to merge are
     * @param address     The cell position e.g. 'A1'
     * @param rowMerge    the number of rows to merge
     * @param columnMerge the number of cells to merge
     * @throws IOException    if the cells can't be merged
     * @throws ParseException if the address can't be parsed
     */
    public void setCellMerge(final Table table, final String address, final int rowMerge,
                             final int columnMerge) throws IOException, ParseException {
        final CellRef position = this.positionUtil.newCellRef(address);
        final int row = position.getRow();
        final int col = position.getColumn();
        table.setCellMerge(row, col, rowMerge, columnMerge);
    }

    /**
     * @param table    the table where the cells to merge are
     * @param rowIndex The row, 0 is the first row
     * @param colIndex The column, 0 is the first column
     * @param value    the value to set
     * @param ts       the cell style
     * @throws IOException if the cell value can't be set
     */
    public void setCellValue(final Table table, final int rowIndex, final int colIndex,
                             final CellValue value, final TableCellStyle ts) throws IOException {
        final TableCell cell = this.getCell(table, rowIndex, colIndex);
        cell.setCellValue(value);
        cell.setStyle(ts);
    }

    /**
     * @param table    the table where the cells to merge are
     * @param rowIndex The row, 0 is the first row
     * @param colIndex The column, 0 is the first column
     * @param value    the value to set
     * @throws IOException if the cell value can't be set
     */
    public void setCellValue(final Table table, final int rowIndex, final int colIndex,
                             final CellValue value) throws IOException {
        final TableCell cell = this.getCell(table, rowIndex, colIndex);
        cell.setCellValue(value);
    }

    /**
     * Sets the cell value in all tables to the given values.
     *
     * @param address The cell position e.g. 'A1'
     * @param table   The table where the value is set
     * @param value   The value to set the cell to
     * @param ts      The table style for this cell
     * @throws IOException    if the cell value can't be set
     * @throws ParseException if the address can't be parsed
     */
    public void setCellValue(final Table table, final String address, final CellValue value,
                             final TableCellStyle ts) throws IOException, ParseException {
        final CellRef position = this.positionUtil.newCellRef(address);
        final int row = position.getRow();
        final int col = position.getColumn();
        this.setCellValue(table, row, col, value, ts);
    }

    /**
     * Sets the cell value in all tables to the given values.
     *
     * @param address The cell position e.g. 'A1'
     * @param table   The table where the value is set
     * @param value   The value to set the cell to
     * @throws IOException    if the cell value can't be set
     * @throws ParseException if the address can't be parsed
     */
    public void setCellValue(final Table table, final String address, final CellValue value)
            throws IOException, ParseException {
        final CellRef position = this.positionUtil.newCellRef(address);
        final int row = position.getRow();
        final int col = position.getColumn();
        this.setCellValue(table, row, col, value);
    }

    /**
     * @param table    the table where the cells to merge are
     * @param rowIndex The row, 0 is the first row
     * @param colIndex The column, 0 is the first column
     * @return the cell walker
     * @throws IOException if the row was flushed
     */
    public TableCellWalker getCell(final Table table, final int rowIndex, final int colIndex)
            throws IOException {
        final TableCellWalker walker = table.getWalker();
        walker.toRow(rowIndex);
        walker.to(colIndex);
        return walker;
    }

    /**
     * @param table   the table where the cells to merge are
     * @param address the address
     * @return the cell walker
     * @throws IOException    if the row was flushed
     * @throws ParseException if the address can't be parsed
     */
    public TableCellWalker getCell(final Table table, final String address)
            throws IOException, ParseException {
        final CellRef position = this.positionUtil.newCellRef(address);
        return this.getCell(table, position.getRow(), position.getColumn());
    }
}
