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

import java.util.Calendar;

import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TableCell.Type;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.Util.Position;

public class OdsFileHelper {
	private OdsFile odsFile;
	private Util util;
	private TableHelper tableHelper;

	public OdsFileHelper(OdsFile odsFile, TableHelper tableHelper, Util util) {
		this.odsFile = odsFile;
		this.tableHelper = tableHelper;
		this.util = util;
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
	public void setCellInAllTables(final int rowIndex, final int colIndex,
			final Calendar cal, final TableCellStyle ts)
			throws FastOdsException {

		for (final Table table : this.odsFile.getTables()) {
			this.tableHelper.setCell(table, rowIndex, colIndex, cal, ts);
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
	public void setCellInAllTables(final String pos,
			final TableCell.Type valuetype, final String value,
			final TableCellStyle ts) throws FastOdsException {
		final Position position = this.util.getPosition(pos);
		final int rowIndex = position.getRow();
		final int colIndex = position.getColumn();
		this.setCellInAllTables(rowIndex, colIndex, valuetype, value, ts);
	}

	public void setCellInAllTables(int rowIndex, int colIndex, Type valuetype,
			String value, TableCellStyle ts) throws FastOdsException {
		for (final Table table : this.odsFile.getTables()) {
			this.tableHelper.setCell(table, rowIndex, colIndex, valuetype, value, ts);
		}
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
			this.tableHelper.setCellMerge(table, rowIndex, colIndex, rowMerge, columnMerge);
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
		final Position position = this.util.getPosition(pos);
		final int row = position.getRow();
		final int col = position.getColumn();
		this.setCellMergeInAllTables(row, col, rowMerge, columnMerge);
	}
}
