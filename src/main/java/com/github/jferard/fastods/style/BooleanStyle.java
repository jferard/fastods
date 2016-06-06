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

import java.io.IOException;

import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Martin Schulz Copyright
 *         2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file BooleanStyle.java is part of FastODS.
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:automatic-styles/number:
 *         currency-style
 *         styles.xml/office:document-styles/office:styles/number:currency-style
 *
 *         16.27.23 <number:boolean-style>
 */
public class BooleanStyle implements DataStyle {
	private final boolean bVolatile;
	private final String sCountry;
	private final String sLanguage;
	private final String sName;

	protected BooleanStyle(final String sName, final String sLanguage,
			final String sCountry, final boolean bVolatile) {
		this.sName = sName;
		this.sLanguage = sLanguage;
		this.sCountry = sCountry;
		this.bVolatile = bVolatile;
	}

	@Override
	public void addToFile(final OdsFile odsFile) {
		odsFile.addDataStyle(this);
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 *
	 * @return The XML string for this object.
	 */
	@Override
	public void appendXMLToStylesEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<number:boolean-style");
		util.appendAttribute(appendable, "style:name", this.sName);
		if (this.bVolatile)
			util.appendEAttribute(appendable, "style:volatile", true);
		util.appendEAttribute(appendable, "number:country", this.sCountry);
		util.appendEAttribute(appendable, "number:language", this.sLanguage);
		appendable.append("/>");
	}

	/**
	 * @return The two letter country code, e.g. 'US' ISO 3166-2
	 */
	public String getCountry() {
		return this.sCountry;
	}

	/**
	 * @return The two letter language code, e.g. 'en'.
	 *         http://www.ietf.org/rfc/rfc3066.txt
	 */
	public String getLanguage() {
		return this.sLanguage;
	}

	/**
	 * Get the name of this boolean style.
	 *
	 * @return The currency style name
	 */
	@Override
	public String getName() {
		return this.sName;
	}
}