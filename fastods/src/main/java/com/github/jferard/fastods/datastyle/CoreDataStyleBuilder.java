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

import java.util.Locale;


/**
 * A CoreDataStyle builder
 * @author Julien Férard
 */
final class CoreDataStyleBuilder {
	/**
	 * 19.342 number:country : "The number:country attribute specifies a country code for a data style"
	 */
	private String countryCode;
	/**
	 * 19.349 number:language : "The number:language attribute specifies a language code"
	 */
	private String languageCode;
	/**
	 * the name of a data style (19.498.2)
	 */
	private final String name;
	/**
	 * 19.517 : "The style:volatile attribute specifies whether unused style in
	 * a document are retained or discarded by consumers." and "false: consumers should discard the unused styles,
	 * true: consumers should keep unused styles."
	 */
	private boolean volatileStyle;
	private boolean hidden;

	/**
	 * The builder
	 *
	 * @param name   The name of this style
	 * @param locale The locale used
	 */
	public CoreDataStyleBuilder(final String name, final Locale locale) {
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
	public CoreDataStyle build() {
		return new CoreDataStyle(this.name, this.hidden, this.languageCode, this.countryCode, this.volatileStyle);

	}

	/**
	 * @return the data style built
	 */
	public CoreDataStyle buildHidden() {
		this.hidden = true;
		return this.build();

	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 *
	 * @param countryCode The two letter country code, e.g. 'US'
	 * @return this for fluent style
	 */
	public CoreDataStyleBuilder country(final String countryCode) {
		this.countryCode = countryCode.toUpperCase();
		return this;
	}

	/**
	 * Set the country and language if you need to distinguish between different
	 * countries. E.g. set it to country='US' and language='en'
	 *
	 * @param languageCode The two letter language code, e.g. 'en'
	 * @return this for fluent style
	 */
	public CoreDataStyleBuilder language(final String languageCode) {
		this.languageCode = languageCode.toLowerCase();
		return this;
	}

	/**
	 * Sets the locale (ie languge + country)
	 *
	 * @param locale the locale to use for langaauge and country
	 * @return this for fluent style
	 */
	public CoreDataStyleBuilder locale(final Locale locale) {
		this.countryCode = locale.getCountry();
		this.languageCode = locale.getLanguage();
		return this;
	}

	/**
	 * @param volatileStyle false: consumers should discard the unused styles,
	 *                      true: consumers should keep unused styles (19.517)
	 * @return this for fluent style
	 */
	public CoreDataStyleBuilder volatileStyle(final boolean volatileStyle) {
		this.volatileStyle = volatileStyle;
		return this;
	}
}
