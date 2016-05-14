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
 * @author Martin Schulz<br>
 * 
 * Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net><br>
 * 
 * This file TableCell.java is part of SimpleODS.
 *
 */
public class TableCell {
	private Util u = Util.getInstance();
	public final static int STYLE_STRING = 1;
	public final static int STYLE_FLOAT  = 2;
	public final static int STYLE_PERCENTAGE = 3;
	public final static int STYLE_CURRENCY = 4;
	public final static int STYLE_DATE = 5;
	//public final static int STYLE_TIME = 6;
	//public final static int STYLE_BOOLEAN = 7;

	
	private int  nValueType=STYLE_STRING;
	private String sText="";
	private String sCurrency="EUR";
	private String sValue="";
	private String sDateValue="";
	private String sStyle="";
	private int nColumnsSpanned = 0;
	private int nRowsSpanned = 0;

	
	/**
	 * A table cell.
	 * @param valuetype The value type for this cell.
	 * @param value The String content for this cell.
	 */
	public TableCell(final int valuetype, final String value) {
		this.setValueType(valuetype);
		if (this.nValueType == STYLE_STRING) {
			this.setValue(u.toXmlString(value));
		} else {
			this.setValue(value);
		}
	}
	
	
	public int getValueType() {
		return nValueType;
	}
	
	/**
	 * Set the type of the value for this cell.
	 * @param valueType - TableCell.STYLE_STRING,TableCell.STYLE_FLOAT,TableCell.STYLE_PERCENTAGE,TableCell.STYLE_CURRENCY or TableCell.STYLE_DATE 
	 */
	public void setValueType(final int valueType) {
		nValueType = valueType;
	}
	
	/**
	 * Get the text within this cell.
	 * @return A String with the text
	 */
	public String getText() {
		return sText;
	}
	
	public void setText(String text) {
		sText = text;
	}
	
	/**
	 * Return the currently set currency value.<br>
	 * There is no check if this is really a table cell with style STYLE_CURRENCY.
	 * @return The currency value
	 */
	public String getCurrency() {
		return sCurrency;
	}
	
	/**
	 * Set the currency value and table cell style to STYLE_CURRENCY.
	 * @param currency The currency value
	 */
	public void setCurrency(final String currency) {
		sCurrency = currency;
		this.nValueType=TableCell.STYLE_CURRENCY;
	}
	
	public String getValue() {
		return sValue;
	}
	
	public void setValue(String value) {
		sValue = value;
	}
	
	/**
	 * @return The date value set by setDateValue() or an empty string if nothing was set.
	 */
	public String getDateValue() {
		return sDateValue;
	}
		
	/**
	 * Set the date value for a cell with TableCell.STYLE_DATE.
	 * @param cal - A Calendar object with the date to be used
	 */
	public void setDateValue(final Calendar cal) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sDateValue = sdf.format(cal.getTime());
		sdf = new SimpleDateFormat("dd.MM.yy");
		sValue = sdf.format(cal.getTime());
		this.nValueType = TableCell.STYLE_DATE;
	}
	
	public String getStyle() {
		return sStyle;
	}
	
	public void setStyle(final String style) {
		sStyle = style;
	}
			
	/**
	 * @return The number of columns that this cell spans overs.
	 */
	public int getColumnsSpanned() {
		return nColumnsSpanned;
	}

	/**
	 * To merge cells, set the number of columns that should be merged.
	 * @param n - The number of cells to be merged
	 */
	public void setColumnsSpanned(final int n) {
		if (n < 0) {
			this.nColumnsSpanned = 0;
		} else {
			this.nColumnsSpanned = n;
		}
	}


	/**
	 * @return The number of rows that this cell spans overs.
	 */
	public int getRowsSpanned() {
		return nRowsSpanned;
	}

	/**
	 * To merge cells, set the number of rows that should be merged.
	 * @param n - The number of rows to be merged
	 */
	public void setRowsSpanned(final int n) {
		if (n < 0) {
			this.nRowsSpanned = 0;
		} else {
			this.nRowsSpanned = n;
		}
	}


	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	public String toXML() {
		StringBuffer sbTemp = new StringBuffer();
		
		sbTemp.append("<table:table-cell ");
		if (this.getStyle().length() > 0) {
			u.appendElement(sbTemp, "table:style-name", this.getStyle());
			// sbTemp.append("table:style-name=\"" + this.getStyle() + "\" ");
		}
		
		String valueAttribute = u.escapeXMLAttribute(this.sValue);
		String valueContent = u.escapeXMLContent(this.sValue);
		
		switch (this.nValueType) {
		case STYLE_STRING:
			// sbTemp.append("office:value-type=\"string\" ");
			u.appendElement(sbTemp, "office:value-type", "string");
			break;
		case STYLE_FLOAT:
			// sbTemp.append("office:value-type=\"float\" ");
			// sbTemp.append("office:value=\"" + this.sValue + "\" ");
			u.appendElement(sbTemp, "office:value-type", "float");
			u.appendElement(sbTemp, "office:value", valueAttribute);
			break;
		case STYLE_PERCENTAGE:
			// sbTemp.append("office:value-type=\"percentage\" ");
			// sbTemp.append("office:value=\"" + this.sValue + "\" ");
			u.appendElement(sbTemp, "office:value-type", "percentage");
			u.appendElement(sbTemp, "office:value", valueAttribute);
			break;
		case STYLE_CURRENCY:
			// sbTemp.append("office:value-type=\"currency\" ");
			// sbTemp.append("office:value=\"" + this.sValue + "\" ");
			u.appendElement(sbTemp, "office:value-type", "currency");
			u.appendElement(sbTemp, "office:value", valueAttribute);
			break;
		case STYLE_DATE:
			// sbTemp.append("office:value-type=\"date\" ");
			// sbTemp.append("office:date-value=\"" + this.sDateValue + "\" ");
			u.appendElement(sbTemp, "office:value-type", "date");
			u.appendElement(sbTemp, "office:value", valueAttribute);
			break;
		default:
			// sbTemp.append("office:value-type=\"string\" ");
			u.appendElement(sbTemp, "office:value-type", "string");
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
			u.appendElement(sbTemp, "table:number-columns-spanned", nColumnsSpanned);
			// sbTemp.append("table:number-columns-spanned=\"" +
			// Integer.toString(nColumnsSpanned) + "\" ");
		}
		if (this.nRowsSpanned > 0) {
			u.appendElement(sbTemp, "table:number-rows-spanned", nRowsSpanned);
			// sbTemp.append("table:number-rows-spanned=\"" +
			// Integer.toString(nRowsSpanned) + "\" ");
		}

		sbTemp.append(">");
		sbTemp.append("<text:p>" + valueContent + "</text:p>");
		sbTemp.append("</table:table-cell>");

		return (sbTemp.toString());

	}
	
}
