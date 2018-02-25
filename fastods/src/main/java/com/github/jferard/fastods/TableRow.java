/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.util.FullList;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * WHERE ? content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-row
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableRow {
    /**
     * Append the XML corresponding to a given row to the appendable
     *
     * @param row        a TableRow
     * @param xmlUtil    an instance of xml util
     * @param appendable where to append the row XML
     * @throws IOException if an error occurs
     */
    public static void appendXMLToTable(final TableRow row, final XMLUtil xmlUtil,
                                        final Appendable appendable) throws IOException {
        if (row == null) appendable.append("<row />");
        else row.appendXMLToTable(xmlUtil, appendable);
    }


    private final int columnCapacity;
    private final Table parent;
    private final int rowIndex;
    private final StylesContainer stylesContainer;
    private final WriteUtil writeUtil;
    private final XMLUtil xmlUtil;
    private final FullList<TableCell> cells;
    private DataStyles dataStyles;
    private TableCellStyle defaultCellStyle;
    private TableRowStyle rowStyle;

    /**
     * Create a new TableRow
     *
     * @param writeUtil       an util
     * @param xmlUtil         an util
     * @param stylesContainer the styles containes
     * @param dataStyles      the data styles
     * @param parent          the parent table
     * @param rowIndex        the index of this row
     * @param columnCapacity  the max column
     */
    TableRow(final WriteUtil writeUtil, final XMLUtil xmlUtil, final StylesContainer stylesContainer,
             final DataStyles dataStyles, final Table parent, final int rowIndex, final int columnCapacity) {
        this.writeUtil = writeUtil;
        this.stylesContainer = stylesContainer;
        this.xmlUtil = xmlUtil;
        this.dataStyles = dataStyles;
        this.parent = parent;
        this.rowIndex = rowIndex;
        this.columnCapacity = columnCapacity;
        this.rowStyle = TableRowStyle.DEFAULT_TABLE_ROW_STYLE;
        this.cells = FullList.newListWithCapacity(columnCapacity);
    }

    private void appendRowOpenTag(final XMLUtil util, final Appendable appendable) throws IOException {
        appendable.append("<table:table-row");
        if (this.rowStyle != null) util.appendEAttribute(appendable, "table:style-name", this.rowStyle.getName());
        if (this.defaultCellStyle != null)
            util.appendEAttribute(appendable, "table:default-cell-style-name", this.defaultCellStyle.getName());
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
    public void appendXMLToTable(final XMLUtil util, final Appendable appendable) throws IOException {
        this.appendRowOpenTag(util, appendable);
        int nullFieldCounter = 0;

        final int size = this.cells.usedSize();
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

    private void appendRepeatedCell(final XMLUtil util, final Appendable appendable,
                                    final int nullFieldCounter) throws IOException {
        if (nullFieldCounter <= 0) return;

        appendable.append("<table:table-cell");
        if (nullFieldCounter >= 2) util.appendAttribute(appendable, "table:number-columns-repeated", nullFieldCounter);
        appendable.append("/>");
    }

    private boolean hasNoValue(final TableCell cell) {
        return cell == null || !cell.hasValue();
    }

    /**
     * @return a CellWalker on the row
     */
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
    public void setCellMerge(final int colIndex, final int rowMerge, final int columnMerge) throws IOException {
        if (rowMerge <= 0 || columnMerge <= 0) return;
        if (rowMerge <= 1 && columnMerge <= 1) return;

        this.parent.setCellMerge(this.rowIndex, colIndex, rowMerge, columnMerge);
    }

    private void coverRightCells(final int colIndex, final int n) {
        for (int c = colIndex + 1; c < colIndex + n; c++) {
            this.getOrCreateCell(c).setCovered();
        }
    }

    /**
     * Add a span across columns
     *
     * @param colIndex the index of the first column
     * @param n        the number of columns in the span
     */
    public void setColumnsSpanned(final int colIndex, final int n) {
        if (n <= 1) return;

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
        this.stylesContainer.addContentStyle(ts);
        this.defaultCellStyle = ts;
    }

    /**
     * Add a format to this TableRow
     *
     * @param format the format
     */
    public void setFormat(final DataStyles format) {
        this.dataStyles = format;
    }

    /**
     * Add a span across rows
     *
     * @param rowIndex the index of the first row
     * @param n        the number of rows in the span
     * @throws IOException if the cells can't be merged
     */
    public void setRowsSpanned(final int rowIndex, final int n) throws IOException {
        if (n <= 1) return;

        final TableCell firstCell = this.getOrCreateCell(rowIndex);
        if (firstCell.isCovered()) return;

        this.parent.setRowsSpanned(this.rowIndex, rowIndex, n);
    }

    /**
     * Get the cell at given index. If the cell was not created before, then it is created by this method.
     *
     * @param colIndex the index of the cell in the row
     * @return a cell
     */
    public TableCell getOrCreateCell(final int colIndex) {
        TableCell cell = this.cells.get(colIndex);
        if (cell == null) {
            cell = new TableCellImpl(this.writeUtil, this.xmlUtil, this.stylesContainer, this.dataStyles, this,
                    colIndex);
            this.cells.set(colIndex, cell);
        }
        return cell;
    }

    /**
     * Set the row style
     *
     * @param rowStyle the style
     */
    public void setStyle(final TableRowStyle rowStyle) {
        this.stylesContainer.addContentStyle(rowStyle);
        this.rowStyle = rowStyle;
    }

    /**
     * @return the current column count
     */
    public int getColumnCount() {
        return this.cells.usedSize();
    }

    /**
     * @param colIndex the index to look for
     * @return true if the cell at the colIndex is covered by a span
     */
    public boolean isCovered(final int colIndex) {
        final TableCell cell = this.cells.get(colIndex);
        return cell != null && cell.isCovered();
    }

    /**
     * Find the default cell style for a column
     * @param columnIndex the column index
     * @return the style, null if none
     */
    public TableCellStyle findDefaultCellStyle(final int columnIndex) {
        TableCellStyle s = this.defaultCellStyle;
        if (s == null) {
            s = this.parent.findDefaultCellStyle(columnIndex);
        }
        return s;
    }
}