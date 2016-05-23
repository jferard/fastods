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
public class TableCell implements XMLAppendable {
	public static enum Type {
		FLOAT("float"), PERCENTAGE("percentage"), CURRENCY(
				"currency"), DATE("date"), STRING("string");
		
		private final String attrValue;

		private Type(String attrValue) {
			this.attrValue = attrValue;
		}

		public String getAttrValue() {
			return this.attrValue;
		}
	}

	public static Type DEFAULT_TYPE = Type.STRING;

	private Type valueType;
	private String sText;
	private String sCurrency;
	private String sValue;
	private String sDateValue;
	private String sStyle;
	private int nColumnsSpanned;
	private int nRowsSpanned;
	private OdsFile odsFile;
	private int nRow;
	private int nCol;

	private static SimpleDateFormat DATE_VALUE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static SimpleDateFormat VALUE_FORMAT = new SimpleDateFormat(
			"dd.MM.yy");

	/**
	 * A table cell.
	 * 
	 * @param valuetype
	 *            The value type for this cell.
	 * @param value
	 *            The String content for this cell.
	 */
	TableCell(OdsFile odsFile, int nRow, int nCol, final Type valuetype,
			final String value) {
		this.sText = "";
		this.sCurrency = "EUR";
		this.odsFile = odsFile;
		this.nRow = nRow;
		this.nCol = nCol;
		this.valueType = valuetype;
		this.sValue = value;
		this.sDateValue = "";
		this.sStyle = "";
		this.nColumnsSpanned = 0;
		this.nRowsSpanned = 0;
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
	 * @return The date value set by setDateValue() or an empty string if
	 *         nothing was set.
	 */
	public String getDateValue() {
		return this.sDateValue;
	}

	/**
	 * @return The number of rows that this cell spans overs.
	 */
	public int getRowsSpanned() {
		return this.nRowsSpanned;
	}

	public String getStyle() {
		return this.sStyle;
	}

	/**
	 * Get the text within this cell.
	 * 
	 * @return A String with the text
	 */
	public String getText() {
		return this.sText;
	}

	public String getValue() {
		return this.sValue;
	}

	public Type getValueType() {
		return this.valueType;
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
	public void setCurrency(final String currency) {
		this.sCurrency = currency;
		this.valueType = TableCell.Type.CURRENCY;
	}

	/**
	 * Set the date value for a cell with TableCell.STYLE_DATE.
	 * 
	 * @param cal
	 *            - A Calendar object with the date to be used
	 */
	public void setDateValue(final Calendar cal) {
		this.sDateValue = DATE_VALUE_FORMAT.format(cal.getTime());
		this.sValue = VALUE_FORMAT.format(cal.getTime());
		this.valueType = TableCell.Type.DATE;
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

	public void setStyle(TableCellStyle style) {
		style.addToFile(this.odsFile);
		this.sStyle = style.getName();
	}

	public void setStyle(final String style) {
		this.sStyle = style;
	}

	public void setText(String text) {
		this.sText = text;
	}

	public void setValue(String value) {
		this.sValue = value;
	}

	/**
	 * Set the type of the value for this cell.
	 * 
	 * @param valueType
	 *            - TableCell.STYLE_STRING,TableCell.STYLE_FLOAT,TableCell.
	 *            STYLE_PERCENTAGE,TableCell.STYLE_CURRENCY or
	 *            TableCell.STYLE_DATE
	 */
	public void setValueType(final Type valueType) {
		this.valueType = valueType;
	}

	@Override
	public void appendXML(Util util, Appendable appendable) throws IOException {
		appendable.append("<table:table-cell ");
		if (this.sStyle.length() > 0) {
			util.appendAttribute(appendable, "table:style-name",
					this.getStyle());
		}
		
		util.appendEAttribute(appendable, "office:value-type", this.valueType.attrValue);

		switch (this.valueType) {
		case CURRENCY:
			util.appendAttribute(appendable, "office:value", this.sCurrency);
			break;
		case DATE:
			util.appendAttribute(appendable, "office:value", this.sDateValue);
			break;
		case STRING:
			break;
		default:
			util.appendAttribute(appendable, "office:value", this.sValue);
		}

		if (this.nColumnsSpanned > 0) {
			util.appendAttribute(appendable, "table:number-columns-spanned",
					this.nColumnsSpanned);
		}
		if (this.nRowsSpanned > 0) {
			util.appendAttribute(appendable, "table:number-rows-spanned",
					this.nRowsSpanned);
		}

		appendable.append("><text:p>").append(util.escapeXMLContent(this.sValue))
				.append("</text:p>");
		appendable.append("</table:table-cell>");
	}

}
