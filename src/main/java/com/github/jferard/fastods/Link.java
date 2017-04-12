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

package com.github.jferard.fastods;

import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 6.1.8 text:a
 * @author Julien Férard
 */
public class Link implements ParagraphElement {
	private final String text;
	private final String content;
	private final TextStyle ts;

	public Link(final String text, final Table table, final TextStyle ts) {
		this.text = text;
		this.content = '#'+table.getName();
		this.ts = ts;
	}

	public Link(final String text, final String ref, final TextStyle ts) {
		this.text = text;
		this.content = '#'+ref;
		this.ts = ts;
	}

	public Link(final String text, final Table table) {
		this(text, table, null);
	}

	public Link(final String text, final String ref) {
		this(text, ref, null);
	}

	public Link(final String text, final File file, final TextStyle ts) {
		this.text = text;
		this.content = file.toURI().toString();
		this.ts = ts;
	}

	public Link(final String text, final File file) {
		this(text, file, null);
	}

	public Link(final String text, final URL url, final TextStyle ts) {
		this.text = text;
		this.content = url.toString();
		this.ts = ts;
	}

	public Link(final String text, final URL url) {
		this(text, url, null);
	}

	@Override
	public void appendXMLToParagraph(final XMLUtil util, final Appendable appendable) throws IOException {
		appendable.append("<text:a");
		if (this.ts != null) {
			util.appendEAttribute(appendable, "text:style-name",
					this.ts.getName());
		}
		util.appendAttribute(appendable, "xlink:href", content);
		util.appendAttribute(appendable, "xlink:type", "simple");
		appendable.append(">").append(this.text).append("</text:a>");
	}

	@Override
	public TextStyle getTextStyle() {
		return ts;
	}

	@Override
	public String getText() {
		return text;
	}
}
