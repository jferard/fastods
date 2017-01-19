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

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * OpenDocument 16.27.23 number:boolean-style
 *
 * @author Julien Férard
 */
public class BooleanStyle extends DataStyle {
	/**
	 * @param name
	 *            name of the style
	 * @param languageCode
	 *            language
	 * @param countryCode
	 *            country
	 * @param volatileStyle "false: consumers should discard the unused styles, true: consumers should keep unused
	 *                         styles." (19.517 style:volatile)
	 */
	protected BooleanStyle(final String name, final String languageCode,
			final String countryCode, final boolean volatileStyle) {
		super(name, languageCode, countryCode, volatileStyle);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<number:boolean-style");
		util.appendAttribute(appendable, "style:name", this.name);
		this.appendLVAttributes(util, appendable);
		appendable.append("/>");
	}
}
