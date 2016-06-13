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
package com.github.jferard.fastods.datastyle;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public abstract class DataStyleBuilder<S extends DataStyle, B extends DataStyleBuilder<S, B>> {
	protected String countryCode;
	protected String languageCode;
	protected final String name;
	/**
	 * 19.517 : "The style:volatile attribute specifies whether unused style in
	 * a document are retained or discarded by consumers."
	 */
	protected boolean volatileStyle;

	/**
	 * The builder
	 *
	 * @param name
	 *            The name of this style
	 * @param locale
	 *            the locale used
	 */
	protected DataStyleBuilder(final String name, final Locale locale) {
		if (name == null)
			throw new IllegalArgumentException();

		this.name = name;
		this.countryCode = locale.getCountry();
		this.languageCode = locale.getLanguage();
		this.volatileStyle = true;
	}

	/**
	 * @return the data style built
	 */
	public abstract S build();

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 *
	 * @param countryCode
	 *            The two letter country code, e.g. 'US'
	 */
	public B country(final String countryCode) {
		this.countryCode = countryCode.toUpperCase();
		return (B) this;
	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 *
	 * @param languageCode
	 *            The two letter language code, e.g. 'en'
	 */
	public B language(final String languageCode) {
		this.languageCode = languageCode.toLowerCase();
		return (B) this;
	}

	/**
	 * Sets the locale (ie languge + country)
	 *
	 * @param locale
	 *            the locale to use for langaauge and country
	 * @return this
	 */
	public B locale(final Locale locale) {
		this.countryCode = locale.getCountry();
		this.languageCode = locale.getLanguage();
		return (B) this;
	}

	/**
	 * Set how many leading zeros are present.
	 *
	 * @param minIntegerDigits
	 *            The number of leading zeros
	 */
	public B volatileStyle(final boolean volatileStyle) {
		this.volatileStyle = volatileStyle;
		return (B) this;
	}
}
