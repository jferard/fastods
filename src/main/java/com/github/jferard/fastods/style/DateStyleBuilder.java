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

import com.github.jferard.fastods.style.DateStyle.Format;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file DateStyleBuilder.java is part of FastODS.
 */
public class DateStyleBuilder {
	private boolean bAutomaticOrder;

	/**
	 * The default date format DATEFORMAT_DDMMYY.
	 */
	private DateStyle.Format dateFormat;

	/**
	 * The name of this style.
	 */
	private final String sName;
	
	private String sCountry;
	private String sLanguage;

	/**
	 * Create a new date style with the name sName.<br>
	 * Version 0.5.1 Added.
	 *
	 * @param sName
	 *            The name of the number style.
	 * @param locale 
	 */
	protected DateStyleBuilder(final String sName, Locale locale) {
		if (sName == null)
			throw new IllegalArgumentException();
		
		this.sCountry = locale.getCountry();
		this.sLanguage = locale.getLanguage();
		this.sName = sName;
		this.bAutomaticOrder = false;
	}
	
	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 *
	 * @param country
	 *            The two letter country code, e.g. 'US'
	 */
	public DateStyleBuilder country(final String country) {
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
	public DateStyleBuilder language(final String language) {
		this.sLanguage = language.toLowerCase();
		return this;
	}

	/**
	 * The automatic-order attribute can be used to automatically order data to
	 * match the default order<br>
	 * for the language and country of the date style.
	 *
	 * @param bAutomatic
	 * @return this for fluent style
	 */
	public DateStyleBuilder automaticOrder(final boolean bAutomatic) {
		this.bAutomaticOrder = bAutomatic;
		return this;
	}

	/**
	 * @return the DateStyle
	 */
	public DateStyle build() {
		return new DateStyle(this.sName, this.dateFormat, this.sCountry,
				this.sLanguage, this.bAutomaticOrder);
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
	public DateStyleBuilder dateFormat(final DateStyle.Format format) {
		this.dateFormat = format;
		return this;
	}
}
