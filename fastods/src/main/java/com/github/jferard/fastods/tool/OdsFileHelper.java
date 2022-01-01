/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.CellValue;
import com.github.jferard.fastods.NamedOdsDocument;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.ref.CellRef;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.style.TableCellStyle;

import java.io.IOException;
import java.text.ParseException;

/**
 * An helper for multi table work.
 *
 * @author Julien Férard
 */
public class OdsFileHelper {
    private final NamedOdsDocument odsDocument;
    private final PositionUtil positionUtil;
    private final TableHelper tableHelper;

    /**
     * Create the helper.
     *
     * @param odsDocument  the document
     * @param tableHelper  a sub helper
     * @param positionUtil an util
     */
    public OdsFileHelper(final NamedOdsDocument odsDocument, final TableHelper tableHelper,
                         final PositionUtil positionUtil) {
        this.odsDocument = odsDocument;
        this.tableHelper = tableHelper;
        this.positionUtil = positionUtil;
    }

    /**
     * Set the merging of multiple cells to one cell.
     *
     * @param rowIndex    The row, 0 is the first row
     * @param colIndex    The column, 0 is the first column
     * @param rowMerge    the number of rows to merge
     * @param columnMerge the number of columns to merge
     * @throws IOException if the cells can't be merged
     */
    public void setCellMergeInAllTables(final int rowIndex, final int colIndex, final int rowMerge,
                                        final int columnMerge) throws IOException {
        for (final Table table : this.odsDocument.getTables()) {
            table.setCellMerge(rowIndex, colIndex, rowMerge, columnMerge);
        }
    }

    /**
     * Set the merging of multiple cells to one cell in all existing tables.
     *
     * @param address     The cell position e.g. 'A1'
     * @param rowMerge    the number of rows to merge
     * @param columnMerge the number of columns to merge
     * @throws IOException    if the cells can't be merged
     * @throws ParseException if the address can't be parsed
     */
    public void setCellMergeInAllTables(final String address, final int rowMerge,
                                        final int columnMerge) throws IOException, ParseException {
        final CellRef position = this.positionUtil.newCellRef(address);
        final int row = position.getRow();
        final int col = position.getColumn();
        this.setCellMergeInAllTables(row, col, rowMerge, columnMerge);
    }

    /**
     * Sets the cell value in all tables to the date from the Calendar object.
     *
     * @param rowIndex The row, 0 is the first row
     * @param colIndex The column, 0 is the first column
     * @param value    The cell value
     * @param ts       The table style for this cell
     * @throws IOException if the cells can't be merged
     */
    public void setCellValueInAllTables(final int rowIndex, final int colIndex,
                                        final CellValue value, final TableCellStyle ts)
            throws IOException {

        for (final Table table : this.odsDocument.getTables()) {
            this.tableHelper.setCellValue(table, rowIndex, colIndex, value, ts);
        }

    }

    /**
     * Sets the cell value in all tables to the date from the Calendar object.
     *
     * @param address The cell position e.g. 'A1'
     * @param value   The cell value
     * @param ts      The table style for this cells
     * @throws IOException    if the cells can't be merged
     * @throws ParseException if the address can't be parsed
     */
    public void setCellValueInAllTables(final String address, final CellValue value,
                                        final TableCellStyle ts)
            throws IOException, ParseException {
        final CellRef position = this.positionUtil.newCellRef(address);
        final int row = position.getRow();
        final int col = position.getColumn();
        this.setCellValueInAllTables(row, col, value, ts);
    }
}
