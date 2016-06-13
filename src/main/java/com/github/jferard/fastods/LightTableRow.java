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

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.util.FullList;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file LightTableRow.java is part of FastODS.
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:body/office:spreadsheet/
 *         table:table/table:table-row
 */
public class LightTableRow {
	private TableCellStyle defaultCellStyle;
	private final DataStyles format;
	private final int row;
	private final OdsFile odsFile;
	private final List<HeavyTableCell> tableCells;
	private TableRowStyle rowStyle;
	private final Util util;
	private final XMLUtil xmlUtil;

	LightTableRow(final OdsFile odsFile, final Util util, final XMLUtil xmlUtil,
			final DataStyles format, final int row, final int columnCapacity) {
		this.util = util;
		this.xmlUtil = xmlUtil;
		this.format = format;
		this.row = row;
		this.odsFile = odsFile;
		this.rowStyle = TableRowStyle.DEFAULT_TABLE_ROW_STYLE;
		this.tableCells = FullList.newListWithCapacity(columnCapacity);
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @throws IOException
	 */
	public void appendXMLToTable(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<table:table-row");
		if (this.rowStyle != null)
			util.appendEAttribute(appendable, "table:style-name",
					this.rowStyle.getName());
		if (this.defaultCellStyle != null)
			util.appendEAttribute(appendable, "table:default-cell-style-name",
					this.defaultCellStyle.getName());
		appendable.append(">");

		int nullFieldCounter = 0;
		for (final HeavyTableCell tc : this.tableCells) {
			if (tc == null) {
				nullFieldCounter++;
			} else {
				if (nullFieldCounter > 0) {
					appendable.append("<table:table-cell");
					if (nullFieldCounter > 1)
						util.appendEAttribute(appendable,
								"table:number-columns-repeated",
								nullFieldCounter);
					appendable.append("/>");
					nullFieldCounter = 0;
				}
				tc.appendXMLToTableRow(util, appendable);
			}
		}

		appendable.append("</table:table-row>");
	}

	/**
	 * Added 0.5.1:<br>
	 * Get a HeavyTableCell, if no HeavyTableCell was present at this col,
	 * create a new one with a default of HeavyTableCell.STYLE_STRING and a
	 * content of "".
	 *
	 * @param col
	 * @return The HeavyTableCell for this position, maybe a new HeavyTableCell
	 */
	public HeavyTableCell getCell(final int col) {
		HeavyTableCell tc = this.tableCells.get(col);
		if (tc == null) {
			tc = new HeavyTableCell(this.odsFile, this.xmlUtil, this.format,
					this.row, col);
			this.tableCells.set(col, tc);
		}
		return tc;
	}

	public HeavyTableCell getCell(final String pos) {
		final int col = this.util.getPosition(pos).getColumn();
		return this.getCell(col);
	}

	public String getRowStyleName() {
		return this.rowStyle.getName();
	}

	public HeavyTableCell nextCell() {
		final int col = this.tableCells.size();
		final HeavyTableCell tc = new HeavyTableCell(this.odsFile, this.xmlUtil,
				this.format, this.row, col);
		this.tableCells.add(tc);
		return tc;
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param col
	 *            The column, 0 is the first column
	 * @param rowMerge
	 * @param columnMerge
	 *
	 * @throws FastOdsException
	 */
	public void setCellMerge(final int col, final int rowMerge,
			final int columnMerge) throws FastOdsException {
		final HeavyTableCell tc = this.getCell(col);
		tc.setRowsSpanned(rowMerge);
		tc.setColumnsSpanned(columnMerge);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param pos
	 *            The cell position e.g. 'A1'
	 * @param rowMerge
	 * @param columnMerge
	 *
	 * @throws FastOdsException
	 */
	public void setCellMerge(final String pos, final int rowMerge,
			final int columnMerge) throws FastOdsException {
		final int col = this.util.getPosition(pos).getColumn();
		this.setCellMerge(col, rowMerge, columnMerge);
	}

	/**
	 * Set the cell rowStyle for the cell at col to ts.
	 *
	 * @param col
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

	/**
	 * @return The List with all HeavyTableCell objects
	 */
	protected List<HeavyTableCell> getCells() {
		return this.tableCells;
	}

	/**
	 * Set HeavyTableCell object at position col.<br>
	 * If there is already a HeavyTableCell object, the old one is overwritten.
	 *
	 * @param col
	 *            The column for this cell
	 * @param tc
	 */
	protected void setCell(final int col, final HeavyTableCell tc) {
		this.tableCells.set(col, tc);
	}
}
