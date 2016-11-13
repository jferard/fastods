/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.util.FullList;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * WHERE ? content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-row
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public class HeavyTableRow {
	private final int columnCapacity;
	private List<Integer> columnsSpanned;
	private List<String> currencies;
	private DataStyles dataStyles;
	private TableCellStyle defaultCellStyle;
	private final ContentEntry contentEntry;
	private final int row;
	private List<Integer> rowsSpanned;
	private TableRowStyle rowStyle;
	private final List<TableCellStyle> styles;
	private final List<TableCell.Type> types;
	private final Util util;
	private final List<String> values;
	private final List<String> tooltips;
	private final XMLUtil xmlUtil;
	private StylesEntry stylesEntry;

	HeavyTableRow(final ContentEntry contentEntry, StylesEntry stylesEntry,
			final Util util, final XMLUtil xmlUtil, final DataStyles dataStyles,
			final int row, final int columnCapacity) {
		this.stylesEntry = stylesEntry;
		this.util = util;
		this.xmlUtil = xmlUtil;
		this.dataStyles = dataStyles;
		this.row = row;
		this.contentEntry = contentEntry;
		this.columnCapacity = columnCapacity;
		this.rowStyle = TableRowStyle.DEFAULT_TABLE_ROW_STYLE;
		this.values = FullList.newListWithCapacity(columnCapacity);
		this.tooltips = FullList.newListWithCapacity(columnCapacity);
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
		int nullFieldCounter = 0;

		final int size = this.values.size();
		final boolean hasSpans = this.rowsSpanned != null
				|| this.columnsSpanned != null;
		for (int i = 0; i < size; i++) {
			final String value = this.values.get(i);
			if (value == null) {
				nullFieldCounter++;
			} else {
				if (nullFieldCounter > 0) {
					appendable.append("<table:table-cell");
					if (nullFieldCounter > 1)
						util.appendEAttribute(appendable,
								"table:number-columns-repeated",
								nullFieldCounter);
					appendable.append("/>");
					nullFieldCounter = 0;
				}
				this.appendXMLToTableRow(util, appendable, i, value, hasSpans);
			}
		}

		appendable.append("</table:table-row>");
	}

	public void appendXMLToTableRow(final XMLUtil util,
			final Appendable appendable, final int i, final String value,
			final boolean hasSpans) throws IOException {
		final TableCellStyle style = this.styles.get(i);
		final TableCell.Type valueType = this.types.get(i);
		final String tooltip = this.tooltips.get(i);

		appendable.append("<table:table-cell");

		if (style != null) {
			util.appendEAttribute(appendable, "table:style-name",
					style.getName());
		}

		util.appendEAttribute(appendable, "office:value-type",
				valueType.getAttrValue());
		util.appendEAttribute(appendable, valueType.getAttrName(), value);
		if (valueType == TableCell.Type.CURRENCY) {
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
					util.appendEAttribute(appendable,
							"table:number-rows-spanned", rowSpan);
				}
			}
		}

		if (tooltip == null) {
			appendable.append("/>");
		} else {
			appendable.append("><office:annotation><text:p>").append(tooltip)
					.append("</text:p></office:annotation></table:table-cell>");
		}
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

	public TableCell.Type getValueType(final int i) {
		return this.types.get(i);
	}

	public TableCellWalker getWalker() {
		return new LightTableCell(this);
	}

	public void setBooleanValue(final int i, final boolean value) {
		this.values.set(i, value ? "true" : "false");
		this.types.set(i, TableCell.Type.BOOLEAN);
		this.setStyle(i, this.dataStyles.getBooleanStyle());
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param col
	 *            The column, 0 is the first column
	 * @param rowMerge
	 * @param columnMerge
	 *
	 * @throws FastOdsException
	 */
	public void setCellMerge(final int col, final int rowMerge,
			final int columnMerge) {
		if (rowMerge > 1)
			this.setRowsSpanned(col, rowMerge);

		if (columnMerge > 1)
			this.setColumnsSpanned(col, columnMerge);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param pos
	 *            The cell position e.g. 'A1'
	 * @param rowMerge
	 * @param columnMerge
	 *
	 * @throws FastOdsException
	 */
	public void setCellMerge(final String pos, final int rowMerge,
			final int columnMerge) throws FastOdsException {
		final int col = this.util.getPosition(pos).getColumn();
		this.setCellMerge(col, rowMerge, columnMerge);
	}

	public void setColumnsSpanned(final int i, final int n) {
		if (n <= 1)
			return;

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
		this.types.set(i, TableCell.Type.CURRENCY);
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
		this.types.set(i, TableCell.Type.CURRENCY);
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
		this.types.set(i, TableCell.Type.CURRENCY);
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
		this.values.set(i, TableCell.DATE_VALUE_FORMAT.format(value));
		this.types.set(i, TableCell.Type.DATE);
		this.setStyle(i, this.dataStyles.getDateStyle());
	}

	/**
	 * Set the cell rowStyle for the cell at col to ts.
	 *
	 * @param col
	 *            The column number
	 * @param ts
	 *            The table rowStyle to be used
	 */
	public void setDefaultCellStyle(final TableCellStyle ts) {
		ts.addToContentAndStyles(this.contentEntry, this.stylesEntry);
		this.defaultCellStyle = ts;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(float)
	 */
	public void setFloatValue(final int i, final double value) {
		this.values.set(i, Double.toString(value));
		this.types.set(i, TableCell.Type.FLOAT);
		this.setStyle(i, this.dataStyles.getNumberStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(float)
	 */
	public void setFloatValue(final int i, final float value) {
		this.values.set(i, Float.toString(value));
		this.types.set(i, TableCell.Type.FLOAT);
		this.setStyle(i, this.dataStyles.getNumberStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(int)
	 */
	public void setFloatValue(final int i, final int value) {
		this.values.set(i, this.util.toString(value));
		this.types.set(i, TableCell.Type.FLOAT);
		this.setStyle(i, this.dataStyles.getNumberStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(java.lang.Number)
	 */
	public void setFloatValue(final int i, final Number value) {
		this.values.set(i, value.toString());
		this.types.set(i, TableCell.Type.FLOAT);
		this.setStyle(i, this.dataStyles.getNumberStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFormat(com.github.jferard.fastods.style.DataStyles)
	 */
	public void setFormat(final DataStyles format) {
		this.dataStyles = format;
	}

	/*
	 * FastOds uses the mapping Apache DB project mapping
	 * @see https://db.apache.org/ojb/docu/guides/jdbc-types.html#Mapping+of+JDBC+Types+to+Java+Types
	 */
	public void setCellValue(final int i, final CellValue value) {
		value.setToRow(this, i);
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setPercentageValue(float)
	 */
	public void setPercentageValue(final int i, final double value) {
		this.values.set(i, Double.toString(value));
		this.types.set(i, TableCell.Type.PERCENTAGE);
		this.setStyle(i, this.dataStyles.getPercentageStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setPercentageValue(float)
	 */
	public void setPercentageValue(final int i, final float value) {
		this.values.set(i, Float.toString(value));
		this.types.set(i, TableCell.Type.PERCENTAGE);
		this.setStyle(i, this.dataStyles.getPercentageStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setPercentageValue(java.lang.Number)
	 */
	public void setPercentageValue(final int i, final Number value) {
		this.values.set(i, value.toString());
		this.types.set(i, TableCell.Type.PERCENTAGE);
		this.setStyle(i, this.dataStyles.getPercentageStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setRowsSpanned(int)
	 */
	public void setRowsSpanned(final int i, final int n) {
		if (n <= 1)
			return;

		if (this.rowsSpanned == null)
			this.rowsSpanned = FullList
					.newListWithCapacity(this.columnCapacity);
		this.rowsSpanned.set(i, n < 0 ? 0 : n);
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setStringValue(java.lang.String)
	 */
	public void setStringValue(final int i, final String value) {
		this.values.set(i, this.xmlUtil.escapeXMLAttribute(value));
		this.types.set(i, TableCell.Type.STRING);
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#styles.set(i, com.github.jferard.fastods.style.TableCellStyle)
	 */
	public void setStyle(final int i, final TableCellStyle style) {
		if (style == null)
			return;

		style.addToContentAndStyles(this.contentEntry, this.stylesEntry);
		final TableCellStyle curStyle = this.styles.get(i);
		if (style.getDataStyle() == null && curStyle != null
				&& curStyle.getDataStyle() != null)
			style.setDataStyle(curStyle.getDataStyle());
		this.styles.set(i, style);
	}

	public void setStyle(final TableRowStyle rowStyle) {
		rowStyle.addToContent(this.contentEntry);
		this.rowStyle = rowStyle;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setTimeValue(long)
	 */
	public void setTimeValue(final int i, final long timeInMillis) {
		this.values.set(i, this.xmlUtil.formatTimeInterval(timeInMillis));
		this.types.set(i, TableCell.Type.TIME);
		this.setStyle(i, this.dataStyles.getTimeStyle());
	}

	public void setVoidValue(int i) {
		this.values.set(i, null);
		this.types.set(i, TableCell.Type.VOID);
	}

	private void appendRowOpenTag(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<table:table-row");
		if (this.rowStyle != null)
			util.appendEAttribute(appendable, "table:style-name",
					this.rowStyle.getName());
		if (this.defaultCellStyle != null)
			util.appendEAttribute(appendable, "table:default-cell-style-name",
					this.defaultCellStyle.getName());
		appendable.append(">");
	}

	public String getTooltip(int i) {
		return this.tooltips.get(i);
	}

	public void setTooltip(int i, String tooltip) {
		this.tooltips.set(i, tooltip);
	}

	
	/**
	 * @param i
	 * @param object
	 * @deprecated Shortcut for {@code setCellValue(i, CellValue.fromObject(object))}
	 */
	@Deprecated
	public void setObjectValue(int i, Object object) {
		this.setCellValue(i, CellValue.fromObject(object));
	}
}
