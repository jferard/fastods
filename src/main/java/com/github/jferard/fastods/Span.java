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
package com.github.jferard.fastods;

import java.io.IOException;

import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * WHERE ? styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:footer/text:p/text:span
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:header/text:p/text:span
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class Span {
	private final String text;
	private final /*@Nullable*/ TextStyle ts;

	public Span(final String text) {
		this(text, null);
	}

	public Span(final String s, final TextStyle t) {
		this.ts = t;
		this.text = s;
	}

	/**
	 * @param util the xml util
	 * @param appendable where to add str
	 *
	 * @throws IOException
	 */
	public void appendXMLOptionalSpanToParagraph(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.ts == null) {
			appendable.append(this.text);
		} else {
			appendable.append("<text:span");
			util.appendEAttribute(appendable, "text:style-name",
					this.ts.getName());
			appendable.append(">").append(this.text).append("</text:span>");
		}
	}

	// DO NOT USE...
	// WARNING: 19.874.29: style-name refers to a style that has the family
	// "paragraph" !
	public void appendXMLTextPToParagraph(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<text:p");
		if (this.ts != null)
			util.appendEAttribute(appendable, "text:style-name",
					this.ts.getName());
		appendable.append(">").append(this.text).append("</text:p>");
	}

	public String getText() {
		return this.text;
	}

	public TextStyle getTextStyle() {
		return this.ts;
	}
}
