/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.ref.TableRef;
import com.github.jferard.fastods.style.TextStyle;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * A builder for links
 *
 * @author J. Férard
 */
public class LinkBuilder {
    private final String text;
    private TextStyle textStyle;
    private String href;

    /**
     * @param text the text content
     */
    public LinkBuilder(final String text) {
        this.text = text;
    }


    /**
     * @return the link
     */
    public Link build() {
        return new Link(this.text, this.textStyle, this.href);
    }

    /**
     * @param ts the text style
     * @return this for fluent style
     */
    public LinkBuilder style(final TextStyle ts) {
        this.textStyle = ts;
        return this;
    }

    /**
     * @param table the table
     * @return this for fluent style
     */
    public LinkBuilder to(final Table table) {
        this.href = '#' + table.getName();
        return this;
    }

    /**
     * @param tableRef the table ref
     * @return this for fluent style
     */
    public LinkBuilder to(final TableRef tableRef) {
        this.href = tableRef.toString();
        return this;
    }

    /**
     * @param relativeRef the ref
     * @return this for fluent style
     */
    public LinkBuilder to(final String relativeRef) {
        this.href = relativeRef;
        return this;
    }

    /**
     * @param file the file
     * @return this for fluent style
     */
    public LinkBuilder to(final File file) {
        this.href = file.toURI().toString();
        return this;
    }

    /**
     * @param url the url
     * @return this for fluent style
     */
    public LinkBuilder to(final URL url) {
        this.href = url.toString();
        return this;
    }

    /**
     * @param uri the uri
     * @return this for fluent style
     */
    public LinkBuilder to(final URI uri) {
        this.href = uri.toString();
        return this;
    }
}
