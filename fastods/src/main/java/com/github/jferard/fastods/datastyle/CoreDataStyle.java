/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.util.Hidable;
import com.github.jferard.fastods.util.NamedObject;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * @author Julien Férard
 */
final class CoreDataStyle implements NamedObject, Hidable {
	private final boolean hidden;
	/**
	 * 19.342 number:country : "The number:country attribute specifies a country code for a data style"
	 */
	private final String countryCode;
	/**
	 * 19.349 number:language : "The number:language attribute specifies a language code"
	 */
	private final String languageCode;
	/**
	 * the name of a data style (19.498.2)
	 */
	private final String name;

	private final boolean volatileStyle;

	/**
	 * Create a new core data style
	 * @param name the name of the style
	 * @param hidden if true, an automatic style
	 * @param languageCode the language code (e.g. "en")
	 * @param countryCode the country code (e.g. "US")
	 * @param volatileStyle true if the style should be kept
	 */
	CoreDataStyle(final String name, final boolean hidden, final String languageCode,
						final String countryCode, final boolean volatileStyle) {
		this.name = name;
		this.hidden = hidden;
		this.countryCode = countryCode;
		this.languageCode = languageCode;
		this.volatileStyle = volatileStyle;
	}

	@Override
	public String getName() {
		return this.name;
	}

    /**
     * Append locale and volatile attributes
     * @param util an util
     * @param appendable the destination
     * @throws IOException if an error occurs
     */
    public void appendLVAttributes(final XMLUtil util,
									  final Appendable appendable) throws IOException {
		this.appendLocaleAttributes(util, appendable);
		this.appendVolatileAttribute(util, appendable);
	}

    private void appendLocaleAttributes(final XMLUtil util,
                                        final Appendable appendable) throws IOException {
        if (this.languageCode != null)
            util.appendEAttribute(appendable, "number:language",
                    this.languageCode);
        if (this.countryCode != null)
            util.appendEAttribute(appendable, "number:country",
                    this.countryCode);
    }

    private void appendVolatileAttribute(final XMLUtil util,
										   final Appendable appendable) throws IOException {
		if (this.volatileStyle)
			util.appendAttribute(appendable, "style:volatile",	this.volatileStyle);
	}

	@Override
	public boolean isHidden() {
		return this.hidden;
	}
}
