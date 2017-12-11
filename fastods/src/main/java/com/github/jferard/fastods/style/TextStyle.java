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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * OpenDocument 16.27.28
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TextStyle implements ObjectStyle {
	/**
	 * The default text style
	 */
	public static final TextStyle DEFAULT_TEXT_STYLE = TextProperties.builder()
			.buildStyle("Default");

	private String key;
	private final String name;
	private final boolean hidden;
	private final TextProperties textProperties;

	/**
	 * Create a new text style
	 * @param name the name
	 * @param hidden true if the style is automatic
	 * @param textProperties the text properties
	 */
	TextStyle(final String name, final boolean hidden, final TextProperties textProperties) {
		this.name = name;
		this.hidden = hidden;
		this.textProperties = textProperties;
	}

	@Override
	public void appendXMLRepresentation(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<style:style ");
		util.appendEAttribute(appendable, "style:name", this.name);
		util.appendAttribute(appendable, "style:family", "text");
		appendable.append(">");
		this.getTextProperties().appendXMLContent(util, appendable);
		appendable.append("</style:style>");
	}

	@Override
	public ObjectStyleFamily getFamily() {
		return ObjectStyleFamily.TEXT;
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

	/**
	 * @return the text properties of this style
	 */
	public TextProperties getTextProperties() {
		return this.textProperties;
	}

	/**
	 * @return true if this style has a name and a least one text property is set
	 */
	public boolean isNotEmpty() {
		return this.name != null && this.name.length() > 0
				&& this.getTextProperties().isNotEmpty();
	}
	
	@Override
	public String getKey() {
		if (this.key == null)
			this.key = this.getFamily()+"@"+this.getName();
		return this.key;
	}

	@Override
	public boolean isHidden() {
		return this.hidden;
	}

	@Override
	public void addToElements(final OdsElements odsElements) {
		throw new UnsupportedOperationException();
	}
}
