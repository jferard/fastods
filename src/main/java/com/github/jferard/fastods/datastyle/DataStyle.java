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

import java.io.IOException;

import com.github.jferard.fastods.entry.OdsEntries;
import com.github.jferard.fastods.util.NamedObject;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard
 */
public abstract class DataStyle implements NamedObject {
	protected final String countryCode;
	protected final String languageCode;
	protected final String name;
	/**
	 * 19.517 : "The style:volatile attribute specifies whether unused style in
	 * a document are retained or discarded by consumers."
	 */
	protected final boolean volatileStyle;

	protected DataStyle(final String name, final String languageCode,
			final String countryCode, final boolean volatileStyle) {
		this.countryCode = countryCode;
		this.languageCode = languageCode;
		this.name = name;
		this.volatileStyle = volatileStyle;
	}

	public void addToEntries(final OdsEntries odsEntries) {
		odsEntries.addDataStyle(this);
	}

	/**
	 * Adds this style to an OdsDocument.
	 *
	 * @param util
	 *            XML util for escaping characters and write data.
	 * @param appendable
	 *            the destination
	 * @throws IOException
	 *             if can't write data to file
	 */
	public abstract void appendXML(final XMLUtil util,
			final Appendable appendable) throws IOException;

	/*
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.name;
	}

	protected void appendLocaleAttributes(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.languageCode != null)
			util.appendAttribute(appendable, "number:language",
					this.languageCode);
		if (this.countryCode != null)
			util.appendAttribute(appendable, "number:country",
					this.countryCode);
	}

	protected void appendLVAttributes(final XMLUtil util,
			final Appendable appendable) throws IOException {
		this.appendLocaleAttributes(util, appendable);
		this.appendVolatileAttribute(util, appendable);
	}

	protected void appendVolatileAttribute(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.volatileStyle)
			util.appendEAttribute(appendable, "style:volatile",
					this.volatileStyle);
	}

	/**
	 * @return The two letter country code, e.g. 'US'
	 */
	String getCountryCode() {
		return this.countryCode;
	}

	/**
	 * @return The two letter language code, e.g. 'en'.
	 * @see http://www.ietf.org/rfc/rfc3066.txt
	 */
	String getLanguageCode() {
		return this.languageCode;
	}
}
