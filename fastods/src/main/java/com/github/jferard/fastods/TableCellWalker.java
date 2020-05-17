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

package com.github.jferard.fastods;

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.annotation.Beta;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Julien Férard
 */
public class TableCellWalker implements RowCellWalker, TableRowWalker, TableColumn {
    private final Table table;
    private TableRowImpl row;
    private TableCell cell;
    private int r;
    private int c;

    /**
     * Create a walker on the given row
     *
     * @param table the table
     * @throws IOException if the row was already flushed
     */
    TableCellWalker(final Table table) throws IOException {
        this.table = table;
        this.r = 0;
        this.c = 0;
        this.updateShortcuts(table);
    }

    private void updateShortcuts(final Table table) throws IOException {
        this.row = table.getRow(this.r);
        this.cell = this.row.getOrCreateCell(this.c);
    }

    @Override
    public void markRowsSpanned(final int n) {
        this.cell.markRowsSpanned(n);
    }

    @Override
    public void setBooleanValue(final boolean value) {
        this.cell.setBooleanValue(value);
    }

    @Override
    public void setText(final Text text) {
        this.cell.setText(text);
    }

    @Override
    public void setCellMerge(final int rowMerge, final int columnMerge) throws IOException {
        if (rowMerge < 0 || columnMerge < 0) {
            throw new IllegalArgumentException("row merge and col merge must be >= 0");
        } else if (rowMerge <= 1 && columnMerge <= 1) {
            return;
        }
        this.table.setCellMerge(this.r, this.c, rowMerge, columnMerge);
    }

    @Override
    public void setColumnsSpanned(final int n) {
        this.row.setColumnsSpanned(this.c, n);
    }

    @Override
    public void markColumnsSpanned(final int n) {
        this.cell.markColumnsSpanned(n);
    }

    @Override
    public void setDateValue(final Date value) {
        this.cell.setDateValue(value);
    }

    @Override
    public void setFloatValue(final Number value) {
        this.cell.setFloatValue(value);
    }

    @Override
    public void setPercentageValue(final Number value) {
        this.cell.setPercentageValue(value);
    }

    @Override
    public void setRowsSpanned(final int n) throws IOException {
        this.row.setRowsSpanned(this.c, n);
    }

    @Override
    public void setVoidValue() {
        this.cell.setVoidValue();
    }

    @Override
    public void setMatrixFormula(final String formula) {
        this.cell.setMatrixFormula(formula);
    }

    @Override
    public void setMatrixFormula(final String formula, final int matrixRowsSpanned,
                                 final int matrixColumnsSpanned) {
        this.cell.setMatrixFormula(formula, matrixRowsSpanned, matrixColumnsSpanned);
    }

    @Override
    public void setStringValue(final String value) {
        this.cell.setStringValue(value);
    }

    @Override
    public void setTimeValue(final long timeInMillis) {
        this.cell.setTimeValue(timeInMillis);
    }

    @Override
    public void setTimeValue(final long years, final long months, final long days, final long hours,
                             final long minutes, final double seconds) {
        this.cell.setTimeValue(years, months, days, hours, minutes, seconds);
    }

    @Override
    public void setNegTimeValue(final long years, final long months, final long days,
                                final long hours, final long minutes, final double seconds) {
        this.cell.setNegTimeValue(years, months, days, hours, minutes, seconds);
    }

    @Override
    public void setTooltip(final String tooltipText) {
        this.cell.setTooltip(tooltipText);
    }

    @Override
    public void setTooltip(final String tooltipText, final Length width, final Length height,
                           final boolean visible) {
        this.cell.setTooltip(tooltipText, width, height, visible);
    }

    @Override
    public void setTooltip(final Tooltip tooltip) {
        this.cell.setTooltip(tooltip);
    }

    @Override
    public void setFormula(final String formula) {
        this.cell.setFormula(formula);

    }

    @Override
    public boolean hasValue() {
        return this.cell.hasValue();
    }

    @Override
    public boolean isCovered() {
        return this.cell.isCovered();
    }

    @Override
    public void setCovered() {
        this.cell.setCovered();
    }

    @Override
    public void setCellValue(final CellValue value) {
        this.cell.setCellValue(value);
    }

    @Override
    public void setCurrencyValue(final float value, final String currency) {
        this.cell.setCurrencyValue(value, currency);
    }

    @Override
    public void setCurrencyValue(final int value, final String currency) {
        this.cell.setCurrencyValue(value, currency);
    }

    @Override
    public void setCurrencyValue(final Number value, final String currency) {
        this.cell.setCurrencyValue(value, currency);
    }

    @Override
    public void setDateValue(final Calendar cal) {
        this.cell.setDateValue(cal);
    }

    @Override
    public void setFloatValue(final float value) {
        this.cell.setFloatValue(value);
    }

    @Override
    public void setFloatValue(final int value) {
        this.cell.setFloatValue(value);
    }

    @Override
    public void setPercentageValue(final float value) {
        this.cell.setPercentageValue(value);
    }

    @Override
    public void setPercentageValue(final int value) {
        this.cell.setPercentageValue(value);
    }

    @Override
    public void setStyle(final TableCellStyle style) {
        this.cell.setStyle(style);
    }

    @Override
    public void setDataStyle(final DataStyle dataStyle) {
        this.cell.setDataStyle(dataStyle);
    }

    @Override
    public void setColumnStyle(final TableColumnStyle columnStyle) {
        this.table.setColumnStyle(this.c, columnStyle);
    }

    @Override
    public void setColumnDefaultCellStyle(final TableCellStyle cellStyle) {
        this.table.setColumnDefaultCellStyle(this.c, cellStyle);
    }

    @Override
    public void setColumnAttribute(final String attribute, final CharSequence value) {
        this.table.setColumnAttribute(this.c, attribute, value);
    }

    @Override
    public void setRowFormat(final DataStyles format) {
        this.row.setRowFormat(format);
    }

    @Override
    public void setRowStyle(final TableRowStyle rowStyle) {
        this.row.setRowStyle(rowStyle);
    }

    @Override
    public int getColumnCount() {
        return this.row.getColumnCount();
    }

    @Override
    public void setRowDefaultCellStyle(final TableCellStyle ts) {
        this.row.setRowDefaultCellStyle(ts);
    }

    @Override
    public int rowIndex() {
        return this.r;
    }

    @Override
    public int colIndex() {
        return this.c;
    }

    @Override
    public void removeRowStyle() {
        this.row.removeRowStyle();
    }

    @Override
    public void setRowAttribute(final String attribute, final CharSequence value) {
        this.row.setRowAttribute(attribute, value);
    }

    @Override
    public boolean hasNext() {
        return this.c < this.row.getColumnCount();
    }

    @Override
    public boolean hasPrevious() {
        return this.c > 0 && this.c <= this.row.getColumnCount();
    }

    @Override
    public void last() {
        this.c = this.row.getColumnCount() - 1;
        this.cell = this.row.getOrCreateCell(this.c);
    }

    @Override
    public void next() {
        this.c++;
        this.cell = this.row.getOrCreateCell(this.c);
    }

    /**
     * Set a custom table cell.
     *
     * @param cell the cell
     */
    @Beta
    public void set(final WritableTableCell cell) {
        this.row.set(this.c, cell);
    }

    @Override
    public void previous() {
        if (this.c <= 0) {
            throw new IndexOutOfBoundsException();
        }
        this.c--;
        this.cell = this.row.getOrCreateCell(this.c);
    }

    @Override
    public void to(final int c) {
        if (c < 0) {
            throw new IndexOutOfBoundsException();
        }
        this.c = c;
        this.cell = this.row.getOrCreateCell(this.c);
    }


    @Override
    public boolean hasNextRow() {
        return this.r < this.table.getRowCount();
    }

    @Override
    public boolean hasPreviousRow() {
        return this.r > 0 && this.r <= this.table.getRowCount();
    }

    @Override
    public void lastRow() throws IOException {
        this.r = this.table.getRowCount() - 1;
        this.c = 0;
        this.updateShortcuts(this.table);
    }

    @Override
    public void nextRow() throws IOException {
        this.r++;
        this.c = 0;
        this.updateShortcuts(this.table);
    }

    @Override
    public void previousRow() throws IOException {
        if (this.r <= 0) {
            throw new IndexOutOfBoundsException();
        }
        this.r--;
        this.c = 0;
        this.updateShortcuts(this.table);
    }

    @Override
    public void toRow(final int r) throws IOException {
        if (r < 0) {
            throw new IndexOutOfBoundsException();
        }
        this.r = r;
        this.c = 0;
        this.updateShortcuts(this.table);
    }

    /**
     * @return the current table for auto-filter etc.
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * Add a wrapped data
     *
     * @param data the wrapped data
     * @throws IOException if an error occurs
     */
    public void addData(final DataWrapper data) throws IOException {
        data.addToTable(this);
    }

    @Override
    public void setAttribute(final String attribute, final CharSequence value) {
        this.cell.setAttribute(attribute, value);
    }
}
