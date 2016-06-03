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
package com.github.jferard.fastods.util;

import java.util.HashMap;
import java.util.Map;

import com.google.common.escape.Escaper;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file Util.java is part of FastODS.
 */
@SuppressWarnings("PMD.UnusedLocalVariable")
public class GuavaBasedXMLEscaper {
	private final Map<String, String> attrMap;

	private final Escaper xmlAttributeEscaper;
	private final Escaper xmlContentEscaper;

	public GuavaBasedXMLEscaper(final Escaper contentEscaper,
			final Escaper attributeEscaper) {
		this.xmlContentEscaper = contentEscaper;
		this.xmlAttributeEscaper = attributeEscaper;
		this.attrMap = new HashMap<String, String>();
	}

	public String escapeXMLAttribute(final String string) {
		String escapedString = this.attrMap.get(string);
		if (escapedString == null) {
			escapedString = this.xmlAttributeEscaper.escape(string);
			this.attrMap.put(string, escapedString);
		}
		return escapedString;
	}

	public String escapeXMLContent(final String string) {
		return this.xmlContentEscaper.escape(string);
	}
}
