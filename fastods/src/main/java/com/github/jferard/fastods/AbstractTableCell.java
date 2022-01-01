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

package com.github.jferard.fastods;

import com.github.jferard.fastods.annotation.Beta;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Validation;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * An abstract class to ease the implementation of `WritableTableCell`. The method 
 * `appendXMLToTableRow` should be implemented. All other methods will throw a 
 * `UnsupportedOperationException` unless they are overriden.
 */
@Beta
public abstract class AbstractTableCell implements WritableTableCell {
    @Override
    public void markRowsSpanned(final int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBooleanValue(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellValue(final CellValue value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrencyValue(final float value, final String currency) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrencyValue(final int value, final String currency) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrencyValue(final Number value, final String currency) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDateValue(final Calendar cal) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDateValue(final Date date) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFloatValue(final float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFloatValue(final int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFloatValue(final Number value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPercentageValue(final float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPercentageValue(final int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPercentageValue(final Number value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStringValue(final String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStyle(final TableCellStyle style) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTimeValue(final long timeInMillis) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTimeValue(final long years, final long months, final long days, final long hours, final long minutes,
                             final double seconds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNegTimeValue(final long years, final long months, final long days, final long hours, final long minutes,
                                final double seconds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTooltip(final String tooltipText) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTooltip(final String tooltipText, final Length width, final Length height, final boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTooltip(final Tooltip tooltip) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFormula(final String formula) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCovered() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCovered() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColumnsSpanned(final int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void markColumnsSpanned(final int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowsSpanned(final int n) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVoidValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMatrixFormula(final String formula) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMatrixFormula(final String formula, final int matrixRowsSpanned, final int matrixColumnsSpanned) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public void setText(final Text text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellMerge(final int rowMerge, final int columnMerge) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDataStyle(final DataStyle dataStyle) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int colIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(final String attribute, final CharSequence value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValidation(final Validation validation) {
        throw new UnsupportedOperationException();
    }
}
