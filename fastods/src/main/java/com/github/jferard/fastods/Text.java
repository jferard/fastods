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

import java.io.IOException;
import java.util.List;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * The Text class represents a text in a footer/header region or the text in a cell.
 * A text is a set of paragraphs (5.1.3 text:p).
 *
 * The destination of embedded styles depends on the location of the text:
 * * if the text is in a cell, the embedded styles will belong to content.xml/automatic-styles
 * * if the text is in a footer/header, this footer/header style will belong to styles.xml/master-styles, and
 *  the embedded styles will go to syles.xml/automatic-styles.
 *
 * @author Julien Férard
 */
public class Text implements ParagraphElement {
    /**
     * 7.3.2<text:date> "The <text:date> element displays a date, by default this is the current date."
     */
    public static final String TEXT_DATE = "<text:date/>";
    /**
     * 7.3.9<text:file-name> "The <text:file-name> element represents a field that displays the name of a file that is being edited."
     */
    public static final String TEXT_FILE_NAME = "<text:file-name/>";
    /**
     * 7.5.18.2<text:page-count>
     */
    public static final String TEXT_PAGE_COUNT = "<text:page-count>99</text:page-count>";
    /**
     * 7.3.4<text:page-number> "The <text:page-number> element displays the current page number."
     */
    public static final String TEXT_PAGE_NUMBER = "<text:page-number>1</text:page-number>";
    /**
     * 7.3.11<text:sheet-name> "The <text:sheet-name> element displays represents the name of a sheet that is currently being edited in a Spreadsheet document."
     */
    public static final String TEXT_SHEET_NAME = "<text:sheet-name/>";
    /**
     * 7.3.3<text:time> "The <text:time> element displays a time, by default this is the current time."
     */
    public static final String TEXT_TIME = "<text:time/>";

    /**
     * @return a new builder
     */
    public static TextBuilder builder() {
		return TextBuilder.create();
	}

    /**
     * Create a simple Text object
     * @param text the text content
     * @return the Text
     */
    public static Text content(final String text) {
		return Text.builder().parContent(text).build();
	}

    /**
     * Create a simple Text object with a style
     * @param text the text content
     * @param ts the style
     * @return the Text
     */
    public static Text styledContent(final String text, final TextStyle ts) {
		return Text.builder().parStyledContent(text, ts).build();
	}

	private final List<Paragraph> paragraphs;

    /**
     * Create a new Text
	 * @param paragraphs the paragraphs
     *
	 */
    Text(final List<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

    /**
     * Add the styles to a container, in content.xml/automatic-syles
     * Use if the text is in a cell
     *
     * @param stylesContainer the container
     */
    @Override
    public void addEmbeddedStylesFromCell(
			final StylesContainer stylesContainer) {
    	for (final Paragraph par : this.paragraphs)
			par.addEmbeddedStylesFromCell(stylesContainer);
	}

    /**
     * Add the styles to a container, in styles.xml/automatic-syles
     * Use if the text is in a footer/header
     *
     * @param stylesContainer the container
     */
    @Override
	public void addEmbeddedStylesFromFooterHeader(
			final StylesContainer stylesContainer) {
	    this.addEmbeddedStylesFromFooterHeader(stylesContainer, Mode.CREATE);
	}

    /**
     * Add the styles to a container, in styles.xml/automatic-syles
     * @param stylesContainer the container
     * @param mode CREATE, UPDATE or CREATE_OR_UPDATE
     */
    @Override
	public void addEmbeddedStylesFromFooterHeader(
			final StylesContainer stylesContainer, final Mode mode) {
        for (final Paragraph par : this.paragraphs)
            par.addEmbeddedStylesFromFooterHeader(stylesContainer, mode);
	}

	@Override
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

    /**
     * @return true if there is no paragraph.
     */
    public boolean isEmpty() {
		return this.paragraphs.isEmpty();
	}
}
