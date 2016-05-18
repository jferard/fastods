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
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file DateStyle.java is part of FastODS. SimpleODS 0.5.2 Added
 *         DATEFORMAT_MMYY Added DATEFORMAT_WW Added DATEFORMAT_YYYYMMDD
 */
public class DateStyle implements NamedObject {

	private static final String DOT_SPACE = "<number:text>. </number:text>";
	private static final String SPACE = "<number:text> </number:text>";
	private static final String DAY = "<number:day/>";
	private static final String WEEK = "<number:week-of-year/>";
	private static final String YEAR = "<number:year/>";
	private static final String LONG_TEXTUAL_MONTH = "<number:month number:style=\"long\" number:textual=\"true\"/>";
	private static final String DASH = "<number:text>-</number:text>";
	private static final String LONG_YEAR = "<number:year number:style=\"long\"/>";
	private static final String LONG_MONTH = "<number:month number:style=\"long\"/>";
	private static final String LONG_DAY = "<number:day number:style=\"long\"/>";
	private static final String DOT = "<number:text>.</number:text>";

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
	/**
	 * The default date format DATEFORMAT_DDMMYY.
	 */
	public static final int DEFAULT_DATEFORMAT = DATEFORMAT_DDMMYY;

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
	public static final int DATEFORMAT_TMMMMYYYY = 3;
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

	public static DateStyleBuilder builder() {
		return new DateStyleBuilder();
	}

	/**
	 * The name of this style.
	 */
	private final String sName;
	private final boolean bAutomaticOrder;

	private final int nDateFormat;

	private String xml;

	/**
	 * Create a new date style with the name sName.<br>
	 * Version 0.5.1 Added.
	 * 
	 * @param sName
	 *            The name of the number style.
	 * @param odsFile
	 *            The odsFile to which this style belongs to.
	 */
	protected DateStyle(final String sName, final int nDateFormat,
			final boolean bAutomaticOrder) {
		this.sName = sName;
		this.nDateFormat = nDateFormat;
		this.bAutomaticOrder = bAutomaticOrder;
	}

	/**
	 * @param odsFile
	 *            The OdsFile to which this style belongs to.
	 */
	public void addToFile(OdsFile odsFile) {
		odsFile.getStyles().addDateStyle(this);
	}

	/**
	 * @return The name of this style.
	 */
	public String getName() {
		return this.sName;
	}

	/**
	 * @return The current value of the automatic order flag
	 */
	public boolean isAutomaticOrder() {
		return this.bAutomaticOrder;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	public String toXML(Util util) {
		if (this.xml == null) {
			StringBuilder sbReturn = new StringBuilder();

			sbReturn.append("<number:date-style style:name=\"")
					.append(util.escapeXMLAttribute(this.getName()))
					.append("\" number:automatic-order=\"")
					.append(this.isAutomaticOrder()).append("\">");

			switch (this.nDateFormat) {
			case DateStyle.DEFAULT_DATEFORMAT:
				sbReturn.append(LONG_DAY).append(DOT).append(LONG_MONTH)
						.append(DOT).append(YEAR);
				break;
			case DateStyle.DATEFORMAT_TMMMMYYYY:
				sbReturn.append(DAY).append(DOT_SPACE)
						.append(LONG_TEXTUAL_MONTH).append(SPACE)
						.append(LONG_YEAR);
				break;
			case DateStyle.DATEFORMAT_MMMM:
				sbReturn.append(LONG_TEXTUAL_MONTH);
				break;
			case DateStyle.DATEFORMAT_MMYY:
				sbReturn.append(LONG_MONTH).append(DOT).append(YEAR);
				break;
			case DateStyle.DATEFORMAT_WW:
				sbReturn.append(WEEK);
				break;
			case DateStyle.DATEFORMAT_YYYYMMDD:
				sbReturn.append(LONG_YEAR).append(DASH).append(LONG_MONTH)
						.append(DASH).append(LONG_DAY);
				break;
			case DateStyle.DATEFORMAT_DDMMYYYY:
			default:
				sbReturn.append(LONG_DAY).append(DOT).append(LONG_MONTH)
						.append(DOT).append(LONG_YEAR);
			}

			sbReturn.append("</number:date-style>");

			this.xml = sbReturn.toString();
		}
		return this.xml;
	}

}
