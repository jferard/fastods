/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods.datastyle;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public class DateStyleBuilder
		extends DataStyleBuilder<DateStyle, DateStyleBuilder> {
	private boolean automaticOrder;

	/**
	 * The default date format DATEFORMAT_DDMMYY.
	 */
	private DateStyle.Format dateFormat;

	/**
	 * Create a new date style with the name name.<br>
	 * Version 0.5.1 Added.
	 *
	 * @param name   The name of the number style.
	 * @param locale The locale used
	 */
	protected DateStyleBuilder(final String name, final Locale locale) {
		super(name, locale);
		this.automaticOrder = false;
	}

	/**
	 * The automatic-order attribute can be used to automatically order data to
	 * match the default order<br>
	 * for the language and country of the date style.
	 *
	 * @param automatic specifies whether data is ordered to match the default
	 *                  order for the language and country of a data style (19.340 number:automatic-order).
	 * @return this for fluent style
	 */
	public DateStyleBuilder automaticOrder(final boolean automatic) {
		this.automaticOrder = automatic;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateStyle build() {
		return new DateStyle(this.name, this.countryCode, this.languageCode,
				this.volatileStyle, this.dateFormat, this.automaticOrder);
	}

	/**
	 * Set the date format.<br>
	 * Valid is one of the following:<br>
	 * DateStyle.DATEFORMAT_DDMMYYYY<br>
	 * DateStyle.DATEFORMAT_DDMMYY<br>
	 * DateStyle.DATEFORMAT_TMMMMYYYY<br>
	 * DateStyle.DATEFORMAT_MMMM<br>
	 * *
	 *
	 * @param format The date format to be used.
	 * @return this for fluent style
	 */
	public DateStyleBuilder dateFormat(final DateStyle.Format format) {
		this.dateFormat = format;
		return this;
	}
}
