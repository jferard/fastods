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
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * OpenDocument 16.27.28
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TextStyle implements StyleTag {
	public static final TextStyle DEFAULT_TEXT_STYLE = TextProperties.builder()
			.buildStyle("Default");

	private final String name;
	private final TextProperties textProperties;

	/**
	 * Create a new text style with the name name.
	 */
	TextStyle(final String name, final TextProperties textProperties) {
		this.name = name;
		this.textProperties = textProperties;
	}

	@Override
	public void appendXML(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<style:style ");
		util.appendAttribute(appendable, "style:name", this.name);
		util.appendEAttribute(appendable, "style:family", "text");
		appendable.append(">");
		this.getTextProperties().appendXMLContent(util, appendable);
		appendable.append("</style:style>");
	}

	@Override
	public String getFamily() {
		return "text";
	}

	/**
	 * Get the name of this text style.
	 *
	 * @return The text style name
	 */
	@Override
	public String getName() {
		return this.name;
	}

	public TextProperties getTextProperties() {
		return this.textProperties;
	}

	public boolean isNotEmpty() {
		return this.name != null && this.name.length() > 0
				&& this.getTextProperties().isNotEmpty();
	}
	
	private String key;
	@Override
	public String getKey() {
		if (this.key == null)
			this.key = this.getFamily()+"@"+this.getName();
		return this.key;
	}

	@Override
	public void addToElements(OdsElements odsElements) {
		throw new UnsupportedOperationException();
	}
}
