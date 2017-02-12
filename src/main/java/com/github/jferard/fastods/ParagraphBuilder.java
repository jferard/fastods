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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ParagraphBuilder {

	private final List<ParagraphElement> paragraphElements;
	private TextStyle style;

	ParagraphBuilder() {
		this.paragraphElements = new ArrayList<ParagraphElement>();
	}

	public Paragraph build() {
		return new Paragraph(this.paragraphElements, this.style);
	}

	public ParagraphBuilder span(final String text) {
		final ParagraphElement paragraphElement = new Span(text);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	public ParagraphBuilder link(final String text, final Table table) {
		final ParagraphElement paragraphElement = new Link(text, table);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	public ParagraphBuilder styledLink(final String text, final Table table, final TextStyle ts) {
		final ParagraphElement paragraphElement = new Link(text, table, ts);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	public ParagraphBuilder link(final String text, final File file) {
		final ParagraphElement paragraphElement = new Link(text, file);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	public ParagraphBuilder styledLink(final String text, final File file, final TextStyle ts) {
		final ParagraphElement paragraphElement = new Link(text, file, ts);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	public ParagraphBuilder link(final String text, final URL url) {
		final ParagraphElement paragraphElement = new Link(text, url);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	public ParagraphBuilder styledLink(final String text, final URL url, final TextStyle ts) {
		final ParagraphElement paragraphElement = new Link(text, url, ts);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	public ParagraphBuilder style(final TextStyle ts) {
		this.style = ts;
		return this;
	}

	public ParagraphBuilder styledSpan(final String text, final TextStyle ts) {
		final ParagraphElement paragraphElement = new Span(text, ts);
		this.paragraphElements.add(paragraphElement);
		return this;
	}
}
