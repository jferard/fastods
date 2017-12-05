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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * content.xml/office:document-content/office:automatic-styles/number:
 * time-style styles.xml/office:document-styles/office:styles/number:time-style
 *
 * @author Julien Férard
 */
public class TimeStyle implements DataStyle {
	private static final String COLON = "<number:text>:</number:text>";
	private static final String DASH = "<number:text>-</number:text>";
	private static final String DOT = "<number:text>.</number:text>";
	private static final String HOURS = "<number:hours/>";
	private static final String MINUTES = "<number:minutes/>";
	private static final String SECONDS = "<number:seconds/>";
	private final CoreDataStyle dataStyle;
	private final TimeStyle.Format timeFormat;

	/**
	 * Create a new date style
	 */
	TimeStyle(final CoreDataStyle dataStyle,
						final Format timeFormat) {
		this.dataStyle = dataStyle;
		this.timeFormat = timeFormat;
	}

	@Override
	public void appendXMLRepresentation(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<number:time-style");
		util.appendAttribute(appendable, "style:name", this.dataStyle.getName());
		this.dataStyle.appendLVAttributes(util, appendable);
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

	@Override
	public boolean isHidden() {
		return this.dataStyle.isHidden();
	}

	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addDataStyle(this);
	}

	public enum Format {
		/**
		 * Set the time format like '01:02:03'.
		 */
		HHMMSS
	}

	@Override
	public boolean isVolatileStyle() {
		return this.dataStyle.isVolatileStyle();
	}

	@Override
	public String getName() {
		return this.dataStyle.getName();
	}

	@Override
	public String getCountryCode() {
		return this.dataStyle.getCountryCode();
	}

	@Override
	public String getLanguageCode() {
		return this.dataStyle.getLanguageCode();
	}
}
