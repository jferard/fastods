/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.odselement.ContentElement;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.config.ConfigElement;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.NamedObject;
import com.github.jferard.fastods.util.IntegerRepresentationCache;
import com.github.jferard.fastods.util.Protection;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Arrays;

/**
 * OpenDocument 9.1.2 table:table
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class Table implements NamedObject, FrameContent {
    /**
     * Create a new Table with a name and a row/column capacity
     *
     * @param contentElement  the content.xml representation
     * @param positionUtil    an util
     * @param cache       an util
     * @param xmlUtil         an util
     * @param name            the name of the tables
     * @param rowCapacity     the row capacity
     * @param columnCapacity  the column capacity
     * @param stylesContainer the container for styles
     * @param format          the data styles
     * @param libreOfficeMode try to get full compatibility with LO if true
     * @param validationsContainer the validations container
     * @return the table
     */
    public static Table create(final ContentElement contentElement, final PositionUtil positionUtil,
                               final IntegerRepresentationCache cache, final XMLUtil xmlUtil,
                               final String name,
                               final int rowCapacity, final int columnCapacity,
                               final StylesContainer stylesContainer, final DataStyles format,
                               final boolean libreOfficeMode,
                               final ValidationsContainer validationsContainer) {
        positionUtil.checkTableName(name);
        final TableBuilder builder = TableBuilder
                .create(positionUtil, cache, xmlUtil, stylesContainer, format, libreOfficeMode,
                        name, rowCapacity, columnCapacity, validationsContainer);
        return new Table(name, contentElement, builder, new TableAppender(builder));
    }

    private final ContentElement contentElement;
    private final TableBuilder builder;
    private final TableAppender appender;
    private final String name;


    /**
     * Create an new table with a given builder
     *
     * @param name           the name of the table
     * @param contentElement the content.xml representation
     * @param builder        the builder
     * @param tableAppender
     */
    Table(final String name, final ContentElement contentElement, final TableBuilder builder,
          final TableAppender tableAppender) {
        this.name = name;
        this.contentElement = contentElement;
        this.builder = builder;
        this.appender = tableAppender;
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
    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        this.appender.appendXMLToContentEntry(util, appendable);
    }

    /**
     * Async flush the XML
     *
     * @throws IOException if an error occurs
     * @deprecated use asyncFlushBeginTable, asyncFlushAvailableRows or asyncFlushEndTable
     */
    @Deprecated
    public void asyncFlush() throws IOException {
        this.builder.asyncFlushBeginTable(this.appender);
        this.builder.asyncFlushEndTable(this.appender);
    }

    /**
     * Async flush the XML
     *
     * @throws IOException if an error occurs
     */
    public void asyncFlushBeginTable() throws IOException {
        this.builder.asyncFlushBeginTable(this.appender);
    }

    /**
     * Async flush the XML
     *
     * @throws IOException if an error occurs
     */
    public void asyncFlushEndTable() throws IOException {
        this.builder.asyncFlushEndTable(this.appender);
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
        this.appender.appendAllAvailableRows(util, appendable);
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
        this.appender.appendRemainingRowsFrom(util, appendable, rowIndex);
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
        this.appender.appendSomeAvailableRowsFrom(util, appendable, rowIndex);
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
    public int getRowCount() {
        return this.builder.getRowCount();
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
     * Return a row from an index
     *
     * @param rowIndex the index
     * @return the row
     * @throws IllegalArgumentException if the index is invalid
     * @throws IOException              if the row was flushed
     */
    public TableRowImpl getRow(final int rowIndex) throws IOException {
        return this.builder.getRow(this, this.appender, rowIndex);
    }

    /**
     * Get the current Table Style
     *
     * @return The current TableStyle
     */
    public String getStyleName() {
        return this.builder.getStyleName();
    }

    /**
     * @return the next row
     * @throws IOException if an error occurs
     */
    @Deprecated
    public TableRowImpl nextRow() throws IOException {
        return this.builder.nextRow(this, this.appender);
    }

    /**
     * @return a CellWalker on the row
     * @throws IOException if the first row was flushed
     */
    public TableCellWalker getWalker() throws IOException {
        return new TableCellWalker(this);
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
     * @param address     The cell position e.g. 'A1'
     * @param rowMerge    the number of rows to merge
     * @param columnMerge the number of cells to merge
     * @throws IOException    if the cells can't be merged
     * @throws ParseException if the address can't be parsed
     */
    @Deprecated
    public void setCellMerge(final String address, final int rowMerge, final int columnMerge)
            throws IOException, ParseException {
        this.builder.setCellMerge(this, this.appender, address, rowMerge, columnMerge);
    }

    /**
     * Set the style of a column.
     *
     * @param col The column number
     * @param ts  The style to be used
     * @throws IllegalStateException    if the preamble was already written
     * @throws IllegalArgumentException if col has an invalid value.
     */
    public void setColumnStyle(final int col, final TableColumnStyle ts) {
        this.builder.setColumnStyle(col, ts);
    }

    /**
     * Set a custom attribute for a column
     * @param col the column
     * @param attribute the attribute
     * @param value the value
     */
    public void setColumnAttribute(final int col, final String attribute,
                                   final CharSequence value) {
        this.builder.setColumnAttribute(col, attribute, value);
    }

    public void setColumnDefaultCellStyle(final int col, final TableCellStyle cellStyle) {
        this.builder.setColumnDefaultCellStyle(col, cellStyle);
    }

    /**
     * Set a custom attribute
     * @param attribute the attribute
     * @param value the value
     */
    public void setAttribute(final String attribute, final CharSequence value) {
        this.builder.setAttribute(attribute, value);
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
     * @param element the config element
     * @param value   the item value
     */
    public void updateConfigItem(final ConfigElement element, final String value) {
        this.builder.updateConfigItem(element.getName(), value);
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

    /**
     * Add a new auto filter
     *
     * @param rangeName the name of the range
     * @param r1        first row of the range
     * @param c1        first col of the range
     * @param r2        last row
     * @param c2        last col
     */
    public void addAutoFilter(final String rangeName, final int r1, final int c1, final int r2,
                              final int c2) {
        this.contentElement
                .addAutoFilter(AutoFilter.builder(rangeName, this, r1, c1, r2, c2).build());
    }

    /**
     * Add a shape
     * @param shape the shape
     */
    public void addShape(final Shape shape) {
        this.builder.addShape(shape);
    }

    /**
     * @param protection the protection
     * @throws NoSuchAlgorithmException should not happen
     */
    public void protect(final Protection protection) throws NoSuchAlgorithmException {
        this.builder.protect(protection);
    }

    public void addPrintRange(final int r1, final int c1, final int r2, final int c2) {
        this.builder.addPrintRange(r1, c1, r2, c2);
    }

    public void setHeaderRowsCount(final int headerRowsCount) {
        this.builder.setHeaderRowsCount(headerRowsCount);
    }

    public void setHeaderColumnsCount(final int headerColumnsCount) {
        this.builder.setHeaderColumnsCount(headerColumnsCount);
    }
}