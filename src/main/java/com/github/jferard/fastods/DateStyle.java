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

/**
 * @author Martin Schulz<br>
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file DateStyle.java is part of SimpleODS.<br>
 *         0.5.2 Added DATEFORMAT_MMYY Added DATEFORMAT_WW Added
 *         DATEFORMAT_YYYYMMDD
 *
 */
public class DateStyle {

	/**
	 * Set the date format like '10.07.2012'.
	 */
	public static final int DATEFORMAT_DDMMYYYY = 1;
	/*
	<number:date-style style:name="N36" number:automatic-order="true">
	<number:day number:style="long"/>
	<number:text>.</number:text>
	<number:month number:style="long"/>
	<number:text>.</number:text>
	<number:year number:style="long"/>
	</number:date-style>
	*/
	/**
	 * Set the date format like '10.07.12'.
	 */
	public static final int DATEFORMAT_DDMMYY = 2;
	/*
	<number:date-style style:name="N37" number:automatic-order="true">
	<number:day number:style="long"/>
	<number:text>.</number:text>
	<number:month number:style="long"/>
	<number:text>.</number:text>
	<number:year/></number:date-style>
	*/
	/**
	 * Set the date format like '10.July 2012'.
	 */
	public static final int DATEFORMAT_TMMMMYY = 3;
	/*
	<number:date-style style:name="N76" number:automatic-order="true">
	<number:day/>
	<number:text>. </number:text>
	<number:month number:style="long" number:textual="true"/>
	<number:text> </number:text>
	<number:year number:style="long"/>
	*/
	/**
	 * Set the date format like 'July'.
	 */
	public static final int DATEFORMAT_MMMM = 4;
	/*
	<number:date-style style:name="N34">
	<number:month number:style="long" number:textual="true"/>
	</number:date-style>
	*/
	/**
	 * Set the date format like '07.12'.<br>
	 * Month.Year
	 */
	public static final int DATEFORMAT_MMYY = 5;
	/*
	 * <number:date-style style:name="N32" number:automatic-order="true">
	 * <number:month number:style="long"/>
	 * <number:text>.</number:text>
	 * <number:year/>
	 * </number:date-style>
	 */

	/**
	 * Set the date format to the weeknumber like '28'.<br>
	 * Week number
	 */
	public static final int DATEFORMAT_WW = 6;
	/* 
	 * <number:date-style style:name="N85">
	 * <number:week-of-year/>
	 * </number:date-style>
	 */

	/**
	 * Set the date format like '2012-07-10'.<br>
	 */
	public static final int DATEFORMAT_YYYYMMDD = 7;
	/* 
	 * <number:year number:style="long"/>
	 * <number:text>-</number:text>
	 * <number:month number:style="long"/>
	 * <number:text>-</number:text>
	 * <number:day number:style="long"/>
	 */

	/**
	 * The name of this style.
	 */
	private String sName = "";

	private boolean bAutomaticOrder = false;

	/**
	 * The default date format DATEFORMAT_DDMMYY.
	 */
	private int nDateFormat = DATEFORMAT_DDMMYY;

	/**
	 * The OdsFile to which this style belongs to.
	 */
	private OdsFile o;

	/**
	 * Create a new date style with the name sName.<br>
	 * Version 0.5.1 Added.
	 * 
	 * @param sName
	 *            The name of the number style.
	 * @param odsFile
	 *            The odsFile to which this style belongs to.
	 */
	public DateStyle(final String sName, final OdsFile odsFile) {
		this.setName(sName);
		this.o = odsFile;
		this.o.getStyles().addDateStyle(this);
	}

	/**
	 * @return The name of this style.
	 */
	public String getName() {
		return this.sName;
	}

	/**
	 * Set the name of this style to sName.
	 * 
	 * @param name
	 *            - The name of this style.
	 */
	public void setName(final String name) {
		this.sName = name;
	}

	/**
	 * @return The current value of the automatic order flag
	 */
	public boolean isAutomaticOrder() {
		return this.bAutomaticOrder;
	}

	/**
	 * The automatic-order attribute can be used to automatically order data to
	 * match the default order<br>
	 * for the language and country of the date style.
	 * 
	 * @param bAutomatic
	 */
	public void setAutomaticOrder(final boolean bAutomatic) {
		this.bAutomaticOrder = bAutomatic;
	}

	/**
	 * Set the date format.<br>
	 * Valid is one of the following:<br>
	 * DateStyle.DATEFORMAT_DDMMYYYY<br>
	 * DateStyle.DATEFORMAT_DDMMYY<br>
	 * DateStyle.DATEFORMAT_TMMMMYY<br>
	 * DateStyle.DATEFORMAT_MMMM<br>
	 * *
	 * 
	 * @param nFormat
	 *            The date format to be used.
	 */
	public void setDateFormat(final int nFormat) {
		this.nDateFormat = nFormat;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	public String toXML() {
		StringBuilder sbReturn = new StringBuilder();

		sbReturn.append("<number:date-style style:name=\"" + this.getName()
				+ "\" number:automatic-order=\"" + this.isAutomaticOrder()
				+ "\">");

		switch (this.nDateFormat) {
		case DateStyle.DATEFORMAT_DDMMYYYY:
			sbReturn.append("<number:day number:style=\"long\"/>");
			sbReturn.append("<number:text>.</number:text>");
			sbReturn.append("<number:month number:style=\"long\"/>");
			sbReturn.append("<number:text>.</number:text>");
			sbReturn.append("<number:year number:style=\"long\"/>");
			break;
		case DateStyle.DATEFORMAT_DDMMYY:
			sbReturn.append("<number:day number:style=\"long\"/>");
			sbReturn.append("<number:text>.</number:text>");
			sbReturn.append("<number:month number:style=\"long\"/>");
			sbReturn.append("<number:text>.</number:text>");
			sbReturn.append("<number:year/>");
			break;
		case DateStyle.DATEFORMAT_TMMMMYY:
			sbReturn.append("<number:day/>");
			sbReturn.append("<number:text>. </number:text>");
			sbReturn.append(
					"<number:month number:style=\"long\" number:textual=\"true\"/>");
			sbReturn.append("<number:text> </number:text>");
			sbReturn.append("<number:year number:style=\"long\"/>");
			break;
		case DateStyle.DATEFORMAT_MMMM:
			sbReturn.append(
					"<number:month number:style=\"long\" number:textual=\"true\"/>");
			break;
		case DateStyle.DATEFORMAT_MMYY:
			sbReturn.append("<number:month number:style=\"long\"/>");
			sbReturn.append("<number:text>.</number:text>");
			sbReturn.append("<number:year/>");
			break;
		case DateStyle.DATEFORMAT_WW:
			sbReturn.append("<number:week-of-year/>");
			break;
		case DateStyle.DATEFORMAT_YYYYMMDD:
			sbReturn.append("<number:year number:style=\"long\"/>");
			sbReturn.append("<number:text>-</number:text>");
			sbReturn.append("<number:month number:style=\"long\"/>");
			sbReturn.append("<number:text>-</number:text>");
			sbReturn.append("<number:day number:style=\"long\"/>");
			break;
		default:
			sbReturn.append("<number:day number:style=\"long\"/>");
			sbReturn.append("<number:text>.</number:text>");
			sbReturn.append("<number:month number:style=\"long\"/>");
			sbReturn.append("<number:text>.</number:text>");
			sbReturn.append("<number:year number:style=\"long\"/>");
		}

		sbReturn.append("</number:date-style>");

		return sbReturn.toString();
	}

}
