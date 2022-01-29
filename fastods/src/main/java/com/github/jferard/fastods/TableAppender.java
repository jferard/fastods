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

import com.github.jferard.fastods.util.Protection;
import com.github.jferard.fastods.util.StringUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * OpenDocument 9.1.2 table:table
 * <p>
 * Provides methods to flush rows from TableModel (ie. TableRow objects) to a given appendable.
 * <p>
 * Users will either call `appendXMLToContentEntry` to convert the whole TableModel, or have
 * a finer control with appendPreamble, appendAllAvailableRows, ...
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class TableAppender {
    private final TableModel model;
    private boolean preambleWritten;
    private int nullFieldCounter;
    private boolean atLeastOneRow;

    /**
     * Create a new appender
     *
     * @param model the table model
     */
    TableAppender(final TableModel model) {
        this.preambleWritten = false;
        this.model = model;
        this.atLeastOneRow = false;
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
        this.appendOpenTagAndPreamble(util, appendable);
        this.appendRows(util, appendable);
        this.appendPostamble(appendable);
    }

    /**
     * Append the preamble
     *
     * @param util       the XMLUtil instance
     * @param appendable where to append
     * @throws IOException if an I/O error occurs
     */
    public void appendOpenTagAndPreamble(final XMLUtil util, final Appendable appendable) throws IOException {
        this.appendTableOpenTag(util, appendable);
        final PreambleAppender preambleAppender = new PreambleAppender(this.model);
        preambleAppender.appendForms(util, appendable);
        preambleAppender.appendShapes(util, appendable);
        preambleAppender.appendColumns(util, appendable);
    }

    private void appendTableOpenTag(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:table");
        util.appendEAttribute(appendable, "table:name", this.model.getName());
        util.appendEAttribute(appendable, "table:style-name", this.model.getStyleName());
        util.appendAttribute(appendable, "table:print", false);
        final List<String> printRanges = this.model.getPrintRanges();
        if (!printRanges.isEmpty()) {
            final String printRangesAddresses = StringUtil.join(" ", printRanges);
            util.appendEAttribute(appendable, "table:print-ranges", printRangesAddresses);
        }
        final Protection protection = this.model.getProtection();
        if (protection != null) {
            protection.appendAttributes(util, appendable);
        }
        final Map<String, CharSequence> customValueByAttribute =
                this.model.getCustomValueByAttribute();
        if (customValueByAttribute != null) {
            for (final Map.Entry<String, CharSequence> entry : customValueByAttribute.entrySet()) {
                util.appendAttribute(appendable, entry.getKey(), entry.getValue());
            }
        }
        appendable.append(">");
    }

    private void appendRows(final XMLUtil util, final Appendable appendable) throws IOException {
        this.appendRows(util, appendable, 0);
    }

    private void appendRows(final XMLUtil util, final Appendable appendable,
                            final int firstRowIndex) throws IOException {
        final int headerRowsCount = this.model.getHeaderRowsCount();
        if (headerRowsCount == 0) {
            this.appendRowsWithoutHeaderRows(util, appendable, firstRowIndex);
        } else {
            if (firstRowIndex == 0) {
                appendable.append("<table:table-header-rows>");
            }
            if (firstRowIndex < headerRowsCount) {
                this.appendRowsWithHeaderRows(util, appendable, firstRowIndex, headerRowsCount);
            } else if (firstRowIndex == headerRowsCount) {
                appendable.append("</table:table-header-rows>");
                this.appendRowsWithoutHeaderRows(util, appendable, firstRowIndex);
            } else {
                this.appendRowsWithoutHeaderRows(util, appendable, firstRowIndex);
            }
        }
    }

    private void appendRowsWithoutHeaderRows(final XMLUtil util, final Appendable appendable,
                                             final int firstRowIndex) throws IOException {
        final int size = this.model.getTableRowsUsedSize();
        if (firstRowIndex == 0) {
            this.nullFieldCounter = 0;
        }

        for (int r = firstRowIndex; r < size; r++) {
            final TableRowImpl tr = this.model.getTableRow(r);
            if (tr == null) { // we don't append null rows immediately
                this.nullFieldCounter++;
            } else {
                this.flushNullRows(util, appendable); // but wait for a non null row
                tr.appendXMLToTable(util, appendable);
                this.atLeastOneRow = true;
                this.nullFieldCounter = 0;
            }
        }
        // forget the remaining null rows
    }

    private void appendRowsWithHeaderRows(final XMLUtil util, final Appendable appendable,
                                          final int firstRowIndex, final int headerRowsCount) throws IOException {
        final int size = this.model.getTableRowsUsedSize();
        if (firstRowIndex == 0) {
            this.nullFieldCounter = 0;
        }

        for (int r = firstRowIndex; r < size; r++) {
            final TableRowImpl tr = this.model.getTableRow(r);
            if (r == headerRowsCount) {
                this.flushNullRows(util, appendable);
                appendable.append("</table:table-header-rows>");
                if (tr == null) {
                    this.nullFieldCounter = 1;
                } else {
                    tr.appendXMLToTable(util, appendable);
                    this.nullFieldCounter = 0;
                }
                this.atLeastOneRow = true;
            } else if (tr == null) {
                this.nullFieldCounter++;
            } else {
                this.flushNullRows(util, appendable);
                tr.appendXMLToTable(util, appendable);
                this.atLeastOneRow = true;
                this.nullFieldCounter = 0;
            }
        }
    }

    private void flushNullRows(final XMLUtil util, final Appendable appendable)
            throws IOException {
        if (this.nullFieldCounter <= 0) {
            return;
        }

        appendable.append("<table:table-row");
        if (this.nullFieldCounter > 1) {
            util.appendAttribute(appendable, "table:number-rows-repeated", this.nullFieldCounter);
        }
        util.appendAttribute(appendable, "table:style-name", "ro1");
        appendable.append(">");
        appendable.append("<table:table-cell/>");
        appendable.append("</table:table-row>");
        this.atLeastOneRow = true;
        this.nullFieldCounter = 0;
    }

    /**
     * Append the postamble
     *
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void appendPostamble(final Appendable appendable) throws IOException {
        if (!this.atLeastOneRow) {
            appendable.append("<table:table-row><table:table-cell/></table:table-row>");
        }
        appendable.append("</table:table>");
    }

    /* ***************** */
    /* For ASYNC version */
    /* ***************** */

    /**
     * Append the preamble only once. This method is idempotent.
     *
     * @param util       an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void appendOpenTagAndPreambleOnce(final XMLUtil util, final Appendable appendable)
            throws IOException {
        if (!this.preambleWritten) {
            this.appendOpenTagAndPreamble(util, appendable);
            this.preambleWritten = true;
        }
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
            this.appendOpenTagAndPreamble(util, appendable);
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
            this.appendOpenTagAndPreamble(util, appendable);
        }
        this.appendRows(util, appendable, rowIndex);
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
        this.appendOpenTagAndPreambleOnce(util, appendable);
        this.appendRows(util, appendable, 0);
    }

    /**
     * Flush a bunch of rows
     *
     * @param rows the rows
     * @throws IOException if an I/O error occurs
     */
    public void flushRows(final XMLUtil xmlUtil, final ZipUTF8Writer writer,
                          final List<TableRowImpl> rows) throws IOException {
        for (final TableRowImpl row : rows) {
            TableRowImpl.appendXMLToTable(row, xmlUtil, writer);
            this.atLeastOneRow = true;
        }
        // free rows
        Collections.fill(rows, null);
    }
}
