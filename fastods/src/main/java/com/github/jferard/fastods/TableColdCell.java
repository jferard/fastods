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
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 9.1.4 table:table-cell
 * <p>
 * A TableColdCell represents the "cold" part of a cell, that means the part that is absent of
 * most of
 * the cells.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class TableColdCell {
    /**
     * @param xmlUtil an util
     * @return the cold cell
     */
    public static TableColdCell create(final XMLUtil xmlUtil) {
        return new TableColdCell(xmlUtil);
    }

    private final XMLUtil xmlUtil;
    private int columnsSpanned;
    private String currency;
    private int rowsSpanned;
    private Text text;
    private Tooltip tooltip;
    private String formula;
    private int matrixRowsSpanned;
    private int matrixColumnsSpanned;

    /**
     * Create an new "cold cell"
     *
     * @param xmlUtil an util
     */
    TableColdCell(final XMLUtil xmlUtil) {
        this.xmlUtil = xmlUtil;
    }

    /**
     * @return the currency (see 19.369 office:currency)
     */
    public String getCurrency() {
        return this.currency;
    }

    /**
     * Set the currency (see 19.369 office:currency)
     *
     * @param currency the currency
     */
    public void setCurrency(final String currency) {
        this.currency = currency; // escape here
    }

    /**
     * 19.676 table:number-columns-spanned
     * Create a span over columns
     *
     * @param n the number of columns > 1
     */
    public void setColumnsSpanned(final int n) {
        this.columnsSpanned = n;
    }

    /**
     * Set a text in the cell
     *
     * @param text the text
     */
    public void setText(final Text text) {
        this.text = text;
    }

    /**
     * Set a tooltip
     *
     * @param tooltipText the content
     */
    public void setTooltip(final String tooltipText) {
        this.tooltip = Tooltip.builder(this.xmlUtil, tooltipText).build();
    }

    /**
     * Set a tooltip of a given size
     *
     * @param tooltipText the content
     * @param width   the width of the tooltip
     * @param height  the height of the tooltip
     * @param visible true if the tooltip is visible
     */
    public void setTooltip(final String tooltipText, final Length width, final Length height,
                           final boolean visible) {
        final TooltipBuilder builder =
                Tooltip.builder(this.xmlUtil, tooltipText).width(width).height(height);
        if (visible) {
            builder.visible();
        }
        this.tooltip = builder.build();
    }

    /**
     * Set a tooltip of a given size
     *
     * @param tooltip the tooltip
     */
    public void setTooltip(final Tooltip tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * @param util       a util class to write XML data
     * @param appendable the object to which data will be appended
     * @throws IOException if data can't be appended
     */
    public void appendXMLToTable(final XMLUtil util, final Appendable appendable)
            throws IOException {

        if (this.formula != null) {
            util.appendEAttribute(appendable, "table:formula", "of:=" + this.formula);
            if (this.matrixRowsSpanned != 0) {
                util.appendAttribute(appendable, "table:number-matrix-rows-spanned",
                        this.matrixRowsSpanned);
            }
            if (this.matrixColumnsSpanned != 0) {
                util.appendAttribute(appendable, "table:number-matrix-columns-spanned",
                        this.matrixColumnsSpanned);
            }
        }

        if (!this.isCovered()) {
            if (this.columnsSpanned != 0) {
                util.appendAttribute(appendable, "table:number-columns-spanned",
                        this.columnsSpanned);
            }
            if (this.rowsSpanned != 0) {
                util.appendAttribute(appendable, "table:number-rows-spanned", this.rowsSpanned);
            }
        }

        if (this.text == null && this.tooltip == null) {
            appendable.append("/>");
        } else { // something between <cell> and </cell>
            appendable.append(">");
            if (this.text != null) {
                this.text.appendXMLContent(util, appendable);
            }
            if (this.tooltip != null) {
                this.tooltip.appendXMLContent(util, appendable);
            }
            if (this.isCovered()) {
                appendable.append("</table:covered-table-cell>");
            } else {
                appendable.append("</table:table-cell>");
            }
        }
    }

    /**
     * 9.1.5 table:covered-table-cell
     *
     * @return true if the cell is covered
     */
    public boolean isCovered() {
        return this.columnsSpanned == -1;
    }

    /**
     * 9.1.5 table:covered-table-cell
     * Set the covered flag on this cell
     */
    public void setCovered() {
        this.columnsSpanned = -1;
    }

    /**
     * 19.642 table:formula
     * Set a formula in this cell
     *
     * @param formula the formula, without = sign
     */
    public void setFormula(final String formula) {
        this.formula = formula;
    }

    /**
     * 19.678 table:number-rows-spanned
     * Create a span over rows
     *
     * @param n the number of rows
     */
    public void setRowsSpanned(final int n) {
        this.rowsSpanned = n;
    }

    public void setMatrixRowsSpanned(final int n) {
        this.matrixRowsSpanned = n;
    }

    public void setMatrixColumnsSpanned(final int n) {
        this.matrixColumnsSpanned = n;
    }
}
