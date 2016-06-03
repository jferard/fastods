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

import java.util.Locale;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file DateStyleBuilder.java is part of FastODS.
 */
public class TimeStyleBuilder {
	private String sCountry;

	private String sLanguage;

	/**
	 * The name of this style.
	 */
	private final String sName;
	/**
	 * The default date format DATEFORMAT_DDMMYY.
	 */
	private TimeStyle.Format timeFormat;

	/**
	 * Create a new date style with the name sName.<br>
	 * Version 0.5.1 Added.
	 *
	 * @param sName
	 *            The name of the number style.
	 */
	protected TimeStyleBuilder(final String sName, final Locale locale) {
		if (sName == null)
			throw new IllegalArgumentException();
		this.sCountry = locale.getCountry();
		this.sLanguage = locale.getLanguage();
		this.sName = sName;
	}

	/**
	 * @return the DateStyle
	 */
	public TimeStyle build() {
		return new TimeStyle(this.sName, this.timeFormat, this.sCountry,
				this.sLanguage);
	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 *
	 * @param country
	 *            The two letter country code, e.g. 'US'
	 */
	public TimeStyleBuilder country(final String country) {
		this.sCountry = country.toUpperCase();
		return this;
	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 *
	 * @param language
	 *            The two letter language code, e.g. 'en'
	 */
	public TimeStyleBuilder language(final String language) {
		this.sLanguage = language.toLowerCase();
		return this;
	}

	public TimeStyleBuilder locale(final Locale locale) {
		this.sCountry = locale.getCountry();
		this.sLanguage = locale.getLanguage();
		return this;
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
	 * @param format
	 *            The date format to be used.
	 * @return this for fluent style
	 */
	public TimeStyleBuilder timeFormat(final TimeStyle.Format format) {
		this.timeFormat = format;
		return this;
	}
}
