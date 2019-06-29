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

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * WHERE ? content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-row
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableCellImpl implements TableCell {
    /**
     * The default date format
     */
    final static SimpleDateFormat DATE_VALUE_FORMAT;

    static {
        /*
         * XML Schema Part 2, 3.2.7 dateTime
         * Z and UTC time zone for universal time.
         */
        DATE_VALUE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DATE_VALUE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private final TableRow parent;
    private final WriteUtil writeUtil;
    private final XMLUtil xmlUtil;
    private final StylesContainer stylesContainer;
    private final DataStyles dataStyles;
    private final boolean libreOfficeMode;
    private final int columnIndex;
    /*
    private TableColdCell coldRow;
	private TableCellStyle defaultCellStyle;
	private TableRowStyle rowStyle;
	private String formula;
	*/
    private TableCellStyle style;
    private TableCell.Type type;
    private TableColdCell coldCell;
    private String value;

    /**
     * Create the table cell implementation
     *
     * @param writeUtil       an util
     * @param xmlUtil         an util
     * @param stylesContainer the styles containers that will dispatch styles to document.xml and
     *                        styles.xml
     * @param dataStyles      the styles
     * @param libreOfficeMode
     * @param parent          the parent row
     * @param columnIndex     index in parent row
     */
    TableCellImpl(final WriteUtil writeUtil, final XMLUtil xmlUtil,
                  final StylesContainer stylesContainer, final DataStyles dataStyles,
                  final boolean libreOfficeMode, final TableRow parent, final int columnIndex) {
        this.writeUtil = writeUtil;
        this.stylesContainer = stylesContainer;
        this.xmlUtil = xmlUtil;
        this.dataStyles = dataStyles;
        this.libreOfficeMode = libreOfficeMode;
        this.parent = parent;
        this.columnIndex = columnIndex;
    }

    @Override
    public void appendXMLToTableRow(final XMLUtil util, final Appendable appendable)
            throws IOException {
        final boolean covered = this.isCovered();
        if (covered) {
            appendable.append("<table:covered-table-cell");
        } else {
            appendable.append("<table:table-cell");
        }

        if (this.style != null) {
            util.appendEAttribute(appendable, "table:style-name", this.style.getName());
        } else if (this.libreOfficeMode) {
            // looks for a parent style to set
            util.appendEAttribute(appendable, "table:style-name", this.getCurCellStyle().getName());
        }

        if (this.type != null) {
            util.appendAttribute(appendable, "office:value-type", this.type.getValueAttribute());
            util.appendEAttribute(appendable, this.type.getValueType(), this.value);
            if (this.type == TableCell.Type.CURRENCY) {
                final String currency = this.getCurrency();
                util.appendEAttribute(appendable, "office:currency", currency);
            }
        }

        if (this.hasColdCell()) {
            this.coldCell.appendXMLToTable(util, appendable);
        } else {
            appendable.append("/>");
        }
    }

    @Override
    public boolean isCovered() {
        return this.hasColdCell() && this.coldCell.isCovered();
    }

    private boolean hasColdCell() {
        return this.coldCell != null;
    }

    @Override
    public void setCovered() {
        this.ensureColdCell();
        this.coldCell.setCovered();
    }

    @Override
    public void setColumnsSpanned(final int n) {
        if (n <= 1) return;

        this.parent.setColumnsSpanned(this.columnIndex, n);
    }

    @Override
    public void markColumnsSpanned(final int n) {
        if (n <= 1) return;

        this.ensureColdCell();
        this.coldCell.setColumnsSpanned(n);
    }

    @Override
    public void setRowsSpanned(final int n) throws IOException {
        if (n <= 1) return;

        this.parent.setRowsSpanned(this.columnIndex, n);
    }

    @Override
    public void markRowsSpanned(final int n) {
        if (n <= 1) return;

        this.ensureColdCell();
        this.coldCell.setRowsSpanned(n);
    }


    private String getCurrency() {
        if (this.coldCell == null) return null;
        else return this.coldCell.getCurrency();
    }

    @Override
    public void setBooleanValue(final boolean value) {
        this.value = value ? "true" : "false";
        this.type = TableCell.Type.BOOLEAN;
        this.setImplicitDataStyle(this.dataStyles.getBooleanDataStyle());
    }

    /*
     * FastOds uses the mapping Apache DB project mapping
     * @see https://db.apache.org/ojb/docu/guides/jdbc-types
     * .html#Mapping+of+JDBC+Types+to+Java+Types
     */
    @Override
    public void setCellValue(final CellValue value) {
        value.setToCell(this);
    }

    @Override
    public void setCurrencyValue(final float value, final String currency) {
        this.setCurrencyValue(Float.toString(value), currency);
    }

    private void setCurrencyValue(final String valueAsString, final String currency) {
        this.value = valueAsString;
        this.type = TableCell.Type.CURRENCY;
        this.setImplicitDataStyle(this.dataStyles.getCurrencyDataStyle());

        this.ensureColdCell();
        this.coldCell.setCurrency(currency); // escape here
    }

    @Override
    public void setCurrencyValue(final int value, final String currency) {
        this.setCurrencyValue(Integer.toString(value), currency);
    }

    @Override
    public void setCurrencyValue(final Number value, final String currency) {
        this.setCurrencyValue(value.toString(), currency);
    }

    private void ensureColdCell() {
        if (this.coldCell == null) this.coldCell = TableColdCell.create(this.xmlUtil);
    }

    @Override
    public void setDataStyle(final DataStyle dataStyle) {
        if (dataStyle == null) return;

        this.stylesContainer.addDataStyle(dataStyle);
        final TableCellStyle curStyle = this.getCurCellStyle();
        final DataStyle curDataStyle = curStyle.getDataStyle();
        if (curDataStyle == null) { // no data style yet: create a custom child style
            this.style = this.stylesContainer.addChildCellStyle(curStyle, dataStyle);
        } else { // a style and a datastyle => create a custom sibling cell style
            this.style = this.stylesContainer
                    .addChildCellStyle(curStyle.getParentCellStyle(), dataStyle);
        }
    }

    /**
     * For implicit datastyle, e.g. will set an implicit datastyle if the data style is not set
     */
    private void setImplicitDataStyle(final DataStyle dataStyle) {
        if (dataStyle == null) return;
        final TableCellStyle curStyle = this.getCurCellStyle();
        final DataStyle curDataStyle = curStyle.getDataStyle();
        if (curDataStyle == null) { // no data style yet: create a custom child style
            this.stylesContainer.addDataStyle(dataStyle);
            this.style = this.stylesContainer.addChildCellStyle(curStyle, dataStyle);
        } else {
            // TODO: Can't we add this on first style use, once for all?
            this.stylesContainer.addDataStyle(curDataStyle);
            this.style = this.stylesContainer
                    .addChildCellStyle(curStyle.getParentCellStyle(), curDataStyle);
        }
    }


    /**
     * @return the current cell style, eventually found in parent (row, column, table). Never null
     */
    private TableCellStyle getCurCellStyle() {
        if (this.style == null) {
            return this.parent.findDefaultCellStyle(this.columnIndex);
        } else {
            return this.style;
        }
    }

    @Override
    public void setDateValue(final Calendar cal) {
        this.setDateValue(cal.getTime());
    }

    @Override
    public void setDateValue(final Date value) {
        this.value = TableCellImpl.DATE_VALUE_FORMAT.format(value);
        this.type = TableCell.Type.DATE;
        this.setImplicitDataStyle(this.dataStyles.getDateDataStyle());
    }

    private void setFloatValue(final String valueAsString) {
        this.value = valueAsString;
        this.type = TableCell.Type.FLOAT;
        this.setImplicitDataStyle(this.dataStyles.getNumberDataStyle());
    }

    @Override
    public void setFloatValue(final float value) {
        this.setFloatValue(Float.toString(value));
    }

    @Override
    public void setFloatValue(final int value) {
        this.setFloatValue(this.writeUtil.toString(value));
    }

    @Override
    public void setFloatValue(final Number value) {
        this.setFloatValue(value.toString());
    }

    @Override
    public void setPercentageValue(final int value) {
        this.setPercentageValue(Integer.toString(value));
    }

    private void setPercentageValue(final String valueAsString) {
        this.value = valueAsString;
        this.type = TableCell.Type.PERCENTAGE;
        this.setImplicitDataStyle(this.dataStyles.getPercentageDataStyle());
    }

    @Override
    public void setPercentageValue(final float value) {
        this.setPercentageValue(Float.toString(value));
    }

    @Override
    public void setPercentageValue(final Number value) {
        this.setPercentageValue(value.toString());
    }

    @Override
    public void setStringValue(final String value) {
        this.value = value;
        this.type = TableCell.Type.STRING;
    }

    @Override
    public void setStyle(final TableCellStyle style) {
        if (style == null) return;

        this.stylesContainer.addContentFontFaceContainerStyle(style);
        final TableCellStyle curStyle = this.style;
        if (curStyle == null) { // we know that dataStyle is null
            this.style = style; // just set the new style as current style
        } else {
            final DataStyle dataStyle = curStyle.getDataStyle();
            if (dataStyle == null) {
                this.style = style; // just replace the current style by the new style
            } else { // a style and a datastyle => create a custom child cell style
                this.style = this.stylesContainer.addChildCellStyle(style, dataStyle);
            }
        }
    }

    @Override
    public void setText(final Text text) {
        this.ensureColdCell();
        this.coldCell.setText(text);
        this.value = "";
        this.type = TableCell.Type.STRING;
        text.addEmbeddedStylesFromCell(this.stylesContainer);
    }

    @Override
    public void setCellMerge(final int rowMerge, final int columnMerge) throws IOException {
        if (rowMerge <= 0 || columnMerge <= 0) return;
        if (rowMerge <= 1 && columnMerge <= 1) return;

        this.parent.setCellMerge(this.columnIndex, rowMerge, columnMerge);
    }

    @Override
    public void setTimeValue(final long timeInMillis) {
        if (timeInMillis == 0) {
            this.value = "";
        } else if (timeInMillis < 0) {
            this.value = this.xmlUtil.formatNegTimeInterval(0, 0, 0, 0, 0, (double) -timeInMillis / 1000);
        } else {
            this.value = this.xmlUtil.formatTimeInterval(0, 0, 0, 0, 0, (double) timeInMillis / 1000);
        }
        this.type = TableCell.Type.TIME;
        this.setImplicitDataStyle(this.dataStyles.getTimeDataStyle());
    }

    @Override
    public void setTimeValue(final String duration) {
        this.value = duration;
        this.type = TableCell.Type.TIME;
        this.setImplicitDataStyle(this.dataStyles.getTimeDataStyle());
    }

    @Override
    public void setTimeValue(final long years, final long months, final long days, final long hours,
                             final long minutes, final double seconds) {
        this.value = this.xmlUtil.formatTimeInterval(years, months, days, hours, minutes, seconds);
        this.type = TableCell.Type.TIME;
        this.setImplicitDataStyle(this.dataStyles.getTimeDataStyle());
    }

    @Override
    public void setNegTimeValue(final long years, final long months, final long days,
                                final long hours, final long minutes, final double seconds) {
        this.value = this.xmlUtil
                .formatNegTimeInterval(years, months, days, hours, minutes, seconds);
        this.type = TableCell.Type.TIME;
        this.setImplicitDataStyle(this.dataStyles.getTimeDataStyle());
    }

    @Override
    public void setTooltip(final String tooltip) {
        this.ensureColdCell();
        this.coldCell.setTooltip(tooltip);
    }

    @Override
    public void setTooltip(final String tooltip, final Length width, final Length height,
                           final boolean visible) {
        this.ensureColdCell();
        this.coldCell.setTooltip(tooltip, width, height, visible);
    }

    @Override
    public void setVoidValue() {
        this.value = "";
        this.type = TableCell.Type.VOID;
    }

    @Override
    public void setFormula(final String formula) {
        this.ensureColdCell();
        this.coldCell.setFormula(formula);
    }

    @Override
    public boolean hasValue() {
        return this.value != null || this.hasColdCell();
    }
}