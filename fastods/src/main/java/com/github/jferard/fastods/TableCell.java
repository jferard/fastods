/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.TableCellStyle;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * A TableCell represents a cell in a spreadsheet sheet.
 *
 * @author Julien Férard
 */
public interface TableCell {
    /**
     * Marks a number of rows with a span
     *
     * @param n the number of rows
     */
    void markRowsSpanned(int n);

    /**
     * Set the boolean value
     *
     * @param value true or false
     */
    void setBooleanValue(boolean value);

    /**
     * Set the float value for a cell with TableCell.Type.STRING.
     *
     * @param value the value as a CellValue object.
     */
    void setCellValue(CellValue value);

    /**
     * Set the currency value and table cell style to STYLE_CURRENCY.
     *
     * @param value    the value as a float
     * @param currency The currency value
     */
    void setCurrencyValue(float value, String currency);

    /**
     * Set the currency value and table cell style to STYLE_CURRENCY.
     *
     * @param value    the value as an int
     * @param currency The currency value
     */
    void setCurrencyValue(int value, String currency);

    /**
     * Set the currency value and table cell style to STYLE_CURRENCY.
     *
     * @param value    the value as a Number
     * @param currency the currency value
     */
    void setCurrencyValue(Number value, String currency);

    /**
     * Set the date value for a cell with TableCell.STYLE_DATE.
     *
     * @param cal a Calendar object with the date to be used
     */
    void setDateValue(Calendar cal);

    /**
     * Set the date value for a cell with TableCell.STYLE_DATE.
     *
     * @param date a Date object
     */
    void setDateValue(Date date);

    /**
     * Set the float value for a cell with TableCell.Type.FLOAT.
     *
     * @param value a double object with the value to be used
     */
    void setFloatValue(float value);

    /**
     * Set the float value for a cell with TableCell.Type.FLOAT.
     *
     * @param value a double object with the value to be used
     */
    void setFloatValue(int value);

    /**
     * Set the float value for a cell with TableCell.Type.FLOAT.
     *
     * @param value a double object with the value to be used
     */
    void setFloatValue(Number value);

    /**
     * Set the float value for a cell with TableCell.Type.PERCENTAGE.
     *
     * @param value a float object with the value to be used
     */
    void setPercentageValue(float value);

    /**
     * Set the int value for a cell with TableCell.Type.PERCENTAGE.
     *
     * @param value an int with the value to be used
     */
    void setPercentageValue(int value);

    /**
     * Set the float value for a cell with TableCell.Type.PERCENTAGE.
     *
     * @param value a double object with the value to be used
     */
    void setPercentageValue(Number value);

    /**
     * Set the float value for a cell with TableCell.Type.STRING.
     * <p>
     * Note that this will not set the data style of the cell to "text" but let it to "standard",
     * hence the quote before numbers in LO. This is the same behavior
     * as LO (see https://github.com/jferard/fastods/issues/148)
     *
     * @param value a double object with the value to be used
     */
    void setStringValue(String value);

    /**
     * Set a style for this cell
     *
     * @param style the style
     */
    void setStyle(TableCellStyle style);

    /**
     * Set the time value as in 19.382 office:time-value. The xml datatype is "duration"
     * (https://www.w3.org/TR/xmlschema-2/#duration)
     *
     * @param timeInMillis the duration in milliseconds
     */
    void setTimeValue(long timeInMillis);

    /**
     * Set the time value as in 19.382 office:time-value. The xml datatype is "duration"
     * (https://www.w3.org/TR/xmlschema-2/#duration)
     * All parameters must be positive
     *
     * @param years   number of years
     * @param months  number of months
     * @param days    number of days
     * @param hours   number of hours
     * @param minutes number of minutes
     * @param seconds number of seconds
     */
    void setTimeValue(long years, long months, long days, long hours, long minutes, double seconds);

    /**
     * Set the time value as in 19.382 office:time-value. The xml datatype is "duration"
     * (https://www.w3.org/TR/xmlschema-2/#duration)
     * All parameters must be positive
     *
     * @param years   number of years
     * @param months  number of months
     * @param days    number of days
     * @param hours   number of hours
     * @param minutes number of minutes
     * @param seconds number of seconds
     */
    void setNegTimeValue(long years, long months, long days, long hours, long minutes,
                         double seconds);

    /**
     * Add a tooltip to the cell
     *
     * @param tooltipText the text of the tooltip
     */
    void setTooltip(String tooltipText);

    /**
     * Add a tooltip to the cell
     *
     * @param tooltipText the text of the tooltip
     * @param width       the width of the tooltip
     * @param height      the height of the tooltip
     * @param visible     if the tooltip should be visible.
     */
    void setTooltip(String tooltipText, Length width, Length height, boolean visible);

    /**
     * Add a tooltip to the cell
     *
     * @param tooltip the tooltip
     */
    void setTooltip(final Tooltip tooltip);

    /**
     * Sets a formula in an existing cell. The user is responsible for creating the cell and
     * setting the
     * correct value, as show below:
     * <pre>{@code
     *     walker.setFloatValue(2.0);
     *     walker.setFormula("1+1");
     * }</pre>
     * <p>
     * One can type Shift+Ctrl+F9 to recalculate the right value in LibreOffice.
     *
     * @param formula the formula, without '=' sign.
     */
    void setFormula(String formula);

    /**
     * @return true if the cell is covered by a span
     */
    boolean isCovered();

    /**
     * Set the cell covered flag
     */
    void setCovered();

    /**
     * Create a span over cells at the right
     *
     * @param n the number of cells to be spanned
     * @throws IllegalArgumentException if n < 0
     */
    void setColumnsSpanned(int n);

    /**
     * Mark the columns a spanned
     *
     * @param n the number of columns
     * @throws IllegalArgumentException if n < 0
     */
    void markColumnsSpanned(int n);

    /**
     * Create a span over cells below
     *
     * @param n the number of cells to be spanned
     * @throws IOException              if the cell can't be merged (only when flushing data)
     * @throws IllegalArgumentException if n < 0
     */
    void setRowsSpanned(int n) throws IOException;

    /**
     * Set a void value in this cell
     */
    void setVoidValue();

    /**
     * Set a matrix formula
     */
    void setMatrixFormula(String formula);

    /**
     * Set a matrix formula
     */
    void setMatrixFormula(String formula, int matrixRowsSpanned, int matrixColumnsSpanned);

    /**
     * @return true if the cell has a value. A void value is a value
     */
    boolean hasValue();

    /**
     * Set a text in this cell
     *
     * @param text the text
     */
    void setText(Text text);

    /**
     * Merge cells
     *
     * @param rowMerge    number of rows below
     * @param columnMerge number of rows at the right
     * @throws IOException if the cell can't be merged (only when flushing data)
     */
    void setCellMerge(int rowMerge, int columnMerge) throws IOException;

    /**
     * Add an attribute to a cell
     * @param attribute the attribute name
     * @param value the value
     */
    void setAttribute(String attribute, CharSequence value);

    /**
     * Set a custom data style. In an Open Document, a data style is always carried by a style.
     * Thus, FastOds will create a new style, child of the current style, with the given data style.
     * The new style will have the same visibility as the data style.
     *
     * @param dataStyle the data style
     */
    void setDataStyle(DataStyle dataStyle);

    /**
     * @return the index of the cell in the current row
     */
    int colIndex();
}