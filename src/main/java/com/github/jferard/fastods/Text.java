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
import java.util.List;
import java.util.Set;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.XMLUtil;

public class Text {
	public static final String TEXT_DATE = "<text:date/>";
	public static final String TEXT_FILE_NAME = "<text:file-name/>";
	public static final String TEXT_PAGE_COUNT = "<text:page-count>99</text:page-count>";
	public static final String TEXT_PAGE_NUMBER = "<text:page-number>1</text:page-number>";
	public static final String TEXT_SHEET_NAME = "<text:sheet-name/>";
	public static final String TEXT_TIME = "<text:time/>";

	public static TextBuilder builder() {
		return new TextBuilder();
	}

	public static Text content(final String text) {
		return Text.builder().parContent(text).build();
	}

	public static Text styledContent(final String text, final TextStyle ts) {
		return Text.builder().parStyledContent(text, ts).build();
	}

	private final List<Paragraph> paragraphs;

	private final Set<TextStyle> textStyles;

	public Text(final List<Paragraph> paragraphs,
			final Set<TextStyle> textStyles) {
		this.paragraphs = paragraphs;
		this.textStyles = textStyles;
	}

	public void addEmbeddedStylesToContentAutomaticStyles(
			final StylesContainer stylesContainer) {
		for (final TextStyle textStyle : this.textStyles)
			stylesContainer.addStyleToContentAutomaticStyles(textStyle);
	}

	public void addEmbeddedStylesToStylesAutomaticStyles(
			final StylesContainer stylesContainer) {
		for (final TextStyle textStyle : this.textStyles)
			stylesContainer.addStyleToStylesAutomaticStyles(textStyle);
	}

	public void addEmbeddedStylesToStylesAutomaticStyles(
			final StylesContainer stylesContainer, final Mode mode) {
		for (final TextStyle textStyle : this.textStyles)
			stylesContainer.addStyleToStylesAutomaticStyles(textStyle, mode);
	}

	public void appendXMLContent(final XMLUtil util,
			final Appendable appendable) throws IOException {
		for (final Paragraph paragraph : this.paragraphs) {
			if (paragraph == null)
				appendable.append("<text:p/>");
			else {
				paragraph.appendXMLContent(util, appendable);
			}
		}
	}

	public boolean isEmpty() {
		return this.paragraphs.isEmpty();
	}
}
