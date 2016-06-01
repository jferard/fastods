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
import java.util.Locale;

import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Martin Schulz Copyright
 *         2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file TimeeStyle.java is part of FastODS. SimpleODS 0.5.2 Added
 *         Format.MMYY Added Format.WW Added Format.YYYYMMDD
 *
 *         content.xml/office:document-content/office:automatic-styles/number:
 *         date-style
 *         styles.xml/office:document-styles/office:styles/number:date-style
 */
public class TimeStyle implements DataStyle {

	public static enum Format {
		/**
		 * Set the time format like '01:02:03'.
		 */
		HHMMSS
	}

	/**
	 * The default date format Format.DDMMYY.
	 */
	private static final String DASH = "<number:text>-</number:text>";
	private static final String DOT = "<number:text>.</number:text>";
	private static final String COLON = "<number:text>:</number:text>";
	private static final String HOURS = "<number:hours/>";
	private static final String MINUTES = "<number:minutes/>";
	private static final String SECONDS = "<number:seconds/>";

	public static TimeStyleBuilder builder(final String sName) {
		return new TimeStyleBuilder(sName, Locale.getDefault());
	}

	private final TimeStyle.Format timeFormat;
	private final String sCountry;
	private final String sLanguage;

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
	protected TimeStyle(final String sName, final Format timeFormat,
			final String sCountry, final String sLanguage) {
		this.sName = sName;
		this.timeFormat = timeFormat;
		this.sCountry = sCountry;
		this.sLanguage = sLanguage;
	}

	/**
	 * @return The two letter country code, e.g. 'US'
	 */
	public String getCountry() {
		return this.sCountry;
	}

	/**
	 * @param odsFile
	 *            The OdsFile to which this style belongs to.
	 */
	@Override
	public void addToFile(final OdsFile odsFile) {
		odsFile.addDataStyle(this);
	}

	/**
	 * @return The two letter language code, e.g. 'en'.
	 */
	public String getLanguage() {
		return this.sLanguage;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 */
	@Override
	public void appendXMLToStylesEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<number:time-style");
		util.appendAttribute(appendable, "style:name", this.sName);
		if (this.sLanguage != null)
			util.appendAttribute(appendable, "number:language", this.sLanguage);
		if (this.sCountry != null)
			util.appendAttribute(appendable, "number:country", this.sCountry);
		if (this.timeFormat == null) {
			util.appendEAttribute(appendable, "number:format-source",
					"language");
			appendable.append("/>");
		} else {
			util.appendEAttribute(appendable, "number:format-source", "fixed");
			appendable.append(">");

			switch (this.timeFormat) {
			case HHMMSS:
			default:
				appendable.append(TimeStyle.HOURS).append(TimeStyle.COLON)
						.append(TimeStyle.MINUTES).append(TimeStyle.COLON)
						.append(TimeStyle.SECONDS);
				break;
			}

			appendable.append("</number:time-style>");
		}
	}

	/**
	 * @return The name of this style.
	 */
	@Override
	public String getName() {
		return this.sName;
	}
}
