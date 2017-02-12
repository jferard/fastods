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

package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.CellValue;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.HeavyTableRow;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.PositionUtil.Position;

import java.io.IOException;

public class TableHelper {
	private final PositionUtil positionUtil;

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
	 * @throws FastOdsException if the rowIndex or the colIndex is negative
	 */
	public void setCellMerge(final Table table, final int rowIndex,
							 final int colIndex, final int rowMerge, final int columnMerge)
			throws FastOdsException, IOException {
		final TableCell cell = this.getCell(table, rowIndex, colIndex);
		cell.setRowsSpanned(rowMerge);
		cell.setColumnsSpanned(columnMerge);
	}

	/**
	 * Set the merging of multiple cells to one cell in all existing tables.
	 *
	 * @param table       the table where the cells to merge are
	 * @param pos         The cell position e.g. 'A1'
	 * @param rowMerge    the number of rows to merge
	 * @param columnMerge the number of cells to merge
	 * @throws FastOdsException if the row index or the col index is negative
	 */
	public void setCellMerge(final Table table, final String pos,
							 final int rowMerge, final int columnMerge) throws FastOdsException, IOException {
		final Position position = this.positionUtil.getPosition(pos);
		final int row = position.getRow();
		final int col = position.getColumn();
		this.setCellMerge(table, row, col, rowMerge, columnMerge);
	}

	public void setCellValue(final Table table, final int rowIndex,
							 final int colIndex, final CellValue value, final TableCellStyle ts)
			throws FastOdsException, IOException {
		final TableCell cell = this.getCell(table, rowIndex, colIndex);
		cell.setCellValue(value);
		cell.setStyle(ts);
	}

	/**
	 * Sets the cell value in all tables to the given values.
	 *
	 * @param pos   The cell position e.g. 'A1'
	 * @param table The tlbe where the value is set
	 * @param value The value to set the cell to
	 * @param ts    The table style for this cell, must be of type
	 *              TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException if the row index or the col index is negative
	 */
	public void setCellValue(final Table table, final String pos,
							 final CellValue value, final TableCellStyle ts)
			throws FastOdsException, IOException {
		final Position position = this.positionUtil.getPosition(pos);
		final int row = position.getRow();
		final int col = position.getColumn();
		this.setCellValue(table, row, col, value, ts);
	}

	private TableCellWalker getCell(final Table table, final int rowIndex,
									final int colIndex) throws FastOdsException, IOException {
		final HeavyTableRow row = table.getRow(rowIndex);
		final TableCellWalker walker = row.getWalker();
		walker.to(colIndex);
		return walker;
	}
}
