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
import com.github.jferard.fastods.entry.StylesContainer;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.util.FullList;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
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
	private boolean hasSpans;
	private boolean isComplexRow;
	private final Table parent;
	private final PositionUtil positionUtil;
	private final int rowIndex;
	private List<Integer> rowsSpanned;
	private TableRowStyle rowStyle;
	private final List<TableCellStyle> styles;
	private final StylesContainer stylesContainer;
	private List<Text> texts;
	private List<String> tooltips;
	private final List<TableCell.Type> types;
	private final List<String> values;
	private final WriteUtil writeUtil;
	private final XMLUtil xmlUtil;

	HeavyTableRow(final PositionUtil positionUtil, final WriteUtil writeUtil,
			final XMLUtil xmlUtil, final StylesContainer stylesContainer,
			final DataStyles dataStyles, final Table parent, final int rowIndex,
			final int columnCapacity) {
		this.writeUtil = writeUtil;
		this.stylesContainer = stylesContainer;
		this.positionUtil = positionUtil;
		this.xmlUtil = xmlUtil;
		this.dataStyles = dataStyles;
		this.parent = parent;
		this.rowIndex = rowIndex;
		this.columnCapacity = columnCapacity;
		this.rowStyle = TableRowStyle.DEFAULT_TABLE_ROW_STYLE;
		this.values = FullList.newListWithCapacity(columnCapacity);
		this.styles = FullList.newListWithCapacity(columnCapacity);
		this.types = FullList.newListWithCapacity(columnCapacity);
		// other lists are not initialized because...
		this.isComplexRow = false;
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
				this.appendRowXMLToTable(util, appendable, i, value);
			}
		}

		appendable.append("</table:table-row>");
	}

	public String getBooleanValue(final int i) {
		return this.values.get(i);
	}

	public int getColumnCount() {
		return this.values.size();
	}

	public int getColumnsSpanned(final int i) {
		if (this.columnsSpanned != null)
			return this.columnsSpanned.get(i);
		else
			return -1;
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
		if (this.rowsSpanned != null)
			return this.rowsSpanned.get(i);
		else
			return -1;
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

	public Text getText(final int i) {
		if (this.texts != null)
			return this.texts.get(i);
		else
			return null;
	}

	public String getTimeValue(final int i) {
		return this.values.get(i);
	}

	public String getTooltip(final int i) {
		if (this.tooltips != null)
			return this.tooltips.get(i);
		else
			return null;
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
	 * @param colIndex
	 *            The column, 0 is the first column
	 * @param rowMerge
	 * @param columnMerge
	 */
	public void setCellMerge(final int colIndex, final int rowMerge,
			final int columnMerge) {
		if (rowMerge <= 1 && columnMerge <= 1)
			return;

		if (this.columnsSpanned == null) {
			this.columnsSpanned = FullList
					.newListWithCapacity(this.columnCapacity);
		}

		final Integer s = this.columnsSpanned.get(colIndex);
		if (s != null && s == -1)
			return;

		if (rowMerge > 1) {
			if (this.rowsSpanned == null)
				this.rowsSpanned = FullList
						.newListWithCapacity(this.columnCapacity);
			this.rowsSpanned.set(colIndex, rowMerge);
		}

		if (columnMerge > 1)
			this.columnsSpanned.set(colIndex, columnMerge);

		this.hasSpans = true;
		this.isComplexRow = true;

		// add negative span for covered cells
		for (int c = 1; c < columnMerge; c++)
			this.columnsSpanned.set(colIndex + c, -1);
		for (int r = 1; r < rowMerge; r++) {
			final HeavyTableRow row = this.parent
					.getRowSecure(this.rowIndex + r);
			for (int c = 0; c < columnMerge; c++) {
				if (row.columnsSpanned == null)
					row.columnsSpanned = FullList
							.newListWithCapacity(this.columnCapacity);
				row.columnsSpanned.set(colIndex + c, -1);
				row.hasSpans = true;
				row.isComplexRow = true;
			}
		}
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param pos
	 *            The cell position e.g. 'A1'
	 * @param rowMerge
	 * @param columnMerge
	 */
	public void setCellMerge(final String pos, final int rowMerge,
			final int columnMerge) {
		final int col = this.positionUtil.getPosition(pos).getColumn();
		this.setCellMerge(col, rowMerge, columnMerge);
	}

	/*
	 * FastOds uses the mapping Apache DB project mapping
	 * @see https://db.apache.org/ojb/docu/guides/jdbc-types.html#Mapping+of+JDBC+Types+to+Java+Types
	 */
	public void setCellValue(final int i, final CellValue value) {
		value.setToRow(this, i);
	}

	public void setColumnsSpanned(final int colIndex, final int n) {
		if (n <= 1)
			return;

		if (this.columnsSpanned == null)
			this.columnsSpanned = FullList
					.newListWithCapacity(this.columnCapacity);

		final Integer s = this.columnsSpanned.get(colIndex);
		if (s != null && s == -1)
			return;

		this.columnsSpanned.set(colIndex, n);
		// add negative span for covered cells
		for (int c = 1; c < n; c++)
			this.columnsSpanned.set(colIndex + c, -1);
		this.hasSpans = true;
		this.isComplexRow = true;
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
		this.stylesContainer.addStyleToStylesCommonStyles(ts);
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
		this.values.set(i, this.writeUtil.toString(value));
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

	/**
	 * @param i
	 * @param object
	 * @deprecated Shortcut for
	 *             {@code setCellValue(i, CellValue.fromObject(object))}
	 */
	@Deprecated
	public void setObjectValue(final int i, final Object object) {
		this.setCellValue(i, CellValue.fromObject(object));
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
	public void setRowsSpanned(final int colIndex, final int n) {
		if (n <= 1)
			return;

		if (this.rowsSpanned == null)
			this.rowsSpanned = FullList
					.newListWithCapacity(this.columnCapacity);
		if (this.columnsSpanned.get(colIndex) == -1)
			return;

		this.rowsSpanned.set(colIndex, n);
		// add negative span for covered cells
		for (int r = 1; r < n; r++) {
			final HeavyTableRow row = this.parent
					.getRowSecure(this.rowIndex + r);
			row.columnsSpanned.set(colIndex, -1);
		}

		this.hasSpans = true;
		this.isComplexRow = true;
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

		this.stylesContainer.addStyleToStylesCommonStyles(style);
		// if there is a current style that has a data and new styles does not
		// have a data style
		final TableCellStyle curStyle = this.styles.get(i);
		if (style.getDataStyle() == null && curStyle != null
				&& curStyle.getDataStyle() != null) {
			style.setDataStyle(curStyle.getDataStyle());
		}
		if (style.getDataStyle() != null)
			this.stylesContainer.addDataStyle(style.getDataStyle());
		this.styles.set(i, style);
	}

	public void setStyle(final TableRowStyle rowStyle) {
		this.stylesContainer.addStyleToContentAutomaticStyles(rowStyle);
		this.rowStyle = rowStyle;
	}

	public void setTextValue(final int i, final Text text) {
		if (this.texts == null)
			this.texts = FullList.newListWithCapacity(this.columnCapacity);

		this.values.set(i, "");
		this.types.set(i, TableCell.Type.STRING);
		this.texts.set(i, text);
		text.addEmbeddedStylesToContentAutomaticStyles(this.stylesContainer);
		this.isComplexRow = true;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setTimeValue(long)
	 */
	public void setTimeValue(final int i, final long timeInMillis) {
		this.values.set(i, this.xmlUtil.formatTimeInterval(timeInMillis));
		this.types.set(i, TableCell.Type.TIME);
		this.setStyle(i, this.dataStyles.getTimeStyle());
	}

	public void setTooltip(final int i, final String tooltip) {
		if (this.tooltips == null)
			this.tooltips = FullList.newListWithCapacity(this.columnCapacity);

		this.tooltips.set(i, tooltip);
		this.isComplexRow = true;
	}

	public void setVoidValue(final int i) {
		this.values.set(i, null);
		this.types.set(i, TableCell.Type.VOID);
	}

	private void appendComplexXMLToTable(final XMLUtil util,
			final Appendable appendable, final int colIndex,
			final boolean covered) throws IOException {
		if (this.hasSpans && !covered) {
			if (this.columnsSpanned != null) {
				final Integer colSpan = this.columnsSpanned.get(colIndex);
				if (colSpan != null && colSpan > 1) {
					util.appendEAttribute(appendable,
							"table:number-columns-spanned", colSpan);
				}
			}
			if (this.rowsSpanned != null) {
				final Integer rowSpan = this.rowsSpanned.get(colIndex);
				if (rowSpan != null && rowSpan > 1) {
					util.appendEAttribute(appendable,
							"table:number-rows-spanned", rowSpan);
				}
			}
		}

		if (this.texts == null && this.tooltips == null) {
			appendable.append("/>");
		} else { // something between <cell> and </cell>
			appendable.append(">");
			if (this.texts != null) {
				final Text text = this.texts.get(colIndex);
				if (text != null) {
					text.appendXMLContent(util, appendable);
				}
			}
			if (this.tooltips != null) {
				final String tooltip = this.tooltips.get(colIndex);
				if (tooltip != null) {
					appendable.append("<office:annotation><text:p>")
							.append(tooltip)
							.append("</text:p></office:annotation>");
				}
			}
			appendable.append("</table:table-cell>");
		}
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

	protected void appendRowXMLToTable(final XMLUtil util,
			final Appendable appendable, final int colIndex, final String value)
			throws IOException {
		final TableCellStyle style = this.styles.get(colIndex);
		final TableCell.Type valueType = this.types.get(colIndex);

		boolean covered;
		if (this.columnsSpanned != null) {
			final Integer s = this.columnsSpanned.get(colIndex);
			if (s != null && s == -1) // covered-cell
				covered = true;
			else
				covered = false;
		} else {
			covered = false;
		}

		if (covered) {
			appendable.append("<table:covered-table-cell");
		} else {
			appendable.append("<table:table-cell");
		}

		if (style != null) {
			util.appendEAttribute(appendable, "table:style-name",
					style.getName());
		}

		util.appendEAttribute(appendable, "office:value-type",
				valueType.getAttrValue());
		util.appendEAttribute(appendable, valueType.getAttrName(), value);
		if (valueType == TableCell.Type.CURRENCY) {
			final String currency = this.currencies.get(colIndex);
			util.appendAttribute(appendable, "office:currency", currency);
		}

		if (this.isComplexRow) {
			this.appendComplexXMLToTable(util, appendable, colIndex, covered);
		} else {
			appendable.append("/>");
		}
	}
}
