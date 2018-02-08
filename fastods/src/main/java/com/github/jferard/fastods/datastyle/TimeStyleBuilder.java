/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

import java.util.Locale;

/**
 * @author Julien Férard
 */
class TimeStyleBuilder implements DataStyleBuilder<TimeStyle, TimeStyleBuilder> {
	private final CoreDataStyleBuilder dataStyleBuilder;
	/**
	 * The date format.
	 */
	private TimeStyle.Format timeFormat;

	/**
	 * Create a new date style with the name name.
	 *
	 * @param name The name of the number style.
	 * @param locale the locale used
	 */
	TimeStyleBuilder(final String name, final Locale locale) {
		this.dataStyleBuilder = new CoreDataStyleBuilder(name, locale);
	}

	@Override
	public TimeStyle build() {
		return new TimeStyle(this.dataStyleBuilder.build(), this.timeFormat);
	}

	@Override
	public TimeStyle buildHidden() {
		return new TimeStyle(this.dataStyleBuilder.buildHidden(), this.timeFormat);
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
	public TimeStyleBuilder timeFormat(final TimeStyle.Format format) {
		this.timeFormat = format;
		return this;
	}

	@Override
	public TimeStyleBuilder country(final String countryCode) {
		this.dataStyleBuilder.country(countryCode);
		return this;
	}

	@Override
	public TimeStyleBuilder language(final String languageCode) {
		this.dataStyleBuilder.language(languageCode);
		return this;
	}

	@Override
	public TimeStyleBuilder locale(final Locale locale) {
		this.dataStyleBuilder.locale(locale);
		return this;
	}

	@Override
	public TimeStyleBuilder volatileStyle(final boolean volatileStyle) {
		this.dataStyleBuilder.volatileStyle(volatileStyle);
		return this;
	}
}
