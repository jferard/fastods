/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.github.jferard.fastods.HeavyTableCell.Type;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file LightTableRow.java is part of FastODS.
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:body/office:spreadsheet/
 *         table:table/table:table-row
 */
public class HeavyTableRow {
	private final int columnCapacity;
	private List<Integer> columnsSpanned;
	private List<String> currencies;
	private DataStyles dataStyles;
	private TableCellStyle defaultCellStyle;
	private final int nRow;
	private final OdsFile odsFile;
	private List<Integer> rowsSpanned;
	private TableRowStyle rowStyle;
	private final List<TableCellStyle> styles;
	private final List<Type> types;
	private final Util util;
	private final List<String> values;
	private XMLUtil xmlUtil;

	HeavyTableRow(final OdsFile odsFile, final Util util,
			XMLUtil xmlUtil, final DataStyles dataStyles,
			final int nRow, final int columnCapacity) {
		this.util = util;
		this.xmlUtil = xmlUtil;
		this.dataStyles = dataStyles;
		this.nRow = nRow;
		this.odsFile = odsFile;
		this.columnCapacity = columnCapacity;
		this.rowStyle = TableRowStyle.DEFAULT_TABLE_ROW_STYLE;
		this.values = FullList.newListWithCapacity(columnCapacity);
		this.styles = FullList.newListWithCapacity(columnCapacity);
		this.types = FullList.newListWithCapacity(columnCapacity);
	}

	/**
	 * Write the XML dataStyles for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @throws IOException
	 */
	public void appendXMLToTable(final XMLUtil util,
			final Appendable appendable) throws IOException {
		this.appendRowOpenTag(util, appendable);
		int nNullFieldCounter = 0;

		final int size = this.values.size();
		final boolean hasSpans = this.rowsSpanned != null || this.columnsSpanned != null;
		for (int i = 0; i < size; i++) {
			final String value = this.values.get(i);
			if (value == null) {
				nNullFieldCounter++;
			} else {
				if (nNullFieldCounter > 0) {
					appendable.append("<table:table-cell");
					if (nNullFieldCounter > 1)
						util.appendEAttribute(appendable,
								"table:number-columns-repeated",
								nNullFieldCounter);
					appendable.append("/>");
					nNullFieldCounter = 0;
				}
				this.appendXMLToTableRow(util, appendable, i, value, hasSpans);
			}
		}

		appendable.append("</table:table-row>");
	}

	private void appendRowOpenTag(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<table:table-row");
		if (this.rowStyle != null)
			util.appendEAttribute(appendable, "table:style-name",
					this.rowStyle.getName());
		if (this.defaultCellStyle != null)
			util.appendEAttribute(appendable, "table:default-cell-style-name",
					this.defaultCellStyle.getName());
		appendable.append(">");
	}

	public void appendXMLToTableRow(final XMLUtil util, final Appendable appendable,
			final int i, final String value, final boolean hasSpans) throws IOException {
		final TableCellStyle style = this.styles.get(i);
		final Type valueType = this.types.get(i);

		appendable.append("<table:table-cell");

		if (style != null) {
			util.appendEAttribute(appendable, "table:style-name",
					style.getName());
		}

		util.appendEAttribute(appendable, "office:value-type",
				valueType.getAttrValue());
		util.appendEAttribute(appendable, valueType.getAttrName(), value);
		if (valueType == Type.CURRENCY) {
			final String currency = this.currencies.get(i);
			util.appendAttribute(appendable, "office:currency", currency);
		}

		if (hasSpans) {
			if (this.columnsSpanned != null) {
				final Integer colSpan = this.columnsSpanned.get(i);
				if (colSpan != null && colSpan > 1) {
					util.appendEAttribute(appendable,
							"table:number-columns-spanned", colSpan);
				}
			}
			if (this.rowsSpanned != null) {
				final Integer rowSpan = this.rowsSpanned.get(i);
				if (rowSpan != null && rowSpan > 1) {
					util.appendEAttribute(appendable, "table:number-rows-spanned",
							rowSpan);
				}
			}
		}

		// if (this.sText == null)
		appendable.append("/>");
		// else {
		// appendable.append("><text:p>").append(this.sText)
		// .append("</text:p>");
		// appendable.append("</table:table-cell>");
		// }

	}

	public String getBooleanValue(final int i) {
		return this.values.get(i);
	}

	public int getColumnCount() {
		return this.values.size();
	}

	public int getColumnsSpanned(final int i) {
		return this.columnsSpanned.get(i);
	}

	public String getCurrency(final int i) {
		return this.currencies.get(i);
	}

	public String getCurrencyValue(final int i) {
		return this.values.get(i);
	}

	public String getDateValue(final int i) {
		return this.values.get(i);
	}

	public String getFloatValue(final int i) {
		return this.values.get(i);
	}

	public String getPercentageValue(final int i) {
		return this.values.get(i);
	}

	public int getRowsSpanned(final int i) {
		return this.rowsSpanned.get(i);
	}

	public String getRowStyleName() {
		return this.rowStyle.getName();
	}

	public String getStringValue(final int i) {
		return this.values.get(i);
	}

	public String getStyleName(final int i) {
		return this.styles.get(i).getName();
	}

	public String getTimeValue(final int i) {
		return this.values.get(i);
	}

	public Type getValueType(final int i) {
		return this.types.get(i);
	}

	public TableCellWalker getWalker() {
		return new LightTableCell(this);
	}

	public void setBooleanValue(final int i, final boolean value) {
		this.values.set(i, value ? "true" : "false");
		this.types.set(i, HeavyTableCell.Type.BOOLEAN);
		this.setStyle(i, this.dataStyles.getBooleanStyle());
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param nRowMerge
	 * @param nColumnMerge
	 *
	 * @throws FastOdsException
	 */
	public void setCellMerge(final int nCol, final int nRowMerge,
			final int nColumnMerge) throws FastOdsException {
		this.rowsSpanned.set(nCol, nRowMerge);
		this.columnsSpanned.set(nCol, nColumnMerge);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nRowMerge
	 * @param nColumnMerge
	 *
	 * @throws FastOdsException
	 */
	public void setCellMerge(final String sPos, final int nRowMerge,
			final int nColumnMerge) throws FastOdsException {
		final int nCol = this.util.getPosition(sPos).getColumn();
		this.setCellMerge(nCol, nRowMerge, nColumnMerge);
	}

	public void setColumnsSpanned(final int i, final int n) {
		if (this.columnsSpanned == null)
			this.columnsSpanned = FullList
					.newListWithCapacity(this.columnCapacity);

		this.columnsSpanned.set(i, n);
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setCurrencyValue(float, java.lang.String)
	 */
	public void setCurrencyValue(final int i, final double value,
			final String currency) {
		this.values.set(i, Double.toString(value));
		if (this.currencies == null)
			this.currencies = FullList.newListWithCapacity(this.columnCapacity);

		this.currencies.set(i, currency); // escape here
		this.types.set(i, HeavyTableCell.Type.CURRENCY);
		this.setStyle(i, this.dataStyles.getCurrencyStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setCurrencyValue(float, java.lang.String)
	 */
	public void setCurrencyValue(final int i, final float value,
			final String currency) {
		this.values.set(i, Float.toString(value));
		if (this.currencies == null)
			this.currencies = FullList.newListWithCapacity(this.columnCapacity);

		this.currencies.set(i, currency); // escape here
		this.types.set(i, HeavyTableCell.Type.CURRENCY);
		this.setStyle(i, this.dataStyles.getCurrencyStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setCurrencyValue(java.lang.Number, java.lang.String)
	 */
	public void setCurrencyValue(final int i, final Number value,
			final String currency) {
		this.values.set(i, value.toString());
		if (this.currencies == null)
			this.currencies = FullList.newListWithCapacity(this.columnCapacity);
		this.currencies.set(i, currency); // escape here
		this.types.set(i, HeavyTableCell.Type.CURRENCY);
		this.setStyle(i, this.dataStyles.getCurrencyStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setDateValue(java.util.Calendar)
	 */
	public void setDateValue(final int i, final Calendar cal) {
		this.setDateValue(i, cal.getTime());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setDateValue(java.util.Date)
	 */
	public void setDateValue(final int i, final Date value) {
		this.values.set(i, HeavyTableCell.DATE_VALUE_FORMAT.format(value));
		this.types.set(i, HeavyTableCell.Type.DATE);
		this.setStyle(i, this.dataStyles.getDateStyle());
	}

	/**
	 * Set the cell rowStyle for the cell at nCol to ts.
	 *
	 * @param nCol
	 *            The column number
	 * @param ts
	 *            The table rowStyle to be used
	 */
	public void setDefaultCellStyle(final TableCellStyle ts) {
		ts.addToFile(this.odsFile);
		this.defaultCellStyle = ts;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(float)
	 */
	public void setFloatValue(final int i, final double value) {
		this.values.set(i, Double.toString(value));
		this.types.set(i, HeavyTableCell.Type.FLOAT);
		this.setStyle(i, this.dataStyles.getNumberStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(float)
	 */
	public void setFloatValue(final int i, final float value) {
		this.values.set(i, Float.toString(value));
		this.types.set(i, HeavyTableCell.Type.FLOAT);
		this.setStyle(i, this.dataStyles.getNumberStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(int)
	 */
	public void setFloatValue(final int i, final int value) {
		this.values.set(i, Integer.toString(value));
		this.types.set(i, HeavyTableCell.Type.FLOAT);
		this.setStyle(i, this.dataStyles.getNumberStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(java.lang.Number)
	 */
	public void setFloatValue(final int i, final Number value) {
		this.values.set(i, value.toString());
		this.types.set(i, HeavyTableCell.Type.FLOAT);
		this.setStyle(i, this.dataStyles.getNumberStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFormat(com.github.jferard.fastods.style.DataStyles)
	 */
	public void setFormat(final DataStyles format) {
		this.dataStyles = format;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setObjectValue(java.lang.Object)
	 */
	public void setObjectValue(final int i, final Object value) {
		if (value == null)
			return;

		final Object retValue;
		if (value instanceof String)
			this.setStringValue(i, (String) value);
		else if (value instanceof Number)
			this.setFloatValue(i, (Number) value);
		else if (value instanceof Date) {
			this.setDateValue(i, (Date) value);
		} else if (value instanceof Calendar) {
			this.setDateValue(i, (Calendar) value);
		} else
			this.setStringValue(i, value.toString());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setPercentageValue(float)
	 */
	public void setPercentageValue(final int i, final double value) {
		this.values.set(i, Double.toString(value));
		this.types.set(i, HeavyTableCell.Type.PERCENTAGE);
		this.setStyle(i, this.dataStyles.getPercentageStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setPercentageValue(float)
	 */
	public void setPercentageValue(final int i, final float value) {
		this.values.set(i, Float.toString(value));
		this.types.set(i, HeavyTableCell.Type.PERCENTAGE);
		this.setStyle(i, this.dataStyles.getPercentageStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setPercentageValue(java.lang.Number)
	 */
	public void setPercentageValue(final int i, final Number value) {
		this.values.set(i, value.toString());
		this.types.set(i, HeavyTableCell.Type.PERCENTAGE);
		this.setStyle(i, this.dataStyles.getPercentageStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setRowsSpanned(int)
	 */
	public void setRowsSpanned(final int i, final int n) {
		if (this.rowsSpanned == null)
			this.rowsSpanned = FullList
					.newListWithCapacity(this.columnCapacity);
		this.rowsSpanned.set(i, n < 0 ? 0 : n);
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setStringValue(java.lang.String)
	 */
	public void setStringValue(final int i, final String value) {
		this.values.set(i, this.xmlUtil.escapeXMLAttribute(value));// TODO ESCAPE
		this.types.set(i, HeavyTableCell.Type.STRING);
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#styles.set(i, com.github.jferard.fastods.style.TableCellStyle)
	 */
	public void setStyle(final int i, final TableCellStyle style) {
		if (style == null)
			return;

		style.addToFile(this.odsFile);
		final TableCellStyle curStyle = this.styles.get(i);
		if (style.getDataStyle() == null && curStyle != null
				&& curStyle.getDataStyle() != null)
			style.setDataStyle(curStyle.getDataStyle());
		this.styles.set(i, style);
	}

	public void setStyle(final TableRowStyle rowStyle) {
		rowStyle.addToFile(this.odsFile);
		this.rowStyle = rowStyle;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setTimeValue(long)
	 */
	public void setTimeValue(final int i, final long timeInMillis) {
		this.values.set(i, this.util.formatTimeInterval(timeInMillis));
		this.types.set(i, HeavyTableCell.Type.TIME);
		this.setStyle(i, this.dataStyles.getTimeStyle());
	}
}