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

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.odselement.ContentElement;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.NamedObject;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.text.ParseException;

/**
 * OpenDocument 9.1.2 table:table
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class Table implements NamedObject {
    /**
     * Create a new Table with a name and a row/column capacity
     *
     *
     * @param contentElement
     * @param positionUtil    an util
     * @param writeUtil       an util
     * @param xmlUtil         an util
     * @param name            the name of the tables
     * @param rowCapacity     the row capacity
     * @param columnCapacity  the column capacity
     * @param stylesContainer the container for styles
     * @param format          the data styles
     * @param libreOfficeMode
     * @return the table
     */
    public static Table create(final ContentElement contentElement, final PositionUtil positionUtil, final WriteUtil writeUtil,
                               final XMLUtil xmlUtil, final String name, final int rowCapacity,
                               final int columnCapacity, final StylesContainer stylesContainer,
                               final DataStyles format, final boolean libreOfficeMode) {
        positionUtil.checkTableName(name);
        final TableBuilder builder = TableBuilder
                .create(positionUtil, writeUtil, xmlUtil, stylesContainer, format, libreOfficeMode, name,
                        rowCapacity, columnCapacity);
        return new Table(name, contentElement, builder);
    }

    private final ContentElement contentElement;
    private final TableBuilder builder;
    private final TableAppender appender;
    private String name;


    /**
     * Create an new table with a given builder
     *  @param name    the name of the table
     * @param contentElement
     * @param builder the builder
     */
    Table(final String name, final ContentElement contentElement, final TableBuilder builder) {
        this.name = name;
        this.contentElement = contentElement;
        this.builder = builder;
        this.appender = new TableAppender(builder);
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

    /**
     * Add an observer to this table
     *
     * @param observer the observer
     */
    public void addObserver(final NamedOdsFileWriter observer) {
        this.builder.addObserver(observer);
    }

    /**
     * Add XML to content.xml
     *
     * @param util       an util
     * @param appendable the output
     * @throws IOException if the XML could not be written
     */
    public void appendXMLToContentEntry(final XMLUtil util, final Appendable appendable)
            throws IOException {
        this.appender.appendXMLToContentEntry(util, appendable);
    }

    /**
     * Flush the XML
     *
     * @throws IOException if an error occurs
     */
    public void flush() throws IOException {
        this.builder.flushBeginTable(this.appender);
        this.builder.flushEndTable(this.appender);
    }

    /**
     * Open the table, flush all rows from start, but do not freeze the table
     *
     * @param util       a XMLUtil instance for writing XML
     * @param appendable where to write
     * @throws IOException if an I/O error occurs during the flush
     */
    public void flushAllAvailableRows(final XMLUtil util, final Appendable appendable)
            throws IOException {
        this.appender.flushAllAvailableRows(util, appendable);
    }

    /**
     * Flush all rows from a given position, and do freeze the table
     *
     * @param util       a XMLUtil instance for writing XML
     * @param appendable where to write
     * @param rowIndex   the first index to use.
     * @throws IOException if an I/O error occurs during the flush
     */
    public void flushRemainingRowsFrom(final XMLUtil util, final Appendable appendable,
                                       final int rowIndex) throws IOException {
        this.appender.flushRemainingRowsFrom(util, appendable, rowIndex);
    }

    /**
     * Flush all rows from a given position, but do not freeze the table
     *
     * @param util       a XMLUtil instance for writing XML
     * @param appendable where to write
     * @param rowIndex   the index of the row
     * @throws IOException if an I/O error occurs during the flush
     */
    public void flushSomeAvailableRowsFrom(final XMLUtil util, final Appendable appendable,
                                           final int rowIndex) throws IOException {
        this.appender.flushSomeAvailableRowsFrom(util, appendable, rowIndex);
    }

    /**
     * @return the config item map of this table
     */
    public ConfigItemMapEntry getConfigEntry() {
        return this.builder.getConfigEntry();
    }

    /**
     * @return the number of the last row (0..)
     */
    public int getLastRowNumber() {
        return this.builder.getLastRowNumber();
    }

    /**
     * Get the name of this table.
     *
     * @return The name of this table.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of this table.
     *
     * @param name The name of this table.
     */
    public void setName(final String name) {
        if (this.appender.isPreambleWritten()) throw new IllegalStateException();

        this.name = name;
    }

    /**
     * Return a row from an index
     *
     * @param rowIndex the index
     * @return the row
     * @throws IllegalArgumentException if the index is invalid
     * @throws IOException      if the row was flushed
     */
    public TableRow getRow(final int rowIndex) throws IOException {
        return this.builder.getRow(this, this.appender, rowIndex);
    }

    /**
     * Get the current TableFamilyStyle
     *
     * @return The current TableStlye
     */
    public String getStyleName() {
        return this.builder.getStyleName();
    }

    /**
     * @return the next row
     * @throws IOException if an error occurs
     */
    public TableRow nextRow() throws IOException {
        return this.builder.nextRow(this, this.appender);
    }

    /**
     * Set a span over cells
     *
     * @param rowIndex    the top row
     * @param colIndex    the leftmost col
     * @param rowMerge    the number of rows
     * @param columnMerge the number of cols
     * @throws IOException if an error occurs
     */
    public void setCellMerge(final int rowIndex, final int colIndex, final int rowMerge,
                             final int columnMerge) throws IOException {
        this.builder.setCellMerge(this, this.appender, rowIndex, colIndex, rowMerge, columnMerge);
    }

    /**
     * Set the merging of multiple cells to one cell.
     *
     * @param address         The cell position e.g. 'A1'
     * @param rowMerge    the number of rows to merge
     * @param columnMerge the number of cells to merge
     * @throws FastOdsException if the row index or the col index is negative
     * @throws IOException      if the cells can't be merged
     */
    @Deprecated
    public void setCellMerge(final String address, final int rowMerge, final int columnMerge)
            throws FastOdsException, IOException, ParseException {
        this.builder.setCellMerge(this, this.appender, address, rowMerge, columnMerge);
    }

    /**
     * Set the style of a column.
     *
     * @param col The column number
     * @param ts  The style to be used, make sure the style is of type
     *            TableFamilyStyle.STYLEFAMILY_TABLECOLUMN
     * @throws FastOdsException Thrown if col has an invalid value.
     */
    public void setColumnStyle(final int col, final TableColumnStyle ts) throws FastOdsException {
        if (this.appender.isPreambleWritten()) throw new IllegalStateException();

        this.builder.setColumnStyle(col, ts);
    }

    /**
     * Set a config item
     *
     * @param name  the item name
     * @param type  the item type
     * @param value the item value
     */
    public void setConfigItem(final String name, final String type, final String value) {
        this.builder.setConfigItem(name, type, value);
    }

    /**
     * Set one of the settings
     *
     * @param viewId the id of the view
     * @param item   the item name
     * @param value  the item value
     */
    public void setSettings(final String viewId, final String item, final String value) {
        this.builder.updateConfigItem(item, value);
    }

    /**
     * Set a new TableFamilyStyle
     *
     * @param style The new TableStyle to be used
     */
    public void setStyle(final TableStyle style) {
        this.builder.setStyle(style);
    }

    /**
     * Set a span over rows
     *
     * @param rowIndex the row index
     * @param colIndex the col index
     * @param n        the number of rows
     * @throws IOException if an error occurs
     */
    public void setRowsSpanned(final int rowIndex, final int colIndex, final int n)
            throws IOException {
        this.builder.setRowsSpanned(this, this.appender, rowIndex, colIndex, n);
    }

    /**
     * Find the default cell style for a column
     *
     * @param columnIndex the column index
     * @return the style, never null
     */
    public TableCellStyle findDefaultCellStyle(final int columnIndex) {
        return this.builder.findDefaultCellStyle(columnIndex);
    }

    public void addAutoFilter(final int r1, final int c1, final int r2, final int c2) {
        this.contentElement.addAutoFilter(this, r1, c1, r2, c2);
    }
}