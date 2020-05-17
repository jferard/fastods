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

import com.github.jferard.fastods.util.FastFullList;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * OpenDocument 9.1.2 table:table
 * <p>
 * Provides methods to flush rows from TableBuilder (ie. TableRow objects) to a given appendable.
 * <p>
 * Users will either call `appendXMLToContentEntry` to convert the whole TableBuilder, or have
 * a finer control with appendPreamble, appendAllAvailableRows, ...
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class TableAppender {
    private static final int MAX_COLUMN_COUNT = 1024;
    private final TableBuilder builder;
    private boolean preambleWritten;
    private int nullFieldCounter;

    /**
     * Create a new appender
     *
     * @param builder the table builder
     */
    TableAppender(final TableBuilder builder) {
        this.preambleWritten = false;
        this.builder = builder;
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
        this.appendPreamble(util, appendable);
        this.appendRows(util, appendable);
        this.appendPostamble(appendable);
    }

    /**
     * Append the postamble
     *
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void appendPostamble(final Appendable appendable) throws IOException {
        appendable.append("</table:table>");
    }

    /**
     * Append the preamble
     *
     * @param util       an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void appendPreambleOnce(final XMLUtil util, final Appendable appendable)
            throws IOException {
        if (!this.preambleWritten) {
            this.appendPreamble(util, appendable);
            this.preambleWritten = true;
        }
    }

    public void appendPreamble(final XMLUtil util, final Appendable appendable) throws IOException {
        appendable.append("<table:table");
        util.appendEAttribute(appendable, "table:name", this.builder.getName());
        util.appendEAttribute(appendable, "table:style-name", this.builder.getStyleName());
        util.appendAttribute(appendable, "table:print", false);
        final Map<String, CharSequence> customValueByAttribute = this.builder.getCustomValueByAttribute();
        if (customValueByAttribute != null) {
            for (final Map.Entry<String, CharSequence> entry : customValueByAttribute.entrySet()) {
                util.appendAttribute(appendable, entry.getKey(), entry.getValue());
            }
        }
        appendable.append(">");
        this.appendForms(util, appendable);
        this.appendColumns(util, appendable, this.builder.getColumns());
        this.appendShapes(util, appendable, this.builder.getShapes());
    }

    private void appendShapes(final XMLUtil util, final Appendable appendable,
                              final List<Shape> shapes) throws IOException {
        if (shapes.isEmpty()) {
            return;
        }

        appendable.append("<table:shapes>");
        for (final Shape shape : shapes) {
            shape.appendXMLContent(util, appendable);
        }
        appendable.append("</table:shapes>");
    }

    private void appendForms(final XMLUtil util, final Appendable appendable) throws IOException {
        appendable.append("<office:forms");
        util.appendAttribute(appendable, "form:automatic-focus", false);
        util.appendAttribute(appendable, "form:apply-design-mode", false);
        appendable.append("/>");
    }

    /**
     * Open the table, flush all rows from start, but do not freeze the table
     *
     * @param util       a XMLUtil instance for writing XML
     * @param appendable where to write
     * @throws IOException if an I/O error occurs during the flush
     */
    public void appendAllAvailableRows(final XMLUtil util, final Appendable appendable)
            throws IOException {
        this.appendPreambleOnce(util, appendable);
        this.appendRows(util, appendable, 0);
    }

    /**
     * Flush all rows from a given position, and do freeze the table
     *
     * @param util       a XMLUtil instance for writing XML
     * @param appendable where to write
     * @param rowIndex   the first index to use.
     * @throws IOException if an I/O error occurs during the flush
     */
    public void appendRemainingRowsFrom(final XMLUtil util, final Appendable appendable,
                                        final int rowIndex) throws IOException {
        if (rowIndex == 0) {
            this.appendPreamble(util, appendable);
        }
        this.appendRows(util, appendable, rowIndex);
        this.appendPostamble(appendable);
    }

    /**
     * Flush all rows from a given position, but do not freeze the table
     *
     * @param util       a XMLUtil instance for writing XML
     * @param appendable where to write
     * @param rowIndex   the index of the row
     * @throws IOException if an I/O error occurs during the flush
     */
    public void appendSomeAvailableRowsFrom(final XMLUtil util, final Appendable appendable,
                                            final int rowIndex) throws IOException {
        if (rowIndex == 0) {
            this.appendPreamble(util, appendable);
        }
        this.appendRows(util, appendable, rowIndex);
    }

    private void appendColumns(final XMLUtil xmlUtil, final Appendable appendable,
                               final FastFullList<TableColumnImpl> tableColumns)
            throws IOException {
        final Iterator<TableColumnImpl> iterator = tableColumns.iterator();
        if (!iterator.hasNext()) {
            TableColumnImpl.DEFAULT_TABLE_COLUMN
                    .appendXMLToTable(xmlUtil, appendable, MAX_COLUMN_COUNT);
            return;
        }

        int count = 1;
        int endCount = MAX_COLUMN_COUNT;
        TableColumnImpl curColumn = iterator.next(); // will be shifted to prevTCS
        while (iterator.hasNext()) {
            final TableColumnImpl prevColumn = curColumn;
            curColumn = iterator.next();

            if (curColumn.equals(prevColumn)) {
                count++;
            } else {
                prevColumn.appendXMLToTable(xmlUtil, appendable, count);
                endCount -= count;
                count = 1;
            }

        }
        curColumn.appendXMLToTable(xmlUtil, appendable, count);
        endCount -= count;
        TableColumnImpl.DEFAULT_TABLE_COLUMN.appendXMLToTable(xmlUtil, appendable, endCount);
    }

    private void appendRows(final XMLUtil util, final Appendable appendable) throws IOException {
        this.appendRows(util, appendable, 0);
    }

    private void appendRows(final XMLUtil util, final Appendable appendable,
                            final int firstRowIndex) throws IOException {
        if (firstRowIndex == 0) {
            this.nullFieldCounter = 0;
        }

        final int size = this.builder.getTableRowsUsedSize();
        for (int r = firstRowIndex; r < size; r++) {
            final TableRowImpl tr = this.builder.getTableRow(r);
            if (tr == null) {
                this.nullFieldCounter++;
            } else {
                this.appendRepeatedRows(util, appendable);
                tr.appendXMLToTable(util, appendable);
                this.nullFieldCounter = 0;
            }
        }
    }

    private void appendRepeatedRows(final XMLUtil util, final Appendable appendable)
            throws IOException {
        if (this.nullFieldCounter <= 0) {
            return;
        }

        appendable.append("<table:table-row");
        if (this.nullFieldCounter > 1) {
            util.appendAttribute(appendable, "table:number-rows-repeated", this.nullFieldCounter);
        }
        util.appendAttribute(appendable, "table:style-name", "ro1");
        appendable.append("><table:table-cell/></table:table-row>");
        this.nullFieldCounter = 0;
    }

    /**
     * @return true if the preamble was written
     */
    public boolean isPreambleWritten() {
        return this.preambleWritten;
    }
}
