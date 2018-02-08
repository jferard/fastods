/*
 * FastODS - a Martin Schulz's SimpleODS fork
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
import com.github.jferard.fastods.util.NamedObject;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 6.1.8 text:a
 * @author Julien Férard
 */
public final class Link implements ParagraphElement {
	private final String text;
	private final String content;
	private final TextStyle ts;

    /**
     * Create a new styled Link to a table
     * @param text the text content
     * @param ts the style
     * @param table the destination
     * @return the link
     */
    public static Link create(final String text, final TextStyle ts, final NamedObject table) {
		return new Link(text, ts, '#'+table.getName());
	}

    /**
     * Create a new Link to a table
     * @param text the text content
     * @param table the destination
     * @return the link
     */
    public static Link create(final String text, final NamedObject table) {
        return new Link(text, null, '#'+table.getName());
    }


    /**
     * Create a new styled link to a given ref
     * @param text the text content
     * @param ts the style
     * @param ref the ref
     * @return the link
     */
	public static Link create(final String text, final TextStyle ts, final String ref) {
        return new Link(text, ts, '#'+ref);
	}

    /**
     * Create a new link to a given ref
     * @param text the text content
     * @param ref the ref
     * @return the link
     */
	public static Link create(final String text, final String ref) {
		return new Link(text, null, '#'+ref);
	}

    /**
     * Create a new styled link to a given file
     * @param text the text content
     * @param ts the style
     * @param file the file
     * @return the link
     */
	public static Link create(final String text, final TextStyle ts, final File file) {
		return new Link(text, ts, file.toURI().toString());
	}

    /**
     * Create a new link to a given file
     * @param text the text content
     * @param file the file
     * @return the link
     */
	public static Link create(final String text, final File file) {
        return new Link(text, null, file.toURI().toString());
	}

    /**
     * Create a new styled link to a given url
     * @param text the text content
     * @param ts the style
     * @param url the file
     * @return the link
     */
	public static Link create(final String text, final TextStyle ts, final URL url) {
		return new Link(text, ts, url.toString());
	}

    /**
     * Create a new link to a given url
     * @param text the text content
     * @param url the file
     * @return the link
     */
	public static Link create(final String text, final URL url) {
        return new Link(text, null, url.toString());
	}

    private Link(final String text, final TextStyle ts, final String content) {
        this.text = text;
        this.content = content;
        this.ts = ts;
    }

    @Override
	public void appendXMLToParagraph(final XMLUtil util, final Appendable appendable) throws IOException {
		appendable.append("<text:a");
		if (this.ts != null) {
			util.appendEAttribute(appendable, "text:style-name",
					this.ts.getName());
		}
		util.appendEAttribute(appendable, "xlink:href", this.content);
		util.appendAttribute(appendable, "xlink:type", "simple");
		appendable.append(">").append(this.text).append("</text:a>");
	}

	@Override
	public TextStyle getTextStyle() {
		return this.ts;
	}

	@Override
	public String getText() {
		return this.text;
	}
}
