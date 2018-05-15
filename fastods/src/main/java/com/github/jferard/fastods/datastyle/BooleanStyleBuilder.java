/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
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
public class BooleanStyleBuilder implements DataStyleBuilder<BooleanStyle, BooleanStyleBuilder> {
	private final CoreDataStyleBuilder dataStyleBuilder;

	/**
	 * The builder
	 *
	 * @param name   The name of this style
	 * @param locale The default locale.
	 */
    public BooleanStyleBuilder(final String name, final Locale locale) {
		this.dataStyleBuilder = new CoreDataStyleBuilder(name, locale);
	}

	@Override
	public BooleanStyle build() {
		return new BooleanStyle(this.dataStyleBuilder.build());
	}

	@Override
	public BooleanStyleBuilder country(final String countryCode) {
		this.dataStyleBuilder.country(countryCode);
		return this;
	}

	@Override
	public BooleanStyleBuilder language(final String languageCode) {
		this.dataStyleBuilder.language(languageCode);
		return this;
	}

	@Override
	public BooleanStyleBuilder locale(final Locale locale) {
		this.dataStyleBuilder.locale(locale);
		return this;
	}

	@Override
	public BooleanStyleBuilder volatileStyle(final boolean volatileStyle) {
		this.dataStyleBuilder.volatileStyle(volatileStyle);
		return this;
	}

	@Override
	public BooleanStyleBuilder visible() {
    	this.dataStyleBuilder.visible();
		return this;
	}
}

