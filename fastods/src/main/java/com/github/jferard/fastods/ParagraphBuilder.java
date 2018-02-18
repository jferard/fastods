/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

/**
 * A paragraph builder builds a small text block.
 *
 * @author Julien Férard
 */
public class ParagraphBuilder {
	private final List<ParagraphElement> paragraphElements;
	private TextStyle style;

	/**
	 * Create an empty builder
	 */
	ParagraphBuilder() {
		this.paragraphElements = new ArrayList<ParagraphElement>();
	}

	/**
	 * @return the paragraph
	 */
	public Paragraph build() {
		return new Paragraph(this.paragraphElements, this.style);
	}

	/**
	 * Create a span in the current paragraph.
	 * @param text the text
	 * @return this for fluent style
	 */
	public ParagraphBuilder span(final String text) {
		final ParagraphElement paragraphElement = new Span(text);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	/**
	 * Create a link in the current paragraph.
	 * @param text the text
	 * @param ref the destination
	 * @return this for fluent style
	 */
	public ParagraphBuilder link(final String text, final String ref) {
		final ParagraphElement paragraphElement = Link.create(text, ref);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	/**
	 * Create a styled link in the current paragraph.
	 * @param text the text
	 * @param ts the style
	 * @param ref the destination
	 * @return this for fluent style
	 */
	public ParagraphBuilder styledLink(final String text, final TextStyle ts, final String ref) {
		final ParagraphElement paragraphElement = Link.create(text, ts, ref);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	/**
	 * Create a link in the current paragraph.
	 * @param text the text
	 * @param table the destination
	 * @return this for fluent style
	 */
	public ParagraphBuilder link(final String text, final Table table) {
		final ParagraphElement paragraphElement = Link.create(text, table);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	/**
	 * Create a styled link in the current paragraph.
	 * @param text the text
	 * @param ts the style
	 * @param table the destination
	 * @return this for fluent style
	 */
	public ParagraphBuilder styledLink(final String text, final TextStyle ts, final Table table) {
		final ParagraphElement paragraphElement = Link.create(text, ts, table);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	/**
	 * Create a link in the current paragraph.
	 * @param text the text
	 * @param file the destination
	 * @return this for fluent style
	 */
	public ParagraphBuilder link(final String text, final File file) {
		final ParagraphElement paragraphElement = Link.create(text, file);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	/**
	 * Create a styled link in the current paragraph.
	 * @param text the text
	 * @param ts the style
	 * @param file the destination
	 * @return this for fluent style
	 */
	public ParagraphBuilder styledLink(final String text, final TextStyle ts, final File file) {
		final ParagraphElement paragraphElement = Link.create(text, ts, file);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	/**
	 * Create a link in the current paragraph.
	 * @param text the text
	 * @param url the destination
	 * @return this for fluent style
	 */
	public ParagraphBuilder link(final String text, final URL url) {
		final ParagraphElement paragraphElement = Link.create(text, url);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	/**
	 * Create a styled link in the current paragraph.
	 * @param text the text
	 * @param ts the style
	 * @param url the destination
	 * @return this for fluent style
	 */
	public ParagraphBuilder styledLink(final String text, final TextStyle ts, final URL url) {
		final ParagraphElement paragraphElement = Link.create(text, ts, url);
		this.paragraphElements.add(paragraphElement);
		return this;
	}

	/**
	 * Set the paragraph style
	 * @param ts the style
	 * @return this for fluent style
	 */
	public ParagraphBuilder style(final TextStyle ts) {
		this.style = ts;
		return this;
	}

	/**
	 * Create a styled span with a text content
	 * @param text the text
	 * @param ts the style
	 * @return this for fluent style
	 */
	public ParagraphBuilder styledSpan(final String text, final TextStyle ts) {
		final ParagraphElement paragraphElement = new Span(text, ts);
		this.paragraphElements.add(paragraphElement);
		return this;
	}
}
