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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.github.jferard.fastods.style.DataStyles;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file TableCell.java is part of FastODS.
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:body/office:spreadsheet/
 *         table:table/table:table-row/table:table-cell
 */
public class TableCell {

	/**
	 * 19.385 office:value-type
	 */
	public static enum Type {
		BOOLEAN("boolean"), CURRENCY("currency"), DATE("date"), FLOAT(
				"float"), PERCENTAGE("percentage"), STRING(
						"string"), TIME("time"), VOID("void");

		private final String attrValue;

		private Type(final String attrValue) {
			this.attrValue = attrValue;
		}

		public String getAttrValue() {
			return this.attrValue;
		}
	}

	public static final Type DEFAULT_TYPE = Type.STRING;

	/**
	 * XML Schema Part 2, 3.2.7 dateTime
	 */
	private static final SimpleDateFormat DATE_VALUE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS");

	private final int nCol;
	private int nColumnsSpanned;
	private final int nRow;
	private int nRowsSpanned;
	private final OdsFile odsFile;
	private String sCurrency;
	private String sDateValue;
	private String sValue;
	private String sText;

	/**
	 * 19.384 office:value
	 */
	private String sBooleanValue;
	private Type valueType;

	private TableCellStyle style;

	private String sStringValue;

	private String sTimeValue;

	private DataStyles dataStyles;

	private Util util;

	/**
	 * A table cell.
	 *
	 * @param valuetype
	 *            The value type for this cell.
	 * @param value
	 *            The String content for this cell.
	 */
	TableCell(final OdsFile odsFile, Util util, final DataStyles dataStyles,
			final int nRow, final int nCol, final Type valuetype,
			final String value) {
		this.util = util;
		this.dataStyles = dataStyles;
		this.sCurrency = "EUR";
		this.odsFile = odsFile;
		this.nRow = nRow;
		this.nCol = nCol;
		this.valueType = valuetype;
		this.sBooleanValue = value;
		this.sDateValue = "";
		this.nColumnsSpanned = 0;
		this.nRowsSpanned = 0;
	}

	public void appendXMLToTableRow(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<table:table-cell");
		if (this.style != null) {
			util.appendAttribute(appendable, "table:style-name",
					this.style.getName());
		}

		util.appendEAttribute(appendable, "office:value-type",
				this.valueType.attrValue);
		switch (this.valueType) {
		case BOOLEAN:
			util.appendAttribute(appendable, "office:boolean-value",
					this.sBooleanValue);
			break;
		case CURRENCY:
			util.appendAttribute(appendable, "office:currency", this.sCurrency);
			//$FALL-THROUGH$
		case FLOAT:
		case PERCENTAGE:
			util.appendAttribute(appendable, "office:value", this.sValue);
			break;
		case DATE:
			util.appendAttribute(appendable, "office:date-value",
					this.sDateValue);
			break;
		case STRING:
			util.appendAttribute(appendable, "office:string-value",
					this.sStringValue);
			break;
		case TIME:
			util.appendAttribute(appendable, "office:time-value",
					this.sTimeValue);
			break;
		case VOID:
		default:
			break;
		}

		if (this.nColumnsSpanned > 0) {
			util.appendEAttribute(appendable, "table:number-columns-spanned",
					this.nColumnsSpanned);
		}
		if (this.nRowsSpanned > 0) {
			util.appendEAttribute(appendable, "table:number-rows-spanned",
					this.nRowsSpanned);
		}

//		if (this.sText == null)
			appendable.append("/>");
//		else {
//			appendable.append("><text:p>").append(this.sText)
//					.append("</text:p>");
//			appendable.append("</table:table-cell>");
//		}
	}

	/**
	 * Return the currently set boolean value.
	 *
	 * @return The currency value
	 */
	public String getBooleanValue() {
		return this.sBooleanValue;
	}

	/**
	 * @return The number of columns that this cell spans overs.
	 */
	public int getColumnsSpanned() {
		return this.nColumnsSpanned;
	}

	/**
	 * Return the currently set currency value.<br>
	 * There is no check if this is really a table cell with style
	 * STYLE_CURRENCY.
	 *
	 * @return The currency value
	 */
	public String getCurrency() {
		return this.sCurrency;
	}

	/**
	 * Return the currently set currency value.<br>
	 * There is no check if this is really a table cell with style
	 * STYLE_CURRENCY.
	 *
	 * @return The currency value
	 */
	public String getCurrencyValue() {
		return this.sValue;
	}

	/**
	 * @return The date value set by setDateValue() or an empty string if
	 *         nothing was set.
	 */
	public String getDateValue() {
		return this.sDateValue;
	}

	/**
	 * @return The float value set by setFloatValue() or an empty string if
	 *         nothing was set.
	 */
	public String getFloatValue() {
		return this.sValue;
	}

	/**
	 * @return The percentage value set by setPercentageValue() or an empty
	 *         string if nothing was set.
	 */
	public String getPercentageValue() {
		return this.sValue;
	}

	/**
	 * @return The number of rows that this cell spans overs.
	 */
	public int getRowsSpanned() {
		return this.nRowsSpanned;
	}

	/**
	 * @return The string value set by setStringValue() or an empty string if
	 *         nothing was set.
	 */
	public String getStringValue() {
		return this.sStringValue;
	}

	/**
	 * @return The timee value set by setTimeValue() or an empty string if
	 *         nothing was set.
	 */
	public String getTimeValue() {
		return this.sTimeValue;
	}

	public String getStyleName() {
		return this.style.getName();
	}

	public Type getValueType() {
		return this.valueType;
	}

	/**
	 * Set the currency value and table cell style to STYLE_CURRENCY.
	 *
	 * @param currency
	 *            The currency value
	 */
	public void setBooleanValue(final Boolean value) {
		this.sBooleanValue = value.toString();
		this.valueType = TableCell.Type.BOOLEAN;
		this.setStyle(this.dataStyles.getBooleanStyle());
	}

	/**
	 * To merge cells, set the number of columns that should be merged.
	 *
	 * @param n
	 *            - The number of cells to be merged
	 */
	public void setColumnsSpanned(final int n) {
		if (n < 0) {
			this.nColumnsSpanned = 0;
		} else {
			this.nColumnsSpanned = n;
		}
	}

	/**
	 * Set the currency value and table cell style to STYLE_CURRENCY.
	 *
	 * @param currency
	 *            The currency value
	 */
	public void setCurrencyValue(final Number value, final String currency) {
		this.sBooleanValue = value.toString();
		this.sCurrency = currency; // escape here
		this.valueType = TableCell.Type.CURRENCY;
		this.setStyle(this.dataStyles.getCurrencyStyle());
	}

	/**
	 * Set the date value for a cell with TableCell.STYLE_DATE.
	 *
	 * @param cal
	 *            - A Calendar object with the date to be used
	 */
	public void setDateValue(final Calendar cal) {
		this.setDateValue(cal.getTime());
	}

	public void setDateValue(Date value) {
		this.sDateValue = TableCell.DATE_VALUE_FORMAT.format(value);
		this.valueType = TableCell.Type.DATE;
		this.setStyle(this.dataStyles.getDateStyle());
	}

	/**
	 * Set the float value for a cell with TableCell.Type.FLOAT.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	public void setFloatValue(final Number value) {
		this.sValue = value.toString();
		this.valueType = TableCell.Type.FLOAT;
		this.setStyle(this.dataStyles.getNumberStyle());
	}

	/**
	 * Set the float value for a cell with TableCell.Type.STRING.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	public void setObjectValue(final Object value) {
		if (value == null)
			return;

		Object retValue;
		if (value instanceof String)
			this.setStringValue((String) value);
		else if (value instanceof Number)
			this.setFloatValue((Number) value);
		else if (value instanceof Date) {
			this.setDateValue((Date) value);
		} else if (value instanceof Calendar) {
			this.setDateValue((Calendar) value);
		} else
			this.setStringValue(value.toString());
	}

	/**
	 * Set the float value for a cell with TableCell.Type.PERCENTAGE.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	public void setPercentageValue(final Number value) {
		this.sValue = value.toString();
		this.valueType = TableCell.Type.PERCENTAGE;
		this.setStyle(this.dataStyles.getPercentageStyle());
	}

	/**
	 * To merge cells, set the number of rows that should be merged.
	 *
	 * @param n
	 *            - The number of rows to be merged
	 */
	public void setRowsSpanned(final int n) {
		if (n < 0) {
			this.nRowsSpanned = 0;
		} else {
			this.nRowsSpanned = n;
		}
	}

	/**
	 * Set the float value for a cell with TableCell.Type.STRING.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	public void setStringValue(final String value) {
		this.sStringValue = value;// TODO ESCAPE
		this.valueType = TableCell.Type.STRING;
	}

	public void setStyle(final TableCellStyle style) {
		if (style == null)
			return;
		
		style.addToFile(this.odsFile);
		if (style.getDataStyle() == null && this.style != null && this.style.getDataStyle() != null)
			style.setDataStyle(this.style.getDataStyle());
		this.style = style;
	}

	public void setTimeValue(final long timeInMillis) {
		this.sTimeValue = this.util.formatTimeInterval(timeInMillis);
		this.valueType = TableCell.Type.TIME;
		this.setStyle(this.dataStyles.getTimeStyle());
	}

	public void setFormat(DataStyles format) {
		this.dataStyles = format;
	}
}
