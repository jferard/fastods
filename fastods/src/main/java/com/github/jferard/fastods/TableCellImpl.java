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

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * WHERE ? content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-row
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableCellImpl implements TableCell {
    private final TableRow parent;
    private final WriteUtil writeUtil;
    private final XMLUtil xmlUtil;
    private final StylesContainer stylesContainer;
    private final DataStyles dataStyles;
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

    TableCellImpl(final WriteUtil writeUtil, final XMLUtil xmlUtil,
                  final StylesContainer stylesContainer, final DataStyles dataStyles,
                  final TableRow parent, final int columnIndex, final int columnCapacity) {
        this.writeUtil = writeUtil;
        this.stylesContainer = stylesContainer;
        this.xmlUtil = xmlUtil;
        this.dataStyles = dataStyles;
        this.parent = parent;
        this.columnIndex = columnIndex;
    }

    @Override
    public void appendXMLToTableRow(final XMLUtil util,
                                 final Appendable appendable)
            throws IOException {
        final boolean covered = this.isCovered();
        if (covered) {
            appendable.append("<table:covered-table-cell");
        } else {
            appendable.append("<table:table-cell");
        }

        if (this.style != null) {
            util.appendEAttribute(appendable, "table:style-name",
                    this.style.getName());
        }

        if (this.type != null) {
            util.appendEAttribute(appendable, "office:value-type",
                    this.type.getAttrValue());
            util.appendEAttribute(appendable, this.type.getAttrName(), this.value);
            if (this.type == TableCell.Type.CURRENCY) {
                final String currency = this.getCurrency();
                util.appendAttribute(appendable, "office:currency", currency);
            }
        }

        if (this.hasColdCell()) {
            this.coldCell.appendXMLToTable(util, appendable, covered);
        } else {
            appendable.append("/>");
        }
    }

    @Override
    public boolean isCovered() {
        return this.hasColdCell()
                && this.coldCell.isCovered();
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
        if (n <= 1)
            return;

        this.parent.setColumnsSpanned(this.columnIndex, n);
    }

    @Override
    public void markColumnsSpanned(final int n) {
        if (n <= 1)
            return;

        this.ensureColdCell();
        this.coldCell.setColumnsSpanned(n);
    }

    @Override
    public void setRowsSpanned(final int n) throws IOException {
        if (n <= 1)
            return;

        this.parent.setRowsSpanned(this.columnIndex, n);
    }

    @Override
    public void markRowsSpanned(final int n) throws IOException {
        if (n <= 1)
            return;

        this.ensureColdCell();
        this.coldCell.setRowsSpanned(n);
    }


    private String getCurrency() {
        if (this.coldCell == null)
            return null;
        else
            return this.coldCell.getCurrency();
    }

    @Override
    public void setBooleanValue(final boolean value) {
        this.value = value ? "true" : "false";
        this.type = TableCell.Type.BOOLEAN;
        this.setDataStyle(this.dataStyles.getBooleanDataStyle());
    }

    /*
     * FastOds uses the mapping Apache DB project mapping
     * @see https://db.apache.org/ojb/docu/guides/jdbc-types.html#Mapping+of+JDBC+Types+to+Java+Types
     */
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
        this.setDataStyle(this.dataStyles.getCurrencyDataStyle());

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
        if (this.coldCell == null)
            this.coldCell = TableColdCell.create(this.parent, this.xmlUtil);
    }

    /* (non-Javadoc)
     * @see com.github.jferard.fastods.TableCell#styles.set(c, com.github.jferard.fastods.style.TableCellStyle)
     */
    private void setDataStyle(
            final DataStyle dataStyle) {
        if (dataStyle == null)
            return;

        TableCellStyle curStyle = this.style;
        if (curStyle == null) // adds the data style
            curStyle = TableCellStyle.DEFAULT_CELL_STYLE;

        this.stylesContainer.addDataStyle(dataStyle);
        this.style = this.stylesContainer.addChildCellStyle(curStyle, dataStyle);
    }

    public void setDateValue(final Calendar cal) {
        this.setDateValue(cal.getTime());
    }

    @Override
    public void setDateValue(final Date value) {
        this.value = TableCellWalkerImpl.DATE_VALUE_FORMAT.format(value);
        this.type = TableCell.Type.DATE;
        this.setDataStyle(this.dataStyles.getDateDataStyle());
    }

    private void setFloatValue(final String valueAsString) {
        this.value = valueAsString;
        this.type = TableCell.Type.FLOAT;
        this.setDataStyle(this.dataStyles.getNumberDataStyle());
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

    /**
     * @param object the object to set at this place
     * @deprecated Shortcut for
     * {@code setCellValue(c, CellValue.fromObject(object))}
     */
    @Deprecated
    public void setObjectValue(final Object object) {
        this.setCellValue(CellValue.fromObject(object));
    }

    @Override
    public void setPercentageValue(final int value) {
        this.setPercentageValue(Integer.toString(value));
    }

    private void setPercentageValue(final String valueAsString) {
        this.value = valueAsString;
        this.type = TableCell.Type.PERCENTAGE;
        this.setDataStyle(this.dataStyles.getPercentageDataStyle());
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
        this.value = this.xmlUtil.escapeXMLAttribute(value);
        this.type = TableCell.Type.STRING;
    }

    @Override
    public void setStyle(final TableCellStyle style) {
        if (style == null)
            return;

        this.stylesContainer.addStyleToStylesCommonStyles(style);
        final TableCellStyle curStyle = this.style;
        if (curStyle == null) {
            this.style = style;
        } else {
            final DataStyle dataStyle = curStyle.getDataStyle();
            if (dataStyle != null) {
                this.style = this.stylesContainer.addChildCellStyle(style, dataStyle);
            } else {
                this.style = style;
            }
        }
    }

    @Override
    public void setText(final Text text) {
        this.ensureColdCell();
        this.coldCell.setText(text);
        this.value = "";
        this.type = TableCell.Type.STRING;
        text.addEmbeddedStylesToContentAutomaticStyles(this.stylesContainer);
    }

    @Override
    public void setCellMerge(final int rowMerge, final int columnMerge) throws IOException, FastOdsException {
        if (rowMerge <= 0 || columnMerge <= 0)
            return;
        if (rowMerge <= 1 && columnMerge <= 1)
            return;

        this.parent.setCellMerge(this.columnIndex, rowMerge, columnMerge);
    }

    @Override
    public void setTimeValue(final long timeInMillis) {
        this.value = this.xmlUtil.formatTimeInterval(timeInMillis);
        this.type = TableCell.Type.TIME;
        this.setDataStyle(this.dataStyles.getTimeDataStyle());
    }

    @Override
    public void setTooltip(final String tooltip) {
        this.ensureColdCell();
        this.coldCell.setTooltip(tooltip);
    }

    @Override
    public void setTooltip(final String tooltip, final Length width, final Length height, final boolean visible) {
        this.ensureColdCell();
        this.coldCell.setTooltip(tooltip, width, height, visible);
    }

    @Override
    public void setVoidValue() {
        this.value = "";
        this.type = TableCell.Type.VOID;
    }

    public void setFormula(final String formula) {
        this.ensureColdCell();
        this.coldCell.setFormula(formula);
    }

    @Override
    public boolean hasValue() {
        return this.value != null || this.hasColdCell();
    }
}