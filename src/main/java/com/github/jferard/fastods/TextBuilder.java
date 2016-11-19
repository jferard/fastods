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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.jferard.fastods.style.FHTextStyle;

public class TextBuilder {
	private Set<FHTextStyle> textStyles;
	private List<Paragraph> paragraphs;

	public TextBuilder() {
		this.paragraphs = new ArrayList<Paragraph>();
	}
	
	public TextBuilder pageCount(final FHTextStyle ts) {
		this.textStyles.add(ts);
		this.styledSpan(ts, "<text:page-count>99</text:page-count>");
		return this;
	}

	public TextBuilder pageNumber(final FHTextStyle ts) {
		this.textStyles.add(ts);
		this.styledSpan(ts, "<text:page-number>1</text:page-number>");
		return this;
	}

	/**
	 * Adds a TextStyle and text to the footer/header region specified by
	 * region.<br>
	 * The paragraph to be used is paragraph.<br>
	 * The text will be shown in the order it was added with this function.
	 *
	 * @param ts
	 *            The text style to be used
	 * @param text
	 *            The string with the text
	 * @param region
	 *            One of : FooterHeader.FLG_REGION_LEFT,
	 *            FooterHeader.FLG_REGION_CENTER or
	 *            FooterHeader.FLG_REGION_RIGHT
	 * @param paragraph
	 *            The paragraph number to be used
	 * @return
	 */
	public TextBuilder styledSpan(final FHTextStyle ts, final String text) {
		this.textStyles.add(ts);
		final Paragraph paragraph = new Paragraph();
		final Span span = new Span(text, ts);
		paragraph.add(span);
		this.paragraphs.add(paragraph);
		return this;
	}
}
