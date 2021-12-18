/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.ref.TableRef;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * 6.1.8 text:a
 * 19.910 xlink:href
 * <p>
 * See: https://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#anyURI and https://www.ietf
 * .org/rfc/rfc2396.txt
 * <p>
 * Represents a link to a table, an URI, an URL or a custom ref.
 *
 * @author Julien Férard
 */
public final class Link implements ParagraphElement {
    /**
     * @param text the text content
     * @return the link builder
     */
    public static LinkBuilder builder(final String text) {
        return new LinkBuilder(text);
    }

    /**
     * Create a new styled Link to a table
     *
     * @param text  the text content
     * @param ts    the style
     * @param table the destination
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final TextStyle ts, final Table table) {
        return Link.builder(text).style(ts).to(table).build();
    }

    /**
     * Create a new Link to a table
     *
     * @param text  the text content
     * @param table the destination
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final Table table) {
        return Link.builder(text).to(table).build();
    }

    /**
     * Create a new styled link to a given ref
     *
     * @param text        the text content
     * @param ts          the style
     * @param relativeRef the ref
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final TextStyle ts, final String relativeRef) {
        return Link.builder(text).style(ts).to(relativeRef).build();
    }

    /**
     * Create a new link to a given ref
     *
     * @param text        the text content
     * @param relativeRef the ref
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final String relativeRef) {
        return Link.builder(text).to(relativeRef).build();
    }

    /**
     * Create a new styled link to a given file
     *
     * @param text the text content
     * @param ts   the style
     * @param file the file
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final TextStyle ts, final File file) {
        return Link.builder(text).style(ts).to(file).build();
    }

    /**
     * Create a new link to a given file
     *
     * @param text the text content
     * @param file the file
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final File file) {
        return Link.builder(text).to(file).build();
    }

    /**
     * Create a new styled link to a given url
     *
     * @param text the text content
     * @param ts   the style
     * @param url  the url
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final TextStyle ts, final URL url) {
        return Link.builder(text).style(ts).to(url).build();
    }

    /**
     * Create a new link to a given url
     *
     * @param text the text content
     * @param url  the url
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final URL url) {
        return Link.builder(text).to(url).build();
    }

    /**
     * Create a new styled link to a given uri
     *
     * @param text the text content
     * @param ts   the style
     * @param uri  the uri
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final TextStyle ts, final URI uri) {
        return Link.builder(text).style(ts).to(uri).build();
    }

    /**
     * Create a new link to a given url
     *
     * @param text the text content
     * @param uri  the file
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final URI uri) {
        return new Link(text, null, uri.toString());
    }

    /**
     * Create a new link to a given table ref
     *
     * @param text     the text content
     * @param tableRef the table ref
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final TableRef tableRef) {
        return Link.builder(text).to(tableRef).build();
    }

    /**
     * Create a new styled link to a given uri
     *
     * @param text     the text content
     * @param ts       the style
     * @param tableRef the table ref
     * @return the link
     */
    @Deprecated
    public static Link create(final String text, final TextStyle ts, final TableRef tableRef) {
        return Link.builder(text).style(ts).to(tableRef).build();
    }

    private final String text;
    private final String href;
    private final TextStyle ts;

    /**
     * @param text the text
     * @param ts   the style or null
     * @param href the ref
     */
    Link(final String text, final TextStyle ts, final String href) {
        this.text = text;
        this.href = href;
        this.ts = ts;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<text:a");
        if (this.ts != null) {
            util.appendEAttribute(appendable, "text:style-name", this.ts.getName());
        }
        util.appendEAttribute(appendable, "xlink:href", this.href);
        util.appendAttribute(appendable, "xlink:type", "simple");
        appendable.append(">").append(this.text).append("</text:a>");
    }

    @Override
    public void addEmbeddedStylesFromFooterHeader(final StylesContainer stylesContainer) {
        if (this.ts != null) {
            stylesContainer.addStylesFontFaceContainerStyle(this.ts);
        }
    }

    @Override
    public void addEmbeddedStylesFromCell(final StylesContainer stylesContainer) {
        if (this.ts != null) {
            stylesContainer.addContentFontFaceContainerStyle(this.ts);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Link)) {
            return false;
        }

        final Link other = (Link) o;
        return this.href.equals(other.href) && this.text.equals(other.text);
    }

    @Override
    public final int hashCode() {
        return 31 * this.href.hashCode() + this.text.hashCode();
    }
}
