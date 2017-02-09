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

import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Julien Férard
 */
public interface TableCell {
	/**
	 * 19.385 office:value-type
	 */
	public static enum Type {
		BOOLEAN("office:boolean-value", "boolean"), CURRENCY("office:value",
				"currency"), DATE("office:date-value", "date"), FLOAT(
						"office:value", "float"), PERCENTAGE("office:value",
								"percentage"), STRING("office:string-value",
										"string"), TIME("office:time-value",
												"time"), VOID(
														"", "office-value");

		final String attrName;
		final String attrValue;

		private Type(final String attrName, final String attrValue) {
			this.attrValue = attrValue;
			this.attrName = attrName;
		}

		public String getAttrName() {
			return this.attrName;
		}

		public String getAttrValue() {
			return this.attrValue;
		}
	}

	/**
	 * XML Schema Part 2, 3.2.7 dateTime
	 */
	SimpleDateFormat DATE_VALUE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS");

	void appendXMLToTableRow(XMLUtil util, Appendable appendable)
			throws IOException;

	/**
	 * Return the currently set boolean value.
	 *
	 * @return The currency value
	 */
	String getBooleanValue();

	/**
	 * @return The number of columns that this cell spans overs.
	 */
	int getColumnsSpanned();

	/**
	 * Return the currently set currency value.<br>
	 * There is no check if this is really a table cell with style
	 * STYLE_CURRENCY.
	 *
	 * @return The currency value
	 */
	String getCurrency();

	/**
	 * Return the currently set currency value.<br>
	 * There is no check if this is really a table cell with style
	 * STYLE_CURRENCY.
	 *
	 * @return The currency value
	 */
	String getCurrencyValue();

	/**
	 * @return The date value set by setDateValue() or an empty string if
	 *         nothing was set.
	 */
	String getDateValue();

	/**
	 * @return The float value set by setFloatValue() or an empty string if
	 *         nothing was set.
	 */
	String getFloatValue();

	/**
	 * @return The percentage value set by setPercentageValue() or an empty
	 *         string if nothing was set.
	 */
	String getPercentageValue();

	/**
	 * @return The number of rows that this cell spans overs.
	 */
	int getRowsSpanned();

	/**
	 * @return The string value set by setStringValue() or an empty string if
	 *         nothing was set.
	 */
	String getStringValue();

	String getStyleName();

	/**
	 * @return The timee value set by setTimeValue() or an empty string if
	 *         nothing was set.
	 */
	String getTimeValue();

	/**
	 * @return the tooltip text or null if no tooltip
	 */
	String getTooltip();

	Type getValueType();

	/**
	 * Set the boolean value
	 *
	 * @param value true or false
	 */
	void setBooleanValue(boolean value);

	/**
	 * Set the float value for a cell with TableCell.Type.STRING.
	 *
	 * @param value
	 *            the value as a CellValue object.
	 *
	 */
	void setCellValue(CellValue value);

	/**
	 * To merge cells, set the number of columns that should be merged.
	 *
	 * @param n
	 *            - The number of cells to be merged
	 */
	void setColumnsSpanned(int n);

	/**
	 * Set the currency value and table cell style to STYLE_CURRENCY.
	 *
	 * @param value the value as a float
	 * @param currency
	 *            The currency value
	 */
	void setCurrencyValue(float value, String currency);

	/**
	 * Set the currency value and table cell style to STYLE_CURRENCY.
	 *
	 * @param value the value as an int
	 * @param currency
	 *            The currency value
	 */
	void setCurrencyValue(int value, String currency);

	/**
	 * Set the currency value and table cell style to STYLE_CURRENCY.
	 *
	 * @param value the value as a Number
	 * @param currency
	 *            The currency value
	 */
	void setCurrencyValue(Number value, String currency);

	/**
	 * Set the date value for a cell with OldHeavyTableCell.STYLE_DATE.
	 *
	 * @param cal
	 *            - A Calendar object with the date to be used
	 */
	void setDateValue(Calendar cal);

	void setDateValue(Date value);

	/**
	 * Set the float value for a cell with TableCell.Type.FLOAT.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setFloatValue(float value);

	/**
	 * Set the float value for a cell with TableCell.Type.FLOAT.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setFloatValue(int value);

	/**
	 * Set the float value for a cell with TableCell.Type.FLOAT.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setFloatValue(Number value);

	/**
	 * Set the float value for a cell with TableCell.Type.STRING.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setObjectValue(Object value);

	/**
	 * Set the float value for a cell with TableCell.Type.PERCENTAGE.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setPercentageValue(float value);

	/**
	 * Set the float value for a cell with TableCell.Type.PERCENTAGE.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setPercentageValue(Number value);

	/**
	 * To merge cells, set the number of rows that should be merged.
	 *
	 * @param n
	 *            - The number of rows to be merged
	 */
	void setRowsSpanned(int n) throws IOException;

	/**
	 * Set the float value for a cell with TableCell.Type.STRING.
	 *
	 * @param value
	 *            - A double object with the value to be used
	 */
	void setStringValue(String value);

	void setStyle(TableCellStyle style);

	void setTimeValue(long timeInMillis);

	/**
	 * Add a tooltip to the cell
	 *
	 * @param tooltip
	 *            the text of the tooltip
	 */
	void setTooltip(String tooltip);
}