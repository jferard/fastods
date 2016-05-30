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
package com.github.jferard.fastods.style;

import java.io.IOException;

import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file DateStyle.java is part of FastODS. SimpleODS 0.5.2 Added
 *         Format.MMYY Added Format.WW Added Format.YYYYMMDD
 *
 *         content.xml/office:document-content/office:automatic-styles/number:
 *         date-style
 *         styles.xml/office:document-styles/office:styles/number:date-style
 */
public class DateStyle implements DataStyle {

	public static enum Format {
		/**
		 * Set the date format like '10.07.12'.
		 */
		DDMMYY,

		/**
		 * Set the date format like '10.07.2012'.
		 */
		DDMMYYYY,

		/**
		 * Set the date format like 'July'.
		 */
		MMMM,

		/**
		 * Set the date format like '07.12'.<br>
		 * Month.Year
		 */
		MMYY,

		/**
		 * Set the date format like '10.July 2012'.
		 */
		TMMMMYYYY,

		/**
		 * Set the date format to the weeknumber like '28'.<br>
		 * Week number
		 */
		WW,

		/**
		 * Set the date format like '2012-07-10'.<br>
		 */
		YYYYMMDD;
	}

	/**
	 * The default date format Format.DDMMYY.
	 */
	public static final Format DEFAULT_FORMAT = Format.DDMMYY;
	private static final String DASH = "<number:text>-</number:text>";
	private static final String DAY = "<number:day/>";
	private static final String DOT = "<number:text>.</number:text>";
	private static final String DOT_SPACE = "<number:text>. </number:text>";
	private static final String LONG_DAY = "<number:day number:style=\"long\"/>";
	private static final String LONG_MONTH = "<number:month number:style=\"long\"/>";
	private static final String LONG_TEXTUAL_MONTH = "<number:month number:style=\"long\" number:textual=\"true\"/>";
	private static final String LONG_YEAR = "<number:year number:style=\"long\"/>";
	private static final String SPACE = "<number:text> </number:text>";

	private static final String WEEK = "<number:week-of-year/>";

	private static final String YEAR = "<number:year/>";

	public static DateStyleBuilder builder(final String sName) {
		return new DateStyleBuilder(sName);
	}

	private final boolean bAutomaticOrder;
	private final Format dateFormat;

	/**
	 * The name of this style.
	 */
	private final String sName;

	/**
	 * Create a new date style with the name sName.<br>
	 * Version 0.5.1 Added.
	 *
	 * @param sName
	 *            The name of the number style.
	 * @param odsFile
	 *            The odsFile to which this style belongs to.
	 */
	protected DateStyle(final String sName, final Format dateFormat,
			final boolean bAutomaticOrder) {
		this.sName = sName;
		this.dateFormat = dateFormat;
		this.bAutomaticOrder = bAutomaticOrder;
	}

	/**
	 * @param odsFile
	 *            The OdsFile to which this style belongs to.
	 */
	@Override
	public void addToFile(final OdsFile odsFile) {
		odsFile.addDateStyle(this);
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 */
	@Override
	public void appendXMLToStylesEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<number:date-style");
		util.appendAttribute(appendable, "style:name", this.sName);
		util.appendEAttribute(appendable, "number:automatic-order",
				this.isAutomaticOrder());
		appendable.append(">");

		switch (this.dateFormat) {
		case TMMMMYYYY:
			appendable.append(DateStyle.DAY).append(DateStyle.DOT_SPACE)
					.append(DateStyle.LONG_TEXTUAL_MONTH)
					.append(DateStyle.SPACE).append(DateStyle.LONG_YEAR);
			break;
		case MMMM:
			appendable.append(DateStyle.LONG_TEXTUAL_MONTH);
			break;
		case MMYY:
			appendable.append(DateStyle.LONG_MONTH).append(DateStyle.DOT)
					.append(DateStyle.YEAR);
			break;
		case WW:
			appendable.append(DateStyle.WEEK);
			break;
		case YYYYMMDD:
			appendable.append(DateStyle.LONG_YEAR).append(DateStyle.DASH)
					.append(DateStyle.LONG_MONTH).append(DateStyle.DASH)
					.append(DateStyle.LONG_DAY);
			break;
		case DDMMYYYY:
			appendable.append(DateStyle.LONG_DAY).append(DateStyle.DOT)
					.append(DateStyle.LONG_MONTH).append(DateStyle.DOT)
					.append(DateStyle.LONG_YEAR);
			break;
		case DDMMYY:
			appendable.append(DateStyle.LONG_DAY).append(DateStyle.DOT)
					.append(DateStyle.LONG_MONTH).append(DateStyle.DOT)
					.append(DateStyle.YEAR);
			break;
		default:
			throw new IllegalStateException();
		}

		appendable.append("</number:date-style>");
	}

	/**
	 * @return The name of this style.
	 */
	@Override
	public String getName() {
		return this.sName;
	}

	/**
	 * @return The current value of the automatic order flag
	 */
	public boolean isAutomaticOrder() {
		return this.bAutomaticOrder;
	}

}
