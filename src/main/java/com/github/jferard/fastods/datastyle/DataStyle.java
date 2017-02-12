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

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.style.AddableToOdsElements;
import com.github.jferard.fastods.util.NamedObject;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * @author Julien Férard
 */
public abstract class DataStyle implements NamedObject, AddableToOdsElements {
	/**
	 * 19.342 number:country : "The number:country attribute specifies a country code for a data style"
	 */
	protected final String countryCode;
	/**
	 * 19.349 number:language : "The number:language attribute specifies a language code"
	 */
	protected final String languageCode;
	/**
	 * the name of a data style (19.498.2)
	 */
	protected final String name;
	/**
	 * 19.517 : "The style:volatile attribute specifies whether unused style in
	 * a document are retained or discarded by consumers." and "false: consumers should discard the unused styles,
	 * true: consumers should keep unused styles."
	 */
	protected final boolean volatileStyle;

	protected DataStyle(final String name, final String languageCode,
						final String countryCode, final boolean volatileStyle) {
		this.countryCode = countryCode;
		this.languageCode = languageCode;
		this.name = name;
		this.volatileStyle = volatileStyle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addDataStyle(this);
	}

	/**
	 * Adds this style to an OdsDocument.
	 *
	 * @param util       XML util for escaping characters and write data.
	 * @param appendable the destination
	 * @throws IOException if can't write data to file
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
	 * See http://www.ietf.org/rfc/rfc3066.txt
	 *
	 * @return The two letter language code, e.g. 'en'.
	 */
	String getLanguageCode() {
		return this.languageCode;
	}
}
