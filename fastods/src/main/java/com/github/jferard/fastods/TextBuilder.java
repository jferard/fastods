/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A text builder builds a text block: list of paragraphs.
 *
 * @author Julien Férard
 */
public class TextBuilder {
    /**
     * @return an empty text builder
     */
    public static TextBuilder create() {
        return new TextBuilder(new ArrayList<Paragraph>());
    }

    private final List<Paragraph> paragraphs;
    private ParagraphBuilder curParagraphBuilder;

    /**
     * Build a text builder with given styles and paragrapps
     * @param paragraphs the paragraphs
     *
     */
    TextBuilder(final List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
        this.curParagraphBuilder = null;
    }

    /**
     * @return the text
     */
    public Text build() {
        this.flushCurPar();
        return new Text(this.paragraphs);
    }

    /**
     * Create a new paragraph
     * @return this for fluent style
     */
    public TextBuilder par() {
        this.flushCurPar();
        this.curParagraphBuilder = new ParagraphBuilder();
        return this;
    }

    private void flushCurPar() {
        if (this.curParagraphBuilder != null) {
            this.paragraphs.add(this.curParagraphBuilder.build());
        }
    }

    /**
     * Create a new paragraph with a text content
     * @param text the text
     * @return this for fluent style
     */
    public TextBuilder parContent(final String text) {
        return this.par().span(text);
    }

    /**
     * Create a new paragraph with a text content
     * @param text the text
     * @param ts the style
     * @return this for fluent style
     */
    public TextBuilder parStyledContent(final String text, final TextStyle ts) {
        return this.par().styledSpan(text, ts);
    }

    /**
     * Create a span in the current paragraph.
     * @param text the text
     * @return this for fluent style
     */
    public TextBuilder span(final String text) {
        this.curParagraphBuilder.span(text);
        return this;
    }

    /**
     * Create a link in the current paragraph.
     * @param text the text
     * @param table the destination
     * @return this for fluent style
     */
    public TextBuilder link(final String text, final Table table) {
        this.curParagraphBuilder.link(text, table);
        return this;
    }

    /**
     * Create a styled link in the current paragraph.
     * @param text the text
     * @param ts the style
     * @param table the destination
     * @return this for fluent style
     */
    public TextBuilder styledLink(final String text, final TextStyle ts, final Table table) {
        this.curParagraphBuilder.styledLink(text, ts, table);
        return this;
    }

    /**
     * Create a link in the current paragraph.
     * @param text the text
     * @param file the destination
     * @return this for fluent style
     */
    public TextBuilder link(final String text, final File file) {
        this.curParagraphBuilder.link(text, file);
        return this;
    }

    /**
     * Create a styled link in the current paragraph.
     * @param text the text
     * @param ts the style
     * @param file the destination
     * @return this for fluent style
     */
    public TextBuilder styledLink(final String text, final TextStyle ts, final File file) {
        this.curParagraphBuilder.styledLink(text, ts, file);
        return this;
    }

    /**
     * Create a link in the current paragraph.
     * @param text the text
     * @param url the destination
     * @return this for fluent style
     */
    public TextBuilder link(final String text, final URL url) {
        this.curParagraphBuilder.link(text, url);
        return this;
    }

    /**
     * Create a styled link in the current paragraph.
     * @param text the text
     * @param ts the style
     * @param url the destination
     * @return this for fluent style
     */
    public TextBuilder styledLink(final String text, final TextStyle ts, final URL url) {
        this.curParagraphBuilder.styledLink(text, ts, url);
        return this;
    }

    /**
     * Create a link in the current paragraph.
     * @param text the text
     * @param uri the destination
     * @return this for fluent style
     */
    public TextBuilder link(final String text, final URI uri) {
        this.curParagraphBuilder.link(text, uri);
        return this;
    }

    /**
     * Create a styled link in the current paragraph.
     * @param text the text
     * @param ts the style
     * @param uri the destination
     * @return this for fluent style
     */
    public TextBuilder styledLink(final String text, final TextStyle ts, final URI uri) {
        this.curParagraphBuilder.styledLink(text, ts, uri);
        return this;
    }

    /**
     * Create a link in the current paragraph.
     * @param text the text
     * @param ref the destination
     * @return this for fluent style
     */
    public TextBuilder link(final String text, final String ref) {
        this.curParagraphBuilder.link(text, ref);
        return this;
    }

    /**
     * Create a styled link in the current paragraph.
     * @param text the text
     * @param ts the style
     * @param ref the destination
     * @return this for fluent style
     */
    public TextBuilder styledLink(final String text, final TextStyle ts, final String ref) {
        this.curParagraphBuilder.styledLink(text, ts, ref);
        return this;
    }

    /**
     * Adds a TextStyle and text to the footer/header region specified by
     * region.<br>
     * The paragraph to be used is paragraph.<br>
     * The text will be shown in the order it was added with this function.
     *
     * @param text The string with the text
     * @param ts   The text style to be used
     * @return this for fluent style
     */
    public TextBuilder styledSpan(final String text, final TextStyle ts) {
        this.curParagraphBuilder.styledSpan(text, ts);
        return this;
    }
}
