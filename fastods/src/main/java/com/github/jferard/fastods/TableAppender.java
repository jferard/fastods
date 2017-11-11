/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * OpenDocument 9.1.2 table:table
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class TableAppender {
    private static final int MAX_COLUMN_COUNT = 1024;
    private final TableBuilder builder;
    private boolean preambleWritten;
    private int nullFieldCounter;

    TableAppender(final TableBuilder builder) {
        this.preambleWritten = false;
        this.builder = builder;
    }

    public void appendPostamble(final Appendable appendable) throws IOException {
        appendable.append("</table:table>");
    }

    public void appendPreamble(final XMLUtil util, final Appendable appendable) throws IOException {
        if (this.preambleWritten)
            return;

        appendable.append("<table:table");
        util.appendAttribute(appendable, "table:name", this.builder.getName());
        util.appendAttribute(appendable, "table:style-name",
                this.builder.getStyleName());
        util.appendEAttribute(appendable, "table:print", false);
        appendable.append("><office:forms");
        util.appendEAttribute(appendable, "form:automatic-focus", false);
        util.appendEAttribute(appendable, "form:apply-design-mode", false);
        appendable.append("/>");
        this.appendColumnStyles(this.builder.getColumnStyles(), appendable, util);
        this.preambleWritten = true;
    }

    public void appendXMLToContentEntry(final XMLUtil util,
                                        final Appendable appendable) throws IOException {
        this.appendPreamble(util, appendable);
        this.appendRows(util, appendable);
        this.appendPostamble(appendable);
    }

    /**
     * Open the table, flush all rows from start, but do not freeze the table
     *
     * @param util       a XMLUtil instance for writing XML
     * @param appendable where to write
     * @throws IOException if an I/O error occurs during the flush
     */
    public void flushAllAvailableRows(final XMLUtil util, final Appendable appendable) throws IOException {
        this.appendPreamble(util, appendable);
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
    public void flushRemainingRowsFrom(final XMLUtil util, final Appendable appendable, final int rowIndex)
            throws IOException {
        if (rowIndex == 0)
            this.appendPreamble(util, appendable);
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
    public void flushSomeAvailableRowsFrom(final XMLUtil util, final Appendable appendable, final int rowIndex)
            throws IOException {
        if (rowIndex == 0)
            this.appendPreamble(util, appendable);
        this.appendRows(util, appendable, rowIndex);
    }

    private void appendColumnStyles(final List<TableColumnStyle> columnStyles, final Appendable appendable,
                                    final XMLUtil xmlUtil) throws IOException {
        final Iterator<TableColumnStyle> iterator = columnStyles.iterator();
        if (!iterator.hasNext()) {
            TableColumnStyle.DEFAULT_TABLE_COLUMN_STYLE
                    .appendXMLToTable(xmlUtil, appendable, MAX_COLUMN_COUNT);
            return;
        }

        TableColumnStyle ts0 = TableColumnStyle.DEFAULT_TABLE_COLUMN_STYLE;
        int count = 1;
        int globalCount = 0;
        TableColumnStyle ts1 = iterator.next();
        while (iterator.hasNext()) {
            ts0 = ts1;
            ts1 = iterator.next();

            if (ts0.equals(ts1)) {
                count++;
            } else {
                ts0.appendXMLToTable(xmlUtil, appendable, count);
                globalCount += count;
                count = 1;
            }

        }
        ts1.appendXMLToTable(xmlUtil, appendable, count);
        globalCount += count;
        TableColumnStyle.DEFAULT_TABLE_COLUMN_STYLE
                .appendXMLToTable(xmlUtil, appendable, MAX_COLUMN_COUNT-globalCount);
    }

    private void appendRows(final XMLUtil util, final Appendable appendable)
            throws IOException {
        this.appendRows(util, appendable, 0);
    }

    private void appendRows(final XMLUtil util, final Appendable appendable, final int firstRowIndex)
            throws IOException {
        if (firstRowIndex == 0)
            this.nullFieldCounter = 0;

        final int size = this.builder.getTableRowsUsedSize();
        for (int r = firstRowIndex; r < size; r++) {
            final TableRow tr = this.builder.getTableRow(r);
            if (tr == null) {
                this.nullFieldCounter++;
            } else {
                this.appendRepeatedRows(util, appendable);
                tr.appendXMLToTable(util, appendable);
                this.nullFieldCounter = 0;
            }
        }
    }

    private void appendRepeatedRows(final XMLUtil util, final Appendable appendable) throws IOException {
        if (this.nullFieldCounter <= 0)
            return;

        appendable.append("<table:table-row");
        if (this.nullFieldCounter > 1) {
            util.appendEAttribute(appendable,
                    "table:number-rows-repeated", this.nullFieldCounter);
        }
        util.appendEAttribute(appendable, "table:style-name",
                "ro1");
        appendable.append("><table:table-cell/></table:table-row>");
        this.nullFieldCounter = 0;
    }

    public boolean isPreambleWritten() {
        return this.preambleWritten;
    }
}
