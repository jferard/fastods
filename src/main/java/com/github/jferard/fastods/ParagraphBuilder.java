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

import java.util.ArrayList;
import java.util.List;

import com.github.jferard.fastods.style.TextStyle;

public class ParagraphBuilder {

	private final List<Span> spans;
	private TextStyle style;

	ParagraphBuilder() {
		this.spans = new ArrayList<Span>();
	}

	public Paragraph build() {
		return new Paragraph(this.spans, this.style);
	}

	public ParagraphBuilder span(final String text) {
		final Span span = new Span(text);
		this.spans.add(span);
		return this;
	}

	public ParagraphBuilder style(final TextStyle ts) {
		this.style = ts;
		return this;
	}

	public ParagraphBuilder styledSpan(final String text, final TextStyle ts) {
		final Span span = new Span(text, ts);
		this.spans.add(span);
		return this;
	}
}
