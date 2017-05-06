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
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * WHERE ? content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-row
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableRow {
	private final int columnCapacity;
	private final Table parent;
	private final int rowIndex;
	private final StylesContainer stylesContainer;
	private final WriteUtil writeUtil;
	private final XMLUtil xmlUtil;
	private DataStyles dataStyles;
	private TableCellStyle defaultCellStyle;
	private TableRowStyle rowStyle;
	private String formula;
	private final List<TableCell> cells;

	TableRow(final WriteUtil writeUtil, final XMLUtil xmlUtil,
             final StylesContainer stylesContainer, final DataStyles dataStyles,
             final Table parent, final int rowIndex, final int columnCapacity) {
		this.writeUtil = writeUtil;
		this.stylesContainer = stylesContainer;
		this.xmlUtil = xmlUtil;
		this.dataStyles = dataStyles;
		this.parent = parent;
		this.rowIndex = rowIndex;
		this.columnCapacity = columnCapacity;
		this.rowStyle = TableRowStyle.DEFAULT_TABLE_ROW_STYLE;
		this.cells = new ArrayList<TableCell>(columnCapacity);
	}

	private void appendRowOpenTag(final XMLUtil util,
								  final Appendable appendable) throws IOException {
		appendable.append("<table:table-row");
		if (this.rowStyle != null)
			util.appendEAttribute(appendable, "table:style-name",
					this.rowStyle.getName());
		if (this.defaultCellStyle != null)
			util.appendEAttribute(appendable, "table:default-cell-style-name",
					this.defaultCellStyle.getName());
		appendable.append(">");
	}

	/**
	 * Write the XML dataStyles for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @param util       a util for XML writing
	 * @param appendable where to write the XML
	 * @throws IOException If an I/O error occurs
	 */
	public void appendXMLToTable(final XMLUtil util,
								 final Appendable appendable) throws IOException {
		this.appendRowOpenTag(util, appendable);
		int nullFieldCounter = 0;

		final int size = this.cells.size();
		for (int c = 0; c < size; c++) {
			final TableCell cell = this.cells.get(c);
			if (this.hasNoValue(cell)) {
				nullFieldCounter++;
				continue;
			}
			this.appendRepeatedCell(util, appendable, nullFieldCounter);
			nullFieldCounter = 0;
			cell.appendXMLToTableRow(util, appendable);
		}

		appendable.append("</table:table-row>");
	}

	private void appendRepeatedCell(final XMLUtil util, final Appendable appendable, final int nullFieldCounter) throws IOException {
		if (nullFieldCounter <= 0)
			return;

		appendable.append("<table:table-cell");
		if (nullFieldCounter >= 2)
			util.appendEAttribute(appendable,
					"table:number-columns-repeated",
					nullFieldCounter);
		appendable.append("/>");
	}

	private boolean hasNoValue(final TableCell cell) {
		return cell == null || !cell.hasValue();
	}

	public TableCellWalker getWalker() {
		return new TableCellWalkerImpl(this);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param colIndex    The column, 0 is the first column
	 * @param rowMerge    the number of rows to merge
	 * @param columnMerge the number of cells to merge
	 * @throws IOException if the cells can't be merged
	 */
	public void setCellMerge(final int colIndex, final int rowMerge,
							 final int columnMerge) throws IOException, FastOdsException {
		if (rowMerge <= 0 || columnMerge <= 0)
			return;
		if (rowMerge <= 1 && columnMerge <= 1)
			return;

		this.parent.setCellMerge(this.rowIndex, colIndex, rowMerge, columnMerge);
	}

	private void coverRightCells(final int colIndex, final int n) {
		for (int c=colIndex+1; c<colIndex+n; c++) {
			this.getOrCreateCell(c).setCovered();
		}
	}

	public void setColumnsSpanned(final int colIndex, final int n) {
		if (n <= 1)
			return;

		final TableCell firstCell = this.getOrCreateCell(colIndex);
		if (firstCell.isCovered()) // already spanned
			return;

		firstCell.markColumnsSpanned(n);
		this.coverRightCells(colIndex, n);
	}

	/**
	 * Set the cell rowStyle for the cell at col to ts.
	 *
	 * @param ts The table rowStyle to be used
	 */
	public void setDefaultCellStyle(final TableCellStyle ts) {
		this.stylesContainer.addStyleToStylesCommonStyles(ts);
		this.defaultCellStyle = ts;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFormat(com.github.jferard.fastods.style.DataStyles)
	 */
	public void setFormat(final DataStyles format) {
		this.dataStyles = format;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setRowsSpanned(int)
	 */
	public void setRowsSpanned(final int colIndex, final int n) throws IOException {
		if (n <= 1)
			return;

		final TableCell firstCell = this.getOrCreateCell(colIndex);
		if (firstCell.isCovered())
			return;

		this.parent.setRowsSpanned(this.rowIndex, colIndex, n);
	}

	public TableCell getOrCreateCell(final int colIndex) {
		if (this.cells.size() <= colIndex) {
			while (this.cells.size() < colIndex) {
				this.cells.add(null);
			}
			// assert this.cells.size() == colIndex
            final TableCell cell = new TableCellImpl(this.writeUtil, this.xmlUtil,
                    this.stylesContainer, this.dataStyles,
                    this, this.rowIndex, this.columnCapacity);
			this.cells.add(cell);

		}
		return this.cells.get(colIndex);
	}

	public void setStyle(final TableRowStyle rowStyle) {
		this.stylesContainer.addStyleToContentAutomaticStyles(rowStyle);
		this.rowStyle = rowStyle;
	}

	public int getColumnCount() {
		return this.cells.size();
	}

	public boolean isCovered(final int colIndex) {
		return this.cells.get(colIndex).isCovered();
	}
}