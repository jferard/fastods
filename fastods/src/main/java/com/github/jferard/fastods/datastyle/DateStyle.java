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
 * date-style styles.xml/office:document-styles/office:styles/number:date-style
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class DateStyle implements DataStyle {
	/**
	 * The format of the date
	 */
	public enum Format {
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
		YYYYMMDD
	}

	/**
	 * The default date format Format.DDMMYY.
	 */
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

	/**
	 * 19.340 number:automatic-order: "specifies whether data is ordered to match the default
	 order for the language and country of a data style"
	 */
	private final boolean automaticOrder;

	private final CoreDataStyle dataStyle;
	private final Format dateFormat;

	/**
	 * Create a new date style with the name name.
	 * @param dataStyle the core data style
	 * @param dateFormat the format for the date
	 * @param automaticOrder true if the order comes from the current locale
	 */
	DateStyle(final CoreDataStyle dataStyle,
			final Format dateFormat, final boolean automaticOrder) {
		this.dataStyle = dataStyle;
		this.dateFormat = dateFormat;
		this.automaticOrder = automaticOrder;
	}

	@Override
	public void appendXMLRepresentation(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<number:date-style");
		util.appendAttribute(appendable, "style:name", this.dataStyle.getName());
		this.dataStyle.appendLVAttributes(util, appendable);
		util.appendEAttribute(appendable, "number:automatic-order",
				this.automaticOrder);
		if (this.dateFormat == null) {
			util.appendEAttribute(appendable, "number:format-source",
					"language");
			appendable.append("/>");
		} else {
			util.appendEAttribute(appendable, "number:format-source", "fixed");
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
	}

	@Override
	public boolean isHidden() {
		return this.dataStyle.isHidden();
	}

	/**
	 * @return The current value of the automatic order flag
	 */
	public boolean isAutomaticOrder() {
		return this.automaticOrder;
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

	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addDataStyle(this);
	}
}
