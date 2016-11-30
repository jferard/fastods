/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods.datastyle;

import java.io.IOException;

import com.github.jferard.fastods.util.XMLUtil;

/**
 * content.xml/office:document-content/office:automatic-styles/number:
 * time-style styles.xml/office:document-styles/office:styles/number:time-style
 *
 * @author Julien Férard
 */
public class TimeStyle extends DataStyle {

	public static enum Format {
		/**
		 * Set the time format like '01:02:03'.
		 */
		HHMMSS
	}

	private static final String COLON = "<number:text>:</number:text>";
	/**
	 * The default date format Format.DDMMYY.
	 */
	private static final String DASH = "<number:text>-</number:text>";
	private static final String DOT = "<number:text>.</number:text>";
	private static final String HOURS = "<number:hours/>";
	private static final String MINUTES = "<number:minutes/>";
	private static final String SECONDS = "<number:seconds/>";

	private final TimeStyle.Format timeFormat;

	/**
	 * Create a new date style with the name name.<br>
	 * Version 0.5.1 Added.
	 *
	 * @param name
	 *            The name of the number style.
	 * @param odsFile
	 *            The odsFile to which this style belongs to.
	 */
	protected TimeStyle(final String name, final String languageCode,
			final String countryCode, final boolean volatileStyle,
			final Format timeFormat) {
		super(name, languageCode, countryCode, volatileStyle);
		this.timeFormat = timeFormat;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 */
	@Override
	public void appendXMLToCommonStyles(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<number:time-style");
		util.appendAttribute(appendable, "style:name", this.name);
		this.appendLVAttributes(util, appendable);
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
}
