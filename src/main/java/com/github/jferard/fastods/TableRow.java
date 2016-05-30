/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods;

import java.io.IOException;
import java.util.List;

import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file TableRow.java is part of FastODS.
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:body/office:spreadsheet/
 *         table:table/table:table-row
 */
public class TableRow {
	private final int nRow;
	private final OdsFile odsFile;
	private final List<TableCell> qTableCells;
	private TableRowStyle rowStyle;
	private TableCellStyle defaultCellStyle;
	private Util util;
	private DataStyles format;

	TableRow(final OdsFile odsFile, final Util util, DataStyles format, final int nRow) {
		this.util = util;
		this.format = format;
		this.nRow = nRow;
		this.odsFile = odsFile;
		this.rowStyle = TableRowStyle.DEFAULT_TABLE_ROW_STYLE;
		this.qTableCells = FullList.newList();
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @throws IOException
	 */
	public void appendXMLToTable(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<table:table-row");
		if (this.rowStyle != null)
			util.appendAttribute(appendable, "table:style-name",
					this.rowStyle.getName());
		if (this.defaultCellStyle != null)
			util.appendAttribute(appendable, "table:default-cell-style-name",
					this.defaultCellStyle.getName());
		appendable.append(">");

		int nNullFieldCounter = 0;
		for (final TableCell tc : this.qTableCells) {
			if (tc == null) {
				nNullFieldCounter++;
			} else {
				if (nNullFieldCounter > 0) {
					appendable.append("<table:table-cell");
					if (nNullFieldCounter > 1)
						util.appendEAttribute(appendable,
								"table:number-columns-repeated",
								nNullFieldCounter);
					appendable.append("/>");
					nNullFieldCounter = 0;
				}
				tc.appendXMLToTableRow(util, appendable);
			}
		}

		appendable.append("</table:table-row>");
	}

	/**
	 * Added 0.5.1:<br>
	 * Get a TableCell, if no TableCell was present at this nCol, create a new
	 * one with a default of TableCell.STYLE_STRING and a content of "".
	 *
	 * @param nCol
	 * @return The TableCell for this position, maybe a new TableCell
	 */
	public TableCell getCell(final int nCol) {
		TableCell tc = this.qTableCells.get(nCol);
		if (tc == null) {
			tc = new TableCell(this.odsFile, this.util, this.format, this.nRow, nCol,
					TableCell.Type.STRING, "");
			this.qTableCells.set(nCol, tc);
		}
		return tc;
	}
	
	public TableCell getCell(final String sPos) {
		final int nCol = this.util.getPosition(sPos).getColumn();
		return this.getCell(nCol);
	}
	

	/**
	 * @return The List with all TableCell objects
	 */
	protected List<TableCell> getCells() {
		return this.qTableCells;
	}

	public String getRowStyleName() {
		return this.rowStyle.getName();
	}

	/**
	 * Set TableCell object at position nCol.<br>
	 * If there is already a TableCell object, the old one is overwritten.
	 *
	 * @param nCol
	 *            The column for this cell
	 * @param tc
	 */
	protected void setCell(final int nCol, final TableCell tc) {
		this.qTableCells.set(nCol, tc);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param nRowMerge
	 * @param nColumnMerge
	 *
	 * @throws FastOdsException
	 */
	public void setCellMerge(final int nCol, final int nRowMerge, final int nColumnMerge)
			throws FastOdsException {
		final TableCell tc = this.getCell(nCol);
		tc.setRowsSpanned(nRowMerge);
		tc.setColumnsSpanned(nColumnMerge);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nRowMerge
	 * @param nColumnMerge
	 *
	 * @throws FastOdsException
	 */
	public void setCellMerge(final String sPos, final int nRowMerge,
			final int nColumnMerge)
			throws FastOdsException {
		final int nCol = this.util.getPosition(sPos).getColumn();
		this.setCellMerge(nCol, nRowMerge,
				nColumnMerge);
	}

	/**
	 * Set the cell rowStyle for the cell at nCol to ts.
	 *
	 * @param nCol
	 *            The column number
	 * @param ts
	 *            The table rowStyle to be used
	 */
	public void setDefaultCellStyle(final TableCellStyle ts) {
		ts.addToFile(this.odsFile);
		this.defaultCellStyle = ts;
	}

	public void setStyle(final TableRowStyle rowStyle) {
		rowStyle.addToFile(this.odsFile);
		this.rowStyle = rowStyle;
	}
}