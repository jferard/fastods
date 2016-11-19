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
package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.CellValue;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.PositionUtil.Position;

public class OdsFileHelper {
	private final OdsFile odsFile;
	private final PositionUtil positionUtil;
	private final TableHelper tableHelper;

	public OdsFileHelper(final OdsFile odsFile, final TableHelper tableHelper,
			final PositionUtil positionUtil) {
		this.odsFile = odsFile;
		this.tableHelper = tableHelper;
		this.positionUtil = positionUtil;
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
	public void setCellMergeInAllTables(final int rowIndex, final int colIndex,
			final int rowMerge, final int columnMerge) throws FastOdsException {
		for (final Table table : this.odsFile.getTables()) {
			this.tableHelper.setCellMerge(table, rowIndex, colIndex, rowMerge,
					columnMerge);
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
	public void setCellMergeInAllTables(final String pos, final int rowMerge,
			final int columnMerge) throws FastOdsException {
		final Position position = this.positionUtil.getPosition(pos);
		final int row = position.getRow();
		final int col = position.getColumn();
		this.setCellMergeInAllTables(row, col, rowMerge, columnMerge);
	}

	/**
	 * Sets the cell value in all tables to the date from the Calendar object.
	 *
	 * @param rowIndex
	 *            The row, 0 is the first row
	 * @param col
	 *            The column, 0 is the first column
	 * @param value
	 *            The cell value
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCellValueInAllTables(final int rowIndex, final int colIndex,
			final CellValue value, final TableCellStyle ts)
			throws FastOdsException {

		for (final Table table : this.odsFile.getTables()) {
			this.tableHelper.setCellValue(table, rowIndex, colIndex, value, ts);
		}

	}

	/**
	 * Sets the cell value in all tables to the date from the Calendar object.
	 *
	 * @param pos
	 *            The cell position e.g. 'A1'
	 * @param value
	 *            The cell value
	 * @param ts
	 *            The table style for this cells, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCellValueInAllTables(final String pos, final CellValue value,
			final TableCellStyle ts) throws FastOdsException {
		final Position position = this.positionUtil.getPosition(pos);
		final int row = position.getRow();
		final int col = position.getColumn();
		this.setCellValueInAllTables(row, col, value, ts);
	}
}
