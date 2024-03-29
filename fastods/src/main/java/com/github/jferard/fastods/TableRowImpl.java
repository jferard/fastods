/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods;

import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.util.FastFullList;
import com.github.jferard.fastods.util.IntegerRepresentationCache;
import com.github.jferard.fastods.util.Validation;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 9.1.3 table:table-row
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableRowImpl implements TableRow {
    /**
     * Append the XML corresponding to a given row to the appendable
     *
     * @param row        a TableRow
     * @param xmlUtil    an instance of xml util
     * @param appendable where to append the row XML
     * @throws IOException if an error occurs
     */
    public static void appendXMLToTable(final TableRowImpl row, final XMLUtil xmlUtil,
                                        final Appendable appendable) throws IOException {
        if (row == null) {
            appendable.append("<row />");
        } else {
            row.appendXMLToTable(xmlUtil, appendable);
        }
    }


    private final Table parentTable;
    private final int rowIndex;
    private final ValidationsContainer validationsContainer;
    private final StylesContainer stylesContainer;
    private final IntegerRepresentationCache cache;
    private final XMLUtil xmlUtil;
    private final FastFullList<WritableTableCell> cells;
    private final boolean libreOfficeMode;
    private DataStyles dataStyles;
    private TableRowStyle rowStyle;
    private TableCellStyle defaultCellStyle;
    private Map<String, CharSequence> customValueByAttribute;

    /**
     * Create a new TableRow
     *  @param cache       an util
     * @param xmlUtil         an util
     * @param stylesContainer the styles container
     * @param dataStyles      the data styles
     * @param libreOfficeMode try to get full compatibility with LO if true
     * @param parentTable     the parent table
     * @param rowIndex        the index of this row
     * @param columnCapacity  the max column
     * @param validationsContainer the container for validations
     */
    TableRowImpl(final IntegerRepresentationCache cache, final XMLUtil xmlUtil,
                 final StylesContainer stylesContainer, final DataStyles dataStyles,
                 final boolean libreOfficeMode, final Table parentTable, final int rowIndex,
                 final int columnCapacity, final ValidationsContainer validationsContainer) {
        this.cache = cache;
        this.stylesContainer = stylesContainer;
        this.xmlUtil = xmlUtil;
        this.dataStyles = dataStyles;
        this.libreOfficeMode = libreOfficeMode;
        this.parentTable = parentTable;
        this.rowIndex = rowIndex;
        this.validationsContainer = validationsContainer;
        this.rowStyle = TableRowStyle.DEFAULT_TABLE_ROW_STYLE;
        this.cells = FastFullList.newListWithCapacity(columnCapacity);
    }

    /**
     * Write the XML dataStyles for this object.<br>
     * This is used while writing the ODS file.
     *
     * @param util       a util for XML writing
     * @param appendable where to write the XML
     * @throws IOException If an I/O error occurs
     */
    public void appendXMLToTable(final XMLUtil util, final Appendable appendable)
            throws IOException {
        this.appendRowOpenTag(util, appendable);

        final int size = this.cells.usedSize();
        if (size == 0) { // relaxNG validation : oneOrMore cells
            appendable.append("<table:table-cell/>");
        } else {
            int nullFieldCounter = 0;
            for (int c = 0; c < size; c++) {
                final WritableTableCell cell = this.cells.get(c);
                if (this.hasNoValue(cell)) {
                    nullFieldCounter++;
                    continue;
                }
                this.insertBlankCells(util, appendable, nullFieldCounter);
                nullFieldCounter = 0;
                cell.appendXMLToTableRow(util, appendable);
            }
            this.insertBlankCells(util, appendable, nullFieldCounter); // relaxNG
        }
        appendable.append("</table:table-row>");
    }

    private void appendRowOpenTag(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:table-row");
        if (this.rowStyle != null) {
            util.appendEAttribute(appendable, "table:style-name", this.rowStyle.getName());
        }
        if (this.defaultCellStyle != null) {
            util.appendEAttribute(appendable, "table:default-cell-style-name",
                    this.defaultCellStyle.getName());
        }
        if (this.customValueByAttribute != null) {
            for (final Map.Entry<String, CharSequence> entry : this.customValueByAttribute
                    .entrySet()) {
                util.appendAttribute(appendable, entry.getKey(), entry.getValue());
            }
        }
        appendable.append(">");
    }

    private void insertBlankCells(final XMLUtil util, final Appendable appendable,
                                  final int nullFieldCounter) throws IOException {
        if (nullFieldCounter <= 0) {
            return;
        }

        appendable.append("<table:table-cell");
        if (nullFieldCounter >= 2) {
            util.appendAttribute(appendable, "table:number-columns-repeated", nullFieldCounter);
        }
        appendable.append("/>");
    }

    private boolean hasNoValue(final TableCell cell) {
        return cell == null || !cell.hasValue();
    }

    /**
     * @return a CellWalker on the row
     */
    @Deprecated
    public RowCellWalker getWalker() {
        return new RowCellWalkerImpl(this);
    }

    /**
     * Set the merging of multiple cells to one cell.
     *
     * @param colIndex    The column, 0 is the first column
     * @param rowMerge    the number of rows to merge
     * @param columnMerge the number of cells to merge
     * @throws IOException if the cells can't be merged
     */
    public void setCellMerge(final int colIndex, final int rowMerge, final int columnMerge)
            throws IOException {
        if (rowMerge < 0 || columnMerge < 0) {
            throw new IllegalArgumentException("row merge and col merge must be >= 0");
        } else if (rowMerge <= 1 && columnMerge <= 1) {
            return;
        }
        this.parentTable.setCellMerge(this.rowIndex, colIndex, rowMerge, columnMerge);
    }

    /**
     * Cover the cells to the right of a given index.
     *
     * @param colIndex the start index
     * @param n        the number of cells to cover
     */
    public void coverRightCells(final int colIndex, final int n) {
        for (int c = colIndex + 1; c < colIndex + n; c++) {
            this.getOrCreateCell(c).setCovered();
        }
    }

    /**
     * Add a span across columns
     *
     * @param colIndex the index of the first column
     * @param n        the number of columns in the span
     * @throws IllegalArgumentException if n &lt; 0
     */
    public void setColumnsSpanned(final int colIndex, final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Can't span negative number of columns");
        } else if (n <= 1) {
            return;
        }

        final TableCell firstCell = this.getOrCreateCell(colIndex);
        if (firstCell.isCovered()) {
            throw new IllegalArgumentException("Can't span from covered cell");
        }
        firstCell.markColumnsSpanned(n);
        this.coverRightCells(colIndex, n);
    }

    @Override
    public void setRowFormat(final DataStyles format) {
        this.dataStyles = format;
    }

    /**
     * Add a span across rows
     *
     * @param colIndex the index of the column
     * @param n        the number of rows in the span
     * @throws IOException              if the cells can't be merged
     * @throws IllegalArgumentException if n &lt; 0 of this cells is covered
     */
    public void setRowsSpanned(final int colIndex, final int n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("Can't span negative number of rows");
        } else if (n <= 1) {
            return;
        }

        final TableCell firstCell = this.getOrCreateCell(colIndex);
        if (firstCell.isCovered()) {
            throw new IllegalArgumentException("Can't span from covered cell");
        }

        this.parentTable.setRowsSpanned(this.rowIndex, colIndex, n);
    }

    /**
     * Get the cell at given index. If the cell was not created before, then it is created by
     * this method. This may update the value of `getCurRowSize`.
     *
     * @param colIndex the index of the cell in the row
     * @return a cell
     */
    public TableCell getOrCreateCell(final int colIndex) {
        WritableTableCell cell = this.cells.get(colIndex);
        if (cell == null) {
            cell = new TableCellImpl(this.cache, this.xmlUtil, this.stylesContainer,
                    this.dataStyles, this.libreOfficeMode, this, colIndex);
            this.cells.set(colIndex, cell);
        }
        return cell;
    }

    /**
     * Set a custom table cell at a given index.
     *
     * @param colIndex the index
     * @param cell     the cell
     */
    public void set(final int colIndex, final WritableTableCell cell) {
        this.cells.set(colIndex, cell);
    }

    @Override
    public void setRowStyle(final TableRowStyle rowStyle) {
        this.stylesContainer.addContentStyle(rowStyle);
        this.rowStyle = rowStyle;
    }

    @Override
    @Deprecated
    public int getColumnCount() {
        return this.getCurRowSize();
    }

    @Override
    public int getCurRowSize() {
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
     *
     * @param columnIndex the column index
     * @return the style, never null
     */
    public TableCellStyle findDefaultCellStyle(final int columnIndex) {
        TableCellStyle s = this.defaultCellStyle;
        if (s == null) {
            s = this.parentTable.findDefaultCellStyle(columnIndex);
        }
        return s;
    }

    @Override
    public void setRowDefaultCellStyle(final TableCellStyle ts) {
//        this.stylesContainer.addStylesFontFaceContainerStyle(ts);
        this.stylesContainer.addContentFontFaceContainerStyle(ts);
        this.stylesContainer.addContentStyle(ts);
        this.defaultCellStyle = ts;
    }

    @Override
    public int rowIndex() {
        return this.rowIndex;
    }

    @Override
    public void removeRowStyle() {
        this.rowStyle = null;
    }

    @Override
    public void setRowAttribute(final String attribute, final CharSequence value) {
        if (this.customValueByAttribute == null) {
            this.customValueByAttribute = new HashMap<String, CharSequence>();
        }
        this.customValueByAttribute.put(attribute, value);
    }

    public void addValidationToContainer(final Validation validation) {
        this.validationsContainer.addValidation(validation);
    }
}