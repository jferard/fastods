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
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.util.FullList;
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * WHERE ? content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-row
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class HeavyTableRow {
	private final int columnCapacity;
	private final Table parent;
	private final int rowIndex;
	private final List<TableCellStyle> styles;
	private final StylesContainer stylesContainer;
	private final List<TableCell.Type> types;
	private final List<String> values;
	private final WriteUtil writeUtil;
	private final XMLUtil xmlUtil;
	private HeavyTableColdRow coldRow;
	private DataStyles dataStyles;
	private TableCellStyle defaultCellStyle;
	private TableRowStyle rowStyle;
	private String formula;

	HeavyTableRow(final WriteUtil writeUtil, final XMLUtil xmlUtil,
				  final StylesContainer stylesContainer, final DataStyles dataStyles,
				  final Table parent, final int rowIndex, final int columnCapacity) {
		this.writeUtil = writeUtil;
		this.stylesContainer = stylesContainer;
		this.xmlUtil = xmlUtil;
		this.dataStyles = dataStyles;
		this.parent = parent;
		this.rowIndex = rowIndex;
		this.columnCapacity = columnCapacity;
		this.rowStyle = TableRowStyle.DEFAULT_TABLE_ROW_STYLE;
		this.values = FullList.newListWithCapacity(columnCapacity);
		this.styles = FullList.newListWithCapacity(columnCapacity);
		this.types = FullList.newListWithCapacity(columnCapacity);
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

		final boolean covered = this.coldRow != null
				&& this.coldRow.isCovered(colIndex);
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
			final String currency = this.coldRow.getCurrency(colIndex);
			util.appendAttribute(appendable, "office:currency", currency);
		}

		if (this.formula != null)
			util.appendAttribute(appendable, "table:formula", "="+this.formula);

		if (this.coldRow == null) {
			appendable.append("/>");
		} else {
			this.coldRow.appendXMLToTable(util, appendable, colIndex, covered);
		}
	}

	/**
	 * Write the XML dataStyles for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @param util       a util for XML writing
	 * @param appendable where to write the XML
	 * @throws IOException If an I/O error occurs
	 */
	public void appendXMLToTable(final XMLUtil util,
								 final Appendable appendable) throws IOException {
		this.appendRowOpenTag(util, appendable);
		int nullFieldCounter = 0;

		final int size = this.values.size();
		for (int c = 0; c < size; c++) {
			final String value = this.values.get(c);
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
				this.appendRowXMLToTable(util, appendable, c, value);
			}
		}

		appendable.append("</table:table-row>");
	}

	public String getBooleanValue(final int c) {
		return this.values.get(c);
	}

	public int getColumnCount() {
		return this.values.size();
	}

	/**
	 * @param c the column index
	 * @return 0 if no span, -1 if the cell is a covered cell
	 */
	public int getColumnsSpanned(final int c) {
		if (this.coldRow == null)
			return 0;
		else
			return this.coldRow.getColumnsSpanned(c);
	}

	public String getCurrency(final int c) {
		if (this.coldRow == null)
			return null;
		else
			return this.coldRow.getCurrency(c);
	}

	public String getCurrencyValue(final int c) {
		return this.values.get(c);
	}

	public String getDateValue(final int c) {
		return this.values.get(c);
	}

	public String getFloatValue(final int c) {
		return this.values.get(c);
	}

	public String getPercentageValue(final int c) {
		return this.values.get(c);
	}

	public String getRowStyleName() {
		return this.rowStyle.getName();
	}

	public int getRowsSpanned(final int c) {
		if (this.coldRow == null)
			return 0;
		else
			return this.coldRow.getRowsSpanned(c);
	}

	public String getStringValue(final int c) {
		return this.values.get(c);
	}

	public String getStyleName(final int c) {
		final TableCellStyle style = this.styles.get(c);
		return style == null ? null : style.getName();
	}

	public Text getText(final int c) {
		if (this.coldRow == null)
			return null;
		else
			return this.coldRow.getText(c);
	}

	public String getTimeValue(final int c) {
		return this.values.get(c);
	}

	public String getTooltip(final int c) {
		if (this.coldRow == null)
			return null;
		else
			return this.coldRow.getTooltip(c);
	}

	public TableCell.Type getValueType(final int c) {
		return this.types.get(c);
	}

	public TableCellWalker getWalker() {
		return new LightTableCell(this);
	}

	public void setBooleanValue(final int c, final boolean value) {
		this.values.set(c, value ? "true" : "false");
		this.types.set(c, TableCell.Type.BOOLEAN);
		this.setDataStyle(c, this.dataStyles.getBooleanDataStyle());
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param colIndex    The column, 0 is the first column
	 * @param rowMerge    the number of rows to merge
	 * @param columnMerge the number of cells to merge
	 */
	public void setCellMerge(final int colIndex, final int rowMerge,
							 final int columnMerge) throws IOException {
		if (rowMerge <= 1 && columnMerge <= 1)
			return;

		if (this.coldRow == null)
			this.coldRow = HeavyTableColdRow.create(this.parent, this.xmlUtil, this.rowIndex,
					this.columnCapacity);
		this.coldRow.setCellMerge(colIndex, rowMerge, columnMerge);
	}

	/*
	 * FastOds uses the mapping Apache DB project mapping
	 * @see https://db.apache.org/ojb/docu/guides/jdbc-types.html#Mapping+of+JDBC+Types+to+Java+Types
	 */
	public void setCellValue(final int c, final CellValue value) {
		value.setToRow(this, c);
	}

	public void setColumnsSpanned(final int colIndex, final int n) {
		if (n <= 1)
			return;

		if (this.coldRow == null)
			this.coldRow = HeavyTableColdRow.create(this.parent, this.xmlUtil, this.rowIndex,
					this.columnCapacity);
		this.coldRow.setColumnsSpanned(colIndex, n);
	}

	public void setCovered(final int colIndex) {
		if (this.coldRow == null)
			this.coldRow = HeavyTableColdRow.create(this.parent, this.xmlUtil, this.rowIndex,
					this.columnCapacity);

		this.coldRow.setCovered(colIndex);
	}

	public void setCovered(final int colIndex, final int n) {
		if (this.coldRow == null)
			this.coldRow = HeavyTableColdRow.create(this.parent, this.xmlUtil, this.rowIndex,
					this.columnCapacity);

		this.coldRow.setCovered(colIndex, n);
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setCurrencyValue(float, java.lang.String)
	 */
	public void setCurrencyValue(final int c, final double value,
								 final String currency) {
		this.values.set(c, Double.toString(value));
		if (this.coldRow == null)
			this.coldRow = HeavyTableColdRow.create(this.parent, this.xmlUtil, this.rowIndex,
					this.columnCapacity);

		this.coldRow.setCurrency(c, currency); // escape here
		this.types.set(c, TableCell.Type.CURRENCY);
		this.setDataStyle(c, this.dataStyles.getCurrencyDataStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setCurrencyValue(float, java.lang.String)
	 */
	public void setCurrencyValue(final int c, final float value,
								 final String currency) {
		this.values.set(c, Float.toString(value));
		if (this.coldRow == null)
			this.coldRow = HeavyTableColdRow.create(this.parent, this.xmlUtil, this.rowIndex,
					this.columnCapacity);

		this.coldRow.setCurrency(c, currency); // escape here
		this.types.set(c, TableCell.Type.CURRENCY);
		this.setDataStyle(c, this.dataStyles.getCurrencyDataStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setCurrencyValue(java.lang.Number, java.lang.String)
	 */
	public void setCurrencyValue(final int c, final Number value,
								 final String currency) {
		this.values.set(c, value.toString());
		if (this.coldRow == null)
			this.coldRow = HeavyTableColdRow.create(this.parent, this.xmlUtil, this.rowIndex,
					this.columnCapacity);

		this.coldRow.setCurrency(c, currency); // escape here
		this.types.set(c, TableCell.Type.CURRENCY);
		this.setDataStyle(c, this.dataStyles.getCurrencyDataStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#styles.set(c, com.github.jferard.fastods.style.TableCellStyle)
	 */
	private void setDataStyle(final int c,
							  final DataStyle dataStyle) {
		if (dataStyle == null)
			return;

		TableCellStyle curStyle = this.styles.get(c);
		if (curStyle == null) // adds the data style
			curStyle = TableCellStyle.getDefaultCellStyle();

		this.stylesContainer.addDataStyle(dataStyle);
		final TableCellStyle anonymousStyle = this.stylesContainer.addChildCellStyle(curStyle, dataStyle);
		this.styles.set(c, anonymousStyle);
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#styles.set(c, com.github.jferard.fastods.style.TableCellStyle)
	 */
	@Deprecated
	private void setDataStyleFromStyle(final int c,
									   final TableCellStyle style) {
		if (style == null)
			return;

		final TableCellStyle curStyle = this.styles.get(c);
		if (curStyle == null) { // adds the data style
			this.stylesContainer.addNewDataStyleFromCellStyle(style);
			this.styles.set(c, style);
		} else { // keep the style, but use the new data style
			// to remove the current data style implies a kind of ref counter,
			// which would be a too complicated feature.
			final DataStyle dataStyle = style.getDataStyle();
			this.stylesContainer.addDataStyle(dataStyle);
			curStyle.setDataStyle(dataStyle);
		}
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setDateValue(java.util.Calendar)
	 */
	public void setDateValue(final int c, final Calendar cal) {
		this.setDateValue(c, cal.getTime());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setDateValue(java.util.Date)
	 */
	public void setDateValue(final int c, final Date value) {
		this.values.set(c, LightTableCell.DATE_VALUE_FORMAT.format(value));
		this.types.set(c, TableCell.Type.DATE);
		this.setDataStyle(c, this.dataStyles.getDateDataStyle());
	}

	/**
	 * Set the cell rowStyle for the cell at col to ts.
	 *
	 * @param ts The table rowStyle to be used
	 */
	public void setDefaultCellStyle(final TableCellStyle ts) {
		this.stylesContainer.addStyleToStylesCommonStyles(ts);
		this.defaultCellStyle = ts;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(float)
	 */
	public void setFloatValue(final int c, final double value) {
		this.values.set(c, Double.toString(value));
		this.types.set(c, TableCell.Type.FLOAT);
		this.setDataStyle(c, this.dataStyles.getNumberDataStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(float)
	 */
	public void setFloatValue(final int c, final float value) {
		this.values.set(c, Float.toString(value));
		this.types.set(c, TableCell.Type.FLOAT);
		this.setDataStyle(c, this.dataStyles.getNumberDataStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(int)
	 */
	public void setFloatValue(final int c, final int value) {
		this.values.set(c, this.writeUtil.toString(value));
		this.types.set(c, TableCell.Type.FLOAT);
		this.setDataStyle(c, this.dataStyles.getNumberDataStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFloatValue(java.lang.Number)
	 */
	public void setFloatValue(final int c, final Number value) {
		this.values.set(c, value.toString());
		this.types.set(c, TableCell.Type.FLOAT);
		this.setDataStyle(c, this.dataStyles.getNumberDataStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setFormat(com.github.jferard.fastods.style.DataStyles)
	 */
	public void setFormat(final DataStyles format) {
		this.dataStyles = format;
	}

	/**
	 * @param c      the column index, starting from 0
	 * @param object the object to set at this place
	 * @deprecated Shortcut for
	 * {@code setCellValue(c, CellValue.fromObject(object))}
	 */
	@Deprecated
	public void setObjectValue(final int c, final Object object) {
		this.setCellValue(c, CellValue.fromObject(object));
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setPercentageValue(float)
	 */
	public void setPercentageValue(final int c, final double value) {
		this.values.set(c, Double.toString(value));
		this.types.set(c, TableCell.Type.PERCENTAGE);
		this.setDataStyle(c, this.dataStyles.getPercentageDataStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setPercentageValue(float)
	 */
	public void setPercentageValue(final int c, final float value) {
		this.values.set(c, Float.toString(value));
		this.types.set(c, TableCell.Type.PERCENTAGE);
		this.setDataStyle(c, this.dataStyles.getPercentageDataStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setPercentageValue(java.lang.Number)
	 */
	public void setPercentageValue(final int c, final Number value) {
		this.values.set(c, value.toString());
		this.types.set(c, TableCell.Type.PERCENTAGE);
		this.setDataStyle(c, this.dataStyles.getPercentageDataStyle());
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setRowsSpanned(int)
	 */
	public void setRowsSpanned(final int colIndex, final int n) throws IOException {
		if (n <= 1)
			return;

		if (this.coldRow == null)
			this.coldRow = HeavyTableColdRow.create(this.parent, this.xmlUtil, this.rowIndex,
					this.columnCapacity);
		this.coldRow.setRowsSpanned(colIndex, n);
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setStringValue(java.lang.String)
	 */
	public void setStringValue(final int c, final String value) {
		this.values.set(c, this.xmlUtil.escapeXMLAttribute(value));
		this.types.set(c, TableCell.Type.STRING);
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#styles.set(c, com.github.jferard.fastods.style.TableCellStyle)
	 */
	public void setStyle(final int c, final TableCellStyle style) {
		if (style == null)
			return;

		this.stylesContainer.addStyleToStylesCommonStyles(style);
		final TableCellStyle curStyle = this.styles.get(c);
		if (curStyle == null) {
			this.styles.set(c, style);
		} else {
			final DataStyle dataStyle = curStyle.getDataStyle();
			if (dataStyle != null) {
				final TableCellStyle anonymousStyle = this.stylesContainer.addChildCellStyle(style, dataStyle);
				this.styles.set(c, anonymousStyle);
			} else {
				this.styles.set(c, style);
			}
		}
	}

	public void setStyle(final TableRowStyle rowStyle) {
		this.stylesContainer.addStyleToContentAutomaticStyles(rowStyle);
		this.rowStyle = rowStyle;
	}

	public void setText(final int c, final Text text) {
		if (this.coldRow == null)
			this.coldRow = HeavyTableColdRow.create(this.parent, this.xmlUtil, this.rowIndex,
					this.columnCapacity);
		this.coldRow.setText(c, text);
		this.values.set(c, "");
		this.types.set(c, TableCell.Type.STRING);
		text.addEmbeddedStylesToContentAutomaticStyles(this.stylesContainer);
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setTimeValue(long)
	 */
	public void setTimeValue(final int c, final long timeInMillis) {
		this.values.set(c, this.xmlUtil.formatTimeInterval(timeInMillis));
		this.types.set(c, TableCell.Type.TIME);
		this.setDataStyle(c, this.dataStyles.getTimeDataStyle());
	}

	public void setTooltip(final int c, final String tooltip) {
		if (this.coldRow == null)
			this.coldRow = HeavyTableColdRow.create(this.parent, this.xmlUtil, this.rowIndex,
					this.columnCapacity);
		this.coldRow.setTooltip(c, tooltip);
	}

	public void setTooltip(final int c, final String tooltip, final Length width, final Length height, final boolean visible) {
		if (this.coldRow == null)
			this.coldRow = HeavyTableColdRow.create(this.parent, this.xmlUtil, this.rowIndex,
					this.columnCapacity);
		this.coldRow.setTooltip(c, tooltip, width, height, visible);
	}

	public void setVoidValue(final int c) {
		this.values.set(c, null);
		this.types.set(c, TableCell.Type.VOID);
	}

	public void setFormula(final String formula) {
		this.formula = formula;
	}
}