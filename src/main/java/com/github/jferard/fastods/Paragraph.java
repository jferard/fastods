/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods;

import java.io.IOException;
import java.util.List;

import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard
 */
public class Paragraph {
	public static ParagraphBuilder builder() {
		return new ParagraphBuilder();
	}

	private final List<Span> spans;

	private final TextStyle style;

	Paragraph(final List<Span> spans, final TextStyle style) {
		this.spans = spans;
		this.style = style;
	}

	public void appendXMLContent(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.spans.isEmpty()) {
			appendable.append("<text:p/>");
		} else {
			appendable.append("<text:p");
			if (this.style != null)
				util.appendAttribute(appendable, "text:style-name",
						this.style.getName());
			appendable.append('>');
			for (final Span span : this.spans)
				span.appendXMLOptionalSpanToParagraph(util, appendable);
			appendable.append("</text:p>");
		}
	}

	public List<Span> getSpans() {
		return this.spans;
	}
}
