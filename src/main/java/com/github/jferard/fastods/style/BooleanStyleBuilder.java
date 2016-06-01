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
 *         This file CurrencyStyleBuilder.java is part of FastODS.
 *
 */
public class BooleanStyleBuilder {
	private boolean bVolatile;
	private String sCountry;
	private String sLanguage;
	private final String sName;

	/**
	 * The builder
	 * @param name
	 *            - The name of this style
	 * @param locale 
	 */
	protected BooleanStyleBuilder(final String name, Locale locale) {
		this.sName = name;
		this.sCountry = locale.getCountry();
		this.sLanguage = locale.getLanguage();
		this.bVolatile = true;
	}

	public BooleanStyle build() {
		return new BooleanStyle(this.sName, this.sLanguage, this.sCountry,
				this.bVolatile);
	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 *
	 * @param country
	 *            The two letter country code, e.g. 'US'
	 */
	public BooleanStyleBuilder country(final String country) {
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
	public BooleanStyleBuilder language(final String language) {
		this.sLanguage = language.toLowerCase();
		return this;
	}

	/**
	 * Set how many leading zeros are present.
	 *
	 * @param minIntegerDigits
	 *            The number of leading zeros
	 */
	public BooleanStyleBuilder volatil(final boolean bVolatile) {
		this.bVolatile = bVolatile;
		return this;
	}
}