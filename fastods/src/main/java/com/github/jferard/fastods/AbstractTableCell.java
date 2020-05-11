package com.github.jferard.fastods;

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.TableCellStyle;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * An abstract class to ease the implementation of `WritableTableCell`. The method 
 * `appendXMLToTableRow` should be implemented. All other methods will throw a 
 * `UnsupportedOperationException`.
 */
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
}
