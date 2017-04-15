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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TextBuilder {
	private ParagraphBuilder curParagraphBuilder;
	private final List<Paragraph> paragraphs;
	private final Set<TextStyle> textStyles;

	TextBuilder() {
		this.textStyles = new HashSet<TextStyle>();
		this.paragraphs = new ArrayList<Paragraph>();
		this.curParagraphBuilder = null;
	}

	public Text build() {
		if (this.curParagraphBuilder != null) {
			this.paragraphs.add(this.curParagraphBuilder.build());
		}
		return new Text(this.paragraphs, this.textStyles);
	}

	public TextBuilder par() {
		if (this.curParagraphBuilder != null) {
			this.paragraphs.add(this.curParagraphBuilder.build());
		}
		this.curParagraphBuilder = new ParagraphBuilder();
		return this;
	}

	public TextBuilder parContent(final String text) {
		return this.par().span(text);
	}

	public TextBuilder parStyledContent(final String text, final TextStyle ts) {
		return this.par().styledSpan(text, ts);
	}

	public TextBuilder span(final String text) {
		this.curParagraphBuilder.span(text);
		return this;
	}

	public TextBuilder link(final String text, final Table table) {
		this.curParagraphBuilder.link(text, table);
		return this;
	}

	public TextBuilder styledLink(final String text, final Table table, final TextStyle ts) {
		this.curParagraphBuilder.styledLink(text, table, ts);
		return this;
	}

	public TextBuilder link(final String text, final File file) {
		this.curParagraphBuilder.link(text, file);
		return this;
	}

	public TextBuilder styledLink(final String text, final File file, final TextStyle ts) {
		this.curParagraphBuilder.styledLink(text, file, ts);
		return this;
	}

	public TextBuilder link(final String text, final URL url) {
		this.curParagraphBuilder.link(text, url);
		return this;
	}

	public TextBuilder styledLink(final String text, final URL url, final TextStyle ts) {
		this.curParagraphBuilder.styledLink(text, url, ts);
		return this;
	}

	/**
	 * Adds a TextStyle and text to the footer/header region specified by
	 * region.<br>
	 * The paragraph to be used is paragraph.<br>
	 * The text will be shown in the order it was added with this function.
	 * 
	 * @param text
	 *            The string with the text
	 * @param ts
	 *            The text style to be used
	 * @return this for fluent style
	 */
	public TextBuilder styledSpan(final String text, final TextStyle ts) {
		this.textStyles.add(ts);
		this.curParagraphBuilder.styledSpan(text, ts);
		return this;
	}

	public TextBuilder link(final String text, final String ref) {
		this.curParagraphBuilder.link(text, ref);
		return this;
	}
}
