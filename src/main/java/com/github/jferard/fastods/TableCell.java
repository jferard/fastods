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

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file TableCell.java is part of FastODS.
 *         
 * WHERE ?
 * content.xml/office:document-content/office:body/office:spreadsheet/table:table/table:table-row/table:table-cell
 */
public class TableCell {
	public final static int STYLE_STRING = 1;
	public final static int STYLE_FLOAT = 2;
	public final static int STYLE_PERCENTAGE = 3;
	public final static int STYLE_CURRENCY = 4;
	public final static int STYLE_DATE = 5;
	// public final static int STYLE_TIME = 6;
	// public final static int STYLE_BOOLEAN = 7;
	private Util u = Util.getInstance();

	private int nValueType = STYLE_STRING;
	private String sText = "";
	private String sCurrency = "EUR";
	private String sValue = "";
	private String sDateValue = "";
	private String sStyle = "";
	private int nColumnsSpanned = 0;
	private int nRowsSpanned = 0;

	/**
	 * A table cell.
	 * 
	 * @param valuetype
	 *            The value type for this cell.
	 * @param value
	 *            The String content for this cell.
	 */
	public TableCell(final int valuetype, final String value) {
		this.setValueType(valuetype);
		if (this.nValueType == STYLE_STRING) {
			this.setValue(this.u.toXmlString(value));
		} else {
			this.setValue(value);
		}
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

	public int getValueType() {
		return this.nValueType;
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
		this.nValueType = TableCell.STYLE_CURRENCY;
	}

	/**
	 * Set the date value for a cell with TableCell.STYLE_DATE.
	 * 
	 * @param cal
	 *            - A Calendar object with the date to be used
	 */
	public void setDateValue(final Calendar cal) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.sDateValue = sdf.format(cal.getTime());
		sdf = new SimpleDateFormat("dd.MM.yy");
		this.sValue = sdf.format(cal.getTime());
		this.nValueType = TableCell.STYLE_DATE;
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
	public void setValueType(final int valueType) {
		this.nValueType = valueType;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	public String toXML(Util util) {
		StringBuilder sbTemp = new StringBuilder();

		sbTemp.append("<table:table-cell ");
		if (this.sStyle.length() > 0) {
			util.appendAttribute(sbTemp, "table:style-name", this.getStyle());
		}

		switch (this.nValueType) {
		case STYLE_STRING:
			util.appendAttribute(sbTemp, "office:value-type", "string");
			break;
		case STYLE_FLOAT:
			util.appendAttribute(sbTemp, "office:value-type", "float");
			util.appendAttribute(sbTemp, "office:value", this.sValue);
			break;
		case STYLE_PERCENTAGE:
			util.appendAttribute(sbTemp, "office:value-type", "percentage");
			util.appendAttribute(sbTemp, "office:value", this.sValue);
			break;
		case STYLE_CURRENCY:
			util.appendAttribute(sbTemp, "office:value-type", "currency");
			util.appendAttribute(sbTemp, "office:value", this.sCurrency);
			break;
		case STYLE_DATE:
			util.appendAttribute(sbTemp, "office:value-type", "date");
			util.appendAttribute(sbTemp, "office:value", this.sDateValue);
			break;
		default:
			util.appendAttribute(sbTemp, "office:value-type", "string");
			/*
			 * case STYLE_TIME:
			 * sbTemp.append("office:value-type=\"time-value\" ");
			 * sbTemp.append("office:value=\""+this.sValue+"\" "); case
			 * STYLE_BOOLEAN:
			 * sbTemp.append("office:value-type=\"boolean-value\" ");
			 * sbTemp.append("office:value=\""+this.sValue+"\" ");
			 */
		}

		if (this.nColumnsSpanned > 0) {
			util.appendAttribute(sbTemp, "table:number-columns-spanned",
					this.nColumnsSpanned);
		}
		if (this.nRowsSpanned > 0) {
			util.appendAttribute(sbTemp, "table:number-rows-spanned",
					this.nRowsSpanned);
		}

		sbTemp.append(">");
		sbTemp.append("<text:p>").append(util.escapeXMLContent(this.sValue)).append("</text:p>");
		sbTemp.append("</table:table-cell>");

		return (sbTemp.toString());

	}

}
